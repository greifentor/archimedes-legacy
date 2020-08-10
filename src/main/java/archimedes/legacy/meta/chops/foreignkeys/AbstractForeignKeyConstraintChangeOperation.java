/*
 * AbstractForeignKeyConstraintChangeOperation.java
 *
 * 14.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.foreignkeys;


import static corentx.util.Checks.*;

import java.util.*;

import archimedes.legacy.meta.MetaDataColumn;
import archimedes.legacy.meta.MetaDataForeignKeyConstraint;
import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A base class for a representation of a foreign key change operation. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.12.2015 - Added.
 */

public class AbstractForeignKeyConstraintChangeOperation extends AbstractChangeOperation {

    private MetaDataForeignKeyConstraint foreignKeyConstraint = null;
    private MetaDataColumn[] columns = null;

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param section The type of the section which the change operation is related to.
     * @param column The column which the foreign key constraint is linked to.
     * @param foreignKeyConstraint The foreign key constraint which the change operation is to
     *         process for.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public AbstractForeignKeyConstraintChangeOperation(ScriptSectionType section,
            MetaDataColumn column, MetaDataForeignKeyConstraint foreignKeyConstraint) {
        super(section);
        ensure(foreignKeyConstraint != null, "foreign key constraint cannot be null.");
        ensure(column != null, "column cannot be null.");
        this.foreignKeyConstraint = foreignKeyConstraint;
        this.columns = new MetaDataColumn[] {column};
    }

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param section The type of the section which the change operation is related to.
     * @param columns The columns which the foreign key constraint is linked to.
     * @param foreignKeyConstraint The foreign key constraint which the change operation is to
     *         process for.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public AbstractForeignKeyConstraintChangeOperation(ScriptSectionType section,
            MetaDataColumn[] columns, MetaDataForeignKeyConstraint foreignKeyConstraint) {
        super(section);
        ensure(foreignKeyConstraint != null, "foreign key constraint cannot be null.");
        ensure(columns != null, "columns cannot be null.");
        this.foreignKeyConstraint = foreignKeyConstraint;
        List<MetaDataColumn> c = new LinkedList<MetaDataColumn>();
        for (MetaDataColumn mdc : columns) {
            if (mdc != null) {
                c.add(mdc);
            }
        }
        this.columns = c.toArray(new MetaDataColumn[0]);
    }

    /**
     * Returns the foreign key constraint which the change operation is to process for.
     *
     * @return The foreign key constraint which the change operation is to process for.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public MetaDataForeignKeyConstraint getForeignKeyConstraint() {
        return this.foreignKeyConstraint;
    }

    /**
     * Returns the columns which the foreign key constraint is linked to.
     *
     * @return The columns which the foreign key constraint is linked to.
     *
     * @changed OLI 15.12.2015 - Added.
     */
    public MetaDataColumn[] getColumns() {
        return this.columns;
    }

    /**
     * Returns a string with the comma separated full names of the columns.
     *
     * @return 
     * 
     * @changed OLI 26.09.2017 - Added.
     */
    public String getColumnsFullNames() {
        StringBuffer sb = new StringBuffer();
        for (MetaDataColumn mdc : this.getColumns()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(mdc.getFullName());
        }
        return sb.toString();
    }

}