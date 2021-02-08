/*
 * ConnectionsTableModel.java
 *
 * 20.01.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.connections;


import archimedes.legacy.model.*;

import baccara.gui.*;

import javax.swing.table.*;


/**
 * A table model for the database connections.
 *
 * @author ollie
 *
 * @changed OLI 20.01.2015 - Added.
 */

public class ConnectionsTableModel extends DefaultTableModel {

    private DiagrammModel diagram = null;
    private GUIBundle guiBundle = null;

    /**
     * Creates a new connection table model with the passed parameters.
     *
     * @param diagram The diagram model whose connections are represented by the model.
     * @param guiBundle The GUI bundle for resource access.
     *
     * @changed OLI 20.01.2015 - Added.
     */
    public ConnectionsTableModel(DiagrammModel diagram, GUIBundle guiBundle) {
        super();
        this.diagram = diagram;
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 20.01.2015 - Added.
     */
    @Override public Class<?> getColumnClass(int columnIndex) {
        return String.class;
    }

    /**
     * @changed OLI 20.01.2015 - Added.
     */
    @Override public int getColumnCount() {
        return 1;
    }

    /**
     * @changed OLI 20.01.2015 - Added.
     */
    @Override public String getColumnName(int columnIndex) {
        return this.guiBundle.getResourceText(
                "archimedes.ConnectionsMainDialog.table.column." + columnIndex + ".title");
    }

    /**
     * @changed OLI 20.01.2015 - Added.
     */
    @Override public int getRowCount() {
        if (this.diagram == null) {
            return 0;
        }
        return this.diagram.getDatabaseConnections().length;
    }

    /**
     * @changed OLI 20.01.2015 - Added.
     */
    @Override public Object getValueAt(int rowIndex, int columnIndex) {
        return this.diagram.getDatabaseConnections()[rowIndex].getName();
    }

    /**
     * @changed OLI 20.01.2015 - Added.
     */
    @Override public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    /**
     * @changed OLI 20.01.2015 - Added.
     */
    @Override public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
    }

}