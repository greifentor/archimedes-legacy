/*
 * DropTable.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.tables;

import archimedes.legacy.meta.MetaDataTable;
import archimedes.legacy.meta.chops.AbstractTableChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A change operation to drop a table. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class DropTable extends AbstractTableChangeOperation {

    /**
     * Creates a new change operation to drop a table.
     *
     * @param table The information about the table to drop.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public DropTable(MetaDataTable table) {
        super(table, ScriptSectionType.DROP_TABLES);
    }

    /**
     * @changed OLI 11.12.2015 - Added.
     */
    @Override public String toString() {
        return "DropTable(table=\"" + this.getTable().getName() + "\")";
    }

}