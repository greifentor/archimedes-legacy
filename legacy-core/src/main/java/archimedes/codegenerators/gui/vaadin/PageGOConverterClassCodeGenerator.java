package archimedes.codegenerators.gui.vaadin;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import org.apache.velocity.VelocityContext;

/**
 * A page converter class code generator for JPA database objects (DBO's).
 *
 * @author ollie (28.07.2021)
 */
public class PageGOConverterClassCodeGenerator extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public PageGOConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"PageGOConverterClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				new GUIVaadinNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PageGOClassName", nameGenerator.getPageGOClassName(table));
		context.put("PageGOPackageName", nameGenerator.getPageGOPackageName(model, table));
		context.put("PageModelClassName", serviceNameGenerator.getPageClassName());
		context.put("PageModelPackageName", serviceNameGenerator.getPagePackageName(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("ToGOConverterInferfaceName", nameGenerator.getToGOConverterInterfaceName(table));
		context.put("ToGOMethodName", nameGenerator.getToGOMethodName(table));
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getPageGOConverterClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getPageGOConverterPackageName(model, table);
	}

}
