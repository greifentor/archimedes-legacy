/*
 * EditorDjinnEventType.java
 *
 * 05.06.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/** 
 * Diese Enum dient der Typisierung der vom EditorDjinObserver verarbeiteten 
 * EditorDjinn-Ereignissen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 05.06.2008 - Hinzugef&uuml;gt.
 *     <P>
 *
 */
 
public enum EditorDjinnEventType {
    
    /** 
     * Dieses Ereignis wird ausgel&ouml;st, nachdem der EditorDjinn geschlossen wird, aber
     * bevor das Event <I>djinnClosed</I> an die angebundenen EditorDjinnListener 
     * weitergegeben wird.
     */
    DISPELLED,
    
    /** 
     * Dieses Ereignis wird ausgel&ouml;st, nachdem der EditorDjinn erzeugt und mit Daten 
     * bef&uuml;llt worden ist. Es ersetzt den Aufruf der Methode 
     * <TT>doAfterDjinnSummoned(Hashtable&lt;String, java.awt.Component&gt; comps, 
     EditorDjinnMode mode)</TT> aus dem Interface <TT>EditorDjinnMaster</TT>.
     */
    SUMMONED
}
