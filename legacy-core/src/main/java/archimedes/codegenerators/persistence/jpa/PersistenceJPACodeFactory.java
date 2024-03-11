package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;
import java.util.List;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.CodeGenerator;
import archimedes.codegenerators.FindByUtils;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.model.OptionType;
import archimedes.model.PredeterminedOptionProvider;

/**
 * A code factory for JPA persistence ports and adapters for CRUD operations.
 *
 * @author ollie (03.03.2021)
 */
public class PersistenceJPACodeFactory extends AbstractClassCodeFactory implements CodeFactoryProgressionEventProvider,
		PredeterminedOptionProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_FOLDER_PATH =
			AbstractCodeFactory.TEMPLATE_PATH + System
					.getProperty(
							PersistenceJPACodeFactory.class.getSimpleName() + ".templates.folder",
							"/persistence-jpa");

	@Override
	protected List<CodeGenerator<?>> getCodeGenerators() {
		return Arrays
				.asList(
						// Domains
						new DBOEnumCodeGenerator(this),
						new DBOEnumConverterClassCodeGenerator(this),
						// Tables
						new DBOClassCodeGenerator(this),
						new DBOConverterClassCodeGenerator(this),
						new DBOMapstructMapperInterfaceCodeGenerator(this),
						new GeneratedJPAPersistenceAdapterClassCodeGenerator(this),
						new GeneratedJPARepositoryInterfaceCodeGenerator(this),
						new JPAPersistenceAdapterClassCodeGenerator(this),
						new JPAPersistenceAdapterDependentClassCodeGenerator(this),
						new JPARepositoryInterfaceCodeGenerator(this),
						new PageConverterClassCodeGenerator(this),
						new PageParametersToPageableConverterClassCodeGenerator(this),
						new ToModelConverterInterfaceCodeGenerator(this));
	}

	@Override
	public String getName() {
		return "Persistence JPA Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "persistence-jpa-code-factory" };
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String[] getSelectableOptions(OptionType optionType) {
		switch (optionType) {
		case COLUMN:
			return new String[] {
					AbstractClassCodeGenerator.AUTO_INCREMENT,
					JPAPersistenceAdapterDependentClassCodeGenerator.DEPENDENT_ATTRIBUTE,
					FindByUtils.FIND_BY,
					AbstractClassCodeGenerator.LIST_ACCESS };
		case DOMAIN:
			return new String[] { AbstractCodeGenerator.ENUM };
		case MODEL:
			return new String[] {
					PersistenceJPANameGenerator.ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX,
					PersistenceJPANameGenerator.ALTERNATE_ADAPTER_PACKAGE_NAME,
					PersistenceJPANameGenerator.ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX,
					PersistenceJPANameGenerator.ALTERNATE_DBOCONVERTER_PACKAGE_NAME,
					PersistenceJPANameGenerator.ALTERNATE_ENTITY_CLASS_NAME_SUFFIX,
					PersistenceJPANameGenerator.ALTERNATE_ENTITY_PACKAGE_NAME,
					ServiceNameGenerator.ALTERNATE_EXCEPTIONS_PACKAGE_NAME,
					PersistenceJPANameGenerator.ALTERNATE_GENERATED_ADAPTER_CLASS_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_MODEL_PACKAGE_NAME,
					PersistenceJPANameGenerator.ALTERNATE_PAGE_CONVERTER_PACKAGE_NAME,
					PersistenceJPANameGenerator.ALTERNATE_PAGE_MODEL_PACKAGE_NAME,
					PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_CONVERTER_PACKAGE_NAME,
					PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_MODEL_PACKAGE_NAME,
					ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME,
					PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX,
					PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_PACKAGE_NAME,
					AbstractClassCodeGenerator.ALTERNATE_MODULE_PREFIX,
					AbstractClassCodeGenerator.COMMENTS,
					AbstractClassCodeGenerator.GENERATE_ID_CLASS,
					AbstractClassCodeGenerator.JAVAX_PACKAGE_NAME,
			        AbstractCodeGenerator.MODULE_MODE,
					AbstractClassCodeGenerator.REFERENCE_MODE,
					PersistenceJPANameGenerator.ALTERNATE_TO_DBO_METHOD_NAME,
					PersistenceJPANameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME };
		case TABLE:
			return new String[] {
					AbstractClassCodeGenerator.GENERATE_ID_CLASS,
					NameGenerator.MODULE,
					AbstractClassCodeFactory.NO_GENERATION,
					AbstractClassCodeGenerator.POJO_MODE,
					AbstractClassCodeGenerator.REFERENCE_MODE,
					AbstractClassCodeGenerator.SUBCLASS,
					AbstractClassCodeGenerator.SUPERCLASS };
		default:
			return new String[0];
		}
	}

}