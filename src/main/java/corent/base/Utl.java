/*
 * Utl.java
 *
 * 06.02.2008
 *
 * (c) by O.Lieshoff
 *
 */

package corent.base;


import java.util.*;

import org.apache.log4j.*;


/**
 * Diese Klasse enth&auml;lt eine Sammlung von Utility-Methoden, die keinem direkten Gebiet
 * zuzuordnen sind.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.02.2008 - Hinzugef&uuml;gt.
 * @changed OLI 14.03.2008 - Erweiterung um den Methodensatz <TT>GetProperty(...)</TT> zum
 *         Auslesen von String-Properties im HTML-Format.
 * @changed OLI 15.06.2009 - Formatanpassungen und Anpassungen an log4j.
 *
 */

public class Utl {

    /* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
    private static Logger log = Logger.getLogger(StrUtil.class); 

    /**
     * Diese Methoden vergleicht zwei Objektreferenzen miteinander. Die Ergebnisse des
     * Vergleichs sind unter "return" zu finden.
     *
     * @param o0 Objektreferenz eins.
     * @param o1 Objektreferenz zwei.
     * @return <TT>-1</TT>, wenn o0 ungleich <TT>null</TT> und o1 gleich <TT>null</TT>,
     *         <TT>0</TT>, wenn o0 gleich <TT>null</TT> und o1 gleich <TT>null</TT>,
     *         <TT>1</TT>, wenn o0 gleich <TT>null</TT> und o1 ungleich <TT>null</TT>,
     *         <TT>Integer.MAX_VALUE</TT>, wenn beide Objekte ungleich <TT>null</TT> sind.
     */
    public static int CompareNullObj(Object o0, Object o1) {
        if ((o0 != null) && (o1 == null)) {
            return -1;
        } else if ((o0 == null) && (o1 == null)) {
            return 0;
        } else if ((o0 == null) && (o1 != null)) {
            return 1;
        }
        return Integer.MAX_VALUE;
    }

    /**
     * Diese Methoden vergleicht zwei Comparables miteinander. Die Ergebnisse des
     * Vergleichs sind unter "return" zu finden.
     *
     * @param c0 Objektreferenz eins.
     * @param c1 Objektreferenz zwei.
     * @return <TT>-1</TT>, wenn c0 ungleich <TT>null</TT> und c1 gleich <TT>null</TT>,
     *         <TT>0</TT>, wenn c0 gleich <TT>null</TT> und c1 gleich <TT>null</TT>,
     *         <TT>1</TT>, wenn c0 gleich <TT>null</TT> und c1 ungleich <TT>null</TT>,
     *         das Ergebnis von <TT>c0.compareTo(c1)</TT>, wenn beide Objekte ungleich
     *         <TT>null</TT> sind.
     */
    public static int CompareNull(Comparable c0, Comparable c1) {
        if ((c0 != null) && (c1 == null)) {
            return -1;
        } else if ((c0 == null) && (c1 == null)) {
            return 0;
        } else if ((c0 == null) && (c1 != null)) {
            return 1;
        }
        return c0.compareTo(c1);
    }

    /**
     * Diese Methode liest eine Property mit dem angegebenen Namen aus dem &Uuml;bergebenen 
     * Property-Set und wandelt deren Inhalt, sofern er nicht <TT>null</TT> ist, aus 
     * HTML-Kodierung in einen Java-String um. Hierbei kommt die Methode 
     * <TT>corent.base.StrUtil.FromHTML(String)</TT> zum Einsatz.
     *
     * @param p Das Property-Set, aus dem die Property gelesen werden soll.
     * @param n Der Name der Property, die aus dem angegebenen Property-Set gelesen werden soll.
     * @param dv Der Defaultwert, der zu&uuml;ckgegeben werden soll, wenn die Property nicht
     *         gesetzt ist.
     * @return Der aus der HTML-Kodierung gewandelte Inhalt der Property.
     *
     * @changed OLI 14.03.2008 - Hinzugef&uuml;gt.
     *
     */
    public static String GetProperty(Properties p, String n, String dv) {
        String s = p.getProperty(n, dv);
        if (s != null) {
            s = StrUtil.FromHTML(s);
        }
        log.debug("getting property " + n + " = " + s);
        return s;
    }

    /**
     * Diese Methode liest eine Property mit dem angegebenen Namen aus dem &Uuml;bergebenen 
     * Property-Set und wandelt deren Inhalt, sofern er nicht <TT>null</TT> ist, aus 
     * HTML-Kodierung in einen Java-String um. Hierbei kommt die Methode 
     * <TT>corent.base.StrUtil.FromHTML(String)</TT> zum Einsatz.
     * <BR>In dieser Variante wird kein Defaultwert angegeben. Anstelle dessen wird der Wert 
     * <TT>null</TT> angenommen.
     *
     * @param p Das Property-Set, aus dem die Property gelesen werden soll.
     * @param n Der Name der Property, die aus dem angegebenen Property-Set gelesen werden soll.
     * @return Der aus der HTML-Kodierung gewandelte Inhalt der Property.
     *
     * @changed OLI 14.03.2008 - Hinzugef&uuml;gt.
     *
     */
    public static String GetProperty(Properties p, String n) {
        return GetProperty(p, n, null);
    }

    /**
     * Diese Methode liest eine Property mit dem angegebenen Namen aus Systemproperties und 
     * wandelt deren Inhalt, sofern er nicht <TT>null</TT> ist, aus HTML-Kodierung in einen 
     * Java-String um. Hierbei kommt die Methode <TT>corent.base.StrUtil.FromHTML(String)</TT> 
     * zum Einsatz.
     *
     * @param n Der Name der Property, die aus dem angegebenen Property-Set gelesen werden soll.
     * @param dv Der Defaultwert, der zu&uuml;ckgegeben werden soll, wenn die Property nicht
     *         gesetzt ist.
     * @return Der aus der HTML-Kodierung gewandelte Inhalt der Property.
     *
     * @changed OLI 14.03.2008 - Hinzugef&uuml;gt.
     *
     */
    public static String GetProperty(String n, String dv) {
        return GetProperty(System.getProperties(), n, dv);
    }

    /**
     * Diese Methode liest eine Property mit dem angegebenen Namen aus Systemproperties und 
     * wandelt deren Inhalt, sofern er nicht <TT>null</TT> ist, aus HTML-Kodierung in einen 
     * Java-String um. Hierbei kommt die Methode <TT>corent.base.StrUtil.FromHTML(String)</TT> 
     * zum Einsatz.
     * <BR>In dieser Variante wird kein Defaultwert angegeben. Anstelle dessen wird der Wert 
     * <TT>null</TT> angenommen.
     *
     * @param n Der Name der Property, die aus dem angegebenen Property-Set gelesen werden soll.
     * @return Der aus der HTML-Kodierung gewandelte Inhalt der Property.
     *
     * @changed OLI 14.03.2008 - Hinzugef&uuml;gt.
     *
     */
    public static String GetProperty(String n) {
        return GetProperty(System.getProperties(), n, null);
    }

}
