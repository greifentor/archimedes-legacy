package archimedes.codegenerators.persistence.jpa;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.OptionGetter;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.OptionListProvider;
import archimedes.model.TableModel;

/**
 * A specialized name generator for persistence JPA names.
 *
 * @author ollie (13.03.2021)
 */
public class PersistenceJPANameGenerator extends NameGenerator {

	public static final PersistenceJPANameGenerator INSTANCE = new PersistenceJPANameGenerator();

	public static final String ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX = "ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_ADAPTER_PACKAGE_NAME = "ALTERNATE_ADAPTER_PACKAGE_NAME";
	public static final String ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX = "ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_DBOCONVERTER_PACKAGE_NAME = "ALTERNATE_DBOCONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_ENTITY_CLASS_NAME_SUFFIX = "ALTERNATE_ENTITY_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_ENTITY_PACKAGE_NAME = "ALTERNATE_ENTITY_PACKAGE_NAME";
	public static final String ALTERNATE_GENERATED_ADAPTER_CLASS_NAME_SUFFIX =
			"ALTERNATE_GENERATED_ADAPTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_PAGE_CONVERTER_PACKAGE_NAME = "ALTERNATE_PAGE_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_MODEL_PACKAGE_NAME = "ALTERNATE_PAGE_MODEL_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_PARAMETERS_CONVERTER_PACKAGE_NAME =
			"ALTERNATE_PAGE_PARAMETERS_CONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_PAGE_PARAMETERS_MODEL_PACKAGE_NAME =
			"ALTERNATE_PAGE_PARAMETERS_MODEL_PACKAGE_NAME";
	public static final String ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX = "ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_REPOSITORY_PACKAGE_NAME = "ALTERNATE_REPOSITORY_PACKAGE_NAME";
	public static final String ALTERNATE_TO_DBO_METHOD_NAME = "ALTERNATE_TO_DBO_METHOD_NAME";
	public static final String ALTERNATE_TO_MODEL_METHOD_NAME = "ALTERNATE_TO_MODEL_METHOD_NAME";

	public String getDBOClassName(TableModel table) {
		return table != null ? getClassName(table) + getDBOClassNameSuffix(table.getDataModel()) : null;
	}

	public String getDBOClassName(DomainModel domain, DataModel model) {
		return domain != null ? getClassName(domain.getName()) + getDBOClassNameSuffix(model) : null;
	}

	private String getDBOClassNameSuffix(DataModel model) {
		return getNameOrAlternativeFromOption(model, "DBO", ALTERNATE_ENTITY_CLASS_NAME_SUFFIX);
	}

	public String getDBOConverterClassName(String tableName, DataModel model) {
		return tableName != null ? getClassName(tableName) + getDBOConverterNameSuffix(model) : null;
	}

	private String getDBOConverterNameSuffix(DataModel model) {
		return model == null
				? "DBOConverter"
				: OptionGetter
						.getParameterOfOptionByName(model, AbstractClassCodeGenerator.MAPPERS)
						.filter(s -> s.toLowerCase().startsWith("mapstruct"))
						.map(s -> getDBOMapperInterfaceNameSuffix(model))
						.orElse("DBOConverter");
	}

	private String getDBOMapperInterfaceNameSuffix(DataModel model) {
		return getNameOrAlternativeFromOption(model, "DBOMapper", ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX);
	}

	public String getDBOConverterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.converter", ALTERNATE_DBOCONVERTER_PACKAGE_NAME);
	}

	public String getDBOConverterPackageName(DataModel model, DomainModel domain) {
		return createPackageName(model, domain, "persistence.converter", ALTERNATE_DBOCONVERTER_PACKAGE_NAME);
	}

	public String getDBOPackageName(DataModel model, OptionListProvider options) {
		return createPackageName(model, options, "persistence.entity", ALTERNATE_ENTITY_PACKAGE_NAME);
	}

	public String getGeneratedJPAPersistenceAdapterClassName(TableModel table) {
		return table != null ? getClassName(table) + getGeneratedJPAPersistenceAdapterClassNameSuffix(table) : null;
	}

	private String getGeneratedJPAPersistenceAdapterClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"GeneratedJPAPersistenceAdapter",
				ALTERNATE_GENERATED_ADAPTER_CLASS_NAME_SUFFIX);
	}

	public String getGeneratedJPARepositoryInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + getGeneratedJPARepositoryInterfaceNameSuffix(table) : null;
	}

	private String getGeneratedJPARepositoryInterfaceNameSuffix(TableModel table) {
		return "Generated" + getNameOrAlternativeFromOption(
				table.getDataModel(),
				"DBORepository",
				ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX);
	}

	public String getGeneratedJPARepositoryPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.repository", ALTERNATE_REPOSITORY_PACKAGE_NAME);
	}

	public String getJPAPersistenceAdapterClassName(TableModel table) {
		return table != null ? getClassName(table) + getJPAPersistenceAdapterClassNameSuffix(table) : null;
	}

	private String getJPAPersistenceAdapterClassNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"JPAPersistenceAdapter",
				ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX);
	}

	public String getJPAPersistenceAdapterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence", ALTERNATE_ADAPTER_PACKAGE_NAME);
	}

	public String getJPARepositoryInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + getJPARepositoryInterfaceNameSuffix(table) : null;
	}

	private String getJPARepositoryInterfaceNameSuffix(TableModel table) {
		return getNameOrAlternativeFromOption(
				table.getDataModel(),
				"DBORepository",
				ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX);
	}

	public String getJPARepositoryPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.repository", ALTERNATE_REPOSITORY_PACKAGE_NAME);
	}

	public String getPageConverterClassName() {
		return "PageConverter";
	}

	public String getPageConverterPackageName(DataModel model) {
		return createPackageName(model, null, "persistence.converter", ALTERNATE_PAGE_CONVERTER_PACKAGE_NAME);
	}

	public String getPageModelClassName() {
		return "Page";
	}

	public String getPageModelPackageName(DataModel model) {
		return createPackageName(model, null, "core.model", ALTERNATE_PAGE_MODEL_PACKAGE_NAME);
	}

	public String getPageParametersModelClassName() {
		return "PageParameters";
	}

	public String getPageParametersModelPackageName(DataModel model) {
		return createPackageName(model, null, "core.model", ALTERNATE_PAGE_PARAMETERS_MODEL_PACKAGE_NAME);
	}

	public String getPageParametersToPageableConverterClassName() {
		return "PageParametersToPageableConverter";
	}

	public String getPageParametersToPageableConverterPackageName(DataModel model) {
		return createPackageName(
				model,
		        null,
				"persistence.converter",
				ALTERNATE_PAGE_PARAMETERS_CONVERTER_PACKAGE_NAME);
	}

	public String getToModelConverterInterfaceName() {
		return "ToModelConverter";
	}

	public String getToDBOMethodName(TableModel table) {
		return table == null ? null : getToDBOMethodName(table.getDataModel());
	}

	public String getToDBOMethodName(DataModel model) {
		return getNameOrAlternativeFromOption(model != null ? model : null, "toDBO", ALTERNATE_TO_DBO_METHOD_NAME);
	}

	public String getToModelMethodName(TableModel table) {
		return table == null ? null : getToModelMethodName(table.getDataModel());
	}

	public String getToModelMethodName(DataModel model) {
		return getNameOrAlternativeFromOption(model != null ? model : null, "toModel", ALTERNATE_TO_MODEL_METHOD_NAME);
	}

}