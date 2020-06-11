/*
 * PTimestamp.java
 *
 * 11.08.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import static corentx.util.Checks.*;

import java.io.*;
import java.text.*;
import java.util.*;


/**
 * Eine Implementierung des Timestamp-Interfaces als P(acked)Timestamp. Die grundlegende
 * Strategie sieht an dieser Stelle die Speicherung des Datumswertes als Ganzzahl vor. Die
 * Zeitstempelinformationen werden wie folgt in dem Zahlenwert kodiert: YYYYMMDDHHmmSS.
 *
 * <P>Zeitstempel, die &uuml;ber die Systemuhr oder ein Date-Objekt erzeugt werden, werden
 * standardm&auml;&szlig;ig durch die Klasse <TT>SystemPTimestampFactory</TT> generiert. Um
 * den Modus der Erzeugung, z. B. zwecks setzen fester Werte f&uuml;r ein Testsystem,
 * zu &auml;ndern kann der statischen Variablen <TT>pTimestampFactory</TT> eine andere
 * Implementierung des Interfaces <TT>PTimestampFactory</TT> zugewiesen werden.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.07.2009 - Hinzugef&uuml;gt
 * @changed OLI 03.09.2009 - Erweiterung um die Implementierung der Methoden <TT>toDate()</TT>
 *         und <TT>format(DateFormatStyle, DateFormatStyle, Locale)</TT>. Umstellung der
 *         <TT>valueOf</TT>-Methoden auf DateFormatStyle. Implementierung des Interfaces
 *         <TT>Comparable</TT>.
 * @changed OLI 21.09.2009 - Umstellung der Calendar-Umrechnung auf die Methode
 *         <TT>getLong(Calendar)</TT>.
 * @changed OLI 02.10.2009 - Herstellung der Serialisierbarkeit.
 * @changed OLI 29.10.2010 - &Uuml;berschreiben der Methode <TT>hashCode()</TT>.
 * @changed OLI 08.08.2011 - Umstellung auf eine Factory zur Gewinnung der Systemzeit und zum
 *         Erzeugen darauf basierender Zeitpunktangaben.
 * @changed OLI 08.11.2012 - Markierung als Tagesdatum durch das Interface <TT>DateOfDay</TT>.
 * @changed OLI 03.07.2014 - Erweiterung um eine (konfigurierbare) Kontrolle der an die
 *         <CODE>valusOf(...)</CODE>-Methoden &uuml;bergebenen Daten.
 */

// FIXME (*) OLI 29.10.2010 - Vervollstaendigung der Testabdeckung. Formatanpassungen.

public class PTimestamp implements DateOfDay, Serializable, Timestamp<PTimestamp> {

    public static PTimestampFactory pTimestampFactory = new SystemPTimestampFactory();

    private static LenientModeChecker lenientModeChecker = new LenientModeChecker(
            PTimestamp.class);

    private long timestamp = -1;

    /**
     * Pr&uuml;ft, ob aus den &uuml;bergebenen Parametern ein valides PTimestamp-Objekt
     * generiert werden kann.
     *
     * @param day Der Tag zum zu pr&uuml;fenden Datum.
     * @param month Der Monat zum zu pr&uuml;fenden Datum.
     * @param year Das Jahr zum zu pr&uuml;fenden Datum.
     * @param hour Die Stundenangabe zum zu pr&uuml;fenden Datum.
     * @param minute Die Minutenangabe zum zu pr&uuml;fenden Datum.
     * @param second Die Sekundenangabe zum zu pr&uuml;fenden Datum.
     * @return <TT>true</TT>, wenn die Parameter ein valides PTimestamp-Objekt ergeben
     *         k&ouml;nnten.
     */
    public static boolean validate(long day, long month, long year, long hour, long minute,
            long second) {
        if (year < 1) {
            return false;
        }
        if ((month < 1) || (month > 12)) {
            return false;
        }
        if (day < 1) {
            return false;
        }
        if ((hour < 0) || (hour > 23)) {
            return false;
        }
        if ((minute < 0) || (minute > 59)) {
            return false;
        }
        if ((second < 0) || (second > 59)) {
            return false;
        }
        return (day <= TimestampUtil.getDayCountForMonth(month, year));
    }

