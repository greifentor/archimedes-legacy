package archimedes.codegenerators.service;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractFileGenerator;
import archimedes.model.DataModel;

/**
 * @author ollie (08.11.2023)
 */

public class ApplicationPropertiesFileGenerator extends AbstractFileGenerator {

	public ApplicationPropertiesFileGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ApplicationPropertiesFile.vm",
				".properties",
				codeFactory,
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				model -> "resources");
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel model0) {
		super.extendVelocityContext(context, model, model0);
		context.put("DBName", model0.getApplicationName().toLowerCase());
	}

	@Override
	public String getClassName(DataModel model, DataModel t) {
		return "application";
	}

	@Override
	protected String getPackageNameSuffix() {
		return "src/main";
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

}
