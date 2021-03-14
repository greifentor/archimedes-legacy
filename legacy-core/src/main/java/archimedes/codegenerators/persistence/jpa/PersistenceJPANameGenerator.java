package archimedes.codegenerators.persistence.jpa;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A specialized name generator for persistence JPA names.
 *
 * @author ollie (13.03.2021)
 */
public class PersistenceJPANameGenerator extends NameGenerator {

	public String getDBOClassName(TableModel table) {
		return table != null ? getClassName(table) + "DBO" : null;
	}

	public String getDBOPackageName(DataModel model) {
		return model != null ? getBasePackageNameWithDotExtension(model) + "persistence.entities" : null;
	}

}