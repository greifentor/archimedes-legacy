/*
 * RessourceManager.java
 *
 * 15.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import java.awt.*;

import javax.swing.*;


/**
 * Dieses Interface definiert das notwendige Verhalten eines RessourceManagers. Er dient dazu
 * Ressourced-Komponenten mit neuen Daten, die vornehmlich im Bereich der GUI-Ansicht liegen 
 * sollten (Stichwort: Mehrsprachigkeit).
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.10.2010 - Erweiterung um die Methode <TT>getText(String, String)</TT>. 
 */
 
public interface RessourceManager {
    
    /**
     * Liefert die Hintergrundfarbe zum angegebenen Context-Namen.
     *
     * @return Die Hintergrundfarbe zum angegebenen Context-Namen bzw. <TT>null</TT>, wenn es 
     *     keine Hintergrundfarben-Ressource zum angegebenen Context-Namen gibt.
     */
    public Color getBackground(String cn);
    
    /**
     * Liefert die Vordergrundfarbe zum angegebenen Context-Namen.
     *
     * @return Die Vordergrundfarbe zum angegebenen Context-Namen bzw. <TT>null</TT>, wenn es 
     *     keine Vordergrundfarben-Ressource zum angegebenen Context-Namen gibt.
     */
    public Color getForeground(String cn);
    
    /**
     * Liefert das Icon zum angegebenen Context-Namen.
     *
     * @return Das Icon zum angegebenen Context-Namen bzw. <TT>null</TT>, wenn es keine 
     *     Icon-Ressource zum angegebenen Context-Namen gibt.
     */
    public Icon getIcon(String cn);
    
    /**
     * Liefert das Mnemonic zum angegebenen Context-Namen.
     *
     * @return Das Mnemonic zum angegebenen Context-Namen bzw. '\u00FF', wenn es keine 
     *     Mnemonic-Ressource zum angegebenen Context-Namen gibt.
     */
    public char getMnemonic(String cn);
    
    /**
     * Liefert den Text zum angegebenen Context-Namen.
     *
     * @return Der Text zum angegebenen Context-Namen, bzw. "", wenn es keine Text-Ressource 
     *     angegebenen Context-Namen gibt. 
     */
    public String getText(String cn);
    
    /**
     * Liefert den Text zum angegebenen Context-Namen.
     *
     * @param cn Der Name der Ressource, zu der der Wert geliefert werden soll.
     * @param dflt Der Defaultwert, der geliefert werden soll, falls zu der angegebenen
     *     Ressource kein Wert hinterlegt ist.
     * @return Der Text zum angegebenen Context-Namen, bzw. "", wenn es keine Text-Ressource 
     *     angegebenen Context-Namen gibt. 
     */
    public String getText(String cn, String dflt);
    
    /**
     * Liefert den Titel zum angegebenen Context-Namen.
     *
     * @return Der Titel zum angegebenen Context-Namen, bzw. "", wenn es keine Titel-Ressource 
     *     angegebenen Context-Namen gibt. 
     */
    public String getTitle(String cn);
    
    /**
     * Liefert den ToolTipText zum angegebenen Context-Namen.
     *
     * @return Der ToolTipText zum angegebenen Context-Namen, bzw. "", wenn es keine 
     *     ToolTipText-Ressource angegebenen Context-Namen gibt. 
     */
    public String getToolTipText(String cn);
    
}
