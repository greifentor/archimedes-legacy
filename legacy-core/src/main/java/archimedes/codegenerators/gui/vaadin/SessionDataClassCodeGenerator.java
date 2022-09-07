package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
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
		context.put("ApplicationName", model.getApplicationName());
		context.put("ClassName", getClassName(model, null));
		context.put("PackageName", getPackageName(model, null));
		context.put("SessionIdClassName", nameGenerator.getSessionIdClassName(model));
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
