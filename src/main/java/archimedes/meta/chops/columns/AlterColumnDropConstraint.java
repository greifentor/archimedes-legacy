/*
 * AlterColumnDropConstraint.java
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
 * A representation of a constraint reduction.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AlterColumnDropConstraint extends AbstractColumnConstraintChangeOperation {

    /**
     * Creates a new change operation to drop a constraint from a column.
     *
     * @param table The information about the table which the column to drop belongs to.
     * @param column The information about the column which is the constraint is to drop from.
     * @param constraintType The type of the constraint which is to drop from the column.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public AlterColumnDropConstraint(MetaDataTable table, MetaDataColumn column,
            ColumnConstraintType constraintType) {
        this(table, column, constraintType, ScriptSectionType.UPDATE_CONSTRAINTS);
    }

    /**
     * Creates a new change operation to drop a constraint from a column.
     *
     * @param table The information about the table which the column to drop belongs to.
     * @param column The information about the column which is the constraint is to drop from.
     * @param constraintType The type of the constraint which is to drop from the column.
     * @param section The type of the section which the change operation is related to.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public AlterColumnDropConstraint(MetaDataTable table, MetaDataColumn column,
            ColumnConstraintType constraintType, ScriptSectionType section) {
        super(table, column, constraintType, section);
    }

    /**
     * @changed OLI 11.12.2015 - Added.
     */
    @Override public String toString() {
        return "AlterColumnDropConstraint(table=\"" + this.getTable().getName() + "\", column=\""
                + this.getColumn().getName() + "\", constraintType=" + this.getConstraintType()
                + ")";
    }

}