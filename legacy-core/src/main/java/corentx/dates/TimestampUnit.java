/*
 * TimestampUnit.java
 *
 * 21.07.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;


/**
 * Dieser Enum definiert die f&uuml;r die g&uuml;ltigen Zeiteinheiten, wie z. B. Sekunden,
 * Minuten oder auch Tage.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.07.2009 - Hinzugef&uuml;gt
 * @changed OLI 30.07.2009 - Erweiterung um den Konstruktor und die Methoden
 *         <TT>getPredUnits()</TT> und <TT>pred()</TT>.
 * @changed OLI 05.08.2009 - &Auml;nderung an der Unit-Logik. Es werden nun die Maximalwerte
 *         f&uuml;r Einheiten der Zeiteinheit angegeben. Zudem wird nun der Nachfolger der
 *         Zeiteinheit referenziert, sofern es einen gibt. &Auml;nderung der Methodik der
 *         Methode <TT>getPred()</TT> (Verkn&uuml;pfungen (Vorg&auml;nger und Nachfolger) werden
 *         nun &uuml;ber die Ordinaltzahlen ermittelt).
 *
 */

public enum TimestampUnit {

    /** Bezeichner f&uuml;r die Zeiteinheit Millisekunde. */
    MILLISECOND(1000),
    /** Bezeichner f&uuml;r die Zeiteinheit Sekunde. */
    SECOND(60),
    /** Bezeichner f&uuml;r die Zeiteinheit Minute. */
    MINUTE(60),
    /** Bezeichner f&uuml;r die Zeiteinheit Stunde. */
    HOUR(24),
    /** Bezeichner f&uuml;r die Zeiteinheit Tag. */
    DAY(1, -1),
    /** Bezeichner f&uuml;r die Zeiteinheit Monat. */
    MONTH(1, 12),
    /** Bezeichner f&uuml;r die Zeiteinheit Jahr. */
    YEAR(1, Long.MAX_VALUE);

    private long maxUnits = Long.MAX_VALUE;
    private long minUnits = 0;

    private TimestampUnit(long maxUnits) {
        this.maxUnits = maxUnits;
    }

    private TimestampUnit(long minUnits, long maxUnits) {
        this.maxUnits = maxUnits;
        this.minUnits = minUnits;
    }

    /**
     * Liefert die Anzahl der Einheiten der vorliegenden Zeiteinheit, die ben&uouml;tigt wird,
     * um eine Einheit der n&auml;chst h&ouml;heren Zeiteinheit zu f&uuml;llen (z. B. 60
     * f&uuml;r die Zeiteinheit MINUTE, da 60 Minuten gebraucht werden, um eine Stunde zu
     * f&uuml;llen).
     *
     * @return Die Anzahl der Einheiten der vorliegenden Zeiteinheit, die ben&uouml;tigt wird,
     *         um eine Einheit der n&auml;chst h&ouml;heren Zeiteinheit zu f&uuml;llen (z. B. 60
     *         f&uuml;r die Zeiteinheit MINUTE, da 60 Minuten gebraucht werden, um eine Stunde
     *         zu f&uuml;llen). Der Wert <TT>-1</TT> weist darauf hin, da&szlig; eine Berechnung
     *         zur Feststellung der notwendigen Zeiteinheiten erforderlich ist. Der Wert
     *         <TT>Long.MAX_VALUE</TT> zeigt an, da&szlig; es keine n&auml;chst h&ouml;here
     *         Einheit mehr gibt (nur bei YEAR der Fall).
     *
     * @changed OLI 05.08.2009 - Hinzugef&uuml;gt.
     *
     */
    public long getMaxUnits() {
        return this.maxUnits;
    }

    /**
     * Liefert die Zahl, mit der die angegebene Zeiteinheit ihre Z&auml;hlung beginnt (z. B. 0
     * f&uuml;r Sekunden, 1 f&uuml;r Monate).
     *
     * @return die Zahl, mit der die angegebene Zeiteinheit ihre Z&auml;hlung beginnt (z. B. 0
     *         f&uuml;r Sekunden, 1 f&uuml;r Monate).
     *
     * @changed OLI 05.08.2009 - Hinzugef&uuml;gt.
     *
     */
    public long getMinUnits() {
        return this.minUnits;
    }

    /**
     * Liefert die n&auml;chst kleinere Zeiteinheit.
     *
     * @return Die n&auml;chst kleinere Zeiteinheit bzw. <TT>null</TT>, wenn es keine
     *         n&auml;chst kleinere Zeiteinheit gibt.
     *
     * @changed OLI 30.07.2009 - Hinzugef&uuml;gt.
     *
     */
    public TimestampUnit getPred() {
        int ord = this.ordinal();
        if (ord > 0) {
            return TimestampUnit.getTimestampUnit(ord-1);
        }
        return null;
    }

    /**
     * Liefert die Anzahl der Einheiten mit der n&auml;chst niedrigen Ordinalzahl, die
     * n&ouml;tig sind, um eine vorliegende Einheit zu erhalten (z. B. 60 Sekunden sind eine
     * Minute; f&uuml;r den Enum MINUTE w&uuml;rde die Methode den Wert 60 zur&uuml;ckliefern).
     *
     * @return Die Anzahl der Teilheiten der n&auml;chst niedrigeren Einheit, die n&ouml;tig
     *         sind, um eine der vorliegenden Einheiten komplett zu f&uuml;llen. Der Wert
     *         <TT>-1</TT> weist darauf hin, da&szlig; eine Berechnung zur Feststellung der
     *         notwendigen Zeiteinheiten erforderlich ist. Der Wert <TT>Long.MAX_VALUE</TT>
     *         zeigt an, da&szlig; es keine n&auml;chst kleinere Einheit mehr gibt (nur bei
     *         MILLISECONDS der Fall).
     *
     * @changed OLI 30.07.2009 - Hinzugef&uuml;gt.
     *
     */
    public long getPredUnits() {
        if (this.getPred() == null) {
            return 0;
        }
        return this.getPred().getMaxUnits();
    }

    /**
     * Liefert die n&auml;chst h&ouml;here Zeiteinheit.
     *
     * @return Die n&auml;chst h&ouml;here Zeiteinheit bzw. <TT>null</TT>, wenn es keine
     *         n&auml;chst h&ouml;here Zeiteinheit gibt.
     *
     * @changed OLI 05.08.2009 - Hinzugef&uuml;gt.
     *
     */
    public TimestampUnit getSucc() {
        int ord = this.ordinal();
        if (ord < YEAR.ordinal()) {
            return TimestampUnit.getTimestampUnit(ord+1);
        }
        return null;
    }

    /**
     * Liefert die TimestampUnit zur angegebenen Ordinalzahl.
     *
     * @param n Die Ordinalzahl, zu der die TimestampUnit zur&uuml;ckgeliefert werden soll.
     * @return Die TimestampUnit zur angegebenen Ordinalzahl.
     * @throws ArrayIndexOutOfBounds Falls die Ordinalzahl au&szlig;erhalb des g&uuml;ltigen
     *         Bereiches liegt.
     *
     * @precondition n &gt;= 0 &amp;&amp; n &lt; YEAR.ordinal().
     *
     * @changed OLI 05.08.2009 - Hinzugef&uuml;gt.
     *
     */
    public static TimestampUnit getTimestampUnit(int n) throws IllegalArgumentException {
        return TimestampUnit.values()[n];
    }

}
