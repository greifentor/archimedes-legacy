/*
 * ModelCheckerMessageTableModel.java
 *
 * 19.05.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.gui.checker;

import static corentx.util.Checks.*;

import baccara.gui.*;
import archimedes.acf.checker.*;

import javax.swing.table.*;


/**
 * A specification of the <CODE>DefaultTableModel</CODE> for model checker messages.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.05.2016 - Added.
 */

public class ModelCheckerMessageTableModel extends DefaultTableModel {

    private static final String RES_MODEL_CHECKER_MESSAGE_TABLE_HEADLINE_CLICKABLE_TITLE
            = "ModelChecker.table.headline.clickable.title";
    private static final String RES_MODEL_CHECKER_MESSAGE_TABLE_HEADLINE_LEVEL_TITLE
            = "ModelChecker.table.headline.level.title";
    private static final String RES_MODEL_CHECKER_MESSAGE_TABLE_HEADLINE_MESSAGE_TITLE
            = "ModelChecker.table.headline.message.title";

    private GUIBundle guiBundle = null;
    private ModelCheckerMessage[] mcms = null;

    /**
     * Creates a new table model for model checker messages with passed parameters.
     *
     * @param mcms The list of model checker messages to show.
     * @param guiBundle A bundle of GUI information.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public ModelCheckerMessageTableModel(ModelCheckerMessage[] mcms, GUIBundle guiBundle) {
        super();
        ensure(guiBundle != null, "GUI bundle cannot be null.");
        ensure(mcms != null, "model checker message list cannot be null.");
        this.guiBundle = guiBundle;
        this.mcms = mcms;
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 2) {
            return Boolean.class;
        }
        return String.class;
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public int getColumnCount() {
        return 3;
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public String getColumnName(int column) {
        switch (column) {
        case 0:
            return this.guiBundle.getResourceText(
                    RES_MODEL_CHECKER_MESSAGE_TABLE_HEADLINE_LEVEL_TITLE);
        case 1:
            return this.guiBundle.getResourceText(
                    RES_MODEL_CHECKER_MESSAGE_TABLE_HEADLINE_MESSAGE_TITLE);
        case 2:
            return this.guiBundle.getResourceText(
                    RES_MODEL_CHECKER_MESSAGE_TABLE_HEADLINE_CLICKABLE_TITLE);
        }
        return "-";
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public int getRowCount() {
        if (this.mcms == null) {
            return 0;
        }
        return this.mcms.length;
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public Object getValueAt(int row, int column) {
        ModelCheckerMessage mcm = mcms[row];
        switch (column) {
        case 0:
            return mcm.getLevel();
        case 1:
            return mcm.getMessage();
        case 2:
            return (mcm.getObject() != null);
        }
        return "-";
    }

    /**
     * @changed OLI 19.05.2016 - Added.
     */
    @Override public boolean isCellEditable(int row, int column) {
        return false;
    }

}