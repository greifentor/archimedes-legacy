package archimedes.codegenerators.gui.vaadin.cube;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;

/**
 * A name generator for service layer classes.
 *
 * @author ollie (15.03.2021)
 */
public class CubeNameGenerator extends NameGenerator {

	public static final CubeNameGenerator INSTANCE = new CubeNameGenerator();

	public static final String ALTERNATE_ACCESS_CHECKER_INTERFACE_NAME = "ALTERNATE_ACCESS_CHECKER_INTERFACE_NAME";
	public static final String ALTERNATE_ACCESS_CHECKER_PACKAGE_NAME = "ALTERNATE_ACCESS_CHECKER_PACKAGE_NAME";
	public static final String ALTERNATE_AUTHORIZATION_DATA_CLASS_NAME = "ALTERNATE_AUTHORIZATION_DATA_CLASS_NAME";
	public static final String ALTERNATE_JWT_SERVICE_INTERFACE_NAME = "ALTERNATE_JWT_SERVICE_INTERFACE_NAME";
	public static final String ALTERNATE_WEB_APP_CONFIGURATION_CLASS_NAME =
			"ALTERNATE_WEB_APP_CONFIGURATION_CLASS_NAME";

	public String getAccessCheckerInterfaceName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "AccessChecker", ALTERNATE_ACCESS_CHECKER_INTERFACE_NAME);
	}

	public String getAccessCheckerPackageName(DataModel model) {
		return createPackageName(model, null, "gui", ALTERNATE_ACCESS_CHECKER_PACKAGE_NAME);
	}

	public String getAuthorizationDataClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "AuthorizationData", ALTERNATE_AUTHORIZATION_DATA_CLASS_NAME);
	}

	public String getJWTServiceInterfaceName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(model, "JWTService", ALTERNATE_JWT_SERVICE_INTERFACE_NAME);
	}

	public String getWebAppConfigurationClassName(DataModel model) {
		return model == null
				? null
				: getNameOrAlternativeFromOption(
						model,
						"WebAppConfiguration",
						ALTERNATE_WEB_APP_CONFIGURATION_CLASS_NAME);
	}

}