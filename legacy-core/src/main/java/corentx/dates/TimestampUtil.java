/*
 * TimestampUtil.java
 *
 * 22.07.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import static corentx.util.Checks.*;

import java.util.*;


/**
 * Eine Utilityklasse zum Thema Zeitstempel und Operationen darauf.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.07.2009 - Hinzugef&uuml;gt
 * @changed OLI 04.08.2009 - Erweiterung um die Methode <TT>add(long, TimestampUnit, long)</TT>.
 * @changed OLI 07.08.2009 - Austausch der Methode add gegen die Implememtierung aus
 *         <TT>corent.dates.PTimestamp</TT>.
 * @changed OLI 23.12.2010 - Erweiterung um die Methode <TT>diff(Timestamp, Timestamp)</TT>.
 */

public class TimestampUtil {

    /**
     * Es ist vollkommen ohne Sinn, diese Klasse zu instantiieren. Daher wird eine
     * <TT>UnsupportedOperationException</TT> geworfen, falls es dennoch versucht wird.
     *
     * @throws UnsupportedOperationException Falls aufgerufen ...
     *
     * @changed OLI 23.12.2010 - Hinzugef&uuml;gt.
     */
    public TimestampUtil() throws UnsupportedOperationException {
        throw new UnsupportedOperationException("is makes nos sense to create an instance of "
                + "this class!");
    }

