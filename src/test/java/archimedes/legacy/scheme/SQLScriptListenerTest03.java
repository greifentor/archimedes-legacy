/*
 * SQLScriptListenerTest03.java
 *
 * 22.03.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;


import archimedes.legacy.script.sql.*;


/**
 * Testklasse f&uuml;r die Unittests.
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 22.03.2009 - Hinzugef&uuml;gt
 *     <P>
 *
 */

public class SQLScriptListenerTest03 implements SQLScriptListener {

    private int cnt = 0;

    /** Generiert ein SQLScriptListenerTest03-Objekt mit Defaultwerten. */
    public SQLScriptListenerTest03() {
        super();
    }

    /**
     * Liefert den aktuellen Stand der Aufrufe.
     *
     * @return Der aktuellen Stand der Aufrufe.
     */
    public int getCallCount() {
        return this.cnt;
    }

    /* Implementierung des Interfaces SQLScriptListener. */

    public boolean dataSchemeChanged(SQLScriptEvent e) {
        this.cnt++;
        return true;
    }

}
