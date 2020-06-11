/*
 * StrUtil.java
 *
 * 04.01.2004
 *
 * (c) O.Lieshoff
 *
 */

package corentx.util;


import java.util.*;

import org.apache.commons.lang3.*;
import org.apache.log4j.*;


/**
 * Diese Klasse sammelt diverse Funktionen, die sich auf den Umgang mit Strings beziehen.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.01.2004 - Hinzugef&uuml;gt.
 * @changed OLI 05.09.2007 - Erweiterung der Methoden <TT>FromHTML(String)</TT> und
 *        <TT>ToHTML(String)</TT> um die Reaktion auf die Property
 *        <TT>corent.base.StrUtil.suppress.html.note</TT>, die einen Hinweis auf eine Klasse mit
 *        besseren Methoden zur Umsetzung von String in das HTML-Format unterdr&uuml;ckt. Ich
 *        habe die Methoden mit Absicht nicht auf <I>deprecated</I> gesetzt, um schlanke
 *        Applikationen auch ohne die apache-commons-lang-Bibliotheken zu erm&ouml;glichen.
 * @changed OLI 31.01.2008 - Erweiterung um die M&ouml;glichkeit die entsprechenden Methoden
 *        zum Umwandeln von HTML aus dem commons-Package, anstatt der eigenen Umsetzung zu
 *        nutzen. Dies geschied durch das Setzen der Property 
 *        <I>corent.base.StrUtil.use.commons</I>.
 * @changed OLI 01.05.2008 - Erweiterung um die Methode <TT>ReplaceFirst(String, String,
 *        String)</TT>.
 * @changed OLI 10.03.2009 - Erweiterung um die Methode <TT>SplitToList(String, String)</TT>.
 * @changed OLI 13.03.2009 - Erweiterung um die Methode <TT>Trim(String, String)</TT>. Dabei
 *        Reorganisation der Reihenfolge der Methoden.
 * @changed OLI 03.06.2009 - Erweiterung um die Methode <TT>ExtractXMLTagValue(String,
 *        String)</TT>. Dabei: Formatanpassungen und Sortierung der Methoden.
 * @changed OLI 04.06.2009 - Erweiterung um die Methode <TT>GermanComparison(String,
 *        String)</TT>.
 * @changed OLI 15.06.2009 - Anpassungen an log4j.
 * @changed OLI 12.08.2009 - &Uuml;bernahme nach corentx.
 * @changed OLI 04.10.2009 - Erweiterung um die Methode <TT>spaces(int)</TT>.
 * @changed OLI 06.10.2009 - Erweiterung um die Methode <TT>quote(String, String)</TT>.
 * @changed OLI 18.10.2009 - Erweiterung um die Methode <TT>nextSubstring(String, String)</TT>.
 * @changed OLI 28.10.2009 - Erweiterung um die Methode <TT>toCamilCase(String, String)</TT>.
 * @changed OLI 14.01.2010 - Anpassungen in der Methode <TT>toCamilCase(String, String)</TT>.
 * @changed OLI 28.02.2010 - Korrekturen und Ma&szlig;nahmen zur Erh&ouml;hung der
 *         Testabdeckung.
 * @changed OLI 18.11.2010 - Erweiterung um die Methode <TT>isNullOrEmpty(String)</TT>.
 */

public class Str {

    /* Referenz auf den f&uuml;r die Klasse zust&auml;ndigen Logger. */
    private static Logger log = Logger.getLogger(Str.class);

    /**
     * Es ist vollkommen sinnfrei, die Klasse zu instanzieren.
     *
     * @throws UnsupportedOperationException Falls der Konstructor aufgerufen wird.
     *
     * @changed OLI 28.02.2010 - Hinzufgef&uuml;gt.
     */
    public Str() {
        throw new UnsupportedOperationException("it makes no sense to create an instance of "
                + "this static class.");
    }

    /* *
     * HINWEIS: DIESE IMPLEMENTIERUNG IST NICHT PERFORMANT. DAHER IST SIE AUS DEM 
     * FUNKTIONSUMFANG ENTFERNT WORDEN. STATTDESSEN IST DIE METHODE <TT>StringUtils.Contains(
     * String, char) ZU NUTZEN (SIEHE PERFORMANCETEST).
     *
     * Pr&uum;ft, ob sich das angegebene Zeichen in dem &uuml;bergebenen String befindet.
     *
     * @param s Der String, in dem das Zeichen gesucht werden soll.
     * @return <TT>true</TT>, falls das Zeichen in dem String enthalten ist, <TT>false</TT>
     *         sonst.
     *
     * @changed
     *     OLI 13.03.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     * /
    public static boolean Contains(String s, char c) {
        int len = s.length();
        for (int i = 0; i < len; i++) {
            if (s.charAt(i) == c) {
                return true;
            }
        }
        return false;
    }
    */

