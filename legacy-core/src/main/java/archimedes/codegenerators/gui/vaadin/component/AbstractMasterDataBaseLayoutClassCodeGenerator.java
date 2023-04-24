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
 * A code generator for the abstract master data base layout.
 *
 * @author ollie (08.12.2021)
 */
public class AbstractMasterDataBaseLayoutClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public AbstractMasterDataBaseLayoutClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"component/AbstractMasterDataBaseLayoutClass.vm",
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
		context.put("MasterDataButtonLayoutClassName", nameGenerator.getMasterDataButtonLayoutClassName(model));
		context.put("MasterDataButtonLayoutPackageName", nameGenerator.getMasterDataButtonLayoutPackageName(model));
		context.put("PackageName", getPackageName(model, model));
		context.put("ResourceManagerClassName", ServiceNameGenerator.INSTANCE.getResourceManagerInterfaceName());
		context
				.put(
						"ResourceManagerPackageName",
						ServiceNameGenerator.INSTANCE.getResourceManagerInterfacePackageName(model));
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
		context.put("TextFieldFactoryClassName", nameGenerator.getTextFieldFactoryClassName(model));
		context.put("UserAuthorizationCheckerClassName", nameGenerator.getUserAuthorizationCheckerClassName(model));
		context.put("UserAuthorizationCheckerPackageName", nameGenerator.getUserAuthorizationCheckerPackageName(model));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getAbstractMasterDataBaseLayoutClassName();
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