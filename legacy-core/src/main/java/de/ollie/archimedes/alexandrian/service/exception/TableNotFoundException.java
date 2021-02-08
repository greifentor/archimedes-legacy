package de.ollie.archimedes.alexandrian.service.exception;

/**
 * An exception for error which are caused by a not found table.
 *
 * @author ollie (29.09.2019)
 */
public class TableNotFoundException extends RuntimeException {

	private String tableName;

	/**
	 * Create a new Exception.
	 * 
	 * @param tableName The name of the table which could not be found.
	 */
	public TableNotFoundException(String tableName) {
		super();
		this.tableName = tableName;
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