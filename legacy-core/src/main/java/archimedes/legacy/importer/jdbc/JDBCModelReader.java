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
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import archimedes.legacy.importer.jdbc.DatabaseMetaDataPort.ColumnImportInfo;
import archimedes.legacy.importer.jdbc.DatabaseMetaDataPort.ForeignKeyReferenceImportInfo;
import archimedes.legacy.importer.jdbc.DatabaseMetaDataPort.IndexMemberImportInfo;
import corent.db.DBExecMode;
import corentx.util.Str;
import de.ollie.archimedes.alexandrian.service.exception.ColumnNotFoundException;
import de.ollie.archimedes.alexandrian.service.exception.TableNotFoundException;
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.IndexSO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
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
	private DatabaseMetaDataPort databaseMetaDataPort = new DefaultDatabaseMetaDataAdapter(
			new DefaultJDBCImportDatabaseMetaDataAdapter());

	/**
	 * Creates a new model reader with the passed parameters.
	 *
	 * @param factory                 An object factory implementation to create the
	 *                                DB objects.
	 * @param typeConverter           A converter for database types.
	 * @param connection              The connection whose data model should be
	 *                                read.
	 * @param schemePattern           The pattern of the scheme names whose data are
	 *                                to read (pass "null" to ignore scheme and load
	 *                                all tables).
	 * @param ignoreIndices           Set this flag to ignore indices while import.
	 * @param ignoreTablePatterns     Patterns of table names which should be
	 *                                returned.
	 * @param importOnlyTablePatterns Patterns of table names which are to import if
	 *                                the table name matches the at least one
	 *                                pattern. The patterns to import only are
	 *                                checked before ignore table patterns (set "*"
	 *                                if all tables are to import).
	 * @param dbExecMode              The database exec mode.
	 * @throws IllegalArgumentException Passing null value.
	 */
	public JDBCModelReader(DBObjectFactory factory, DBTypeConverter typeConverter, Connection connection,
			String schemePattern, boolean ignoreIndices, String ignoreTablePatterns, String importOnlyTablePatterns,
			DBExecMode dbExecMode) {
		super();
		this.connection = connection;
		this.factory = factory;
		this.ignoreIndices = ignoreIndices;
		this.ignoreTablePatterns = getPatterns(ignoreTablePatterns);
		this.importOnlyTablePatterns = getPatterns(importOnlyTablePatterns);
		this.schemePattern = schemePattern;
		this.typeConverter = typeConverter;
		if (dbExecMode == DBExecMode.MYSQL) {
			this.databaseMetaDataPort = new MySQLDatabaseMetaDataAdapter(new MySQLJDBCImportDatabaseMetaDataAdapter());
		}
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
		List<SchemeSO> schemes = new ArrayList<>();
		for (String schemeName : databaseMetaDataPort.getSchemeNames(dbmd, schemePattern)) {
			SchemeSO scheme = factory.createScheme(schemeName, new ArrayList<>());
			schemes.add(scheme);
			addTables(dbmd, scheme);
		}
		return new DatabaseSO().setName("database").addSchemes(schemes);
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
		List<String> tableNames = databaseMetaDataPort.getTableNames(dbmd, scheme.getName());
		int max = tableNames.size();
		int current = 0;
		for (String tableName : tableNames) {
			if (!isMatchingImportOnlyPattern(tableName)) {
				fireModelReaderEvent(new ModelReaderEvent(current, max, 1,
						ModelReaderEventType.IMPORT_ONLY_PATTERN_NOT_MATCHING, tableName));
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
			for (ColumnImportInfo columnInfo : databaseMetaDataPort.getColumns(dbmd, scheme.getName(),
					table.getName())) {
				try {
					table.addColumns(factory.createColumn(
							columnInfo.getColumnName(), typeConverter.convert(columnInfo.getDataType(),
									columnInfo.getColumnSize(), columnInfo.getDecimalDigits()),
							columnInfo.isNullable(), columnInfo.isAutoIncrement()));
				} catch (Exception e) {
					log.error("Problems while reading column '" + scheme.getName() + "." + table.getName() + "."
							+ columnInfo.getColumnName() + "' (" + columnInfo.getDataType() + " {"
							+ columnInfo.getDataType() + "}): " + e.getMessage(), e);
				}
			}
			fireModelReaderEvent(
					new ModelReaderEvent(current, max, 2, ModelReaderEventType.COLUMNS_ADDED, table.getName()));
			current++;
		}
	}

	private void loadPrimaryKeys(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		int max = scheme.getTables().size();
		int current = 0;
		for (TableSO table : scheme.getTables()) {
			for (String columnName : databaseMetaDataPort.getPrimaryKeyColumnNames(dbmd, scheme.getName(),
					table.getName())) {
				try {
					ColumnSO pkColumn = this.getColumnByName(columnName, table);
					pkColumn.setNullable(false);
					pkColumn.setPkMember(true);
				} catch (IllegalArgumentException e) {
					log.warn("warning: " + e.getMessage());
				}
			}
			fireModelReaderEvent(
					new ModelReaderEvent(current, max, 3, ModelReaderEventType.PRIMARY_KEY_ADDED, table.getName()));
			current++;
		}

	}

	private void loadForeignKeys(DatabaseMetaData dbmd, SchemeSO scheme) throws SQLException {
		int max = scheme.getTables().size();
		int current = 0;
		for (TableSO table : scheme.getTables()) {
			for (ForeignKeyReferenceImportInfo fkImportInfo : databaseMetaDataPort
					.getForeignKeyReferencesInformation(dbmd, scheme.getName(), table.getName())) {
				try {
					table.getForeignKeyByName(fkImportInfo.getFkName()).ifPresentOrElse(fk -> log.info("FK: " + fk),
							() -> {
								ForeignKeySO fk = new ForeignKeySO().setName(fkImportInfo.getFkName());
								TableSO referencedTable = scheme.getTableByName(fkImportInfo.getPkTableName())
										.orElseThrow(() -> new TableNotFoundException(fkImportInfo.getPkTableName()));
								TableSO referencingTable = scheme.getTableByName(fkImportInfo.getFkTableName())
										.orElseThrow(() -> new TableNotFoundException(fkImportInfo.getFkTableName()));
								fk.addReferences(new ReferenceSO()
										.setReferencedColumn(referencedTable
												.getColumnByName(fkImportInfo.getPkColumnName())
												.orElseThrow(() -> new ColumnNotFoundException(
														fkImportInfo.getPkTableName(), fkImportInfo.getPkColumnName())))
										.setReferencingColumn(
												referencingTable.getColumnByName(fkImportInfo.getFkColumnName())
														.orElseThrow(() -> new ColumnNotFoundException(
																fkImportInfo.getFkTableName(),
																fkImportInfo.getFkColumnName()))));
								table.addForeignKeys(fk);
								log.info("FK created: " + fk);
							});
				} catch (Exception e) {
					log.error("ERROR while reading from model: " + e.getMessage(), e);
				}
			}
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
			for (IndexMemberImportInfo indexInfo : databaseMetaDataPort.getIndexInformation(dbmd, scheme.getName(),
					table.getName())) {
				if (!isAForeignKey(indexInfo.getIndexName(), table)) {
					try {
						IndexSO index = getIndexByName(indexInfo.getIndexName(), table);
						ColumnSO column = getColumnByName(indexInfo.getColumnName(), table);
						index.getColumns().add(column);
					} catch (IllegalArgumentException iae) {
						log.error(LocalDateTime.now() + " - Index '" + indexInfo.getIndexName() + "'not added: "
								+ iae.getMessage(), iae);
					}
				}
			}
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

	@Override
	public void removeModelReaderListener(ModelReaderListener listener) {
		if (listener != null) {
			this.listeners.remove(listener);
		}
	}

}