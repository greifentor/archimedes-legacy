/*
 * DropMultipleForeignKeyConstraint.java
 *
 * 29.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.foreignkeys;

import static corentx.util.Checks.*;

import archimedes.legacy.meta.MetaDataColumn;
import archimedes.legacy.meta.MetaDataComplexForeignKey;
import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;

/**
 * A representation of a drop multiple foreign key constraint change operation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.09.2017 - Added.
 */

public class DropMultipleForeignKeyConstraint extends AbstractChangeOperation {

    private MetaDataComplexForeignKey mdcfk = null;

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param mdcfk The foreign key constraint which the change operation is to
     *         process for.
     *
     * @changed OLI 29.09.2017 - Added.
     */
    public DropMultipleForeignKeyConstraint(MetaDataComplexForeignKey mdcfk) {
        this(ScriptSectionType.DROP_FOREIGNKEYS, mdcfk);
    }

    /**
     * Creates a new change operation with the passed parameters.
     *
     * @param section The type of the section which the change operation is related to.
     * @param mdcfk The complex foreign key constraint which the change operation is to
     *         process for.
     *
     * @changed OLI 29.09.2017 - Added.
     */
    public DropMultipleForeignKeyConstraint(ScriptSectionType section,
            MetaDataComplexForeignKey mdcfk) {
        super(section);
        ensure(mdcfk != null, "complex foreign key cannot be null.");
        this.mdcfk = mdcfk;
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
        for (MetaDataColumn mdc : this.mdcfk.getSourceColumns()) {
            if (sb.length() > 0) {
                sb.append(",");
            }
            sb.append(mdc.getFullName());
        }
        return sb.toString();
    }

    /**
     * Returns the complex foreign key which is to process by the change operation.
     *
     * @return The complex foreign key which is to process by the change operation.
     *
     * @changed OLI 29.09.2017 - Added.
     */
    public MetaDataComplexForeignKey getComplexForeignKey() {
        return this.mdcfk;
    }

    /**
     * @changed OLI 29.09.2017 - Added.
     */
    @Override public String toString() {
        return "DropMultipleForeignKeyConstraint(foreignKeyConstraint=\""
                + this.getComplexForeignKey().getName() + "\", columns="
                + this.getColumnsFullNames() + ")";
    }

}