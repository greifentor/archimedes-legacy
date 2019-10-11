/*
 * STFDatabaseConnectionReader.java
 *
 * 15.01.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import org.apache.log4j.Logger;

import archimedes.connections.DatabaseConnection;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.stf.handler.STFDatabaseConnectionHandler;
import corent.db.DBExecMode;
import corent.files.StructuredTextFile;

/**
 * A reader for database connection from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 15.01.2015 - Added.
 */

public class STFDatabaseConnectionReader extends STFDatabaseConnectionHandler {

	static Logger log = Logger.getLogger(STFDatabaseConnectionReader.class);

	/**
	 * Updates the database connections in the passed data model by the
	 * information stored in the STF.
	 * 
	 * @param stf
	 *            The STF whose database connections should be read to the
	 *            diagram.
	 * @param model
	 *            The diagram model which is to fill with the database
	 *            connections.
	 * 
	 * @changed OLI 15.01.2015 - Added.
	 */
	public void read(StructuredTextFile stf, DiagrammModel model) {
		int len = (int) stf.readLong(this.createPath(COUNT), 0);
		for (int i = 0; i < len; i++) {
			String name = fromHTML(stf.readStr(this.createPath(i, NAME), null));
			DBExecMode dbExecMode = null;
			try {
				dbExecMode = DBExecMode.valueOf(fromHTML(stf.readStr(this.createPath(i, DB_EXEC_MODE), "POSTGRESQL")));
			} catch (Exception e) {
				log.warn("DBExecMode not valid: [" + stf.readStr(this.createPath(i, DB_EXEC_MODE), "POSTGRESQL") + "]");
				throw new RuntimeException("DBExecMode not valid: ["
						+ stf.readStr(this.createPath(i, DB_EXEC_MODE), "POSTGRESQL") + "]", e);
			}
			String driver = fromHTML(stf.readStr(this.createPath(i, DRIVER), ""));
			String quote = fromHTML(stf.readStr(this.createPath(i, QUOTE), ""));
			boolean setDomains = new Boolean(stf.readStr(this.createPath(i, SET_DOMAINS), "FALSE").toString())
					.booleanValue();
			boolean setNotNull = new Boolean(stf.readStr(this.createPath(i, SET_NOT_NULL), "FALSE").toString())
					.booleanValue();
			boolean setReferences = new Boolean(stf.readStr(this.createPath(i, SET_REFERENCES), "FALSE").toString())
					.booleanValue();
			String url = fromHTML(stf.readStr(this.createPath(i, URL), ""));
			String userName = fromHTML(stf.readStr(this.createPath(i, USER_NAME), ""));
			model.addDatabaseConnection(new DatabaseConnection(name, driver, url, userName, dbExecMode, setDomains,
					setNotNull, setReferences, quote));
		}
	}

}
