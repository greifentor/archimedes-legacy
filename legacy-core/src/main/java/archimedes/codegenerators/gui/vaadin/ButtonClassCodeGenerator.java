package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A code generator for button classes.
 *
 * @author ollie (07.12.2021)
 */
public class ButtonClassCodeGenerator extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	public ButtonClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"component/ButtonClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				new GUIVaadinNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getButtonClassName(table.getDataModel());
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getVaadinComponentPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}