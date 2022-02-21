package archimedes.codegenerators.service;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;

/**
 * A name generator for service layer classes.
 *
 * @author ollie (15.03.2021)
 */
public class ServiceNameGenerator extends NameGenerator {

	public static final String ALTERNATE_APPLICATION_PACKAGE_NAME = "ALTERNATE_APPLICATION_PACKAGE_NAME";
	public static final String ALTERNATE_EXCEPTIONS_PACKAGE_NAME = "ALTERNATE_EXCEPTIONS_PACKAGE_NAME";
	public static final String ALTERNATE_GENERATED_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX =
			"ALTERNATE_GENERATED_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX";
	public static final String ALTERNATE_GENERATED_SERVICE_IMPL_CLASS_NAME_SUFFIX =
			"ALTERNATE_GENERATED_SERVICE_IMPL_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_GENERATED_SERVICE_INTERFACE_NAME_SUFFIX =
			"ALTERNATE_GENERATED_SERVICE_INTERFACE_NAME_SUFFIX";
	public static final String ALTERNATE_MODEL_CLASS_NAME_SUFFIX = "ALTERNATE_MODEL_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_MODEL_PACKAGE_NAME = "ALTERNATE_MODEL_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_PACKAGE_NAME = "ALTERNATE_PAGE_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_PARAMETERS_PACKAGE_NAME = "ALTERNATE_PAGE_PARAMETERS_PACKAGE_NAME";
	public static final String ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX =
			"ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX";
	public static final String ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME = "ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME";
	public static final String ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX = "ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_SERVICE_IMPL_PACKAGE_NAME = "ALTERNATE_SERVICE_IMPL_PACKAGE_NAME";
	public static final String ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX = "ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX";
	public static final String ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME = "ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME";
	public static final String ALTERNATE_UTIL_PACKAGE_NAME = "ALTERNATE_UTILITIES_PACKAGE_NAME";

	public String getApplicationClassName(DataModel model) {
		return model != null
				? (model.getApplicationName() != null ? deleteNonLetterCharacters(model.getApplicationName()) : "") + "Application"
				: null;
	}

	private String deleteNonLetterCharacters(String s) {
		for (char c = 10; c <= 255; c++) {
			if (((c < 'a') || (c > 'z')) && ((c < 'A') || (c > 'Z'))) {
				s = s.replace("" + c, "");
			}
		}
		return s;
	}

	public String getApplicationPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "", ALTERNATE_APPLICATION_PACKAGE_NAME);
	}

	public String getExceptionsPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.service.exception", ALTERNATE_EXCEPTIONS_PACKAGE_NAME);
	}

	public String getGeneratedPersistencePortInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + getGeneratedPersistencePortInterfaceNameSuffix(table) : null;
	}

	private String getGeneratedPersistencePortInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"GeneratedPersistencePort",
				ALTERNATE_GENERATED_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX);
	}

	public String getGeneratedServiceInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + getGeneratedServiceInterfaceNameSuffix(table) : null;
	}

	private String getGeneratedServiceInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"GeneratedService",
				ALTERNATE_GENERATED_SERVICE_INTERFACE_NAME_SUFFIX);
	}

	public String getGeneratedServiceImplClassName(TableModel table) {
		return table != null ? getClassName(table) + getGeneratedServiceImplClassNameSuffix(table) : null;
	}

	private String getGeneratedServiceImplClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"GeneratedServiceImpl",
				ALTERNATE_GENERATED_SERVICE_IMPL_CLASS_NAME_SUFFIX);
	}

	public String getIdModelClassName(TableModel table) {
		return table != null
				? getClassName(table) + "Id"
				: null;
	}

	public String getModelClassName(DomainModel domain, DataModel model) {
		return domain != null ? getClassName(domain.getName()) + getModelClassNameSuffix(model) : null;
	}

	public String getModelClassName(TableModel table) {
		return table != null
				? getClassName(table) + getModelClassNameSuffix(table.getDataModel())
				: null;
	}

	private String getModelClassNameSuffix(DataModel model) {
		return getNameOrAlternativeFromOption(model, "", ALTERNATE_MODEL_CLASS_NAME_SUFFIX);
	}

	public String getModelPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.model", ALTERNATE_MODEL_PACKAGE_NAME);
	}

	public String getPageClassName() {
		return "Page";
	}

	public String getPagePackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.model", ALTERNATE_PAGE_PACKAGE_NAME);
	}

	public String getPageParametersClassName() {
		return "PageParameters";
	}

	public String getPageParametersPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.model", ALTERNATE_PAGE_PARAMETERS_PACKAGE_NAME);
	}

	public String getPersistencePortInterfaceName(TableModel table) {
		return table != null
				? getClassName(table) + getPersistencePortInterfaceNameSuffix(table)
				: null;
	}

	private String getPersistencePortInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
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

	public String getServiceInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + getServiceInterfaceNameSuffix(table) : null;
	}

	private String getServiceInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(table.getDataModel(), "Service", ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX);
	}

	public String getServiceImplClassName(TableModel table) {
		return table != null ? getClassName(table) + getServiceImplClassNameSuffix(table) : null;
	}

	private String getServiceImplClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"ServiceImpl",
				ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX);
	}

	public String getServiceImplPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.service.impl", ALTERNATE_SERVICE_IMPL_PACKAGE_NAME);
	}

	public String getServiceInterfacePackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "core.service", ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME);
	}

	public String getUtilPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "util", ALTERNATE_UTIL_PACKAGE_NAME);
	}

}