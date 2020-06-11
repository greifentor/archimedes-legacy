/*
 * SystemPDateFactory.java
 *
 * 09.08.2011
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


import java.util.*;

import org.apache.log4j.*;


/**
 * Eine auf der Systemuhr basierende Implementierung des Interfaces <TT>PDateFactory</TT>. Die
 * Verarbeitung findet &uuml;ber die <TT>Calendar</TT>-Klasse statt.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 09.08.2011 - Hinzugef&uuml;gt.
 */

public class SystemPDateFactory implements PDateFactory {

    private static final Logger LOG = Logger.getLogger(SystemPDateFactory.class);

    /**
     * Liefert eine Integerzahl im Format JJJJMMTT mit den Daten des &uuml;bergebenen Calendars,
     * vorausgesetzt, da&szlig; der Calendar ein g&uuml;ltiges Datum enth&auml;lt.
     *
     * @param cal Der Calendar, zu dem die entsprechende Integerzahl ermittelt werden soll.
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
     * @changed OLI 21.09.2009 - Auf Basis des Konstruktors <TT>PDate(Date)</TT>
     *         hinzugef&uuml;gt.
     * @changed OLI 29.10.2010 - R&uuml;ckbau der Absicherungen, da das <TT>Calendar</TT>-Objekt
     *         immer einen g&uuml;tigen Zustand hat.
     */
    public static int getInt(Calendar cal) throws IllegalArgumentException, NullPointerException
            {
        int result = -1;
        // if (TimestampUtil.validate(cal.get(Calendar.DAY_OF_MONTH), cal.get(Calendar.MONTH)+1,
        //             cal.get(Calendar.YEAR))) {
            result = (cal.get(Calendar.YEAR) * 10000) + ((cal.get(Calendar.MONTH)+1) * 100)
                    + cal.get(Calendar.DAY_OF_MONTH);
            LOG.debug("PDate     > " + result);
            LOG.debug("cal.year  > " + cal.get(Calendar.YEAR) * 10000);
            LOG.debug("cal.month > " + (cal.get(Calendar.MONTH)+1) * 100);
            LOG.debug("cal.day   > " + cal.get(Calendar.DAY_OF_MONTH));
            return result;
        // }
        /*
         * OLI 23.07.2009 - Das hier kann nicht eintreten, da der Calendar ein falsches
         *         Datum entsprechend umrechnet (aus dem 32.03. wird beispielsweise der
         *         01.04).
         * /
        throw new IllegalArgumentException("date parameter not valid: "
                + cal.get(Calendar.DAY_OF_MONTH) + "." + (cal.get(Calendar.MONTH)+1) + "."
                + cal.get(Calendar.YEAR));
         */
    }

    /**
     * @changed OLI 09.08.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public long getLong() {
        return getInt(Calendar.getInstance());
    }

    /**
     * @changed OLI 09.08.2011 - Hinzugef&uuml;gt.
     */
    @Override
    public long getLong(Date date) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        return getInt(cal);
    }

}