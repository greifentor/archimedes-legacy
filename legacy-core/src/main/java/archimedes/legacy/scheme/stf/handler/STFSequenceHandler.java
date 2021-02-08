/*
 * STFSequenceHandler.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;

import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of sequences.
 *
 * @author ollie
 *
 * @changed OLI 26.04.2013 - Added.
 */

public class STFSequenceHandler extends AbstractSTFHandler {

    public static final String COMMENT = "Comment";
    public static final String COUNT = "Count";
    public static final String HISTORY = "History";
    public static final String INCREMENT = "Increment";
    public static final String NAME = "Name";
    public static final String SEQUENCE = "Sequence";
    public static final String SEQUENCES = "Sequences";
    public static final String START_VALUE = "StartValue";

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return SEQUENCE;
    }

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return SEQUENCES;
    }

}