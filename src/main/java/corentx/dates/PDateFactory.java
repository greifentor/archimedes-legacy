/*
 * PDateFactory.java
 *
 * 09.08.2011
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import java.util.*;


/**
 * Eine Factory zur Generierung von Datumsobjekten.
 *
 * <P>Das Interface dient dazu, die Generierung von Zeitstempeln, z. B. f&uuml;r Testsysteme
 * beeinflussbar zu machen. Die <TT>PDate</TT>-Klasse verf&uuml;gt &uuml;ber eine statische
 * Instanz einer Implementierung dieses Interfaces, die im Standardfall auf die Systemuhr
 * zugreift.
 *
 * <P><I><B>Wichtig!</B> Beachten Sie, dass in der Implementierung der Factory weder der
 * parameterlose, noch der mit dem <TT>java.util.Data</TT> parametrierte Konstruktor der
 * Zeitstempelklasse zum Einsatz kommen darf.</I>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 09.08.2011 - Hinzugef&uuml;gt.
 */

public interface PDateFactory {

    /**
     * Liefert das aktuelle Datum in gepackter Long-Schreibweise (JJJJMMDD).
     *
     * @return Das aktuelle Datum in gepackter Long-Schreibweise (JJJJMMDD).
     *
     * @changed OLI 09.08.2011 - Hinzugef&uuml;gt.
     */
    public long getLong();

    /**
     * Liefert das sich aus dem angegebenen Date-Objekt ergebende Datum in gepackter
     * Long-Schreibweise (JJJJMMDD).
     *
     * @param date Das Date-Objekt, aus dem das Datum generiert werden soll.
     * @return Das sich aus dem Date-Objekt ergebende Datum in gepackter Long-Schreibweise
     *         (JJJJMMDD).
     * @throws IllegalArgumentException Falls eine der Vorbedingungen verletzt wird.
     * @precondition date != <TT>null</TT>.
     *
     * @changed OLI 09.08.2011 - Hinzugef&uuml;gt.
     */
    public long getLong(Date date);

}