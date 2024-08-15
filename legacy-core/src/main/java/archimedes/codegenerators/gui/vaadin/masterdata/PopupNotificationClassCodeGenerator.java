package archimedes.codegenerators.gui.vaadin.masterdata;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.AbstractModelCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.gui.vaadin.GUIVaadinCodeFactory;
import archimedes.codegenerators.gui.vaadin.GUIVaadinNameGenerator;
import archimedes.model.DataModel;

public class PopupNotificationClassCodeGenerator extends AbstractModelCodeGenerator<GUIVaadinNameGenerator> {

	public PopupNotificationClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"masterdata/PopupNotificationClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				GUIVaadinNameGenerator.INSTANCE,
				TypeGenerator.INSTANCE,
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel model0) {
		context.put("ClassName", getClassName(model, null));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("PackageName", getPackageName(model, null));
	}

	@Override
	public String getClassName(DataModel model, DataModel dummy) {
		return "PopupNotification";
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel dummy) {
		return nameGenerator.getMasterDataPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}
