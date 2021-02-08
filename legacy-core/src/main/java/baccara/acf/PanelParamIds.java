/*
 * PanelParamIds.java
 *
 * 13.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.acf;


/**
 * Special panel parameter id's.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 13.07.2016 - Added.
 */

public class PanelParamIds extends archimedes.acf.param.PanelParamIds {

    /** 
     * Marks a panel which is for a list whose elements are to remove completely before be
     * transfered from the GUI.
     */
    public static final String CLEAN_BEFORE_TRANSFER = "CLEAN_BEFORE_TRANSFER";
    /** Marks a panel which is inherited from the super class. */
    public static final String INHERITED = "INHERITED";
    /** Marks a panel for non unique selections for a list maintenance. */
    public static final String SELECTION = "SELECTION";
    /** Marks a panel for unique selections for a list maintenance. */
    public static final String UNIQUE_SELECTION = "UNIQUE_SELECTION";

}