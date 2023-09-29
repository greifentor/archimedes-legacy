package archimedes.codegenerators.persistence.jpa;

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
				PersistenceJPANameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("GeneratedClassName", nameGenerator.getGeneratedJPARepositoryInterfaceName(table));
		context.put("PackageName", getPackageName(model, table));
		context.put("Subclass", isSubclass(table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
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

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel t) {
		return super.isToIgnoreFor(model, t) || isAMember(t);
	}

}