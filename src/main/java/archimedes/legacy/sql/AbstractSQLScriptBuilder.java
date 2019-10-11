/*
 * AbstractSQLScriptBuilder.java
 *
 * 25.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static corentx.util.Checks.ensure;
import archimedes.model.DataModel;
import corent.db.DBExecMode;

/**
 * An abstract class with some basic field which are used in the SQL script
 * builders.
 * 
 * @author ollie
 * 
 * @changed OLI 25.04.2013 - Added.
 */

abstract public class AbstractSQLScriptBuilder {

	private DBExecMode mode = null;
	private DataModel model = null;
	private String quote = null;

	/**
	 * Creates a new sequence script builder with the passed parameters.
	 * 
	 * @param mode
	 *            The db mode for which the script is build.
	 * @param model
	 *            The diagram model whose data are the base of the script.
	 * @param sequences
	 *            The meta data of the sequences of the data scheme which is to
	 *            update.
	 * @param quote
	 *            The character sequence to quote names.
	 * @throws IllegalArgumentException
	 *             In case of passing a null pointer.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public AbstractSQLScriptBuilder(DBExecMode mode, DataModel model, String quote) throws IllegalArgumentException {
		super();
		ensure(mode != null, "db exec mode cannot be null.");
		ensure(model != null, "data mode cannot be null.");
		ensure(quote != null, "quote character sequence cannot be null.");
		this.mode = mode;
		this.model = model;
		this.quote = quote;
	}

	/**
	 * Returns the DB mode which is to use to build the SQL script.
	 * 
	 * @return The DB mode which is to use to build the SQL script.
	 * 
	 * @changed OLI 25.04.2013 - Added.
	 */
	public DBExecMode getDBMode() {
		return this.mode;
	}

	/**
	 * Returns the model which is used as base to generate the update script.
	 * 
	 * @return The model which is used as base to generate the update script.
	 * 
	 * @changed OLI 25.04.2013 - Added.
	 */
	public DataModel getModel() {
		return this.model;
	}

	/**
	 * Returns the character sequence which is to use to quote object
	 * identifiers in the script.
	 * 
	 * @return The character sequence which is to use to quote object
	 *         identifiers in the script.
	 * 
	 * @changed OLI 25.04.2013 - Added.
	 */
	public String getQuotes() {
		return this.quote;
	}

	/**
	 * Sets the passed string into quote sequences.
	 * 
	 * @param s
	 *            The string to quote.
	 * @return The quoted string.
	 * 
	 * @changed OLI 25.04.2013 - Added.
	 */
	public String quote(String s) {
		return this.getQuotes().concat(s).concat(this.getQuotes());
	}

}