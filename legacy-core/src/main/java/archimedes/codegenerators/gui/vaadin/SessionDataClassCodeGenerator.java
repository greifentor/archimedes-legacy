package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.CubeNameGenerator;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the session data classes.
 *
 * @author ollie (07.09.2022)
 */

public class SessionDataClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public SessionDataClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"SessionDataClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel model0) {
		context.put("AccessCheckerInterfaceName", CubeNameGenerator.INSTANCE.getAccessCheckerInterfaceName(model));
		context.put("AccessCheckerPackageName", CubeNameGenerator.INSTANCE.getAccessCheckerPackageName(model));
		context.put("ApplicationName", model.getApplicationName());
		context.put("AuthorizationDataClassName", CubeNameGenerator.INSTANCE.getAuthorizationDataClassName(model));
		context.put("ClassName", getClassName(model, null));
		context.put("CubeApplication", isCubeApplication(model));
		context.put("JWTServiceInterfaceName", CubeNameGenerator.INSTANCE.getJWTServiceInterfaceName(model));
		context.put("LocalizationSOClassName", LocalizationNameGenerator.INSTANCE.getLocalizationSOClassName());
		context
				.put(
						"LocalizationSOPackageName",
						LocalizationNameGenerator.INSTANCE.getLocalizationSOPackageName(model));
		context.put("PackageName", getPackageName(model, null));
		context.put("ServicePackageName", ServiceNameGenerator.INSTANCE.getServiceInterfacePackageName(model, null));
		context.put("SessionIdClassName", nameGenerator.getSessionIdClassName(model));
	}

	private boolean isCubeApplication(DataModel model) {
		return model.isOptionSet(AbstractGUIVaadinClassCodeGenerator.CUBE_APPLICATION);
	}

	@Override
	public String getClassName(DataModel model, DataModel dummy) {
		return nameGenerator.getSessionDataClassName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel dummy) {
		return nameGenerator.getSessionDataPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}
