/*
 * DatabaseConnection.java
 *
 * 15.01.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.connections;


import static corentx.util.Checks.*;

import corent.db.*;


/**
 * A container for database connection data.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 15.01.2015 - Added.
 */

public class DatabaseConnection implements Comparable {

    private DBExecMode dbMode = null;
    private String driver = null;
    private String name = null;
    private String quote = "";
    private boolean setDomains = false;
    private boolean setNotNull = false;
    private boolean setReferences = false;
    private String url = null;
    private String userName = null;

    /**
     * Creates a new instance of the class with the passed parameters.
     *
     * @param name The name of the database connection record.
     * @param driver The name of the driver class.
     * @param url The URL which is used to connect with the database.
     * @param userName The name of the user which is used to connect with the dbms.
     * @param dbMode The db exec mode which should be used for the script generator.
     * @param setDomains Flag if the domains should be used on database level.
     * @param setNotNull Flag which is checked for set not null option in the scripts.
     * @param setReferences Flag if the foreign key constraints should be set in the scripts.
     * @param quote A quote character for database object names in the scripts.
     * @throws IllegalArgumentException In case of precondition violation.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public DatabaseConnection(String name, String driver, String url, String userName,
            DBExecMode dbMode, boolean setDomains, boolean setNotNull, boolean setReferences,
            String quote) throws IllegalArgumentException {
        super();
        this.setDBMode(dbMode);
        this.setDriver(driver);
        this.setName(name);
        this.setQuote(quote);
        this.setSetDomains(setDomains);
        this.setSetNotNull(setNotNull);
        this.setSetReferences(setReferences);
        this.setUrl(url);
        this.setUserName(userName);
    }

    /**
     * Creates a new database connection from the passed one.
     *
     * @param dc The database connection which should be copied.
     *
     * @changed OLI 17.02.2015 - Added.
     */
    public DatabaseConnection(DatabaseConnection dc) {
        this(dc.getName(), dc.getDriver(), dc.getUrl(), dc.getUserName(), dc.getDBMode(),
                dc.isSetDomains(), dc.isSetNotNull(), dc.isSetReferences(), dc.getQuote());
    }

    /**
     * @changed OLI 15.01.2015 - Added.
     */
    @Override public int compareTo(Object o) {
        DatabaseConnection dc = (DatabaseConnection) o;
        return this.getName().compareTo(dc.getName());
    }

    /**
     * Returns the db exec mode which should be used for the script generator.
     *
     * @return The db exec mode which should be used for the script generator.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public DBExecMode getDBMode() {
        return this.dbMode;
    }

    /**
     * Returns the name of the driver class.
     *
     * @return The name of the driver class.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public String getDriver() {
        return this.driver;
    }

    /**
     * Returns the name of the database connection record.
     *
     * @return The name of the database connection record.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Returns a quote character for database object names in the scripts.
     *
     * @return A quote character for database object names in the scripts.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public String getQuote() {
        return this.quote;
    }

    /**
     * Returns the flag if the domains should be used on database level.
     *
     * @return The flag if the domains should be used on database level.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public boolean isSetDomains() {
        return this.setDomains;
    }

    /**
     * Returns the flag which is checked for set not null option in the scripts.
     *
     * @return The flag which is checked for set not null option in the scripts.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public boolean isSetNotNull() {
        return this.setNotNull;
    }

    /**
     * Returns the flag if the foreign key constraints should be set in the scripts.
     *
     * @return The flag if the foreign key constraints should be set in the scripts.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public boolean isSetReferences() {
        return this.setReferences;
    }

    /**
     * Returns the URL which is used to connect with the database.
     *
     * @return The URL which is used to connect with the database.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * Returns the name of the user which is used to connect with the DBMS.
     *
     * @return The name of the user which is used to connect with the DBMS.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Sets the db exec mode which should be used for the script generator.
     *
     * @param dbMode The db exec mode which should be used for the script generator.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setDBMode(DBExecMode dbMode) throws IllegalArgumentException {
        this.dbMode = dbMode;
    }

    /**
     * Sets the name of the driver class.
     *
     * @param driver The name of the driver class.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setDriver(String driver) throws IllegalArgumentException {
        this.driver = driver;
    }

    /**
     * Sets the name of the database connection record.
     *
     * @param name The name of the database connection record.
     * @throws IllegalArgumentException If the name is passed as null or empty string.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setName(String name) throws IllegalArgumentException {
        ensure(name != null, "name cannot be null.");
        ensure(!name.isEmpty(), "name cannot be empty.");
        this.name = name;
    }

    /**
     * Sets a quote character for database object names in the scripts.
     *
     * @param quote A quote character for database object names in the scripts.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setQuote(String quote) throws IllegalArgumentException {
        this.quote = quote;
    }

    /**
     * Sets the flag if the domains should be used on database level.
     *
     * @param setDomains The flag if the domains should be used on database level.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setSetDomains(boolean setDomains) throws IllegalArgumentException {
        this.setDomains = setDomains;
    }

    /**
     * Sets the flag which is checked for set not null option in the scripts.
     *
     * @param setNotNull The flag which is checked for set not null option in the scripts.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setSetNotNull(boolean setNotNull) throws IllegalArgumentException {
        this.setNotNull = setNotNull;
    }

    /**
     * Sets the flag if the foreign key constraints should be set in the scripts.
     *
     * @param setReferences The flag if the foreign key constraints should be set in the
     *         scripts.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setSetReferences(boolean setReferences) throws IllegalArgumentException {
        this.setReferences = setReferences;
    }

    /**
     * Sets the URL which is used to connect with the database.
     *
     * @param url The URL which is used to connect with the database.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setUrl(String url) throws IllegalArgumentException {
        this.url = url;
    }

    /**
     * Sets the name of the user which is used to connect with the DBMS.
     *
     * @param userName The name of the user which is used to connect with the DBMS.
     *
     * @changed OLI 15.01.2015 - Added.
     */
    public void setUserName(String userName) throws IllegalArgumentException {
        this.userName = userName;
    }

    /**
     * @changed OLI 15.01.2015 - Added.
     */
    @Override public String toString() {
        return "DBMode=" + this.getDBMode()
                + "Driver=" + this.getDriver()
                + "Name=" + this.getName()
                + "Quote=" + this.getQuote()
                + "SetDomains=" + this.isSetDomains()
                + "SetNotNull=" + this.isSetNotNull()
                + "SetReferences=" + this.isSetReferences()
                + "Url=" + this.getUrl()
                + "UserName=" + this.getUserName();
    }

}