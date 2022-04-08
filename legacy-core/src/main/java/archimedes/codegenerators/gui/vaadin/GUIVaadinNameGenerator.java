package archimedes.codegenerators.gui.vaadin;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.OptionGetter;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A specialized name generator for persistence JPA names.
 *
 * @author ollie (13.03.2021)
 */
public class GUIVaadinNameGenerator extends NameGenerator {

	public static final GUIVaadinNameGenerator INSTANCE = new GUIVaadinNameGenerator();

	public static final String ALTERNATE_BUTTON_CLASS_NAME_SUFFIX = "ALTERNATE_BUTTON_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_BUTTON_FACTORY_CLASS_NAME_SUFFIX =
			"ALTERNATE_BUTTON_FACTORY_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_GUI_VAADIN_MODULE_PREFIX = "ALTERNATE_GUI_VAADIN_MODULE_PREFIX";
	public static final String ALTERNATE_TO_GO_METHOD_NAME = "ALTERNATE_TO_GO_METHOD_NAME";
	public static final String ALTERNATE_TO_MODEL_METHOD_NAME = "ALTERNATE_TO_MODEL_METHOD_NAME";
	public static final String ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX = "ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_GO_CONVERTER_PACKAGE_NAME = "ALTERNATE_GO_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_GO_CLASS_NAME_SUFFIX = "ALTERNATE_GO_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_GO_PACKAGE_NAME = "ALTERNATE_GO_PACKAGE_NAME";
	public static final String ALTERNATE_HEADER_LAYOUT_CLASS_NAME_SUFFIX = "ALTERNATE_HEADER_LAYOUT_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_HEADER_LAYOUT_PACKAGE_NAME = "ALTERNATE_HEADER_LAYOUT_PACKAGE_NAME";
	public static final String ALTERNATE_IMAGE_CLASS_NAME_SUFFIX = "ALTERNATE_IMAGE_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_CLASS_NAME_SUFFIX =
	        "ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_PACKAGE_NAME =
	        "ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_GO_CONVERTER_PACKAGE_NAME = "ALTERNATE_PAGE_GO_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_GO_PACKAGE_NAME = "ALTERNATE_PAGE_GO_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_LAYOUT_PACKAGE_NAME = "ALTERNATE_PAGE_LAYOUT_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_LAYOUT_CLASS_NAME_SUFFIX = "ALTERNATE_PAGE_LAYOUT_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME =
			"ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME = "ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME";
	public static final String ALTERNATE_SESSION_DATA_CLASS_NAME_SUFFIX = "ALTERNATE_SESSION_DATA_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_SESSION_DATA_PACKAGE_NAME = "ALTERNATE_SESSION_DATA_PACKAGE_NAME";
	public static final String ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX = "ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_VAADIN_COMPONENT_PACKAGE_NAME = "ALTERNATE_VAADIN_COMPONENT_PACKAGE_NAME";

	public String getAbstractMasterDataDetailLayoutClassName() {
		return "AbstractMasterDataDetailLayout";
	}

	public String getVaadinComponentPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.component", ALTERNATE_VAADIN_COMPONENT_PACKAGE_NAME);
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

	public String getHeaderLayoutClassName(DataModel model) {
		return model == null
		        ? null
		        : getNameOrAlternativeFromOption(model, "HeaderLayout", ALTERNATE_HEADER_LAYOUT_CLASS_NAME_SUFFIX);
	}

	public String getHeaderLayoutPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin.component", ALTERNATE_HEADER_LAYOUT_PACKAGE_NAME);
	}

	public String getImageClassName(DataModel model) {
		return model == null ? null : getNameOrAlternativeFromOption(model, "Image", ALTERNATE_IMAGE_CLASS_NAME_SUFFIX);
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

	public String getPageLayoutClassName(TableModel table) {
		return table != null ? getClassName(table) + getPageLayoutClassNameSuffix(table) : null;
	}

	private String getPageLayoutClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"PageLayout",
				ALTERNATE_PAGE_LAYOUT_CLASS_NAME_SUFFIX);
	}

	public String getPageLayoutPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "gui.vaadin.masterdata", ALTERNATE_PAGE_LAYOUT_PACKAGE_NAME);
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

	public String getSessionDataClassName(DataModel model) {
		return model == null
		        ? null
		        : getNameOrAlternativeFromOption(model, "SessionData", ALTERNATE_SESSION_DATA_CLASS_NAME_SUFFIX);
	}

	public String getSessionDataPackageName(DataModel model) {
		return createPackageName(model, null, "gui.vaadin", ALTERNATE_SESSION_DATA_PACKAGE_NAME);
	}

	public String getTextFieldClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "TextField", ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX);
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

}