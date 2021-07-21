package archimedes.codegenerators;

import java.util.List;

import org.apache.velocity.VelocityContext;

import archimedes.codegenerators.Columns.ColumnData;

public class CommonImportAdder {

	public void addCommonImports(VelocityContext context, List<ColumnData> columnData) {
		if (Columns.containsFieldWithType(columnData, "LocalDate")) {
			context.put("ImportLocalDate", "java.time.LocalDate");
		}
		if (Columns.containsFieldWithType(columnData, "LocalDateTime")) {
			context.put("ImportLocalDateTime", "java.time.LocalDateTime");
		}
		if (Columns.containsFieldWithType(columnData, "LocalTime")) {
			context.put("ImportLocalDate", "java.time.LocalTime");
		}
	}

}