package archimedes.codegenerators.restcontroller;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * Specilization of the name generator for REST controllers.
 *
 * @author ollie (13.03.2021)
 */
public class RESTControllerNameGenerator extends NameGenerator {

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

}