    /**
     * Liefert den Inhalt des angegebenen XML-Style-Tags (begrenzt von einem attributfreien
     * &ouml;ffnenden und einem schliessenden Tag).
     *
     * @param src Der XML-Style-String, aus dem der Inhalt des angegebenen Tags extrahiert
     *         werden soll.
     * @param tagname Der Name des Tags, dessen Inhalt extrahiert werden soll.
     * @return Der Inhalt des angegebenen Tags.
     * @throws NoSuchElementException Falls der Quellstring kein attributfreies Tag mit den
     *         angegebenen Namen aufweist.
     * @throws NullPointerException Falls Quellstring oder Tagname als <TT>null</TT>-Referenz
     *         &uuml;bergeben werden.
     * @precondition src und tagname sind ungleich null.
     *
     * @changed OLI 03.06.2009 - Hinzugef&uuml;gt.
     * @changed OLI 28.02.2010 - Korrektur der Meldung der Exception.
     */
    public static String extractXMLStyleTagValue(String src, String tagname)
            throws NoSuchElementException, NullPointerException {
        int end = 0;
        int start = 0;
        String result = null;
        String tagEnd = "</".concat(tagname).concat(">");
        String tagStart = "<".concat(tagname).concat(">");
        if (!src.contains(tagStart)) {
            throw new NoSuchElementException("Tagname (" + tagname + ") does not exists in "
                    + "string (" + src + ")!");
        }
        start = src.indexOf(tagStart);
        end = src.indexOf(tagEnd);
        if ((start >= 0) && (end >= 0)) {
            result = src.substring(start + tagStart.length(), end);
        } else {
            throw new NoSuchElementException("Closing tag not found. May be tag (" + tagname 
                    + ") is not in a valid form!");
        }
        return result;
    }

    /**
     * Mit Hilfe dieser Funktion l&auml;&szlig;t sich ein double-Wert in einen formatierten 
     * String umwandeln.
     *
     * @param d Der umzuwandelnde doube-Wert.
     * @param nks Die Anzahl der Nachkommastellen.
     * @return Ein String mit dem formatierten double-Wert.
     * @throws IllegalArgumentException Falls die Anzahl der Nachkommastellen kleiner als null
     *         ist.
     * @precondition nks &gt;= <TT>0</TT>
     *
     * @changed OLI 28.02.2010 - Implementierung der Pr&uuml;fung der Nachkommastellenzahl.
     */
    public static String fixedDoubleToStr(double d, int nks) {
        Double db = null;
        int i = 0;
        int j = 0;
        int k = 0;
        String s = null;
        if (nks < 0) {
            throw new IllegalArgumentException("nks have to be zero or more.");
        }
        d = (Math.rint(d * Math.pow(10, (double) nks))) / Math.pow(10, (double) nks);
        db = new Double(d);
        s = db.toString();
        i = s.indexOf(".");
        /* OLI 28.02.2010 - Das kommt irgendwie niemals vor ...
        if (i < 0) {
            s += ".";
            for (k = 1; k <= nks; k++) {
                s += "0";
            }
        } else {
        */
            j = s.length() - i;
            if (j < nks + 1) {
                for (k = 0; k < (nks - j) + 1; k++) {
                    s += "0";
                }
            } else if (nks == 0) {
                s = s.substring(0, i);
            /* OLI 28.02.2010 - Das auch nicht ...
            } else if (j > nks + 1) {
                s = s.substring(0, i + nks + 1);
                */
            }
        // }
        return s;
    }

