package archimedes.codegenerators.rest.controller.springboot;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.OptionGetter;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * Specilization of the name generator for REST controllers.
 *
 * @author ollie (13.03.2021)
 */
public class RESTControllerNameGenerator extends NameGenerator {

	public static final String ALTERNATE_DTO_CLASS_NAME_SUFFIX = "ALTERNATE_DTO_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_DTO_PACKAGE_NAME = "ALTERNATE_DTO_PACKAGE_NAME";
	public static final String ALTERNATE_DTOCONVERTER_CLASS_NAME_SUFFIX = "ALTERNATE_DTOCONVERTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_DTOCONVERTER_PACKAGE_NAME = "ALTERNATE_DTOCONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_RESTCONTROLLER_CLASS_NAME_SUFFIX =
			"ALTERNATE_RESTCONTROLLER_CLASS_NAME_SUFFIX";
	public static final String REST_URL_PREFIX = "REST_URL_PREFIX";

	public String getDTOClassName(TableModel table) {
		return table != null ? getClassName(table) + getDTOClassNameSuffix(table) : null;
	}

	private String getDTOClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(table.getDataModel(), "DTO", ALTERNATE_DTO_CLASS_NAME_SUFFIX);
	}

	public String getDTOPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "rest.v1.dto", ALTERNATE_DTO_PACKAGE_NAME);
	}

	public String getDTOConverterClassName(TableModel table) {
		return table != null ? getClassName(table) + getDTOConverterNameSuffix(table) : null;
	}

	private String getDTOConverterNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "DTOConverter"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), AbstractClassCodeGenerator.MAPPERS)
						.filter(s -> s.toLowerCase().startsWith("mapstruct"))
						.map(s -> getDTOMapperInterfaceNameSuffix(table))
						.orElse("DTOConverter");
	}

	private String getDTOMapperInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"DTOMapper",
				ALTERNATE_DTOCONVERTER_CLASS_NAME_SUFFIX);
	}

	public String getDTOConverterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "rest.v1.converter", ALTERNATE_DTOCONVERTER_PACKAGE_NAME);
	}

	public String getListDTOClassName(TableModel table) {
		return table != null ? getClassName(table) + "List" + getDTOClassNameSuffix(table) : null;
	}

	public String getRESTControllerClassName(TableModel table) {
		return table != null ? getClassName(table) + getRESTControllerClassNameSuffix(table) : null;
	}

	private String getRESTControllerClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"RESTController",
				ALTERNATE_RESTCONTROLLER_CLASS_NAME_SUFFIX);
	}

	public String getRESTControllerPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "rest.v1", null);
	}

	public String getURLName(DataModel model, TableModel table) {
		String prefix = "api/v1";
		prefix = getPrefix(prefix, model.getOptionByName(REST_URL_PREFIX));
		prefix = getPrefix(prefix, table.getOptionByName(REST_URL_PREFIX));
		return prefix + "/" + getAttributeName(getPluralName(table)).toLowerCase();
	}

	private String getPrefix(String prefix, OptionModel option) {
		if (option != null) {
			return option.getParameter();
		}
		return prefix;
	}

}