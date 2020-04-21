/*
 * SelectionTableRow.java
 *
 * 02.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import java.util.*;


/**
 * Dieses Interface beschreibt das Verhalten von Zeilen eines SelectionTableModels.
 *
 * @author O.Lieshoff
 *
 */
 
public interface SelectionTableRow {
    
    /**
     * Der Key der in der Tabelle zu sehenden Datens&auml;tze.
     *
     * @return Der Key der in der Tabelle angezeigten Datens&auml;tze.
     */
    public Object getKeys();
    
    /**
     * Liefert eine Liste mit den Daten der in der Tabelle anzuzeigenden Datens&auml;tze.
     *
     * @return Die Liste mit den Daten der in der Tabelle angezeigten Datens&auml;tze.
     */
    public Vector getData();
    
}
