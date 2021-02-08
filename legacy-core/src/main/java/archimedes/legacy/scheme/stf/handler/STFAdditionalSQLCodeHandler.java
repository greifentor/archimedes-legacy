/*
 * STFAdditionalSQLCodeHandler.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;

import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of additional SQL code.
 *
 * @author ollie
 *
 * @changed OLI 26.04.2013 - Added.
 */

public class STFAdditionalSQLCodeHandler extends AbstractSTFHandler {

    public static final String ADDITIONAL_SQL_CODE = "AdditionalSQLCode";
    public static final String ADDITIONAL_SQL_CODE_POST_CHANGING =
            "AdditionalSQLCodePostChanging";
    public static final String ADDITIONAL_SQL_CODE_POST_REDUCING =
            "AdditionalSQLCodePostReducing";
    public static final String ADDITIONAL_SQL_CODE_PRE_CHANGING =
            "AdditionalSQLCodePreChanging";
    public static final String ADDITIONAL_SQL_CODE_PRE_EXTENDING =
            "AdditionalSQLCodePreExtending";
    public static final String SQL_CODE = "SQLCode";

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return SQL_CODE;
    }

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return ADDITIONAL_SQL_CODE;
    }

}