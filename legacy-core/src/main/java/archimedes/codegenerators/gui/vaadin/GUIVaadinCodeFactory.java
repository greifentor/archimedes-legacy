package archimedes.codegenerators.gui.vaadin;

import java.util.Arrays;
import java.util.List;

import archimedes.acf.checker.ModelChecker;
import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CodeGenerator;
import archimedes.codegenerators.Filters;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.gui.vaadin.component.ButtonClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.ImageClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.TextFieldClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.modelcheckers.ModelCheckerGuiEditorPosHasAValue;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.model.OptionType;
import archimedes.model.PredeterminedOptionProvider;

/**
 * A code factory for JPA persistence ports and adapters for CRUD operations.
 *
 * @author ollie (03.03.2021)
 */
public class GUIVaadinCodeFactory extends AbstractClassCodeFactory implements CodeFactoryProgressionEventProvider,
		PredeterminedOptionProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_FOLDER_PATH =
			AbstractCodeFactory.TEMPLATE_PATH + System
					.getProperty(GUIVaadinCodeFactory.class.getSimpleName() + ".templates.folder", "/gui-vaadin");

	@Override
	protected List<CodeGenerator<?>> getCodeGenerators() {
		return Arrays
				.asList(
						new ButtonClassCodeGenerator(this),
						// new GOClassCodeGenerator(this),
						// new GOConverterClassCodeGenerator(this),
						new DetailsLayoutClassCodeGenerator(this),
						new ImageClassCodeGenerator(this),
						new MaintenanceViewClassCodeGenerator(this),
						new PageViewClassCodeGenerator(this),
						// new PageGOClassCodeGenerator(this),
						// new PageParametersGOClassCodeGenerator(this),
						// new PageParametersGOConverterClassCodeGenerator(this),
						// new PageGOConverterClassCodeGenerator(this),
						new SessionDataClassCodeGenerator(null),
						new SessionIdClassCodeGenerator(this),
						new TextFieldClassCodeGenerator(this)
				// new ToGOConverterInterfaceCodeGenerator(this)
				);
	}

	@Override
	public String getName() {
		return "GUI Vaadin Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "gui-vaadin-code-factory" };
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String[] getSelectableOptions(OptionType optionType) {
		switch (optionType) {
		case COLUMN:
			return new String[] {
					Filters.FILTER,
					AbstractGUIVaadinClassCodeGenerator.GUI_EDITOR_POS,
					AbstractClassCodeGenerator.MAX,
					AbstractClassCodeGenerator.MIN,
					AbstractGUIVaadinClassCodeGenerator.NAME_FIELD,
					DetailsLayoutClassCodeGenerator.PREFERENCE,
					AbstractClassCodeGenerator.STEP };
		case DOMAIN:
			return new String[] {
					AbstractClassCodeGenerator.MAX,
					AbstractClassCodeGenerator.MIN,
					AbstractClassCodeGenerator.STEP,
					AbstractClassCodeGenerator.TEXT };
		case MODEL:
			return new String[] {
					GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX,
					GUIVaadinNameGenerator.ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX,
					GUIVaadinNameGenerator.ALTERNATE_GO_CONVERTER_PACKAGE_NAME,
					GUIVaadinNameGenerator.ALTERNATE_GO_CLASS_NAME_SUFFIX,
					GUIVaadinNameGenerator.ALTERNATE_GO_PACKAGE_NAME,
					ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_MODEL_PACKAGE_NAME,
					GUIVaadinNameGenerator.ALTERNATE_PAGE_GO_CONVERTER_PACKAGE_NAME,
					GUIVaadinNameGenerator.ALTERNATE_PAGE_GO_PACKAGE_NAME,
					GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME,
					GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME,
					ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME,
					AbstractClassCodeGenerator.ALTERNATE_MODULE_PREFIX,
					AbstractClassCodeGenerator.COMMENTS,
					AbstractClassCodeGenerator.GENERATE_ID_CLASS,
					AbstractClassCodeGenerator.MODULE_MODE,
					GUIVaadinNameGenerator.ALTERNATE_TO_GO_METHOD_NAME,
					GUIVaadinNameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME };
		case TABLE:
			return new String[] {
					AbstractClassCodeFactory.NO_GENERATION,
					AbstractClassCodeGenerator.GENERATE_ID_CLASS,
					AbstractGUIVaadinClassCodeGenerator.GENERATE_MASTER_DATA_GUI,
					NameGenerator.MODULE,
					NameGenerator.PLURAL_NAME,
					AbstractClassCodeGenerator.POJO_MODE };
		default:
			return new String[0];
		}
	}

	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[] { new ModelCheckerGuiEditorPosHasAValue(guiBundle) };
	}

}
