package archimedes.codegenerators;

import lombok.Data;
import lombok.experimental.Accessors;

public class Subclasses {

	@Accessors(chain = true)
	@Data
	public static class SubclassData {
		private String converterAttributeName;
		private String converterClassName;
		private String dboClassName;
		private String dboClassNameQualified;
		private String modelClassName;
		private String modelClassNameQualified;
	}

}