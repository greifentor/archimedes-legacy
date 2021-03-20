package archimedes.codegenerators.restcontroller;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * Specilization of the name generator for REST controllers.
 *
 * @author ollie (13.03.2021)
 */
public class RESTControllerNameGenerator extends NameGenerator {

	public static final String REST_URL_PREFIX = "REST_URL_PREFIX";

	public String getDTOClassName(TableModel table) {
		return table != null ? getClassName(table) + "DTO" : null;
	}

	public String getDTOPackageName(DataModel model) {
		return model != null ? getBasePackageNameWithDotExtension(model) + "rest.dto" : null;
	}

	public String getDTOConverterClassName(TableModel table) {
		return table != null ? getClassName(table) + "DTOConverter" : null;
	}

	public String getDTOConverterPackageName(DataModel model) {
		return model != null ? getBasePackageNameWithDotExtension(model) + "rest.converter" : null;
	}

	public String getListDTOClassName(TableModel table) {
		return table != null ? getClassName(table) + "ListDTO" : null;
	}

	public String getRESTControllerClassName(TableModel table) {
		return table != null ? getClassName(table) + "RESTController" : null;
	}

	public String getRESTControllerPackageName(DataModel model) {
		return model != null ? getBasePackageNameWithDotExtension(model) + "rest" : null;
	}

	public String getURLName(DataModel model, TableModel table) {
		String prefix = "api/v1";
		prefix = getPrefix(prefix, model.getOptionByName(REST_URL_PREFIX));
		prefix = getPrefix(prefix, table.getOptionByName(REST_URL_PREFIX));
		return prefix + "/" + getPluralName(getAttributeName(table.getName())).toLowerCase();
	}

	private String getPrefix(String prefix, OptionModel option) {
		if (option != null) {
			return option.getParameter();
		}
		return prefix;
	}

}