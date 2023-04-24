package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator;
import archimedes.model.DataModel;

/**
 * A class code generator for the session id classes.
 *
 * @author ollie (02.09.2022)
 */
public class GUIApplicationStarterClassCodeGenerator extends AbstractVaadinModelCodeGenerator {

	public GUIApplicationStarterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super("GUIApplicationStarterClass.vm", codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, DataModel model0) {
		context.put("ClassName", getClassName(model, null));
		context.put("CommentsOff", isCommentsOff(model));
		context.put("EntityPackageName", PersistenceJPANameGenerator.INSTANCE.getDBOPackageName(model, null));
		context.put("PackageName", getPackageName(model, null));
		context
				.put(
						"RepositoryPackageName",
						PersistenceJPANameGenerator.INSTANCE.getJPARepositoryPackageName(model, null));
	}

	@Override
	public String getClassName(DataModel model, DataModel dummy) {
		return nameGenerator.getGUIApplicationStarterClassName(model);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, DataModel dummy) {
		return nameGenerator.getGUIApplicationStarterPackageName(model);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}