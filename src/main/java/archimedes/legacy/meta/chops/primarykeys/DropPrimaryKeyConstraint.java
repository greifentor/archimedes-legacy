/*
 * DropPrimaryKeyConstraint.java
 *
 * 21.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.primarykeys;

import archimedes.legacy.meta.MetaDataTable;
import archimedes.legacy.meta.chops.AbstractTableChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A representation of a primary key drop constraint change operation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.12.2015 - Added.
 */

public class DropPrimaryKeyConstraint extends AbstractTableChangeOperation {

    /**
     * Creates a new change operation to drop the primary key of a table.
     *
     * @param table The information about table whose the primary key is to drop.
     *
     * @changed OLI 21.12.2015 - Added.
     */
    public DropPrimaryKeyConstraint(MetaDataTable table) {
        super(table, ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS);
    }

    /**
     * @changed OLI 21.12.2015 - Added.
     */
    @Override public String toString() {
        return "DropPrimaryKeyConstraint(table=\"" + this.getTable().getName() + "\")";
    }

}