/*
 * VectorTableModel.java
 *
 * 28.10.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;
import javax.swing.table.*;


public class VectorTableModel extends AbstractTableModel {
    
    protected String[] columnname = new String[] {"Tabellenspalte"};
    protected Vector v = null;
    
    public VectorTableModel(Vector v) {
        super();
        this.v = v;
    }
    
    public VectorTableModel(Vector v, String[] columnnames) {
        this(v);
        this.columnname = columnnames;
    }
    
    /**
     * Liefert eine Referenz auf den Vector, der dem TableModel zugrunde liegt.
     *
     * @return Die Referenz auf den dem TableModel zugrunde liegenden Vector.
     */
    public Vector getVector() {
        return this.v;
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public Class getColumnClass(int column) {
        if ((v.size() > 0) && (v.elementAt(0) instanceof ColumnViewable)) {
            return ((ColumnViewable) v.elementAt(0)).getColumnclass(column);
        }
        return new String().getClass();
    }

    public String getColumnName(int column) {
        if ((this.columnname != null) && (column >= 0) && (column < this.columnname.length)
                && (this.columnname[column] != null)) {
            return (String) this.columnname[column];
        }
        return "";
    }

    public int getRowCount() {
        return v.size();
    }

    public int getColumnCount() {
        if (this.columnname != null) {
            return this.columnname.length;
        }
        return 1;
    }

    public Object getValueAt(int row, int column) {
        Object o = v.elementAt(row);
        if (o instanceof ColumnViewable) {
            return ((ColumnViewable) o).getValueAt(column);
        }
        return o.toString();
    }
    
    public boolean isCellEditable(int row, int column) {
        if ((v.size() > row) && (v.elementAt(row) instanceof EditableColumnViewable)) {
            return ((EditableColumnViewable) v.elementAt(row)).isCellEditable(column);
        }
        return false;
    }

    public void setValueAt(Object obj, int row, int column) {
        Object o = v.elementAt(row);
        if (o instanceof EditableColumnViewable) {
            ((EditableColumnViewable) o).setValueAt(obj, column);
            return;
        }
    }

}
