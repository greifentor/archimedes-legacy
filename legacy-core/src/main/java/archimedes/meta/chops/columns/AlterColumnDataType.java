/*
 * AlterColumnDataType.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.meta.chops.columns;

import archimedes.meta.*;
import archimedes.meta.chops.*;


/**
 * A representation of a data type change for a column.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AlterColumnDataType extends AbstractColumnChangeOperation {

    /**
     * Creates a new change operation for the column data type.
     *
     * @param table The information about the table which the column belongs to.
     * @param column The information about the column whose data type is to change.
     * @param section The type of the section which the change operation is related to.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public AlterColumnDataType(MetaDataTable table, MetaDataColumn column,
            ScriptSectionType section) {
        super(table, column, section);
    }

    /**
     * @changed OLI 11.12.2015 - Added.
     */
    @Override public String toString() {
        return "AlterColumnDataType(table=\"" + this.getTable().getName() + "\", column=\""
                + this.getColumn().getName() + "\")";
    }

}