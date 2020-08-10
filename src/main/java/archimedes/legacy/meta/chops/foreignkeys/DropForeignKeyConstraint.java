/*
 * DropForeignKeyConstraint.java
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
 * A representation of a drop foreign key constraint change operation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.12.2015 - Added.
 */

public class DropForeignKeyConstraint extends AbstractForeignKeyConstraintChangeOperation {

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param column The column which the foreign key constraint is linked to.
     * @param foreignKeyConstraint The foreign key constraint which the change operation is to
     *         process for.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public DropForeignKeyConstraint(MetaDataColumn column,
            MetaDataForeignKeyConstraint foreignKeyConstraint) {
        this(ScriptSectionType.DROP_FOREIGNKEYS, column, foreignKeyConstraint);
    }

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param section The type of the section which the change operation is related to.
     * @param table The table which the foreign key constraint is linked to.
     * @param foreignKeyConstraint The foreign key constraint which the change operation is to
     *         process for.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public DropForeignKeyConstraint(ScriptSectionType section, MetaDataColumn column,
            MetaDataForeignKeyConstraint foreignKeyConstraint) {
        super(section, column, foreignKeyConstraint);
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public String toString() {
        return "DropForeignKeyConstraint(foreignKeyConstraint=\""
                + this.getForeignKeyConstraint().getName() + "\", columns="
                + this.getColumnsFullNames() + ")";
    }

}