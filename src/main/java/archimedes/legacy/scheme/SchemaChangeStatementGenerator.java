/*
 * SchemaChangeStatementGenerator.java
 *
 * 24.02.2012
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;


import corent.db.*;


/**
 * Dieses Interface kann von Klassen implementiert werden, die Statements zum Wechseln zu einem
 * engegebenen Schema in der Datenbank erzeugen.
 *
 * @author ollie
 *
 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
 */

public interface SchemaChangeStatementGenerator {

    /**
     * Liefert ein Statement zum Wechsel in das angegebene Schema der Datenbank f&uuml;r das
     * genannte DBMS.
     *
     * @param schemaName Der Name des Schema, in das gewechselt werden soll oder eine leerer
     *         oder null-String falls in kein Schema gewechselt werden soll.
     * @param dbms Ein Bezeichner, der das DBMS identifiziert, f&uuml;r das das Statement
     *         generiert werden soll.
     * @return Ein Statement, falls ein Schemaname angegeben worden ist, sonst ein null-Pointer.
     * @throws IllegalArgumentException Falls das DBMS als null-Pointer &uuml;bergeben wird.
     * @throws UnsupportedOperationException Falls das DBMS nicht f&uuml;r diese Funktion
     *         unterst&uuml;tzt wird.
     *
     * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
     */
    abstract public String create(String schemaName, DBExecMode dbms)
            throws IllegalArgumentException, UnsupportedOperationException;

}