package archimedes.codegenerators.persistence.jpa;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.AnnotationData;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.Columns.ParameterData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

/**
 * A class code generator for JPA database objects (DBO's).
 *
 * @author ollie (03.03.2021)
 */
public class DBOClassCodeGenerator extends AbstractClassCodeGenerator<PersistenceJPANameGenerator> {

	public DBOClassCodeGenerator() {
		super(
				"DBOClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator());
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, DataModel model, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("ClassName", getClassName(table));
		context.put("ColumnData", columnData);
		context.put("EntityName", nameGenerator.getClassName(table));
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
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
								.setAnnotations(getAnnotations(column))
								.setFieldName(nameGenerator.getAttributeName(column))
								.setFieldType(typeGenerator.getJavaTypeString(column.getDomain(), false)))
				.collect(Collectors.toList());
	}

	private List<AnnotationData> getAnnotations(ColumnModel column) {
		List<AnnotationData> annotations = new ArrayList<>();
		if (column.isPrimaryKey()) {
			annotations.add(new AnnotationData().setName("Id"));
		}
		annotations
				.add(
						new AnnotationData()
								.setName("Column")
								.setParameters(
										Arrays
												.asList(
														new ParameterData()
																.setName("name")
																.setValue("\"" + column.getName() + "\""))));
		return annotations;
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