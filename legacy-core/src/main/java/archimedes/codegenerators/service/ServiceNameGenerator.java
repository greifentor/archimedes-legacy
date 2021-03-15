package archimedes.codegenerators.service;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A name generator for service layer classes.
 *
 * @author ollie (15.03.2021)
 */
public class ServiceNameGenerator extends NameGenerator {

	public String getSOClassName(TableModel table) {
		return table != null ? getClassName(table) + "SO" : null;
	}

	public String getSOPackageName(DataModel model) {
		return model != null ? getBasePackageNameWithDotExtension(model) + "service.model" : null;
	}

}