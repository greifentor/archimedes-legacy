/*
 * DropColumn.java
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
 * A change operation to drop a column from a model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class DropColumn extends AbstractColumnChangeOperation {

    /**
     * Creates a new change operation to drop a column from a model.
     *
     * @param table The information about the table which the column to drop belongs to.
     * @param column The information about the column which is to drop.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public DropColumn(MetaDataTable table, MetaDataColumn column) {
        super(table, column, ScriptSectionType.DROP_COLUMNS);
    }

    /**
     * @changed OLI 11.12.2015 - Added.
     */
    @Override public String toString() {
        return "DropColumn(table=\"" + this.getTable().getName() + "\", column=\""
                + this.getColumn().getName() + "\")";
    }

}