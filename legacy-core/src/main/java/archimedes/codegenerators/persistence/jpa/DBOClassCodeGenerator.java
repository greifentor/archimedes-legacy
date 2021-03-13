package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.Columns;
import archimedes.codegenerators.Columns.ColumnData;
import archimedes.codegenerators.TypeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.TableModel;

/**
 * A class code generator for JPA database objects (DBO's).
 *
 * @author ollie (03.03.2021)
 */
public class DBOClassCodeGenerator extends AbstractCodeGenerator<PersistenceJPANameGenerator> {

	public DBOClassCodeGenerator() {
		super(
				"DBOClass.vm",
				PersistenceJPACodeFactory.TEMPLATE_PATH,
				new PersistenceJPANameGenerator(),
				new TypeGenerator());
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("ClassName", nameGenerator.getDBOClassName(table));
		context.put("ColumnData", columnData);
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
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

}