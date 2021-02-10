package archimedes.legacy.importer.jdbc;

/**
 * Identifiers for model reader event.
 * 
 * @author ollie
 *
 */
public enum ModelReaderEventType {

	COLUMNS_ADDED,
	FOREIGN_KEY_ADDED,
	IGNORED_BY_PATTERN,
	IMPORT_ONLY_PATTERN_NOT_MATCHING,
	INDEX_ADDED,
	INDEX_IMPORT_IGNORED,
	MESSAGE,
	PRIMARY_KEY_ADDED,
	TABLE_ADDED;

}
