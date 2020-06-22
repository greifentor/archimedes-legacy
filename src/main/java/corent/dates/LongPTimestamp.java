/*
 * LongPTimestamp.java
 *
 * 21.05.2007
 *
 * (c) by O.Lieshoff
 *
 */

package corent.dates;


import java.io.*;
import java.text.*;
import java.util.*;

import logging.Logger;


/**
 * Diese Klasse enth&auml;t einen Zeitpunkt mit Millisekunden. Sie ist in der Lage Ganzzahlen
 * zu erzeugen, die diesen Zeitpunkt in der Form JJJJMMTTHHMMSSmmm zu repr&auml;sentieren.
 *
 * <P><H3>Properties:</H3>
 * <TABLE BORDER=1>
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.dates.LongPTimestamp.toString.suppress.warning</TD>
 *         <TD>false</TD>
 *         <TD>boolean</TD>
 *         <TD>
 *             Wenn diese Flagge gesetzt ist, wird eine eventuelle log4j-Ausgabe einer Warnung 
 *             innerhalb der toString()-Methode unterdr&uuml;ckt. Die Ausgabe hat den WARNING).
 *         </TD>
 *     </TR>
 * </TABLE>
 * <P>&nbsp;
 *
 * @author O.Lieshoff
 *
 * @changed OLI 01.09.2008 - Erweiterung um die Implementierung der Methode <TT>getMillis()</TT>.
 * @changed OLI 05.01.2009 - Einbau einer M&ouml;glichkeit die Ausgabe der Warnung in der
 *         Methode <TT>toString()</TT> zu unterdr&uuml;cken.
 * @changed OLI 12.03.2009 - Umstellen der Laufzeitausgaben auf log4j.
 * @changed OLI 19.03.2009 - Reaktivierung der Property
 *         "corent.dates.LongPTimestamp.toString.suppress.warning". Hier&uuml;ber kann nun die
 *         Ausgabe in das Log unterdr&uuml;ckt werden.
 * @changed OLI 19.06.2009 - Formatanpassungen.
 * @changed OLI 30.06.2009 - Umsetzung des log4j-Levels f&uuml;r die Warnmeldung in der Methode
 *         <TT>toString()</TT> auf DEBUG.
 *
 */

public class LongPTimestamp implements Comparable, Serializable {

    /** Eine Konstante f&uuml;r einen undefinierten LongPTimestamp. */
    public static final LongPTimestamp NULL = new LongPTimestamp(-1);

    /* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
    private static Logger log = Logger.getLogger(LongPTimestamp.class);

    /* Hier werden die Millisekunden zum PTimestamp festgehalten. */
    private long timestamp = 0;

    /** Erzeugt eine Instanz der Klasse mit Defaultwerten. */
    public LongPTimestamp() {
        super();
        Calendar dt = Calendar.getInstance();
        this.timestamp = (((long) new PDate().toInt()) * 1000000 + ((long) new PTime().toInt()))
                * 1000;
        this.timestamp = this.timestamp + dt.get(Calendar.MILLISECOND);
    }

    private LongPTimestamp(int i) {
        super();
        this.timestamp = -1;
    }

    /**
     * Erzeugt eine neue Instanz mit den Daten des &uuml;bergebenen Objekts.
     *
     * @param tsm Der PTimestamp, dessen Daten &uuml;bernommen werden sollen.
     */
    public LongPTimestamp(TimestampModel tsm) {
        super();
        PTimestamp pts = new PTimestamp(tsm);
        this.timestamp = pts.toLong() * 1000;
    }

    /**
     * Erzeugt eine neue Instanz mit den Daten des &uuml;bergebenen Objekts.
     *
     * @param l Eine long-Zahl mit dem gepackten Datum.
     * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
     */
    public LongPTimestamp(long l) throws ParseException {
        super();
        PTimestamp pts = new PTimestamp((l / 1000));
        this.timestamp = pts.toLong() * 1000 + (l % 1000);
    }

