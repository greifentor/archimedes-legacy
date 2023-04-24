package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the session id classes.
 *
 * @author ollie (02.09.2022)
 */
public class SessionIdClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public SessionIdClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"SessionIdClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel model0) {
		context.put("ClassName", getClassName(model, null));
		context.put("PackageName", getPackageName(model, null));
	}

	@Override
	public String getClassName(DataModel model, DataModel dummy) {
		return nameGenerator.getSessionIdClassName(model);
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