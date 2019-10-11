/*
 * STFRelationHandler.java
 *
 * 29.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;


import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of relation data.
 *
 * @author ollie
 *
 * @changed OLI 29.04.2013 - Added.
 */

public class STFRelationHandler extends AbstractSTFColumnHandler {

    public static final String COLUMN = "Spalte";
    public static final String COUNT = "Anzahl";
    public static final String DIRECTION = "Direction";
    public static final String OFFSET = "Offset";
    public static final String POINT = "Point";
    public static final String POINTS = "Points";
    public static final String REFERENCE = "Referenz";
    public static final String VIEW = "View";
    public static final String VIEW_NAME = "Name";
    public static final String VIEWS = "Views";
    public static final String X = "X";
    public static final String Y = "Y";

    /**
     * @changed OLI 26.04.2013 - Added.
     */
    @Override public String[] createPathForColumn(int i, int j, String... additionalIds) {
        return this.createPath(i, concat(new String[] {COLUMNS, COLUMN+j, REFERENCE},
                additionalIds));
    }

    protected String[] createPathPoint(int i, int j, int k, String... additionalIds) {
        return this.createPath(i, concat(new String[] {COLUMNS, COLUMN+j, REFERENCE,
                POINTS, POINT+k}, additionalIds));
    }

    protected String[] createPathView(int i, int j, int k, String... additionalIds) {
        return this.createPath(i, concat(new String[] {COLUMNS, COLUMN+j, REFERENCE,
                VIEWS, VIEW+k}, additionalIds));
    }

    protected String[] createPathViewPoints(int i, int j, int k, int l, String... additionalIds)
            {
        return this.createPath(i, concat(new String[] {COLUMNS, COLUMN+j, REFERENCE,
                VIEWS, VIEW+k, POINTS, POINT+l}, additionalIds));
    }

}