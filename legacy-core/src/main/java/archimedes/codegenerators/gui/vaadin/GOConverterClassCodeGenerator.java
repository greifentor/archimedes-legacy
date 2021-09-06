package archimedes.codegenerators.gui.vaadin;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.OptionGetter;
import archimedes.codegenerators.TypeGenerator;
import archimedes.codegenerators.persistence.jpa.PersistenceJPACodeFactory;
import archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator;
import archimedes.codegenerators.service.ServiceNameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import org.apache.velocity.VelocityContext;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A code generator for GO converters.
 *
 * @author ollie (06.09.2021)
 */
public class GOConverterClassCodeGenerator extends AbstractClassCodeGenerator<GUIVaadinNameGenerator> {

	private ServiceNameGenerator serviceNameGenerator = new ServiceNameGenerator();

	public GOConverterClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"GOConverterClass.vm",
				GUIVaadinCodeFactory.TEMPLATE_FOLDER_PATH,
				new GUIVaadinNameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns(), model);
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("GOClassName", nameGenerator.getGOClassName(table));
		context
				.put(
						"GOClassNameQualified",
						getQualifiedName(
								nameGenerator.getGOPackageName(model, table),
								nameGenerator.getGOClassName(table)));
		context.put("GenerateIdClass", isGenerateIdClass(model, table));
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		context.put("ModelClassName", serviceNameGenerator.getModelClassName(table));
		context
				.put(
						"ModelClassNameQualified",
						getQualifiedName(
								serviceNameGenerator.getModelPackageName(model, table),
								serviceNameGenerator.getModelClassName(table)));
		context.put("PackageName", getPackageName(model, table));
		context.put("ToGOMethodName", nameGenerator.getToGOMethodName(table));
		context.put("ToModelMethodName", nameGenerator.getToModelMethodName(table));
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
		return processTemplate(context, "GOKeyGetter.vm");
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getGOConverterClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "gui";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getGOConverterPackageName(model, table);
	}

	@Override
	protected boolean isToIgnoreFor(DataModel model, TableModel table) {
		return OptionGetter.getOptionByName(model, AbstractClassCodeGenerator.MAPPERS).isPresent();
	}

	@Override
	protected String getAlternateModule() {
		return GUIVaadinNameGenerator.ALTERNATE_GUI_VAADIN_MODULE_PREFIX;
	}

}