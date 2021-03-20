package archimedes.codegenerators.restcontroller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for REST controllers.
 *
 * @author ollie (18.03.2021)
 */
public class RESTControllerClassCodeGenerator extends AbstractClassCodeGenerator<RESTControllerNameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public RESTControllerClassCodeGenerator() {
		super(
				"RESTControllerClass.vm",
				RESTControllerCodeFactory.TEMPLATE_PATH,
				new RESTControllerNameGenerator(),
				new TypeGenerator());
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("DTOConverterClassName", nameGenerator.getDTOConverterClassName(table));
		context
				.put(
						"DTOConverterClassNameQualified",
						getQualifiedName(
								nameGenerator.getDTOConverterPackageName(model),
								nameGenerator.getDTOConverterClassName(table)));
		context.put("DTOClassName", nameGenerator.getDTOClassName(table));
		context
				.put(
						"DTOClassNameQualified",
						getQualifiedName(nameGenerator.getDTOPackageName(model), nameGenerator.getDTOClassName(table)));
		context.put("ListDTOClassName", nameGenerator.getListDTOClassName(table));
		context
				.put(
						"ListDTOClassNameQualified",
						getQualifiedName(
								nameGenerator.getDTOPackageName(model),
								nameGenerator.getListDTOClassName(table)));
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		context.put("PackageName", getPackageName(model));
		context.put("SimpleName", nameGenerator.getSimpleName(table));
		context.put("SOClassName", serviceNameGenerator.getSOClassName(table));
		context
				.put(
						"SOClassNameQualified",
						getQualifiedName(
								serviceNameGenerator.getSOPackageName(model),
								serviceNameGenerator.getSOClassName(table)));
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
		return nameGenerator.getRESTControllerClassName(table);
	}

	@Override
	public String getPackageName(DataModel model) {
		return nameGenerator.getRESTControllerPackageName(model);
	}

}