    /**
     * Liefert ein PTimestamp, das aus dem &uuml;bergebenen String gewonnen wird. Der String
     * mu&szlig; der Zeitpunkt im Stil SHORT f&uuml;r Datum und Uhrzeit enthalten und die
     * Konventionen des durch das Betriebssystem voreingestellten Locales erf&uuml;llen.
     *
     * @param s Der String mit der Zeitpunktangabe
     * @throws NullPointerException Falls als Argument eine <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
     *
     * @precondition s != <TT>null</TT>
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public static PTimestamp valueOf(String s) throws NullPointerException, ParseException {
        return valueOf(s, DateFormatStyle.SHORT, DateFormatStyle.SHORT, Locale.getDefault());
    }

    /**
     * Liefert ein PTimestamp, das aus dem &uuml;bergebenen String gewonnen wird.
     * <P>Unter dem deutschen Locale verarbeitet die Methode folgende Muster:
     * <P><B>Formate f&uuml;r das Datum:</B>
     * <BR>&nbsp;
     * <TABLE BORDER=1 WIDTH="100%">
     *     <TR VALIGN=TOP>
     *         <TH ALIGN=LEFT>DateFormatStyle</TH>
     *         <TH ALIGN=LEFT>Format</TH>
     *         <TH ALIGN=LEFT>Beispiel</TH>
     *     </TR>
     *     <TR VALIGN=TOP>
     *         <TD>SHORT</TD>
     *         <TD>dd.MM.yy</TD>
     *         <TD>06.02.98</TD>
     *     </TR>
     *     <TR VALIGN=TOP>
     *         <TD>MEDIUM</TD>
     *         <TD>dd.MM.yyyy</TD>
     *         <TD>06.02.1998</TD>
     *     </TR>
     *     <TR VALIGN=TOP>
     *         <TD>LONG</TD>
     *         <TD>d. MMMM yyyy</TD>
     *         <TD>7. April 2000</TD>
     *     </TR>
     *     <TR VALIGN=TOP>
     *         <TD>FULL</TD>
     *         <TD>EEEE, d. MMMM yyyy</TD>
     *         <TD>Freitag, 7. April 2000</TD>
     *     </TR>
     * </TABLE>
     *
     * <P><B>Formate f&uuml;r die Uhrzeit:</B>
     * <BR>&nbsp;
     * <TABLE BORDER=1 WIDTH="100%">
     *     <TR VALIGN=TOP>
     *         <TH ALIGN=LEFT>DateFormatStyle</TH>
     *         <TH ALIGN=LEFT>Format</TH>
     *         <TH ALIGN=LEFT>Beispiel</TH>
     *     </TR>
     *     <TR VALIGN=TOP>
     *         <TD>SHORT</TD>
     *         <TD>HH:mm</TD>
     *         <TD>12:34</TD>
     *     </TR>
     *     <TR VALIGN=TOP>
     *         <TD>MEDIUM</TD>
     *         <TD>HH:mm:ss</TD>
     *         <TD>12:34:56</TD>
     *     </TR>
     *     <TR VALIGN=TOP>
     *         <TD>LONG</TD>
     *         <TD>HH:mm:ss z</TD>
     *         <TD>12:34:56 CEST</TD>
     *     </TR>
     *     <TR VALIGN=TOP>
     *         <TD>FULL</TD>
     *         <TD>H.mm' Uhr 'z</TD>
     *         <TD>12.34 Uhr CEST</TD>
     *     </TR>
     * </TABLE>
     *
     * @param s Der String mit der Zeitpunktangabe
     * @param styleDate Der <TT>DateFormatStyle</TT> f&uuml;r die Formatierung des Datums.
     * @param styleTime Der <TT>DateFormatStyle</TT> f&uuml;r die Formatierung der Uhrzeit.
     * @param locale Das Locale, zu dem die Auswertung stattfinden soll.
     * @throws NullPointerException Falls der String, eine der Stilangaben oder das Locale als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
     *
     * @precondition s != <TT>null</TT> &amp;&amp; locale != <TT>null</TT> &amp;&amp; styleDate
     *         != <TT>null</TT> &amp;&amp; styleTime != <TT>null</TT>
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     *
     * @todo OLI 22.09.2009 - Die DateFormat-Geschichte l&auml;uft nicht auf allen Zielsystemen
     *         gleich. Daher mu&szlig; hier eine eigene Funktion zur Bildung von Strings bzw.
     *         zum Lesen aus String implementiert werden.
     */
    public static PTimestamp valueOf(String s, DateFormatStyle styleDate,
            DateFormatStyle styleTime, Locale locale) throws NullPointerException,
            ParseException {
        DateFormat df = DateFormat.getDateTimeInstance(styleDate.getStyleConstant(),
                styleTime.getStyleConstant(), locale);
        df.setLenient(lenientModeChecker.isLenient());
        Date d = df.parse(s);
        return new PTimestamp(d);
    }

