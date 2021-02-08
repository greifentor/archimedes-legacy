package archimedes.legacy.importer.jdbc;

import archimedes.connections.DatabaseConnection;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * A container for JDBC import connection data.
 *
 * @author ollie (11.10.2019)
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class JDBCImportConnectionData {

	public static final String FIELD_ADJUSTMENT = "FIELD_ADJUSTMENT";
	public static final String FIELD_CONNECTION = "FIELD_CONNECTION";
	public static final String FIELD_PASSWORD = "FIELD_PASSWORD";
	public static final String FIELD_IGNORE_INDICES = "FIELD_IGNORE_INDICES";
	public static final String FIELD_SCHEMA = "FIELD_SCHEMA";
	public static final String FIELD_IGNORE_TABLES_PATTERNS = "FIELD_IGNORE_TABLES_PATTERNS";
	public static final String FIELD_IMPORT_ONLY_TABLES_PATTERNS = "FIELD_IMPORT_ONLY_TABLES_PATTERNS";

	public enum Adjustment {
		LEFT_BY_NAME, //
		CENTER_BY_REFERENCE_COUNT
	}

	private Adjustment adjustment;
	private DatabaseConnection connection;
	private DatabaseConnection[] connections;
	private String password;
	private boolean ignoreIndices = false;
	private String schema;
	private String ignoreTablePatterns;
	private String importOnlyTablePatterns = "*";

}