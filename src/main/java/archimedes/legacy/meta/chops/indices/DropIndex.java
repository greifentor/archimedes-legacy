/*
 * DropIndex.java
 *
 * 17.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.indices;

import archimedes.legacy.meta.MetaDataIndex;
import archimedes.legacy.meta.chops.ScriptSectionType;
import archimedes.legacy.meta.*;
import archimedes.legacy.meta.chops.*;


/**
 * A change operation to remove an existing index.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.12.2015 - Added.
 */

public class DropIndex extends AbstractIndexChangeOperation {

    /**
     * Creates a change operation to remove an index.
     *
     * @param index The index which is to remove.
     * @param section The type of the section which the change operation is related to.
     *
     * @changed OLI 17.12.2015 - Added.
     */
    public DropIndex(MetaDataIndex index, ScriptSectionType section) {
        super(index, section);
    }

}