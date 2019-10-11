/*
 * SQLScriptListenerTest.java
 *
 * 22.03.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;


import archimedes.legacy.script.sql.*;

import corent.util.*;


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

public class SQLScriptListenerTest implements SQLScriptListener {

    private String name = null;

    /** 
     * Generiert ein SQLScriptListenerTest01-Objekt anhand der &uuml;bergebenen Parameter.
     *
     * @param name Eine Name f&uuml;r die Instanz.
     */
    public SQLScriptListenerTest(String name) {
        super();
        this.name = name;
    }


    /* Implementierung des Interfaces SQLScriptListener. */

    public boolean dataSchemeChanged(SQLScriptEvent e) {
        if (e.getEventType() == SQLScriptEventType.COMPLETEBUILDING) {
            try {
                FileUtil.WriteTextToFile(this.name + ".tmp", false, this.name);
            } catch (Exception exception) {
                return false;
            }
            e.getScriptVectorReference().addExtendingStatement("");
            e.getScriptVectorReference().addExtendingStatement("/* Build complete. */");
        }
        return true;
    }

}
