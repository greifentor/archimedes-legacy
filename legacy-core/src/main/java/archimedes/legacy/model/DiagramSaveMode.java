/*
 * DiagramSaveMode.java
 *
 * 16.08.2008
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.model;


/**
 * Ein Typ zur Festlegung des Speichermodus von Datenmodellen.
 *
 * @author
 *     <P>ollie
 *     <P>
 *
 * @changed
 *     <P>OLI 16.08.2008 - Hinzugef&uuml;gt.
 *
 */
 
public enum DiagramSaveMode {

    /** Modus zum normalen Speichern des Modells mit allen verf&uuml;gbaren Daten. */
    REGULAR,

    /** 
     * Modus zum Speichern des Modells nur mit applicationsrelevanten Daten. Daten zur 
     * grafischen Darstellung des Modell entfallen hier.
     */
    APPLICATION;
    
}
