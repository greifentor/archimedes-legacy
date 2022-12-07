package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;

/**
 * A class code generator for the session id classes.
 *
 * @author ollie (02.09.2022)
 */
public class ApplicationStartViewClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public static final String GUI_BASE_URL = "GUI_BASE_URL";

	public ApplicationStartViewClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ApplicationStartViewClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel model0) {
		context.put("BaseURL", getBaseURL(model));
		context.put("ClassName", getClassName(model, null));
		context.put("PackageName", getPackageName(model, null));
		context.put("MainMenuViewClassName", nameGenerator.getMainMenuViewClassName());
	}

	private String getBaseURL(DataModel model) {
		return OptionGetter
				.getOptionByName(model, GUI_BASE_URL)
				.map(OptionModel::getParameter)
				.orElseGet(() -> model.getApplicationName().toLowerCase());
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