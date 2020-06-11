/*
 * Constants.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base;


import corent.gui.*;

import java.awt.*;

import javax.swing.border.*;


/**
 * Diese Klasse bietet eine Sammlung von verschiedenen Konstanten zur globalen Manipulation der
 * corent-Bibliothek.
 *
 * @author O.Lieshoff
 *
 */
 
public class Constants {
    
    /** Globale Voreinstellung zum Restoreverhalten der ComponentsWithInifile. */
    public static boolean BRUTERESTORE = true; 
    /** Standard Farbe f&uuml;r die Umrahmung der fokussierten Komponente in EditorDjinns. */
    public static Color DJINNBORDER = Color.red;
    /** Standard Etch f&uuml;r Rahmen der eigenen GUI-Komponenten. */
    public static int ETCH = EtchedBorder.RAISED;
    /** 
     * Konstante f&uuml;r den horizontalen Abstand von GUI-Komponenten in diversen Containern. 
     */
    public static int HGAP = 3;
    /** Konstante zur betriebssystemabh&auml;ngigen Einstellung des Zeilenende-Zeichens. */
    public static String LINEEND = null;
    /** 
     * Konstante f&uuml;r den vertikalen Abstand von GUI-Komponenten in diversen Containern. 
     */
    public static int VGAP = 3;
    /**
     * Hier kann ein statischer Standard RessourceManager gesetzt werden, den die ContextOwner
     * beim verschiedenen Gelegenheiten (z. B. Context-&Auml;nderungen) aufrufen k&ouml;nnen.
     */
    public static RessourceManager StandardRessourceManager = null;
    
    
    /** Betriebssystemspezifische Initialisierung. */
    static {
        String s = System.getProperty("os.name");
        if (s.equalsIgnoreCase("linux") || s.equalsIgnoreCase("unix")) {
            LINEEND = "\n";
        } else if (s.toLowerCase().indexOf("windows") > -1) {
            LINEEND = "\r\n";
        } else {
            LINEEND = "\n";
        }
    }
    
}
