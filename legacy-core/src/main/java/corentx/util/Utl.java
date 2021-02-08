/*
 * Utl.java
 *
 * 06.02.2008
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import logging.Logger;

/**
 * Diese Klasse enth&auml;lt eine Sammlung von Utility-Methoden, die keinem direkten Gebiet zuzuordnen sind.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.02.2008 - Hinzugef&uuml;gt.
 * @changed OLI 14.03.2008 - Erweiterung um den Methodensatz <TT>getProperty(...)</TT> zum Auslesen von
 *          String-Properties im HTML-Format.
 * @changed OLI 15.06.2009 - Formatanpassungen und Anpassungen an log4j.
 * @changed OLI 12.08.2009 - &Uuml;bertragung nach corentx.
 * @changed OLI 11.09.2009 - Erweiterung um die Methoden <TT>readProperties(String)</TT> und
 *          <TT>readProperties(Properties, String)</TT>.
 * @changed OLI 07.10.2009 - Erweiterung um die Methode <TT>equals(Object, Object)</TT>.
 * @changed OLI 05.02.2010 - Korrektur der <TT>null</TT>-Logik in den Methoden <TT>compareNullRef(Object, Object)</TT>
 *          und <TT>compareRef(Comparable,
 *         Comparable)</TT>.
 */

public class Utl {

	/**
	 * Es ist vollkommen sinnfrei, die Klasse zu instanzieren.
	 *
	 * @throws UnsupportedOperationException Falls der Konstructor aufgerufen wird.
	 *
	 * @changed OLI 28.02.2010 - Hinzufgef&uuml;gt.
	 */
	public Utl() {
		throw new UnsupportedOperationException("it makes no sense to create an instance of " + "this static class.");
	}

	/* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
	private static Logger log = Logger.getLogger(Utl.class);

	/**
	 * Diese Methoden vergleicht zwei Objektreferenzen miteinander. Die Ergebnisse des Vergleichs sind unter "return" zu
	 * finden.
	 *
	 * @param o0 Objektreferenz eins.
	 * @param o1 Objektreferenz zwei.
	 * @return <TT>-1</TT>, wenn o0 gleich <TT>null</TT> und o1 ungleich <TT>null</TT>, <TT>0</TT>, wenn o0 gleich
	 *         <TT>null</TT> und o1 gleich <TT>null</TT>, <TT>1</TT>, wenn o0 ungleich <TT>null</TT> und o1 gleich
	 *         <TT>null</TT>, <TT>Integer.MAX_VALUE</TT>, wenn beide Objekte ungleich <TT>null</TT> sind.
	 *
	 * @changed OLI 05.02.2010 - Korrektur der <TT>null</TT>-Logik. Der <TT>null</TT>-Wert ist nun kleiner als ein
	 *          belegter Wert.
	 */
	public static int compareNullRef(Object o0, Object o1) {
		if ((o0 != null) && (o1 == null)) {
			return 1;
		} else if ((o0 == null) && (o1 == null)) {
			return 0;
		} else if ((o0 == null) && (o1 != null)) {
			return -1;
		}
		return Integer.MAX_VALUE;
	}

	/**
	 * Diese Methoden vergleicht zwei Comparables miteinander. Die Ergebnisse des Vergleichs sind unter "return" zu
	 * finden.
	 *
	 * @param c0 Objektreferenz eins.
	 * @param c1 Objektreferenz zwei.
	 * @return <TT>-1</TT>, wenn c0 gleich <TT>null</TT> und c1 ungleich <TT>null</TT>, <TT>0</TT>, wenn c0 gleich
	 *         <TT>null</TT> und c1 gleich <TT>null</TT>, <TT>1</TT>, wenn c0 ungleich <TT>null</TT> und c1 gleich
	 *         <TT>null</TT>, das Ergebnis von <TT>c0.compareTo(c1)</TT>, wenn beide Objekte ungleich <TT>null</TT>
	 *         sind.
	 * @changed OLI 05.02.2010 - Korrektur der <TT>null</TT>-Logik. Der <TT>null</TT>-Wert ist nun kleiner als ein
	 *          belegter Wert.
	 */
	public static int compareRef(Comparable c0, Comparable c1) {
		if ((c0 != null) && (c1 == null)) {
			return 1;
		} else if ((c0 == null) && (c1 == null)) {
			return 0;
		} else if ((c0 == null) && (c1 != null)) {
			return -1;
		}
		return c0.compareTo(c1);
	}

	/**
	 * Vergleicht die beiden angegebenen Objekte auf inhaltliche Gleichheit. Im Gegensatz zur Methode
	 * <TT>equals(Object)</TT> aus der <TT>Object</TT>-Klasse, wird diese Implementierung auch mit
	 * <TT>null</TT>-Referenzen fertig.
	 *
	 * @param o0 Das eine zu vergleichende Objekt.
	 * @param o1 Das andere zu vergleichende Object.
	 * @return <TT>true</TT>, falls beide Objekt inhaltlich gleich oder beide Objektreferenzen <TT>null</TT>-Referenzen
	 *         sind; <BR>
	 *         <TT>false</TT> sonst.
	 *
	 * @changed OLI 07.10.2009 - Hinzugef&uuml;gt.
	 */
	public static boolean equals(Object o0, Object o1) {
		if (o0 == o1) {
			return true;
		}
		if (((o0 != null) && (o1 == null)) || ((o0 == null) && (o1 != null))) {
			return false;
		}
		return o0.equals(o1);
	}

