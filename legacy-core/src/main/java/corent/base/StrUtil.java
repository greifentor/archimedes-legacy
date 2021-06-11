/*
 * StrUtil.java
 *
 * 04.01.2004
 *
 * (c) O.Lieshoff
 *
 */

package corent.base;

import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.text.StringEscapeUtils;

import logging.Logger;

/**
 * Diese Klasse sammelt diverse Funktionen, die sich auf den Umgang mit Strings beziehen.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.01.2004 - Hinzugef&uuml;gt.
 * @changed OLI 05.09.2007 - Erweiterung der Methoden <TT>FromHTML(String)</TT> und <TT>ToHTML(String)</TT> um die
 *          Reaktion auf die Property <TT>corent.base.StrUtil.suppress.html.note</TT>, die einen Hinweis auf eine Klasse
 *          mit besseren Methoden zur Umsetzung von String in das HTML-Format unterdr&uuml;ckt. Ich habe die Methoden
 *          mit Absicht nicht auf <I>deprecated</I> gesetzt, um schlanke Applikationen auch ohne die
 *          apache-commons-lang-Bibliotheken zu erm&ouml;glichen.
 * @changed OLI 31.01.2008 - Erweiterung um die M&ouml;glichkeit die entsprechenden Methoden zum Umwandeln von HTML aus
 *          dem commons-Package, anstatt der eigenen Umsetzung zu nutzen. Dies geschied durch das Setzen der Property
 *          <I>corent.base.StrUtil.use.commons</I>.
 * @changed OLI 01.05.2008 - Erweiterung um die Methode <TT>ReplaceFirst(String, String,
 *        String)</TT>.
 * @changed OLI 10.03.2009 - Erweiterung um die Methode <TT>SplitToList(String, String)</TT>.
 * @changed OLI 13.03.2009 - Erweiterung um die Methode <TT>Trim(String, String)</TT>. Dabei Reorganisation der
 *          Reihenfolge der Methoden.
 * @changed OLI 03.06.2009 - Erweiterung um die Methode <TT>ExtractXMLTagValue(String,
 *        String)</TT>. Dabei: Formatanpassungen und Sortierung der Methoden.
 * @changed OLI 04.06.2009 - Erweiterung um die Methode <TT>GermanComparison(String,
 *        String)</TT>.
 * @changed OLI 15.06.2009 - Anpassungen an log4j.
 *
 */

public class StrUtil {

	/* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
	private static Logger log = Logger.getLogger(StrUtil.class);

	/*
	 * * HINWEIS: DIESE IMPLEMENTIERUNG IST NICHT PERFORMANT. DAHER IST SIE AUS DEM FUNKTIONSUMFANG ENTFERNT WORDEN.
	 * STATTDESSEN IST DIE METHODE <TT>StringUtils.Contains( String, char) ZU NUTZEN (SIEHE PERFORMANCETEST).
	 *
	 * Pr&uum;ft, ob sich das angegebene Zeichen in dem &uuml;bergebenen String befindet.
	 *
	 * @param s Der String, in dem das Zeichen gesucht werden soll.
	 * 
	 * @return <TT>true</TT>, falls das Zeichen in dem String enthalten ist, <TT>false</TT> sonst.
	 *
	 * @changed OLI 13.03.2009 - Hinzugef&uuml;gt. <P>
	 *
	 * / public static boolean Contains(String s, char c) { int len = s.length(); for (int i = 0; i < len; i++) { if
	 * (s.charAt(i) == c) { return true; } } return false; }
	 */

	/**
	 * Mit Hilfe dieser Funktion l&auml;&szlig;t sich ein double-Wert in einen formatierten String umwandeln.
	 *
	 * @param d   Der umzuwandelnde doube-Wert.
	 * @param nks Die Anzahl der Nachkommastellen.
	 * @return Ein String mit dem formatierten double-Wert.
	 */
	public static String FixedDoubleToStr(double d, int nks) {
		d = (Math.rint(d * Math.pow(10, (double) nks))) / Math.pow(10, (double) nks);
		Double db = new Double(d);
		String s = db.toString();
		int i = s.indexOf(".");
		if (i < 0) {
			s += ".";
			for (int k = 1; k <= nks; k++) {
				s += "0";
			}
		} else {
			int j = s.length() - i;
			if (j < nks + 1) {
				for (int k = 0; k < (nks - j) + 1; k++) {
					s += "0";
				}
			} else if (nks == 0) {
				s = s.substring(0, i);
			} else if (j > nks + 1) {
				s = s.substring(0, i + nks + 1);
			}
		}
		return s;
	}

