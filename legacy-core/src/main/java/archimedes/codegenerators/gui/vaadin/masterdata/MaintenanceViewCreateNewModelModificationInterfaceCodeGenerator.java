package archimedes.codegenerators.gui.vaadin.masterdata;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.model.DataModel;

/**
 * A code generator for maintenance view renderer interfaces.
 *
 * @author ollie (18.07.2024)
 */
public class MaintenanceViewCreateNewModelModificationInterfaceCodeGenerator
		extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public MaintenanceViewCreateNewModelModificationInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"masterdata/MaintenanceViewCreateNewModelModificationInterface.vm",
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
	}

	@Override
	public String getClassName(DataModel model, DataModel sameModel) {
		return nameGenerator.getMaintenanceViewCreateNewModelModificationInterfaceName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel sameModel) {
		return nameGenerator.getMasterDataPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}
