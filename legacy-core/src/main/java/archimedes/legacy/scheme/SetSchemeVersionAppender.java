/*
 * SetSchemeVersionAppender.java
 *
 * 28.08.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.*;
import archimedes.legacy.script.sql.*;
import archimedes.legacy.sql.*;


/**
 * A class which is able to extend a SQL script by a statement which sets the model version to
 * the data scheme.
 *
 * @author ollie
 *
 * @changed OLI 28.08.2014 - Added.
 */

public class SetSchemeVersionAppender {

    private SQLScriptFactory factory = null;
    private String modelVersion = null;
    private String schemeName = null;

    /**
     * Creates a new appender with the passed parameters.
     *
     * @param modelVersion The version of the data model.
     * @param schemeName The name of the data scheme which the version is to set to.
     * @param factory The factory for SQL code generation.
     * @throws IllegalArgumentException In case of passing a null pointer as factory.
     *
     * @changed OLI 28.08.2014 - Added.
     */
    public SetSchemeVersionAppender(String modelVersion, String schemeName,
            SQLScriptFactory factory) throws IllegalArgumentException {
        ensure(factory != null, "SQL script factory cannot be null.");
        this.factory = factory;
        this.modelVersion = modelVersion;
        this.schemeName = schemeName;
    }

    /**
     * Extends the SQL script by a statement to set the model version in the database (if there
     * is a possibility therefore in the DDMS).
     *
     * @param script The script which is to extend by the statement. 
     * @throws IllegalArgumentException If the a null pointer is passed instead of a script.
     *
     * @changed OLI 28.08.2014 - Added.
     */
    public void append(SQLScript script) throws IllegalArgumentException {
        ensure(script != null, "script to append to cannot be null.");
        String stmt = null;
        if (!this.isNotEmptyOrNull(this.modelVersion)) {
            return;
        }
        if (!this.isNotEmptyOrNull(this.schemeName)) {
            return;
        }
        try {
            stmt = this.factory.setModelVersionStatement(this.modelVersion, this.schemeName);
        } catch (UnsupportedOperationException uoe) {
            stmt = "/* Model version set is not supported for " + this.factory.getDBMode(
                    ).toToken() + " by the generator! */";
        }
        if (this.isNotEmptyOrNull(stmt)) {
            script.addExtendingStatement(stmt);
            script.addExtendingStatement("");
        }
    }

    private boolean isNotEmptyOrNull(String s) {
        return (s != null) && !s.isEmpty();
    }

}