package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;

/**
 * A code generator for master data grid field renderer interfaces.
 *
 * @author ollie (19.12.2022)
 */
public class MasterDataGridFieldRendererInterfaceCodeGenerator
		extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public MasterDataGridFieldRendererInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"MasterDataGridFieldRendererInterface.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel sameModel) {
		context.put("InterfaceName", getClassName(model, model));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, model));
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getMasterDataGridFieldRendererInterfaceName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getPageViewPackageName(model, null);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}