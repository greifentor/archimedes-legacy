/*
 * SystemPTimestampFactory.java
 *
 * 08.08.2011
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import java.util.*;

import org.apache.log4j.*;


/**
 * Eine Implementierung der <TT>PTimestampFactory</TT>, die mit der Systemzeit arbeitet. Die
 * Verarbeitung findet &uuml;ber die <TT>Calendar</TT>-Klasse statt.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.08.2011 - Hinzugef&uuml;gt.
 */

public class SystemPTimestampFactory implements PTimestampFactory {

    private static final Logger LOG = Logger.getLogger(SystemPTimestampFactory.class);

    /**
     * Liefert eine Longzahl im Format JJJJMMTTHHmmSS mit den Daten des &uuml;bergebenen
     * Calendars, vorausgesetzt, da&szlig; der Calendar ein g&uuml;ltiges Datum enth&auml;lt.
     *
     * @param cal Der Calendar, zu dem die entsprechende Longzahl ermittelt werden soll.
     * @throws IllegalArgumentException Falls der Inhalt des &uuml;bergebenen Java-Date-Objektes
     *         kein valides Datum enth&auml;lt.
     * @throws NullPointerException Falls der Calendar als <TT>null</TT>-Referenz &uuml;bergeben
     *         wird.
     *
     * <P><I><B>Hinweis:</B> Die Exception wird niemals ausgel&ouml;st, weil der Calendar ein
     * falsches Datum in ein valides umrechnet (aus dem 32.03. wird beispielsweise der 01.04).
     * </I>
     *
     * @precondition cal != <TT>null</TT>
     *
     * @changed OLI 21.09.2009 - Auf Basis des Konstruktors <TT>PTimestamp(Date)</TT>
     *         hinzugef&uuml;gt.
     */
    public static long getLong(Calendar cal) throws IllegalArgumentException,
            NullPointerException {
        long result = -1;
        /*
        if (TimestampUtil.validate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1,
                    cal.get(Calendar.YEAR), cal.get(Calendar.HOUR), cal.get(Calendar.MINUTE),
                    cal.get(Calendar.SECOND))) {
                    */
            result = (cal.get(Calendar.YEAR) * 10000000000L) + ((cal.get(Calendar.MONTH)+1)
                    * 100000000L) + (cal.get(Calendar.DAY_OF_MONTH) * 1000000L) + (cal.get(
                    Calendar.HOUR_OF_DAY) * 10000L) + (cal.get(Calendar.MINUTE) * 100L)
                    + cal.get(Calendar.SECOND);
            LOG.debug("PTimestamp> " + result);
            LOG.debug("cal.year  > " + cal.get(Calendar.YEAR) * 10000000000L);
            LOG.debug("cal.month > " + (cal.get(Calendar.MONTH)+1) * 100000000L);
            LOG.debug("cal.day   > " + cal.get(Calendar.DAY_OF_MONTH) * 1000000L);
            LOG.debug("cal.hour  > " + cal.get(Calendar.HOUR_OF_DAY) * 10000);
            LOG.debug("cal.minute> " + cal.get(Calendar.MINUTE) * 100);
            LOG.debug("cal.second> " + cal.get(Calendar.SECOND));
            return result;
        // }
        /*
         * OLI 23.07.2009 - Das hier kann nicht eintreten, da der Calendar ein falsches
         *         Datum entsprechend umrechnet (aus dem 32.03. wird beispielsweise der
         *         01.04).
         * /
        throw new IllegalArgumentException("date parameter not valid: "
                + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH)+1) + "."
                + cal.get(Calendar.YEAR) + "  " + cal.get(Calendar.HOUR_OF_DAY) + ":" + 
                + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));
         */
    }

    /**
     * @changed OLI 08.08.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public long getLong() {
        return getLong(Calendar.getInstance());
    }

    /**
     * @changed OLI 08.08.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public long getLong(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getLong(cal);
    }

}