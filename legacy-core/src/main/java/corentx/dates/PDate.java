/*
 * PDate.java
 *
 * 22.07.2009
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
 * Eine Implementierung des Timestamp-Interfaces als P(acked)Date. Die grundlegende Strategie
 * sieht an dieser Stelle die Speicherung des Datumswertes als Ganzzahl vor. Die
 * Datumsinformationen werden wie folgt in dem Zahlenwert kodiert: YYYYMMDD.
 *
 * <P>Zeitstempel, die &uuml;ber die Systemuhr oder ein Date-Objekt erzeugt werden, werden
 * standardm&auml;&szlig;ig durch die Klasse <TT>SystemPDateFactory</TT> generiert. Um
 * den Modus der Erzeugung, z. B. zwecks setzen fester Werte f&uuml;r ein Testsystem,
 * zu &auml;ndern kann der statischen Variablen <TT>pDateFactory</TT> eine andere
 * Implementierung des Interfaces <TT>PDateFactory</TT> zugewiesen werden.
 *
 * <I><B>Hinweis:</B> Es werden nur Datumsangaben ab dem Jahr 1 (n. Chr.) verarbeitet. Bei den
 * Wochentagen der Datumsangaben vor 1500 kann es aufgrund des vorher benutzten julianischen
 * Kalenders zu Unstimmigkeiten kommen.</I>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.07.2009 - Hinzugef&uuml;gt
 * @changed OLI 07.08.2009 - Erweiterung um die Implementierung des Interfaces
 *         <TT>Cloneable</TT> (als Erfordernis durch &Auml;nderung des Interfaces
 *         <TT>Timestamp</TT>.
 * @changed OLI 03.09.2009 - Erweiterung um die Implementierung der Methoden <TT>toDate()</TT>
 *         und <TT>format(DateFormatStyle, DateFormatStyle, Locale)</TT>. Umstellung der
 *         <TT>valueOf</TT>-Methoden auf DateFormatStyle. Implementierung des Interfaces
 *         <TT>Comparable</TT>.
 * @changed OLI 21.09.2009 - Umstellung der Calendar-Umrechnung auf die Methode
 *         <TT>getInt(Calendar)</TT>.
 * @changed OLI 02.10.2009 - Herstellung der Serialisierbarkeit.
 * @changed OLI 29.10.2010 - &Uuml;berschreiben der Methode <TT>hashCode()</TT> und
 *         Umstrukturierungen im Rahmen der Vervollst&auml;ndigung der Testabdeckung.
 * @changed OLI 09.08.2011 - Umstellung auf eine Factory zur Gewinnung der Systemzeit und zum
 *         Erzeugen darauf basierender Zeitpunktangaben.
 * @changed OLI 08.11.2012 - Markierung als Tagesdatum durch das Interface <TT>DateOfDay</TT>.
 * @changed OLI 03.07.2014 - Erweiterung um eine (konfigurierbare) Kontrolle der an die
 *         <CODE>valusOf(...)</CODE>-Methoden &uuml;bergebenen Daten.
 */

public class PDate implements DateOfDay, Serializable, Timestamp<PDate> {

    /** Eine PDateFactory, die zum Generieren aktueller Datumsangaben genutzt wird. */
    public static PDateFactory pDateFactory = new SystemPDateFactory();

    private static LenientModeChecker lenientModeChecker = new LenientModeChecker(PDate.class);

    private long date = -1;

    /** Generiert ein PDate-Objekt mit dem aktuellen Datum der lokalen Rechnerzeit. */
    public PDate() {
        super();
        this.date = pDateFactory.getLong();
    }

    /**
     * Erzeugt ein PDate aus dem angegebenen Java-Date-Objekt.
     * 
     * @param date Das Java-Date-Objekt, zu dem ein PDate generiert werden soll.
     * @throws IllegalArgumentException Falls der Inhalt des &uuml;bergebenen Java-Date-Objektes
     *         kein valides Datum enth&auml;lt.
     *
     * <P><I><B>Hinweis:</B> Die Exception wird niemals ausgel&ouml;st, weil der Calendar ein
     * falsches Datum in ein valides umrechnet (aus dem 32.03. wird beispielsweise der 01.04).
     * </I>
     *
     */
    public PDate(Date date) {
        super();
        this.date = pDateFactory.getLong(date);
    }

