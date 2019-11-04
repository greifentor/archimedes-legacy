/*
 * ModelReader.java
 *
 * 20.09.2018
 *
 * (c) by O.Lieshoff 
 */
package archimedes.legacy.importer.jdbc;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import corentx.util.Str;
import de.ollie.archimedes.alexandrian.service.exception.ColumnNotFoundException;
import de.ollie.archimedes.alexandrian.service.exception.TableNotFoundException;
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.IndexSO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.SequenceSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;

/**
 * A class which is able to read the meta data of a database.
 *
 * @author O.Lieshoff
 */
public class JDBCModelReader implements ModelReader {

	private DBObjectFactory factory;
	private DBTypeConverter typeConverter;
	private Connection connection;
	private String schemeName;
	private boolean ignoreIndices;
	private String[] ignoreTablePatterns;

	/**
	 * Creates a new model reader with the passed parameters.
	 *
	 * @param factory             An object factory implementation to create the DB objects.
	 * @param typeConverter       A converter for database types.
	 * @param connection          The connection whose data model should be read.
	 * @param schemeName          The name of the scheme whose data are to read (pass "null" to ignore scheme and load
	 *                            all tables).
	 * @param ignoreIndices       Set this flag to ignore indices while import.
	 * @param ignoreTablePatterns Patterns of table names which should be returned.
	 * @throws IllegalArgumentException Passing null value.
	 */
	public JDBCModelReader(DBObjectFactory factory, DBTypeConverter typeConverter, Connection connection,
			String schemeName, boolean ignoreIndices, String ignoreTablePatterns) {
		super();
		this.connection = connection;
		this.factory = factory;
		this.ignoreIndices = ignoreIndices;
		this.ignoreTablePatterns = getIgnoreTablePatterns(ignoreTablePatterns);
		this.schemeName = schemeName;
		this.typeConverter = typeConverter;
	}

	private String[] getIgnoreTablePatterns(String s) {
		return Str.splitToList(s, ";").toArray(new String[0]);
	}

	@Override
	public DatabaseSO readModel() throws Exception {
		SchemeSO scheme = this.factory.createScheme(this.schemeName, new ArrayList<>());
		DatabaseMetaData dbmd = this.connection.getMetaData();
		addTables(dbmd, scheme);
		List<SequenceSO> sequences = getSequences(dbmd);
		return new DatabaseSO().setName("database").addScheme(scheme);
	}

	private void addTables(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		loadTables(dbmd, scheme);
		loadColumns(dbmd, scheme);
		loadPrimaryKeys(dbmd, scheme);
		loadForeignKeys(dbmd, scheme);
		if (!this.ignoreIndices) {
			loadIndices(dbmd, scheme.getTables());
		}
	}

