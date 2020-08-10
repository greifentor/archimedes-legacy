/*
 * DatabaseConnectionRecord.java
 *
 * 02.02.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import archimedes.legacy.connections.DatabaseConnection;

/**
 * A container for database connection with password and base data.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 02.02.2015 - Added.
 */

public class DatabaseConnectionRecord {

	private DatabaseConnection dc = null;
	private DatabaseConnection[] dcs = null;
	private String password = null;

	/**
	 * Creates a new database connection record with the passed parameters.
	 *
	 * @param dc       The database connection record which contains the connection data.
	 * @param dcs      A list of database connections for selection.
	 * @param password The password which is used to connection with database.
	 *
	 * @changed OLI 02.02.2015 - Added.
	 */
	public DatabaseConnectionRecord(DatabaseConnection dc, DatabaseConnection[] dcs, String password) {
		super();
		this.dc = dc;
		this.dcs = dcs;
		this.password = password;
	}

	/**
	 * @changed OLI 10.02.2017 - Added.
	 */
	@Override
	public boolean equals(Object o) {
		return EqualsBuilder.reflectionEquals(this, o);
	}

	/**
	 * Returns the database connection which is represented by the container.
	 *
	 * @return The database connection which is represented by the container.
	 *
	 * @changed OLI 02.02.2015 - Added.
	 */
	public DatabaseConnection getDatabaseConnection() {
		return this.dc;
	}

	/**
	 * Returns the database connections which are selectable by the container.
	 *
	 * @return The database connections which are selectable by the container.
	 *
	 * @changed OLI 02.02.2015 - Added.
	 */
	public DatabaseConnection[] getDatabaseConnections() {
		return this.dcs;
	}

	/**
	 * Returns the password to connect with the database.
	 *
	 * @return The password to connect with the database.
	 *
	 * @changed OLI 02.02.2015 - Added.
	 */
	public String getPassword() {
		return this.password;
	}

	/**
	 * @changed OLI 10.02.2017 - Added.
	 */
	@Override
	public int hashCode() {
		return HashCodeBuilder.reflectionHashCode(this);
	}

	/**
	 * Sets the passed database connection a the new database connection for the record.
	 *
	 * @param dc The new database connection.
	 *
	 * @changed OLI 02.02.2015 - Added.
	 */
	public void setDatabaseConnection(DatabaseConnection dc) {
		this.dc = dc;
	}

	/**
	 * Sets the passed password as the new password for the record.
	 *
	 * @param password The new password for the record.
	 *
	 * @changed OLI 02.02.2015 - Added.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

}