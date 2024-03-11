package archimedes.codegenerators.gui.vaadin.masterdata;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.model.DataModel;

/**
 * A code generator for selection subclass classes.
 *
 * @author ollie (11.03.2024)
 */
public class SelectableSubclassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public SelectableSubclassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"component/SelectableSubclassClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("ClassName", getClassName(model, model));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, model));
		context.put("SelectionDialogClassName", nameGenerator.getSelectionDialogClassName(model));
		context.put("SelectionDialogPackageName", nameGenerator.getSelectionDialogPackageName(model));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getSelectableSubclassClassName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getSelectableSubclassPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}