package archimedes.codegenerators.service;

import java.util.Arrays;
import java.util.List;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CodeGenerator;
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
	protected List<CodeGenerator> getCodeGenerators() {
		return Arrays
				.asList(
						new ModelClassCodeGenerator(this),
						new PageClassCodeGenerator(this),
						new PageParametersClassCodeGenerator(this),
						new PersistencePortInterfaceCodeGenerator(this),
						new ServiceImplClassCodeGenerator(this),
						new ServiceInterfaceCodeGenerator(this));
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
			return new String[] { AbstractClassCodeGenerator.AUTOINCREMENT };
		case MODEL:
			return new String[] {
					ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_MODEL_PACKAGE_NAME,
					ServiceNameGenerator.ALTERNATE_PAGE_PACKAGE_NAME,
					ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME,
					ServiceNameGenerator.ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_SERVICE_IMPL_PACKAGE_NAME,
					ServiceNameGenerator.ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME };
		case TABLE:
			return new String[] {
					AbstractClassCodeGenerator.GENERATE_ID_CLASS,
					NameGenerator.MODULE,
					AbstractClassCodeGenerator.POJO_MODE };
		default:
			return new String[0];
		}
	}

}