package archimedes.codegenerators;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * A utility class for columns.
 *
 * @author ollie (13.03.2021)
 */
public class Columns {

	@Accessors(chain = true)
	@Data
	public static class ColumnData {
		private String fieldName;
		private String fieldType;
		private String getterName;
		private String setterName;
	}

	public static boolean containsFieldWithType(List<ColumnData> columnData, String typeName) {
		return columnData.stream().anyMatch(cd -> cd.getFieldType().equals(typeName));
	}

}