    /**
     * Erzeugt ein PDate aus dem &uuml;bergebenen Integer-Wert, falls dieser ein valides Datum
     * im PDate-Format (JJJJMMTT) enth&auml;t.
     *
     * @param date Ein Datum im PDate-Format (JJJJMMTT).
     * @throws IllegalArgumentException Falls der &uuml;bergebene Integer-Wert kein valides
     *         Datum enth&auml;lt.
     */
    public PDate(long date) throws IllegalArgumentException {
        this(date % 100, (date / 100) % 100, date / 10000);
    }

    /**
     * Erzeugt ein PDate aus den &uuml;bergebenen Parametern.
     *
     * @param day Der Tag des zu setzenden Datums.
     * @param month Der Monat des zu setzenden Datums.
     * @param year Der Jahr des zu setzenden Datums.
     * @throws IllegalArgumentException Falls die Parameter nicht in ein valides Datum umgesetzt
     *         werden k&ouml;nnen.
     */
    public PDate(long day, long month, long year) throws IllegalArgumentException {
        super();
        if (TimestampUtil.validate(day, month, year)) {
            this.date = (year * 10000) + (month * 100) + day;
        } else {
            throw new IllegalArgumentException("can not create a valid date by parameters: "
                    + day + "." + month + "." + year);
        }
    }

    /**
     * Generiert ein PDate anhand des &uuml;bergebenen PDates.
     *
     * @param pdate Das PDate, aus dem das neue PDate generiert werden soll.
     */
    public PDate(PDate pdate) {
        this(pdate.get(TimestampUnit.DAY), pdate.get(TimestampUnit.MONTH),
                pdate.get(TimestampUnit.YEAR));
    }

    public PDate add(TimestampUnit tsu, long unitsref) {
        return (PDate) TimestampUtil.add(this, tsu, unitsref);
    }

    @Override public PDate clone() throws CloneNotSupportedException {
        return new PDate(this.get(TimestampUnit.DAY), this.get(TimestampUnit.MONTH),
                this.get(TimestampUnit.YEAR));
    }

    public int compareTo(Object o) {
        PDate pd = (PDate) o;
        if (this.toLong() == pd.toLong()) {
            return 0;
        } else if (this.toLong() < pd.toLong()) {
            return -1;
        }
        return 1;
    }

    @Override public boolean equals(Object o) {
        if (!(o instanceof PDate)) {
            return false;
        }
        PDate pd = (PDate) o;
        return pd.toLong() == this.toLong();
    }

    public String format(DateFormatStyle styleDate, DateFormatStyle styleTime, Locale locale)
            throws IllegalArgumentException, NullPointerException {
        ensure(styleTime == null, "time informations can not be managed in PDate.");
        DateFormat df = DateFormat.getDateInstance(styleDate.getStyleConstant(), locale);
        return df.format(this.toDate());
    }

    public long get(TimestampUnit tsu) {
        ensure(tsu != null, new NullPointerException("timestamp unit can not be null."));
        long num = this.toLong();
        if (tsu == TimestampUnit.DAY) {
            return num % 100;
        } else if (tsu == TimestampUnit.MONTH) {
            return (num % 10000) / 100;
        } else if (tsu == TimestampUnit.YEAR) {
            return num / 10000;
        }
        throw new IllegalArgumentException(tsu + " is not a valid timestamp unit for PDate.");
    }

    /**
     * @changed OLI 29.10.2010 - Hinzugef&uuml;gt.
     */
    @Override public int hashCode() {
        return (int) this.toLong();
    }

    public PDate set(TimestampUnit tsu, long valueref) {
        ensure(tsu.ordinal() > TimestampUnit.HOUR.ordinal(), tsu + " is not a valid timestamp "
                + "unit for PDate.");
        long value = valueref;
        long day = 0;
        long month = 0;
        long year = 0;
        day = (int) this.get(TimestampUnit.DAY);
        month = (int) this.get(TimestampUnit.MONTH);
        year = (int) this.get(TimestampUnit.YEAR);
        if (tsu == TimestampUnit.DAY) {
            day = value;
        } else if (tsu == TimestampUnit.MONTH) {
            month = value;
        } else if (tsu == TimestampUnit.YEAR) {
            year = value;
        }
        return new PDate(day, month, year);
    }

    private void setNumber(long date) {
        this.date = date;
    }

    public PDate setUnchecked(TimestampUnit tsu, long valueref) {
        ensure(tsu.ordinal() > TimestampUnit.HOUR.ordinal(), tsu + " is not a valid timestamp "
                + "unit for PDate.");
        long value = valueref;
        long day = 0;
        long month = 0;
        long year = 0;
        PDate pdate = new PDate();
        day = this.get(TimestampUnit.DAY);
        month = this.get(TimestampUnit.MONTH);
        year = this.get(TimestampUnit.YEAR);
        if (tsu == TimestampUnit.DAY) {
            day = value;
        } else if (tsu == TimestampUnit.MONTH) {
            month = value;
        } else if (tsu == TimestampUnit.YEAR) {
            year = value;
        }
        pdate.setNumber((year * 10000) + (month * 100) + day);
        return pdate;
    }