    /**
     * Wandelt den angebenen HTML-String in einen Java-String um.
     *
     * @param html Der umzuwandelnde HTML-String.
     * @return Die Java-Umwandlung des Strings.
     * @throws NullPointerException Falls der &uuml;bergebene String eine <TT>null</TT>-Referenz
     *         ist.
     * @precondition html != <TT>null</TT>
     *
     * @changed OLI 05.09.2007 - Erweiterung um die Reaktion auf die Property
     *         <TT>corentx.util.Str.suppress.html.note</TT>, die einen Hinweis auf eine Klasse
     *         mit besseren Methoden zur Umsetzung von String in das HTML-Format
     *         unterdr&uuml;ckt. Ich habe die Methode mit Absicht nicht auf <I>deprecated</I>
     *         gesetzt, um schlanke Applikationen auch ohne die apache-commons-lang-Bibliotheken
     *         zu erm&ouml;glichen.
     * @changed OLI 31.01.2008 - Erweiterung um die M&ouml;glichkeit die Methode
     *         <TT>StringEscapeUtils.unescapeHtml(String)</TT>, anstatt der eigenen Umsetzung zu
     *         nutzen. Dies geschied durch das Setzen der Property
     *         <I>corentx.util.Str.use.commons</I>.
     */
    public static String fromHTML(String html) throws NullPointerException {
        String s = html;
        if (Boolean.getBoolean("corentx.util.Str.use.commons")) {
            return StringEscapeUtils.unescapeHtml3(html);
        }
        if (!Boolean.getBoolean("corentx.util.Str.suppress.html.note")) {
            log.warn("Use StringEscapeUtil methods instead of Str.fromHTML(String)!");
            log.warn("or suppress this note by setting property \"corentx.util.Str.suppress."
                    + "html.note\"!");
        }
        /*
        s = replace(s, "&amp;", "&");
        s = replace(s, "&auml;", "�");
        s = replace(s, "&Auml;", "�");
        s = replace(s, "&gt;", ">");
        s = replace(s, "&lt;", "<");
        s = replace(s, "&nbsp;", " ");
        s = replace(s, "&ouml;", "�");
        s = replace(s, "&Ouml;", "�");
        s = replace(s, "&radic;", "ø");
        s = replace(s, "&Radic;", "Ø");
        s = replace(s, "&szlig;", "�");
        s = replace(s, "&uuml;", "�");
        s = replace(s, "&Uuml;", "�");
        s = replace(s, "&aacute;", "�");
        s = replace(s, "&eacute;", "�");
        s = replace(s, "&iacute;", "�");
        s = replace(s, "&oacute;", "�");
        s = replace(s, "&uacute;", "�");
        s = replace(s, "&acirc;", "�");
        s = replace(s, "&ecirc;", "�");
        s = replace(s, "&icirc;", "�");
        s = replace(s, "&ocirc;", "�");
        s = replace(s, "&ucirc;", "�");
        */
        for (StrEntity se : StrEntity.values()) {
            s = s.replace(se.getHTMLEntity(), se.getJavaString());
        }
        return s;
    }

    /**
     * Vergleich (z. B. zwecks Sortierung) zwischen den angegebenen String nach deutscher
     * Sortierung (&uuml; wird beispielsweise als u gewertet, &ouml; als o usw.).
     *
     * <P><I><B>Hinweis:</B> Der Vergleich ist nicht case sensitive</I>.
     *
     * @param s0 Der String gegen den verglichen werden soll.
     * @param s1 Der zuvergleichende String.
     * @return <TT>-1</TT> wenn s0 > s1, <TT>0</TT> bei Gleichheit und <TT>1</TT> wenn s0 < s1.
     * @throws NullPointerException Falls einer der beiden &uuml;bergebenen Strings eine
     *         null-Referenz ist.
     * @precondition s0 und s1 sind ungleich <TT>null</TT>.
     *
     * @changed OLI 04.06.2009 - Hinzugef&uuml;gt.
     */
    public static int germanCompare(String s0, String s1) throws NullPointerException {
        int result = replaceVowelMutations(s0, VowelMutationReplaceStrategy.SIMPLE_REPLACE
                ).compareToIgnoreCase(replaceVowelMutations(s1,
                VowelMutationReplaceStrategy.SIMPLE_REPLACE));
        if (result == 0) {
            result = replaceVowelMutations(s0, VowelMutationReplaceStrategy.TWO_CHAR_REPLACE
                ).compareToIgnoreCase(replaceVowelMutations(s1,
                VowelMutationReplaceStrategy.TWO_CHAR_REPLACE));
        }
        return result;
    }

    /**
     * F&uuml;gt den angegebenen String ins in den String s an der Position p ein und liefert 
     * das Ergebnis zur&uuml;ck.
     *
     * @param s Der String, in den der String ins eingef&uuml;gt werden soll.
     * @param ins Der einzuf&uuml;gende String
     * @param pos Die Position an der ins in s eingef&uuml;gt werden soll.
     * @return Der String s mit dem an Position pos eingef&uuml;gten String ins.
     * @throws IndexOutOfBoundsException Falls die Position, an der eingef&uuml;gt werden 
     *         soll au&szlig;erhalb des String s liegt.
     * @throws NullPointerException Falls Der &uuml;bergebene String oder das einzuf&uuml;gende
     *         String-Fragment <TT>null</TT> sind.
     * @precondition s != <TT>null</TT> &amp;&amp; ins != <TT>null</TT> &amp;&amp; pos in
     *         [0..s.length()]
     */
    public static String insert(String s, String ins, int pos) 
            throws IndexOutOfBoundsException, NullPointerException {
        return s.substring(0, pos).concat(ins).concat(s.substring(pos, s.length()));
    }

