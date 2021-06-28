package archimedes.codegenerators.service;

import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.OptionGetter;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A name generator for service layer classes.
 *
 * @author ollie (15.03.2021)
 */
public class ServiceNameGenerator extends NameGenerator {

	public static final String ALTERNATE_SO_CLASS_NAME_SUFFIX = "ALTERNATE_SO_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_SO_PACKAGE_NAME = "ALTERNATE_SO_PACKAGE_NAME";

	public String getIdSOClassName(TableModel table) {
		return table != null ? getClassName(table) + "IdSO" : null;
	}

	public String getServiceClassName(TableModel table) {
		return table != null ? getClassName(table) + "Service" : null;
	}

	public String getServicePackageName(DataModel model, TableModel table) {
		if (model == null) {
			return null;
		}
		String basePackageName = getBasePackageNameWithDotExtension(model, table);
		String packageName = "service";
		return basePackageName + packageName;
	}

	public String getSOClassName(TableModel table) {
		return table != null ? getClassName(table) + getSOClassNameSuffix(table) : null;
	}

	private String getSOClassNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "SO"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_SO_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("SO");
	}

	public String getSOPackageName(DataModel model, TableModel table) {
		if (model == null) {
			return null;
		}
		String basePackageName = getBasePackageNameWithDotExtension(model, table);
		OptionModel optionAlternatePackageNameForSOClasses = model.getOptionByName(ALTERNATE_SO_PACKAGE_NAME);
		String packageName = "service.model";
		if (optionAlternatePackageNameForSOClasses != null) {
			packageName = optionAlternatePackageNameForSOClasses.getParameter();
		}
		return basePackageName + packageName;
	}

}