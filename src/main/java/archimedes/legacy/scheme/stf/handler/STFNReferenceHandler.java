/*
 * STFNReferenceHandler.java
 *
 * 03.05.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;


import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of NReference objects.
 *
 * @author ollie
 *
 * @changed OLI 03.05.2013 - Added.
 */

public class STFNReferenceHandler extends AbstractSTFHandler {

    public static final String ALTERABLE = "Alterable";
    public static final String CODEGENERATOR = "Codegenerator";
    public static final String COLUMN = "Spalte";
    public static final String COUNT = "Anzahl";
    public static final String DELETE_CONFIRMATION_REQUIRED = "DeleteConfirmationRequired";
    public static final String EXTENSIBLE = "Extensible";
    public static final String ID = "Id";
    public static final String NREFERENCE = "NReferenz";
    public static final String NREFERENCES = "NReferenzen";
    public static final String PANEL = "Panel";
    public static final String PANEL_TYPE = "PanelType";
    public static final String PERMIT_CREATE = "PermitCreate";
    public static final String TABLE = "Tabelle";
    public static final String TABLES = "Tabellen";

    /**
     * Creates an array with the strings to identify a field of table i and the nReferences.
     *
     * @param additionalIds The additional id's which are following the id header for list
     *         elements.
     *
     * @changed OLI 03.05.2013 - Added.
     */
    @Override public String[] createPath(int i, String... additionalIds) {
        return super.createPath(i, concat(new String[] {CODEGENERATOR, NREFERENCES},
                additionalIds));
    }

    /**
     * Creates an array with the strings to identify a field of table i and nReference j.
     *
     * @param i The index of the table whose path create.
     * @param j The index of the nReference whose path create.
     * @param additionalIds The additional id's which are following the id header for list
     *         elements.
     *
     * @changed OLI 03.05.2013 - Added.
     */
    public String[] createPathForNReference(int i, int j, String... additionalIds) {
        return this.createPath(i, concat(new String[] {NREFERENCE + j}, additionalIds));
    }

    /**
     * @changed OLI 03.05.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return TABLE;
    }

    /**
     * @changed OLI 03.05.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return TABLES;
    }

}