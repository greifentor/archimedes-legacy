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
import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.apache.log4j.Logger;

import corentx.util.VowelMutationReplaceStrategy;

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
	 * Liefert den Inhalt des angegebenen XML-Style-Tags (begrenzt von einem attributfreien &ouml;ffnenden und einem
	 * schliessenden Tag).
	 *
	 * @param src     Der XML-Style-String, aus dem der Inhalt des angegebenen Tags extrahiert werden soll.
	 * @param tagname Der Name des Tags, dessen Inhalt extrahiert werden soll.
	 * @return Der Inhalt des angegebenen Tags.
	 * @throws NoSuchElementException Falls der Quellstring kein attributfreies Tag mit den angegebenen Namen aufweist.
	 * @throws NullPointerException   Falls Quellstring oder Tagname als <TT>null</TT>-Referenz &uuml;bergeben werden.
	 *
	 * @precondition src und tagname sind ungleich null.
	 *
	 * @changed OLI 03.06.2009 - Hinzugef&uuml;gt.
	 *
	 */
	public static String ExtractXMLStyleTagValue(String src, String tagname)
			throws NoSuchElementException, NullPointerException {
		assert (src != null) && (tagname != null) : "Source string and tagname have to be not "
				+ "null while calling method StrUtil.ExtractXMLStyleTagValue(String, String)!";
		int end = 0;
		int start = 0;
		String result = null;
		String tagEnd = "</".concat(tagname).concat(">");
		String tagStart = "<".concat(tagname).concat(">");
		if (!src.contains(tagStart)) {
			throw new NoSuchElementException("Tagname (" + tagname + ") does not exists in " + "string (" + src + ")!");
		}
		start = src.indexOf(tagStart);
		end = src.indexOf(tagEnd);
		if ((start >= 0) && (end >= 0)) {
			result = src.substring(start + tagStart.length(), end);
		} else {
			throw new NoSuchElementException(
					"Closing tag not found. May be tag (" + tagname + ") is not in a valifd form!");
		}
		return result;
	}

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
	 * Vergleich (z. B. zwecks Sortierung) zwischen den angegebenen String nach deutscher Sortierung (&uuml; wird
	 * beispielsweise als u gewertet, &ouml; als o usw.).
	 *
	 * <P>
	 * <I><B>Hinweis:</B> Der Vergleich ist nicht case sensitive</I>.
	 *
	 * @param s0 Der String gegen den verglichen werden soll.
	 * @param s1 Der zuvergleichende String.
	 * @return <TT>-1</TT> wenn s0 > s1, <TT>0</TT> bei Gleichheit und <TT>1</TT> wenn s0 < s1.
	 * @throws NullPointerException Falls einer der beiden &uuml;bergebenen Strings eine null-Referenz ist.
	 *
	 * @precondition s0 und s1 sind ungleich <TT>null</TT>.
	 *
	 * @changed OLI 04.06.2009 - Hinzugef&uuml;gt.
	 *
	 */
	public static int GermanCompare(String s0, String s1) {
		assert (s0 != null) && (s1 != null) : "One of the parameter strings is null!";
		int result = ReplaceVowelMutations(s0, VowelMutationReplaceStrategy.SIMPLE_REPLACE)
				.compareToIgnoreCase(ReplaceVowelMutations(s1, VowelMutationReplaceStrategy.SIMPLE_REPLACE));
		if (result == 0) {
			result = ReplaceVowelMutations(s0, VowelMutationReplaceStrategy.TWO_CHAR_REPLACE)
					.compareToIgnoreCase(ReplaceVowelMutations(s1, VowelMutationReplaceStrategy.TWO_CHAR_REPLACE));
		}
		return result;
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
	 * Wandelt das &uuml;bergebene Zeichen in einen Kleinbuchstaben um. Nicht die effizienteste Art ein Zeichen
	 * umzuwandeln, aber eine funktionsf&auml;hige.
	 *
	 * @param c Der umzuwandelnde Buchstabe.
	 * @return Der Kleinbuchstabe zu c.
	 */
	public static char LowerCase(char c) {
		return ("" + c).toLowerCase().charAt(0);
	}

	/**
	 * Parst einen String auf nummerischen Inhalt und liefert ein entsprechendes Wrapper-Objekt zur&uuml;ck.
	 *
	 * @param s Der zu parsende String.
	 * @return Ein Objekt mit dem entsprechenden Wert in einem Wrapper-Objekt, bzw. <TT>null</TT>, falls kein
	 *         nummerischer Wert ermittelt werden konnte.
	 */
	public static Number Parse(String s) {
		s = s.toLowerCase();
		try {
			if (s.endsWith("d")) {
				s = s.substring(0, s.length() - 1);
				try {
					return new Double(s);
				} catch (NumberFormatException nfe1) {
				}
			} else if (s.endsWith("f")) {
				s = s.substring(0, s.length() - 1);
				try {
					return new Float(s);
				} catch (NumberFormatException nfe1) {
				}
			} else if (s.endsWith("l")) {
				s = s.substring(0, s.length() - 1);
				try {
					return new Long(s);
				} catch (NumberFormatException nfe1) {
				}
			}
			try {
				return new Integer(s);
			} catch (NumberFormatException nfe1) {
				try {
					return new Long(s);
				} catch (NumberFormatException nfe2) {
					try {
						return new Float(s);
					} catch (NumberFormatException nfe3) {
						try {
							return new Double(s);
						} catch (NumberFormatException nfe4) {
						}
					}
				}
			}
		} catch (NumberFormatException nfe) {
			if (Boolean.getBoolean("corent.StrUtil.debug")) {
				nfe.printStackTrace();
			}
		}
		return null;
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
	 * Diese Methode ersetzt in dem angegebenen String s den ersten Teilstrings p durch den String c.
	 *
	 * @param s Der String, der als Grundlage f&uuml;r die Ersetzung dienen soll.
	 * @param p Das Muster, welches ersetzt werden soll.
	 * @param c Der Teilstring, der anstelle des Musters eingesetzt werden soll.
	 * @return Der String s mit den entsprechenden Ersetzungen.
	 *
	 * @changed OLI 01.05.2008 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	public static String ReplaceFirst(String s, String p, String c) {
		int len = p.length();
		int pos = s.indexOf(p);
		if (pos >= 0) {
			s = s.substring(0, pos).concat(c).concat(s.substring(pos + len));
		}
		return s;
	}

	/**
	 * Ersetzt die Umlaute in dem &uuml;bergebenen String anhand der gew&auml;hlten Strategie.
	 *
	 * @param s    Der String, in dem die Umlaute ersetzt werden sollen.
	 * @param vmrs Die Austauschstrategie.
	 * @return Der String mit den ausgetauschten Umlauten.
	 * @throws NullPointerException Falls der String als null-Referenz &uuml;bergeben wird.
	 *
	 * @precondition s ungleich <TT>null</TT>
	 *
	 * @changed OLI 04.06.2009 - Hinzugef&uuml;gt.
	 *
	 */
	public static String ReplaceVowelMutations(String s, VowelMutationReplaceStrategy vmrs) {
		assert s != null : "the string parameter can not be null!";
		if (vmrs == VowelMutationReplaceStrategy.SIMPLE_REPLACE) {
			s = s.replace(FromHTML("&auml;"), "a");
			s = s.replace(FromHTML("&ouml;"), "o");
			s = s.replace(FromHTML("&uuml;"), "u");
			s = s.replace(FromHTML("&szlig;"), "ss");
			s = s.replace(FromHTML("&Auml;"), "A");
			s = s.replace(FromHTML("&Ouml;"), "O");
			s = s.replace(FromHTML("&Uuml;"), "U");
		} else {
			s = s.replace(FromHTML("&auml;"), "ae");
			s = s.replace(FromHTML("&ouml;"), "oe");
			s = s.replace(FromHTML("&uuml;"), "ue");
			s = s.replace(FromHTML("&szlig;"), "ss");
			s = s.replace(FromHTML("&Auml;"), "Ae");
			s = s.replace(FromHTML("&Ouml;"), "Oe");
			s = s.replace(FromHTML("&Uuml;"), "Ue");
		}
		return s;
	}

	/**
	 * Wandelt einen Wert vom Typ int in eine vorzeichenbehaftete Stringdarstellung um.
	 *
	 * @param l Der umzuwandelnde Wert.
	 */
	public static String SignedIntegerToStr(int l) {
		Integer ol = new Integer(l);
		String s = ol.toString();
		if (ol.intValue() > 0) {
			s = "+".concat(s);
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

	/**
	 * Entfernt die in dem String <TT>toRemove</TT> definierten Zeichen vom Anfang und Ende des Strings <TT>s</TT>.
	 *
	 * @param s        Der String, dessen Anfangs- und Endsequenz von den im String <TT>toRemove</TT> definierten
	 *                 Zeichen bereinigt werden sollen.
	 * @param toRemove Die Zeichen, die gegebenenfalls entfernt werden sollen, sofern sie am Anfang bzw. Ende des
	 *                 Strings <TT>s</TT> stehen.
	 * @return Der String <TT>s</TT> mit der entsprechend bereinigten Anfangs- und Endsequenz.
	 * @throws NullPointerException falls wenigstens eines der beiden Argumente eine <TT>null</TT>-Referenz ist.
	 *
	 * @changed OLI 13.03.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	public static String Trim(String s, String toRemove) throws NullPointerException {
		int i = 0;
		int len = s.length();
		int n = 0;
		if (toRemove == null) {
			throw new NullPointerException("the charset to remove may not be null.");
		}
		if (len > 0) {
			i = 0;
			n = 0;
			while ((i < len) && StringUtils.contains(toRemove, s.charAt(i))) {
				n++;
				i++;
			}
			if ((n > 0) && (n < len)) {
				s = s.substring(n, len);
			}
			len = s.length();
			i = len - 1;
			n = -1;
			while ((i >= 0) && StringUtils.contains(toRemove, s.charAt(i))) {
				n = i;
				i--;
			}
			if (n >= 0) {
				s = s.substring(0, n);
			}
		}
		return s;
	}

	/**
	 * Wandelt das &uuml;bergebene Zeichen in einen Gro&szlig;buchstaben um. Nicht die effizienteste Art ein Zeichen
	 * umzuwandeln, aber eine funktionsf&auml;hige.
	 *
	 * @param c Der umzuwandelnde Buchstabe.
	 * @return Der Gro&szlig;buchstabe zu c.
	 */
	public static char UpperCase(char c) {
		return ("" + c).toUpperCase().charAt(0);
	}

}
