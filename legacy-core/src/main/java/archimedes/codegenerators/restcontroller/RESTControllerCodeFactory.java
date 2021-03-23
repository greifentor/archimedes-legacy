package archimedes.codegenerators.restcontroller;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import archimedes.acf.checker.ModelChecker;
import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.CodeGenerator;
import archimedes.legacy.acf.event.CodeFactoryProgressionEventProvider;
import archimedes.legacy.acf.gui.StandardCodeFactoryProgressionFrameUser;
import archimedes.model.DataModel;
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

	private List<CodeGenerator> codeGenerators =
			Arrays.asList(new DTOClassCodeGenerator(), new DTOClassCodeGenerator() {
				@Override
				protected String getDefaultModuleName(DataModel dataModel) {
					return "rest-client";
				}
			},
					new DTOConverterClassCodeGenerator(),
					new ListDTOClassCodeGenerator(),
					new RESTControllerClassCodeGenerator());

	@Override
	public boolean generate(String path) {
		LOG.info("Started code generation");
		new File(path).mkdirs();
		String basePackageName = this.dataModel.getBasePackageName();
		for (TableModel tableModel : dataModel.getTables()) {
			if (tableModel.isGenerateCode()) {
				codeGenerators.forEach(codeGenerator -> {
					if (codeGenerator instanceof AbstractClassCodeGenerator<?>) {
						((AbstractClassCodeGenerator<?>) codeGenerator)
								.generate(path, basePackageName, dataModel, tableModel);
					}
				});
			}
		}
		return true;
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
		return new String[] { "archimedes", "restcontroller-code-factory" };
	}

	@Override
	public String getVersion() {
		return "1.0.0";
	}

}