/*
 * STFStereotypeHandler.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;

import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of stereotypes.
 *
 * @author ollie
 *
 * @changed OLI 26.04.2013 - Added.
 */

public class STFStereotypeHandler extends AbstractSTFHandler {

    public static final String COMMENT = "Kommentar";
    public static final String COUNT = "Anzahl";
    public static final String DO_NOT_PRINT = "DoNotPrint";
    public static final String HIDE_TABLE = "HideTable";
    public static final String HISTORY = "History";
    public static final String NAME = "Name";
    public static final String STEREOTYPE = "Stereotype";
    public static final String STEREOTYPES = "Stereotype";

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return STEREOTYPE;
    }

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return STEREOTYPES;
    }

}
