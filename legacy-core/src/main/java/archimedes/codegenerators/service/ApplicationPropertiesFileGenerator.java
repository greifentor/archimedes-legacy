package archimedes.codegenerators.service;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractFileGenerator;
import archimedes.model.DataModel;

/**
 * @author ollie (08.11.2023)
 */

public class ApplicationPropertiesFileGenerator extends AbstractFileGenerator {

	public static final String SUPPRESS_APPLICATION_PROPERTIES_FILE = "SUPPRESS_APPLICATION_PROPERTIES_FILE";

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

	@Override
	protected boolean isToIgnoreFor(DataModel model, DataModel t) {
		return super.isToIgnoreFor(model, t) || (model.getOptionByName(SUPPRESS_APPLICATION_PROPERTIES_FILE) != null);
	}

}
