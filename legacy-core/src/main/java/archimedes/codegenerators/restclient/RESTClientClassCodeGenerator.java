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
 * A class code generator for REST clients.
 *
 * @author ollie (05.04.2021)
 */
public class RESTClientClassCodeGenerator extends AbstractClassCodeGenerator<RESTClientNameGenerator> {

	public RESTClientClassCodeGenerator() {
		super(
				"RESTClientClass.vm",
				RESTClientCodeFactory.TEMPLATE_FOLDER_PATH,
				new RESTClientNameGenerator(),
				new TypeGenerator());
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("ClassName", getClassName(table));
		context.put("DTOClassName", nameGenerator.getDTOClassName(table));
		context
				.put(
						"DTOQualifiedClassName",
						getQualifiedName(
								nameGenerator.getDTOPackageName(model, table),
								nameGenerator.getDTOClassName(table)));
		context.put("ColumnData", columnData);
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		context.put("IdDTOClassName", nameGenerator.getIdDTOClassName(table));
		context
				.put(
						"IdDTOQualifiedClassName",
						getQualifiedName(
								nameGenerator.getDTOPackageName(model, table),
								nameGenerator.getIdDTOClassName(table)));
		context.put("ListDTOClassName", nameGenerator.getListDTOClassName(table));
		context
				.put(
						"ListDTOQualifiedClassName",
						getQualifiedName(
								nameGenerator.getDTOPackageName(model, table),
								nameGenerator.getListDTOClassName(table)));
		context.put("PackageName", getPackageName(model, table));
		context.put("URL", nameGenerator.getURLName(model, table));
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
		return nameGenerator.getRESTClientClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "rest-client";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getRESTClientPackageName(model, table);
	}

}