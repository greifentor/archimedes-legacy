/*
 * ColumnViewable.java
 *
 * 09.07.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.table.*;


/**
 * Diese Interface definiert das notwendige Verhalten f&uuml;r Objekte, die in Tabellensichten
 * dargestellt werden k&ouml;nnen.
 *
 * @author O.Lieshoff
 *
 */
 
public interface ColumnViewable {
    
    /** 
     * @return Anzahl der Spalten, die zur Darstellung des Objekts in einer Tabellensicht 
     *     ben&ouml;tigt werden. 
     */
    public int getColumnCount();
    
    /** @return Ein Feld mit den Spalten&uuml;berschriften f&uuml;r die Tabellensicht. */
    public String[] getColumnnames();

    /** 
     * Liefert eine Klassenangabe zur Tabellenspalte. 
     *
     * @param col Die Nummer der Tabellenspalte, zu der die Klassenangabe geliefert werden soll.
     * @return Die Klasse, f&uuml;r die der Renderer der Tabellenspalte erstellt werden soll. 
     */
    public Class getColumnclass(int col);

    /**
     * Liefert den Wert, der bei der Darstellung des Objektes in einer Tabellensicht in der 
     * angegebenen Spalte angezeigt werden soll.
     *
     * @param col Die Nummer der Spalte, zu der der Wert geliefert werden soll.
     * @return Der Wert zur angegebenen Spalte.
     */
    public Object getValueAt(int col);
    
    /**
     * Liefert einen alternativen TableCellRenderer zur angegebenen Spalte bzw. <TT>null</TT>,
     * falls der DefaultTableCellRenderer benutzt werden soll.
     *
     * @param column Die Nummer der Spalte, f&uuml;r die der TableCellRenderer erstellt werden
     *     soll.
     * @return Ein alternativer TableCellRenderer zur angegebenen Spalte oder <TT>null</TT>, 
     *     falls der DefaultTableCellRenderer benutzt werden soll.
     */
    public TableCellRenderer getCellRenderer(int column);
    
}
