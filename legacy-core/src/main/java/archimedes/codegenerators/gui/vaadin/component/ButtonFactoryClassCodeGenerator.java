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
 * A code generator for button classes.
 *
 * @author ollie (07.12.2021)
 */
public class ButtonFactoryClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	private final static ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public ButtonFactoryClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"component/ButtonFactoryClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("ApplicationStartViewClassName", nameGenerator.getApplicationStartViewClassName());
		context.put("ApplicationStartViewPackageName", nameGenerator.getApplicationStartViewPackageName(model));
		context.put("ButtonClassName", nameGenerator.getButtonClassName(model));
		context.put("ButtonFactoryConfigurationClassName", nameGenerator.getButtonFactoryConfigurationClassName(model));
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
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getButtonFactoryClassName(model);
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