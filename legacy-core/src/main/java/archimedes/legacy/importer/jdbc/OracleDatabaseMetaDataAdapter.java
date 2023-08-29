package archimedes.legacy.importer.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 * A specialization of the DefaultDatabaseMetaDataAdapter class for Oracle.
 *
 * @author ollie (29.08.2023)
 */
public class OracleDatabaseMetaDataAdapter extends DefaultDatabaseMetaDataAdapter {

	public OracleDatabaseMetaDataAdapter(JDBCImportDatabaseMetaDataPort jdbcImportDatabaseMetaDataPort) {
		super(jdbcImportDatabaseMetaDataPort);
	}

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
			boolean autoIncrement = false; // "YES".equalsIgnoreCase(rs.getString("IS_AUTOINCREMENT"));
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

}