    public Date toDate() {
        Calendar cal = Calendar.getInstance();
        cal.set((int) this.get(TimestampUnit.YEAR), (int) this.get(TimestampUnit.MONTH)-1,
                (int) this.get(TimestampUnit.DAY), 0, 0, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }

    /**
     * Liefert den Longwert zum Datum (JJJJMMTT).
     *
     * @return Der Longwert zum Datum (JJJJMMTT).
     */
    public long toLong() {
        return this.date;
    }

    @Override public String toString() {
        return format(DateFormatStyle.MEDIUM, null, Locale.GERMANY);
    }

    public boolean validate(long... p) throws IllegalArgumentException, NullPointerException {
        ensure(p != null, new NullPointerException("parameter list can not be null."));
        ensure(p.length == 3, "validate method should have three parameters for PDates.");
        long day = p[0];
        long month = p[1];
        long year = p[2];
        return PDate.validate(day, month, year);
    }

    /**
     * Pr&uuml;ft, ob aus den &uuml;bergebenen Parametern ein valides PDate-Objekt generiert
     * werden kann.
     *
     * @param day Der Tag zum zu pr&uuml;fenden Datum.
     * @param month Der Monat zum zu pr&uuml;fenden Datum.
     * @param year Das Jahr zum zu pr&uuml;fenden Datum.
     * @return <TT>true</TT>, wenn die Parameter ein valides PDate-Objekt ergeben k&ouml;nnten.
     *
     * @changed OLI 12.08.2009 - Hinzugef&uuml;gt.
     */
    public static boolean validate(long day, long month, long year) {
        if (year < 1) {
            return false;
        }
        if ((month < 1) || (month > 12)) {
            return false;
        }
        if (day < 1) {
            return false;
        }
        return (day <= TimestampUtil.getDayCountForMonth(month, year));
    }

    /**
     * Liefert ein PDate, das aus dem &uuml;bergebenen String gewonnen wird. Der String
     * mu&szlig; das Datum im Stil SHORT enthalten und die Konventionen des durch das
     * Betriebssystem voreingestellten Locales erf&uuml;llen.
     *
     * @param s Der String mit der Datumsangabe
     * @throws NullPointerException Falls als Argument eine <TT>null</TT>-Referenz
     *         &uuml;bergeben wird.
     * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
     *
     * @precondition s != <TT>null</TT>
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public static PDate valueOf(String s) throws NullPointerException, ParseException {
        return valueOf(s, DateFormatStyle.SHORT, Locale.getDefault());
    }

    /**
     * Liefert ein PDate, das aus dem &uuml;bergebenen String gewonnen wird. Als Stil wird hier
     * SHORT angenommen.
     *
     * @param s Der String mit der Datumsangabe
     * @param locale Das Locale, zu dem die Auswertung stattfinden soll.
     * @throws NullPointerException Falls der String oder das Locale als <TT>null</TT>-Referenz
     *         &uuml;bergeben werden.
     * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
     *
     * @precondition s != <TT>null</TT> &amp;&amp; locale != <TT>null</TT>
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public static PDate valueOf(String s, Locale locale) throws NullPointerException,
            ParseException {
        return valueOf(s, DateFormatStyle.SHORT, locale);
    }

    /**
     * Liefert ein PDate, das aus dem &uuml;bergebenen String gewonnen wird.
     * <P>Unter dem deutschen Locale verarbeitet die Methode folgende Muster:
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
     * @param s Der String mit der Datumsangabe
     * @param style Der Stil, in dem die Datumsangabe formatiert werden soll.
     * @param locale Das Locale, zu dem die Auswertung stattfinden soll.
     * @throws NullPointerException Falls der String, die Stilangabe oder das Locale als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
     *
     * @precondition s != <TT>null</TT> &amp;&amp; locale != <TT>null</TT> &amp;&amp;
     *         style != <TT>null</TT>
     *
     * @changed OLI 03.09.2009 - Hinzugef&uuml;gt.
     */
    public static PDate valueOf(String s, DateFormatStyle style, Locale locale)
            throws NullPointerException, ParseException {
        DateFormat df = DateFormat.getDateInstance(style.getStyleConstant(), locale);
        df.setLenient(lenientModeChecker.isLenient());
        Date d = df.parse(s);
        return new PDate(d);
    }

}