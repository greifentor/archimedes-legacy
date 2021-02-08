/*
 * TabbedEditorDjinnCellAlterListener.java
 *
 * 05.11.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.awt.*;
import java.util.*;

import javax.swing.*;


/**
 * Dieses Interface erweitert die M&ouml;glichkeiten des EditorDjinnCellAlterListener auf die
 * Manipulation eines eventuell im EditorDjinn vorhandenen TabbedPanes.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 11.05.2008 - Hinzugef&uuml;gt.
 *
 */
 
public interface TabbedEditorDjinnCellAlterListener extends EditorDjinnCellAlterListener {
    
    /**
     * Diese Methode wird aufgerufen, wenn der Dateninhalt einer Komponente des EditorDjinns
     * ge&auml;ndert worden ist.
     *
     * @param comp Die Komponente, deren Dateninhalt ge&auml;ndert worden ist.
     * @param comps Eine Liste mit Referenzen auf die anderen Komponenten des Djinns.
     * @param jtp Eine Referenz auf das TabbedPane des EditorDjinnPanels bzw. <TT>null</TT>, 
     *     wenn es kein TabbedPane in dem abgeh&ouml;rten EditorDjinnPanel gibt oder, wenn es 
     *     sich um eine &Auml;nderung innerhalb eines Stapelpflegedialoges handelt.
     *     <BR>Sicherheitshalber sollte die Referenz daher gegen <TT>null</TT> gepr&uuml;ft 
     *     werden.
     */
    public void dataChanged(Component comp, Hashtable<String, Component> comps, 
            JTabbedPane jtp);
    
}