    /**
     * Pr&uuml;ft, ob der angegebene String eine <TT>null</TT>-Referenz oder ein leerer String
     * ist.
     *
     * @return <TT>true</TT>, falls der angegebene String ein leerer String oder eine
     *          <TT>null</TT>-Referenz ist.
     */
    public static boolean isNullOrEmpty(String s) {
        if ((s != null) && !s.isEmpty()) {
            return false;
        }
        return true;
    }

    /**
     * Wandelt das &uuml;bergebene Zeichen in einen Kleinbuchstaben um. Nicht die effizienteste
     * Art ein Zeichen umzuwandeln, aber eine funktionsf&auml;hige.
     *
     * @param c Der umzuwandelnde Buchstabe.
     * @return Der Kleinbuchstabe zu c.
     */
    public static char lowerCase(char c) {
        return ("" + c).toLowerCase().charAt(0);
    }

    /**
     * Liefert einen Teilstring vom angegebenen String, der von dessen Anfang bis zur
     * angegebenen Sequenz reicht. Ist die angegebene Sequenz nicht vorhanden, wird der gesamte
     * String zur&uuml;ckgegeben.
     *
     * @since 1.9.1
     *
     * @param s Der String, aus dem der Teilstring separiert werden soll.
     * @param p Die Sequenz, die den String nach hinten begrenzt.
     * @return Der Teilstring vom angegebenen String, der von dessen Anfang bis zur angegebenen
     *         Sequenz reicht. Ist die angegebene Sequenz nicht vorhanden, wird der gesamte
     *         String zur&uuml;ckgegeben. Die begrenzende Sequenz ist in dem Teilstring nicht
     *         mehr enthalten.
     * @throws IllegalArgumentException Falls die Sequenz, die das Ende des Teilstring bestimmt
     *         als <TT>null</TT>-Referenz oder leer &uuml;bergeben wird.
     * @throws NullPointerException Falls der String, aus dem der Teilstring gelesen werden soll
     *         als <TT>null</TT>-Referenz &uuml;bergeben wird.
     * @precondition s != <TT>null</TT>.
     * @precondition p != <TT>null</TT> &amp;&amp; p.length() &gt; 0.
     */
    public static String nextSubstring(String s, String p) throws IllegalArgumentException,
            NullPointerException {
        if ((p == null) || (p.length() < 1)) {
            throw new IllegalArgumentException("end sequence of substring can not be null or "
                    + "empty.");
        }
        int pos = s.indexOf(p);
        if (pos > -1) {
            s = s.substring(0, pos);
        }
        return s;
    }

    /**
     * Liefert einen Teilstring vom angegebenen String, der vom ersten Auftreten der angegebenen
     * Sequenz bis zum Ende des Strings. Ist die angegebene Sequenz nicht vorhanden, wird der
     * gesamte String zur&uuml;ckgegeben.
     *
     * @since 1.9.1
     *
     * @param s Der String, aus dem der Teilstring separiert werden soll.
     * @param p Die Sequenz, ab der der Teilstring separiert werden soll.
     * @return Der Teilstring vom angegebenen String, der vom ersten Auftreten der angegebenen
     *         Sequenz bis zum Ende des Strings. Ist die angegebene Sequenz nicht vorhanden,
     *         wird der gesamte String zur&uuml;ckgegeben.
     * @throws IllegalArgumentException Falls die Sequenz, die den Anfang des Teilstring
     *         bestimmt als <TT>null</TT>-Referenz oder leer &uuml;bergeben wird.
     * @throws NullPointerException Falls der String, aus dem der Teilstring gelesen werden soll
     *         als <TT>null</TT>-Referenz &uuml;bergeben wird.
     * @precondition s != <TT>null</TT>.
     * @precondition p != <TT>null</TT> &amp;&amp; p.length() &gt; 0.
     */
    public static String nextTailString(String s, String p) throws IllegalArgumentException,
            NullPointerException {
        if ((p == null) || (p.length() < 1)) {
            throw new IllegalArgumentException("end sequence of substring can not be null or "
                    + "empty.");
        }
        int pos = s.indexOf(p);
        if (pos > -1) {
            s = s.substring(pos + p.length(), s.length());
        }
        return s;
    }

