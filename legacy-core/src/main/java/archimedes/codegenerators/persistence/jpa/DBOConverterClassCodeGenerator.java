package archimedes.codegenerators.persistence.jpa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.Converters.ConverterData;
import archimedes.codegenerators.GlobalIdOptionChecker;
import archimedes.codegenerators.GlobalIdType;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.Subclasses.SubclassData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for DBO converters.
 *
 * @author ollie (28.06.2021)
 */
public class DBOConverterClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	private static final ServiceNameGenerator SERVICE_NAME_GENERATOR = ServiceNameGenerator.INSTANCE;

	public DBOConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DBOConverterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				PersistenceJPANameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		ReferenceMode referenceMode = getReferenceMode(model, table);
		List<ColumnData> columnData = getColumnData(table.getColumns(), table, model, referenceMode);
		columnData.addAll(getInheritedColumns(table, model, referenceMode));
		List<SubclassData> subClasses = getSubclassData(model, table);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context
				.put(
						"ConverterData",
						getConverterData(
								getColumnsIncludingInherited(table).toArray(new ColumnModel[0]),
								table,
								model,
								referenceMode, columnData));
		context.put("DBOClassName", nameGenerator.getDBOClassName(table));
		context
				.put(
						"DBOClassNameQualified",
						getQualifiedName(
								nameGenerator.getDBOPackageName(model, table),
								nameGenerator.getDBOClassName(table)));
		context.put("DBOSuperclassName", getSuperclassName(table, nameGenerator::getDBOClassName));
		context
				.put(
						"DBOSuperclassNameQualified",
						getSuperclassName(
								table,
								t -> nameGenerator.getDBOPackageName(model, t) + "."
										+ nameGenerator.getDBOClassName(t)));
		context.put("GenerateIdClass", isGenerateIdClass(model, table));
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		context.put("HasEnums", hasEnums(getColumnsIncludingInherited(table)));
		context.put("HasMemberLists", hasMemberLists(table));
		context.put("HasSubclasses", !subClasses.isEmpty());
		context.put("HasReferences", hasReferences(table, model, referenceMode));
		context.put("ModelClassName", SERVICE_NAME_GENERATOR.getModelClassName(table));
		context
				.put(
						"ModelClassNameQualified",
						getQualifiedName(
								SERVICE_NAME_GENERATOR.getModelPackageName(model, table),
								SERVICE_NAME_GENERATOR.getModelClassName(table)));
		context.put("ModelSuperclassName", getSuperclassName(table, SERVICE_NAME_GENERATOR::getModelClassName));
		context
				.put(
						"ModelSuperclassNameQualified",
						getSuperclassName(
								table,
								t -> SERVICE_NAME_GENERATOR.getModelPackageName(model, t) + "."
										+ SERVICE_NAME_GENERATOR.getModelClassName(t)));
		context.put("PackageName", getPackageName(model, table));
		context.put("ReferenceMode", referenceMode);
		context.put("Subclasses", subClasses);
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(model));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(model));
		if (hasGlobalIdTypeConfiguration(GlobalIdType.UUID, table)) {
			importDeclarations.add("java.util", "UUID");
		}
	}

	private boolean hasGlobalIdTypeConfiguration(GlobalIdType globalIdType, TableModel table) {
		boolean b = GlobalIdOptionChecker.INSTANCE.hasGlobalIdTypeConfiguration(GlobalIdType.UUID, table);
		while (getSuperclassTable(table) != null) {
			table = getSuperclassTable(table);
			b = b || GlobalIdOptionChecker.INSTANCE.hasGlobalIdTypeConfiguration(GlobalIdType.UUID, table);
		}
		return b;
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns, TableModel table, DataModel model,
			ReferenceMode referenceMode) {
		List<ColumnData> l =
				List
						.of(columns)
						.stream()
						.filter(column -> !isAMember(table) || !isColumnReferencingAParent(column))
						.map(
								column -> new ColumnData()
										.setConverterAttributeName(getDBOConverterAttributeName(column, model))
										.setEnumType(isEnum(column))
										.setFieldName(nameGenerator.getAttributeName(column))
										.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false))
										.setGetterCall(getGetterCall(column, model, referenceMode))
										.setPkMember(column.isPrimaryKey())
										.setReference(column.getReferencedColumn() != null)
										.setSetterName(getSetterName(column))
										.setUuid(isUuid(column)))
						.collect(Collectors.toList());
		getCompositionLists(table).forEach(cld -> {
			l
					.add(
							new ColumnData()
									.setColumnName(cld.getMemberTable().getName())
									.setFieldType("LIST")
									.setFieldName(nameGenerator.getAttributeName(cld.getMemberTable().getName()) + "s")
									.setGetterCall(
											getGetterCall(
													nameGenerator.getClassName(cld.getMemberTable().getName()) + "s"))
									.setSetterName(
											getSetterName(
													nameGenerator.getClassName(cld.getMemberTable().getName()) + "s"))
									.setConverterAttributeName(
											nameGenerator
													.getAttributeName(
															nameGenerator
																	.getDBOConverterClassName(
																			cld.getMemberTable().getName(),
																			model))));
		});
		return l;
	}

	private boolean isUuid(ColumnModel column) {
		return GlobalIdOptionChecker.INSTANCE.getGlobalIdType(column) == GlobalIdType.UUID;
	}

	private String getDBOConverterAttributeName(ColumnModel column, DataModel model) {
		return (column.getReferencedColumn() != null)
				? nameGenerator.getAttributeName(getClassName(column.getReferencedTable()))
				: getEnumAttributeName(column, model);
	}

	private String getEnumAttributeName(ColumnModel column, DataModel model) {
		return isEnum(column)
				? nameGenerator
						.getAttributeName(nameGenerator.getDBOConverterClassName(column.getDomain().getName(), model))
				: "UNKNOWN";
	}

	private String getGetterCall(ColumnModel column, DataModel model, ReferenceMode referenceMode) {
		String getterName = super.getGetterName(column);
		VelocityContext context = new VelocityContext();
		context.put("GetterName", getterName);
		context.put("ReferenceMode", referenceMode.name());
		if (isGenerateIdClass(model, column.getTable())) {
			context.put("KeyFromIdClass", column.isPrimaryKey());
		} else {
			context.put("KeyFromIdClass", "false");
		}
		System.out.println();
		return processTemplate(context, "DBOKeyGetter.vm");
	}

	public String getGetterCall(String columnName) {
		String getterName = super.getGetterName(columnName);
		VelocityContext context = new VelocityContext();
		context.put("GetterName", getterName);
		context.put("ReferenceMode", "OBJECT");
		context.put("KeyFromIdClass", "false");
		return processTemplate(context, "DBOKeyGetter.vm");
	}

	private List<ConverterData> getConverterData(ColumnModel[] columns, TableModel table, DataModel model,
			ReferenceMode referenceMode, List<ColumnData> columnData) {
		if ((referenceMode != ReferenceMode.OBJECT) && !hasEnums(columns)) {
			return List.of();
		}
		List<ConverterData> l =
				List
						.of(columns)
						.stream()
						.filter(
								column -> !(column.isPrimaryKey()
										&& column.getTable().isOptionSet(AbstractClassCodeGenerator.SUBCLASS)))
						.filter(
								column -> ((column.getReferencedColumn() != null) || isEnum(column))
										&& !(isAMember(table) && isAParent(column.getReferencedTable())))
						.map(column -> toConverterData(column, model))
						.sorted((cd0, cd1) -> cd0.getClassName().compareTo(cd1.getClassName()))
						.collect(Collectors.toSet())
						.stream()
						.collect(Collectors.toList());
		table.ifOptionSetWithValueDo(MEMBER_LIST, "PARENT", om -> {
			getReferencingColumns(table, table.getDataModel())
					.stream()
					.filter(
							cm -> OptionGetter
									.getParameterOfOptionByName(cm.getTable(), MEMBER_LIST)
									.filter(s -> s.toUpperCase().equals("MEMBER"))
									.isPresent())
					.forEach(cm -> {
						String cn = nameGenerator.getDBOConverterClassName(cm.getTable().getName(), model);
						l
								.add(
										new ConverterData()
												.setAttributeName(nameGenerator.getAttributeName(cn))
												.setClassName(cn).setColumnName(cm.getName()));
					});
		});
		addMissingConverterData(l, columnData);
		return cleanUpDoubles(l);
	}

	private ConverterData toConverterData(ColumnModel column, DataModel model) {
		String dboConverterClassName =
				isEnum(column)
						? nameGenerator.getDBOConverterClassName(column.getDomain().getName(), model)
						: getClassName(column.getReferencedTable());
		return new ConverterData()
				.setAttributeName(nameGenerator.getAttributeName(dboConverterClassName))
				.setClassName(dboConverterClassName).setColumnName(column.getName());
	}

	private List<ColumnData> getInheritedColumns(TableModel table, DataModel model, ReferenceMode referenceMode) {
		List<ColumnData> l = new ArrayList<>();
		if (table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)) {
			addInheritedColumns(l, getDirectSuperclassTable(table), model, referenceMode);
		}
		return l;
	}

	private void addInheritedColumns(List<ColumnData> columnData, TableModel table, DataModel model,
			ReferenceMode referenceMode) {
		if (table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)) {
			addInheritedColumns(columnData, getDirectSuperclassTable(table), model, referenceMode);
		}
		List
				.of(table.getColumns())
				.stream()
				.filter(column -> !column.isPrimaryKey())
				.forEach(
						column -> columnData
								.add(
										new ColumnData()
												.setConverterAttributeName(getDBOConverterAttributeName(column, model))
												.setEnumType(isEnum(column))
												.setFieldName(nameGenerator.getAttributeName(column))
												.setFieldType(
														typeGenerator.getJavaTypeString(column.getDomain(), false))
												.setGetterCall(getGetterCall(column, model, referenceMode))
												.setPkMember(column.isPrimaryKey())
												.setReference(column.getReferencedColumn() != null)
												.setSetterName(getSetterName(column))
												.setUuid(isUuid(column))));
	}

	private List<SubclassData> getSubclassData(DataModel model, TableModel table) {
		if (!table.isOptionSet(AbstractClassCodeGenerator.SUPERCLASS)) {
			return List.of();
		}
		Set<TableModel> subclassTables =
				List
						.of(model.getAllColumns())
						.stream()
						.filter(column -> column.getTable().isOptionSet(AbstractClassCodeGenerator.SUBCLASS))
						.filter(column -> column.isPrimaryKey() && (column.getReferencedTable() == table))
						.map(ColumnModel::getTable)
						.collect(Collectors.toSet());
		return subclassTables
				.stream()
				.sorted((t0, t1) -> t0.getName().compareTo(t1.getName()))
				.map(
						t -> new SubclassData()
								.setConverterAttributeName(nameGenerator.getAttributeName(getClassName(t)))
								.setConverterClassName(getClassName(t))
								.setDboClassName(nameGenerator.getDBOClassName(t))
								.setDboClassNameQualified(
										nameGenerator.getDBOPackageName(model, t) + "."
												+ nameGenerator.getDBOClassName(t))
								.setModelClassName(SERVICE_NAME_GENERATOR.getModelClassName(t))
								.setModelClassNameQualified(
										SERVICE_NAME_GENERATOR.getModelPackageName(model, t) + "."
												+ SERVICE_NAME_GENERATOR.getModelClassName(t)))
				.collect(Collectors.toList());
	}

	private boolean hasReferences(TableModel table, DataModel model, ReferenceMode referenceMode) {
		return (!table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS) && hasReferences(table.getColumns()))
				|| hasANonPrimarkeyReference(table, model, referenceMode);
	}

	private boolean hasANonPrimarkeyReference(TableModel table, DataModel model, ReferenceMode referenceMode) {
		return getInheritedColumns(table, model, referenceMode)
				.stream()
				.anyMatch(column -> !column.isPkMember() && column.isReference());
	}

	private List<ColumnModel> getColumnsIncludingInherited(TableModel table) {
		List<ColumnModel> columns = new ArrayList<>(List.of(table.getColumns()));
		TableModel superclassTable = getDirectSuperclassTable(table);
		if (superclassTable != null) {
			columns.addAll(getColumnsIncludingInherited(superclassTable));
		}
		return columns;
	}

	private void addMissingConverterData(List<ConverterData> l, List<ColumnData> columnData) {
		columnData.stream().filter(cd -> cd.getConverterAttributeName() != null).forEach(cd -> {
			if (findByName(l, cd.getConverterAttributeName()).isEmpty() && "LIST".equals(cd.getFieldType())) {
				l.add(new ConverterData()
						.setAttributeName(nameGenerator.getAttributeName(cd.getConverterAttributeName()))
						.setClassName(nameGenerator.getClassName(cd.getConverterAttributeName()))
						.setColumnName(cd.getColumnName()));
			}
		});
	}

	private Optional<ConverterData> findByName(List<ConverterData> l, String converterAttributeName) {
		return l.stream().filter(cd -> cd.getAttributeName().equals(converterAttributeName)).findFirst();
	}

	private List<ConverterData> cleanUpDoubles(List<ConverterData> l) {
		Map<String, ConverterData> m = new HashMap<>();
		l.forEach(cd -> m.put(cd.getAttributeName(), cd));
		return m.entrySet().stream().map(Entry::getValue)
				.sorted((cd0, cd1) -> cd0.getAttributeName().compareTo(cd1.getAttributeName()))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getDBOConverterClassName(table.getName(), model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDBOConverterPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		return OptionGetter.getOptionByName(model, AbstractClassCodeGenerator.MAPPERS).isPresent();
	}

}