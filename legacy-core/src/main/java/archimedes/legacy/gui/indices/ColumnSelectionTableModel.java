/*
 * ColumnSelectionTableModel.java
 *
 * 15.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;


import gengen.metadata.*;

import java.util.*;

import javax.swing.event.*;
import javax.swing.table.*;

import corentx.util.*;


/**
 * Eine Implementierung des TableModels zur Anzeige unf Selektion von Tabellenspalten f&uuml;r
 * komplexe Indices.
 *
 * @author ollie
 *
 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
 */

public class ColumnSelectionTableModel implements TableModel {

    private ClassMetaData table = null;
    private Vector<AttributeMetaData> indexMembers = new Vector<AttributeMetaData>();

    /**
     * Erzeugt ein neues TableModel anhand der &uuml;bergebenen Parameter.
     *
     * @param table Die Tabelle zu der die Spalten angezeigt werden sollen.
     *
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    public ColumnSelectionTableModel(ClassMetaData table) {
        super();
        this.table = table;
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public void addTableModelListener(TableModelListener l) {
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 1) {
            return Boolean.class;
        }
        return String.class;
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public int getColumnCount() {
        return 2;
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public String getColumnName(int columnIndex) {
        if (columnIndex == 1) {
            return Str.fromHTML("ber&uuml;cksichtigen");
        }
        return "Tabellenspalte";
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public int getRowCount() {
        return this.table.getAttributes().size();
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        AttributeMetaData column = this.table.getAttribute(rowIndex);
        if (columnIndex == 0) {
            return column;
        }
        return (indexMembers.contains(column));
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex == 1);
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public void removeTableModelListener(TableModelListener arg0) {
    }

    /**
     * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        AttributeMetaData column = this.table.getAttribute(rowIndex);
        if (columnIndex == 1) {
            if (((Boolean) value).booleanValue()) {
                this.indexMembers.add(column);
            } else {
                this.indexMembers.remove(column);
            }
        }
    }

}