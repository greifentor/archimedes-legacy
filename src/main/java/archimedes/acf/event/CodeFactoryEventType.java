/*
 * CodeFactoryEventType.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.event;


/**
 * A enumeration of identifiers which describe types of events fired by the
 * <CODE>CodeFactory</CODE> implementation.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 */

public enum CodeFactoryEventType {

    /** An id to notify a complete preparation of the CodeFactory. */
    PREPARATION_COMPLETE,
    /** An id to notify a complete preparation of the CodeFactory. */
    GENERATION_FINISHED;

}