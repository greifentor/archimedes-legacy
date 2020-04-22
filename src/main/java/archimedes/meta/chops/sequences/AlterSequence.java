/*
 * AlterSequence.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.meta.chops.sequences;

import archimedes.meta.*;
import archimedes.meta.chops.*;


/**
 * A representation of a sequence change.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AlterSequence extends AbstractSequenceChangeOperation {

    /**
     * Creates a new alter sequence operation.
     *
     * @param sequence The sequence which should be dropped by the operation.
     * @param section The section which the change operation should be added to.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public AlterSequence(MetaDataSequence sequence) {
        super(sequence, ScriptSectionType.ALTER_COLUMNS);
    }

    /**
     * @changed OLI 11.12.2015 - Added.
     */
    @Override public String toString() {
        return "AlterSequence(sequence=\"" + this.getSequence().getName() + "\")";
    }

}