package archimedes.legacy.importer.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import lombok.RequiredArgsConstructor;

/**
 * A default implementation of the "DatabaseMetaDataPort".
 *
 * @author ollie (12.05.2021)
 */
@RequiredArgsConstructor
public class DefaultDatabaseMetaDataAdapter implements DatabaseMetaDataPort {

	protected final JDBCImportDatabaseMetaDataPort jdbcImportDatabaseMetaDataPort;

	@Override
	public List<ColumnImportInfo> getColumns(DatabaseMetaData dbmd, String schemeName, String tableName)
			throws SQLException {
		List<ColumnImportInfo> columnImportInfos = new ArrayList<>();
		ResultSet rs = jdbcImportDatabaseMetaDataPort.getColumns(dbmd, schemeName, tableName);
		while (rs.next()) {
			String columnName = rs.getString("COLUMN_NAME");
			String typeName = rs.getString("TYPE_NAME");
			int dataType = rs.getInt("DATA_TYPE");
			boolean nullable = "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"));
			boolean autoIncrement = "YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"));
			int columnSize = -1;
			int decimalDigits = -1;
			if ((dataType == Types.CHAR) || (dataType == Types.DECIMAL) || (dataType == Types.FLOAT)
					|| (dataType == Types.LONGVARCHAR) || (dataType == Types.NUMERIC) || (dataType == Types.VARBINARY)
					|| (dataType == Types.VARCHAR)) {
				columnSize = rs.getInt("COLUMN_SIZE");
			}
			if ((dataType == Types.DECIMAL) || (dataType == Types.NUMERIC)) {
				decimalDigits = rs.getInt("DECIMAL_DIGITS");
			}
			columnImportInfos
					.add(
							new ColumnImportInfo()
									.setAutoIncrement(autoIncrement)
									.setColumnName(columnName)
									.setColumnSize(columnSize)
									.setDataType(dataType)
									.setDecimalDigits(decimalDigits)
									.setNullable(nullable)
									.setTypeName(typeName));
		}
		rs.close();
		return columnImportInfos;
	}

	@Override
	public List<ForeignKeyReferenceImportInfo> getForeignKeyReferencesInformation(DatabaseMetaData dbmd,
			String schemeName, String tableName) throws SQLException {
		List<ForeignKeyReferenceImportInfo> foreignKeyReferences = new ArrayList<>();
		ResultSet rs = jdbcImportDatabaseMetaDataPort.getImportedKeys(dbmd, schemeName, tableName);
		while (rs.next()) {
			String fkName = rs.getString("FK_NAME");
			String fkTableName = rs.getString("FKTABLE_NAME");
			String fkColumnName = rs.getString("FKCOLUMN_NAME");
			String pkTableName = rs.getString("PKTABLE_NAME");
			String pkColumnName = rs.getString("PKCOLUMN_NAME");
			foreignKeyReferences
					.add(
							new ForeignKeyReferenceImportInfo()
									.setFkColumnName(fkColumnName)
									.setFkName(fkName)
									.setFkTableName(fkTableName)
									.setPkColumnName(pkColumnName)
									.setPkTableName(pkTableName));
		}
		rs.close();
		return foreignKeyReferences;
	}

	@Override
	public List<IndexMemberImportInfo> getIndexInformation(DatabaseMetaData dbmd, String schemeName, String tableName)
			throws SQLException {
		List<IndexMemberImportInfo> indexInfos = new ArrayList<>();
		ResultSet rs = jdbcImportDatabaseMetaDataPort.getIndices(dbmd, schemeName, tableName);
		while (rs.next()) {
			boolean nonUniqueIndex = rs.getBoolean("NON_UNIQUE");
			if (nonUniqueIndex) {
				String indexName = rs.getString("INDEX_NAME");
				String columnName = rs.getString("COLUMN_NAME");
				indexInfos.add(new IndexMemberImportInfo().setColumnName(columnName).setIndexName(indexName));
			}
		}
		rs.close();
		return indexInfos;
	}

	@Override
	public List<String> getPrimaryKeyColumnNames(DatabaseMetaData dbmd, String schemeName, String tableName)
			throws SQLException {
		List<String> pkColumnNames = new ArrayList<>();
		ResultSet rs = jdbcImportDatabaseMetaDataPort.getPrimaryKeys(dbmd, schemeName, tableName);
		while (rs.next()) {
			pkColumnNames.add(rs.getString("COLUMN_NAME"));
		}
		rs.close();
		return pkColumnNames;
	}

	@Override
	public List<String> getSchemeNames(DatabaseMetaData dbmd, String schemePattern) throws SQLException {
		List<String> schemes = new ArrayList<>();
		ResultSet rs = jdbcImportDatabaseMetaDataPort.getSchemas(dbmd, schemePattern);
		while (rs.next()) {
			schemes.add(rs.getString("TABLE_SCHEM"));
		}
		rs.close();
		return schemes;
	}

	@Override
	public List<SequenceImportInfo> getSequences(DatabaseMetaData dbmd, String schemeName) throws SQLException {
		return new ArrayList<>();
	}

	@Override
	public List<String> getTableNames(DatabaseMetaData dbmd, String schemeName) throws SQLException {
		ResultSet rs = jdbcImportDatabaseMetaDataPort.getTables(dbmd, schemeName);
		List<String> tableNames = new ArrayList<>();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			tableNames.add(tableName);
		}
		rs.close();
		return tableNames;
	}

}