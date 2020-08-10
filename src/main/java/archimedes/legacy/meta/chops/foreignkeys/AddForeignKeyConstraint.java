/*
 * AddForeignKeyConstraint.java
 *
 * 14.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.foreignkeys;

import archimedes.legacy.meta.MetaDataColumn;
import archimedes.legacy.meta.MetaDataForeignKeyConstraint;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A representation of an add foreign key constraint change operation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.12.2015 - Added.
 */

public class AddForeignKeyConstraint extends AbstractForeignKeyConstraintChangeOperation {

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param column The column which the foreign key constraint is linked to.
     * @param foreignKeyConstraint The foreign key constraint which the change operation is to
     *         process for.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public AddForeignKeyConstraint(MetaDataColumn column,
            MetaDataForeignKeyConstraint foreignKeyConstraint) {
        super(ScriptSectionType.ADD_FOREIGN_KEYS, column, foreignKeyConstraint);
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public String toString() {
        return "AddForeignKeyConstraint(foreignKeyConstraint=\"" + this.getForeignKeyConstraint(
                ).getName() + "\", column=" + this.getColumns()[0].getName() + ")";
    }

}