package archimedes.codegenerators.persistence.jpa;

import java.io.File;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.acf.checker.ModelChecker;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.gui.checker.ModelCheckerMessageListFrameListener;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.model.TableModel;

/**
 * A code factory for JPA persistence ports and adapters for CRUD operations.
 *
 * @author ollie (03.03.2021)
 */
public class PersistenceJPACodeFactory extends AbstractCodeFactory
		implements CodeFactoryProgressionEventProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_PATH = "src/main/resources/templates/persistence-jpa";

	private static final Logger LOG = LogManager.getLogger(PersistenceJPACodeFactory.class);

	@Override
	public boolean generate(String path) {
		LOG.info("Started code generation");
		new File(path).mkdirs();
		String basePackageName = this.dataModel.getBasePackageName();
		for (TableModel tableModel : dataModel.getTables()) {
			if (tableModel.isGenerateCode()) {
				new DBOClassCodeGenerator().generate(basePackageName, dataModel, tableModel);
			}
		}
		return false;
	}

	@Override
	public ModelChecker[] getModelCheckers() {
		return new ModelChecker[] {};
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
	public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... listeners) {
		// NOP
	}

}