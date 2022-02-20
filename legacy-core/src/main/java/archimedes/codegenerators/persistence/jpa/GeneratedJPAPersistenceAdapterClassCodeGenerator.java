package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.FindByUtils;
import archimedes.codegenerators.NullableUtils;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.ReferenceMode;
import archimedes.codegenerators.TypeGenerator;
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

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public GeneratedJPAPersistenceAdapterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"GeneratedJPAPersistenceAdapterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
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
		context.put("DBOConverterClassName", nameGenerator.getDBOConverterClassName(table));
		context.put("DBOConverterPackageName", nameGenerator.getDBOConverterPackageName(model, table));
		context
				.put(
						"FindBys",
						FindByUtils
								.getFindBys(
										table.getColumns(),
										referenceMode,
										nameGenerator,
										serviceNameGenerator::getModelClassName,
										t -> serviceNameGenerator.getModelPackageName(model, t),
										nameGenerator::getDBOConverterClassName,
										t -> nameGenerator.getDBOConverterPackageName(model, t),
										typeGenerator));
		context.put("HasUniques", FindByUtils.hasUniques(table.getColumns()));
		context.put("HasNotNulls", FindByUtils.hasNotNulls(table.getColumns()));
		context.put("HasNoUniques", FindByUtils.hasNoUniques(table.getColumns()));
		context
				.put(
						"HasObjectReferences",
						FindByUtils.hasObjectReferences(table.getColumns()) && (referenceMode == ReferenceMode.OBJECT));
		context.put("IdClassName", getIdClassName(table));
		context.put("IdFieldName", nameGenerator.getAttributeName(getIdFieldNameCamelCase(table)));
		context.put("IdFieldNameCamelCase", getIdFieldNameCamelCase(table));
		context.put("JPARepositoryClassName", nameGenerator.getJPARepositoryInterfaceName(table));
		context.put("JPARepositoryPackageName", nameGenerator.getJPARepositoryPackageName(model, table));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("ExceptionsPackageName", serviceNameGenerator.getExceptionsPackageName(model, table));
		context.put("NoKeyValue", getNoKeyValue(table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PageClassName", serviceNameGenerator.getPageClassName());
		context.put("PageConverterClassName", nameGenerator.getPageConverterClassName(table));
		context.put("PageConverterPackageName", nameGenerator.getPageConverterPackageName(model, table));
		context.put("PagePackageName", serviceNameGenerator.getPagePackageName(model, table));
		context.put("PageParametersClassName", serviceNameGenerator.getPageParametersClassName());
		context
				.put(
						"PageParametersToPageableConverterClassName",
						nameGenerator.getPageParametersToPageableConverterClassName(table));
		context
				.put(
						"PageParametersToPageableConverterPackageName",
						nameGenerator.getPageParametersToPageableConverterPackageName(model, table));
		context.put("PersistencePortInterfaceName", serviceNameGenerator.getPersistencePortInterfaceName(table));
		context.put("PersistencePortPackageName", serviceNameGenerator.getPersistencePortPackageName(model, table));
		context.put("TableName", table.getName());
		context.put("TableAttributeName", nameGenerator.getAttributeName(table.getName()));
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(table));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(table));
		context.put("UtilPackageName", serviceNameGenerator.getUtilPackageName(model, table));
	}

	private String getNoKeyValue(TableModel table) {
		ColumnModel[] pks = table.getPrimaryKeyColumns();
		if (pks.length == 0) {
			return "NO_KEY_FOUND";
		}
		return pks[0].isNotNull() ? "-1" : "null";
	}

	@Override
	public String getClassName(TableModel table) {
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
								.setNotNull(column.isNotNull())
								.setPkMember(column.isPrimaryKey())
								.setSimpleType(isSimpleType(getType(column, referenceMode)))
								.setUnique(column.isUnique()))
				.collect(Collectors.toList());
	}

	private String getType(ColumnModel column, ReferenceMode referenceMode) {
		if ((column.getReferencedColumn() != null) && (referenceMode == ReferenceMode.OBJECT)) {
			return serviceNameGenerator.getModelClassName(column.getReferencedTable());
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