    /**
     * Parst einen String auf nummerischen Inhalt und liefert ein entsprechendes Wrapper-Objekt
     * zur&uuml;ck.
     *
     * @param s Der zu parsende String.
     * @return Ein Objekt mit dem entsprechenden Wert in einem Wrapper-Objekt, bzw.
     *         <TT>null</TT>, falls kein nummerischer Wert ermittelt werden konnte.
     * @throws NullPointerException Falls der zu parsende String eine <TT>null</TT>-Referenz
     *         ist.
     * @throws NumberFormatException Wenn sich der String nicht in eine g&uuml;ltige Zahl
     *         umrechnen l&auml;szlig;t
     * @precondition s != <TT>null</TT>
     */
    public static Number parseNumber(String s) throws NullPointerException,
            NumberFormatException {
        s = s.toLowerCase();
        if (s.endsWith("d")) {
            s = s.substring(0, s.length()-1);
            return new Double(s);
        } else if (s.endsWith("f")) {
            s = s.substring(0, s.length()-1);
            return new Float(s);
        } else if (s.endsWith("l")) {
            s = s.substring(0, s.length()-1);
            return new Long(s);
        }
        try {
            return new Integer(s);
        } catch (NumberFormatException nfe1) {
            try {
                return new Long(s);
            } catch (NumberFormatException nfe2) {
                return new Float(s);
                // OLI 14.08.2009 - Auf einen Double wuerde er nicht mehr kommen.
            }
        }
    }

    /**
     * Mit Hilfe dieser Methode kann ein String erzeugt werden, der den Inhalt des
     * &uuml;bergebenen Originals enth&auml;lt, aber um eine Erweiterung aufgeblasen wird.
     *
     * @param s Das zu erweiternde Original.
     * @param e Der String, mit dem das Original erweitert werden soll.
     * @param l Die L&auml;nge, die der erzeugte String mindestens erreichen soll.
     * @param r Die Richtung, in die der String aufgeblasen werden soll (LEFT oder RIGHT).
     * @throws IllegalArgumentException Wenn eine falsche Richtung &uuml;bergeben wird (nicht
     *         LEFT oder RIGHT).
     * @throws NullPointerException Falls der aufzupumpende String oder die Erweiterung als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @precondition s != <TT>null</TT> &amp;&amp; e != <TT>null</TT> &amp;&amp; r !=
     *         <TT>null</TT> &amp;&amp; (r == Direction.LEFT || r == Direction.RIGHT)
     */
    public static String pumpUp(String s, String e, int l, Direction r) 
            throws IllegalArgumentException, NullPointerException {
       String erg = null;
       if (e == null) {
           throw new NullPointerException("extension string can not be null.");
       }
       if ((r == Direction.LEFT) || (r == Direction.RIGHT)) {
           erg = s;
           while (erg.length() < l) {
               if (r == Direction.LEFT) {
                   erg = e.concat(erg);
               } else {
                   erg = erg.concat(e);
               }
           }
           return erg;
       }
       throw new IllegalArgumentException("Direction " + r + " is not valid for this operation!"
               );
    }

    /**
     * Mit Hilfe dieser Methode kann ein StringBuffer erzeugt werden, der den Inhalt des 
     * &uuml;bergebenen Originals enth&auml;lt, aber um eine Erweiterung aufgeblasen wird.
     *
     * @param sb Das zu erweiternde Original.
     * @param e Der String, mit dem das Original erweitert werden soll.
     * @param l Die L&auml;nge, die der erzeugte String mindestens erreichen soll.
     * @param r Die Richtung, in die der String aufgeblasen werden soll (LEFT oder RIGHT).
     * @throws IllegalArgumentException Wenn eine falsche Richtung &uuml;bergeben wird (nicht
     *         LEFT oder RIGHT).
     * @throws NullPointerException Falls der aufzupumpende StringBuffer oder die Erweiterung
     *         als <TT>null</TT>-Referenz &uuml;bergeben werden. Ebenfalls, wenn die Richtung
     *         als <TT>null</TT>-Referenz &uuml;bergeben wird.
     * @precondition sb != <TT>null</TT> &amp;&amp; e != <TT>null</TT> &amp;&amp; r !=
     *         <TT>null</TT>
     */
    public static StringBuffer pumpUp(StringBuffer sb, String e, int l, Direction r) 
            throws IllegalArgumentException, NullPointerException {
       StringBuffer eb = null;
       StringBuffer erg = null;
       if (e == null) {
           throw new NullPointerException("extension string can not be null.");
       }
       if ((r == Direction.LEFT) || (r == Direction.RIGHT)) {
            eb = new StringBuffer(e);
            erg = new StringBuffer(sb.toString());
            while (erg.length() < l) {
                if (r == Direction.LEFT) {
                    erg = new StringBuffer(eb).append(erg);
                } else {
                    erg.append(eb);
                }
            }
            return erg;
        }
        throw new IllegalArgumentException("Direction " + r + " is not valid for this "
                + "operation!");
    }

