/*
 * DefaultSelectionTableRow.java
 *
 * 02.12.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.db.xs;


import corent.base.*;

import java.util.*;


/**
 * Mit Hilfe dieser Musterimplementierung des DefaultSelectionTables k&ouml;nnen die meisten
 * Standardsituationen abgedeckt werden.
 *
 * @author
 *     O.Lieshoff
 *
 * @changed 
 *     OLI 20.02.2008 - Umstellung auf die Methode <TT>Utl.CompareNull(Comparable, 
 *             Comparable)</TT> in der Methode <TT>compareTo(Object)</TT>.
 */
 
public class DefaultSelectionTableRow implements Comparable, SelectionTableRow {

    /* Die Liste mit den Daten der angezeigten Tabellenspalten. */
    private Vector data = new Vector();
    /* Der Key der Tabellenzeile. */
    private Object keys = null;
    /* 
     * Eine Liste mit den Indices der Datenfelder, nach denen die Tabellenzeilen sortiert
     * werden koennen.
     */
    private Vector<Integer> sort = new Vector<Integer>();
    
    /**
     * Generiert eine DefaultSelectionTableRow anhand der &uuml;bergebenen Parameter.
     *
     * @param keys Der Key zur Tabellenzeile.
     * @param data Liste mit den Inhalten der anzuzeigenden Datenfelder der Tabellenzeile.
     * @param sort Liste mit den Indices der Tabellenspalten nach denen die Tabellenzeilen 
     *     sortiert werden sollen.
     */
    public DefaultSelectionTableRow(Object keys, Vector data, Vector<Integer> sort) {
        super();
        this.data = data;
        this.keys = keys;
        this.sort = sort;
    }
    
    
    /* Implementierung des Interfaces Comparable. */
    
    /**
     * @changed 
     *     OLI 20.02.2008 - Umstellung auf die Methode Utl.CompareNull(Comparable, Comparable).
     */
    public int compareTo(Object o) {
        Vector data0 = ((DefaultSelectionTableRow) o).getData();
        for (int i = 0, len = sort.size(); i < len; i++) {
            int index = sort.elementAt(i);
            Comparable c0 = (Comparable) this.data.elementAt(index);
            int comp = Utl.CompareNull(c0, (Comparable) data0.elementAt(index));
            // int comp = c0.compareTo(data0.elementAt(index));
            if (comp != 0) {
                return comp;
            }
        }
        return 0;
    }
    
    
    /* Implementierung des Interfaces SelectionTableRow. */
    
    public Object getKeys() {
        return this.keys;
    }
    
    public Vector getData() {
        return this.data;
    }
    
}
