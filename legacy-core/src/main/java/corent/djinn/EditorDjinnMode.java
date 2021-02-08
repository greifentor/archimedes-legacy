/*
 * EditorDjinnMode.java
 *
 * 26.02.2008
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Ein Type zur Definition der Funktion eines EditorDjinnaufrufes.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 26.02.2008 - Hinzugef&uuml;gt.
 *
 */
 
public enum EditorDjinnMode {
    
    /** Typ zur Kennzeichnung einer Neuanlage. */
    CREATE,

    /** Typ zur Kennzeichnung einer Duplikation. */
    DUPLICATE,
    
    /** Typ zur Kennzeichnung einer &Auml;nderung. */
    EDIT;
    
}
