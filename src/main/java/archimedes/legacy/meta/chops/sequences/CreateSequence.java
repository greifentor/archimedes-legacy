/*
 * CreateSequence.java
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
 * A representation of a sequence addition.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class CreateSequence extends AbstractSequenceChangeOperation {

    /**
     * Creates a new create sequence operation.
     *
     * @param sequence The sequence which should be created by the operation.
     * @param section The section which the change operation should be added to.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public CreateSequence(MetaDataSequence sequence) {
        super(sequence, ScriptSectionType.CREATE_SEQUENCES);
    }

    /**
     * @changed OLI 11.12.2015 - Added.
     */
    @Override public String toString() {
        return "CreateSequence(sequence=\"" + this.getSequence().getName() + "\")";
    }

}