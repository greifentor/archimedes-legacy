/*
 * ColParamIds.java
 *
 * 01.08.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf;


/**
 * Some special column param id's for Baccara applications.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.08.2016 - Added.
 */

public class ColParamIds extends archimedes.acf.param.ColParamIds {

    /** Marks a field which should provide an action button. */
    public static final String ACTION_BUTTON = "ACTION_BUTTON";
    /**
     * Marks a field which is acting as a assigned element in a many-to-many relation which is
     * made for a single table.
     */
    public static final String ASSIGNED_ELEMENT = "ASSIGNED_ELEMENT";

}