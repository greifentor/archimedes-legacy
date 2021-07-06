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

	public static final String ALTERNATE_MODEL_CLASS_NAME_SUFFIX = "ALTERNATE_MODEL_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MODEL_PACKAGE_NAME = "ALTERNATE_MODEL_PACKAGE_NAME";
	public static final String ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX =
			"ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX";
	public static final String ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME = "ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME";
	public static final String ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX = "ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_SERVICE_IMPL_PACKAGE_NAME = "ALTERNATE_SERVICE_IMPL_PACKAGE_NAME";
	public static final String ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX = "ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX";
	public static final String ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME = "ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME";

	public String getIdModelClassName(TableModel table) {
		return table != null
				? getClassName(table) + "Id"
				: null;
	}

	public String getServiceInterfaceName(TableModel table) {
		return table != null
				? getClassName(table) + getServiceInterfaceNameSuffix(table)
				: null;
	}

	private String getServiceInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(table, "Service", ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX);
	}

	public String getServiceImplClassName(TableModel table) {
		return table != null
				? getClassName(table) + getServiceImplClassNameSuffix(table)
				: null;
	}

	private String getServiceImplClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(table, "ServiceImpl", ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX);
	}

	public String getServiceImplPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.service.impl", ALTERNATE_SERVICE_IMPL_PACKAGE_NAME);
	}

	public String getServiceInterfacePackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.service", ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME);
	}

	public String getModelClassName(TableModel table) {
		return table != null
				? getClassName(table) + getModelClassNameSuffix(table)
				: null;
	}

	private String getModelClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(table, "", ALTERNATE_MODEL_CLASS_NAME_SUFFIX);
	}

	public String getModelPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.model", ALTERNATE_MODEL_PACKAGE_NAME);
	}

	public String getPersistencePortInterfaceName(TableModel table) {
		return table != null
				? getClassName(table) + getPersistencePortInterfaceNameSuffix(table)
				: null;
	}

	private String getPersistencePortInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table,
				"PersistencePort",
				ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX);
	}

	public String getPersistencePortPackageName(DataModel model, TableModel table) {
		return createPackageName(
				model,
				table,
				"core.service.port.persistence",
				ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME);
	}

}