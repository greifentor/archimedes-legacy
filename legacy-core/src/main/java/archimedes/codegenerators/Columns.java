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

		public String toJavaCode() {
			return getName() + " = " + getValue();
		}
	}

	@Accessors(chain = true)
	@Data
	public static class AnnotationData {
		private String name;
		private List<ParameterData> parameters = new ArrayList<>();

		public AnnotationData addParameter(ParameterData parameterData) {
			parameters.add(parameterData);
			return this;
		}

		public String toJavaCode() {
			String s = "@" + getName();
			if (!getParameters().isEmpty()) {
				s += "(" + getParameters()
						.stream()
						.map(ParameterData::toJavaCode)
						.reduce((s0, s1) -> s0 + ", " + s1)
						.orElse("") + ")";
			}
			return s;
		}
	}

	@Accessors(chain = true)
	@Data
	public static class ColumnData {
		private List<AnnotationData> annotations = new ArrayList<>();
		private String columnName;
		private String converterAttributeName;
		private String descriptionName;
		private String enumIdentifier;
		private String fieldName;
		private String fieldType;
		private String getterCall;
		private String getterName;
		private boolean notNull;
		private boolean pkMember;
		private boolean reference;
		private String setterName;
		private String simpleName;
		private boolean simpleType;
		private boolean unique;
	}

	public static boolean containsFieldWithType(List<ColumnData> columnData, String typeName) {
		return columnData.stream().anyMatch(cd -> cd.getFieldType().equals(typeName));
	}

}