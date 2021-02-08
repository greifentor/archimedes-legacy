/*
 * SelectionDjinnListener.java
 *
 * 04.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


/**
 * Mit Hilfe dieses Interfaces werden die Ereignisse eines SelectionDjinns definiert.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface SelectionDjinnListener {
    
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Auswahl-Button bet&auml;tigt worden 
     * ist.
     *
     * @param selected Die in dem Djinn ausgew&auml;hlten Objekte.
     */
    public void selectionDone(Vector selected);
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Abbruch-Button bet&auml;tigt worden 
     * ist.
     */
    public void selectionAborted();
    
    /**
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Duplizieren-Button gedr&uuml;ckt worden
     * ist.
     *
     * @param selected Die markierten Elemente der Liste.
     */
    public void selectionDuplicated(Vector selected);
    
    /** Diese Methode wird aufgerufen, wenn die Selection aktualisiert wird. */
    public void selectionUpdated();
        
    /**
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Neu-Button gedr&uuml;ckt worden ist.
     */
    public void elementCreated();
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Djinn mit dem Prozedere zum 
     * Schlie&szlig;en beginnt.
     */
    public void djinnClosing();
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Djinn das Prozedere zum Schlie&szlig;en
     * abgeschlossen hat.
     */
    public void djinnClosed();
        
}
