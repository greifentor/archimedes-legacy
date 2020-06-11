/*
 * DBExecMode.java
 *
 * 01.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db;


/**
 * Mit Hilfe dieser Implementierung eines typsicheren Enum werden die Betriebsmodi f&uuml;r 
 * DBExec repr&auml;sentiert.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.09.2007 - Die Methode <TT>valueOf(String)</TT> ist statisch gemacht worden.
 * @changed OLI 04.09.2007 - Erweiterung um die Methode <TT>getDBExecMode(String,
 *         DBExecMode)</TT>. Mit ihrer Hilfe k&ouml;nnen DBExecModes bequem aus den Properties
 *         gelesen werden.<BR>
 * @changed OLI 07.06.2011 - Erweiterung um das Teststatement zur Pr&uuml;fung der Funktion des
 *         DBMS. Umwandlung in eine echte Enum-Klasse.
 */
 
public enum DBExecMode {

    /** Ein Modusbezeichner f&uuml;r die javabasierte HSQL (HypersonicSQL) Datenbank. */
    HSQL("HSQL-Modus", "HSQL", "call NOW()"),
    /** Ein Modusbezeichner f&uuml;r den Microsoft SQL Server. */
    MSSQL("MS-SQL-Modus", "MSSQL", null),
    /** Ein Modusbezeichner f&uuml;r mySQL. */
    MYSQL("mySQL-Modus", "MYSQL", null),
    /** Ein Modusbezeichner f&uuml;r die PostgreSQL-Datenbank. */
    POSTGRESQL("PostgreSQL-Modus", "POSTGRESQL", "select CURRENT_DATE"),
    /** Ein Modusbezeichner f&uuml;r die StandardSQL. */
    STANDARDSQL("Standard-SQL-Modus", "STANDARDSQL", null);

    private String name = null;
    private String testStatement = null;
    private String token = null;

    private DBExecMode(String name, String token, String testStatement) {
        this.name = name;
        this.testStatement = testStatement;
        this.token = token;
    }

    /**
     * Liest einen DBExecMode aus der Property mit dem angegebenen Namen. Die Property mu&szlig;
     * das Token (den Namen des DBExecMode-Bezeichners) enthalten.
     *
     * @param pn Name der Property, aus der der DBExecMode gelesen werden soll.
     * @param dflt Ein DBExecMode, der als Default zur&uuml;ckgeliefert wird, falls die Property
     *     keine g&uuml;ltigen Daten f&uuml;r einen DBExecMode enth&auml;lt.
     * @return Der DBExecMode, der sich aus den in der Property angegebenen Daten ergibt, bzw.
     *     der angegebene Defaultwert, falls die Property keine g&uuml;ltigen Daten f&uuml;r 
     *     einen DBExecMode enth&auml;lt.
     *
     * @changed
     *     OLI 04.09.2007 - Hinzugef&uuml;gt.
     */
    public static DBExecMode getDBExecMode(String pn, DBExecMode dflt) {
        DBExecMode[] modes = DBExecMode.values();
        String s = System.getProperty(pn, "");
        for (int i = 0, len = modes.length; i < len; i++) {
            if (modes[i].token.equalsIgnoreCase(s)) {
                return modes[i];
            }
        }
        return dflt;
    }

    /**
     * Liefert das Test-Statement zur Pr&uuml;fung der Erreichbarkeit der Datenbank.
     *
     * @return Das Test-Statement zur Pr&uuml;fung der Erreichbarkeit der Datenbank.
     *
     * @changed OLI 07.06.2011 - Hinzugef&uuml;gt.
     */
    public String getTestStatement() {
        return this.testStatement;
    }

    /**
     * Liefert ein K&uuml;rzel zum Modus. Dieses K&uuml;rzel kann auch mit Hilfe der 
     * valueOf-Methode wieder in einen Modus verwandelt werden.
     *
     * @return Das K&uuml;rzel zum DBExecMode.
     */
    public String toToken() {
        return this.token;
    }

    /**
     * @changed OLI 07.06.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public String toString() {
        return this.name;
    }

}