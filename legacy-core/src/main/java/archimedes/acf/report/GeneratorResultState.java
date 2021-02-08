/*
 * GeneratorResultState.java
 *
 * 19.05.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.report;


/**
 * States for generator results.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.05.2015 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public enum GeneratorResultState {

    /** A state for generation failure. */
    FAILURE,
    /**
     * A state for unnecessary class code. Code with this state will be shown on the list with
     * the files to delete.
     */
    NOT_NECESSARY,
    /** A state for the successful code generation. */
    SUCCESS;

}