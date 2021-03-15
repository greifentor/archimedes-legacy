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
 * A code generator for DTO converters.
 *
 * @author ollie (15.03.2021)
 */
public class DTOConverterClassCodeGenerator extends AbstractClassCodeGenerator<RESTControllerNameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public DTOConverterClassCodeGenerator() {
		super(
				"DTOConverterClass.vm",
				RESTControllerCodeFactory.TEMPLATE_PATH,
				new RESTControllerNameGenerator(),
				new TypeGenerator());
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("DTOClassName", nameGenerator.getDTOClassName(table));
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		context.put("PackageName", getPackageName(model));
		context.put("SOClassName", serviceNameGenerator.getSOClassName(table));
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns) {
		return Arrays
				.asList(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false))
								.setGetterName(getGetterName(column))
								.setSetterName(getSetterName(column)))
				.collect(Collectors.toList());
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getDTOConverterClassName(table);
	}

	@Override
	public String getPackageName(DataModel model) {
		return nameGenerator.getDTOConverterPackageName(model);
	}

}