package archimedes.codegenerators.gui.vaadin.cube;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the access checker interface.
 *
 * @author ollie (05.01.2023)
 */
public class AccessCheckerInterfaceCodeGenerator extends AbstractModelCodeGenerator<CubeNameGenerator> {

	public AccessCheckerInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"AccessCheckerInterface.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH + "/cube",
				CubeNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("ClassName", getClassName(model, null));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, null));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getAccessCheckerInterfaceName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getAccessCheckerPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, DataModel sameModel) {
		return !model.isOptionSet(AbstractGUIVaadinClassCodeGenerator.CUBE_APPLICATION);
	}

}