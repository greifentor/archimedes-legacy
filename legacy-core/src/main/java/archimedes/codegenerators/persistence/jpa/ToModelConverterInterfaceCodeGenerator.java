package archimedes.codegenerators.persistence.jpa;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;

/**
 * A to model converter interface code generator for JPA database objects (DBO's).
 *
 * @author ollie (28.07.2021)
 */
public class ToModelConverterInterfaceCodeGenerator extends AbstractModelCodeGenerator<PersistenceJPANameGenerator> {

	public ToModelConverterInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ToModelConverterInterface.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
		        PersistenceJPANameGenerator.INSTANCE,
		        TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("ClassName", getClassName(model, sameModel));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, sameModel));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getToModelConverterInterfaceName();
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getDBOConverterPackageName(model, null);
	}

}
