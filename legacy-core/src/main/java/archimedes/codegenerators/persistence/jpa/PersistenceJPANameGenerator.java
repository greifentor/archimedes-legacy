package archimedes.codegenerators.persistence.jpa;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.OptionGetter;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A specialized name generator for persistence JPA names.
 *
 * @author ollie (13.03.2021)
 */
public class PersistenceJPANameGenerator extends NameGenerator {

	public static final String ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX = "ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_ADAPTER_PACKAGE_NAME = "ALTERNATE_ADAPTER_PACKAGE_NAME";
	public static final String ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX = "ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_DBOCONVERTER_PACKAGE_NAME = "ALTERNATE_DBOCONVERTER_PACKAGE_NAME";
	public static final String ALTERNATE_ENTITY_CLASS_NAME_SUFFIX = "ALTERNATE_ENTITY_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_ENTITY_PACKAGE_NAME = "ALTERNATE_ENTITY_PACKAGE_NAME";
	public static final String ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX = "ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX";
	public static final String ALTERNATE_REPOSITORY_PACKAGE_NAME = "ALTERNATE_REPOSITORY_PACKAGE_NAME";

	public String getDBOClassName(TableModel table) {
		return table != null ? getClassName(table) + getDBOClassNameSuffix(table) : null;
	}

	private String getDBOClassNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "DBO"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_ENTITY_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("DBO");
	}

	public String getDBOConverterClassName(TableModel table) {
		return table != null ? getClassName(table) + getDBOConverterNameSuffix(table) : null;
	}

	private String getDBOConverterNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "DBOConverter"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), AbstractClassCodeGenerator.MAPPERS)
						.filter(s -> s.toLowerCase().startsWith("mapstruct"))
						.map(s -> getDBOMapperInterfaceNameSuffix(table))
						.orElse("DBOConverter");
	}

	private String getDBOMapperInterfaceNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "DBOMapper"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("DBOMapper");
	}

	public String getDBOConverterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.converter", ALTERNATE_DBOCONVERTER_PACKAGE_NAME);
	}

	public String getDBOPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.entity", ALTERNATE_ENTITY_PACKAGE_NAME);
	}

	public String getJPAPersistenceAdapterClassName(TableModel table) {
		return table != null ? getClassName(table) + getJPAPersistenceAdapterClassNameSuffix(table) : null;
	}

	private String getJPAPersistenceAdapterClassNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "JPAPersistenceAdapter"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("JPAPersistenceAdapter");
	}

	public String getJPAPersistenceAdapterPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence", ALTERNATE_ADAPTER_PACKAGE_NAME);
	}

	public String getJPARepositoryInterfaceName(TableModel table) {
		return table != null ? getClassName(table) + getJPARepositoryInterfaceNameSuffix(table) : null;
	}

	private String getJPARepositoryInterfaceNameSuffix(TableModel table) {
		return table.getDataModel() == null
				? "DBORepository"
				: OptionGetter
						.getParameterOfOptionByName(table.getDataModel(), ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX)
						.map(s -> s)
						.orElse("DBORepository");
	}

	public String getJPARepositoryPackageName(DataModel model, TableModel table) {
		return createPackageName(model, table, "persistence.repository", ALTERNATE_REPOSITORY_PACKAGE_NAME);
	}

}