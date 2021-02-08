/*
 * ArtifactGeneratorListTableModel.java
 *
 * 16.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.gui;


import archimedes.acf.*;

import baccara.gui.*;

import java.util.*;

import javax.swing.table.*;


/**
 * A table model for an array of code generators.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 16.10.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class CodeGeneratorListTableModel extends AbstractTableModel {

    private static final String[] COLUMN_HEADER_RESOURCE_NAMES = new String[] {
            "code.generators.configuration.table.column.name",
            "code.generators.configuration.table.column.active"};
    private CodeGenerator[] codeGenerators = null;
    private GUIBundle guiBundle = null;

    /**
     * Creates a new table model for the passed code generator array.
     *
     * @param codeGenerators The code generator arrays which is represented by the table model.
     * @param guiBundle A bundle with GUI information.
     *
     * @changed OLI 16.10.2013 - Added.
     */
    public CodeGeneratorListTableModel(CodeGenerator[] codeGenerators, GUIBundle guiBundle)
            {
        super();
        this.codeGenerators = codeGenerators;
        this.guiBundle = guiBundle;
        Arrays.sort(codeGenerators, new CodeGeneratorComparator(guiBundle));
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public Class<?> getColumnClass(int column) {
        if (column == 1) {
            return Boolean.class;
        }
        return String.class;
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public int getColumnCount() {
        return COLUMN_HEADER_RESOURCE_NAMES.length;
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public String getColumnName(int column) {
        return this.guiBundle.getResourceText(COLUMN_HEADER_RESOURCE_NAMES[column]);
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public int getRowCount() {
        return this.codeGenerators.length;
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public Object getValueAt(int row, int column) {
        if (column == 1) {
            if (this.codeGenerators[row].isDeprecated()) {
                return true;
            }
            return !this.codeGenerators[row].isTemporarilySuspended();
        }
        return this.guiBundle.getResourceText("code.generators.configuration.name."
                + this.codeGenerators[row].getClass().getSimpleName() + ".title");
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public boolean isCellEditable(int row, int column) {
        return (column == 1) && !this.codeGenerators[row].isDeprecated();
    }

    /**
     * @changed OLI 16.10.2013 - Added.
     */
    @Override public void setValueAt(Object o, int row, int column) {
        if (column == 1) {
            this.codeGenerators[row].setTemporarilySuspended(!((Boolean) o).booleanValue());
        }
    }

}


class CodeGeneratorComparator implements Comparator<CodeGenerator> {

    private GUIBundle guiBundle = null;

    CodeGeneratorComparator(GUIBundle guiBundle) {
        super();
        this.guiBundle = guiBundle;
    }

    /**
     * @changed OLI 01.11.2013 - Added.
     */
    @Override public int compare(CodeGenerator cg0, CodeGenerator cg1) {
        String cgn0 = this.guiBundle.getResourceText("code.generators.configuration.name."
                + cg0.getClass().getSimpleName() + ".title");
        String cgn1 = this.guiBundle.getResourceText("code.generators.configuration.name."
                + cg1.getClass().getSimpleName() + ".title");
        return cgn0.compareToIgnoreCase(cgn1);
    }
}