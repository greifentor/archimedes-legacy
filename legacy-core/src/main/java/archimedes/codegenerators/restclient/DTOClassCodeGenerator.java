package archimedes.codegenerators.restclient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for DTO's.
 *
 * @author ollie (01.04.2021) - Copy of DTO class generator of the REST controller.
 */
public class DTOClassCodeGenerator extends AbstractClassCodeGenerator<RESTClientNameGenerator> {

	public DTOClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DTOClass.vm",
				RESTClientCodeFactory.TEMPLATE_FOLDER_PATH,
				new RESTClientNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		context.put("PackageName", getPackageName(model, table));
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
	public String getClassName(DataModel model, TableModel table) {
		return nameGenerator.getDTOClassName(table);
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