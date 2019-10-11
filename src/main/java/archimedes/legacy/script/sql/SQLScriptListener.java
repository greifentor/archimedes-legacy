/*
 * SQLScriptListener.java
 *
 * 22.03.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.script.sql;


/**
 * Dieses Interface muss von Klassen implementiert werden, die sich an die 
 * SQL-Script-Generierung eines DiagrammModels ankoppeln lassen sollen.
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

public interface SQLScriptListener {

    /**
     * Diese Methode wird aufgerufen, wenn die SQL-Script-Generierung durch ein DiagrammModel
     * eine &Auml;derung am Datenschema erkannt wird. Die genaue Art der &Auml;nderung (z. B.
     * L&ouml;schen oder Anlegen einer Tabelle) wird durch den Typ des SQLScriptEvents 
     * festgelegt, das an die Methode &uuml;bergeben wird.
     *
     * @param e Das SQLScriptEvent mit den Details der &Auml;nderung am Datenmodell.
     * @return <TT>true</TT>, wenn die &Auml;nderung erfolgreich durch den Listener bearbeitet
     *         werden kann, <TT>false</TT> sonst.
     */
    public boolean dataSchemeChanged(SQLScriptEvent e);

}
