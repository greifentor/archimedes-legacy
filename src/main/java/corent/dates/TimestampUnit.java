/*
 * TimestampUnit.java
 *
 * 05.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.dates;


import java.util.*;


/**
 * Diese Klasse ist eine Implementierung des typsicheren Enum-Musters. Sie dient der Darstellung
 * der Zeiteinheiten (Jahr, Monat, Tag, Stunde usw.), die im Zusammenhang mit dem TimestampModel
 * zum Einsatz kommen.
 *
 * @author O.Lieshoff
 *
 */

public final class TimestampUnit implements Comparable {

    /* Die Liste der &g&uuml;ltigen TimestampUnits. */
    private static final Vector TimestampUnits = new Vector();
    
    /** Konstante f&uuml;r die Zeiteinheit: Jahr. */    
    public static final TimestampUnit YEAR = new TimestampUnit("YEAR", 0);
    /** Konstante f&uuml;r die Zeiteinheit: Monat. */    
    public static final TimestampUnit MONTH = new TimestampUnit("MONTH", 1);
    /** Konstante f&uuml;r die Zeiteinheit: Tag. */    
    public static final TimestampUnit DAY = new TimestampUnit("DAY", 2);
    /** Konstante f&uuml;r die Zeiteinheit: Stunde. */    
    public static final TimestampUnit HOUR = new TimestampUnit("HOUR", 3);
    /** Konstante f&uuml;r die Zeiteinheit: Minute. */    
    public static final TimestampUnit MINUTE = new TimestampUnit("MINUTE", 4);
    /** Konstante f&uuml;r die Zeiteinheit: Sekunde. */    
    public static final TimestampUnit SECOND = new TimestampUnit("SECOND", 5);
    /** Konstante f&uuml;r die Zeiteinheit: Millisekunde. */    
    public static final TimestampUnit MILLI = new TimestampUnit("MILLI", 6);

    private String name = null;
    private int index = -1;
    
    /**
     * Liefert eine TimestampUnit zur angegebenen Ordnungszahl
     *
     * @param n Die Ordnungszahl, zu der die entsprechende TimestampUnit ermittelt werden 
     *     soll.
     * @return Die TimestampUnit zur Ordnungszahl.
     * @throws ArrayIndexOutOfBoundsException Falls der Ordnungszahl keine TimestampUnit 
     *     zugeordnet ist. 
     */
    public static TimestampUnit GetTimestampUnit(int n) throws ArrayIndexOutOfBoundsException {
        for (int i = 0, len = TimestampUnits.size(); i < len; i++) {
            TimestampUnit tsu = (TimestampUnit) TimestampUnits.elementAt(i);
            if (tsu.ord() == n) {
                return tsu;
            }
        }
        throw new ArrayIndexOutOfBoundsException("TimestampUnit for index (" + n + ") not "
                + "found!");
    }
    
    /**
     * Liefert eine TimestampUnit zur angegebenen Ordnungszahl
     *
     * @param n Der Name der TimestampUnit, die geliefert werden soll.
     * @return Die TimestampUnit zum Namen.
     * @throws ArrayIndexOutOfBoundsException Falls der Ordnungszahl keine TimestampUnit 
     *     zugeordnet ist. 
     */
    public static TimestampUnit GetTimestampUnit(String n) 
            throws ArrayIndexOutOfBoundsException {
        for (int i = 0, len = TimestampUnits.size(); i < len; i++) {
            TimestampUnit tsu = (TimestampUnit) TimestampUnits.elementAt(i);
            if (tsu.getName().equalsIgnoreCase(n)) {
                return tsu;
            }
        }
        throw new ArrayIndexOutOfBoundsException("TimestampUnit for index (" + n + ") not "
                + "found!");
    }
    
    /** @return Maximale, g&uuml;ltige Ordnungszahl f&uuml;r TimestampUnits. */
    public static int MaxTimestampUnit() {
        return TimestampUnits.size();
    }
    
    private TimestampUnit(String name, int ord) {
        super();
        this.name = name;
        this.index = ord;
        TimestampUnits.addElement(this);
    }
    
    /** @return Die Ordnungszahl der TimestampUnit. */
    public int ord() {
        return this.index;
    }
    
    /** @return Der Name der TimestampUnit. */
    public String getName() {
        return this.name;
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public String toString() {
        return this.name;
    }
    
    
    /* Implementierung des Interfaces Comparable. */
    
    public int compareTo(Object o) {
        TimestampUnit tsu = (TimestampUnit) o;
        return this.ord() - tsu.ord();
    }
    
}
