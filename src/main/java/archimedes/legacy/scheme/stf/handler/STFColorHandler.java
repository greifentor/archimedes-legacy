/*
 * STFColorHandler.java
 *
 * 29.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;

import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of colors.
 *
 * @author ollie
 *
 * @changed OLI 29.04.2013 - Added.
 */

public class STFColorHandler extends AbstractSTFHandler {

    public static final String B = "B";
    public static final String COLOR = "Color";
    public static final String COLORS = "Colors";
    public static final String COUNT = "Anzahl";
    public static final String G = "G";
    public static final String NAME = "Name";
    public static final String R = "R";

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return COLOR;
    }

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return COLORS;
    }

}