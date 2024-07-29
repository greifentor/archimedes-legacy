package archimedes.codegenerators.persistence.jpa;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;

/**
 * A page parameters to pageable converter class code generator for JPA database objects (DBO's).
 *
 * @author ollie (28.07.2021)
 */
public class PageParametersToPageableConverterClassCodeGenerator
		extends AbstractModelCodeGenerator<PersistenceJPANameGenerator> {

	public PageParametersToPageableConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"PageParametersToPageableConverterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				PersistenceJPANameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("ClassName", getClassName(model, model));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PageParametersModelClassName", nameGenerator.getPageParametersModelClassName());
		context.put("PageParametersModelPackageName", nameGenerator.getPageParametersModelPackageName(model));
		context.put("PackageName", getPackageName(model, sameModel));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getPageParametersToPageableConverterClassName();
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getPageParametersToPageableConverterPackageName(model);
	}

}
