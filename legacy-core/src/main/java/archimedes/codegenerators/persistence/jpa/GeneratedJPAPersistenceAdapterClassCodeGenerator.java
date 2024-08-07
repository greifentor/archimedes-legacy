package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.FindByUtils;
import archimedes.codegenerators.GlobalIdOptionChecker;
import archimedes.codegenerators.GlobalIdType;
import archimedes.codegenerators.ListAccess.ListAccessConverterData;
import archimedes.codegenerators.NoKeyValueFinder;
import archimedes.codegenerators.NullableUtils;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.Subclasses.SubclassData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceInterfaceCodeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for JPA persistence adapters.
 *
 * @author ollie (28.06.2021)
 */
public class GeneratedJPAPersistenceAdapterClassCodeGenerator
		extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	private static final ServiceNameGenerator SERVICE_NAME_GENERATOR = ServiceNameGenerator.INSTANCE;
	private static final NoKeyValueFinder NO_KEY_VALUE_FINDER = new NoKeyValueFinder();

	public GeneratedJPAPersistenceAdapterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"GeneratedJPAPersistenceAdapterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				PersistenceJPANameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		ReferenceMode referenceMode = getReferenceMode(model, table);
		List<ColumnData> columnData = getColumnData(table.getColumns(), model, getReferenceMode(model, table));
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("DBOClassName", nameGenerator.getDBOClassName(table));
		context.put("DBOPackageName", nameGenerator.getDBOPackageName(model, table));
		context.put("DBOConverterClassName", nameGenerator.getDBOConverterClassName(table.getName(), model));
		context.put("DBOConverterPackageName", nameGenerator.getDBOConverterPackageName(model, table));
		context
				.put(
						"FindBys",
						FindByUtils
								.getFindBys(
										table.getColumns(),
										referenceMode,
										nameGenerator,
										ServiceNameGenerator.INSTANCE::getModelClassName,
										t -> ServiceNameGenerator.INSTANCE.getModelPackageName(model, t),
										t -> nameGenerator.getDBOConverterClassName(t.getName(), model),
										t -> nameGenerator.getDBOConverterPackageName(model, t),
										typeGenerator,
										(c, m) -> ServiceNameGenerator.INSTANCE.getModelClassName(c.getDomain(), m),
										d -> ServiceNameGenerator.INSTANCE.getModelPackageName(model, d)));
		context.put("HasNotBlanks", FindByUtils.hasNotBlanks(table.getColumns()));
		context.put("HasNotNulls", FindByUtils.hasNotNulls(table.getColumns()));
		context.put("HasNoUniques", FindByUtils.hasNoUniques(table.getColumns()));
		context
				.put(
						"HasValidation",
						FindByUtils.hasNotBlanks(table.getColumns()) || FindByUtils.hasNotNulls(table.getColumns())
								|| FindByUtils.hasNoUniques(table.getColumns()));
		context
				.put(
						"HasObjectReferences",
						FindByUtils.hasObjectReferences(table.getColumns()) && (referenceMode == ReferenceMode.OBJECT));
		context.put("HasSimpleTypeId", hasSimpleTypeId(columnData));
		context.put("IdClassName", getIdClassName(table));
		context.put("IdFieldName", nameGenerator.getAttributeName(getIdFieldNameCamelCase(table)));
		context.put("IdFieldNameCamelCase", getIdFieldNameCamelCase(table));
		context.put("IdFieldIsEnum", getIdFieldIsEnum(table));
		context.put("JPARepositoryClassName", nameGenerator.getJPARepositoryInterfaceName(table));
		context.put("JPARepositoryPackageName", nameGenerator.getJPARepositoryPackageName(model, table));
		context.put("ModelClassName", ServiceNameGenerator.INSTANCE.getModelClassName(table));
		context.put("ModelPackageName", ServiceNameGenerator.INSTANCE.getModelPackageName(model, table));
		context.put("ExceptionsPackageName", ServiceNameGenerator.INSTANCE.getExceptionsPackageName(model));
		context
				.put(
						"ListAccess",
						getListAccesses(
								model,
								table,
								c -> ServiceNameGenerator.INSTANCE.getModelClassName(c.getReferencedTable()),
								(c, m) -> ServiceNameGenerator.INSTANCE.getModelClassName(c.getDomain(), model),
								c -> ServiceNameGenerator.INSTANCE.getModelPackageName(model, table) + "."
										+ ServiceNameGenerator.INSTANCE.getModelClassName(c.getReferencedTable()),
								(c, m) -> ServiceNameGenerator.INSTANCE.getModelPackageName(model, table) + "."
										+ ServiceNameGenerator.INSTANCE.getModelClassName(c.getDomain(), model),
								c -> new ListAccessConverterData()
										.setAttributeName(
												nameGenerator
														.getAttributeName(
																nameGenerator
																		.getDBOConverterClassName(
																				c.getReferencedTable().getName(),
																				model)))
										.setClassName(
												nameGenerator
														.getDBOConverterClassName(
																c.getReferencedTable().getName(),
																model))
										.setPackageName(
												nameGenerator
														.getDBOConverterPackageName(model, c.getReferencedTable()))));
		context.put("NoKeyValue", NO_KEY_VALUE_FINDER.find(table.getPrimaryKeyColumns()));
		context.put("PackageName", getPackageName(model, table));
		context.put("PageClassName", ServiceNameGenerator.INSTANCE.getPageClassName());
		context.put("PageConverterClassName", nameGenerator.getPageConverterClassName());
		context.put("PageConverterPackageName", nameGenerator.getPageConverterPackageName(model));
		context.put("PagePackageName", ServiceNameGenerator.INSTANCE.getPagePackageName(model));
		context.put("PageParametersClassName", ServiceNameGenerator.INSTANCE.getPageParametersClassName());
		context
				.put(
						"PageParametersToPageableConverterClassName",
						nameGenerator.getPageParametersToPageableConverterClassName());
		context
				.put(
						"PageParametersToPageableConverterPackageName",
						nameGenerator.getPageParametersToPageableConverterPackageName(model));
		context
				.put(
						"PersistencePortInterfaceName",
						ServiceNameGenerator.INSTANCE.getPersistencePortInterfaceName(table));
		context
				.put(
						"PersistencePortPackageName",
						ServiceNameGenerator.INSTANCE.getPersistencePortPackageName(model, table));
		context.put("Subclasses", getSubclassData(model, table));
		context
				.put(
						"SubclassSelectors",
						table.getOptionByName(ServiceInterfaceCodeGenerator.SUPPRESS_SUBCLASS_SELECTORS) == null);
		context.put("TableName", table.getName());
		context.put("TableAttributeName", nameGenerator.getAttributeName(table.getName()));
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(table));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(table));
		context.put("UtilPackageName", ServiceNameGenerator.INSTANCE.getUtilPackageName(model));
		if (GlobalIdOptionChecker.INSTANCE.hasGlobalIdTypeConfiguration(GlobalIdType.UUID, table)) {
			importDeclarations.add("java.util", "UUID");
		}
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getGeneratedJPAPersistenceAdapterClassName(table);
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns, DataModel model, ReferenceMode referenceMode) {
		return Arrays
				.asList(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setColumnName(nameGenerator.getCamelCase(column.getName()))
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(getType(column, referenceMode))
								.setGetterCall(getGetterCall(column, model, referenceMode))
								.setNotBlank(column.findOptionByName(AbstractClassCodeFactory.NOT_BLANK).isPresent())
								.setNotNull(column.isNotNull())
								.setPkMember(column.isPrimaryKey())
								.setSimpleType(isSimpleType(getType(column, referenceMode)))
								.setUnique(column.isUnique()))
				.collect(Collectors.toList());
	}

	private String getType(ColumnModel column, ReferenceMode referenceMode) {
		if ((column.getReferencedColumn() != null) && (referenceMode == ReferenceMode.OBJECT)) {
			return ServiceNameGenerator.INSTANCE.getModelClassName(column.getReferencedTable());
		}
		return typeGenerator.getJavaTypeString(column.getDomain(), NullableUtils.isNullable(column));
	}

	private boolean isSimpleType(String typeName) {
		return Set.of("boolean", "byte", "char", "double", "float", "int", "long", "short").contains(typeName);
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

	private List<SubclassData> getSubclassData(DataModel model, TableModel table) {
		return getSubclassTables(table)
				.stream()
				.map(
						subclassTable -> new SubclassData()
								.setConverterAttributeName(
										nameGenerator
												.getAttributeName(
														nameGenerator
																.getDBOConverterClassName(
																		subclassTable.getName(),
																		model)))
								.setConverterClassName(
										nameGenerator.getDBOConverterClassName(subclassTable.getName(), model))
								.setConverterClassNameQualified(
										nameGenerator.getDBOConverterPackageName(model, subclassTable) + "."
												+ nameGenerator
														.getDBOConverterClassName(subclassTable.getName(), model))
								.setDboRepositoryAttributeName(
										nameGenerator
												.getAttributeName(
														nameGenerator.getJPARepositoryInterfaceName(subclassTable)))
								.setDboRepositoryClassName(nameGenerator.getJPARepositoryInterfaceName(subclassTable))
								.setDboRepositoryClassNameQualified(
										nameGenerator.getJPARepositoryPackageName(model, subclassTable) + "."
												+ nameGenerator.getJPARepositoryInterfaceName(subclassTable))
								.setConverterClassName(
										nameGenerator.getDBOConverterClassName(subclassTable.getName(), model))
								.setConverterClassNameQualified(
										nameGenerator.getDBOConverterPackageName(model, subclassTable) + "."
												+ nameGenerator
														.getDBOConverterClassName(subclassTable.getName(), model))
								.setModelClassName(SERVICE_NAME_GENERATOR.getModelClassName(subclassTable))
								.setModelClassNameQualified(
										SERVICE_NAME_GENERATOR.getModelPackageName(model, subclassTable) + "."
												+ SERVICE_NAME_GENERATOR.getModelClassName(subclassTable)))
				.collect(Collectors.toList());
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getJPAPersistenceAdapterPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {

		return hasDependentAttribute(model, table) || isSubclass(table) || isAMember(table);
	}

	private boolean hasDependentAttribute(DataModel model, TableModel table) {
		return Arrays
				.asList(table.getColumns())
				.stream()
				.anyMatch(
						column -> OptionGetter
								.getOptionByName(
										column,
										JPAPersistenceAdapterDependentClassCodeGenerator.DEPENDENT_ATTRIBUTE)
								.isPresent());
	}

}