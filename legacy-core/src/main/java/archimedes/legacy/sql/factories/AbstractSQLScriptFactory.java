/*
 * AbstractSQLScriptFactory.java
 *
 * 30.07.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import archimedes.legacy.sql.SQLScriptFactory;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import corent.db.DBExecMode;

/**
 * An abstract base implementation of the <CODE>SQLScriptFactory</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 30.07.2013 - Added.
 */

abstract public class AbstractSQLScriptFactory implements SQLScriptFactory {

	private DBExecMode dbMode = null;
	private DataModel model = null;
	private String quotes = null;

	/**
	 * Creates a new factory with the passed parameters.
	 * 
	 * @param quotes
	 *            The quote character used to quote object names in the script.
	 * @param dataModel
	 *            The data model which is the base for the SQL script
	 *            generation.
	 * @param dbMode
	 *            The database mode the SQL script is to generate for.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	public AbstractSQLScriptFactory(String quotes, DataModel model, DBExecMode dbMode) {
		super();
		this.dbMode = dbMode;
		this.model = model;
		this.quotes = quotes;
	}

	/**
	 * Returns a script fragment which creates a primary key constraint for a
	 * create table statement.
	 * 
	 * @param table
	 *            The table which the statement is to create for.
	 * @return The primary key constraint.
	 * 
	 * @changed OLI 28.10.2013 - Added.
	 */
	protected String createPrimaryKeyConstraint(TableModel table) {
		ColumnModel[] pks = table.getPrimaryKeyColumns();
		String s = "";
		if (pks.length > 1) {
			s = ",\n    PRIMARY KEY(";
			String a = "";
			for (ColumnModel c : pks) {
				if (a.length() > 0) {
					a += ", ";
				}
				a += this.quote(c.getName());
			}
			s += a + ")";
		}
		return s;
	}

	/**
	 * Returns a name for a simple index on the passed column.
	 * 
	 * @param column
	 *            The column which the simple index is to create for.
	 * @return The name of a simple index for the column.
	 * 
	 * @changed OLI 28.10.2013 - Added.
	 */
	public String createSimpleIndexName(ColumnModel column) {
		return "I" + column.getTable().getName() + column.getName();
	}

	/**
	 * Returns the data model which is the base of the SQL script generation.
	 * 
	 * @return The data model which is the base of the SQL script generation.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	public DataModel getDataModel() {
		return this.model;
	}

	/**
	 * @changed OLI 30.07.2013 - Added.
	 */
	@Override
	public DBExecMode getDBMode() {
		return this.dbMode;
	}

	/**
	 * Returns the quote character.
	 * 
	 * @return The quote character.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	public String getQuotes() {
		return this.quotes;
	}

	/**
	 * Quotes the passed string with the quote characters.
	 * 
	 * @param s
	 *            The string to quote.
	 * @return The quoted string.
	 * 
	 * @changed OLI 30.07.2013 - Added.
	 */
	public String quote(String s) {
		return this.getQuotes().concat(s).concat(this.getQuotes());
	}

}