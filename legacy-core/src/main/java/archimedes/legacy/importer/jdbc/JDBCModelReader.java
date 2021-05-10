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
import logging.Logger;

/**
 * A class which is able to read the meta data of a database.
 *
 * @author O.Lieshoff
 */
public class JDBCModelReader implements ModelReader {

	private static final Logger log = Logger.getLogger(JDBCModelReader.class);

	private DBObjectFactory factory;
	private DBTypeConverter typeConverter;
	private Connection connection;
	private String schemePattern;
	private boolean ignoreIndices;
	private String[] ignoreTablePatterns;
	private String[] importOnlyTablePatterns;
	private List<ModelReaderListener> listeners = new ArrayList<>();

	/**
	 * Creates a new model reader with the passed parameters.
	 *
	 * @param factory                 An object factory implementation to create the DB objects.
	 * @param typeConverter           A converter for database types.
	 * @param connection              The connection whose data model should be read.
	 * @param schemePattern           The pattern of the scheme names whose data are to read (pass "null" to ignore
	 *                                scheme and load all tables).
	 * @param ignoreIndices           Set this flag to ignore indices while import.
	 * @param ignoreTablePatterns     Patterns of table names which should be returned.
	 * @param importOnlyTablePatterns Patterns of table names which are to import if the table name matches the at least
	 *                                one pattern. The patterns to import only are checked before ignore table patterns
	 *                                (set "*" if all tables are to import).
	 * @throws IllegalArgumentException Passing null value.
	 */
	public JDBCModelReader(
			DBObjectFactory factory,
			DBTypeConverter typeConverter,
			Connection connection,
			String schemePattern,
			boolean ignoreIndices,
			String ignoreTablePatterns,
			String importOnlyTablePatterns) {
		super();
		this.connection = connection;
		this.factory = factory;
		this.ignoreIndices = ignoreIndices;
		this.ignoreTablePatterns = getPatterns(ignoreTablePatterns);
		this.importOnlyTablePatterns = getPatterns(importOnlyTablePatterns);
		this.schemePattern = schemePattern;
		this.typeConverter = typeConverter;
	}

	private String[] getPatterns(String s) {
		if (s == null) {
			return new String[0];
		}
		return Str.splitToList(s, ";").toArray(new String[0]);
	}

	@Override
	public ModelReader addModelReaderListener(ModelReaderListener listener) {
		if (listener != null) {
			this.listeners.add(listener);
		}
		return this;
	}

