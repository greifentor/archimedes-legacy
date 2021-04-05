package archimedes.codegenerators.restclient;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for id DTO's.
 *
 * @author ollie (05.04.2021)
 */
public class IdDTOClassCodeGenerator extends AbstractClassCodeGenerator<RESTClientNameGenerator> {

	public IdDTOClassCodeGenerator() {
		super(
				"IdDTOClass.vm",
				RESTClientCodeFactory.TEMPLATE_FOLDER_PATH,
				new RESTClientNameGenerator(),
				new TypeGenerator());
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
				.filter(ColumnModel::isPrimaryKey)
				.map(
						column -> new ColumnData()
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false)))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getIdDTOClassName(table);
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