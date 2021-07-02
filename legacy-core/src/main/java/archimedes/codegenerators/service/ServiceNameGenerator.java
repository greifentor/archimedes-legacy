package archimedes.codegenerators.service;

import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.OptionGetter;
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

	public String getIdModelClassName(TableModel table) {
		return table != null ? getClassName(table) + "Id" : null;
	}

	public String getServiceClassName(TableModel table) {
		return table != null ? getClassName(table) + "Service" : null;
	}

	public String getServicePackageName(DataModel model, TableModel table) {
		if (model == null) {
			return null;
		}
		String basePackageName = getBasePackageNameWithDotExtension(model, table);
		String packageName = "core";
		return basePackageName + packageName;
	}

	public String getModelClassName(TableModel table) {
		return table != null ? getClassName(table) + getModelClassNameSuffix(table) : null;
	}

	private String getModelClassNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? ""
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_MODEL_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("");
	}

	public String getModelPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.model", ALTERNATE_MODEL_PACKAGE_NAME);
	}

	public String getPersistencePortInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + getPersistencePortInterfaceNameSuffix(table) : null;
	}

	private String getPersistencePortInterfaceNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "PersistencePort"
				: OptionGetter
						.getParameterOfOptionByName(
								table.getDataModel(),
								ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX)
						.map(s -> s)
						.orElse("PersistencePort");
	}

	public String getPersistencePortPackageName(DataModel model, TableModel table) {
		return createPackageName(
				model,
				table,
				"core.service.port.persistence",
				ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME);
	}

}