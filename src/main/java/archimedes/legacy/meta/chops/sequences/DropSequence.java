/*
 * DropSequence.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.sequences;

import archimedes.legacy.meta.MetaDataSequence;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A representation of a sequence reduction.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class DropSequence extends AbstractSequenceChangeOperation {

    /**
     * Creates a new drop sequence operation.
     *
     * @param sequence The sequence which should be dropped by the operation.
     * @param section The section which the change operation should be added to.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public DropSequence(MetaDataSequence sequence) {
        super(sequence, ScriptSectionType.DROP_SEQUENCES);
    }

    /**
     * @changed OLI 11.12.2015 - Added.
     */
    @Override public String toString() {
        return "DropSequence(sequence=\"" + this.getSequence().getName() + "\")";
    }

}