    /**
     * Liefert einen String der in die angegebenen Anf&uuml;hrungszeichen gesetzt ist,
     * vorausgesetzt das die Anf&uuml;hrungszeichen angegeben werden. Mit
     * "Anf&uuml;hrungszeichen" ist hier ein String gemeint, der dem angegebenen String voran-
     * und nachgestellt wird.
     *
     * @param s Der String der in "Anf&uuml;hrungszeichen" gesetzt werden soll.
     * @param q Die "Anf&uuml;hrungszeichen".
     * @return Ein String aus q + s + q, falls q != <TT>null</TT>, sonst s.
     * @throws NullPointerException Falls s als <TT>null</TT>-Referenz &uuml;bergeben wird.
     * @precondition s != <TT>null</TT>.
     *
     * @changed OLI 06.10.2009 - Hinzugef&uuml;gt.
     */
    public static String quote(String s, String q) throws NullPointerException {
        if (q != null) {
            s = q.concat(s).concat(q);
        }
        return s;
    }

    @Deprecated
    /**
     * Diese Methode ersetzt in dem angegebenen String s alle Teilstrings p durch den String c.
     *
     * @param s Der String, der als Grundlage f&uuml;r die Ersetzung dienen soll.
     * @param p Das Muster, welches ersetzt werden soll.
     * @param c Der Teilstring, der anstelle des Musters eingesetzt werden soll.
     * @return Der String s mit den entsprechenden Ersetzungen.
     * @throws NullPointerException Falls einer der &uuml;bergebenen String eine
     *         <TT>null</TT>-Referenz ist.
     * @precondition s != <TT>null</TT> &amp;&amp; p != <TT>null</TT> &amp;&amp; c !=
     *         <TT>null</TT>
     *
     * @deprecated OLI 14.08.2009 - Es ist die Methode <TT>java.lang.String.replace(String,
     *         String)</TT> zu nutzen.
     */
    public static String replace(String s, String p, String c) throws NullPointerException {
        int len = p.length();
        int pos = s.indexOf(p);
        if (pos >= 0) {
            s = s.substring(0, pos) + c + replace(s.substring(pos+len), p, c);
        }
        return s;
    }

    /**
     * Diese Methode ersetzt in dem angegebenen String s den ersten Teilstrings p durch den
     * String c.
     *
     * <P>Anstelle dieser Methode kann auch die Methode
     * <TT>org.apache.commons.lang.StringUtils.replaceOnce(String, String, String)</TT> genutzt
     * werden.
     *
     * @param s Der String, der als Grundlage f&uuml;r die Ersetzung dienen soll.
     * @param p Das Muster, welches ersetzt werden soll.
     * @param c Der Teilstring, der anstelle des Musters eingesetzt werden soll.
     * @return Der String s mit den entsprechenden Ersetzungen.
     * @throws NullPointerException Falls einer der &uuml;bergebenen String eine
     *         <TT>null</TT>-Referenz ist.
     * @precondition s != <TT>null</TT> &amp;&amp; p != <TT>null</TT> &amp;&amp; c !=
     *         <TT>null</TT>
     *
     * @changed OLI 01.05.2008 - Hinzugef&uuml;gt.
     */
    public static String replaceFirst(String s, String p, String c) {
        int len = p.length();
        int pos = s.indexOf(p);
        if (pos >= 0) {
            s = s.substring(0, pos).concat(c).concat(s.substring(pos+len));
        }
        return s;
    }

    /**
     * Ersetzt die Umlaute in dem &uuml;bergebenen String anhand der gew&auml;hlten Strategie.
     *
     * @param s Der String, in dem die Umlaute ersetzt werden sollen.
     * @param vmrs Die Austauschstrategie.
     * @return Der String mit den ausgetauschten Umlauten.
     * @throws NullPointerException Falls der String als null-Referenz &uuml;bergeben wird.
     * @precondition s ungleich <TT>null</TT>
     *
     * @changed OLI 04.06.2009 - Hinzugef&uuml;gt.
     */
    public static String replaceVowelMutations(String s, VowelMutationReplaceStrategy vmrs) {
        if (vmrs == VowelMutationReplaceStrategy.SIMPLE_REPLACE) {
            s = s.replace(fromHTML("&auml;"), "a");
            s = s.replace(fromHTML("&ouml;"), "o");
            s = s.replace(fromHTML("&uuml;"), "u");
            s = s.replace(fromHTML("&szlig;"), "ss");
            s = s.replace(fromHTML("&Auml;"), "A");
            s = s.replace(fromHTML("&Ouml;"), "O");
            s = s.replace(fromHTML("&Uuml;"), "U");
        } else {
            s = s.replace(fromHTML("&auml;"), "ae");
            s = s.replace(fromHTML("&ouml;"), "oe");
            s = s.replace(fromHTML("&uuml;"), "ue");
            s = s.replace(fromHTML("&szlig;"), "ss");
            s = s.replace(fromHTML("&Auml;"), "Ae");
            s = s.replace(fromHTML("&Ouml;"), "Oe");
            s = s.replace(fromHTML("&Uuml;"), "Ue");
        }
        return s;
    }