    /**
     * Liefert ein PTimestamp, das aus dem &uuml;bergebenen String gewonnen wird. Als Stil wird
     * hier SHORT f&uuml;r Datum und Uhrzeit angenommen.
     *
     * @param s Der String mit der Zeitpunktangabe
     * @param locale Das Locale, zu dem die Auswertung stattfinden soll.
     * @throws NullPointerException Falls der String oder das Locale als <TT>null</TT>-Referenz
     *         &uuml;bergeben werden.
     * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
     *
     * @precondition s != <TT>null</TT> &amp;&amp; locale != <TT>null</TT>
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public static PTimestamp valueOf(String s, Locale locale) throws NullPointerException,
            ParseException {
        return valueOf(s, DateFormatStyle.SHORT, DateFormatStyle.SHORT, locale);
    }

    /** Generiert ein PTimestamp-Objekt mit dem aktuellen Datum der lokalen Rechnerzeit. */
    public PTimestamp() {
        super();
        this.timestamp = pTimestampFactory.getLong();
    }

    /**
     * Erzeugt einen PTimestamp aus dem angegebenen Java-Date-Objekt.
     * 
     * @param date Das Java-Date-Objekt, zu dem ein PTimestamp generiert werden soll.
     * @throws IllegalArgumentException Falls der Inhalt des &uuml;bergebenen Java-Date-Objektes
     *         kein valides Datum enth&auml;lt.
     *
     * <P><I><B>Hinweis:</B> Die Exception wird niemals ausgel&ouml;st, weil der Calendar ein
     * falsches Datum in ein valides umrechnet (aus dem 32.03. wird beispielsweise der 01.04).
     * </I>
     *
     */
    public PTimestamp(Date date) {
        super();
        this.timestamp = pTimestampFactory.getLong(date);
    }

    /**
     * Erzeugt einen PTimestamp aus dem &uuml;bergebenen Long-Wert, falls dieser einen validen
     * Zeitstempel im PTimestamp-Format (JJJJMMTTHHMMSS) enth&auml;t.
     *
     * @param date Ein Zeitstempel im PTimestamp-Format (JJJJMMTTHHMMSS).
     * @throws IllegalArgumentException Falls der &uuml;bergebene Long-Wert keinen validen
     *         Zeitstempel enth&auml;lt.
     */
    public PTimestamp(long date) throws IllegalArgumentException {
        this((date % 100000000L / 1000000), (date % 10000000000L / 100000000L),
                (date / 10000000000L), (date % 1000000 / 10000), (date % 10000 / 100),
                (date % 100));
    }

