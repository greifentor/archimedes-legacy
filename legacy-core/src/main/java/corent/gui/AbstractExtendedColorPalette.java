/*
 * AbstractExtendedColorPalette.java
 *
 * 12.06.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.base.*;

import java.awt.*;
import java.util.*;


/**
 * Mit Hilfe dieser Klasse k&ouml;nnen Paletten von menschenlesbaren Farben erzeugt werden.
 *
 * @author O.Lieshoff
 *
 */
 
abstract public class AbstractExtendedColorPalette {
    
    /* Die Liste mit den Farben der Palette. */
    private Hashtable<String, ExtendedColor> palette = null;
    
    /** Erzeugt eine neue Palette mit ExtendedColors. */
    public AbstractExtendedColorPalette() {
        super();
        this.palette = this.createPalette();
    }
    
    /** 
     * Liefert eine Farbe zum angegebenen Namen.
     *
     * @param name Der Name der Farbe, die zur&uuml;ckgegeben werden soll.
     * @return Die passende Farbe zum Namen, bzw. <TT>null</TT>, wenn es zu dem Namen keine
     *    Farbe in der Palette gibt.
     */
    public ExtendedColor get(String name) {
        return this.palette.get(name);
    }
    
    /** 
     * Liefert eine Farbe zum angegebenen Namen.
     *
     * @param name Der Name der Farbe, die zur&uuml;ckgegeben werden soll.
     * @return Die passende Farbe zum Namen, bzw. defautlc, wenn es zu dem Namen keine
     *    Farbe in der Palette gibt.
     */
    public Color get(String name, Color defaultc) {
        Color c = this.palette.get(name);
        if (c == null) {
            c = defaultc;
        }
        return c;
    }
    
    /** @return Eine Liste mit den Farben der Palette. */
    public java.util.List<ExtendedColor> getColors() {
        SortedVector sv = new SortedVector();
        for (Enumeration e = this.palette.keys(); e.hasMoreElements(); ) {
            ExtendedColor c = this.palette.get((String) e.nextElement());
            sv.addElement(c);
        }
        Vector<ExtendedColor> v = new Vector<ExtendedColor>();
        for (int i = 0, len = sv.size(); i < len; i++) {
            v.addElement((ExtendedColor) sv.elementAt(i));
        }
        return v;
    }
    
    /**
     * L&ouml;scht den Inhalt der AbstractExtendedColorPalette.
     */
    public void removeColors() {
        this.palette.clear();
    }
    
    /** 
     * Setzt eine Farbe zum angegebenen Namen. Ist bereits eine Farbe mit dem angegebenen Namen
     * vorhanden, so wird diese gel&ouml;scht.
     *
     * @param name Der Name der Farbe, zu der die angegebene ExtendedColor gesetzt werden soll.
     * @param c Die ExtendedColor, die zu dem angegebenen Namen gesetzt werden soll.
     */
    public void set(String name, ExtendedColor c) {
        this.palette.put(name, c);
    }
    
    /**
     * Diese Methode mu&szlig; eine Hashtable&gt;String,ExtendedColor&lt; liefern, in dem die 
     * Farben der Palette abgelegt sind.
     */
    abstract public Hashtable<String,ExtendedColor> createPalette();
    
}
