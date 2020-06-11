/*
 * SelectionDjinn.java
 * 
 * 04.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


/**
 * Mit Hilfe dieses Interfaces wird die notwendige Funktionalit&auml;t f&uuml;r ein 
 * SelectionDjinn beschrieben.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 */
 
public interface SelectionDjinn {
    
    /** 
     * F&uuml;gt den &uuml;bergebenen SelectionDjinnListener an die Liste derjenigen 
     * SelectionDjinnListener an, die auf die Ereignisse des SelectionDjinns reagieren sollen.
     *
     * @param sdl Der aufzunehmende SelectionDjinnListener.
     */
    public void addSelectionDjinnListener(SelectionDjinnListener sdl);

    /** 
     * L&ouml;scht den &uuml;bergebenen SelectionDjinnListener aus der Liste derjenigen 
     * SelectionDjinnListener, die auf die Ereignisse des SelectionDjinns reagieren sollen.
     *
     * @param sdl Der zu l&ouml;schende SelectionDjinnListener.
     */
    public void removeSelectionDjinnListener(SelectionDjinnListener sdl);

    /** 
     * Diese Methode ruft die SelectionDjinnListener auf und &uuml;bermittelt ein 
     * 'selectionDone'-Ereignis.
     *
     * @param selected Die Liste der selektierten Objekte.
     */
    public void fireSelectionDone(Vector selected);
    
    /** 
     * Diese Methode ruft die SelectionDjinnListener auf und &uuml;bermittelt ein 
     * 'selectionAborted'-Ereignis.
     */
    public void fireSelectionAborted();
                
    /** 
     * Diese Methode ruft die SelectionDjinnListener auf und &uuml;bermittelt ein 
     * 'selectionDuplicated'-Ereignis.
     *
     * @param selected Die Liste der selektierten Objekte.
     */
    public void fireSelectionDuplicated(Vector selected);
    
    /** 
     * Diese Methode ruft die SelectionDjinnListener auf und &uuml;bermittelt ein 
     * 'selectionUpdated'-Ereignis.
     */
    public void fireSelectionUpdated();
    
    /** 
     * Diese Methode ruft die SelectionDjinnListener auf und &uuml;bermittelt ein 
     * 'elementCreated'-Ereignis.
     */
    public void fireElementCreated();
                
    /** 
     * Diese Methode ruft die SelectionDjinnListener auf und &uuml;bermittelt ein 
     * 'djinnClosing'-Ereignis. Dieses tritt ein, wenn der Djinn das Schlie&szlig;-Prozedere 
     * beginnt.
     */
    public void fireDjinnClosing();
    
    /** 
     * Diese Methode ruft die SelectionDjinnListener auf und &uuml;bermittelt ein 
     * 'djinnClosed'-Ereignis. Dieses tritt ein, wenn der Djinn das Schlie&szlig;-Prozedere 
     * abgeschlossen hat.
     */
    public void fireDjinnClosed();
    
}
