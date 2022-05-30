package archimedes.codegenerators;

import lombok.Data;
import lombok.experimental.Accessors;

public class Subclasses {

	@Accessors(chain = true)
	@Data
	public static class SubclassData {
		private String converterAttributeName;
		private String converterClassName;
		private String converterClassNameQualified;
		private String dboClassName;
		private String dboClassNameQualified;
		private String dboRepositoryAttributeName;
		private String dboRepositoryClassName;
		private String dboRepositoryClassNameQualified;
		private String modelClassName;
		private String modelClassNameQualified;
	}

}