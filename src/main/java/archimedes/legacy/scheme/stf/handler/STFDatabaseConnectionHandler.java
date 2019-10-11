/*
 * STFDatabaseConnectionHandler.java
 *
 * 15.01.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;


import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of database connections.
 *
 * @author ollie
 *
 * @changed OLI 15.01.2015 - Added.
 */

public class STFDatabaseConnectionHandler extends AbstractSTFHandler {

    public static final String COUNT = "Count";
    public static final String DATABASE_CONNECTION = "DatabaseConnection";
    public static final String DATABASE_CONNECTIONS = "DatabaseConnections";
    public static final String DB_EXEC_MODE= "DBExecMode";
    public static final String DRIVER = "Driver";
    public static final String NAME = "Name";
    public static final String QUOTE = "Quote";
    public static final String SET_DOMAINS = "SetDomains";
    public static final String SET_NOT_NULL = "SetNotNull";
    public static final String SET_REFERENCES = "SetReferences";
    public static final String URL = "URL";
    public static final String USER_NAME = "UserName";

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return DATABASE_CONNECTION;
    }

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return DATABASE_CONNECTIONS;
    }

}