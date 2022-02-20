package archimedes.codegenerators.service;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the NotNullConstraintViolationException.
 *
 * @author ollie (20.02.2022)
 */
public class UniqueConstraintViolationExceptionClassCodeGenerator
		extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public UniqueConstraintViolationExceptionClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"UniqueConstraintViolationExceptionClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				new ServiceNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("BasePackageName", model.getBasePackageName());
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
	}

	@Override
	public String getClassName(TableModel table) {
		return "UniqueConstraintViolationException";
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getExceptionsPackageName(model, table);
	}

}