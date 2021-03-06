package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A JPA repository code generator for JPA database objects (DBO's).
 *
 * @author ollie (27.06.2021)
 */
public class JPARepositoryInterfaceCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public JPARepositoryInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"JPARepositoryInterface.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("DBOClassName", nameGenerator.getDBOClassName(table));
		context.put("DBOPackageName", nameGenerator.getDBOPackageName(model, table));
		context.put("IdClassName", getIdClassName(table));
		context.put("PackageName", getPackageName(model, table));
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getJPARepositoryInterfaceName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getJPARepositoryPackageName(model, table);
	}

}