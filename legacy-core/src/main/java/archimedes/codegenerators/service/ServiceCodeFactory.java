package archimedes.codegenerators.service;

import java.util.Arrays;
import java.util.List;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.CodeGenerator;
import archimedes.codegenerators.FindByUtils;
import archimedes.codegenerators.NameGenerator;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.model.OptionType;
import archimedes.model.PredeterminedOptionProvider;

/**
 * A code factory for services implementations and interfaces for CRUD operations.
 *
 * @author ollie (04.07.2021)
 */
public class ServiceCodeFactory extends AbstractClassCodeFactory implements CodeFactoryProgressionEventProvider,
		PredeterminedOptionProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_FOLDER_PATH = AbstractCodeFactory.TEMPLATE_PATH
			+ System.getProperty(ServiceCodeFactory.class.getSimpleName() + ".templates.folder", "/service");

	@Override
	protected List<CodeGenerator<?>> getCodeGenerators() {
		return Arrays
				.asList(
						// Domain
						new ModelEnumCodeGenerator(this),
						// Table
						new ApplicationClassCodeGenerator(this),
						new ApplicationPropertiesFileGenerator(this),
						new CheckClassCodeGenerator(this),
						new GeneratedModelClassCodeGenerator(this),
						new GeneratedPersistencePortInterfaceCodeGenerator(this),
						new GeneratedServiceImplClassCodeGenerator(this),
						new GeneratedServiceInterfaceCodeGenerator(this),
						new ModelClassCodeGenerator(this),
						new NotNullConstraintViolationExceptionClassCodeGenerator(this),
						new PageClassCodeGenerator(this),
						new PageParametersClassCodeGenerator(this),
						new PersistencePortInterfaceCodeGenerator(this),
						new ServiceImplClassCodeGenerator(this),
						new ServiceInterfaceCodeGenerator(this),
						new UniqueConstraintViolationExceptionClassCodeGenerator(this));
	}

	@Override
	public String getName() {
		return "Service Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "service-code-factory" };
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
					FindByUtils.FIND_BY,
					AbstractModelCodeGenerator.GLOBAL_ID,
					AbstractClassCodeGenerator.INIT_WITH,
					AbstractClassCodeGenerator.LIST_ACCESS };
		case DOMAIN:
			return new String[] { AbstractClassCodeGenerator.ENUM };
		case MODEL:
			return new String[] {
//					ServiceNameGenerator.ALTERNATE_APPLICATION_PACKAGE_NAME,
//					ServiceNameGenerator.ALTERNATE_EXCEPTIONS_PACKAGE_NAME,
//					ServiceNameGenerator.ALTERNATE_GENERATED_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX,
//					ServiceNameGenerator.ALTERNATE_GENERATED_SERVICE_IMPL_CLASS_NAME_SUFFIX,
//					ServiceNameGenerator.ALTERNATE_GENERATED_SERVICE_INTERFACE_NAME_SUFFIX,
					AbstractModelCodeGenerator.GLOBAL_ID };
//					ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX,
//					ServiceNameGenerator.ALTERNATE_MODEL_PACKAGE_NAME,
//					ServiceNameGenerator.ALTERNATE_PAGE_PACKAGE_NAME,
//					ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX,
//					ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME,
//					ServiceNameGenerator.ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX,
//					ServiceNameGenerator.ALTERNATE_SERVICE_IMPL_PACKAGE_NAME,
//					ServiceNameGenerator.ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX,
//					ServiceNameGenerator.ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME };
		case TABLE:
			return new String[] {
					AbstractClassCodeGenerator.GENERATE_ID_CLASS,
					AbstractModelCodeGenerator.GLOBAL_ID,
					ModelClassCodeGenerator.IMPLEMENTS,
					NameGenerator.MODULE,
					AbstractClassCodeFactory.NO_GENERATION,
					AbstractClassCodeGenerator.POJO_MODE };
		default:
			return new String[0];
		}
	}

}