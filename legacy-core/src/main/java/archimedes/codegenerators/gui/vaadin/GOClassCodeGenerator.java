package archimedes.codegenerators.gui.vaadin;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceCodeFactory;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import org.apache.velocity.VelocityContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class code generator for GO's.
 *
 * @author ollie (06.09.2021)
 */
public class GOClassCodeGenerator extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	public GOClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"GOClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				new GUIVaadinNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		commonImportAdder.addCommonImports(context, columnData);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("PackageName", getPackageName(model, table));
		context.put("POJOMode", getPOJOMode(model, table).name());
		context.put("TableName", table.getName());
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns) {
		return Arrays
				.asList(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false)))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getGOClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getGOPackageName(model, table);
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}