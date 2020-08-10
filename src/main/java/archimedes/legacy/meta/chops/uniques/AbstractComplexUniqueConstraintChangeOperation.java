/*
 * AbstractComplexUniqueConstraint.java
 *
 * 17.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.uniques;

import static corentx.util.Checks.*;

import archimedes.legacy.meta.MetaDataUniqueConstraint;
import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A basic change operation for complex unique constraints.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.12.2015 - Added.
 */

public class AbstractComplexUniqueConstraintChangeOperation extends AbstractChangeOperation {

    private MetaDataUniqueConstraint uniqueConstraint = null;

    /**
     * Creates a new basic change operation for complex unique change operation.
     *
     * @param uniqueConstraint The unique constraint which is related to the change operation.
     * @param section The type of the section which the change operation is related to.
     *
     * @changed OLI 17.12.2015 - Added.
     */
    public AbstractComplexUniqueConstraintChangeOperation(
            MetaDataUniqueConstraint uniqueConstraint, ScriptSectionType section) {
        super(section);
        ensure(uniqueConstraint != null, "unique constraint cannot be null.");
        this.uniqueConstraint = uniqueConstraint;
    }

    /**
     * Returns the unique constraint which the change operation is related to.
     *
     * @return The unique constraint which the change operation is related to.
     *
     * @changed OLI 17.12.2015 - Added.
     */
    public MetaDataUniqueConstraint getUniqueConstraint() {
        return this.uniqueConstraint;
    }

}