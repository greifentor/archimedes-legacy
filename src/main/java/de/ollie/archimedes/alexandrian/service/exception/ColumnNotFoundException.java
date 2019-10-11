package de.ollie.archimedes.alexandrian.service.exception;

/**
 * An exception for error which are caused by a not found table.
 *
 * @author ollie (29.09.2019)
 */
public class ColumnNotFoundException extends RuntimeException {

	private String columnName;
	private String tableName;

	/**
	 * Create a new Exception.
	 * 
	 * @param tableName  The name of the table which could not be found.
	 * @param columnName The name of the column which could not be found.
	 */
	public ColumnNotFoundException(String tableName, String columnName) {
		super();
		this.columnName = columnName;
		this.tableName = tableName;
	}

	/**
	 * Returns the name of the column which could not be found.
	 * 
	 * @return The name of the column which could not be found.
	 */
	public String getColumnName() {
		return this.columnName;
	}

	/**
	 * Returns the name of the table which could not be found.
	 * 
	 * @return The name of the table which could not be found.
	 */
	public String getTableName() {
		return this.tableName;
	}

}