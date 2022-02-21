package archimedes.codegenerators.localization;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for file based localization resource manager configuration classes.
 *
 * @author ollie (07.01.2022)
 */
public class FileBasedResourceManagerConfigurationClassCodeGenerator extends AbstractClassCodeGenerator<LocalizationNameGenerator> {

	public FileBasedResourceManagerConfigurationClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"FileBasedResourceManagerConfigurationClass.vm",
				LocalizationCodeFactory.TEMPLATE_FOLDER_PATH,
				new LocalizationNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("AppResourcePrefix", getAppResourcePrefix(model));
		context.put("BasePackageName", model.getBasePackageName());
		context.put("ClassName", getClassName(table));;
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("Dollar", "$");
		context.put("LocalizationSOPackageName", nameGenerator.getLocalizationSOPackageName(model, table));
		context.put("LocalizationSOClassName", nameGenerator.getLocalizationSOClassName());
		context.put("PackageName", getPackageName(model, table));
	}

	private String getAppResourcePrefix(DataModel model) {
		return model.getApplicationName().toLowerCase().replace(" ", "-");
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getFileBasedResourceManagerConfigurationClassName();
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getResourceManagerImplPackageName(model, table);
	}

}