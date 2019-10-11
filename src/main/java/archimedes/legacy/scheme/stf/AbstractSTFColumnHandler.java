/*
 * AbstractSTFColumnHandler.java
 *
 * 29.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf;


/**
 * An extension of the <CODE>AbstractSTFHandler</CODE> for column data.
 *
 * @author ollie
 *
 * @changed OLI 29.04.2013 - Added.
 */

abstract public class AbstractSTFColumnHandler extends AbstractSTFHandler {

    public static final String COLUMN = "Spalte";
    public static final String COLUMNS = "Spalten";
    public static final String TABLE = "Tabelle";
    public static final String TABLES = "Tabellen";

    /**
     * Creates an array with the strings to identify a field of table i and column j.
     *
     * @param i The index of the table whose path create.
     * @param j The index of the column whose path create.
     * @param additionalIds The additional id's which are following the id header for list
     *         elements.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    public String[] createPathForColumn(int i, int j, String... additionalIds) {
        return this.createPath(i, concat(new String[] {COLUMNS, COLUMN+j}, additionalIds));
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