package archimedes.codegenerators.localization;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for localization service classes.
 *
 * @author ollie (07.01.2022)
 */
public class LocalizationSOClassCodeGenerator extends AbstractClassCodeGenerator<LocalizationNameGenerator> {

	public LocalizationSOClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"LocalizationSOClass.vm",
				LocalizationCodeFactory.TEMPLATE_FOLDER_PATH,
				new LocalizationNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("BasePackageName", model.getBasePackageName());
		context.put("ClassName", getClassName(table));;
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getLocalizationSOClassName();
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getLocalizationSOPackageName(model);
	}

}