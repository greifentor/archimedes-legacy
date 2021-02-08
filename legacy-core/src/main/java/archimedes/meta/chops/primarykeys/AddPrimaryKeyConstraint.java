/*
 * AddPrimaryKeyConstraint.java
 *
 * 21.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.meta.chops.primarykeys;

import archimedes.meta.*;
import archimedes.meta.chops.*;


/**
 * A representation of a primary key add constraint change operation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.12.2015 - Added.
 */

public class AddPrimaryKeyConstraint extends AbstractTableChangeOperation {

    /**
     * Creates a new change operation to add a primary key to a table.
     *
     * @param table The information about table which a primary key is to add to.
     *
     * @changed OLI 21.12.2015 - Added.
     */
    public AddPrimaryKeyConstraint(MetaDataTable table) {
        super(table, ScriptSectionType.UPDATE_CONSTRAINTS);
    }

    /**
     * @changed OLI 21.12.2015 - Added.
     */
    @Override public String toString() {
        return "AddPrimaryKeyConstraint(table=\"" + this.getTable().getName() + "\")";
    }

}