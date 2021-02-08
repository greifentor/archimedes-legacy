/*
 * PTimestampFactory.java
 *
 * 08.08.2011
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import java.util.*;


/**
 * Eine Factory zur Generierung von Zeitstempeln.
 *
 * <P>Das Interface dient dazu, die Generierung von Zeitstempeln, z. B. f&uuml;r Testsysteme
 * beeinflussbar zu machen. Die <TT>PTimestamp</TT>-Klasse verf&uuml;gt &uuml;ber eine statische
 * Instanz einer Implementierung dieses Interfaces, die im Standardfall auf die Systemuhr
 * zugreift.
 *
 * <P><I><B>Wichtig!</B> Beachten Sie, dass in der Implementierung der Factory weder der
 * parameterlose, noch der mit dem <TT>java.util.Data</TT> parametrierte Konstruktor der
 * Zeitstempelklasse zum Einsatz kommen darf.</I>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.08.2011 - Hinzugef&uuml;gt.
 */

public interface PTimestampFactory {

    /**
     * Liefert den aktuellen Zeitstempel in gepackter Long-Schreibweise (JJJJMMDDHHmmSS).
     *
     * @return Der aktuelle Zeitstempel in gepackter Long-Schreibweise (JJJJMMDDHHmmSS).
     *
     * @changed OLI 08.08.2011 - Hinzugef&uuml;gt.
     */
    public long getLong();

    /**
     * Liefert den sich aus dem angegebenen Date-Objekt ergebenden Zeitstempel in gepackter
     * Long-Schreibweise (JJJJMMDDHHmmSS).
     *
     * @param date Das Date--Objekt, aus dem der Zeitstempel generiert werden soll.
     * @return Der sich aus dem Date-Objekt ergebende Zeitstempel in gepackter Long-Schreibweise
     *         (JJJJMMDDHHmmSS).
     * @throws IllegalArgumentException Falls eine der Vorbedingungen verletzt wird.
     * @precondition date != <TT>null</TT>.
     *
     * @changed OLI 08.08.2011 - Hinzugef&uuml;gt.
     */
    public long getLong(Date date);

}