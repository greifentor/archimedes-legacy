package archimedes.codegenerators;

import lombok.Data;
import lombok.experimental.Accessors;

public class ListAccess {

	@Accessors(chain = true)
	@Data
	public static class ListAccessData {
		private String fieldName;
		private String fieldNameCamelCase;
		private String typeName;
	}

}