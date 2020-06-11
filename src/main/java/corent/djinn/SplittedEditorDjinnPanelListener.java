/*
 * SplittedEditorDjinnPanelListener.java
 *
 * 13.05.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Dieses Interface kann von Objekten implementiert werden, die in einem EditorDjinnPanel 
 * angezeigt werden sollen und &uuml;ber den aktuellen Splitstatus des Panels informiert werden
 * sollen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 11.05.2008 - Hinzugef&uuml;gt.
 *
 */
 
public interface SplittedEditorDjinnPanelListener {
    
    /**
     * Liefert den aktuellen Splitstatus beim Aufruf eines EditorDjinnPanels, in dem das 
     * implementierende Objekt bearbeitet werden soll.
     *
     * @param splitstatus Der aktuelle Splitstatus des EditorDjinnPanels, in dem das 
     *     implementierende Objekt bearbeitet werden soll. Wird der Wert <TT>true</TT> 
     *     &uuml;bergeben, so ist das EditorDjinnPanel ein SplitPane, andernfalls handelt es
     *     sich um ein normales Panel.
     */
    public void notifySplitState(boolean splitstatus);
    
}
