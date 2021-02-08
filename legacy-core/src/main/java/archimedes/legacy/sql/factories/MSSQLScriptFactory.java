/*
 * MSSQLScriptFactory.java
 *
 * 28.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import archimedes.model.DataModel;
import corent.db.DBExecMode;

/**
 * A specific script factory for the MicroSoft SQL server.
 * 
 * @author ollie
 * 
 * @changed OLI 28.10.2013 - Added.
 */

public class MSSQLScriptFactory extends StandardSQLScriptFactory {

	/**
	 * Creates a new factory with the passed parameters.
	 * 
	 * @param quotes
	 *            The quote character used to quote object names in the script.
	 * @param dbMode
	 *            The database mode which the factory should produce code for.
	 * 
	 * @changed OLI 28.10.2013 - Added.
	 */
	public MSSQLScriptFactory(String quotes, DataModel model) {
		super(quotes, model, null, DBExecMode.MSSQL);
	}

}