    /**
     * Erzeugt einemn PTimestamp aus den &uuml;bergebenen Parametern.
     *
     * @param day Der Tag des zu setzenden Zeitstempels.
     * @param month Der Monat des zu setzenden Zeitstempels.
     * @param year Der Jahr des zu setzenden Zeitstempels.
     * @param hour Die Stunde des zu setzenden Zeitstempels.
     * @param minute Die Minutenangabe des zu setzenden Zeitstempels.
     * @param second Die Sekunden des zu setzenden Zeitstempels.
     * @throws IllegalArgumentException Falls die Parameter nicht in einen validen Zeitstempel
     *         umgesetzt werden k&ouml;nnen.
     */
    public PTimestamp(long day, long month, long year, long hour, long minute, long second)
            throws IllegalArgumentException {
        super();
        if (TimestampUtil.validate(day, month, year, hour, minute, second)) {
            this.timestamp = (year * 10000000000L) + (month * 100000000L) + (day * 1000000)
                    + (hour * 10000) + (minute * 100) + second;
        } else {
            throw new IllegalArgumentException("can not create a valid date by parameters: "
                    + day + "." + month + "." + year + "  " + hour + ":" + minute + ":" + second
                    );
        }
    }

    /**
     * Generiert ein PTimestamp anhand des &uuml;bergebenen PTimestamps.
     *
     * @param pts PTimestamp Das PTimestamp, aus dem das neue PTimestamp generiert werden soll.
     */
    public PTimestamp(PTimestamp pts) {
        this(pts.get(TimestampUnit.DAY), pts.get(TimestampUnit.MONTH),
                pts.get(TimestampUnit.YEAR), pts.get(TimestampUnit.HOUR), pts.get(
                TimestampUnit.MINUTE), pts.get(TimestampUnit.SECOND));
    }

    @Override
    public PTimestamp add(TimestampUnit tsu, long unitsref) {
        return (PTimestamp) TimestampUtil.add(this, tsu, unitsref);
    }

    @Override
    public PTimestamp clone() throws CloneNotSupportedException {
        return new PTimestamp(this.get(TimestampUnit.DAY), this.get(TimestampUnit.MONTH),
                this.get(TimestampUnit.YEAR), this.get(TimestampUnit.HOUR),
                this.get(TimestampUnit.MINUTE), this.get(TimestampUnit.SECOND));
    }