    /**
     * Wandelt einen Wert vom Typ int in eine vorzeichenbehaftete Stringdarstellung um.
     *
     * @param l Der umzuwandelnde Wert.
     */
    public static String signedIntegerToStr(int l) {
        Integer ol = new Integer(l);
        String s = ol.toString();
        if (ol.intValue() > 0) {
           s = "+".concat(s);
        }
        return s;
    }

    /**
     * Liefert einen String mit der angegebenen Zahl von Spaces.
     *
     * @param i Die Anzahl der Spaces, die der String enthalten soll.
     * @return Ein String mit der angegebenen ANzahl von Spaces.
     * @throws IllegalArgumentException Falls eine negative Anzahl von Spaces verlangt wird.
     * @precondition i > 0
     *
     * @changed OLI 04.10.2009 - Hinzugef&uuml;gt.
     */
    public static String spaces(int i) {
        StringBuffer sb = new StringBuffer();
        if (i < 0) {
            throw new IllegalArgumentException("method can not create a string with length "
                    + "less than zero.");
        }
        while (i > 0) {
            sb.append(" ");
            i--;
        }
        return sb.toString();
    }

    /**
     * Liefert eine Liste von String, die sich aus der Aufspaltung des &uuml;bergebenen Strings
     * anhand der Trennzeichen ergibt.
     *
     * @param s Der aufzuspaltende String.
     * @param delim Das oder die Trennzeichen.
     * @return Eine Liste mit den sich durch die Trennzeichen ergebenden Teilstrings bzw. eine
     *         leere Liste, wenn die keine Teilstrings zu bilden sind.
     * @throws NullPointerException Falls der aufzuspaltende String oder das Trennzeichen als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @precondition s != <TT>null</TT> &amp;&amp; delim != <TT>null</TT>
     *
     * @changed OLI 10.03.2009 - Hinzugef&uuml;gt.
     */
    public static List<String> splitToList(String s, String delim) throws NullPointerException {
        List<String> l = new Vector<String>();
        StringTokenizer st = new StringTokenizer(s, delim);
        while (st.hasMoreTokens()) {
            s = st.nextToken();
            l.add(s.trim());
        }
        return l;
    }

    /**
     * Wandelt den angegebenen String in CamilCase-Schreibweise um. Die Trennzeichen werden
     * dabei entfernt.
     *
     * @since 1.11.1
     *
     * @param s Der String, der in CamilCase-Schreibweise umgesetzt werden soll.
     * @param delim Das oder die Trennzeichen.
     * @return Eine Camil-Case-Umwandlung des &uuml;bergebenen Strings.
     * @throws NullPointerException Falls der umzuwandelnde String oder die Trennzeichen als
     *         <TT>null</TT>-Referenz &uuml;bergeben werden.
     * @precondition delim != <TT>null</TT>.
     * @precondition s != <TT>null</TT>.
     *
     * @changed OLI 28.10.2009 - Hinzugef&uuml;gt.
     * @changed OLI 14.01.2010 - &Auml;nderung in der Art, da&szlig; Gro&szlig;buchstaben im
     *         Wort unangetastet bleiben.
     */
    public static String toCamilCase(String s, String delim) throws NullPointerException {
        int i = 0;
        int leni = 0;
        List<String> l = Str.splitToList(s, delim);
        String h = null;
        s = "";
        for (i = 0, leni = l.size(); i < leni; i++) {
            h = l.get(i);
            if (h.length() > 1) {
                h = h.substring(0, 1).toUpperCase().concat(h.substring(1)); // .toLowerCase());
            } else {
                h = h.toUpperCase();
            }
            s = s.concat(h);
        }
        return s;
    }

