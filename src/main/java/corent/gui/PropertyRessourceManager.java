/*
 * PropertyRessourceManager.java
 *
 * 15.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.base.*;

import java.awt.*;

import javax.swing.*;


/**
 * Diese Implementierung des RessourceManagers bezieht ihre Daten aus der 
 * System-Property-Tabelle. Die Ressourcendaten m&uuml;ssen in folgender Form hinterlegt sein:
 * <P><I>vollst&auml;ndiger Context-Name.RessourceIdent</I>
 * <P>Der Context-Name ergibt sich aus der Komponente. An RessourceIdents sind die folgende 
 * Bezeichner g&uuml;ltig:
 * <PRE>
 * <B>Bezeichner                        Werte</B>
 * background                        Name der Farbe.
 * foreground                        Name der Farbe.
 * icon                              Name der Icon-Datei (relativ zur Property "corent.gui.images").
 * mnemonic                          Mnemonic-Zeichen.
 * text                              Text der Komponente.
 * title                             Titel der Komponente.
 * toolTipText                       ToolTipText zur Komponente.
 * </PRE>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.10.2007 - Erweiterung um die Methode <TT>getTitle(String)</TT>.
 * @changed OLI 16.03.2008 - Umstellung des Propertylesens auf die Methode 
 *         <TT>Utl.GetProperty(...)</TT> zur Nutzung der HTML-Kodierung in den Ressourcen.
 * @changed OLI 06.10.2010 - Erweiterung um die Methode <TT>getText(String, String)</TT>.
 *
 * @todo OLI - Der Fall, da&szlig; keine passende Property gefunden wird, mu&szlig; abgefangen
 *         und verarbeitet werden. Es sollen alternativ eine Datei oder eine Log-Ausgabe
 *         fabriziert werden. Die Konfiguration findet &uuml;ber Properties statt
 *         (OLI 01.04.2009).
 *
 */
 
public class PropertyRessourceManager implements RessourceManager {
    
    /** 
     * Generiert einen PropertyRessourceManager, der auf der System-Property-Tabelle arbeitet.
     */
    public PropertyRessourceManager() {
        super();
    }
    
    
    /** Implementierung des Interfaces RessourceManager. */
    
    public Color getBackground(String cn) {
        Color c = null;
        String s = Utl.GetProperty(cn.concat(".background"));
        if (s != null) {
            c = ColorUtil.GetColor(s);
        }
        return c;
    }
    
    public Color getForeground(String cn) {
        Color c = null;
        String s = Utl.GetProperty(cn.concat(".foreground"));
        if (s != null) {
            c = ColorUtil.GetColor(s);
        }
        return c;
    }
    
    public Icon getIcon(String cn) {
        String s = Utl.GetProperty(cn.concat(".icon"));
        if (s == null) {
            return null;
        }
        String p = Utl.GetProperty("corent.gui.images", ".").replace("\\", "/");
        if (!p.endsWith("/")) {
            p = p.concat("/");
        }
        if (Boolean.getBoolean("corent.gui.debug")) {
            System.out.println(">>> " + p + s);
        }
        ImageIcon icon = new ImageIcon(p + s);
        return icon;
    }
    
    public char getMnemonic(String cn) {
        String s = Utl.GetProperty(cn.concat(".mnemonic"), "\u00FF");
        if (s.length() > 0) {
            return s.charAt(0);
        }
        return '\u00FF';
    }
    
    public String getText(String cn) {
        return Utl.GetProperty(cn.concat(".text"), "");
    }
    
    /**
     * @changed OLI 06.10.2010 - Hinzugef&uuml;gt.
     */
    public String getText(String cn, String dflt) {
        String s = Utl.GetProperty(cn.concat(".text"));
        if (s == null) {
            s = dflt;
        }
        return s;
    }
    
    public String getTitle(String cn) {
        return Utl.GetProperty(cn.concat(".title"), "");
    }
    
    public String getToolTipText(String cn) {
        return Utl.GetProperty(cn.concat(".tooltiptext"), "");
    }
    
}
