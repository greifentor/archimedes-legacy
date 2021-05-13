package archimedes.legacy.importer.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * An interface for JDBC access for getting database meta data via a JDBC driver.
 *
 * @author ollie (13.05.2021)
 */
public interface JDBCImportDatabaseMetaDataPort {

	/**
	 * Returns a result set with the column information about a specific table.
	 * 
	 * @param dbmd       The database meta data access.
	 * @param schemaName The name of the schema which the table is a part of.
	 * @param tableName  The name of the table whose column information are to read.
	 * @return A result set with the column information about a specific table.
	 * @throws SQLException If an error occurs on reading the columns.
	 */
	ResultSet getColumns(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException;

	/**
	 * Returns a result set with the foreign key information about a specific table.
	 * 
	 * @param dbmd       The database meta data access.
	 * @param schemaName The name of the schema which the table is a part of.
	 * @param tableName  The name of the table whose foreign key information are to read.
	 * @return A result set with the foreign key information about a specific table.
	 * @throws SQLException If an error occurs on reading the indices.
	 */
	ResultSet getImportedKeys(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException;

	/**
	 * Returns a result set with the index information about a specific table.
	 * 
	 * @param dbmd       The database meta data access.
	 * @param schemaName The name of the schema which the table is a part of.
	 * @param tableName  The name of the table whose index information are to read.
	 * @return A result set with the index information about a specific table.
	 * @throws SQLException If an error occurs on reading the indices.
	 */
	ResultSet getIndices(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException;

	/**
	 * Returns a result set with the primary key information about a specific table.
	 * 
	 * @param dbmd       The database meta data access.
	 * @param schemaName The name of the schema which the table is a part of.
	 * @param tableName  The name of the table whose primary key information are to read.
	 * @return A result set with the primary key information about a specific table.
	 * @throws SQLException If an error occurs on reading the indices.
	 */
	ResultSet getPrimaryKeys(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException;

	/**
	 * Returns a result set with the scheme information about a specific table.
	 * 
	 * @param dbmd          The database meta data access.
	 * @param schemaPattern A pattern of the scheme names which are to return.
	 * @return A result set with the scheme information about a specific table.
	 * @throws SQLException If an error occurs on reading the indices.
	 */
	ResultSet getSchemas(DatabaseMetaData dbmd, String schemePattern) throws SQLException;

	/**
	 * Returns a result set with the table information about a specific scheme.
	 * 
	 * @param dbmd       The database meta data access.
	 * @param schemaName The name of the scheme whose tables should be returned.
	 * @return A result set with the table information about a specific table.
	 * @throws SQLException If an error occurs on reading the indices.
	 */
	ResultSet getTables(DatabaseMetaData dbmd, String schemeName) throws SQLException;

}