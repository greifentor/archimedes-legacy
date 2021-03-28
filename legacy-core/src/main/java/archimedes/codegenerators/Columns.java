package archimedes.codegenerators;

import java.util.ArrayList;
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
	public static class ParameterData {
		private String name;
		private String value;
	}

	@Accessors(chain = true)
	@Data
	public static class AnnotationData {
		private String name;
		private List<ParameterData> parameters = new ArrayList<>();
	}

	@Accessors(chain = true)
	@Data
	public static class ColumnData {
		private List<AnnotationData> annotations = new ArrayList<>();
		private String fieldName;
		private String fieldType;
		private String getterCall;
		private String getterName;
		private boolean pkMember;
		private String setterName;
	}

	public static boolean containsFieldWithType(List<ColumnData> columnData, String typeName) {
		return columnData.stream().anyMatch(cd -> cd.getFieldType().equals(typeName));
	}

}