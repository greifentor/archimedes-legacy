package archimedes.codegenerators.persistence.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.Converters.ConverterData;
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
		List<ColumnData> columnData = getColumnData(table.getColumns(), model, referenceMode);
		columnData.addAll(getInheritedColumns(table, model, referenceMode));
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context
				.put(
						"ConverterData",
						getConverterData(
								getColumnsIncludingInherited(table).toArray(new ColumnModel[0]),
								model,
								referenceMode));
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
		context.put("Subclasses", getSubclassData(model, table));
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(model));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(model));
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns, DataModel model, ReferenceMode referenceMode) {
		return List
				.of(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setConverterAttributeName(getDBOConverterAttributeName(column, model))
								.setEnumType(isEnum(column))
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false))
								.setGetterCall(getGetterCall(column, model, referenceMode))
								.setPkMember(column.isPrimaryKey())
								.setReference(column.getReferencedColumn() != null)
								.setSetterName(getSetterName(column)))
				.collect(Collectors.toList());
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
		return processTemplate(context, "DBOKeyGetter.vm");
	}

	private List<ConverterData> getConverterData(ColumnModel[] columns, DataModel model, ReferenceMode referenceMode) {
		if ((referenceMode != ReferenceMode.OBJECT) && !hasEnums(columns)) {
			return List.of();
		}
		return List
				.of(columns)
				.stream()
				.filter(
						column -> !(column.isPrimaryKey()
								&& column.getTable().isOptionSet(AbstractClassCodeGenerator.SUBCLASS)))
				.filter(column -> (column.getReferencedColumn() != null) || isEnum(column))
				.map(column -> toConverterData(column, model))
				.sorted((cd0, cd1) -> cd0.getClassName().compareTo(cd1.getClassName()))
				.collect(Collectors.toSet())
				.stream()
				.collect(Collectors.toList());
	}

	private ConverterData toConverterData(ColumnModel column, DataModel model) {
		String dboConverterClassName =
				isEnum(column)
						? nameGenerator.getDBOConverterClassName(column.getDomain().getName(), model)
						: getClassName(column.getReferencedTable());
		return new ConverterData()
				.setAttributeName(nameGenerator.getAttributeName(dboConverterClassName))
				.setClassName(dboConverterClassName);
	}

	private List<ColumnData> getInheritedColumns(TableModel table, DataModel model, ReferenceMode referenceMode) {
		if (!table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)) {
			return List.of();
		}
		TableModel superclassTable = getSuperclassTable(table);
		return superclassTable == null
				? List.of()
				: List
						.of(superclassTable.getColumns())
						.stream()
						.filter(column -> !column.isPrimaryKey())
						.map(
								column -> new ColumnData()
										.setConverterAttributeName(getDBOConverterAttributeName(column, model))
										.setEnumType(isEnum(column))
										.setFieldName(nameGenerator.getAttributeName(column))
										.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false))
										.setGetterCall(getGetterCall(column, model, referenceMode))
										.setPkMember(column.isPrimaryKey())
										.setReference(column.getReferencedColumn() != null)
										.setSetterName(getSetterName(column)))
						.collect(Collectors.toList());
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
		return (!table.isOptionSet(AbstractClassCodeGenerator.SUBCLASS)
				&& hasReferences(table.getColumns())) || hasANonPrimarkeyReference(table, model, referenceMode);
	}

	private boolean hasANonPrimarkeyReference(TableModel table, DataModel model, ReferenceMode referenceMode) {
		return getInheritedColumns(table, model, referenceMode)
				.stream()
				.anyMatch(column -> !column.isPkMember() && column.isReference());
	}

	private List<ColumnModel> getColumnsIncludingInherited(TableModel table) {
		List<ColumnModel> columns = new ArrayList<>(List.of(table.getColumns()));
		TableModel superclassTable = getSuperclassTable(table);
		if (superclassTable != null) {
			columns.addAll(getColumnsIncludingInherited(superclassTable));
		}
		return columns;
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