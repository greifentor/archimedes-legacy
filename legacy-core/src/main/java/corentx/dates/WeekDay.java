/*
 * WeekDay.java
 *
 * 12.08.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import corentx.util.*;


/**
 * Ein Enum f&uuml;r die Wochentage.
 *
 * <P><I><B>Hinweis:</B> Die Bezeichnungen und K&uuml;rzel f&uuml;r die Wochentage sind
 * &uuml;ber Properties konfigurierbar. Standardm&auml;&szlig;ig werden deutsche Bezeichnungen
 * und K&uuml;rzel initialisiert.</I>
 *
 * <P><B>Properties:</B>
 * <TABLE BORDER=1 WIDTH="100%">
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corentx.dates.[day].name</TD>
 *         <TD>Deutscher Tagesname</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Initialisierung des Tagesnamen (z. B. zur Nutzung in der toString()-Methode).
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corentx.dates.[day].token</TD>
 *         <TD>Deutsches K&uuml;rzel zum Tag</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Initialisierung des Tagesk&uuml;rzels.
 *         </TD>
 *     </TR>
 * </TABLE>
 * <P>&nbsp;
 *
 * @author O.Lieshoff
 *
 * @changed OLI 12.08.2009 - Hinzugef&uuml;gt
 *
 */

public enum WeekDay {

    /** Der Bezeichner f&uuml;r den Montag. */
    MONDAY(System.getProperty("corentx.dates.monday.name", "Montag"),
            System.getProperty("corentx.dates.monday.token", "Mo"), "monday"),
    /** Der Bezeichner f&uuml;r den Dienstag. */
    TUESDAY(System.getProperty("corentx.dates.tuesday.name", "Dienstag"),
            System.getProperty("corentx.dates.tuesday.token", "Di"), "tuesday"),
    /** Der Bezeichner f&uuml;r den Mittwoch. */
    WEDNESDAY(System.getProperty("corentx.dates.wednesday.name", "Mittwoch"),
            System.getProperty("corentx.dates.wednesday.token", "Mi"), "wednesday"),
    /** Der Bezeichner f&uuml;r den Donnerstag. */
    THURSDAY(System.getProperty("corentx.dates.thursday.name", "Donnerstag"),
            System.getProperty("corentx.dates.thursday.token", "Do"), "thursday"),
    /** Der Bezeichner f&uuml;r den Freitag. */
    FRIDAY(System.getProperty("corentx.dates.friday.name", "Freitag"),
            System.getProperty("corentx.dates.friday.token", "Fr"), "friday"),
    /** Der Bezeichner f&uuml;r den Sonnabend. */
    SATURDAY(System.getProperty("corentx.dates.saturday.name", "Sonnabend"),
            System.getProperty("corentx.dates.saturday.token", "Sa"), "saturday"),
    /** Der Bezeichner f&uuml;r den Sonntag. */
    SUNDAY(System.getProperty("corentx.dates.sunday.name", "Sonntag"),
            System.getProperty("corentx.dates.sunday.token", "So"), "sunday");

    private String defaultname = null;
    private String defaulttoken = null;
    private String resid = null;

    private WeekDay(String defaultname, String defaulttoken, String resid) {
        this.defaultname = defaultname;
        this.defaulttoken = defaulttoken;
        this.resid = resid;
    }

    /**
     * Liefert den Namen des Tages.
     *
     * @return Der Name des Tages.
     */
    public String getName() {
        return Utl.getProperty("corentx.dates." + this.resid + ".name", this.defaultname);
    }

    /**
     * Liefert das K&uuml;rzel zum Tag.
     *
     * @return Das K&uuml;rzel zum Tag.
     */
    public String getToken() {
        return Utl.getProperty("corentx.dates." + this.resid + ".token", this.defaulttoken);
    }

    /**
     * Liefert den Namen des Enum-Objekts zur&uuml;ck.
     *
     * @return Der Name des Enum-Objekts.
     */
    @Override
    public String toString() {
        return this.getName();
    }

}
