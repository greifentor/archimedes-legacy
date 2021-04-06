package archimedes.codegenerators.restclient;

import java.util.Arrays;
import java.util.List;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CodeGenerator;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;

/**
 * A code factory for a simple REST client which matches the REST controller generated by the
 * RESTControllerCodeFactory..
 *
 * @author ollie (01.04.2021)
 */
public class RESTClientCodeFactory extends AbstractClassCodeFactory
		implements CodeFactoryProgressionEventProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_FOLDER_PATH = AbstractCodeFactory.TEMPLATE_PATH
			+ System.getProperty(RESTClientCodeFactory.class.getSimpleName() + ".templates.folder", "/restclient");

	@Override
	protected List<CodeGenerator> getCodeGenerators() {
		return Arrays
				.asList(
						new DTOClassCodeGenerator(),
						new IdDTOClassCodeGenerator(),
						new ListDTOClassCodeGenerator(),
						new RESTClientClassCodeGenerator());
	}

	@Override
	public String getName() {
		return "REST Client Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "archimedes", "restclient-code-factory" };
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

}