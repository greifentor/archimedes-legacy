/*
 * SelectionComponent.java
 *
 * 03.10.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.gui.*;

import java.awt.event.*;


/**
 * Dieses Interface beschreibt den Funktionsumfang einer Klasse, die als Selektionskomponente
 * f&uuml;r SelectionDjinns herhalten soll.
 *
 * <P><B>Hinweis:</B> Die implementierende Klasse mu&szlig; eine <TT>JComponent</TT> sein! 
 *
 * @author
 *    O.Lieshoff
 *    <P>
 *
 * @changed
 *    OLI 03.10.2008 - Hinzugef&uuml;gt.
 *    <P>
 *
 */
 
public interface SelectionComponent {
    
    public void addActionListener(ActionListener l);
    public void addKeyListener(KeyListener l);
    public void addMassiveListSelectorListener(MassiveListSelectorListener l);
    
    /**
     * Liefert zus&auml;tzlich zu den im Schnellsucheformat gemachte Suchkriterien.
     *
     * @return Ein String mit zus&auml;tzlich zu den im Schnellsucheformat gemachte 
     *     Suchkriterien.
     *
     */
    public String getAdditions();
    
    /**
     * Liefert eine Anfrage im Schnellsucheformat.
     *
     * @return Ein String mit Suchkriterien im Schnellsucheformat.
     */
    public String getFastCriteria();
    
    public void removeActionListener(ActionListener l);
    public void removeKeyListener(KeyListener l);
    public void removeMassiveListSelectorListener(MassiveListSelectorListener l);
    
}
