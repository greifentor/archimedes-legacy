package archimedes.codegenerators.gui.vaadin.cube;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the JWT service interface.
 *
 * @author ollie (06.01.2023)
 */
public class JWTServiceInterfaceCodeGenerator extends AbstractModelCodeGenerator<CubeNameGenerator> {

	public JWTServiceInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"JWTServiceInterface.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH + "/cube",
				CubeNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("AuthorizationDataClassName", nameGenerator.getAuthorizationDataClassName(model));
		context.put("AuthorizationUserInterfaceName", nameGenerator.getAuthorizationUserInterfaceName(model));
		context.put("AuthorizationUserPackageName", ServiceNameGenerator.INSTANCE.getModelPackageName(model, null));
		context.put("ClassName", getClassName(model, null));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, null));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getJWTServiceInterfaceName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return ServiceNameGenerator.INSTANCE.getServiceInterfacePackageName(model, null);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, DataModel sameModel) {
		return !model.isOptionSet(AbstractGUIVaadinClassCodeGenerator.CUBE_APPLICATION);
	}

}