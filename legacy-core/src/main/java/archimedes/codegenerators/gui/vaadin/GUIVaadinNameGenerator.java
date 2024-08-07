package archimedes.codegenerators.gui.vaadin;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.OptionGetter;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;

/**
 * A specialized name generator for persistence JPA names.
 *
 * @author ollie (13.03.2021)
 */
public class GUIVaadinNameGenerator extends NameGenerator {

	public static final GUIVaadinNameGenerator INSTANCE = new GUIVaadinNameGenerator();

	public static final String ALTERNATE_APPLICATION_START_VIEW_PACKAGE_NAME =
			"ALTERNATE_APPLICATION_START_VIEW_PACKAGE_NAME";
	public static final String ALTERNATE_BUTTON_CLASS_NAME_SUFFIX = "ALTERNATE_BUTTON_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_BUTTON_FACTORY_CLASS_NAME_SUFFIX =
			"ALTERNATE_BUTTON_FACTORY_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_BUTTON_FACTORY_CONFIGURATION_CLASS_NAME_SUFFIX =
			"ALTERNATE_BUTTON_FACTORY_CONFIGURATION_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_BUTTON_GRID_CLASS_NAME_SUFFIX = "ALTERNATE_BUTTON_GRID_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_COMPONENT_FACTORY_CLASS_NAME_SUFFIX =
			"ALTERNATE_COMPONENT_FACTORY_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MASTER_DATA_VIEW_BUTTON_ADDER_CLASS_NAME_SUFFIX =
			"ALTERNATE_MASTER_DATA_VIEW_BUTTON_ADDER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_DETAILS_LAYOUT_CLASS_NAME_SUFFIX =
			"ALTERNATE_DETAILS_LAYOUT_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_DETAILS_LAYOUT_COMBO_BOX_ITEM_LABEL_GENERATOR_INTERFACE_NAME =
			"ALTERNATE_DETAILS_LAYOUT_COMBO_BOX_ITEM_LABEL_GENERATOR_INTERFACE_NAME";
	public static final String ALTERNATE_DETAILS_DIALOG_CLASS_NAME_SUFFIX =
			"ALTERNATE_DETAILS_DIALOG_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_DETAILS_DIALOG_PACKAGE_NAME = "ALTERNATE_DETAILS_DIALOG_PACKAGE_NAME";
	public static final String ALTERNATE_GUI_APPLICATION_STARTER_CLASS_NAME_SUFFIX =
			"ALTERNATE_GUI_APPLICATION_STARTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_GUI_VAADIN_MODULE_PREFIX = "ALTERNATE_GUI_VAADIN_MODULE_PREFIX";
	public static final String ALTERNATE_GUI_PACKAGE_NAME = "ALTERNATE_GUI_PACKAGE_NAME";
	public static final String ALTERNATE_TO_GO_METHOD_NAME = "ALTERNATE_TO_GO_METHOD_NAME";
	public static final String ALTERNATE_TO_MODEL_METHOD_NAME = "ALTERNATE_TO_MODEL_METHOD_NAME";
	public static final String ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX = "ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_GO_CONVERTER_PACKAGE_NAME = "ALTERNATE_GO_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_GO_CLASS_NAME_SUFFIX = "ALTERNATE_GO_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_GO_PACKAGE_NAME = "ALTERNATE_GO_PACKAGE_NAME";
	public static final String ALTERNATE_GUI_CONFIGURATION_CLASS_NAME = "ALTERNATE_GUI_CONFIGURATION_CLASS_NAME";
	public static final String ALTERNATE_GUI_CONFIGURATION_PACKAGE_NAME = "ALTERNATE_GUI_CONFIGURATION_PACKAGE_NAME";
	public static final String ALTERNATE_HEADER_LAYOUT_CLASS_NAME_SUFFIX = "ALTERNATE_HEADER_LAYOUT_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_HEADER_LAYOUT_PACKAGE_NAME = "ALTERNATE_HEADER_LAYOUT_PACKAGE_NAME";
	public static final String ALTERNATE_IMAGE_CLASS_NAME_SUFFIX = "ALTERNATE_IMAGE_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_ITEM_LABEL_GENERATOR_CLASS_NAME_SUFFIX =
			"ALTERNATE_ITEM_LABEL_GENERATOR_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_ITEM_LABEL_GENERATOR_PACKAGE_NAME =
			"ALTERNATE_ITEM_LABEL_GENERATOR_PACKAGE_NAME";
	public static final String ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_CLASS_NAME_SUFFIX =
			"ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_PACKAGE_NAME =
			"ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_PACKAGE_NAME";
	public static final String ALTERNATE_LIST_DETAILS_LAYOUT_CLASS_NAME_SUFFIX =
			"ALTERNATE_LIST_DETAILS_LAYOUT_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_LIST_DETAILS_LAYOUT_PACKAGE_NAME =
			"ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_PACKAGE_NAME";
	public static final String ALTERNATE_MAINTENANCE_VIEW_CLASS_NAME_SUFFIX =
			"ALTERNATE_MAINTENANCE_VIEW_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MAINTENANCE_VIEW_PACKAGE_NAME = "ALTERNATE_MAINTENANCE_VIEW_PACKAGE_NAME";
	public static final String ALTERNATE_MAINTENANCE_VIEW_RENDERER_INTERFACE_NAME =
			"ALTERNATE_MAINTENANCE_VIEW_RENDERER_INTERFACE_NAME_SUFFIX";
	public static final String ALTERNATE_MAINTENANCE_VIEW_CREATE_NEW_MODEL_MODIFICATION_CLASS_NAME_SUFFIX =
			"	ALTERNATE_MAINTENANCE_VIEW_CREATE_NEW_MODEL_MODIFICATION_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MAINTENANCE_VIEW_CREATE_NEW_MODEL_MODIFICATION_PACKAGE_NAME =
			"ALTERNATE_MAINTENANCE_VIEW_CREATE_NEW_MODEL_MODIFICATION_PACKAGE_NAME";
	public static final String ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_CLASS_NAME_SUFFIX =
			"ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_PACKAGE_NAME =
			"ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_PACKAGE_NAME";
	public static final String ALTERNATE_MASTER_DATA_GRID_FIELD_RENDERER_CLASS_PACKAGE_NAME =
			"ALTERNATE_MASTER_DATA_GRID_FIELD_RENDERER_CLASS_PACKAGE_NAME";
	public static final String ALTERNATE_MASTER_DATA_GRID_FIELD_RENDERER_INTERFACE_NAME =
			"ALTERNATE_MASTER_DATA_GRID_FIELD_RENDERER_INTERFACE_NAME";
	public static final String ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_CLASS_NAME =
			"ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_CLASS_NAME";
	public static final String ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_PACKAGE_NAME =
			"ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_PACKAGE_NAME";
	public static final String ALTERNATE_MASTER_DATA_VIEW_CLASS_NAME = "ALTERNATE_MASTER_DATA_VIEW_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MASTER_DATA_VIEW_PACKAGE_NAME = "ALTERNATE_MASTER_DATA_VIEW_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_GO_CONVERTER_PACKAGE_NAME = "ALTERNATE_PAGE_GO_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_GO_PACKAGE_NAME = "ALTERNATE_PAGE_GO_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_VIEW_PACKAGE_NAME = "ALTERNATE_PAGE_VIEW_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_VIEW_CLASS_NAME_SUFFIX = "ALTERNATE_PAGE_VIEW_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME =
			"ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME = "ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME";
	public static final String ALTERNATIVE_REMOVE_CONFIRM_DIALOG_CLASS_NAME =
			"ALTERNATIVE_REMOVE_CONFIRM_DIALOG_CLASS_NAME";
	public static final String ALTERNATE_SELECTABLE_SUBCLASS_CLASS_NAME_SUFFIX =
			"ALTERNATE_SELECTABLE_SUBCLASS_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_SELECTABLE_SUBCLASS_PACKAGE_NAME = "ALTERNATE_SELECTABLE_SUBCLASS_PACKAGE_NAME";
	public static final String ALTERNATE_SELECTION_DIALOG_CLASS_NAME_SUFFIX =
			"ALTERNATE_SELECTION_DIALOG_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_SELECTION_DIALOG_PACKAGE_NAME = "ALTERNATE_SELECTION_DIALOG_PACKAGE_NAME";
	public static final String ALTERNATE_SERVICE_PROVIDER_CLASS_NAME_SUFFIX =
			"ALTERNATE_SERVICE_PROVIDER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_SESSION_DATA_CLASS_NAME_SUFFIX = "ALTERNATE_SESSION_DATA_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_SESSION_DATA_PACKAGE_NAME = "ALTERNATE_SESSION_DATA_PACKAGE_NAME";
	public static final String ALTERNATE_SESSION_ID_CLASS_NAME = "ALTERNATE_SESSION_ID_CLASS_NAME";
	public static final String ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX = "ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_TEXT_FIELD_FACTORY_CLASS_NAME_SUFFIX =
			"ALTERNATE_TEXT_FIELD_FACTORY_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_USER_AUTHORIZATION_CHECKER_CLASS_NAME_SUFFIX =
			"ALTERNATE_USER_AUTHORIZATION_CHECKER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_USER_AUTHORIZATION_CHECKER_PACKAGE_NAME =
			"ALTERNATE_USER_AUTHORIZATION_CHECKER_PACKAGE_NAME";
	public static final String ALTERNATE_VAADIN_COMPONENT_PACKAGE_NAME = "ALTERNATE_VAADIN_COMPONENT_PACKAGE_NAME";
	public static final String ALTERNATE_VAADIN_PACKAGE_NAME = "ALTERNATE_VAADIN_PACKAGE_NAME";
	public static final String ALTERNATE_VAADIN_STYLES_PACKAGE_NAME = "ALTERNATE_VAADIN_STYLES_PACKAGE_NAME";