	/**
	 * Diese Methode liest eine Property mit dem angegebenen Namen aus dem &Uuml;bergebenen Property-Set und wandelt
	 * deren Inhalt, sofern er nicht <TT>null</TT> ist, aus HTML-Kodierung in einen Java-String um. Hierbei kommt die
	 * Methode <TT>corentx.util.Str.FromHTML(String)</TT> zum Einsatz.
	 *
	 * @param p  Das Property-Set, aus dem die Property gelesen werden soll.
	 * @param n  Der Name der Property, die aus dem angegebenen Property-Set gelesen werden soll.
	 * @param dv Der Defaultwert, der zu&uuml;ckgegeben werden soll, wenn die Property nicht gesetzt ist.
	 * @return Der aus der HTML-Kodierung gewandelte Inhalt der Property.
	 * @throws NullPointerException Falls das Property-Set oder der Property-Name als <TT>null</TT>-Referenz
	 *                              &uuml;bergeben werden.
	 * @precondition p != <TT>null</TT> &amp;&amp; n != <TT>null</TT>
	 *
	 * @changed OLI 14.03.2008 - Hinzugef&uuml;gt.
	 *
	 */
	public static String getProperty(Properties p, String n, String dv) throws NullPointerException {
		String s = p.getProperty(n, dv);
		if (s != null) {
			s = Str.fromHTML(s);
		}
		log.debug("getting property " + n + " = " + s);
		return s;
	}

	/**
	 * Diese Methode liest eine Property mit dem angegebenen Namen aus dem &Uuml;bergebenen Property-Set und wandelt
	 * deren Inhalt, sofern er nicht <TT>null</TT> ist, aus HTML-Kodierung in einen Java-String um. Hierbei kommt die
	 * Methode <TT>corentx.util.Str.FromHTML(String)</TT> zum Einsatz. <BR>
	 * In dieser Variante wird kein Defaultwert angegeben. Anstelle dessen wird der Wert <TT>null</TT> angenommen.
	 *
	 * @param p Das Property-Set, aus dem die Property gelesen werden soll.
	 * @param n Der Name der Property, die aus dem angegebenen Property-Set gelesen werden soll.
	 * @return Der aus der HTML-Kodierung gewandelte Inhalt der Property.
	 * @throws NullPointerException Falls das Property-Set oder der Property-Name als <TT>null</TT>-Referenz
	 *                              &uuml;bergeben werden.
	 * @precondition p != <TT>null</TT> &amp;&amp; n != <TT>null</TT>
	 *
	 * @changed OLI 14.03.2008 - Hinzugef&uuml;gt.
	 *
	 */
	public static String getProperty(Properties p, String n) {
		return getProperty(p, n, null);
	}

	/**
	 * Diese Methode liest eine Property mit dem angegebenen Namen aus Systemproperties und wandelt deren Inhalt, sofern
	 * er nicht <TT>null</TT> ist, aus HTML-Kodierung in einen Java-String um. Hierbei kommt die Methode
	 * <TT>corentx.util.Str.FromHTML(String)</TT> zum Einsatz. <BR>
	 * In dieser Variante wird kein Defaultwert angegeben. Anstelle dessen wird der Wert <TT>null</TT> angenommen.
	 *
	 * @param n Der Name der Property, die aus dem angegebenen Property-Set gelesen werden soll.
	 * @return Der aus der HTML-Kodierung gewandelte Inhalt der Property.
	 * @throws NullPointerException Falls der Property-Name als <TT>null</TT>-Referenz &uuml;bergeben werden.
	 * @precondition n != <TT>null</TT>
	 *
	 * @changed OLI 14.03.2008 - Hinzugef&uuml;gt.
	 *
	 */
	public static String getProperty(String n) {
		return getProperty(System.getProperties(), n, null);
	}

	/**
	 * Diese Methode liest eine Property mit dem angegebenen Namen aus Systemproperties und wandelt deren Inhalt, sofern
	 * er nicht <TT>null</TT> ist, aus HTML-Kodierung in einen Java-String um. Hierbei kommt die Methode
	 * <TT>corentx.util.Str.FromHTML(String)</TT> zum Einsatz.
	 *
	 * @param n  Der Name der Property, die aus dem angegebenen Property-Set gelesen werden soll.
	 * @param dv Der Defaultwert, der zu&uuml;ckgegeben werden soll, wenn die Property nicht gesetzt ist.
	 * @return Der aus der HTML-Kodierung gewandelte Inhalt der Property.
	 * @throws NullPointerException Falls der Property-Name als <TT>null</TT>-Referenz &uuml;bergeben werden.
	 * @precondition n != <TT>null</TT>
	 *
	 * @changed OLI 14.03.2008 - Hinzugef&uuml;gt.
	 *
	 */
	public static String getProperty(String n, String dv) {
		return getProperty(System.getProperties(), n, dv);
	}

