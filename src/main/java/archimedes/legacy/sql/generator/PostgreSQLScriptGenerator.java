/*
 * PostgreSQLScriptGenerator.java
 *
 * 14.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.sql.generator;

import archimedes.legacy.util.NameGenerator;
import corent.db.DBExecMode;

/**
 * A SQL script generator for PostgreSQL.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.12.2015 - Added.
 */

public class PostgreSQLScriptGenerator extends StandardSQLScriptGenerator {

	/**
	 * Creates a new SQL script generator for standard SQL with the passed parameters.
	 *
	 * @param dbMode                The mode of the DBMS which the SQL script is to create for.
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
	public PostgreSQLScriptGenerator(DBExecMode dbMode, String quotes, boolean useDomains,
			boolean foreignKeyConstraints, boolean notNullConstraints, NameGenerator nameGenerator,
			ScriptHeaderBuilder scriptHeaderBuilder) throws IllegalArgumentException {
		super(dbMode, quotes, useDomains, foreignKeyConstraints, notNullConstraints, nameGenerator,
				scriptHeaderBuilder);
	}

}