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
		context.put("GenerateIdClass", isGenerateIdClass(model, table));
		context.put("IdCall", getIdCall(model, table));
		context
				.put(
						"IdSOClassNameQualified",
						getQualifiedName(
								serviceNameGenerator.getSOPackageName(model),
								serviceNameGenerator.getIdSOClassName(table)));
		context
				.put(
						"ListDTOClassNameQualified",
						getQualifiedName(
								nameGenerator.getDTOPackageName(model),
								nameGenerator.getListDTOClassName(table)));
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
		context.put("ServiceClassName", serviceNameGenerator.getServiceClassName(table));
		context
				.put(
						"ServiceClassNameQualified",
						getQualifiedName(
								serviceNameGenerator.getServicePackageName(model),
								serviceNameGenerator.getServiceClassName(table)));
		context.put("SimpleName", nameGenerator.getSimpleName(table));
		context
				.put(
						"SOClassNameQualified",
						getQualifiedName(
								serviceNameGenerator.getSOPackageName(model),
								serviceNameGenerator.getSOClassName(table)));
		context.put("URL", nameGenerator.getURLName(model, table));
	}

	private String getIdCall(DataModel model, TableModel table) {
		VelocityContext context = new VelocityContext();
		context.put("GenerateIdClass", isGenerateIdClass(model, table));
		context.put("IdSOClass", serviceNameGenerator.getIdSOClassName(table));
		return processTemplate(context, "FindByIdCall.vm");
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
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model) {
		return nameGenerator.getRESTControllerPackageName(model);
	}

}