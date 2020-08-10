/*
 * SQLScriptGenerator.java
 *
 * 14.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.sql.generator;

import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.*;


/**
 * An interface which describes the methods of a SQL script writer.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.12.2015 - Added.
 */

public interface SQLScriptGenerator {

    /**
     * Creates a SQL script based on the passed list of change operations.
     *
     * @param changes The list of change operations which are to transfer to a SQL script.
     * @return A SQL script for the passed list of change operations (in a String).
     * @throws IllegalArgumentException Passing a null pointer as changes to process.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    abstract public String generate(AbstractChangeOperation[] changes)
            throws IllegalArgumentException;

}