    /**
     * Addiert den angegebenen Wert zur ebenfalls angegebenen Zeiteinheit des Zeitstempels und
     * liefert einen neuen Zeitstempel mit den aktualisierten Daten zur&uuml;ck.
     *
     * @param tss Der Zeitstempel auf den die Addition angewandt werden soll.
     * @param tsu Die Zeiteinheit, auf die sich die Addition bezieht.
     * @param value Der Wert, der auf die angegebene Zeiteinheit des Zeitstempels addiert werden
     *         soll.
     * @return Ein Zeitstempel mit aktualisierten Werten nach Addition des angebenen Wertes auf
     *         entsprechende Zeiteinheit.
     * @throws IllegalArgumentExcpetion Falls die Zeiteinheit f&uuml;r die
     *         Zeitstempelimplementierung nicht g&uuml;ltig ist (z. B. MINUTE bei PDate)
     *         <B>oder</B> sich der Quellzeitstempel wider Definition als nicht clonebar
     *         erweist.
     * @throws NullPointerException Falls der &uuml;bergebene Zeitstempel oder die Zeiteinheit,
     *         auf die die Addition angewandt werden soll, als <TT>null</TT>-Referenz
     *         &uuml;bergeben werden.
     *
     * @precondition tss != <TT>null</TT> &amp;&amp; tsu != <TT>null</TT>
     *
     * @changed OLI 07.08.2009 - Hinzugef&uuml;gt.
     *
     */
    public static Timestamp add(Timestamp tss, TimestampUnit tsu, long value)
            throws IllegalArgumentException, NullPointerException {
        long i = 0;
        long count = 0;
        long dfm = 0;
        long inc = 0;
        Timestamp ts = null;
        ensure(tsu != null, new NullPointerException("timestamp unit can not be null for "
                + "addition."));
        try {
            ts = tss.clone();
        } catch (CloneNotSupportedException cnse) {
            // Das hier kann nicht eintreten, da Timestamp Cloneable erweitert ...
            throw new IllegalArgumentException("timestamp is noch cloneable.");
        }
        if (tsu == TimestampUnit.YEAR) {
            ts = ts.setUnchecked(TimestampUnit.YEAR, ts.get(TimestampUnit.YEAR) + value);
            if ((ts.get(TimestampUnit.MONTH) == 2) && (getDayCountForMonth(
                    ts.get(TimestampUnit.MONTH), ts.get(TimestampUnit.YEAR))
                    < ts.get(TimestampUnit.DAY))) {
                ts = ts.set(TimestampUnit.DAY, 1);
                ts = ts.set(TimestampUnit.MONTH, 3);
            }
        } else if (tsu == TimestampUnit.MONTH) {
            ts = ts.add(TimestampUnit.YEAR, (value / 12));
            value = value % 12;
            count = (value > 0 ? value : 0 - value);
            inc = (value > 0 ? 1 : -1);
            for (i = 0; i < count; i++) {
                if (ts.get(TimestampUnit.MONTH) + inc < 1) {
                    ts = ts.add(TimestampUnit.YEAR, -1);
                    ts = ts.set(TimestampUnit.MONTH, 12);
                } else if (ts.get(TimestampUnit.MONTH) + inc > 12) { 
                    ts = ts.add(TimestampUnit.YEAR, 1);
                    ts = ts.set(TimestampUnit.MONTH, 1);
                } else {
                    ts = ts.setUnchecked(TimestampUnit.MONTH, (int) (ts.get(TimestampUnit.MONTH)
                            + inc));
                }
            }
            dfm = getDayCountForMonth(ts.get(TimestampUnit.MONTH), ts.get(TimestampUnit.YEAR));
            if ((dfm < ts.get(TimestampUnit.DAY))) {
                /*
                ts = ts.add(TimestampUnit.DAY, (getDayCountForMonth(ts.get(TimestampUnit.MONTH),
                        ts.get(TimestampUnit.YEAR)) - ts.get(TimestampUnit.DAY)));
                ts = ts.add(TimestampUnit.MONTH, 1);
                */
                ts = ts.set(TimestampUnit.DAY, dfm);
            }
        } else if (tsu == TimestampUnit.DAY) {
            count = (value > 0 ? value : 0 - value);
            inc = (value > 0 ? 1 : -1);
            for (i = 0; i < count; i++) {
                if (ts.get(TimestampUnit.DAY) + inc < 1) {
                    ts = ts.add(TimestampUnit.MONTH, -1);
                    ts = ts.set(TimestampUnit.DAY, getDayCountForMonth(ts.get(
                            TimestampUnit.MONTH), ts.get(TimestampUnit.YEAR)));
                } else if (ts.get(TimestampUnit.DAY) + inc > getDayCountForMonth(ts.get(
                        TimestampUnit.MONTH), ts.get(TimestampUnit.YEAR))) { 
                    ts = ts.setUnchecked(TimestampUnit.MONTH, (int) (ts.get(TimestampUnit.MONTH)
                            + 1));
                    if (ts.get(TimestampUnit.MONTH) > 12) {
                        ts = ts.set(TimestampUnit.MONTH, 1);
                        ts = ts.add(TimestampUnit.YEAR, 1);
                    }
                    ts = ts.set(TimestampUnit.DAY, 1);
                } else {
                    ts = ts.setUnchecked(TimestampUnit.DAY, (int) (ts.get(TimestampUnit.DAY)
                            + inc));
                }
            }
        } else if (tsu == TimestampUnit.HOUR) {
            ts = ts.add(TimestampUnit.DAY, (value / 24));
            value = value % 24;
            count = (value > 0 ? value : 0 - value);
            inc = (value > 0 ? 1 : -1);
            for (i = 0; i < count; i++) {
                if (ts.get(TimestampUnit.HOUR) + inc < 0) {
                    ts = ts.add(TimestampUnit.DAY, -1);
                    ts = ts.set(TimestampUnit.HOUR, 23);
                } else if (ts.get(TimestampUnit.HOUR) + inc > 23) { 
                    ts = ts.add(TimestampUnit.DAY, 1);
                    ts = ts.set(TimestampUnit.HOUR, 0);
                } else {
                    ts = ts.setUnchecked(TimestampUnit.HOUR, (int) (ts.get(TimestampUnit.HOUR)
                            + inc));
                }
            }
        } else if (tsu == TimestampUnit.MINUTE) {
            ts = ts.add(TimestampUnit.HOUR, (value / 60));
            value = value % 60;
            count = (value > 0 ? value : 0 - value);
            inc = (value > 0 ? 1 : -1);
            for (i = 0; i < count; i++) {
                if (ts.get(TimestampUnit.MINUTE) + inc < 0) {
                    ts = ts.add(TimestampUnit.HOUR, -1);
                    ts = ts.set(TimestampUnit.MINUTE, 59);
                } else if (ts.get(TimestampUnit.MINUTE) + inc > 59) { 
                    ts = ts.add(TimestampUnit.HOUR, 1);
                    ts = ts.set(TimestampUnit.MINUTE, 0);
                } else {
                    ts = ts.setUnchecked(TimestampUnit.MINUTE, (int) (ts.get(
                            TimestampUnit.MINUTE) + inc));
                }
            }
        } else if (tsu == TimestampUnit.SECOND) {
            ts = ts.add(TimestampUnit.HOUR, (value / 3600));
            value = value % 3600;
            ts = ts.add(TimestampUnit.MINUTE, (value / 60));
            value = value % 60;
            count = (value > 0 ? value : 0 - value);
            inc = (value > 0 ? 1 : -1);
            for (i = 0; i < count; i++) {
                if (ts.get(TimestampUnit.SECOND) + inc < 0) {
                    ts = ts.add(TimestampUnit.MINUTE, -1);
                    ts = ts.set(TimestampUnit.SECOND, 59);
                } else if (ts.get(TimestampUnit.SECOND) + inc > 59) { 
                    ts = ts.add(TimestampUnit.MINUTE, 1);
                    ts = ts.set(TimestampUnit.SECOND, 0);
                } else {
                    ts = ts.setUnchecked(TimestampUnit.SECOND, (int) (ts.get(
                            TimestampUnit.SECOND) + inc));
                }
            }
        } else if (tsu == TimestampUnit.MILLISECOND) {
            ts = ts.add(TimestampUnit.HOUR, (value / 3600000));
            value = value % 3600000;
            ts = ts.add(TimestampUnit.MINUTE, (value / 60000));
            value = value % 60000;
            ts = ts.add(TimestampUnit.SECOND, (value / 1000));
            value = value % 1000;
            count = (value > 0 ? value : 0 - value);
            inc = (value > 0 ? 1 : -1);
            for (i = 0; i < count; i++) {
                if (ts.get(TimestampUnit.MILLISECOND) + inc < 0) {
                    ts = ts.add(TimestampUnit.SECOND, -1);
                    ts = ts.set(TimestampUnit.MILLISECOND, 999);
                } else if (ts.get(TimestampUnit.MILLISECOND) + inc > 999) { 
                    ts = ts.add(TimestampUnit.SECOND, 1);
                    ts = ts.set(TimestampUnit.MILLISECOND, 0);
                } else {
                    ts = ts.setUnchecked(TimestampUnit.MILLISECOND, (int) (ts.get(
                            TimestampUnit.MILLISECOND) + inc));
                }
            }
        }
        return ts;
    }

