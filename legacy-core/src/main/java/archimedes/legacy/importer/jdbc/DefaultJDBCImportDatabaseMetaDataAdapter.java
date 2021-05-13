package archimedes.legacy.importer.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 
 *
 * @author ollie (13.05.2021)
 */
public class DefaultJDBCImportDatabaseMetaDataAdapter implements JDBCImportDatabaseMetaDataPort {

	@Override
	public ResultSet getColumns(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getColumns(null, schemeName, tableName, "%");
	}

	@Override
	public ResultSet getImportedKeys(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getImportedKeys(null, schemeName, tableName);
	}

	@Override
	public ResultSet getIndices(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getIndexInfo(null, schemeName, tableName, false, false);
	}

	@Override
	public ResultSet getPrimaryKeys(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getPrimaryKeys(null, schemeName, tableName);
	}

	@Override
	public ResultSet getSchemas(DatabaseMetaData dbmd, String schemePattern) throws SQLException {
		return dbmd.getSchemas(null, schemePattern);
	}

	@Override
	public ResultSet getTables(DatabaseMetaData dbmd, String schemeName) throws SQLException {
		return dbmd.getTables(null, schemeName, "%", new String[] { "TABLE" });
	}

}