package archimedes.codegenerators.restcontroller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.NameGenerator;
import archimedes.codegenerators.TypeGenerator;
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
	public static class ColumnData {
		private String fieldName;
		private String fieldType;
	}

	public DTOClassCodeGenerator() {
		super("DTOClass.vm", RestControllerCodeFactory.TEMPLATE_PATH, new NameGenerator(), new TypeGenerator());
	}

	@Override
	protected void extendVelocityContext(VelocityContext context, TableModel table) {
		List<ColumnData> columnData = getColumnData(table.getColumns());
		context.put("ClassName", nameGenerator.getDTOClassName(table));
		context.put("ColumnData", columnData);
		if (containsFieldWithType(columnData, "LocalDate")) {
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

	private boolean containsFieldWithType(List<ColumnData> columnData, String typeName) {
		return columnData.stream().anyMatch(cd -> cd.getFieldType().equals(typeName));
	}

}