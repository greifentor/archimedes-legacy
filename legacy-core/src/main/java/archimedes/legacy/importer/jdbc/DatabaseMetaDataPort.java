package archimedes.legacy.importer.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * An interface for access to the database meta data.
 *
 * @author ollie (12.05.2021)
 */
public interface DatabaseMetaDataPort {

	@Accessors(chain = true)
	@Data
	public static class ColumnImportInfo {
		private String columnName;
		private int columnSize = -1;
		private int dataType;
		private int decimalDigits = -1;
		private boolean nullable;
		private boolean autoIncrement;
		private String typeName;
	}

	/**
	 * Returns the column information of the passed table.
	 * 
	 * @param dbmd       An access to the database meta data.
	 * @param schemeName The name of the scheme which the table whose column information are to read for is a member.
	 * @param tableName  The name of the table which the column information are to read for.
	 * @return The list with the column information for the passed table.
	 * @throws SQLException If an error occurs on database level.
	 */
	List<ColumnImportInfo> getColumns(DatabaseMetaData dbmd, String schemeName, String tableName) throws SQLException;

	@Accessors(chain = true)
	@Data
	public static class ForeignKeyReferenceImportInfo {
		private String fkName;
		private String fkTableName;
		private String fkColumnName;
		private String pkTableName;
		private String pkColumnName;
	}

	/**
	 * Returns the foreign key reference information for the passed table.
	 * 
	 * @param dbmd       An access to the database meta data.
	 * @param schemeName The name of the scheme which the table whose foreign key reference information are to read for
	 *                   is a member.
	 * @param tableName  The name of the table which the foreign key reference information are to read for.
	 * @return The list with the foreign key reference information for the passed table.
	 * @throws SQLException If an error occurs on database level.
	 */
	List<ForeignKeyReferenceImportInfo> getForeignKeyReferencesInformation(DatabaseMetaData dbmd, String schemeName,
			String tableName) throws SQLException;

	@Accessors(chain = true)
	@Data
	public static class IndexMemberImportInfo {
		private String columnName;
		private String indexName;
	}

	/**
	 * Returns the index information for the passed table.
	 * 
	 * @param dbmd       An access to the database meta data.
	 * @param schemeName The name of the scheme which the table whose index information are to read for is a member.
	 * @param tableName  The name of the table which the index information are to read for.
	 * @return The list with the index information for the passed table.
	 * @throws SQLException If an error occurs on database level.
	 */
	List<IndexMemberImportInfo> getIndexInformation(DatabaseMetaData dbmd, String schemeName, String tableName)
			throws SQLException;

	/**
	 * Returns the primary key column names for the passed table.
	 * 
	 * @param dbmd       An access to the database meta data.
	 * @param schemeName The name of the scheme which the table whose primary key column names are to read for is a
	 *                   member.
	 * @param tableName  The name of the table which the primary key column names are to read for.
	 * @return The list with the primary key column names for the passed table.
	 * @throws SQLException If an error occurs on database level.
	 */
	List<String> getPrimaryKeyColumnNames(DatabaseMetaData dbmd, String schemeName, String tableName)
			throws SQLException;

	/**
	 * Returns the scheme names of the database.
	 * 
	 * @param dbmd          An access to the database meta data.
	 * @param schemePattern A pattern for the schemes to read. Pass a null value or "*" to read all schemes present in
	 *                      the database.
	 * @return The list with the scheme names of the database.
	 * @throws SQLException If an error occurs on database level.
	 */
	List<String> getSchemeNames(DatabaseMetaData dbmd, String schemePattern) throws SQLException;

	@Accessors(chain = true)
	@Data
	public static class SequenceImportInfo {
		private String name;
	}

	/**
	 * Returns the sequences of the scheme.
	 * 
	 * @param dbmd       An access to the database meta data.
	 * @param schemeName The name of the scheme which the sequences are to read for.
	 * @return The list of sequences related to the passed scheme.
	 * @throws SQLException If an error occurs on database level.
	 */
	List<SequenceImportInfo> getSequences(DatabaseMetaData dbmd, String schemeName) throws SQLException;

	/**
	 * Returns the names of the tables which are part of the passed scheme.
	 * 
	 * @param dbmd       An access to the database meta data.
	 * @param schemeName The name of the scheme whose table names are to read.
	 * @return The list with the schemes of the database.
	 * @throws SQLException If an error occurs on database level.
	 */
	List<String> getTableNames(DatabaseMetaData dbmd, String schemeName) throws SQLException;

}