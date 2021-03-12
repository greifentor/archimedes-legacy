package archimedes.codegenerators.persistence.jpa;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.TableModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A class code generator for JPA database objects (DBO's).
 *
 * @author ollie (03.03.2021)
 */
public class DBOClassCodeGenerator extends AbstractCodeGenerator {

	@Accessors(chain = true)
	@Data
	static class ColumnData {
		private String fieldName;
		private String fieldType;
	}

	public DBOClassCodeGenerator() {
		super("DBOClass.vm", PersistenceJPACodeFactory.TEMPLATE_PATH, new NameGenerator());
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, TableModel table) {
		context.put("ClassName", nameGenerator.getClassName(table) + "DBO");
		context.put("ColumnData", getColumnData(table.getColumns()));
	}

	private List<ColumnData> getColumnData(ColumnModel[] columns) {
		return Arrays
				.asList(columns)
				.stream()
				.map(
						column -> new ColumnData()
								.setFieldName(column.getName().toLowerCase())
								.setFieldType(column.getName().equals("id") ? "long" : "String"))
				.collect(Collectors.toList());
	}

}