    /** 
     * Wandelt den angebenen String in einen HTML-String um.
     *
     * @param s Der umzuwandelnde String.
     * @return Die HTML-Umwandlung des Strings.
     * @throws NullPointerException Falls der umzuwandelnde String als <TT>null</TT>-Pointer
     *         &uuml;bergeben wird.
     * @precondition s != <TT>null</TT>.
     *
     * @changed OLI 05.09.2007 - Erweiterung um die Reaktion auf die Property
     *         <TT>corentx.util.Str.suppress.html.note</TT>, die einen Hinweis auf eine Klasse
     *         mit besseren Methoden zur Umsetzung von String in das HTML-Format
     *         unterdr&uuml;ckt. Ich habe die Methode mit Absicht nicht auf <I>deprecated</I>
     *         gesetzt, um schlanke Applikationen auch ohne die
     *         apache-commons-lang-Bibliotheken zu erm&ouml;glichen.
     * @changed OLI 31.01.2008 - Erweiterung um die M&ouml;glichkeit die Methode
     *         <TT>StringEscapeUtils.escapeHtml(String)</TT>, anstatt der eigenen Umsetzung zu
     *         nutzen. Dies geschied durch das Setzen der Property
     *         <I>corentx.util.Str.use.commons</I>.
     */
    public static String toHTML(String s) throws NullPointerException {
        String html = s;
        if (Boolean.getBoolean("corentx.util.Str.use.commons")) {
            return StringEscapeUtils.escapeHtml3(s);
        }
        if (!Boolean.getBoolean("corentx.util.Str.suppress.html.note")) {
            log.warn("Use StringEscapeUtil methods instead of Str.toHTML(String)!");
            log.warn("or suppress this note by setting property \"corentx.util.Str.suppress."
                    + "html.note\"!");
        }
        /*
        html = replace(html, "&", "&amp;");
        html = replace(html, "�", "&auml;");
        html = replace(html, "�", "&Auml;");
        html = replace(html, ">", "&gt;");
        html = replace(html, "<", "&lt;");
        // html = replace(html, " ", "&nbsp;");
        html = replace(html, "�", "&ouml;");
        html = replace(html, "�", "&Ouml;");
        html = replace(html, "ø", "&radic;");
        html = replace(html, "Ø", "&Radic;");
        html = replace(html, "�", "&szlig;");
        html = replace(html, "�", "&uuml;");
        html = replace(html, "�", "&Uuml;");
        html = replace(html, "�", "&Uuml;");
        html = replace(html, "�", "&aacute;");
        html = replace(html, "�", "&eacute;");
        html = replace(html, "�", "&iacute;");
        html = replace(html, "�", "&oacute;");
        html = replace(html, "�", "&uacute;");
        html = replace(html, "�", "&acirc;");
        html = replace(html, "�", "&ecirc;");
        html = replace(html, "�", "&icirc;");
        html = replace(html, "�", "&ocirc;");
        html = replace(html, "�", "&ucirc;");
        */
        for (StrEntity se : StrEntity.values()) {
            if (se.isToSetAtToHTML()) {
                html = html.replace(se.getJavaString(), se.getHTMLEntity());
            }
        }
        return html;
    }

    /**
     * Entfernt die in dem String <TT>toRemove</TT> definierten Zeichen vom Anfang und Ende des
     * Strings <TT>s</TT>.
     *
     * @param s Der String, dessen Anfangs- und Endsequenz von den im String <TT>toRemove</TT>
     *         definierten Zeichen bereinigt werden sollen.
     * @param toRemove Die Zeichen, die gegebenenfalls entfernt werden sollen, sofern sie am
     *         Anfang bzw. Ende des Strings <TT>s</TT> stehen.
     * @return Der String <TT>s</TT> mit der entsprechend bereinigten Anfangs- und Endsequenz.
     * @throws NullPointerException falls wenigstens eines der beiden Argumente eine
     *         <TT>null</TT>-Referenz ist.
     * @precondition s != <TT>null</TT> &amp;&amp; toRemove != <TT>null</TT>
     *
     * @changed OLI 13.03.2009 - Hinzugef&uuml;gt.
     */
    public static String trim(String s, String toRemove) throws NullPointerException {
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
            i = len-1;
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
     * Wandelt das &uuml;bergebene Zeichen in einen Gro&szlig;buchstaben um. Nicht die 
     * effizienteste Art ein Zeichen umzuwandeln, aber eine funktionsf&auml;hige.
     *
     * @param c Der umzuwandelnde Buchstabe.
     * @return Der Gro&szlig;buchstabe zu c.
     */
    public static char upperCase(char c) {
        return ("" + c).toUpperCase().charAt(0);
    }

}
