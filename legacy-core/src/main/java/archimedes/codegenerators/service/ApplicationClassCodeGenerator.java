package archimedes.codegenerators.service;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import org.apache.velocity.VelocityContext;

/**
 * A class code generator for application classes.
 *
 * @author ollie (03.08.2021)
 */
public class ApplicationClassCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public ApplicationClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ApplicationClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				new ServiceNameGenerator(),
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
	public String getClassName(DataModel model, TableModel table) { return nameGenerator.getApplicationClassName(table.getDataModel()); }

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getApplicationPackageName(model, table);
	}

}