	/**
	 * Wandelt den angebenen HTML-String in einen Java-String um.
	 *
	 * @param html Der umzuwandelnde HTML-String.
	 * @return Die Java-Umwandlung des Strings.
	 *
	 * @changed OLI 05.09.2007 - Erweiterung um die Reaktion auf die Property
	 *          <TT>corent.base.StrUtil.suppress.html.note</TT>, die einen Hinweis auf eine Klasse mit besseren Methoden
	 *          zur Umsetzung von String in das HTML-Format unterdr&uuml;ckt. Ich habe die Methode mit Absicht nicht auf
	 *          <I>deprecated</I> gesetzt, um schlanke Applikationen auch ohne die apache-commons-lang-Bibliotheken zu
	 *          erm&ouml;glichen.
	 *          <P>
	 *          OLI 31.01.2008 - Erweiterung um die M&ouml;glichkeit die Methode
	 *          <TT>StringEscapeUtils.unescapeHtml(String)</TT>, anstatt der eigenen Umsetzung zu nutzen. Dies geschied
	 *          durch das Setzen der Property <I>corent.base.StrUtil.use.commons</I>.
	 *          <P>
	 *
	 */
	public static String FromHTML(String html) {
		if (Boolean.getBoolean("corent.base.StrUtil.use.commons")) {
			return StringEscapeUtils.unescapeHtml4(html);
		}
		if (!Boolean.getBoolean("corent.base.StrUtil.suppress.html.note")) {
			log.warn("Use StringEscapeUtil methods instead of StrUtil.FromHTML(String)!");
			log.warn("or suppress this note by setting property \"corent.base.StrUtil.suppress." + "html.note\"!");
		}
		String s = html;
		s = Replace(s, "&amp;", "&");
		s = Replace(s, "&auml;", "ä");
		s = Replace(s, "&Auml;", "Ä");
		s = Replace(s, "&gt;", ">");
		s = Replace(s, "&lt;", "<");
		s = Replace(s, "&nbsp;", " ");
		s = Replace(s, "&ouml;", "ö");
		s = Replace(s, "&Ouml;", "Ö");
		s = Replace(s, "&radic;", "ø");
		s = Replace(s, "&Radic;", "Ø");
		s = Replace(s, "&szlig;", "ß");
		s = Replace(s, "&uuml;", "ü");
		s = Replace(s, "&Uuml;", "Ü");
		return s;
	}

	/**
	 * Diese Methode ersetzt den angegebenen String durch den Inhalt der angegebenen Property die, falls existent, gegen
	 * den Inhalt einer Property mit dem um den angegebenen Suffix erweiterten Namen ersetzt wird.
	 *
	 * @param pn     Der Name der Property.
	 * @param def    Ein Defaultwert, falls die Property "pn" nicht definiert ist.
	 * @param suffix Der Suffix, der zus&auml;tzlich in der Form "pn" + "." + "suffix" untersucht werden soll. Existiert
	 *               eine Property mit einem solchen Namen, so deren Wert zur&uuml;ckgegeben.
	 */
	public static String GetProperty(String pn, String def, String suffix) {
		String p = System.getProperty(pn, def);
		if (System.getProperty(pn + "." + suffix) != null) {
			p = System.getProperty(pn + "." + suffix);
		}
		return p;
	}

	/**
	 * F&uuml;gt den angegebenen String ins in den String s an der Position p ein und liefert das Ergebnis zur&uuml;ck.
	 *
	 * @param s   Der String, in den der String ins eingef&uuml;gt werden soll.
	 * @param ins Der einzuf&uuml;gende String
	 * @param pos Die Position an der ins in s eingef&uuml;gt werden soll.
	 * @return Der String s mit dem an Position pos eingef&uuml;gten String ins.
	 * @throws IndexOutOfBoundsException Falls die Position, an der eingef&uuml;gt werden soll au&szlig;erhalb des
	 *                                   String s liegt.
	 */
	public static String InsertStr(String s, String ins, int pos) throws IndexOutOfBoundsException {
		return s.substring(0, pos).concat(ins).concat(s.substring(pos, s.length()));
	}

	/**
	 * Mit Hilfe dieser Methode kann ein String erzeugt werden, der den Inhalt des &uuml;bergebenen Originals
	 * enth&auml;lt, aber um eine Erweiterung aufgeblasen wird.
	 *
	 * @param s Das zu erweiternde Original.
	 * @param e Der String, mit dem das Original erweitert werden soll.
	 * @param l Die L&auml;nge, die der erzeugte String mindestens erreichen soll.
	 * @param r Die Richtung, in die der String aufgeblasen werden soll (LEFT oder RIGHT).
	 * @throws IllegalArgumentException wenn eine falsche Richtung &uuml;bergeben wird.
	 */
	public static String PumpUp(String s, String e, int l, Direction r) throws IllegalArgumentException {
		if ((r == Direction.LEFT) || (r == Direction.RIGHT)) {
			String erg = s;
			while (erg.length() < l) {
				if (r == Direction.LEFT) {
					erg = e.concat(erg);
				} else {
					erg = erg.concat(e);
				}
			}
			return erg;
		}
		throw new IllegalArgumentException("Direction " + r + " is not valid for this operation!");
	}

