package archimedes.legacy.importer.jdbc;

import java.sql.Connection;

import archimedes.legacy.importer.jdbc.JDBCImportConnectionData.Adjustment;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * @author ollie (18.02.2021)
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
public class JDBCImportData {

	private Adjustment adjustment;
	private Connection connection;
	private String password;
	private boolean ignoreIndices = false;
	private String schema;
	private String ignoreTablePatterns;
	private String importOnlyTablePatterns = "*";
	private String options = "";

}