package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.TableUtil;
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
public class JPAPersistenceAdapterClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public JPAPersistenceAdapterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"JPAPersistenceAdapterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		if (TableUtil.hasCompositeKey(table)) {
			context.put("ImportIdClassName", getIdClassName(table));
		}
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("DBOClassName", nameGenerator.getDBOClassName(table));
		context.put("DBOPackageName", nameGenerator.getDBOPackageName(model, table));
		context.put("DBOConverterClassName", nameGenerator.getDBOConverterClassName(table));
		context.put("DBOConverterPackageName", nameGenerator.getDBOConverterPackageName(model, table));
		context.put("IdClassName", getIdClassName(table));
		context.put("IdFieldName", nameGenerator.getAttributeName(getIdFieldNameCamelCase(table)));
		context.put("IdFieldNameCamelCase", getIdFieldNameCamelCase(table));
		context.put("JPARepositoryClassName", nameGenerator.getJPARepositoryInterfaceName(table));
		context.put("JPARepositoryPackageName", nameGenerator.getJPARepositoryPackageName(model, table));
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context.put("ModelPackageName", serviceNameGenerator.getModelPackageName(model, table));
		context.put("NoKeyValue", getNoKeyValue(table));
		context.put("PackageName", getPackageName(model, table));
		context.put("PageClassName", serviceNameGenerator.getPageClassName());
		context.put("PageConverterClassName", nameGenerator.getPageConverterClassName(table));
		context.put("PageConverterPackageName", nameGenerator.getPageConverterPackageName(model, table));
		context.put("PagePackageName", serviceNameGenerator.getPagePackageName(model, table));
		context.put("PageParametersClassName", serviceNameGenerator.getPageParametersClassName());
		context.put(
				"PageParametersToPageableConverterClassName",
				nameGenerator.getPageParametersToPageableConverterClassName(table));
		context.put(
				"PageParametersToPageableConverterPackageName",
				nameGenerator.getPageParametersToPageableConverterPackageName(model, table));
		context.put("PersistencePortInterfaceName", serviceNameGenerator.getPersistencePortInterfaceName(table));
		context.put("PersistencePortPackageName", serviceNameGenerator.getPersistencePortPackageName(model, table));
		context.put("ToDBOMethodName", nameGenerator.getToDBOMethodName(table));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(table));
	}

	private String getNoKeyValue(TableModel table) {
		ColumnModel[] pks = table.getPrimaryKeyColumns();
		if (pks.length == 0) {
			return "NO_KEY_FOUND";
		} else if (pks.length > 1) {
			return "null";
		}
		return pks[0].isNotNull()
				? "-1"
				: "null";
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getJPAPersistenceAdapterClassName(table);
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

	@Override
	protected String getCompositeKeyClassName(TableModel table) {
		return serviceNameGenerator.getCompositeKeyClassName(table);
	}

}