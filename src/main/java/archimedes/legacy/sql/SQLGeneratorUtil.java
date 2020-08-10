/*
 * SQLGeneratorUtil.java
 *
 * 26.07.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static corentx.util.Checks.ensure;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DomainModel;
import corent.db.DBExecMode;

/**
 * Some utility methods for SQL script generation.
 * 
 * @author ollie
 * 
 * @changed OLI 26.07.2013 - Added.
 */

public class SQLGeneratorUtil {

	/**
	 * Returns a string with a default value for the passed column and DB mode.
	 * 
	 * @param column
	 *            The column which the default value is to create for.
	 * @param dbmode
	 *            The DB mode which the type is returned for.
	 * @return A default value string for using in the generated SQL statement.
	 * @throws IllegalArgumentException
	 *             Passing a null pointer.
	 * 
	 * @changed OLI 26.07.2013 - Added.
	 */
	public static String getDefaultValue(ColumnModel column, DBExecMode dbmode) throws IllegalArgumentException {
		ensure(column != null, "column cannot be null.");
		ensure(dbmode != null, "DB mode cannot be null.");
		String defaultValue = column.getDefaultValue();
		DomainModel domain = column.getDomain();
		defaultValue = (defaultValue != null ? defaultValue.replace("\"", "'") : "null");
		if ((dbmode == DBExecMode.POSTGRESQL) && domain.getName().toLowerCase().equals("boolean")) {
			if ("1".equals(defaultValue) || "true".equalsIgnoreCase(defaultValue)) {
				defaultValue = "true";
			} else {
				defaultValue = "false";
			}
		}
		return defaultValue;
	}

	/**
	 * Returns the type string for the passed domain and DB mode.
	 * 
	 * @param hasDomains
	 *            Create a type with domains.
	 * @param domain
	 *            The domain model whose SQL type string has to be returned.
	 * @param dbmode
	 *            The DB mode which the type is returned for.
	 * @return The type string for using in the generated SQL statement.
	 * 
	 * @changed OLI 26.07.2013 - Added.
	 */
	public static String getTypeString(boolean hasDomains, DomainModel domain, DBExecMode dbmode) {
		ensure(dbmode != null, "DB mode cannot be null.");
		ensure(domain != null, "domain cannot be null.");
		String typeString = (hasDomains ? domain.getName() : domain.getType(dbmode));
		if (!hasDomains && (dbmode == DBExecMode.POSTGRESQL) && (domain.getName().toLowerCase().equals("boolean"))) {
			typeString = "Boolean";
		}
		return (hasDomains ? typeString : typeString.toUpperCase());
	}

	/**
	 * Checks if the passed column is of an string type.
	 * 
	 * @param column
	 *            The column which is to check.
	 * @param dbmode
	 *            The mode which is valid for the database.
	 * @return <CODE>true</CODE> if the column is of a string type.
	 * 
	 * @changed OLI 01.08.2013 - Added.
	 */
	public static boolean isStringType(ColumnModel column, DBExecMode dbmode) {
		return column.getDomain().getType(dbmode).toLowerCase().contains("char")
				|| column.getDomain().getType(dbmode).toLowerCase().contains("text");
	}

}