    /**
     * Die Implementierung der Methode wurde anhand einer Vorarbeit von Volodymyr Medvid
     * durchgef&uuml;hrt.
     *
     * @return Die Millisekunden seit 01.01.1970 0:00, bzw. <TT>-1</TT>, wenn es bei der
     *         Berechnung der Millis einen Fehler gibt.
     *
     * @changed OLI 01.09.2008 - Hinzugef&uuml;gt.
     *
     */
    public long getMillis() {
        try {
            Calendar c = GregorianCalendar.getInstance();
            long millis = this.timestamp % 1000;
            PTimestamp pts = new PTimestamp(this.timestamp / 1000);
            c.set(Calendar.YEAR, pts.get(TimestampUnit.YEAR));
            c.set(Calendar.MONTH, pts.get(TimestampUnit.MONTH)-1);
            c.set(Calendar.DAY_OF_MONTH, pts.get(TimestampUnit.DAY));
            c.set(Calendar.HOUR_OF_DAY, pts.get(TimestampUnit.HOUR));
            c.set(Calendar.MINUTE, pts.get(TimestampUnit.MINUTE));
            c.set(Calendar.SECOND, pts.get(TimestampUnit.SECOND));
            c.set(Calendar.MILLISECOND, (int) millis);
            return c.getTimeInMillis();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Liefert einen Long-Wert mit dem Timestamp im Format JJJJMMDDHHMMSSmmm.
     *
     * @return Der Timestamp in der oben angegebenen Ganzzahlrepr&auml;sentation.
     */
    public long toLong() {
        return this.timestamp;
    }


    /* Ueberschreiben von Methoden der Superklasse. */

    public boolean equals(Object o) {
        if (!(o instanceof LongPTimestamp)) {
            return false;
        }
        LongPTimestamp lpts = (LongPTimestamp) o; 
        return this.toLong() == lpts.toLong();
    }

    public int hashCode() {
        return (int) this.toLong();
    }

    /**
     * @changed OLI 05.01.2009 - Einbau einer M&ouml;:glichkeit, die Warnung im Exception-Zweig
     *         zu unterdr&uuml;cken (durch Setzen der Property
     *         <I>corent.dates.LongPTimestamp.toString.suppress.warning</I>).
     * @changed OLI 15.01.2009 - Erweiterung der ausgegebenen Warnung um dem Wert der Variablen
     *         <TT>this.timestamp</TT>.
     * @changed OLI 12.03.2009 - Ersetzen der System.out.println-Ausgabe durch einen
     *         log4j-Eintrag.
     * @changed OLI 30.06.2009 - Umsetzung des log4j-Levels f&uuml;r die Warnmeldung auf DEBUG.
     *
     */
    public String toString() {
        PTimestamp pts = PTimestamp.NULL;
        try {
            pts = new PTimestamp((this.timestamp / 1000));
        } catch (Exception e) {
            if (!Boolean.getBoolean("corent.dates.LongPTimestamp.toString.suppress.warning")) {
                log.debug("PTimestamp can not be created in LongPTimestamp.toString() ("
                        + "timestamp=" + this.timestamp + ").");
            }
            /* OLI 19.06.2009 - Herauskommentiert vor dem 19.06.2009.
            if (!Boolean.getBoolean("corent.dates.LongPTimestamp.toString.suppress.warning")) {
                System.out.println("WARNING: PTimestamp can not be created in "
                        + "LongPTimestamp.toString() (timestamp=" + this.timestamp + ").");
            }
            */
        }
        return pts.toString() + "." + (this.timestamp % 1000);
    }


    /* Ueberschreiben von statischen Methoden der Superklasse. */

    /**
     * Diese Methode kann zum Test der Klasse aufgerufen werden.
     *
     * @param args Die Kommandozeilenparameter des Aufrufs (hier unwirksam).
     *
     * @changed OLI 01.09.2008 - Hinzugef&uuml;gt.
     *
     */
    public static void main(String[] args) {
        System.out.println("\nMillis ...");
        try {
            System.out.println("01.01.1970 01:00:00 - " + new LongPTimestamp(19700101010000000l
                    ).getMillis());
            Calendar c = GregorianCalendar.getInstance();
            c.set(Calendar.YEAR, 1970);
            c.set(Calendar.MONTH, 0);
            c.set(Calendar.DAY_OF_MONTH, 1);
            c.set(Calendar.HOUR_OF_DAY, 1);
            c.set(Calendar.MINUTE, 0);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            System.out.println(" same with Calendar - " + c.getTimeInMillis());
            System.out.println("01.01.1970 01:01:00 - " + new LongPTimestamp(19700101010100000l
                    ).getMillis());
            System.out.println("07.04.2000 23:15:00 - " + new LongPTimestamp(20000407231500000l
                    ).getMillis());
            c = GregorianCalendar.getInstance();
            c.set(Calendar.YEAR, 2000);
            c.set(Calendar.MONTH, 3);
            c.set(Calendar.DAY_OF_MONTH, 7);
            c.set(Calendar.HOUR_OF_DAY, 23);
            c.set(Calendar.MINUTE, 15);
            c.set(Calendar.SECOND, 0);
            c.set(Calendar.MILLISECOND, 0);
            System.out.println(" same with Calendar - " + c.getTimeInMillis());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* Implementierung des Interfaces Comparable. */

    public int compareTo(Object o) {
        LongPTimestamp lpts = (LongPTimestamp) o;
        return (int) (this.toLong() - lpts.toLong());
    }

}
