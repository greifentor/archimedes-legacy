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
	public static final String ALTERNATE_DTOMAPPER_CLASS_NAME_SUFFIX = "ALTERNATE_DTOMAPPER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_DTOMAPPER_PACKAGE_NAME = "ALTERNATE_DTOMAPPER_PACKAGE_NAME";
	public static final String ALTERNATE_RESTCONTROLLER_CLASS_NAME_SUFFIX =
			"ALTERNATE_RESTCONTROLLER_CLASS_NAME_SUFFIX";
	public static final String REST_URL_PREFIX = "REST_URL_PREFIX";

	public String getDTOClassName(TableModel table) {
		return table != null ? getClassName(table) + getDTOClassNameSuffix(table) : null;
	}

	private String getDTOClassNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "DTO"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_DTO_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("DTO");
	}

	public String getDTOPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "rest.dto", ALTERNATE_DTO_PACKAGE_NAME);
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
		return table.getDataModel() == null
				? "DTOMapper"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_DTOMAPPER_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("DTOMapper");
	}

	public String getDTOConverterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "rest.converter", ALTERNATE_DTOMAPPER_PACKAGE_NAME);
	}

	public String getListDTOClassName(TableModel table) {
		return table != null ? getClassName(table) + "List" + getDTOClassNameSuffix(table) : null;
	}

	public String getRESTControllerClassName(TableModel table) {
		return table != null ? getClassName(table) + getRESTControllerClassNameSuffix(table) : null;
	}

	private String getRESTControllerClassNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "RESTController"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_RESTCONTROLLER_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("RESTController");
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