package archimedes.codegenerators.service;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.localization.LocalizationNameGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the NotNullConstraintViolationException.
 *
 * @author ollie (20.02.2022)
 */
public class PersistenceFailureExceptionClassCodeGenerator
		extends AbstractModelCodeGenerator<ServiceNameGenerator> {

	public PersistenceFailureExceptionClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"PersistenceFailureExceptionClass.vm",
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
		context
				.put(
						"LocalizationModelPackageName",
						LocalizationNameGenerator.INSTANCE.getLocalizationSOPackageName(model));
		context.put("LocalizationSOClassName", LocalizationNameGenerator.INSTANCE.getLocalizationSOClassName());
		context
				.put(
						"ResourceManagerInterfaceName",
						LocalizationNameGenerator.INSTANCE.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						LocalizationNameGenerator.INSTANCE.getResourceManagerPackageName(model, null));
		context.put("PackageName", getPackageName(model, model));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return "PersistenceFailureException";
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getExceptionsPackageName(model);
	}

}