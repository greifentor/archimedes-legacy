package archimedes.codegenerators.persistence.jpa;

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
public class PersistenceJPACodeFactory extends AbstractClassCodeFactory
		implements CodeFactoryProgressionEventProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_FOLDER_PATH = AbstractCodeFactory.TEMPLATE_PATH + System
			.getProperty(PersistenceJPACodeFactory.class.getSimpleName() + ".templates.folder", "/persistence-jpa");

	@Override
	protected List<CodeGenerator> getCodeGenerators() {
		return Arrays.asList(new DBOClassCodeGenerator(this));
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

}