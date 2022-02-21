package archimedes.codegenerators.service;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import org.apache.velocity.VelocityContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class code generator for page of model objects.
 *
 * @author ollie (22.07.2021)
 */
public class PageClassCodeGenerator extends AbstractClassCodeGenerator<ServiceNameGenerator> {

	public PageClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"PageClass.vm",
				ServiceCodeFactory.TEMPLATE_FOLDER_PATH,
				new ServiceNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		context.put("ClassName", getClassName(table));;
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
	}

	@Override
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getPageClassName();
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getPagePackageName(model, table);
	}

}