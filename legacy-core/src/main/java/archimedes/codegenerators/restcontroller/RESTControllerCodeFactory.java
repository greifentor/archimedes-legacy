package archimedes.codegenerators.restcontroller;

import java.io.File;
import java.io.FileWriter;

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
public class RESTControllerCodeFactory extends AbstractCodeFactory
		implements CodeFactoryProgressionEventProvider, StandardCodeFactoryProgressionFrameUser {

	public static final String TEMPLATE_PATH = System
			.getProperty("RestControllerCodeFactory.templates.path", "src/main/resources/templates/restcontroller");

	private static final Logger LOG = LogManager.getLogger(RESTControllerCodeFactory.class);

	private RESTControllerNameGenerator nameGenerator = new RESTControllerNameGenerator();

	@Override
	public boolean generate(String path) {
		LOG.info("Started code generation");
		new File(path).mkdirs();
		String basePackageName = this.dataModel.getBasePackageName();
		for (TableModel tableModel : dataModel.getTables()) {
			if (tableModel.isGenerateCode()) {
				String code = new DTOClassCodeGenerator().generate(basePackageName, tableModel);
				String pathName = path + "/src/main/" + nameGenerator.getDTOPackageName(dataModel).replace(".", "/");
				File packagePath = new File(pathName);
				if (!packagePath.exists()) {
					packagePath.mkdirs();
				}
				String fileName = pathName + "/" + nameGenerator.getDTOClassName(tableModel) + ".java";
				try (FileWriter writer = new FileWriter(fileName)) {
					writer.write(code);
					LOG.info("wrote file: " + fileName);
				} catch (Exception e) {
					e.printStackTrace();
				}
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
		return "REST Controller Code Factory";
	}

	@Override
	public String[] getResourceBundleNames() {
		return new String[] { "restcontroller-code-factory" };
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