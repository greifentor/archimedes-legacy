package archimedes.ucg.yaml;

import java.util.List;

public final class UcgYamlNames {

	public static final String COLUMN_OPTION_ENUM = "enum";
	public static final String COLUMN_OPTION_JAVA_TYPE = "java-type";
	public static final String DOMAIN_OPTION_ENUM = "ENUM";
	public static final String PROPERTY_DECIMAL_PLACES = "decimal-places";
	public static final String PROPERTY_IMPORT = "import";
	public static final String PROPERTY_LENGTH = "length";
	public static final String PROPERTY_NULLABLE = "nullable";
	public static final String PROPERTY_PRIMARY_KEY = "primary-key";
	public static final String PROPERTY_UNIQUE = "unique";

	public static final List<String> RESERVED_COLUMN_OPTION_NAMES = List.of(COLUMN_OPTION_ENUM, COLUMN_OPTION_JAVA_TYPE);
	public static final List<String> RESERVED_ATTRIBUTE_PROPERTY_NAMES =
			List.of(PROPERTY_NULLABLE, PROPERTY_PRIMARY_KEY, PROPERTY_UNIQUE);

	public static final String ARCHIMEDES_FILE_SUFFIX = ".archimedes.yml";

	private UcgYamlNames() {
	}

	public static String getArchimedesFileName(String modelFileName) {
		String lowerCaseFileName = modelFileName.toLowerCase();
		for (String extension : List.of(".yml", ".yaml")) {
			if (lowerCaseFileName.endsWith(extension)) {
				return modelFileName.substring(0, modelFileName.length() - extension.length()) + ARCHIMEDES_FILE_SUFFIX;
			}
		}
		return modelFileName + ARCHIMEDES_FILE_SUFFIX;
	}

}
