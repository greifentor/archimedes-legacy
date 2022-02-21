package archimedes.codegenerators.persistence.jpa;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import org.apache.velocity.VelocityContext;

/**
 * A page converter class code generator for JPA database objects (DBO's).
 *
 * @author ollie (28.07.2021)
 */
public class PageConverterClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public PageConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"PageConverterClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PageModelClassName", nameGenerator.getPageModelClassName(table));
		context.put("PageModelPackageName", nameGenerator.getPageModelPackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("ToModelConverterInferfaceName", nameGenerator.getToModelConverterInterfaceName(table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getPageConverterClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getPageConverterPackageName(model, table);
	}

}
