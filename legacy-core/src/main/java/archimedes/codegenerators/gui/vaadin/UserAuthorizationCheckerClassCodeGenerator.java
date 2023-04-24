package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;

/**
 * A code generator for user authorization checker classes.
 *
 * @author ollie (19.12.2022)
 */
public class UserAuthorizationCheckerClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public UserAuthorizationCheckerClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"UserAuthorizationCheckerClass.vm",
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
		context.put("SessionDataClassName", nameGenerator.getSessionDataClassName(model));
		context.put("SessionDataPackageName", nameGenerator.getSessionDataPackageName(model));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getUserAuthorizationCheckerClassName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getUserAuthorizationCheckerPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}