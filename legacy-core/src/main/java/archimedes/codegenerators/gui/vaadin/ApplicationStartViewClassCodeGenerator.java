package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.model.DataModel;

/**
 * A class code generator for the session id classes.
 *
 * @author ollie (02.09.2022)
 */
public class ApplicationStartViewClassCodeGenerator extends AbstractVaadinModelCodeGenerator {

	public ApplicationStartViewClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("ApplicationStartViewClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel model0) {
		context.put("BaseURL", getBaseURL(model));
		context.put("ClassName", getClassName(model, null));
		context.put("PackageName", getPackageName(model, null));
		context.put("MainMenuViewClassName", nameGenerator.getMainMenuViewClassName());
	}

	@Override
	public String getClassName(DataModel model, DataModel dummy) {
		return nameGenerator.getApplicationStartViewClassName();
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel dummy) {
		return nameGenerator.getApplicationStartViewPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}