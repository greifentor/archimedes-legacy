package archimedes.codegenerators.gui.vaadin.component;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;

/**
 * A code generator for remove confirm dialog classes.
 *
 * @author ollie (07.12.2021)
 */
public class RemoveConfirmDialogClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public RemoveConfirmDialogClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"component/RemoveConfirmDialogClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("ButtonFactoryClassName", nameGenerator.getButtonFactoryClassName(sameModel));
		context.put("ClassName", getClassName(model, model));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, model));
		context
				.put(
						"ResourceManagerInterfacePackageName",
						ServiceNameGenerator.INSTANCE.getResourceManagerInterfacePackageName(sameModel));
		context.put("ResourceManagerInterfaceName", ServiceNameGenerator.INSTANCE.getResourceManagerInterfaceName());
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(sameModel));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(sameModel));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getRemoveConfirmDialogClassName(model);
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
