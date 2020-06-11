/*
 * DateFormatStyle.java
 *
 * 03.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import java.text.*;


/**
 * Dieser Enum definiert Formatierungsstile f&uuml;r Datumsangaben im Zusammenspiel mit den
 * Timestamp-Implementierungen.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.09.2009 - Hinzugef&uuml;gt
 *
 */

public enum DateFormatStyle {

    /** Bezeichner f&uuml;r die kurze Formatierung. */
    SHORT(DateFormat.SHORT),
    /** Bezeichner f&uuml;r die mittellange Formatierung. */
    MEDIUM(DateFormat.MEDIUM),
    /** Bezeichner f&uuml;r die lange Formatierung. */
    LONG(DateFormat.LONG),
    /** Bezeichner f&uuml;r die vollst&auml;ndige Formatierung. */
    FULL(DateFormat.FULL);

    private int styleConstant = -1;

    private DateFormatStyle(int styleConstant) {
        this.styleConstant = styleConstant;
    }

    /**
     * Liefert die Style-Konstante der DateFormat-Klasse.
     *
     * @return Die Style-Konstante der DateFormat-Klasse.
     *
     */
    public int getStyleConstant() {
        return this.styleConstant;
    }

    /**
     * Liefert den <TT>DateFormatStyle</TT> zur angegebenen <TT>DateFormat</TT>-Konstante.
     *
     * @param style Die <TT>DateFormat</TT>-Konstante zu der der entsprechende
     *         <TT>DateFormatStyle</TT> ermittelt werden soll.
     * @return Der <TT>DateFormatStyle</TT> zur angebene <TT>DateFormat</TT>-Konstante.
     * @throws IllegalArgumentException Falls der angegebene Wert keine Entsprechung als
     *         <TT>DateFormatStyle</TT> hat.
     */
    public static DateFormatStyle getDateFormatStyle(int style) {
        DateFormatStyle[] dfs = DateFormatStyle.values();
        int i = 0;
        for (i = 0; i < dfs.length; i++) {
            if (dfs[i].getStyleConstant() == style) {
                return dfs[i];
            }
        }
        throw new IllegalArgumentException("value " + style + " doesn't matches with any "
                + "DateFormatStyle.");
    }

}