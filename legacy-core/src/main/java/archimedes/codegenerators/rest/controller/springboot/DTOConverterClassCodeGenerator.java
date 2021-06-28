package archimedes.codegenerators.rest.controller.springboot;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.OptionGetter;
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

	public DTOConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DTOConverterClass.vm",
				RESTControllerSpringBootCodeFactory.TEMPLATE_FOLDER_PATH,
				new RESTControllerNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns(), model);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("DTOClassName", nameGenerator.getDTOClassName(table));
		context
				.put(
						"DTOClassNameQualified",
						getQualifiedName(
								nameGenerator.getDTOPackageName(model, table),
								nameGenerator.getDTOClassName(table)));
		context.put("GenerateIdClass", isGenerateIdClass(model, table));
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		context.put("PackageName", getPackageName(model, table));
		context.put("SOClassName", serviceNameGenerator.getSOClassName(table));
		context
				.put(
						"SOClassNameQualified",
						getQualifiedName(
								serviceNameGenerator.getSOPackageName(model, table),
								serviceNameGenerator.getSOClassName(table)));
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns, DataModel model) {
		return Arrays
				.asList(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false))
								.setGetterCall(getGetterCall(column, model))
								.setPkMember(column.isPrimaryKey())
								.setSetterName(getSetterName(column)))
				.collect(Collectors.toList());
	}

	private String getGetterCall(ColumnModel column, DataModel model) {
		String getterName = super.getGetterName(column);
		VelocityContext context = new VelocityContext();
		context.put("GetterName", getterName);
		if (isGenerateIdClass(model, column.getTable())) {
			context.put("KeyFromIdClass", column.isPrimaryKey());
		} else {
			context.put("KeyFromIdClass", "false");
		}
		return processTemplate(context, "DTOKeyGetter.vm");
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getDTOConverterClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDTOConverterPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		return OptionGetter.getOptionByName(model, AbstractClassCodeGenerator.MAPPERS).isPresent();
	}

}