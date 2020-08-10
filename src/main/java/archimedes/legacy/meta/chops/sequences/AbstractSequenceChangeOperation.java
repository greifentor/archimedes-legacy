/*
 * AbstractSequenceChangeOperation.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.sequences;

import static corentx.util.Checks.*;

import archimedes.legacy.meta.MetaDataSequence;
import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A base class for sequence change operations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AbstractSequenceChangeOperation extends AbstractChangeOperation {

    private MetaDataSequence sequence = null;

    /**
     * Creates a new base change operation for sequences.
     *
     * @param sequence The sequence which should be changed by the operation.
     * @param section The section which the change operation should be added to.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public AbstractSequenceChangeOperation(MetaDataSequence sequence, ScriptSectionType section)
            {
        super(section);
        ensure(sequence != null, "sequence cannot be null.");
        this.sequence = sequence;
    }

    /**
     * Returns the sequence of the change operation.
     *
     * @return The sequence of the change operation.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public MetaDataSequence getSequence() {
        return this.sequence;
    }

}