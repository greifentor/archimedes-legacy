package archimedes.codegenerators.gui.vaadin.component;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.codegenerators.gui.vaadin.LabelPropertiesGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;

/**
 * A code generator for selection dialog classes.
 *
 * @author ollie (11.03.2024)
 */
public class SelectionDialogClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	private final static ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public SelectionDialogClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"component/SelectionDialogClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("ButtonClassName", nameGenerator.getButtonClassName(model));
		context.put("ButtonFactoryClassName", nameGenerator.getButtonFactoryClassName(model));
		context.put("ClassName", getClassName(model, model));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, model));
		context.put("ResourceManagerInterfaceName", serviceNameGenerator.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerInterfacePackageName",
						serviceNameGenerator.getResourceManagerInterfacePackageName(model));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		LabelPropertiesGenerator.addLabel("SelectionDialog.button.cancel.label", "Cancel");
		LabelPropertiesGenerator.addLabel("SelectionDialog.button.select.label", "Select");
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getSelectionDialogClassName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getVaadinComponentPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}