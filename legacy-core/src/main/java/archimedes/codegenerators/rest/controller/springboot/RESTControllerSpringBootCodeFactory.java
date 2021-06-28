package archimedes.codegenerators.rest.controller.springboot;

import java.util.Arrays;
import java.util.List;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CodeGenerator;
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
public class RESTControllerSpringBootCodeFactory extends AbstractClassCodeFactory
		implements CodeFactoryProgressionEventProvider, PredeterminedOptionProvider,
		StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_FOLDER_PATH = AbstractCodeFactory.TEMPLATE_PATH + System
			.getProperty(
					RESTControllerSpringBootCodeFactory.class.getSimpleName() + ".templates.folder",
					"/rest-controller-springboot");

	@Override
	protected List<CodeGenerator> getCodeGenerators() {
		return Arrays
				.asList(
						new DTOClassCodeGenerator(this),
						new DTOConverterClassCodeGenerator(this),
						new DTOMapstructMapperInterfaceCodeGenerator(this),
						new ListDTOClassCodeGenerator(this),
						new RESTControllerSpringBootClassCodeGenerator(this));
	}

	@Override
	public String getName() {
		return "REST Controller Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "archimedes", "restcontroller-code-factory" };
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

	@Override
	public String[] getSelectableOptions(OptionType optionType) {
		switch (optionType) {
		case MODEL:
			return new String[] {
					RESTControllerNameGenerator.ALTERNATE_DTO_CLASS_NAME_SUFFIX,
					RESTControllerNameGenerator.ALTERNATE_DTO_PACKAGE_NAME,
					RESTControllerNameGenerator.ALTERNATE_DTOMAPPER_CLASS_NAME_SUFFIX,
					RESTControllerNameGenerator.ALTERNATE_DTOMAPPER_PACKAGE_NAME,
					RESTControllerNameGenerator.ALTERNATE_RESTCONTROLLER_CLASS_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_SO_CLASS_NAME_SUFFIX,
					ServiceNameGenerator.ALTERNATE_SO_PACKAGE_NAME,
					AbstractClassCodeGenerator.ALTERNATE_MODULE_PREFIX,
					AbstractClassCodeGenerator.MAPPERS,
					AbstractClassCodeGenerator.MODULE_MODE,
					RESTControllerNameGenerator.REST_URL_PREFIX };
		case TABLE:
			return new String[] {
					RESTControllerNameGenerator.MODULE,
					NameGenerator.PLURAL_NAME,
					RESTControllerNameGenerator.REST_URL_PREFIX };
		default:
			return new String[0];
		}
	}

}