	protected void fireModelReaderEvent(ModelReaderEvent event) {
		for (ModelReaderListener l : listeners) {
			try {
				l.eventCaught(event);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public DatabaseSO readModel() throws Exception {
		DatabaseMetaData dbmd = this.connection.getMetaData();
		SchemeSO[] schemes = getSchemes(dbmd, this.schemePattern);
		for (SchemeSO scheme : schemes) {
			addTables(dbmd, scheme);
			List<SequenceSO> sequences = getSequences(dbmd);
		}
		return new DatabaseSO().setName("database").addSchemes(schemes);
	}

	private SchemeSO[] getSchemes(DatabaseMetaData dbmd, String schemePattern) throws SQLException {
		List<SchemeSO> schemes = new ArrayList<>();
		ResultSet rs = dbmd.getSchemas(null, schemePattern);
		while (rs.next()) {
			String schemeName = rs.getString("TABLE_SCHEM");
			schemes.add(this.factory.createScheme(schemeName, new ArrayList<>()));
		}
		rs.close();
		if (schemes.isEmpty()) {
			schemes.add(this.factory.createScheme("public", new ArrayList<>()));
		}
		return schemes.toArray(new SchemeSO[schemes.size()]);
	}

	private void addTables(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		loadTables(dbmd, scheme);
		loadColumns(dbmd, scheme);
		loadPrimaryKeys(dbmd, scheme);
		loadForeignKeys(dbmd, scheme);
		if (!this.ignoreIndices) {
			loadIndices(dbmd, scheme.getTables(), scheme);
		} else {
			fireModelReaderEvent(new ModelReaderEvent(1, 1, 5, ModelReaderEventType.INDEX_IMPORT_IGNORED, ""));
		}
	}

	private void loadTables(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		ResultSet rs = dbmd.getTables(null, scheme.getName(), "%", new String[] { "TABLE" });
		List<String> tableNames = new ArrayList<>();
		while (rs.next()) {
			String tableName = rs.getString("TABLE_NAME");
			tableNames.add(tableName);
		}
		int max = tableNames.size();
		int current = 0;
		for (String tableName : tableNames) {
			if (!isMatchingImportOnlyPattern(tableName)) {
				fireModelReaderEvent(
						new ModelReaderEvent(
								current,
								max,
								1,
								ModelReaderEventType.IMPORT_ONLY_PATTERN_NOT_MATCHING,
								tableName));
			} else if (isMatchingIgnorePattern(tableName)) {
				fireModelReaderEvent(
						new ModelReaderEvent(current, max, 1, ModelReaderEventType.IGNORED_BY_PATTERN, tableName));
			} else {
				scheme.addTables(this.factory.createTable(tableName, new ArrayList<>()));
				fireModelReaderEvent(
						new ModelReaderEvent(current, max, 1, ModelReaderEventType.TABLE_ADDED, tableName));
				current++;
			}
		}
		rs.close();
	}

	private boolean isMatchingIgnorePattern(String tableName) {
		for (String pattern : this.ignoreTablePatterns) {
			if (pattern.equals("*") || pattern.equals(tableName)
					|| (pattern.startsWith("*") && pattern.endsWith("*")
							&& tableName.contains(pattern.substring(1, pattern.length() - 1))) //
					|| (pattern.startsWith("*") && tableName.endsWith(pattern.substring(1))) //
					|| (pattern.endsWith("*") && tableName.startsWith(pattern.substring(0, pattern.length() - 1)))) {
				return true;
			}
		}
		return false;
	}

	private boolean isMatchingImportOnlyPattern(String tableName) {
		for (String pattern : this.importOnlyTablePatterns) {
			if (pattern.equals("*") || pattern.equals(tableName)
					|| (pattern.startsWith("*") && pattern.endsWith("*")
							&& tableName.contains(pattern.substring(1, pattern.length() - 1))) //
					|| (pattern.startsWith("*") && tableName.endsWith(pattern.substring(1))) //
					|| (pattern.endsWith("*") && tableName.startsWith(pattern.substring(0, pattern.length() - 1)))) {
				return true;
			}
		}
		return false;
	}

	private void loadColumns(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		int max = scheme.getTables().size();
		int current = 0;
		for (TableSO table : scheme.getTables()) {
			ResultSet rs = dbmd.getColumns(null, scheme.getName(), table.getName(), "%");
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
					table
							.addColumns(
									this.factory
											.createColumn(
													columnName,
													this.typeConverter.convert(dataType, columnSize, decimalDigits),
													nullable));
				} catch (Exception e) {
					log
							.error(
									"Problems while reading column '" + rs.getString("TABLE_SCHEM") + "."
											+ table.getName() + "." + columnName + "' (" + typeName + " {" + dataType
											+ "}): " + e.getMessage(),
									e);
				}
			}
			rs.close();
			fireModelReaderEvent(
					new ModelReaderEvent(current, max, 2, ModelReaderEventType.COLUMNS_ADDED, table.getName()));
			current++;
		}
	}

	private void loadPrimaryKeys(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		int max = scheme.getTables().size();
		int current = 0;
		for (TableSO table : scheme.getTables()) {
			ResultSet rs = dbmd.getPrimaryKeys(null, scheme.getName(), table.getName());
			while (rs.next()) {
				String columnName = rs.getString("COLUMN_NAME");
				try {
					ColumnSO pkColumn = this.getColumnByName(columnName, table);
					pkColumn.setNullable(false);
					pkColumn.setPkMember(true);
				} catch (IllegalArgumentException e) {
					log.warn("warning: " + e.getMessage());
				}
			}
			rs.close();
			fireModelReaderEvent(
					new ModelReaderEvent(current, max, 3, ModelReaderEventType.PRIMARY_KEY_ADDED, table.getName()));
			current++;
		}
	}

	private void loadForeignKeys(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		int max = scheme.getTables().size();
		int current = 0;
		for (TableSO table : scheme.getTables()) {
			ResultSet rs = dbmd.getImportedKeys(null, scheme.getName(), table.getName());
			while (rs.next()) {
				try {
					String fkName = rs.getString("FK_NAME");
					String fkTableName = rs.getString("FKTABLE_NAME");
					String fkColumnName = rs.getString("FKCOLUMN_NAME");
					String pkTableName = rs.getString("PKTABLE_NAME");
					String pkColumnName = rs.getString("PKCOLUMN_NAME");
					table.getForeignKeyByName(fkName).ifPresentOrElse(fk -> {
						log.info("FK: " + fk);
					}, () -> {
						ForeignKeySO fk = new ForeignKeySO().setName(fkName);
						TableSO referencedTable = scheme
								.getTableByName(pkTableName)
								.orElseThrow(() -> new TableNotFoundException(pkTableName));
						TableSO referencingTable = scheme
								.getTableByName(fkTableName)
								.orElseThrow(() -> new TableNotFoundException(fkTableName));
						fk
								.addReferences(
										new ReferenceSO()
												.setReferencedColumn(
														referencedTable
																.getColumnByName(pkColumnName)
																.orElseThrow(
																		() -> new ColumnNotFoundException(
																				pkTableName,
																				pkColumnName)))
												.setReferencingColumn(
														referencingTable
																.getColumnByName(fkColumnName)
																.orElseThrow(
																		() -> new ColumnNotFoundException(
																				fkTableName,
																				fkColumnName))));
						table.addForeignKeys(fk);
						log.info("FK created: " + fk);
					});
				} catch (Exception e) {
					log.error("ERROR while reading from model: " + e.getMessage(), e);
				}
			}
			rs.close();
			fireModelReaderEvent(
					new ModelReaderEvent(current, max, 4, ModelReaderEventType.FOREIGN_KEY_ADDED, table.getName()));
			current++;
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

	private void loadIndices(DatabaseMetaData dbmd, List<TableSO> tables, SchemeSO scheme) throws SQLException {
		int max = tables.size();
		int current = 0;
		for (TableSO table : tables) {
			// TODO: Set "false, false" to "true, true" for large oracle tables.
			ResultSet rs = dbmd.getIndexInfo(null, scheme.getName(), table.getName(), false, false);
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
							log
									.error(
											LocalDateTime.now() + " - Index '" + indexName + "'not added: "
													+ iae.getMessage(),
											iae);
						}
					}
				}
			}
			rs.close();
			fireModelReaderEvent(
					new ModelReaderEvent(current, max, 5, ModelReaderEventType.INDEX_ADDED, table.getName()));
			current++;
		}
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

	@Override
	public void removeModelReaderListener(ModelReaderListener listener) {
		if (listener != null) {
			this.listeners.remove(listener);
		}
	}

}