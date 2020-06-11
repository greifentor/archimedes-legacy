/*
 * SelectableVectorTableModel.java
 *
 * 10.06.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


public class SelectableVectorTableModel extends VectorTableModel {
    
    protected Vector allelements = null;
    
    public SelectableVectorTableModel(Vector v) {
        super(v);
    }
    
    public SelectableVectorTableModel(Vector v, String[] columnnames) {
        this(v);
        this.columnname = columnnames;
    }
    
    public Class getColumnClass(int column) {
        if ((v.size() > 0) && (v.elementAt(0) instanceof SelectableVectorElement)) {
            Object o = ((SelectableVectorElement) v.elementAt(0)).getObject();
            if (o instanceof ColumnViewable) {
                if (column == ((ColumnViewable) o).getColumnCount()) {
                    return Boolean.TRUE.getClass();
                }
                return ((ColumnViewable) o).getColumnclass(column);
            }
        }
        return new String().getClass();
    }


    public int getColumnCount() {
        if ((v.size() > 0) && (v.elementAt(0) instanceof SelectableVectorElement)) {
            Object o = ((SelectableVectorElement) v.elementAt(0)).getObject();
            if (o instanceof ColumnViewable) {
                return ((ColumnViewable) o).getColumnCount()+1;
            }
        }
        if (this.columnname != null) {
            return this.columnname.length;
        }
        return 1;
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if ((columnIndex+1 == this.getColumnCount()) 
                && (this.getColumnClass(columnIndex) == Boolean.TRUE.getClass())) {
            return true;
        }
        return false;
    }

    public Object getValueAt(int row, int column) {
        Object o = ((SelectableVectorElement) v.elementAt(row)).getObject();
        if (o instanceof ColumnViewable) {
            if (column < ((ColumnViewable) o).getColumnCount()) {
                return ((ColumnViewable) o).getValueAt(column);
            }
            return ((SelectableVectorElement) v.elementAt(row)).isSelected();
        }
        return v.elementAt(row).toString();
    }
    
    public void setValueAt(Object o, int row, int column) {
        if ((column+1 == this.getColumnCount()) 
                && (this.getColumnClass(column) == Boolean.TRUE.getClass())) {
            ((SelectableVectorElement) v.elementAt(row)).setSelected(((Boolean) o).booleanValue(
                    ));
        }
    }
    
}
