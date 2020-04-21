/*
 * DBExecListener.java
 *
 * 27.04.2011
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db;


/**
 * Ein Interface zur Definition der notwendigen Methoden f&uuml;r eine Klasse, die eine
 * <TT>DBExec</TT>-Instanz belauschen k&ouml;nnen soll.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
 */

public interface DBExecListener {

    /**
     * Diese Methode wird aufgerufen, wenn in der <TT>DBExec</TT>-Instanz, an die der Listener
     * angebunden ist, ein Ereignis aufgetreten ist.
     *
     * @param dbe Ein <TT>DBExecEvent</TT>-Objekt mit den Daten zum Ereignis.
     *
     * @changed OLI 27.04.2011 - Hinzugef&uuml;gt.
     */
    public void eventDetected(DBExecEvent dbe);

}