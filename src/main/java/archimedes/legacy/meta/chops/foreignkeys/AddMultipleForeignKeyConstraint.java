/*
 * AddMultipleForeignKeyConstraint.java
 *
 * 28.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.foreignkeys;

import static corentx.util.Checks.*;

import java.util.*;

import archimedes.legacy.meta.MetaDataColumn;
import archimedes.legacy.meta.MetaDataComplexForeignKey;
import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;

/**
 * A representation of an add multiple foreign key constraint change operation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 28.09.2017 - Added.
 */

public class AddMultipleForeignKeyConstraint extends AbstractChangeOperation {

    private MetaDataComplexForeignKey foreignKeyConstraint = null;

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param section The type of the section which the change operation is related to.
     * @param columns The columns which the foreign key constraint is linked to.
     * @param foreignKeyConstraint The complex foreign key which the change operation is to
     *         process for.
     *
     * @changed OLI 28.09.2017 - Added.
     */
    public AddMultipleForeignKeyConstraint(MetaDataComplexForeignKey foreignKeyConstraint) {
        this(ScriptSectionType.ADD_FOREIGN_KEYS, foreignKeyConstraint);
    }

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param section The type of the section which the change operation is related to.
     * @param foreignKeyConstraint The complex foreign key which the change operation is to
     *         process for.
     *
     * @changed OLI 28.09.2017 - Added.
     */
    public AddMultipleForeignKeyConstraint(ScriptSectionType section,
            MetaDataComplexForeignKey foreignKeyConstraint) {
        super(section);
        ensure(foreignKeyConstraint != null, "foreign key constraint cannot be null.");
        this.foreignKeyConstraint = foreignKeyConstraint;
        List<MetaDataColumn> c = new LinkedList<MetaDataColumn>();
    }

    /**
     * Returns the complex foreign key which the change operation is to process for.
     *
     * @return The complex foreign key which the change operation is to process for.
     *
     * @changed OLI 28.09.2017 - Added.
     */
    public MetaDataComplexForeignKey getComplexForeignKey() {
        return this.foreignKeyConstraint;
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
        for (MetaDataColumn mdc : this.foreignKeyConstraint.getSourceColumns()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(mdc.getFullName());
        }
        return sb.toString();
    }

    /**
     * @changed OLI 29.09.2017 - Added.
     */
    @Override public String toString() {
        return "AddMultipleForeignKeyConstraint(foreignKeyConstraint=\""
                + this.getComplexForeignKey().getName() + "\", columns="
                + this.getColumnsFullNames() + ")";
    }

}