/*
 * ExtendedColor.java
 *
 * 12.06.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import corent.base.*;

import java.awt.*;
import java.awt.color.*;


/**
 * Diese Erweiterung der Color-Klasse bietet die M&ouml;glichkeit einen Fundus an Farben 
 * menschenlesbar darzustellen und aus der menschenlesbaren Information Color-Objekte zu 
 * generieren.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.12.2015 - Extended by the interface "NamedObject".
 */
 
public class ExtendedColor extends Color implements Comparable, NamedObject {
    
    /* Der menschenlesbare Name der Farbe. Nach M&ouml;glichkeit eindeutig ;o). */
    public String name = null;
    
    /** 
     * Generiert ein neues Farben-Objekt mit den angegebenen Parametern.
     *
     * @param name Der menschenlesbare Name der Farbe.
     * @param cspace Der ColorSpace, der genutzt wird, um die Komponenten zu interpretieren.
     * @param components Eine Anzahl von Komponenten, die kompatibel sind mit dem 
     * @param alpha Alphawert
     */
    public ExtendedColor(String name, ColorSpace cspace, float[] components, float alpha) {
        super(cspace, components, alpha);
        this.name = name;
    }
    
    /** 
     * Generiert ein neues Farben-Objekt mit den angegebenen Parametern.
     *
     * @param name Der menschenlesbare Name der Farbe.
     * @param r Die Rot-Komponente der Farbe.
     * @param g Die Gr&uuml;n-Komponente der Farbe.
     * @param b Die Blau-Komponente der Farbe.
     */
    public ExtendedColor(String name, float r, float g, float b) {
        super(r, g, b);
        this.name = name;
    }
    
    /** 
     * Generiert ein neues Farben-Objekt mit den angegebenen Parametern.
     *
     * @param name Der menschenlesbare Name der Farbe.
     * @param r Die Rot-Komponente der Farbe.
     * @param g Die Gr&uuml;n-Komponente der Farbe.
     * @param b Die Blau-Komponente der Farbe.
     * @param a Die Alpha-Komponente zur Farbe.
     */
    public ExtendedColor(String name, float r, float g, float b, float a) {
        super(r, g, b, a);
        this.name = name;
    }

    /** 
     * Generiert ein neues Farben-Objekt mit den angegebenen Parametern.
     *
     * @param name Der menschenlesbare Name der Farbe.
     * @param rgb Die kombinierten RGB-Komponenten.
     */
    public ExtendedColor(String name, int rgb) {
        super(rgb);
        this.name = name;
    }

    /** 
     * Generiert ein neues Farben-Objekt mit den angegebenen Parametern.
     *
     * @param name Der menschenlesbare Name der Farbe.
     * @param rgba Die kombinierten RGB- und Alpha-Komponenten.
     * @param hasalpha <TT>true</TT>, falls das Alpha-Bit gelesen g&uuml;ltig ist, 
     *     <TT>false</TT> sonst.
     */
    public ExtendedColor(String name, int rgba, boolean hasalpha) {
        super(rgba, hasalpha);
        this.name = name;
    }
    /** 
     * Generiert ein neues Farben-Objekt mit den angegebenen Parametern.
     *
     * @param name Der menschenlesbare Name der Farbe.
     * @param r Die Rot-Komponente der Farbe.
     * @param g Die Gr&uuml;n-Komponente der Farbe.
     * @param b Die Blau-Komponente der Farbe.
     */
    public ExtendedColor(String name, int r, int g, int b) {
        super(r, g, b);
        this.name = name;
    }
    
    /** 
     * Generiert ein neues Farben-Objekt mit den angegebenen Parametern.
     *
     * @param name Der menschenlesbare Name der Farbe.
     * @param r Die Rot-Komponente der Farbe.
     * @param g Die Gr&uuml;n-Komponente der Farbe.
     * @param b Die Blau-Komponente der Farbe.
     * @param a Die Alpha-Komponente zur Farbe.
     */
    public ExtendedColor(String name, int r, int g, int b, int a) {
        super(r, g, b, a);
        this.name = name;
    }
    
    /**
     * Generiert ein neues Farben-Objekt aus den Daten des &uuml;bergebenen Farb-Objektes an. 
     *
     * @param name Der menschenlesbare Name der Farbe.
     * @param c Die Farbe, deren Komponentendaten kopiert werden sollen.
     */
    public ExtendedColor(String name, Color c) {
        super(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha());
        this.name = name;
    }

    /**
     * Returns the name of the extended color.
     *
     * @return The name of the extended color.
     *
     * @changed OLI 23.12.2015 - Added.
     */
    public String getName() {
        return this.name;
    }

    
    /* &Uuml;berschriebene Methoden der Superklasse. */
    
    public String toString() {
        return this.name;
    }
    
    
    /* Implementierung des Interfaces Comparable. */
    
    public int compareTo(Object o) {
        return this.name.compareTo(((ExtendedColor) o).name);
    }

}
