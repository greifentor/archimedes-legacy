/*
 * ListTableModel.java
 *
 * 28.10.2004
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.gui;


import java.util.*;
import javax.swing.table.*;


public class ListTableModel extends AbstractTableModel {
    
    protected String[] columnname = new String[] {"Tabellenspalte"};
    protected List<?> v = null;
    
    public ListTableModel(List<?> v) {
        super();
        this.v = v;
    }
    
    public ListTableModel(List<?> v, String[] columnnames) {
        this(v);
        this.columnname = columnnames;
    }
    
    public Class<?> getColumnClass(int column) {
        return new String().getClass();
    }

    public String getColumnName(int column) {
        if ((columnname != null) && (column >= 0) && (column < columnname.length)
                && (columnname[column] != null)) {
            return (String) columnname[column];
        }
        return "";
    }

    public int getRowCount() {
        return v.size();
    }

    public int getColumnCount() {
        if (columnname != null) {
            return columnname.length;
        }
        return 1;
    }

    public Object getValueAt(int row, int column) {
        return v.get(row).toString();
    }

}
