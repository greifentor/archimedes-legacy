package archimedes.codegenerators.localization;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A name generator for localization classes in the service layer.
 *
 * @author ollie (07.01.2022)
 */
public class LocalizationNameGenerator extends NameGenerator {

	public static final String ALTERNATE_LOCALIZATION_SO_PACKAGE_NAME = "ALTERNATE_LOCALIZATION_SO_PACKAGE_NAME";
	public static final String ALTERNATE_RESOURCE_MANAGER_PACKAGE_NAME = "ALTERNATE_RESOURCE_MANAGER_PACKAGE_NAME";
	public static final String ALTERNATE_RESOURCE_MANAGER_IMPL_PACKAGE_NAME =
			"ALTERNATE_RESOURCE_MANAGER_IMPL_PACKAGE_NAME";

	public String getFileBasedResourceManagerConfigurationClassName() {
		return "FileBasedResourceManagerConfiguration";
	}

	public String getFileBasedResourceManagerImplClassName() {
		return "FileBasedResourceManagerImpl";
	}

	public String getLocalizationSOClassName() {
		return "LocalizationSO";
	}

	public String getLocalizationSOPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.model.localization", ALTERNATE_LOCALIZATION_SO_PACKAGE_NAME);
	}

	public String getResourceManagerImplPackageName(DataModel model, TableModel table) {
		return createPackageName(
				model,
				table,
				"core.service.impl.localization",
				ALTERNATE_RESOURCE_MANAGER_IMPL_PACKAGE_NAME);
	}

	public String getResourceManagerInterfaceName() {
		return "ResourceManager";
	}

	public String getResourceManagerPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.service.localization", ALTERNATE_RESOURCE_MANAGER_PACKAGE_NAME);
	}

}