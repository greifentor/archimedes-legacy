/*
 * GlobalListener.java
 *
 * 26.02.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.event;


import java.util.*;


/**
 * Mit Hilfe dieses Interfaces k&ouml;nnen Objekte in die Lage versetzt werden &uuml;ber den 
 * GlobalEventManager auf einfache Weise an der applikationsweiten AWTEvent-Verarbeitung 
 * teilzunehmen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface GlobalListener {
    
    /**
     * Diese Methode mu&szlig; den Wert <TT>true</TT> zur&uuml;ckliefern, wenn das Event durch
     * das implementierende Objekt verarbeitet werden soll.
     *
     * @param e Das Event, welchen aus Verarbeitbarkeit &uuml;berpr&uuml;ft werden soll.
     * @return <TT>true</TT>, falls das Objekt das &uuml;bergebene Objekt verarbeiten soll.
     */
    public boolean accept(EventObject e);

    /**
     * Diese Methode wird aufgerufen, wenn das Event durch das Objekt verarbeitet werden soll.
     * Die Verarbeitung findet in der Methode statt.
     *
     * @param e Das Event, das durch das Objekt verarbeitet werden soll.
     */
    public void eventDispatched(EventObject e);     
    
}
