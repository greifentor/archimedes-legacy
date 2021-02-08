/*
 * SchemaChangeStatementAppender.java
 *
 * 24.02.2012
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;


import static corentx.util.Checks.*;

import archimedes.legacy.script.sql.*;
import archimedes.legacy.sql.*;


/**
 * Diese Klasse f&uuml;gt gegebenenfalls ein Statement zum Wechseln des Datenbank-Schemas an ein
 * bestehendes Script an.
 *
 * @author ollie
 *
 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
 */

public class SchemaChangeStatementAppender {

    private SQLScriptFactory factory = null;
    private String schemaName = null;

    /**
     * Erzeugt einen neuen Appender mit den angegebenen Parametern.
     *
     * @param schemaName Der Name des Schema auf das das Statement wechseln soll.
     * @param factory Die Factory, die den SQL code erzeugt.
     * @throws IllegalArgumentException Falls der DBMS-Bezeichner als <CODE>null</CODE>-Pointer
     *         angegeben wird.
     *
     * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
     */
    public SchemaChangeStatementAppender(String schemaName, SQLScriptFactory factory)
            throws IllegalArgumentException {
        ensure(factory != null, "SQL script factory cannot be null.");
        this.factory = factory;
        this.schemaName = schemaName;
    }

    /**
     * H&auml;ngt (gegebenenfalls) ein Statement zum Wechsel auf das angegebene Schema in der
     * Datenbank an das angegebene Script an. Gegebenenfalls heisst:
     * <OL>
     *     <LI>Es ist ein Schemaname angegeben worden.</LI>
     *     <LI>Das DBMS unterst&uuml;tzt Schemata.</LI>
     *     <LI>Der Wechsel wird f&uuml;r das angegenene DBMS vom Genertor unterst&uuml;tzt.</LI>
     * </OL>
     *
     * @param script Das Script, an das das Statement gegebenenfalls angeh&auml;ngt werden soll.
     * @throws IllegalArgumentException Falls das Script als <CODE>null</CODE>-Pointer
     *         &uuml;bergeben wird.
     *
     * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
     */
    public void append(SQLScript script) throws IllegalArgumentException {
        ensure(script != null, "script to append to cannot be null.");
        String stmt = null;
        if (!this.isNotEmptyOrNull(this.schemaName)) {
            return;
        }
        try {
            stmt = this.factory.setSchemaStatement(this.schemaName);
        } catch (UnsupportedOperationException uoe) {
            stmt = "/* Schema changing is not supported for " + this.factory.getDBMode(
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