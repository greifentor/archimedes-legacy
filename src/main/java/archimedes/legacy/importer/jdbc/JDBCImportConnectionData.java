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

	public static final String FIELD_CONNECTION = "FIELD_CONNECTION";
	public static final String FIELD_PASSWORD = "FIELD_PASSWORD";
	public static final String FIELD_IGNORE_INDICES = "FIELD_IGNORE_INDICES";
	public static final String FIELD_SCHEMA = "FIELD_SCHEMA";
	public static final String FIELD_IGNORE_TABLES_PATTERNS = "FIELD_IGNORE_TABLES_PATTERNS";

	private DatabaseConnection connection;
	private DatabaseConnection[] connections;
	private String password;
	private boolean ignoreIndices = false;
	private String schema;
	private String ignoreTablePatterns;

}