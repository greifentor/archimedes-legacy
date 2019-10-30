package archimedes.legacy.importer.jdbc;

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

	public static final String FIELD_DRIVER_NAME = "FIELD_DRIVER_NAME";
	public static final String FIELD_URL = "FIELD_URL";
	public static final String FIELD_USER_NAME = "FIELD_USER_NAME";
	public static final String FIELD_PASSWORD = "FIELD_PASSWORD";

	private String driverName;
	private String url;
	private String userName;
	private String password;

}