package archimedes.codegenerators.restclient;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for DTO lists.
 *
 * @author ollie (05.04.2021)
 */
public class ListDTOClassCodeGenerator extends AbstractClassCodeGenerator<RESTClientNameGenerator> {

	public ListDTOClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"ListDTOClass.vm",
				RESTClientCodeFactory.TEMPLATE_FOLDER_PATH,
				new RESTClientNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));
		context.put("DTOClassName", nameGenerator.getDTOClassName(table));
		context.put("PackageName", getPackageName(model, table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getListDTOClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "rest-client";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDTOPackageName(model, table);
	}

}