	/**
	 * Diese Methode ersetzt den angegebenen String durch den Inhalt der angegebenen Property die, falls existent, gegen
	 * den Inhalt einer Property mit dem um den angegebenen Suffix erweiterten Namen ersetzt wird.
	 *
	 * @param pn     Der Name der Property.
	 * @param def    Ein Defaultwert, falls die Property "pn" nicht definiert ist.
	 * @param suffix Der Suffix, der zus&auml;tzlich in der Form "pn" + "." + "suffix" untersucht werden soll. Existiert
	 *               eine Property mit einem solchen Namen, so deren Wert zur&uuml;ckgegeben.
	 * @throws NullPointerException Falls der Name der Property oder der Suffix als <TT>null</TT>-Referenz
	 *                              &uuml;bergeben werden.
	 * @precondition pn != <TT>null</TT> &amp;&amp; suffix != <TT>null</TT>
	 *
	 * @changed OLI 13.08.2009 - Aus <TT>Str</TT> &uuml;bernommen.
	 *
	 */
	public static String getProperty(String pn, String def, String suffix) throws NullPointerException {
		String p = System.getProperty(pn, def);
		if (suffix == null) {
			throw new NullPointerException("suffix can not be null.");
		}
		if (System.getProperty(pn + "." + suffix) != null) {
			p = System.getProperty(pn + "." + suffix);
		}
		return p;
	}

	/**
	 * Liefert einen hashCode f&uuml;r einen boolean-Wert.
	 *
	 * @param b Der boolean-Wert, zu dem ein hashCode geliefert werden soll.
	 * @return Ein hashCode zum angegebenen boolean-Wert.
	 *
	 * @changed OLI 12.03.2013 - Added.
	 */
	public static int hashCode(boolean b) {
		return (b ? 1 : 0);
	}

	/**
	 * Liefert einen hashCode f&uuml;r einen long-Wert.
	 *
	 * @param l Der long-Wert, zu dem ein hashCode geliefert werden soll.
	 * @return Ein hashCode zum angegebenen long-Wert.
	 *
	 * @changed OLI 12.03.2013 - Added.
	 */
	public static int hashCode(long l) {
		return new Long(l).hashCode();
	}

	/**
	 * Liest die angegebene Property-Datei in die Systemproperties ein. Es wird entsprechender Output auf der Konsole
	 * erzeugt.
	 *
	 * @param pfn Der Name der Datei, aus der die Properties gelesen werden sollen.
	 * @throws FileNotFoundException Falls es keine Datei unter dem angegebenen Namen gibt.
	 * @throws IOException           Falls es beim Einlesen der Datei zu einem Fehler kommt.
	 * @throws NullPointerException  Falls der Dateiname als <TT>null</TT>-Referenz &uuml;bergeben wird.
	 * @precondition pfn != <TT>null</TT>
	 *
	 * @changed OLI 26.01.2009 - R&uuml;ckbau zu Gunsten der Methode <TT>ReadProperties(Properties,String)</TT>.
	 * @changed OLI 11.09.2009 - &Uuml;bernahme nach <TT>corentx.util.Utl</TT>.
	 */
	public static void readProperties(String pfn) throws FileNotFoundException, IOException, NullPointerException {
		String s = "reading properties (" + pfn + ") ... ";
		readProperties(System.getProperties(), pfn);
		log.info(s + "ok");
	}

	/**
	 * Liest die angegebene Property-Datei in die angegebenen Properties ein. Es wird entsprechender Output auf der
	 * Konsole erzeugt.
	 *
	 * @param p   Die Properties, in die die Propertiesdatei eingelesen werden soll.
	 * @param pfn Der Name der Datei, aus der die Properties gelesen werden sollen.
	 * @throws FileNotFoundException Falls es keine Datei unter dem angegebenen Namen gibt.
	 * @throws IOException           Falls es beim Einlesen der Datei zu einem Fehler kommt.
	 * @throws NullPointerException  Falls die Properties oder der Dateiname als <TT>null</TT>-Referenz &uuml;bergeben
	 *                               werden.
	 *
	 * @precondition p != <TT>null</TT> &amp;&amp; pfn != <TT>null</TT>
	 *
	 * @changed OLI 26.01.2009 - Hinzugef&uuml;gt.
	 * @changed OLI 11.02.2009 - Konsolenausgaben entfernt.
	 * @changed OLI 11.09.2009 - &Uuml;bernahme nach <TT>corentx.util.Utl</TT>.
	 */
	public static void readProperties(Properties p, String pfn)
			throws FileNotFoundException, IOException, NullPointerException {
		File f = new File(pfn);
		FileInputStream fis = new FileInputStream(f);
		p.load(fis);
		fis.close();
	}

}
