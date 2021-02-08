/*
 * EditorDjinnListener.java
 *
 * 10.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


/**
 * Mit Hilfe dieses Interfaces werden die Ereignisse eines EditorDjinns definiert.
 *
 * @author
 *     O.Lieshoff
 *
 * @changed
 *     OLI 29.10.2007 - Erweiterung der Parameterliste der Methode <TT>objectChanged()</TT> um
 *             den Parameter <TT>saveOnly</TT>. Bei dieser Gelegenheit: alphabetische 
 *             Sortierung der Methoden des Interfaces.<BR>
 *
 */
 
public interface EditorDjinnListener {
    
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der &Uuml;bernehmen-Button bet&auml;tigt
     * worden ist.
     *
     * @param ht Eine Hashtable&lt;Integer, Object&gt; mit Attribute-Id-Wert-Paaren, die die 
     *     ge&auml;nderten Datenfelder und ihre neuen Werte enthalten. 
     */
    public void objectBatchChanged(Hashtable<Integer, Object> ht);
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der &Uuml;bernehmen-Button bet&auml;tigt
     * worden ist.
     *
     * @param saveOnly Wird diese Flagge gesetzt, so soll der in dem Djinn enthaltene Datensatz
     *     lediglich gespeichert, der EditorDjinn aber nicht geschlossen werden.
     *
     * @changed
     *     OLI 29.10.2007 - Erweiterung um den Parameter <TT>saveOnly</TT>.<BR>
     */
    public void objectChanged(boolean saveOnly);
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Djinn das Prozedere zum Schlie&szlig;en
     * abgeschlossen hat.
     */
    public void djinnClosed();
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Djinn mit dem Prozedere zum 
     * Schlie&szlig;en beginnt.
     */
    public void djinnClosing();
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der L&ouml;schen-Button bet&auml;tigt 
     * worden ist.
     */
    public void objectDeleted();
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Verwerfen-Button bet&auml;tigt worden 
     * ist.
     */
    public void objectDiscarded();
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Drucken-Button bet&auml;tigt worden 
     * ist; nachdem der EditorDjinn den Druckauftrag ausgef&uuml;hrt hat.
     */
    public void objectPrinted();
        
    /** 
     * Diese Methode kommt zur Ausf&uuml;hrung, wenn der Drucken-Button bet&auml;tigt worden 
     * ist; bevor der EditorDjinn den Druckauftrag ausgef&uuml;hrt hat.
     */
    public void objectReadyToPrint();
    
} 
