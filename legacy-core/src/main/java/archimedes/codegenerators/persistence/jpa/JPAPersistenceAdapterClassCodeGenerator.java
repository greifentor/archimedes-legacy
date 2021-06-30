package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
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
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("DBOConverterClassName", nameGenerator.getDBOConverterClassName(table));
		context.put("DBOConverterPackageName", nameGenerator.getDBOConverterPackageName(model, table));
		context.put("IdClassName", getIdClassName(table));
		context.put("IdFieldNameCamelCase", getIdFieldNameCamelCase(table));
		context.put("JPARepositoryClassName", nameGenerator.getJPARepositoryInterfaceName(table));
		context.put("JPARepositoryPackageName", nameGenerator.getJPARepositoryPackageName(model, table));
		context.put("SOClassName", serviceNameGenerator.getSOClassName(table));
		context.put("SOPackageName", serviceNameGenerator.getSOPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
	}

	private String getIdFieldNameCamelCase(TableModel table) {
		return Arrays
				.asList(table.getPrimaryKeyColumns())
				.stream()
				.findFirst()
				.map(column -> nameGenerator.getClassName(column.getName()))
				.orElse("UNKNOWN");
	}

	private String getIdClassName(TableModel table) {
		return Arrays
				.asList(table.getPrimaryKeyColumns())
				.stream()
				.findFirst()
				.map(column -> typeGenerator.getJavaTypeString(column.getDomain(), true))
				.orElse("UNKNOWN");
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

}