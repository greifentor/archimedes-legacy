package archimedes.codegenerators.restcontroller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.TableModel;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A class code generator for DTO's.
 *
 * @author ollie (10.03.2021)
 */
public class DTOClassCodeGenerator extends AbstractCodeGenerator {

	@Accessors(chain = true)
	@Data
	static class ColumnData {
		private String fieldName;
		private String fieldType;
	}

	public DTOClassCodeGenerator() {
		super("DTOClass.vm", RestControllerCodeFactory.TEMPLATE_PATH);
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, TableModel table) {
		context.put("ClassName", table.getName() + "DTO");
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