package archimedes.codegenerators.persistence.jpa;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;

/**
 * A page converter class code generator for JPA database objects (DBO's).
 *
 * @author ollie (28.07.2021)
 */
public class PageConverterClassCodeGenerator extends AbstractModelCodeGenerator<PersistenceJPANameGenerator> {

	public PageConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
		        "PageConverterClass.vm",
		        PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
		        PersistenceJPANameGenerator.INSTANCE,
		        TypeGenerator.INSTANCE,
		        codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("ClassName", getClassName(model, sameModel));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PageModelClassName", nameGenerator.getPageModelClassName());
		context.put("PageModelPackageName", nameGenerator.getPageModelPackageName(model));
		context.put("PackageName", getPackageName(model, null));
		context.put("ToModelConverterInferfaceName", nameGenerator.getToModelConverterInterfaceName());
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getPageConverterClassName();
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getPageConverterPackageName(model);
	}

}
