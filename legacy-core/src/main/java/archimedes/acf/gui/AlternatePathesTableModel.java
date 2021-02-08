/*
 * AlternatePathesTableModel.java
 *
 * 12.07.2014
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.gui;


import baccara.gui.*;

import java.util.*;

import javax.swing.event.*;
import javax.swing.table.*;


/**
 * A table model for individual preferences alternate pathes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 12.07.2014 - Added.
 */

public class AlternatePathesTableModel implements TableModel {

    private static final String[] COLUMN_NAME_IDS = new String[] {
        "individual.preferences.alternate.pathes.table.token.label",
        "individual.preferences.alternate.pathes.table.alternate.path.label"};

    private GUIBundle guiBundle = null;
    private List<AlternatePathContainer> alternatePathes = null;

    /**
     * The individual preferences whose alternate pathes are represented by the model.
     *
     * @param guiBundle A bundle to access GUI data.
     * @param alternatePathes The alternate pathes which the table model is created
     *         for.
     *
     * @changed OLI 12.07.2014 - Added.
     */
    public AlternatePathesTableModel(GUIBundle guiBundle,
            List<AlternatePathContainer> alternatePathes) {
        super();
        this.guiBundle = guiBundle;
        this.alternatePathes = alternatePathes;
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public void addTableModelListener(TableModelListener l) {
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public int getColumnCount() {
        return COLUMN_NAME_IDS.length;
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public String getColumnName(int columnIndex) {
        return this.guiBundle.getResourceText(COLUMN_NAME_IDS[columnIndex]);
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public int getRowCount() {
        return this.alternatePathes.size()+1;
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        if (rowIndex == this.getRowCount()-1) {
            return "";
        }
        if (columnIndex == 0) {
            return this.alternatePathes.get(rowIndex).getToken();
        }
        return this.alternatePathes.get(rowIndex).getAlternatePath();
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) {
        return (columnIndex != 0) || (rowIndex == this.getRowCount()-1);
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public void removeTableModelListener(TableModelListener l) {
    }

    /**
     * @changed OLI 12.07.2014 - Added.
     */
    @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
        if (rowIndex == this.getRowCount()-1) {
            if ((columnIndex == 0) && !aValue.toString().isEmpty()) {
                this.alternatePathes.add(new AlternatePathContainer(aValue.toString(), ""));
            }
            return;
        }
        if (aValue.toString().isEmpty()) {
            this.alternatePathes.remove(rowIndex);
        }
        this.alternatePathes.get(rowIndex).setAlternatePath(aValue.toString());
    }

}