	private void loadTables(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		ResultSet rs = dbmd.getTables(null, this.schemeName, "%", new String[] { "TABLE" });
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			if (isMatchingIgnorePattern(tableName)) {
				System.out.println(
						LocalDateTime.now() + " - table ignored: " + rs.getString("TABLE_SCHEM") + "." + tableName);
				continue;
			}
			scheme.addTables(this.factory.createTable(tableName, new ArrayList<>()));
			System.out
					.println(LocalDateTime.now() + " - table added: " + rs.getString("TABLE_SCHEM") + "." + tableName);
		}
		rs.close();
	}

	private boolean isMatchingIgnorePattern(String tableName) {
		for (String pattern : this.ignoreTablePatterns) {
			if ((pattern.startsWith("*") && pattern.endsWith("*")
					&& tableName.contains(pattern.substring(1, pattern.length() - 1))) //
					|| (pattern.startsWith("*") && tableName.endsWith(pattern.substring(1))) //
					|| (pattern.endsWith("*") && tableName.startsWith(pattern.substring(0, pattern.length() - 1)))) {
				return true;
			}
		}
		return false;
	}

	private void loadColumns(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		for (TableSO table : scheme.getTables()) {
			System.out.println(LocalDateTime.now() + " - reading columns for table: " + table.getName());
			ResultSet rs = dbmd.getColumns(null, this.schemeName, table.getName(), "%");
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				String typeName = rs.getString("TYPE_NAME");
				int dataType = rs.getInt("DATA_TYPE");
				boolean nullable = "YES".equalsIgnoreCase(rs.getString("IS_NULLABLE"));
				int columnSize = -1;
				int decimalDigits = -1;
				if ((dataType == Types.CHAR) || (dataType == Types.DECIMAL) || (dataType == Types.FLOAT)
						|| (dataType == Types.LONGVARCHAR) || (dataType == Types.NUMERIC)
						|| (dataType == Types.VARBINARY) || (dataType == Types.VARCHAR)) {
					columnSize = rs.getInt("COLUMN_SIZE");
				}
				if ((dataType == Types.DECIMAL) || (dataType == Types.NUMERIC)) {
					decimalDigits = rs.getInt("DECIMAL_DIGITS");
				}
				try {
					table.addColumns(this.factory.createColumn(columnName,
							this.typeConverter.convert(dataType, columnSize, decimalDigits), nullable));
				} catch (Exception e) {
					System.out.println(
							"Problems while reading column '" + rs.getString("TABLE_SCHEM") + "." + table.getName()
									+ "." + columnName + "' (" + typeName + " {" + dataType + "}): " + e.getMessage());
				}
			}
			rs.close();
		}
	}

	private void loadPrimaryKeys(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		for (TableSO table : scheme.getTables()) {
			System.out.println(LocalDateTime.now() + " - reading primary keys for table: " + table.getName());
			ResultSet rs = dbmd.getPrimaryKeys(null, this.schemeName, table.getName());
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				try {
					ColumnSO pkColumn = this.getColumnByName(columnName, table);
					pkColumn.setNullable(false);
					pkColumn.setPkMember(true);
				} catch (IllegalArgumentException e) {
					System.out.println("warning: " + e.getMessage());
				}
			}
			rs.close();
		}
	}

	private void loadForeignKeys(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		for (TableSO table : scheme.getTables()) {
			System.out.println(LocalDateTime.now() + " - reading foreign keys for table: " + table.getName());
			ResultSet rs = dbmd.getImportedKeys(null, this.schemeName, table.getName());
			while (rs.next()) {
				try {
					String fkName = rs.getString("FK_NAME");
					String fkTableName = rs.getString("FKTABLE_NAME");
					String fkColumnName = rs.getString("FKCOLUMN_NAME");
					String pkTableName = rs.getString("PKTABLE_NAME");
					String pkColumnName = rs.getString("PKCOLUMN_NAME");
					table.getForeignKeyByName(fkName).ifPresentOrElse(fk -> {
						System.out.println(fk);
					}, () -> {
						ForeignKeySO fk = new ForeignKeySO().setName(fkName);
						TableSO referencedTable = scheme.getTableByName(pkTableName)
								.orElseThrow(() -> new TableNotFoundException(pkTableName));
						TableSO referencingTable = scheme.getTableByName(fkTableName)
								.orElseThrow(() -> new TableNotFoundException(fkTableName));
						fk.addReferences(new ReferenceSO()
								.setReferencedColumn(referencedTable.getColumnByName(pkColumnName)
										.orElseThrow(() -> new ColumnNotFoundException(pkTableName, pkColumnName)))
								.setReferencingColumn(referencingTable.getColumnByName(fkColumnName)
										.orElseThrow(() -> new ColumnNotFoundException(fkTableName, fkColumnName))));
						table.addForeignKeys(fk);
						System.out.println("FK created: " + fk);
					});
				} catch (Exception e) {
					System.out.println("ERROR while reading from model: " + e.getMessage());
					e.printStackTrace();
				}
			}
			rs.close();
		}
	}

	private void loadIndices(DatabaseMetaData dbmd, List<TableSO> tables) throws SQLException {
		for (TableSO table : tables) {
			System.out.println(LocalDateTime.now() + " - reading indices for table: " + table.getName());
			// TODO: Set "false, false" to "true, true" for large oracle tables.
			ResultSet rs = dbmd.getIndexInfo(null, this.schemeName, table.getName(), false, false);
			while (rs.next()) {
				boolean nonUniqueIndex = rs.getBoolean("NON_UNIQUE");
				if (nonUniqueIndex) {
					String indexName = rs.getString("INDEX_NAME");
					if (!isAForeignKey(indexName, table)) {
						String columnName = rs.getString("COLUMN_NAME");
						try {
							IndexSO index = getIndexByName(indexName, table);
							ColumnSO column = getColumnByName(columnName, table);
							index.getColumns().add(column);
						} catch (IllegalArgumentException iae) {
							System.out.println(
									LocalDateTime.now() + " - Index '" + indexName + "'not added: " + iae.getMessage());
						}
					}
				}
			}
			rs.close();
		}
	}

	private boolean isAForeignKey(String indexName, TableSO table) {
		for (ForeignKeySO foreignKey : table.getForeignKeys()) {
			if (indexName.contains(foreignKey.getName())) {
				return true;
			}
		}
		return false;
	}

	private IndexSO getIndexByName(String name, TableSO table) {
		for (IndexSO index : table.getIndices()) {
			if (index.getName().equals(name)) {
				return index;
			}
		}
		IndexSO index = this.factory.createIndex(name, new ArrayList<>());
		table.getIndices().add(index);
		return index;
	}

	private ColumnSO getColumnByName(String name, TableSO table) {
		for (ColumnSO column : table.getColumns()) {
			if (column.getName().equals(name)) {
				return column;
			}
		}
		throw new IllegalArgumentException("column '" + name + "' does not exist in table '" + table.getName() + "'.");
	}

	private List<SequenceSO> getSequences(DatabaseMetaData dbmd) throws SQLException {
		List<SequenceSO> sequences = new ArrayList<>();
		return sequences;
	}

}