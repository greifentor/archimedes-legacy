package archimedes.codegenerators.persistence.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeFactory;
import archimedes.codegenerators.Columns.AnnotationData;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.Columns.ParameterData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.OptionModel;
import archimedes.model.TableModel;

/**
 * A class code generator for JPA database objects (DBO's).
 *
 * @author ollie (03.03.2021)
 */
public class DBOClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public DBOClassCodeGenerator(AbstractCodeFactory codeFactory) {
		super(
				"DBOClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_FOLDER_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator(),
				codeFactory);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		commonImportAdder.addCommonImports(context, columnData);
		context.put("Autoincrement", getAutoincrementMode(columnData));
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("CommentsOff", isCommentsOff(model, table));
		context.put("EntityName", nameGenerator.getClassName(table));
		context.put("PackageName", getPackageName(model, table));
		context.put("POJOMode", getPOJOMode(model, table).name());
		context.put("TableName", table.getName());
	}

	private String getAutoincrementMode(List<ColumnData> columnData) {
		return columnData
				.stream()
				.filter(
						column -> column
								.getAnnotations()
								.stream()
								.anyMatch(annotation -> annotation.getName().equals("GeneratedValue")))
				.map(column -> getGeneratedValueType(column.getAnnotations()))
				.reduce((s0, s1) -> s0 + "," + s1)
				.orElse(null);
	}

	private String getGeneratedValueType(List<AnnotationData> annotations) {
		return annotations
				.stream()
				.filter(annotation -> annotation.getName().equals("GeneratedValue"))
				.map(annotation -> annotation.getParameters().get(0).toJavaCode().replace("GenerationType.", ""))
				.findFirst()
				.orElse(null);
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns) {
		return Arrays
				.asList(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setAnnotations(getAnnotations(column))
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), isNullable(column))))
				.collect(Collectors.toList());
	}

	private boolean isNullable(ColumnModel column) {
		return !column.isNotNull();
	}

	private List<AnnotationData> getAnnotations(ColumnModel column) {
		List<AnnotationData> annotations = new ArrayList<>();
		if (column.isPrimaryKey()) {
			annotations.add(new AnnotationData().setName("Id"));
		}
		OptionModel autoincrement = column.getOptionByName(AbstractClassCodeGenerator.AUTO_INCREMENT);
		if (autoincrement != null) {
			if (autoincrement.getParameter().equals("IDENTITY")) {
				annotations
						.add(
								new AnnotationData()
										.setName("GeneratedValue")
										.setParameters(
												Arrays
														.asList(
																new ParameterData()
																		.setName("strategy")
																		.setValue("GenerationType.IDENTITY"))));
			} else if (autoincrement.getParameter().equals("SEQUENCE")) {
				String className = nameGenerator.getClassName(column.getTable());
				String sequenceGeneratorName = className + "Sequence";
				String sequenceName =
						column.getTable().getName().toLowerCase() + "_" + column.getName().toLowerCase() + "_seq";
				annotations
						.add(
								new AnnotationData()
										.setName("SequenceGenerator")
										.setParameters(
												Arrays
														.asList(
																new ParameterData()
																		.setName("allocationSize")
																		.setValue("1"),
																new ParameterData()
																		.setName("name")
																		.setValue("\"" + sequenceGeneratorName + "\""),
																new ParameterData()
																		.setName("sequenceName")
																		.setValue("\"" + sequenceName + "\""))));
				annotations
						.add(
								new AnnotationData()
										.setName("GeneratedValue")
										.setParameters(
												Arrays
														.asList(
																new ParameterData()
																		.setName("strategy")
																		.setValue("GenerationType.SEQUENCE"),
																new ParameterData()
																		.setName("generator")
																		.setValue(
																				"\"" + sequenceGeneratorName + "\""))));
			}
		}
		annotations.add(new AnnotationData().setName("Column").setParameters(getColumnAnnotationParameters(column)));
		return annotations;
	}

	private List<ParameterData> getColumnAnnotationParameters(ColumnModel column) {
		List<ParameterData> l = new ArrayList<ParameterData>();
		l.add(new ParameterData().setName("name").setValue("\"" + column.getName() + "\""));
		if (column.isNotNull()) {
			l.add(new ParameterData().setName("nullable").setValue("false"));
		}
		return l;
	}

	@Override
	public String getClassName(TableModel table) {
		return nameGenerator.getDBOClassName(table);
	}

	@Override
	protected String getDefaultModuleName(DataModel dataModel) {
		return "service";
	}

	@Override
	public String getPackageName(DataModel model, TableModel table) {
		return nameGenerator.getDBOPackageName(model, table);
	}

}