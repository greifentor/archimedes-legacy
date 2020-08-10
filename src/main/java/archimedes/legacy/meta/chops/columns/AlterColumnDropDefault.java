/*
 * AlterColumnDropDefault.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.columns;

import archimedes.legacy.meta.MetaDataColumn;
import archimedes.legacy.meta.MetaDataTable;
import archimedes.legacy.meta.chops.AbstractColumnChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A representation of a default value drop for a column.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AlterColumnDropDefault extends AbstractColumnChangeOperation {

    /**
     * Creates a new change operation to drop the column default value.
     *
     * @param table The information about the table which the column belongs to.
     * @param column The information about the column whose default value is to drop.
     * @param section The type of the section which the change operation is related to.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public AlterColumnDropDefault(MetaDataTable table, MetaDataColumn column,
            ScriptSectionType section) {
        super(table, column, section);
    }

    /**
     * @changed OLI 11.12.2015 - Added.
     */
    @Override public String toString() {
        return "AlterColumnDropDefault(table=\"" + this.getTable().getName()
                + "\", column=\"" + this.getColumn().getName() + "\")";
    }

}