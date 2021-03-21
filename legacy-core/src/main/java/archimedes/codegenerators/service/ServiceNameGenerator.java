package archimedes.codegenerators.service;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A name generator for service layer classes.
 *
 * @author ollie (15.03.2021)
 */
public class ServiceNameGenerator extends NameGenerator {

	public static final String ALTERNATE_PACKAGE_SO_CLASS = "ALTERNATE_PACKAGE_SO_CLASS";

	public String getIdSOClassName(TableModel table) {
		return table != null ? getClassName(table) + "IdSO" : null;
	}

	public String getServiceClassName(TableModel table) {
		return table != null ? getClassName(table) + "Service" : null;
	}

	public String getServicePackageName(DataModel model) {
		if (model == null) {
			return null;
		}
		String basePackageName = getBasePackageNameWithDotExtension(model);
		String packageName = "service";
		return basePackageName + packageName;
	}

	public String getSOClassName(TableModel table) {
		return table != null ? getClassName(table) + "SO" : null;
	}

	public String getSOPackageName(DataModel model) {
		if (model == null) {
			return null;
		}
		String basePackageName = getBasePackageNameWithDotExtension(model);
		OptionModel optionAlternatePackageNameForSOClasses = model.getOptionByName(ALTERNATE_PACKAGE_SO_CLASS);
		String packageName = "service.model";
		if (optionAlternatePackageNameForSOClasses != null) {
			packageName = optionAlternatePackageNameForSOClasses.getParameter();
		}
		return basePackageName + packageName;
	}

}