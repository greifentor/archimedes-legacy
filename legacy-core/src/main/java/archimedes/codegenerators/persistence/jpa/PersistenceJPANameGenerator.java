package archimedes.codegenerators.persistence.jpa;

import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.OptionGetter;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A specialized name generator for persistence JPA names.
 *
 * @author ollie (13.03.2021)
 */
public class PersistenceJPANameGenerator extends NameGenerator {

	public static final String ALTERNATE_ENTITIES_PACKAGE_NAME = "ALTERNATE_ENTITIES_PACKAGE_NAME";
	public static final String MODULE = "MODULE";

	public String getDBOClassName(TableModel table) {
		return table != null ? getClassName(table) + "DBO" : null;
	}

	public String getDBOPackageName(DataModel model, TableModel table) {
		String packageName = "persistence.entities";
		String prefix = "";
		if (model != null) {
			OptionModel option = model.getOptionByName(ALTERNATE_ENTITIES_PACKAGE_NAME);
			if ((option != null) && (option.getParameter() != null) && !option.getParameter().isEmpty()) {
				packageName = option.getParameter();
			}
		}
		if (table != null) {
			prefix = OptionGetter.getOptionByName(table, MODULE).map(option -> option.getParameter() + ".").orElse("");
		}
		return model != null ? getBasePackageNameWithDotExtension(model, table) + prefix + packageName : null;
	}

}