    /**
     * Liefert die Anzahl der Millisekunden, die zwischen den beiden Timestamps liegt.
     *
     * <P><I><B>Hinweis:</B> Nicht alle Timestamp-Implementierungen bringen hier sinnvolle
     * Ergebnisse. <TT>PDate</TT> rechnet beispielsweise immer mit 0:00 als Uhrzeit,
     * w&auml;hrend <TT>PTime</TT> das Datum 0.0.0 zugrunde legt.</I>
     *
     * @since 1.23.1
     * 
     * @param ts0 Zeitpunkt 1 zur Berechnung.
     * @param ts1 Zeitpunkt 2 zur Berechnung.
     * @return Die Anzahl der Millisekunden zwischen den beiden Zeitpunkten. Das Ergebnis kann
     *         negativ sein, wenn ts0 zeitlich nach ts1 liegt.
     * @throws IllegalArgumentException Falls eine oder beide Zeitpunkte als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @precondition ts0 != <TT>null</TT>
     * @precondition ts1 != <TT>null</TT>
     *
     * @changed OLI 23.12.2010 - Hinzugef&uuml;gt.
     */
    public static long diff(Timestamp ts0, Timestamp ts1) throws IllegalArgumentException {
        ensure(ts0 != null, "timestamp 0 cannot be null.");
        ensure(ts1 != null, "timestamp 1 cannot be null.");
        return ts1.toDate().getTime() - ts0.toDate().getTime();
    }

    /**
     * Liefert die Anzahl der Tage zum angegebenen Monat im angegeben Jahr.
     *
     * @param month Der Monat zu dem die Anzahl der Tage im angegebenen Jahr ermittelt werden
     *         sollen.
     * @param year Das Jahr, in dem der Monat liegt, f&uuml;r den die Anzahl der Tage ermittelt
     *         werden soll.
     * @return Die Anzahl der Tage zum angegebenen Monat im angegeben Jahr.
     * @throws IllegalArgumentException Falls die Jahreszahl kleiner als 1 oder die Monatsangabe
     *         au&szlig;erhalb des Intervals 1..12 liegt.
     *
     * @precondition year &gt; 0
     */
    public static int getDayCountForMonth(long month, long year) throws IllegalArgumentException
            {
        if (year < 1) {
            throw new IllegalArgumentException("year have to be a positive value.");
        }
        if ((month < 1) || (month > 12)) {
            throw new IllegalArgumentException("month is out of range (1..12): " + month);
        }
        switch ((int) month) {
        case 2:
            if (!leapYear(year)) {
                return 28;
            }
            return 29;
        case 4:
        case 6:
        case 9:
        case 11:
            return 30;
        }
        return 31;
    }

