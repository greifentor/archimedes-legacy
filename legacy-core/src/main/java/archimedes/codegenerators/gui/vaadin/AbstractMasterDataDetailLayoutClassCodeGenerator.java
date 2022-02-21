package archimedes.codegenerators.gui.vaadin;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for the abstract master data detail layout.
 *
 * @author ollie (14.09.2021)
 */
public class AbstractMasterDataDetailLayoutClassCodeGenerator extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public AbstractMasterDataDetailLayoutClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"AbstractMasterDataDetailLayoutClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				new GUIVaadinNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getAbstractMasterDataDetailLayoutClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui-web";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getVaadinComponentPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}
