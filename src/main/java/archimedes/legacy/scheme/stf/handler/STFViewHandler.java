/*
 * STFViewHandler.java
 *
 * 08.05.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.handler;


import archimedes.legacy.scheme.stf.*;


/**
 * A class with the basic data for STF access of views.
 *
 * @author ollie
 *
 * @changed OLI 08.05.2013 - Added.
 */

public class STFViewHandler extends AbstractSTFHandler {

    public static final String COUNT = "Anzahl";
    public static final String DESCRIPTION = "Beschreibung";
    public static final String HIDE_TECHNICAL_COLUMNS = "TechnischeSpaltenVerstecken";
    public static final String NAME = "Name";
    public static final String SHOW_REFERENCED_COLUMNS = "ReferenzierteSpaltenAnzeigen";
    public static final String TABLE = "Tabelle";
    public static final String TABLE_COUNT = "Tabellenanzahl";
    public static final String VIEW = "View";
    public static final String VIEWS = "Views";

    /**
     * @changed OLI 08.05.2013 - Added.
     */
    @Override public String getSingleListElementTagId() {
        return VIEW;
    }

    /**
     * @changed OLI 08.05.2013 - Added.
     */
    @Override public String getWholeBlockTagId() {
        return VIEWS;
    }

}