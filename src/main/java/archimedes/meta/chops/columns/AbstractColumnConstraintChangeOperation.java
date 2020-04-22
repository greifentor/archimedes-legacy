/*
 * AbstractColumnConstraintChangeOperation.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.meta.chops.columns;

import static corentx.util.Checks.*;

import archimedes.meta.*;
import archimedes.meta.chops.*;


/**
 * A base class for column constraint changes.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

abstract public class AbstractColumnConstraintChangeOperation
        extends AbstractColumnChangeOperation {

    private ColumnConstraintType constraintType = null;

    /**
     * Creates a new change operation to change a constraint to a column.
     *
     * @param table The information about the table which the column to change belongs to.
     * @param column The information about the column which is the constraint is to change.
     * @param constraintType The type of the constraint which is to change.
     * @param section The type of the section which the change operation is related to.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public AbstractColumnConstraintChangeOperation(MetaDataTable table, MetaDataColumn column,
            ColumnConstraintType constraintType, ScriptSectionType section) {
        super(table, column, section);
        ensure(constraintType != null, "constraint type cannot be null.");
        this.constraintType = constraintType;
    }

    /**
     * Returns the constraint type which is to add to the column.
     *
     * @return The constraint type which is to add to the column.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public ColumnConstraintType getConstraintType() {
        return this.constraintType;
    }

}