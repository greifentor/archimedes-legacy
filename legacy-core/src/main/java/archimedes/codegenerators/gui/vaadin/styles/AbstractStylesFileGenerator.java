package archimedes.codegenerators.gui.vaadin.styles;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractFileGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the session id classes.
 *
 * @author ollie (08.11.2023)
 */
public abstract class AbstractStylesFileGenerator extends AbstractFileGenerator {

	public AbstractStylesFileGenerator(String templateFileName, String fileExtension,
			AbstractCodeFactory codeFactory) {
		super(
				templateFileName,
				fileExtension,
				codeFactory,
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				model -> GUIVaadinNameGenerator.INSTANCE.getVaadinStylesPackageName(model));
	}

	public AbstractStylesFileGenerator(String templateFileName, AbstractCodeFactory codeFactory) {
		this(templateFileName, ".css", codeFactory);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected String getPackageNameSuffix() {
		return "frontend";
	}

}