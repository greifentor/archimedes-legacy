package archimedes.codegenerators;

import lombok.Data;
import lombok.experimental.Accessors;

public class ListAccess {

	@Accessors(chain = true)
	@Data
	public static class ListAccessConverterData {
		private String attributeName;
		private String className;
		private String packageName;

		public String getQualifiedClassName() {
			return getPackageName() + "." + getClassName();
		}
	}

	@Accessors(chain = true)
	@Data
	public static class ListAccessData {
		private String fieldName;
		private String fieldNameCamelCase;
		private String typeName;
		private String typeQualifiedName;
		private ListAccessConverterData converterData;
	}

}