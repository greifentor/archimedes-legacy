/*
 * BaseConfiguration.java
 *
 * 24.11.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.configuration;

import corentx.util.*;


/**
 * A container for the base configuration of Archimedes.
 *
 * @author ollie
 *
 * @changed OLI 24.11.2014 - Added.
 */

public class BaseConfiguration {

    private String company = "";
    private String dbName = "";
    private String dbServerName = "";
    private String dbUserName = "";
    private String language = "";
    private String userName = "";
    private String userToken = "";

    /**
     * A parameterless constructor.
     *
     * @changed OLI 25.11.2014 - Generated.
     */
    public BaseConfiguration() {
        super();
    }

    /**
     * Creates a new base configuration entity from another base configuration object.
     *
     * @param baseConfiguration The object which is the base for the data of the new entity.
     *
     * @changed OLI 25.11.2014 - Generated.
     */
    public BaseConfiguration(BaseConfiguration baseConfiguration) {
        this();
        this.setCompany(baseConfiguration.getCompany());
        this.setDBName(baseConfiguration.getDBName());
        this.setDBServerName(baseConfiguration.getDBServerName());
        this.setDBUserName(baseConfiguration.getDBUserName());
        this.setLanguage(baseConfiguration.getLanguage());
        this.setUserName(baseConfiguration.getUserName());
        this.setUserToken(baseConfiguration.getUserToken());
    }

    /**
     * @changed OLI 25.11.2014 - Added.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof BaseConfiguration)) {
            return false;
        }
        BaseConfiguration bc = (BaseConfiguration) o;
        return Utl.equals(bc.getCompany(), this.getCompany())
                && Utl.equals(bc.getDBName(), this.getDBName())
                && Utl.equals(bc.getDBServerName(), this.getDBServerName())
                && Utl.equals(bc.getDBUserName(), this.getDBUserName())
                && Utl.equals(bc.getLanguage(), this.getLanguage())
                && Utl.equals(bc.getUserName(), this.getUserName())
                && Utl.equals(bc.getUserToken(), this.getUserToken());
    }

    /**
     * Returns the company of the object.
     *
     * @return The company of the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public String getCompany() {
        return this.company;
    }

    /**
     * Returns the database name setting for database connection wildcard replacement.
     *
     * @return The database name setting for database connection wildcard replacement.
     *
     * @changed OLI 17.02.2015 - Added.
     */
    public String getDBName() {
        return this.dbName;
    }

    /**
     * Returns the sever name setting for database connection wildcard replacement.
     *
     * @return The server name setting for database connection wildcard replacement.
     *
     * @changed OLI 17.02.2015 - Added.
     */
    public String getDBServerName() {
        return this.dbServerName;
    }

    /**
     * Returns the database user name setting for database connection wildcard replacement.
     *
     * @return The database user name setting for database connection wildcard replacement.
     *
     * @changed OLI 17.02.2015 - Added.
     */
    public String getDBUserName() {
        return this.dbUserName;
    }

    /**
     * Returns the language of the object.
     *
     * @return The language of the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public String getLanguage() {
        return this.language;
    }

    /**
     * Returns the name of the object.
     *
     * @return The name of the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public String getUserName() {
        return this.userName;
    }

    /**
     * Returns the token of the object.
     *
     * @return The token of the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public String getUserToken() {
        return this.userToken;
    }

    /**
     * @changed OLI 25.11.2014 - Added.
     */
    @Override public int hashCode() {
        return this.toString().hashCode();
    }

    /**
     * Sets a new value for the company of the object.
     *
     * @param company The new company for the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public void setCompany(String company) {
        this.company = company;
    }

    /**
     * Sets a new value for the database name of the object for database connection wildcard
     * replacement.
     *
     * @param dbName The new database name for the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public void setDBName(String dbName) {
        this.dbName = dbName;
    }

    /**
     * Sets a new value for the server name of the object for database connection wildcard
     * replacement.
     *
     * @param dbServerName The new server name for the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public void setDBServerName(String dbServerName) {
        this.dbServerName = dbServerName;
    }

    /**
     * Sets a new value for the database user name of the object for database connection
     * wildcard replacement.
     *
     * @param dbUserName The new database user name for the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public void setDBUserName(String dbUserName) {
        this.dbUserName = dbUserName;
    }

    /**
     * Sets a new value for the language of the object.
     *
     * @param language The new language for the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * Sets a new value for the user name of the object.
     *
     * @param userName The new user name for the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * Sets a new value for the user token of the object.
     *
     * @param token The new user token for the object.
     *
     * @changed OLI 25.11.2014 - Added.
     */
    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    /**
     * @changed OLI 25.11.2014 - Added.
     */
    @Override public String toString() {
        return "Company=" + this.getCompany()
                + ", DBName=" + this.getDBName()
                + ", DBServerName=" + this.getDBServerName()
                + ", DBUserName=" + this.getDBUserName()
                + ", Language=" + this.getLanguage()
                + ", UserName=" + this.getUserName()
                + ", UserToken=" + this.getUserToken();
    }

}