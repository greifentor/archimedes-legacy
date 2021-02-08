/*
 * TableMetaData.java
 *
 * 05.04.2004
 *
 * (c) by ollie
 *
 */
  
package archimedes.legacy.model;


import java.util.*;

import corent.base.*;

/**
 * Diese Klasse dient der Aufnahme von Tabellen-Metadaten.<BR>
 * <HR>
 *
 * @author ollie
 *
 */
 
public class TableMetaData implements Comparable {
    
    public String name = null;
    private Hashtable columns = null;
    
    public TableMetaData(String name) {
        super();
        this.columns = new Hashtable();
        this.name = name;
    }
    
    public int compareTo(Object o) {
        TableMetaData tmd = (TableMetaData) o;
        return this.name.compareTo(tmd.name);
    }
    
    public String toString() {
        ColumnMetaData cmd = null;
        int len = 0;
        SortedVector svc = new SortedVector();
        String s = new String(this.name.toUpperCase());
        for (Enumeration e = columns.elements(); e.hasMoreElements(); ) {
            cmd = (ColumnMetaData) e.nextElement();
            svc.addElement(cmd);
        }
        len = svc.size();
        for (int i = 0; i < len; i++) {
            cmd = (ColumnMetaData) svc.elementAt(i);
            s = s.concat("\n    " + cmd.toString());
        }
        return s;
    }
    
    public void addColumn(ColumnMetaData cmd) {
        this.columns.put(cmd.name, cmd);
    }
    
    public ColumnMetaData getColumn(String name) {
        for (Enumeration e = this.columns.elements(); e.hasMoreElements(); ) {
            ColumnMetaData cmd = (ColumnMetaData) e.nextElement();
            if (name.equalsIgnoreCase(cmd.name)) {
                return cmd;
            }
        }
        return null;
    }
    
    public Hashtable getColumns() {
        return this.columns;
    }

    public boolean hasPrimaryKey() {
        for (Enumeration e = this.columns.elements(); e.hasMoreElements(); ) {
            ColumnMetaData cmd = (ColumnMetaData) e.nextElement();
            if (cmd.primaryKey) {
                return true;
            }
        }
        return false;
    }

}