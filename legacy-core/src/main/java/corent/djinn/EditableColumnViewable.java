/*
 * EditableColumnViewable.java
 *
 * 11.06.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.table.*;


/**
 * Diese Interface definiert das notwendige Verhalten f&uuml;r Objekte, die in Tabellensichten
 * dargestellt und manipuliert werden k&ouml;nnen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface EditableColumnViewable extends ColumnViewable {
    
    /** 
     * @param col Der CellEditor, der in Verbindung mit der Spalte col eingesetzt werden soll.
     * @return Der TableCellEditor zur Spalte. 
     */
    public TableCellEditor getCellEditor(int col);
    
    /** 
     * Pr&uuml;ft, ob das Attribut des Objekt in der Tabellensicht ge&auml;ndert werden kann. 
     *
     * @param col Die Nummer der Tabellenspalte, zu der die Pr&uuml;fung stattfinden soll.
     * @return <TT>true</TT>, wenn das Attribut ge&auml;ndert werden darf. 
     */
    public boolean isCellEditable(int col);

    /**
     * &Auml;ndert den Wert des durch die col angegebenen Attributes durch den in obj 
     * definierten ab.
     *
     * @param obj Der neue Wert f&uuml;r das durch die Spalte col repr&auml;sentierte Attribut.
     * @param col Die Nummer der Spalte, zu der der Wert geliefert werden soll.
     */
    public void setValueAt(Object obj, int col);
    
}
