/*
 * AddComplexUniqueConstraint.java
 *
 * 17.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.meta.chops.uniques;

import archimedes.meta.*;
import archimedes.meta.chops.*;


/**
 * A representation of an add complex unique constraint operation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.12.2015 - Added.
 */

public class AddComplexUniqueConstraint extends AbstractComplexUniqueConstraintChangeOperation {

    /**
     * Creates a new add complex constraint change operation with the passed parameters.
     *
     * @param uniqueConstraint The unique constraint which is related to the change operation.
     *
     * @changed OLI 17.12.2015 - Added.
     */
    public AddComplexUniqueConstraint(MetaDataUniqueConstraint uniqueConstraint) {
        this(uniqueConstraint, ScriptSectionType.UPDATE_CONSTRAINTS);
    }

    /**
     * Creates a new add complex constraint change operation with the passed parameters.
     *
     * @param uniqueConstraint The unique constraint which is related to the change operation.
     * @param section The type of the section which the change operation is related to.
     *
     * @changed OLI 17.12.2015 - Added.
     */
    public AddComplexUniqueConstraint(MetaDataUniqueConstraint uniqueConstraint,
            ScriptSectionType section) {
        super(uniqueConstraint, section);
    }

    /**
     * @changed OLI 21.12.2015 - Added.
     */
    @Override public String toString() {
        MetaDataUniqueConstraint uc = this.getUniqueConstraint();
        return "AddComplexUniqueConstraint(table=" + this.quote(uc.getTable()) + ", columns={"
                + this.quote(uc.getColumns()) + "})";
    }

}