	/**
	 * Mit Hilfe dieser Methode kann ein StringBuffer erzeugt werden, der den Inhalt des &uuml;bergebenen Originals
	 * enth&auml;lt, aber um eine Erweiterung aufgeblasen wird.
	 *
	 * @param sb Das zu erweiternde Original.
	 * @param e  Der String, mit dem das Original erweitert werden soll.
	 * @param l  Die L&auml;nge, die der erzeugte String mindestens erreichen soll.
	 * @param r  Die Richtung, in die der String aufgeblasen werden soll (LEFT oder RIGHT).
	 * @throws IllegalArgumentException wenn eine falsche Richtung &uuml;bergeben wird.
	 */
	public static StringBuffer PumpUp(StringBuffer sb, String e, int l, Direction r) throws IllegalArgumentException {
		if ((r == Direction.LEFT) || (r == Direction.RIGHT)) {
			StringBuffer eb = new StringBuffer(e);
			StringBuffer erg = new StringBuffer(sb.toString());
			while (erg.length() < l) {
				if (r == Direction.LEFT) {
					erg = eb.append(erg);
				} else {
					erg.append(eb);
				}
			}
			return erg;
		}
		throw new IllegalArgumentException("Direction " + r + " is not valid for this " + "operation!");
	}

	/**
	 * Diese Methode ersetzt in dem angegebenen String s alle Teilstrings p durch den String c.
	 *
	 * @param s Der String, der als Grundlage f&uuml;r die Ersetzung dienen soll.
	 * @param p Das Muster, welches ersetzt werden soll.
	 * @param c Der Teilstring, der anstelle des Musters eingesetzt werden soll.
	 * @return Der String s mit den entsprechenden Ersetzungen.
	 */
	public static String Replace(String s, String p, String c) {
		int len = p.length();
		int pos = s.indexOf(p);
		if (pos >= 0) {
			s = s.substring(0, pos) + c + Replace(s.substring(pos + len), p, c);
		}
		return s;
	}

	/**
	 * Liefert eine Liste von String, die sich aus der Aufspaltung des &uuml;bergebenen Strings anhand der Trennzeichen
	 * ergibt.
	 *
	 * @param s     Der auszuspaltende String.
	 * @param delim Das oder die Trennzeichen.
	 * @return Eine Liste mit den sich durch die Trennzeichen ergebenden Teilstrings bzw. eine leere Liste, wenn die
	 *         keine Teilstrings zu bilden sind.
	 *
	 * @changed OLI 10.03.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	public static List<String> SplitToList(String s, String delim) {
		List<String> l = new Vector<String>();
		StringTokenizer st = new StringTokenizer(s, delim);
		while (st.hasMoreTokens()) {
			s = st.nextToken();
			l.add(s.trim());
		}
		return l;
	}

	/**
	 * Wandelt den angebenen String in einen HTML-String um.
	 *
	 * @param s Der umzuwandelnde String.
	 * @return Die HTML-Umwandlung des Strings.
	 *
	 * @changed OLI 05.09.2007 - Erweiterung um die Reaktion auf die Property
	 *          <TT>corent.base.StrUtil.suppress.html.note</TT>, die einen Hinweis auf eine Klasse mit besseren Methoden
	 *          zur Umsetzung von String in das HTML-Format unterdr&uuml;ckt. Ich habe die Methode mit Absicht nicht auf
	 *          <I>deprecated</I> gesetzt, um schlanke Applikationen auch ohne die apache-commons-lang-Bibliotheken zu
	 *          erm&ouml;glichen.
	 *          <P>
	 *          OLI 31.01.2008 - Erweiterung um die M&ouml;glichkeit die Methode
	 *          <TT>StringEscapeUtils.escapeHtml(String)</TT>, anstatt der eigenen Umsetzung zu nutzen. Dies geschied
	 *          durch das Setzen der Property <I>corent.base.StrUtil.use.commons</I>.
	 *          <P>
	 *
	 */
	public static String ToHTML(String s) {
		if (Boolean.getBoolean("corent.base.StrUtil.use.commons")) {
			return StringEscapeUtils.escapeHtml4(s);
		}
		if (!Boolean.getBoolean("corent.base.StrUtil.suppress.html.note")) {
			log.warn("Use StringEscapeUtil methods instead of StrUtil.ToHTML(String)!");
			log.warn("or suppress this note by setting property \"corent.base.StrUtil.suppress." + "html.note\"!");
		}
		String html = s;
		html = Replace(html, "&", "&amp;");
		html = Replace(html, "ä", "&auml;");
		html = Replace(html, "Ä", "&Auml;");
		html = Replace(html, ">", "&gt;");
		html = Replace(html, "<", "&lt;");
		// html = Replace(html, " ", "&nbsp;");
		html = Replace(html, "ö", "&ouml;");
		html = Replace(html, "Ö", "&Ouml;");
		html = Replace(html, "ø", "&radic;");
		html = Replace(html, "Ø", "&Radic;");
		html = Replace(html, "ß", "&szlig;");
		html = Replace(html, "ü", "&uuml;");
		html = Replace(html, "Ü", "&Uuml;");
		html = Replace(html, "Ü", "&Uuml;");
		html = Replace(html, "â", "&acircum;");
		html = Replace(html, "ê", "&ecircum;");
		html = Replace(html, "î", "&icircum;");
		html = Replace(html, "ô", "&ocircum;");
		html = Replace(html, "û", "&ucircum;");
		return html;
	}

}