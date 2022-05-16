package archimedes.codegenerators;

import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * Utility class for find bys.
 * 
 * @author ollie (05.02.2022)
 */
public class FindBys {

	@Accessors(chain = true)
	@Data
	public static class FindByData {
		private String attributeName;
		private String columnName;
		private String converterAttributeName;
		private String converterClassName;
		private String converterPackageName;
		private boolean objectReference;
		private String typeName;
		private String typePackageName;
		private boolean unique;

		public boolean isSimpleType() {
			return List
					.of(
							"boolean",
							"Boolean",
							"byte",
							"Byte",
							"char",
							"Character",
							"double",
							"Double",
							"float",
							"Float",
							"int",
							"Integer",
							"long",
							"Long",
							"short",
							"Short",
							"String")
					.contains(typeName);
		}
	}

}
