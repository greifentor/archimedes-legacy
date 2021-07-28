package archimedes.codegenerators.persistence.jpa;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import org.apache.velocity.VelocityContext;

/**
 * A to model converter interface code generator for JPA database objects (DBO's).
 *
 * @author ollie (28.07.2021)
 */
public class ToModelConverterInterfaceCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public ToModelConverterInterfaceCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ToModelConverterInterface.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
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
	public String getClassName(TableModel table) {
		return nameGenerator.getToModelConverterInterfaceName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDBOConverterPackageName(model, table);
	}

}
