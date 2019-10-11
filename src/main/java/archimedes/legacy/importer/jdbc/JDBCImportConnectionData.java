package archimedes.legacy.importer.jdbc;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.experimental.FieldNameConstants;

/**
 * A container for JDBC import connection data.
 *
 * @author ollie (11.10.2019)
 */
@Accessors(chain = true)
@Data
@NoArgsConstructor
@FieldNameConstants
public class JDBCImportConnectionData {

	private String driverName;
	private String url;
	private String userName;
	private String password;

}