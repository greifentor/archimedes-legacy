/*
 * SQLScriptGeneratorFactory.java
 *
 * 14.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.sql.generator;

import static corentx.util.Checks.ensure;

import archimedes.legacy.util.NameGenerator;
import corent.db.DBExecMode;

/**
 * A factory which creates SQL script generators for different DBMS.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.12.2015 - Added.
 */

public class SQLScriptGeneratorFactory {

	private boolean foreignKeyConstraints = false;
	private NameGenerator nameGenerator = null;
	private boolean notNullConstraints = false;
	private String quotes = null;
	private ScriptHeaderBuilder scriptHeaderBuilder = null;
	private boolean useDomains = false;

	/**
	 * Creates a new SQL script generator for standard SQL with the passed parameters.
	 *
	 * @param quotes                The quotes for object identifiers (like table names).
	 * @param useDomains            Set this flag if the database should work with domains.
	 * @param foreignKeyConstraints Set this flag if the foreign key constraints should be set.
	 * @param notNullConstraints    Set this flag if the not null constraints should be set.
	 * @param nameGenerator         A generator for data base object names.
	 * @param scriptHeaderBuilder   A builder for the script header.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 14.12.2015 - Added.
	 */
	public SQLScriptGeneratorFactory(String quotes, boolean useDomains, boolean foreignKeyConstraints,
			boolean notNullConstraints, NameGenerator nameGenerator, ScriptHeaderBuilder scriptHeaderBuilder)
			throws IllegalArgumentException {
		super();
		ensure(quotes != null, "quotes cannot be null");
		this.foreignKeyConstraints = foreignKeyConstraints;
		this.nameGenerator = nameGenerator;
		this.notNullConstraints = notNullConstraints;
		this.quotes = quotes;
		this.scriptHeaderBuilder = scriptHeaderBuilder;
		this.useDomains = useDomains;
	}

	/**
	 * Returns the SQL script generator for the passed DBMS.
	 *
	 * @param dbMode The mode for the required DBMS.
	 * @return The SQL script generator for the passed DBMS.
	 * @throws DBMSNotSupportedException If the passed DBMS is not supported by the factory.
	 * @throws IllegalArgumentException  Passing a null pointer for DB mode.
	 *
	 * @changed OLI 14.12.2015 - Added.
	 */
	public SQLScriptGenerator getSQLScriptGenerator(DBExecMode dbMode)
			throws DBMSNotSupportedException, IllegalArgumentException {
		ensure(dbMode != null, "DB mode cannot be null.");
		if (dbMode == DBExecMode.POSTGRESQL) {
			return new PostgreSQLScriptGenerator(dbMode, this.quotes, this.useDomains, this.foreignKeyConstraints,
					this.notNullConstraints, this.nameGenerator, this.scriptHeaderBuilder);
		} else if (dbMode == DBExecMode.MYSQL) {
			return new StandardSQLScriptGenerator(dbMode, this.quotes, this.useDomains, this.foreignKeyConstraints,
					this.notNullConstraints, this.nameGenerator, this.scriptHeaderBuilder);
		} else if (dbMode == DBExecMode.HSQL) {
			return new StandardSQLScriptGenerator(dbMode, this.quotes, this.useDomains, this.foreignKeyConstraints,
					this.notNullConstraints, this.nameGenerator, this.scriptHeaderBuilder);
		}
		throw new DBMSNotSupportedException("DB mode is not supported: " + dbMode);
	}

}