    @Override
    public int compareTo(Object o) {
        PTimestamp pts = (PTimestamp) o;
        if (this.toLong() == pts.toLong()) {
            return 0;
        } else if (this.toLong() < pts.toLong()) {
            return -1;
        }
        return 1;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PTimestamp)) {
            return false;
        }
        PTimestamp pts = (PTimestamp) o;
        return pts.toLong() == this.toLong();
    }

    /**
     * @todo OLI 22.09.2009 - Die DateFormat-Geschichte l&auml;uft nicht auf allen Zielsystemen
     *         gleich. Daher mu&szlig; hier eine eigene Funktion zur Bildung von Strings bzw.
     *         zum Lesen aus String implementiert werden.
     */
    public String format(DateFormatStyle styleDate, DateFormatStyle styleTime, Locale locale)
            throws IllegalArgumentException, NullPointerException {
        DateFormat df = DateFormat.getDateTimeInstance(styleDate.getStyleConstant(),
                styleTime.getStyleConstant(), locale);
        return df.format(this.toDate());
    }

    public long get(TimestampUnit tsu) {
        long num = this.toLong();
        if (tsu.ordinal() <= TimestampUnit.MILLISECOND.ordinal()) {
            throw new IllegalArgumentException(tsu + " is not a valid timestamp unit for"
                    + "PTimestamp.");
        }
        switch (tsu) {
        case DAY:
            return (num % 100000000L) / 1000000;
        case HOUR:
            return (num % 1000000) / 10000;
        case MINUTE:
            return (num % 10000) / 100;
        case MONTH:
            return (num % 10000000000L) / 100000000L;
        case SECOND:
            return num % 100;
        case YEAR:
            return num / 10000000000L;
        }
        return 0;
    }

    /**
     * @changed OLI 29.10.2010 - Hinzugef&uuml;gt.
     */
    @Override
    public int hashCode() {
        return (int) this.toLong();
    }

    public PTimestamp set(TimestampUnit tsu, long valueref) {
        long value = valueref;
        long day = 0;
        long hour = 0;
        long minute = 0;
        long month = 0;
        long second = 0;
        long year = 0;
        if (tsu.ordinal() <= TimestampUnit.MILLISECOND.ordinal()) {
            throw new IllegalArgumentException(tsu + " is not a valid timestamp unit for"
                    + "PTimestamp.");
        }
        day = this.get(TimestampUnit.DAY);
        hour = this.get(TimestampUnit.HOUR);
        minute = this.get(TimestampUnit.MINUTE);
        month = this.get(TimestampUnit.MONTH);
        second = this.get(TimestampUnit.SECOND);
        year = this.get(TimestampUnit.YEAR);
        switch (tsu) {
        case DAY:
            day = value;
            break;
        case HOUR:
            hour = value;
            break;
        case MINUTE:
            minute = value;
            break;
        case MONTH:
            month = value;
            break;
        case SECOND:
            second = value;
            break;
        case YEAR:
            year = value;
            break;
        }
        return new PTimestamp(day, month, year, hour, minute, second);
    }

    private void setNumber(long timestamp) {
        this.timestamp = timestamp;
    }

    public PTimestamp setUnchecked(TimestampUnit tsu, long valueref) {
        long value = valueref;
        long day = 0;
        long hour = 0;
        long minute = 0;
        long month = 0;
        long second = 0;
        long year = 0;
        PTimestamp pts = new PTimestamp();
        if (tsu.ordinal() <= TimestampUnit.MILLISECOND.ordinal()) {
            throw new IllegalArgumentException(tsu + " is not a valid timestamp unit for "
                    + "PTimestamp.");
        }
        day = this.get(TimestampUnit.DAY);
        hour = this.get(TimestampUnit.HOUR);
        minute = this.get(TimestampUnit.MINUTE);
        month = this.get(TimestampUnit.MONTH);
        second = this.get(TimestampUnit.SECOND);
        year = this.get(TimestampUnit.YEAR);
        switch (tsu) {
        case DAY:
            day = value;
            break;
        case HOUR:
            hour = value;
            break;
        case MINUTE:
            minute = value;
            break;
        case MONTH:
            month = value;
            break;
        case SECOND:
            second = value;
            break;
        case YEAR:
            year = value;
            break;
        }
        pts.setNumber((year * 10000000000L) + (month * 100000000L) + (day * 1000000)
                + (hour * 10000) + (minute * 100) + second);
        return pts;
    }

    /**
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public Date toDate() {
        Calendar cal = Calendar.getInstance();
        cal.set((int) this.get(TimestampUnit.YEAR), (int) this.get(TimestampUnit.MONTH)-1,
                (int) this.get(TimestampUnit.DAY), (int) this.get(TimestampUnit.HOUR),
                (int) this.get(TimestampUnit.MINUTE), (int) this.get(TimestampUnit.SECOND));
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Liefert den Longwert zum Datum (JJJJMMTTHHSSMM).
     *
     * @return Der Longwert zum Datum (JJJJMMTTHHSSMM).
     */
    public long toLong() {
        return this.timestamp;
    }

    @Override
    public String toString() {
        return this.format(DateFormatStyle.MEDIUM, DateFormatStyle.MEDIUM, Locale.GERMANY);
    }

    public boolean validate(long... p) {
        ensure(p.length == 6, "validate method should have six parameters for PTimestamps.");
        long day = p[0];
        long hour = p[3];
        long minute = p[4];
        long month = p[1];
        long second = p[5];
        long year = p[2];
        return validate(day, month, year, hour, minute, second);
    }

}