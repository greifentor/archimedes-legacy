package archimedes.legacy.importer.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An implementation of the JDBCImportDatabaseMetaDataPort for mySQL.
 *
 * @author ollie (14.05.2021)
 */
public class OracleJDBCImportDatabaseMetaDataAdapter extends DefaultJDBCImportDatabaseMetaDataAdapter {

	@Override
	public ResultSet getIndices(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getIndexInfo(null, schemeName, tableName, true, true);
	}

}