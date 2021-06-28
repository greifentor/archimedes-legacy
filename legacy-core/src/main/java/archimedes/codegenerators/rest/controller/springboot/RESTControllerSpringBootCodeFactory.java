package archimedes.codegenerators.rest.controller.springboot;

import java.util.Arrays;
import java.util.List;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CodeGenerator;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;

/**
 * A code factory for JPA persistence ports and adapters for CRUD operations.
 *
 * @author ollie (03.03.2021)
 */
public class RESTControllerSpringBootCodeFactory extends AbstractClassCodeFactory
		implements CodeFactoryProgressionEventProvider, StandardCodeFactoryProgressionFrameUser {

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

}