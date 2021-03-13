package archimedes.codegenerators.persistence.jpa;

import archimedes.codegenerators.NameGenerator;
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

}