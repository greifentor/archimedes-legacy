/*
 * EditorDjinnPanel.java
 *
 * 10.01.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.djinn;


import java.awt.*;
import java.util.*;


/**
 * Diese Interface definiert die notwendigen Methoden einer EditorDjinn-Komponente. Diese 
 * Komponente ist in der Lage Objekte, die das Interface Editable implementieren, zur Anzeige
 * zu bringen und gegebenenfalls das &Auml;ndern der Attribute des Objektes zu erm&ouml;glichen.
 * Um auf die Ereignisse des EditorDjinns reagieren zu k&ouml;nnen, mu&szlig; das Fenster, in 
 * dem die Komponente abgebildet wird, den EditorDjinnListener implementieren.
 * <P>Die letztendliche Darstellung der Attribute innerhalb der Komponente wird durch den 
 * EditorDescriptor geregelt.
 *
 * @author O.Lieshoff
 *
 * @changed
 *     OLI 23.09.2007 - Hinzuf&uuml;gen der Methode <TT>getComponentTable()</TT>.
 *     <P>OLI 29.10.2007 - Erweiterung der Parameterliste der Methode 
 *             <TT>fireObjectChanged()</TT> um den Parameter <TT>saveOnly</TT>. Bei dieser 
 *             Gelegenheit: alphabetische Sortierung der Methoden des Interfaces.
 *     <P>OLI 05.01.2009 - Erweiterung um die Methode <TT>setEditable(Editable)</TT>.
 *     <P>OLI 12.01.2009 - R&uuml;ckbau der Methode <TT>setEditable(Editable)</TT>. Im Moment 
 *             ist nicht ausreichend Zeit, um das vern&uuml;nftig auszuprogrammieren (wegen
 *             Unhandlichkeiten im <TT>DefaultEditorDjinnPanel</TT>).
 *     <P>
 *
 */
 
public interface EditorDjinn {
    
    /** 
     * F&uuml;gt den &uuml;bergebenen EditorDjinnListener an die Liste derjenigen 
     * EditorDjinnListener an, die auf die Ereignisse des EditorDjinns reagieren sollen.
     *
     * @param edl Der aufzunehmende EditorDjinnListener.
     */
    public void addEditorDjinnListener(EditorDjinnListener edl);

    /** 
     * Diese Methode ruft die EditorDjinnListener auf und &uuml;bermittelt ein 
     * 'batch changed'-Ereignis.
     *
     * @param ht Eine Hashtable&lt;Integer, Object&gt; mit Attribute-Id-Wert-Paaren, die die 
     *     ge&auml;nderten Datenfelder und ihre neuen Werte enthalten. 
     */
    public void fireObjectBatchChanged(Hashtable<Integer, Object> ht);
        
    /** 
     * Diese Methode ruft die EditorDjinnListener auf und &uuml;bermittelt ein 
     * 'changed'-Ereignis.
     *
     * @param saveOnly Diese Flagge wird gesetzt, wenn das bearbeitete Objekt gespeichert, der
     *     EditorDjinn aber nicht geschlossen werden soll.
     */
    public void fireObjectChanged(boolean saveOnly);
        
    /** 
     * Diese Methode ruft die EditorDjinnListener auf und &uuml;bermittelt ein 
     * 'djinnClosed'-Ereignis. Dieses tritt ein, wenn der Djinn das Schlie&szlig;-Prozedere 
     * abgeschlossen hat.
     */
    public void fireDjinnClosed();
    
    /** 
     * Diese Methode ruft die EditorDjinnListener auf und &uuml;bermittelt ein 
     * 'djinnClosing'-Ereignis. Dieses tritt ein, wenn der Djinn das Schlie&szlig;-Prozedere 
     * beginnt.
     */
    public void fireDjinnClosing();
    
    /** 
     * Diese Methode ruft die EditorDjinnListener auf und &uuml;bermittelt ein 
     * 'deleted'-Ereignis.
     */
    public void fireObjectDeleted();
        
    /** 
     * Diese Methode ruft die EditorDjinnListener auf und &uuml;bermittelt ein 
     * 'discarded'-Ereignis.
     */
    public void fireObjectDiscarded();
        
    /** 
     * Diese Methode ruft die EditorDjinnListener auf und &uuml;bermittelt ein 
     * 'objectPrinted'-Ereignis.
     */
    public void fireObjectPrinted();
        
    /** 
     * Diese Methode ruft die EditorDjinnListener auf und &uuml;bermittelt ein 
     * 'objectReadyToPrint'-Ereignis.
     */
    public void fireObjectReadyToPrint();
    
    /**
     * Liefert eine Hashtable mit den Komponenten des EditorDjinns, die nach ihren Namen 
     * geschl&uuml;sselt sind.
     *
     * @return Eine Hashtable mit den Komponenten des EditorDjinnsm die nach ihren Namen 
     *     geschl&uuml;sselt sind.
     *
     * @changed
     *     OLI 23.09.2007 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public Hashtable<String, Component> getComponentTable();
    
    /** 
     * Liefert das Objekt, das in dem EditorDjinn bearbeitet wird.
     *
     * @return Eine Referenz auf das im EditorDjinn angezeigte Editable. 
     */
    public Editable getEditable();
    
    /**
     * Liefert eine Referenz auf doe Komponente, in der der EditorDjinn abgebildet wird.
     *
     * @return Eine Referenz auf die Component, in dem der EditorDjinn abgebildet wird. 
     */
    public Component getOwner(); 
    
    /** 
     * L&ouml;scht den &uuml;bergebenen EditorDjinnListener aus der Liste derjenigen 
     * EditorDjinnListener, die auf die Ereignisse des EditorDjinns reagieren sollen.
     *
     * @param edl Der zu l&ouml;schende EditorDjinnListener.
     */
    public void removeEditorDjinnListener(EditorDjinnListener edl);
    
    /* *
     * Setzt das &uuml;bergebene Editable als neues Objekt ein, das durch den Djinn 
     * ge&auml;ndert werden kann.
     *
     * <B>WICHTIG:</B> Es mu&szlig; <U>nicht</U> gesichert sein, da&szlig; der Inhalt bzw. die
     * Anzeige des EditorDjinn aktualisiert wird.
     *
     * @changed
     *     OLI 05.01.2009 - Hinzugef&uuml;gt.
     *     <P>OLI 12.01.2009 - R&uuml;ckbau. Das ist im Moment nich anst&auml;ndig umzusetzen.
     *     <P>
     *
     * /
    public void setEditable(Editable e);
    */

}
