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
import archimedes.codegenerators.gui.vaadin.component.AbstractMasterDataBaseLayoutClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.ButtonClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.ButtonFactoryClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.ButtonFactoryConfigurationClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.ButtonGridClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.HeaderLayoutClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.ImageClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.MasterDataButtonLayoutClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.MasterDataViewButtonAdderInterfaceCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.TextFieldClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.component.TextFieldFactoryClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.AccessCheckerInterfaceCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.AuthorizationUserInterfaceCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.AuthorizationUserServiceImplClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.AuthorizationUserServiceInterfaceCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.JWTNotValidExceptionClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.JWTServiceConfigurationClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.JWTServiceImplClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.JWTServiceInterfaceCodeGenerator;
import archimedes.codegenerators.gui.vaadin.cube.WebAppConfigurationClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.masterdata.DetailsLayoutComboBoxItemLabelGeneratorInterfaceCodeGenerator;
import archimedes.codegenerators.gui.vaadin.masterdata.MaintenanceViewRendererInterfaceCodeGenerator;
import archimedes.codegenerators.gui.vaadin.masterdata.MasterDataGUIConfigurationClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.masterdata.MasterDataViewClassCodeGenerator;
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

	public static final String TEMPLATE_FOLDER_PATH = AbstractCodeFactory.TEMPLATE_PATH
			+ System.getProperty(GUIVaadinCodeFactory.class.getSimpleName() + ".templates.folder", "/gui-vaadin");

	@Override
	protected List<CodeGenerator<?>> getCodeGenerators() {
		return Arrays
				.asList(
						new AbstractMasterDataBaseLayoutClassCodeGenerator(this),
						new AccessCheckerInterfaceCodeGenerator(this),
						new ApplicationStartViewClassCodeGenerator(this),
						new AuthorizationUserInterfaceCodeGenerator(this),
						new AuthorizationUserServiceImplClassCodeGenerator(this),
						new AuthorizationUserServiceInterfaceCodeGenerator(this),
						new ButtonClassCodeGenerator(this),
						new ButtonFactoryClassCodeGenerator(this),
						new ButtonFactoryConfigurationClassCodeGenerator(this),
						new ButtonGridClassCodeGenerator(this),
						// new GOClassCodeGenerator(this),
						// new GOConverterClassCodeGenerator(this),
						new DetailsLayoutClassCodeGenerator(this),
						new DetailsLayoutComboBoxItemLabelGeneratorInterfaceCodeGenerator(this),
						new GUIApplicationStarterClassCodeGenerator(this),
						new GUIConfigurationClassCodeGenerator(this),
						new HeaderLayoutClassCodeGenerator(this),
						new ImageClassCodeGenerator(this),
						new ItemLabelGeneratorCollectionClassCodeGenerator(this),
						new JWTNotValidExceptionClassCodeGenerator(this),
						new JWTServiceConfigurationClassCodeGenerator(this),
						new JWTServiceImplClassCodeGenerator(this),
						new JWTServiceInterfaceCodeGenerator(this),
						new MainMenuViewClassCodeGenerator(this),
						new MaintenanceViewClassCodeGenerator(this),
						new MaintenanceViewRendererInterfaceCodeGenerator(this),
						new MasterDataButtonLayoutClassCodeGenerator(this),
						new MasterDataGridFieldRendererClassCodeGenerator(this),
						new MasterDataGridFieldRendererInterfaceCodeGenerator(this),
						new MasterDataGUIConfigurationClassCodeGenerator(this),
						new MasterDataViewClassCodeGenerator(this),
						new MasterDataViewButtonAdderInterfaceCodeGenerator(this),
						new PageViewClassCodeGenerator(this),
						// new PageGOClassCodeGenerator(this),
						// new PageParametersGOClassCodeGenerator(this),
						// new PageParametersGOConverterClassCodeGenerator(this),
						// new PageGOConverterClassCodeGenerator(this),
						new SessionDataClassCodeGenerator(this),
						new SessionIdClassCodeGenerator(this),
						new TextFieldClassCodeGenerator(this),
						new TextFieldFactoryClassCodeGenerator(this),
						// new ToGOConverterInterfaceCodeGenerator(this),
						new UserAuthorizationCheckerClassCodeGenerator(this),
						new WebAppConfigurationClassCodeGenerator(this),
						// Label property generator must called at last !!!
						new LabelPropertiesGenerator(this));
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
					GUIVaadinNameGenerator.ALTERNATE_BUTTON_CLASS_NAME_SUFFIX,
					GUIVaadinNameGenerator.ALTERNATE_BUTTON_FACTORY_CLASS_NAME_SUFFIX,
					GUIVaadinNameGenerator.ALTERNATE_BUTTON_FACTORY_CONFIGURATION_CLASS_NAME_SUFFIX,
					GUIVaadinNameGenerator.ALTERNATE_BUTTON_GRID_CLASS_NAME_SUFFIX,
					GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_CLASS_NAME_SUFFIX,
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
					AbstractGUIVaadinClassCodeGenerator.CUBE_APPLICATION,
					AbstractClassCodeGenerator.COMMENTS,
					AbstractClassCodeGenerator.GENERATE_ID_CLASS,
					ApplicationStartViewClassCodeGenerator.GUI_BASE_URL,
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
