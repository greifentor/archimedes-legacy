package archimedes.legacy.importer.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An implementation of the JDBCImportDatabaseMetaDataPort for mySQL.
 *
 * @author ollie (14.05.2021)
 */
public class MySQLJDBCImportDatabaseMetaDataAdapter implements JDBCImportDatabaseMetaDataPort {

	@Override
	public ResultSet getColumns(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getColumns(schemeName, null, tableName, "%");
	}

	@Override
	public ResultSet getImportedKeys(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getImportedKeys(schemeName, null, tableName);
	}

	@Override
	public ResultSet getIndices(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getIndexInfo(schemeName, null, tableName, false, false);
	}

	@Override
	public ResultSet getPrimaryKeys(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException {
		return dbmd.getPrimaryKeys(schemeName, null, tableName);
	}

	@Override
	public ResultSet getSchemas(DatabaseMetaData dbmd, String schemePattern) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getTables(DatabaseMetaData dbmd, String schemeName) throws SQLException {
		return dbmd.getTables(schemeName, null, "%", new String[] { "TABLE" });
	}

}