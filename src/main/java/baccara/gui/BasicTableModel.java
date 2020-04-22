/*
 * BasicTableModel.java
 *
 * 19.10.2017
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;

import java.util.*;

import javax.swing.event.*;
import javax.swing.table.*;

/**
 * An abstract base table model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.10.2017 - Added.
 */

abstract public class BasicTableModel implements TableModel {

    protected List<TableModelListener> listeners = new LinkedList<TableModelListener>();

    /**
     * @changed OLI 19.10.2017 - Added.
     */
    @Override public void addTableModelListener(TableModelListener l) {
        this.listeners.add(l);
    }

    protected void fireTableModelEvent(TableModelEvent e) {
        try {
            for (TableModelListener l : this.listeners) {
                try {
                    l.tableChanged(e);
                } catch (Exception e1) {
                }
            }
        } catch (Exception e0) {
        }
    }

    /**
     * @changed OLI 19.10.2017 - Added.
     */
    @Override public void removeTableModelListener(TableModelListener l) {
        this.listeners.remove(l);
    }

}