    /**
     * Liefert den Wochentag zum angebenen Timestamp.
     *
     * @param timestamp Der Timestamp, zu dem der Wochentag berechnet werden soll.
     * @return Der Wochentag zum angegebenen Timestamp.
     * @throws IllegalArgumentException Falls ein NullPointer &uuml;bergeben wird.
     *
     * @changed OLI 08.11.2012 - Hinzugef&uuml;gt.
     */
    public static WeekDay getWeekDay(DateOfDay timestamp) throws IllegalArgumentException {
        ensure(timestamp != null, "timestamp cannot be null.");
        Calendar cal = Calendar.getInstance();
        cal.setTime(timestamp.toDate());
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK)-1;
        if (dayOfWeek > 0) {
            return WeekDay.values()[dayOfWeek-1];
        }
        return WeekDay.SUNDAY;
    }

    /**
     * Pr&uuml;ft, ob das angegebene Jahr ein Schaltjahr ist.
     *
     * @param year Der Jahr, das bez&uuml;glich seiner Eigenschaft als Schaltjahr gepr&uuml;ft
     *         werden soll. Das Jahr mu&szlig; eine positive Zahl sein.
     * @return <TT>true</TT>, wenn angegeben Jahr ein Schaltjahr ist,
     *         <BR><TT>false</TT> sonst.
     * @throws IllegalArgumentException Falls die Jahreszahl kleiner als 1 ist.
     *
     * @precondition year &gt; 0
     *
     */
    public static boolean leapYear(long year) throws IllegalArgumentException {
        if (year < 1) {
            throw new IllegalArgumentException("year have to be a positive value.");
        }
        if (year % 400 == 0) {
            return true;
        } else if (year % 100 == 0) {
        } else if (year % 4 == 0) {
            return true;
        }
        return false;
    }

    /**
     * Pr&uuml;ft, ob die angegebenen Parameter ein g&uuml;tiges Datum ergeben (die Methode
     * validiert nur Datumsangaben ab dem Jahr 1).
     *
     * @param day Der Tag des Datums, das gepr&uuml;ft werden soll.
     * @param month Der Monat des Datums, das gepr&uuml;ft werden soll.
     * @param year Der Jahr des Datums, das gepr&uuml;ft werden soll.
     * @return <TT>true</TT>, wenn die &uuml;bergebenen Parameter ein g&uuml;ltiges Datum
     *         ergeben,<BR><TT>false</TT> andernfalls.
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
        return (day <= getDayCountForMonth(month, year));
    }

    /**
     * Pr&uuml;ft, ob die angegebenen Parameter einen g&uuml;tigen Zeitstempel ergeben (die
     * Methode validiert nur Datumsangaben ab dem Jahr 1).
     *
     * @param day Der Tag des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param month Der Monat des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param year Der Jahr des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param hour Die Stunde des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param minute Die Minute des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param second Die Sekunde des Zeitstempels, der gepr&uuml;ft werden soll.
     * @return <TT>true</TT>, wenn die &uuml;bergebenen Parameter ein g&uuml;ltiges Datum
     *         ergeben,<BR><TT>false</TT> andernfalls.
     */
    public static boolean validate(long day, long month, long year, long hour, long minute,
            long second) {
        boolean ok = validate(day, month, year);
        if (!ok) {
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
        return true;
    }

    /**
     * Pr&uuml;ft, ob die angegebenen Parameter einen g&uuml;tigen Zeitstempel ergeben (die
     * Methode validiert nur Datumsangaben ab dem Jahr 1).
     *
     * @param day Der Tag des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param month Der Monat des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param year Der Jahr des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param hour Die Stunde des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param minute Die Minute des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param second Die Sekunde des Zeitstempels, der gepr&uuml;ft werden soll.
     * @param millisecond Die Millisekunden des Zeitstempels, der gepr&uuml;ft werden soll.
     * @return <TT>true</TT>, wenn die &uuml;bergebenen Parameter ein g&uuml;ltiges Datum
     *         ergeben,<BR><TT>false</TT> andernfalls.
     */
    public static boolean validate(long day, long month, long year, long hour, long minute,
            long second, long millisecond) {
        boolean ok = validate(day, month, year, hour, minute, second);
        if (!ok) {
            return false;
        }
        if ((millisecond < 0) || (millisecond > 999)) {
            return false;
        }
        return true;
    }

}