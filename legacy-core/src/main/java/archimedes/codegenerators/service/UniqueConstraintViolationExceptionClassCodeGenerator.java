package archimedes.codegenerators.service;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the NotNullConstraintViolationException.
 *
 * @author ollie (20.02.2022)
 */
public class UniqueConstraintViolationExceptionClassCodeGenerator
		extends AbstractModelCodeGenerator<ServiceNameGenerator> {

	public UniqueConstraintViolationExceptionClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"UniqueConstraintViolationExceptionClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				ServiceNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("BasePackageName", model.getBasePackageName());
		context.put("ClassName", getClassName(model, model));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, model));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return "UniqueConstraintViolationException";
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getExceptionsPackageName(model);
	}

	@Override
	public boolean isDeprecated() {
		return true;
	}

}