/*
 * TimeInputDialog.java
 *
 * 17.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.dates.*;


/**
 * Dieses Interface definiert das Verhalten eines TimeInputDialoges zur Nutzung im 
 * TimestampField.
 *
 * @author O.Lieshoff
 *
 */

public interface TimeInputDialog {
    
    /**
     * Liefert eine Information dar&uuml;ber, ob die Eingabe aus dem Dialog &uuml;bernommen 
     * werden sollen.
     *
     * @return <TT>true</TT>, wenn der Dialoginhalt &uuml;bernommen werden soll, <TT>false</TT>
     *     sonst.
     */
    public boolean isConfirmed();
    
    /**
     * Das PTime-Objekt, das in dem Dialog angezeigt wird.
     *
     * @return Die Zeit, die in dem Dialog aktuell angezeigt wird.
     */
    public PTime getPTime();
    
    /**
     * Setzt die Zeitangabe des Dialoges auf den angegeben Wert. 
     *
     * @param pt Die Zeit, die das TimePanel repraesentieren soll.
     */
    public void setPTime(PTime pt);

    
}
