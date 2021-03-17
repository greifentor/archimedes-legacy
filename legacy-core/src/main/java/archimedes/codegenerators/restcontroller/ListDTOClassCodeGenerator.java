package archimedes.codegenerators.restcontroller;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for DTO lists.
 *
 * @author ollie (17.03.2021)
 */
public class ListDTOClassCodeGenerator extends AbstractClassCodeGenerator<RESTControllerNameGenerator> {

	public ListDTOClassCodeGenerator() {
		super(
				"ListDTOClass.vm",
				RESTControllerCodeFactory.TEMPLATE_PATH,
				new RESTControllerNameGenerator(),
				new TypeGenerator());
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("DTOClassName", nameGenerator.getDTOClassName(table));
		context.put("PackageName", getPackageName(model));
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getListDTOClassName(table);
	}

	@Override
	public String getPackageName(DataModel model) {
		return nameGenerator.getDTOPackageName(model);
	}

}