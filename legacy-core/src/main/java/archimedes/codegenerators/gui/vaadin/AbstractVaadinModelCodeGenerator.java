package archimedes.codegenerators.gui.vaadin;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;

public abstract class AbstractVaadinModelCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public static final String GUI_BASE_URL = "GUI_BASE_URL";

	public AbstractVaadinModelCodeGenerator(String templateName, AbstractCodeFactory codeFactory) {
		super(
				templateName,
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	protected String getBaseURL(DataModel model) {
		return model
				.findOptionByName(GUI_BASE_URL)
				.map(OptionModel::getParameter)
				.orElseGet(() -> model.getApplicationName().toLowerCase());
	}

}