	public String getAbstractMasterDataBaseLayoutClassName() {
		return "AbstractMasterDataBaseLayout";
	}

	public String getAbstractMasterDataDetailLayoutClassName() {
		return "AbstractMasterDataDetailLayout";
	}

	public String getApplicationStartViewClassName() {
		return "ApplicationStartView";
	}

	public String getApplicationStartViewPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin", ALTERNATE_APPLICATION_START_VIEW_PACKAGE_NAME);
	}

	public String getGUIApplicationStarterClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"GUIApplicationStarter",
						ALTERNATE_GUI_APPLICATION_STARTER_CLASS_NAME_SUFFIX);
	}

	public String getGUIApplicationStarterPackageName(DataModel model) {
		return createPackageName(model, null, "gui", ALTERNATE_GUI_PACKAGE_NAME);
	}

	public String getVaadinComponentPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.component", ALTERNATE_VAADIN_COMPONENT_PACKAGE_NAME);
	}

	public String getVaadinPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin", ALTERNATE_VAADIN_PACKAGE_NAME);
	}

	public String getVaadinStylesPackageName(DataModel model) {
		return "styles";
	}

	public String getButtonClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "Button", ALTERNATE_BUTTON_CLASS_NAME_SUFFIX);
	}

	public String getButtonFactoryClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "ButtonFactory", ALTERNATE_BUTTON_FACTORY_CLASS_NAME_SUFFIX);
	}

	public String getButtonFactoryConfigurationClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"ButtonFactoryConfiguration",
						ALTERNATE_BUTTON_FACTORY_CONFIGURATION_CLASS_NAME_SUFFIX);
	}

	public String getButtonGridClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "ButtonGrid", ALTERNATE_BUTTON_GRID_CLASS_NAME_SUFFIX);
	}

	public String getComponentFactoryClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"ComponentFactory",
						ALTERNATE_COMPONENT_FACTORY_CLASS_NAME_SUFFIX);
	}

	public String getDetailsLayoutClassName(DataModel model, TableModel table) {
		return table != null ? getClassName(table) + getDetailsLayoutClassNameSuffix(table) : null;
	}

	private String getDetailsLayoutClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"DetailsLayout",
				ALTERNATE_DETAILS_LAYOUT_CLASS_NAME_SUFFIX);
	}

	public String getDetailsDialogClassName(DataModel model, TableModel table) {
		return table != null ? getClassName(table) + getDetailsDialogClassNameSuffix(table) : null;
	}

	private String getDetailsDialogClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"DetailsDialog",
				ALTERNATE_DETAILS_DIALOG_CLASS_NAME_SUFFIX);
	}

	public String getDetailsDialogPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.masterdata.dialog", ALTERNATE_DETAILS_DIALOG_PACKAGE_NAME);
	}

	public String getDetailsLayoutComboBoxItemLabelGeneratorInterfaceName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"DetailsLayoutComboBoxItemLabelGenerator",
						ALTERNATE_DETAILS_LAYOUT_COMBO_BOX_ITEM_LABEL_GENERATOR_INTERFACE_NAME);
	}

	public String getGOClassName(TableModel table) {
		return table != null ? getClassName(table) + getGOClassNameSuffix(table) : null;
	}

	private String getGOClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(table.getDataModel(), "GO", ALTERNATE_GO_CLASS_NAME_SUFFIX);
	}

	public String getGOConverterClassName(TableModel table) {
		return table != null ? getClassName(table) + getGOConverterNameSuffix(table) : null;
	}

	private String getGOConverterNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "GOConverter"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), AbstractClassCodeGenerator.MAPPERS)
						.filter(s -> s.toLowerCase().startsWith("mapstruct"))
						.map(s -> getGOMapperInterfaceNameSuffix(table))
						.orElse("GOConverter");
	}

	private String getGOMapperInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"GOMapper",
				ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX);
	}

	public String getGOConverterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "gui.vaadin.converter", ALTERNATE_GO_CONVERTER_PACKAGE_NAME);
	}

	public String getGOPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "gui.vaadin.go", ALTERNATE_GO_PACKAGE_NAME);
	}

	public String getGUIConfigurationClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "GUIConfiguration", ALTERNATE_GUI_CONFIGURATION_CLASS_NAME);
	}

	public String getGUIConfigurationPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin", ALTERNATE_GUI_CONFIGURATION_PACKAGE_NAME);
	}

	public String getHeaderLayoutClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "HeaderLayout", ALTERNATE_HEADER_LAYOUT_CLASS_NAME_SUFFIX);
	}

	public String getHeaderLayoutPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.component", ALTERNATE_HEADER_LAYOUT_PACKAGE_NAME);
	}

	public String getItemLabelGeneratorClassName(DataModel model, DomainModel domain) {
		return domain == null
				? null
				: getClassName(domain.getName()) + getNameOrAlternativeFromOption(
						model,
						"ItemLabelGenerator",
						ALTERNATE_ITEM_LABEL_GENERATOR_CLASS_NAME_SUFFIX);
	}

	public String getItemLabelGeneratorCollectionClassName(DataModel model, TableModel table) {
		return table == null
				? null
				: getClassName(table) + getNameOrAlternativeFromOption(
						model,
						"ItemLabelGeneratorCollection",
						ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_CLASS_NAME_SUFFIX);
	}

	public String getItemLabelGeneratorPackageName(DataModel model) {
		return createPackageName(
				model,
				null,
				"gui.vaadin.masterdata.renderer",
				ALTERNATE_ITEM_LABEL_GENERATOR_PACKAGE_NAME);
	}

	public String getItemLabelGeneratorCollectionPackageName(DataModel model) {
		return createPackageName(
				model,
				null,
				"gui.vaadin.masterdata.renderer",
				ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_PACKAGE_NAME);
	}

	public String getListDetailsLayoutClassName(DataModel model, TableModel table) {
		return table == null
				? null
				: getClassName(table) + getNameOrAlternativeFromOption(
						model,
						"ListDetailsLayout",
						ALTERNATE_LIST_DETAILS_LAYOUT_CLASS_NAME_SUFFIX);
	}

	public String getListDetailsLayoutPackageName(DataModel model) {
		return createPackageName(
				model,
				null,
				"gui.vaadin.masterdata.layout.list",
				ALTERNATE_LIST_DETAILS_LAYOUT_PACKAGE_NAME);
	}

	public String getImageClassName(DataModel model) {
		return model == null ? null : getNameOrAlternativeFromOption(model, "Image", ALTERNATE_IMAGE_CLASS_NAME_SUFFIX);
	}

	public String getMainMenuViewClassName() {
		return "MainMenuView";
	}

	public String getMaintenanceViewClassName(DataModel model, TableModel table) {
		return table != null ? getClassName(table) + getMaintenanceViewClassNameSuffix(table) : null;
	}

	private String getMaintenanceViewClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"MaintenanceView",
				ALTERNATE_MAINTENANCE_VIEW_CLASS_NAME_SUFFIX);
	}

	public String getMaintenanceViewPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "gui.vaadin.masterdata", ALTERNATE_MAINTENANCE_VIEW_PACKAGE_NAME);
	}

	public String getMaintenanceViewCreateNewModelModificationInterfaceName(DataModel model) {
		return getNameOrAlternativeFromOption(
				model,
				"MaintenanceViewCreateNewModelModification",
				ALTERNATE_MAINTENANCE_VIEW_CREATE_NEW_MODEL_MODIFICATION_CLASS_NAME_SUFFIX);
	}

	public String getMaintenanceViewCreateNewModelModificationPackageName(DataModel model, TableModel table) {
		return createPackageName(
				model,
				table,
				"gui.vaadin.masterdata",
				ALTERNATE_MAINTENANCE_VIEW_CREATE_NEW_MODEL_MODIFICATION_PACKAGE_NAME);
	}

	public String getMaintenanceViewRendererInterfaceName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"MaintenanceViewRenderer",
						ALTERNATE_MAINTENANCE_VIEW_RENDERER_INTERFACE_NAME);
	}

	public String getMasterDataButtonLayoutClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"MasterDataButtonLayout",
						ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_CLASS_NAME_SUFFIX);
	}

	public String getMasterDataButtonLayoutPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.component", ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_PACKAGE_NAME);
	}

	public String getMasterDataGridFieldRendererClassName(TableModel table) {
		return table != null ? getClassName(table) + "MasterDataGridFieldRenderer" : null;
	}

	public String getMasterDataGridFieldRendererClassPackageName(DataModel model, TableModel table) {
		return createPackageName(
				model,
				table,
				"gui.vaadin.masterdata.renderer",
				ALTERNATE_MASTER_DATA_GRID_FIELD_RENDERER_CLASS_PACKAGE_NAME);
	}

	public String getMasterDataGridFieldRendererInterfaceName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"MasterDataGridFieldRenderer",
						ALTERNATE_MASTER_DATA_GRID_FIELD_RENDERER_INTERFACE_NAME);
	}

	public String getMasterDataGUIConfigurationClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"MasterDataGUIConfiguration",
						ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_CLASS_NAME);
	}

	public String getMasterDataGUIConfigurationPackageName(DataModel model) {
		return createPackageName(
				model,
				null,
				"gui.vaadin.masterdata",
				ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_PACKAGE_NAME);
	}

	public String getMasterDataViewClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "MasterDataView", ALTERNATE_MASTER_DATA_VIEW_CLASS_NAME);
	}

	public String getMasterDataPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.masterdata", ALTERNATE_MASTER_DATA_VIEW_PACKAGE_NAME);
	}

	public String getMasterDataViewButtonAdderInterfaceName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"MasterDataViewButtonAdder",
						ALTERNATE_MASTER_DATA_VIEW_BUTTON_ADDER_CLASS_NAME_SUFFIX);
	}

	public String getMasterDataViewButtonAdderPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.component", ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_PACKAGE_NAME);
	}

	public String getPageGOConverterClassName(TableModel table) {
		return table != null ? "PageGOConverter" : null;
	}

	public String getPageGOConverterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "gui.vaadin.converter", ALTERNATE_PAGE_GO_CONVERTER_PACKAGE_NAME);
	}

	public String getPageGOClassName(TableModel table) {
		return table != null ? "PageGO" : null;
	}

	public String getPageGOPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "gui.vaadin.go.converter", ALTERNATE_PAGE_GO_PACKAGE_NAME);
	}

	public String getPageViewClassName(TableModel table) {
		return table != null ? getClassName(table) + getPageViewClassNameSuffix(table) : null;
	}

	private String getPageViewClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(table.getDataModel(), "PageView", ALTERNATE_PAGE_VIEW_CLASS_NAME_SUFFIX);
	}

	public String getPageViewPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "gui.vaadin.masterdata", ALTERNATE_PAGE_VIEW_PACKAGE_NAME);
	}

	public String getPageParametersGOClassName(TableModel table) {
		return table != null ? "PageParametersGO" : null;
	}

	public String getPageParametersGOPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "gui.vaadin.go.converter", ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME);
	}

	public String getPageParametersGOConverterClassName(TableModel table) {
		return table != null ? "PageParametersGOConverter" : null;
	}

	public String getPageParametersGOConverterPackageName(DataModel model, TableModel table) {
		return createPackageName(
				model,
				table,
				"gui.vaadin.converter",
				ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME);
	}

	@Override
	public String getPluralName(TableModel table) {
		return table == null
				? null
				: table.isOptionSet(PLURAL_NAME)
						? table.getOptionByName(PLURAL_NAME).getParameter()
						: getPluralName(table.getName());
	}

	public String getRemoveConfirmDialogClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"RemoveConfirmDialog",
						ALTERNATIVE_REMOVE_CONFIRM_DIALOG_CLASS_NAME);
	}

	public String getSelectableSubclassClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"SelectableSubclass",
						ALTERNATE_SELECTABLE_SUBCLASS_CLASS_NAME_SUFFIX);
	}

	public String getSelectableSubclassPackageName(DataModel model) {
		return createPackageName(
				model,
				null,
				"gui.vaadin.masterdata",
				ALTERNATE_SELECTABLE_SUBCLASS_PACKAGE_NAME);
	}

	public String getSelectionDialogClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"SelectionDialog",
						ALTERNATE_SELECTION_DIALOG_CLASS_NAME_SUFFIX);
	}

	public String getSelectionDialogPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.component", ALTERNATE_SELECTION_DIALOG_PACKAGE_NAME);
	}

	public String getServiceProviderClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"ServiceProvider",
						ALTERNATE_SERVICE_PROVIDER_CLASS_NAME_SUFFIX);
	}

	public String getSessionDataClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "SessionData", ALTERNATE_SESSION_DATA_CLASS_NAME_SUFFIX);
	}

	public String getSessionDataPackageName(DataModel model) {
		return createPackageName(model, null, "gui", ALTERNATE_SESSION_DATA_PACKAGE_NAME);
	}

	public String getSessionIdClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "SessionId", ALTERNATE_SESSION_ID_CLASS_NAME);
	}

	public String getTextFieldClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "TextField", ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX);
	}

	public String getTextFieldFactoryClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"TextFieldFactory",
						ALTERNATE_TEXT_FIELD_FACTORY_CLASS_NAME_SUFFIX);
	}

	public String getToGOConverterInterfaceName(TableModel table) {
		return table != null ? "ToGOConverter" : null;
	}

	public String getToGOMethodName(TableModel table) {
		return getNameOrAlternativeFromOption(
				table == null ? null : table.getDataModel(),
				"toGO",
				ALTERNATE_TO_GO_METHOD_NAME);
	}

	public String getToModelMethodName(TableModel table) {
		return getNameOrAlternativeFromOption(
				table == null ? null : table.getDataModel(),
				"toModel",
				ALTERNATE_TO_MODEL_METHOD_NAME);
	}

	public String getUserAuthorizationCheckerClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"UserAuthorizationChecker",
						ALTERNATE_USER_AUTHORIZATION_CHECKER_CLASS_NAME_SUFFIX);
	}

	public String getUserAuthorizationCheckerPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin", ALTERNATE_USER_AUTHORIZATION_CHECKER_PACKAGE_NAME);
	}

}