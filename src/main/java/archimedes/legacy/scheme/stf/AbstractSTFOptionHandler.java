/*
 * AbstractSTFOptionHandler.java
 *
 * 15.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf;


/**
 * An extension of the <CODE>AbstractSTFHandler</CODE> for option data.
 *
 * @author ollie
 *
 * @changed OLI 15.10.2013 - Added.
 */

public class AbstractSTFOptionHandler extends AbstractSTFHandler {

    public static final String OPTIONS = "Options";
    public static final String TABLE = "Tabelle";
    public static final String TABLES = "Tabellen";

    /**
     * Creates an array with the strings to identify a field of table i.
     *
     * @param i The index of the table whose path create.
     * @param additionalIds The additional id's which are following the id header for list
     *         elements.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public String[] createPathForOption(int i, String... additionalIds) {
        return this.createPath(i, concat(new String[] {OPTIONS}, additionalIds));
    }

    /**
     * @changed OLI 29.04.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return TABLE;
    }

    /**
     * @changed OLI 29.04.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return TABLES;
    }

}