/*
 * TableParamIds.java
 *
 * 24.05.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf;


/**
 * Special table parameter ids. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.05.2016 - Added.
 */

public class TableParamIds extends archimedes.acf.param.TableParamIds {

    /** Marks a table which is comparable has no compare-to-members marked. */
    public static final String COMPARABLE = "COMPARABLE";
    /** Marks an additional interface implementations. */
    public static final String IMPLEMENTS = "IMPLEMENTS";
    /** Marks a heritage relation. */
    public static final String INHERITS = "INHERITS";
    /** Marks a table which does not provides a combo box renderer. */
    public static final String NO_COMBOBOX_RENDERER = "NO_COMBOBOX_RENDERER";
    /** Suppresses to generate a data writer for the table. */
    public static final String NO_DATA_WRITER = "NO_DATA_WRITER";
    /** Marks a table which provides no selection view. */
    public static final String NO_SELECTION_VIEW = "NO_SELECTION_VIEW";
    /** Marks a table with as having a scrollable GUI. */
    public static final String SCROLLABLE = "SCROLLABLE";
    /** Marks a table with a separated data stock for vendor and customer. */
    public static final String SEPARATED_DATA_STOCK = "SEPARATED_DATA_STOCK";

}