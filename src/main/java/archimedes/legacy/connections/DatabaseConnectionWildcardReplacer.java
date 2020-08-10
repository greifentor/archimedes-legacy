/*
 * DatabaseConnectionWildcardReplacer.java
 *
 * 17.02.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.connections;

/**
 * Replaces some wildcards in a database connection by system properties values.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.02.2015 - Added.
 */

public class DatabaseConnectionWildcardReplacer {

	private DatabaseConnection dc = null;

	/**
	 * Creates a new database connection wildcard replacer with the passed parameters.
	 *
	 * @param dc The database connection whose values are to replace.
	 *
	 * @changed OLI 17.02.2015 - Added.
	 */
	public DatabaseConnectionWildcardReplacer(DatabaseConnection dc) {
		super();
		this.dc = dc;
	}

	/**
	 * Replaces the wildcards.
	 *
	 * @changed OLI 17.02.2015 - Added.
	 */
	public DatabaseConnection replace() {
		DatabaseConnection dc = new DatabaseConnection(this.dc);
		dc.setUrl(this.replaceWildcard(dc.getUrl(), "%DB_NAME%", "archimedes.user.database.name"));
		dc.setUrl(this.replaceWildcard(dc.getUrl(), "%DB_SERVER_NAME%", "archimedes.user.database.server.name"));
		dc.setUserName(this.replaceWildcard(dc.getUserName(), "%DB_USER_NAME%", "archimedes.user.database.user.name"));
		return dc;
	}

	private String replaceWildcard(String s, String wildcard, String property) {
		return s.replace(wildcard, System.getProperty(property, wildcard));
	}

}