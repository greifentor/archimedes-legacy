/*
 * STFDatabaseConnectionWriter.java
 *
 * 15.01.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.connections.DatabaseConnection;
import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.scheme.stf.handler.STFDatabaseConnectionHandler;
import corent.files.StructuredTextFile;

/**
 * A writer for database connections to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 15.01.2015 - Added.
 */

public class STFDatabaseConnectionWriter extends STFDatabaseConnectionHandler {

	private static final Logger LOG = Logger.getLogger(STFDatabaseConnectionWriter.class);

	/**
	 * Writes the passed database connections to the STF.
	 * 
	 * @param databaseConnections
	 *            The database connections which are to store in the passed STF.
	 * @param stf
	 *            The STF which is to update with the database connection data.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public void write(StructuredTextFile stf, DatabaseConnection[] databaseConnections, DiagramSaveMode dsm) {
		if (dsm == DiagramSaveMode.REGULAR) {
			stf.writeLong(this.createPath(COUNT), databaseConnections.length);
			for (int i = 0; i < databaseConnections.length; i++) {
				stf.writeStr(this.createPath(i, NAME), toHTML(databaseConnections[i].getName()));
				stf.writeStr(this.createPath(i, DB_EXEC_MODE), toHTML(databaseConnections[i].getDBMode().name()));
				stf.writeStr(this.createPath(i, DRIVER), toHTML(databaseConnections[i].getDriver()));
				stf.writeStr(this.createPath(i, QUOTE), toHTML(databaseConnections[i].getQuote()));
				stf.writeStr(this.createPath(i, SET_DOMAINS), new Boolean(databaseConnections[i].isSetDomains())
						.toString());
				stf.writeStr(this.createPath(i, SET_NOT_NULL), new Boolean(databaseConnections[i].isSetNotNull())
						.toString());
				stf.writeStr(this.createPath(i, SET_REFERENCES), new Boolean(databaseConnections[i].isSetReferences())
						.toString());
				stf.writeStr(this.createPath(i, URL), toHTML(databaseConnections[i].getUrl()));
				stf.writeStr(this.createPath(i, USER_NAME), toHTML(databaseConnections[i].getUserName()));
				LOG.debug("database connection written: " + databaseConnections[i].getName());
			}
		}
	}

}