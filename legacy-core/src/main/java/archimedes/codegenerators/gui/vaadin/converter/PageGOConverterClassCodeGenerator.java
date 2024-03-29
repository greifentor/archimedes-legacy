package archimedes.codegenerators.gui.vaadin.converter;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A page converter class code generator for graphic user interface objects (GO's).
 *
 * @author ollie (28.07.2021)
 */
public class PageGOConverterClassCodeGenerator extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public PageGOConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"converter/PageGOConverterClass.vm",
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
		context.put("PageModelPackageName", serviceNameGenerator.getPagePackageName(model));
		context.put("PackageName", getPackageName(model, table));
		context.put("ToGOConverterInferfaceName", nameGenerator.getToGOConverterInterfaceName(table));
		context.put("ToGOMethodName", nameGenerator.getToGOMethodName(table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getPageGOConverterClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getPageGOConverterPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}
