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

	public static final String ALTERNATE_ADAPTER_PACKAGE_NAME = "ALTERNATE_ADAPTER_PACKAGE_NAME";
	public static final String ALTERNATE_CONVERTER_PACKAGE_NAME = "ALTERNATE_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_ENTITY_PACKAGE_NAME = "ALTERNATE_ENTITY_PACKAGE_NAME";
	public static final String ALTERNATE_REPOSITORY_PACKAGE_NAME = "ALTERNATE_REPOSITORY_PACKAGE_NAME";

	public String getDBOClassName(TableModel table) {
		return table != null ? getClassName(table) + "DBO" : null;
	}

	public String getDBOConverterClassName(TableModel table) {
		return table != null ? getClassName(table) + "DBOConverter" : null;
	}

	public String getDBOConverterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.converter", ALTERNATE_CONVERTER_PACKAGE_NAME);
	}

	public String getDBOPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.entity", ALTERNATE_ENTITY_PACKAGE_NAME);
	}

	public String getJPAPersistenceAdapterClassName(TableModel table) {
		return table != null ? getClassName(table) + "JPAPersistenceAdapter" : null;
	}

	public String getJPAPersistenceAdapterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence", ALTERNATE_ADAPTER_PACKAGE_NAME);
	}

	public String getJPARepositoryInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + "DBORepository" : null;
	}

	public String getJPARepositoryPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.repository", ALTERNATE_REPOSITORY_PACKAGE_NAME);
	}

}