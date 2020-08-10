/*
 * ScriptHeaderBuilder.java
 *
 * 16.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.sql.generator;

import static corentx.util.Checks.*;

import archimedes.legacy.model.UserInformation;
import archimedes.legacy.model.*;

import corent.db.*;

import corentx.dates.*;


/**
 * Build a script header for a SQL script.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.12.2015 - Added.
 */

public class ScriptHeaderBuilder {

    private DBExecMode dbMode = null;
    private String dbName = null;
    private String schemeName = null;
    private PTimestamp timestamp = null;
    private UserInformation userInfos = null;
    private String version = null;

    /**
     * Creates a new script header builder with the passed parameters.
     * 
     * @param dbMode The mode of the DBMS which the script is created for.
     * @param dbName The name of the database or <CODE>null</CODE> if there is no name for the
     *         database.
     * @param schemeName The name of the scheme or <CODE>null</CODE> if no scheme is used.
     * @param timestamp A timestamp with the date of generation.
     * @param version The version which is created by the script. 
     * @param userInfos Some user related information.
     * @return A string with the SQL script header.
     *
     * @changed OLI 16.12.2015 - Added.
     */
    public ScriptHeaderBuilder(DBExecMode dbMode, String dbName, String schemeName,
            PTimestamp timestamp, String version, UserInformation userInfos) {
        super();
        ensure(dbMode != null, "DB mode cannot be null.");
        ensure(timestamp != null, "timestamp cannot be null.");
        ensure(userInfos != null, "user infos cannot be null.");
        ensure(version != null, "version cannot be null.");
        this.dbMode = dbMode;
        this.dbName = dbName;
        this.schemeName = schemeName;
        this.timestamp = timestamp;
        this.userInfos = userInfos;
        this.version = version;
    }

    /**
     * Returns a string with the SQL script header.
     *
     * @return A string with the SQL script header.
     *
     * @changed OLI 16.12.2015 - Added.
     */
    protected String build() {
        StringBuffer sql = new StringBuffer(
                "/********************************************************************\n");
        sql.append(" *\n");
        sql.append(" * Archimedes update script\n");
        sql.append(" *\n");
        if (this.schemeName != null) {
            sql.append(" * Database:    " + this.dbName + "\n");
        }
        if (this.schemeName != null) {
            sql.append(" * Schema:      " + this.schemeName + "\n");
        }
        sql.append(" * Version:     " + this.version + "\n");
        sql.append(" * Created at:  " + this.timestamp + "\n");
        sql.append(" * Created by:  " + this.userInfos.getUserName() + " ("
                + this.userInfos.getUserToken() + ")\n");
        sql.append(" * Created for: " + this.userInfos.getVendorName() + "\n");
        sql.append(" *\n");
        sql.append(" * DBMS:        " + this.dbMode + "\n");
        sql.append(" *\n");
        sql.append(" *\n");
        sql.append(" * THIS SCRIPT IS GENERATED!!!\n");
        sql.append(" *\n");
        sql.append(" * Do not change manually!\n");
        sql.append(" *\n");
        sql.append(" *\n");
        sql.append(" ********************************************************************/\n");
        sql.append("\n");
        sql.append("\n");
        if ((this.dbMode == DBExecMode.POSTGRESQL) && (this.schemeName != null)) {
            sql.append("\n");
            sql.append("\n");
            sql.append("SET search_path TO \"" + this.schemeName + "\";\n");
            sql.append("\n");
            sql.append("COMMENT ON SCHEMA \"" + this.schemeName + "\" IS 'Version "
                    + this.version + "';\n");
        }
        return sql.toString();
    }

}