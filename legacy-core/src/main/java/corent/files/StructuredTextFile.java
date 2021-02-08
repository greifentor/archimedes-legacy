/*
 * StructuredTextFile.java
 *
 * 23.01.2000
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.files;


import java.io.*;
import java.util.*;

import corent.base.*;


/**
 * Durch diese Klasse k&ouml;nnen strukturierte Textdateien angelegt, gelesen und aktualisiert
 * werden. Hierbei handelt es sich um sehr vereinfachtes XML-Format, das nur Anfangs- und 
 * Ende-Tags akzeptiert, die in XML-Notation angegeben werden m&uuml;ssen. Es k&ouml;nnen also
 * keine Attribute definiert werden. Als Inhalte werden Strings in HTML-Schreibweise akzeptiert.
 * <P><I>Beispiel:
 * <PRE>
 * &lt;Adresse&gt;
 *     &lt;Strasse&gt;
 *         &lt;Name&gt;Beispielweg&lt;/Name&gt;
 *         &lt;Nummer&gt;42&lt;/Nummer&gt;
 *     &lt;/Strasse&gt;
 *     &lt;PLZ&gt;04711&lt;/PLZ&gt;
 *     &lt;Ort&gt;Exempelstadt&lt;/Ort&gt;
 * &lt;/Adresse&gt;
 * </PRE></I>
 * <P>Eine Instanzierung der Klasse erzeugt lediglich ein Objekt der Klasse und bef&uuml;llt 
 * dieses mit dem Dateinamen der Datei, die mit der Instanz verbunden ist. Das eigentliche 
 * Einlesen des Dateiinhaltes wird &uuml;ber den Aufruf der Methode <TT>load()</TT> initiiert.
 * <P><I>Beispiel:
 * <PRE>
 * StructuredTextFile stf = new StructuredTextFile("Adressen.stf"));
 * try {
 *     stf.load();
 * } catch (FileNotFoundException fnfe) {
 *     System.out.println("Datei nicht gefunden!");
 * } catch (IOException ioe) {
 *     System.out.println("Probleme beim Einlesen der Datei!");
 * } 
 * </PRE></I>
 *
 * <P>Die Informationen k&ouml;nnen anschlie&szlig;end &uuml;ber einen Satz von 
 * <TT>read...</TT>-Methoden aus dem Objekt gelesen werden.
 * <P><I>Beispiel:
 * <PRE>
 * long ln = stf.readLong(new String[] {"Adresse", "PLZ"}, 4711);
 * </PRE></I>
 * Diese Anweisung lie&szlig;t beispielsweise die PLZ des Adressendatensatzes aus der oben 
 * angegebenen Beispieldatei. Sollte f&uuml;r die PLZ kein Eintrag in der Datei vorliegen, so 
 * wird der Defaultwert "4711" zur&uuml;ckgeliefert. Der Satz der <TT>read...</TT>-Methoden ist 
 * hierbei auf solche f&uuml;r die Datentypen mit den gr&ouml;&szlig;ten Wertebereichen 
 * beschr&auml;nkt. Zum Einlesen von Daten in Variablen mit kleineren Wertebereichen (z. B. PLZ 
 * als <TT>int</TT>), mu&szlig; der Entwickler einen entsprechenden Cast vornehmen.
 *
 * <P>Analog zu den <TT>read...</TT>-Methoden gibt es entsprechende <TT>write...</TT>-Methoden.
 * <P><I>Beispiel:
 * <PRE>
 * stf.writeLong(new String[] {"Adresse", "PLZ"}, 43);
 * </PRE></I>
 * Mit Hilfe dieses Aufrufs wird der Wert "43" unter dem Pfad "Adresse/PLZ" im stf-Objekt 
 * abgelegt.
 *
 * <P>Um die Datei auf den Datentr&auml;ger zu speichern, mu&szlig; die <TT>save()</TT>-Methode
 * explizit aufgerufen werden. Sie kann eine <TT>IOException</TT> werfen, die entsprechend 
 * abgefangen werden mu&szlig;.
 * <P><I>Beispiel:
 * <PRE>
 * try {
 *     stf.save();
 * } catch (IOException ioe) {
 *     System.out.println("Probleme beim Schreiben der Datei!");
 * } 
 * </PRE></I>
 *
 * <P>Zus&auml;tzlich zu den beschriebenen Standardf&auml;llen, gibt es eine Reihe von Methoden,
 * die zur Bearbeitung der hinter dem <TT>StructuredTextFile</TT> stehenden Baumstruktur dienen.
 * Mit Hilfe der Methoden <TT>getLeafPathes()</TT> k&ouml;nnen beispielsweise die Pfade 
 * ermittelt werden, die keine eigenen Unterknoten haben, also Bl&auml;tter sind.
 * <P><I>Beispiel:
 * <PRE>
 * Vector v = stf.getLeafPathes();
 * </PRE>
 * v = ({"Adresse", "Strasse", "Name"}, {"Adresse", "Strasse", "Nummer"}, {"Adresse", "PLZ"},
 * {"Adresse", "Ort"}).
 * </I>
 *
 * <P>&Uuml;ber die Funktionsweise der anderen Funktionen geben die Dokumentationen zu den 
 * einzelnen Methoden Auskunft.
 *
 * <P><B>Anmerkung - 1:</B> &Uuml;ber die Property 
 * <I>corent.files.StructuredTextFile.suppress.FileNotFound</I> (Boolean) kann innerhalb der 
 * <TT>load()</TT>-Methode eine FileNotFoundException vermieden werden. Dies ist f&uuml;r 
 * F&auml;lle vorgesehen, in denen damit gerechnet wird, da&szlig; eine Datei nicht vorgefunden 
 * werden kann. Die Lesemethoden der Klasse bieten f&uuml;r diesen Umstand die M&ouml;glichkeit,
 * festvorgegebene Defaultwerte zu definieren.
 *
 * <P><B>Anmerkung - 2:</B> StructuredTextFiles, die &uuml;ber den Konstruktor mit dem 
 * InputStream-Parameter erzeugt worden sind, werfen bei einem Schreibversuch eine Exception.
 * Wenn Sie diese Klasse benutzen m&ouml;chten, um Daten zu speichern, benutzen Sie den 
 * Konstruktor <TT>StructuredTextFile(String)</TT>. Dies l&auml;&szlig;t sich umgehen, indem ein
 * Dateiname &uuml;ber den Aufruf der Methode <TT>setFilename(String)</TT> definiert wird. In 
 * diesem Fall werden die Daten des Objektes in die spezifizierte Datei geschrieben.
 * 
 * @author 
 *     <P>O.Lieshoff
 *     <P>
 *
 * @changed 
 *     <P>OLI 07.10,2007 - Erweiterung um den Konstruktor, der als Parameter einen InputStream 
 *             akzeptiert. Anpassung der <TT>load()</TT> und der <TT>save()</TT>-Methode an 
 *             diese Betriebsvariante.
 *     <P>OLI 16.08.2008 - Erweiterung um die M&ouml;glichkeit, eine komprimierte Form der Datei
 *             ohne Formatierung abzulegen.
 *
 */

public class StructuredTextFile {

    /** 
     * Referenz auf den InputStream, aus der die Informationen zur Instanz gelesen werden 
     * sollen.
     */
    protected InputStream instr = null;
    /** Name der Dateiname. */
    protected String dateiname = null;
    /** Der (strukturierte) Inhalt der Datei. */
    protected TreeMap dateiinhalt = new TreeMap();

    /* 
    * Diese Flagge mu&szlig; gel&ouml;scht werden, wenn der Dateiinhalt nicht HTML-kodiert 
    * werden soll. 
    */
    private boolean htmlKodieren = true;

    /**
     * Generiert ein Objekt zur Verwaltung einer strukturierten Textdatei im Speicher des
     * Rechners und weist ihm den &uuml;bergebenen Namen als Dateinamen zu, in den die Datei
     * zu speichern ist.
     *
     * @param dateiname Der Name der Datei, in die das Objekt geschrieben werden soll.
     */
    public StructuredTextFile(String dateiname) {
        super();
        this.setFilename(dateiname);
    }
    
    /**
     * Generiert ein Objekt zur Verwaltung einer strukturierten Textdatei im Speicher des
     * Rechners und weist ihm den InputStream zu, aus dem der Inhalt des Objektes geladen werden
     * soll.
     * <P><I>Hinweis:</I> Sie k&ouml;nnen diesen Konstruktor <U>nicht</U> benutzen, wenn Sie die
     * Datei nach einer eventuellen Bearbeitung wieder speichern m&ouml;chten. 
     *
     * @param in Ein InputStream, &uuml;ber den die Datei eingelesen werden soll.
     *
     * @changed
     *     OLI 07.10.2007 - Hinzugef&uuml;gt.
     */
    public StructuredTextFile(InputStream in) {
        super();
        this.instr = in;
    }
    
    
    /* Accessoren und Mutatoren. */

    /**
     * Accessor f&uuml;r die Eigenschaft HTMLCoding
     *
     * @return Der Wert der Eigenschaft HTMLCoding
     */
    public boolean isHTMLCoding() {
        return this.htmlKodieren;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft HTMLCoding
     *
     * @param htmlKodieren Der neue Wert f&uuml;r die Eigenschaft HTMLCoding.
     */
    public void setHTMLCoding(boolean htmlKodieren) {
        this.htmlKodieren = htmlKodieren;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Filename
     *
     * @return Der Wert der Eigenschaft Filename
     */
    public String getFilename() {
        return this.dateiname;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Filename
     *
     * @param dateiname Der neue Wert f&uuml;r die Eigenschaft Filename.
     */
    public void setFilename(String dateiname) {
        if (dateiname == null) {
            dateiname = "";
        }
        this.dateiname = dateiname;
    }


    /** 
     * L&auml;dt die Daten aus der Datei in den Speicher.
     *
     * @throws FileNotFoundException Wenn der Dateiname eine <TT>null</TT>-Referenz ist oder die
     *     Datei nicht existiert. Diese Exception wird nur dann geworfen, wenn die Property
     *     "corent.files.StructuredTextFile.suppress.FileNotFound" (Boolean) gesetzt (true) 
     *     wird.
     * @throws IOException Falls beim Einlesen der Datei ein Fehler auftritt.
     *
     * @changed
     *     OLI 07.10.2007 - Erweiterung um die Reaktion auf das Attribut <TT>instr</TT>. Ist es
     *             ungleich <TT>null</TT>, wird diese Referenz als Quelle zum Einlesen der
     *             Objektinhalte genutzt.
     */
    public void load() throws IOException, FileNotFoundException {
        if (((this.dateiname != null) && (new File(this.dateiname).exists())) 
                || (this.instr != null)) {
            BufferedReader in = null;
            FileReader fr= (this.instr == null ? new FileReader(dateiname) : null);
            int len = 0;
            String s = null;
            String s0 = null;
            String s1 = null;
            Vector pfad = new Vector();
            Vector v = null;
            this.dateiinhalt = new TreeMap();
            in = (this.instr == null ? new BufferedReader(fr) : new BufferedReader(
                    new InputStreamReader(this.instr)));
            v = this.readTokens(in);
            in.close();
            if (fr != null) {
                fr.close();
            }
            len = v.size();
            for (int i = 0; i < len; i++) {
                s = (String) v.elementAt(i);
                if (i+1 < len) {
                    s1 = (String) v.elementAt(i+1);
                } else {
                    s1 = null;
                }
                if (this.isStartToken(s)) {
                    pfad.addElement(this.getTagName(s));
                } else if (this.isEndToken(s)) {
                    if (pfad.size() > 0) {
                        pfad.removeElementAt(pfad.size()-1);
                    }
                } else if ((s0 != null) && (this.isStartToken(s0)) && (s1 != null)
                        && (this.isEndToken(s1))) {
                    this.writeObject(this.dateiinhalt, new Vector(pfad), (this.isHTMLCoding() ?
                            StrUtil.FromHTML(s) : s));
                }
                s0 = s;
            }
        } else if (!Boolean.getBoolean("corent.files.StructuredTextFile.suppress.FileNotFound"))
                {
            throw new FileNotFoundException("ERROR: file " + this.dateiname + " not found!");
        }
    }

    /** 
     * Schreibt die Daten aus dem Speicher in die Datei. Das Schreiben wird formatiert mit
     * Einr&uuml;ckungen und Zeilenumbr&uuml;chen durchgef&uuml;hrt.
     */
    public void save() throws IOException {
        this.save(true);
    }
    
    /** 
     * Schreibt die Daten aus dem Speicher in die Datei.
     *
     * @param format Diese Flagge mu&szlig; gesetzt sein, wenn die Datei formatiert mit 
     *     Einr&uuml;ckungen und Zeilenumbr&uuml;chen geschrieben werden sollen.
     *
     * @changed
     *     <P>OLI 16.08.2008 - Um Parameter format erweitert.
     *
     */
    public void save(boolean format) throws IOException {
        if (this.dateiname != null) {      
            BufferedWriter writer = null;
            FileWriter fw = new FileWriter(this.dateiname, false);
            writer = new BufferedWriter(fw);
            writer.write(this.toString(this.dateiinhalt, null, (format ? "" : null), true));
            writer.flush();
            writer.close();
            fw.close();
        } else if (this.instr != null) {
            throw new IOException("StructuredTextFiles can not be saved, when instantiated with"
                    + " constructor StructuredTextFile(InputStream), excepted if a filename "
                    + "has be defined. Note that the filename marks usually not the file, which"
                    + "has been red.");
        }
    }

    /** @return Eine Liste der Tokens des Readers. */
    private Vector readTokens(BufferedReader in) throws IOException {
        boolean red = false;
        boolean trim = Boolean.getBoolean("corent.files.StructuredTextFile.trim");
        char c = '\0';
        String s = "";
        Vector v = new Vector();
        if (in.ready()) {
            c = (char) in.read();
            s = s.concat("" + c);
            while (in.ready()) {
                c = (char) in.read();
                if ((c == '\n') || (c == '\t') || (c == '\r') || (c == '\0')) {
                    c = ' ';
                }
                if (c == '<') {
                    if (red) {
                        if (trim) {
                            s = s.trim();
                        }
                        v.addElement(s);
                    }
                    s = new String("");
                    red = false;
                }
                s = s.concat("" + c);
                if (c == '>') {
                    v.addElement(s.trim());
                    s = "";
                    red = false;
                } else {
                    red = true;
                }
            }
        }
        return v;
    }

    /** @return Der Name des Tags ohne spitze Klammern und Slash. */
    private String getTagName(String s) {
        if (s.charAt(0) == '<') {
            s = s.substring(1, s.length()-1);
        }
        if (s.charAt(0) == '/') {
            s = s.substring(1, s.length()-1);
        }
        if (s.charAt(s.length()-1) == '<') {
            s = s.substring(0, s.length()-2);
        }
        return s;
    }

    /**
     * @return <TT>true</TT>, wenn der String ein Starttoken enth&auml;lt,<BR>
     *     <TT>false</TT> sonst.
     */
    private boolean isStartToken(String s) {
        if ((s != null) && (s.length() > 0) && (s.charAt(0) == '<') && (s.charAt(1) != '/')) {
            return true;
        }
        return false;
    }

    /**
     * @return <TT>true</TT>, wenn der String ein Starttoken enth&auml;lt,<BR>
     *     <TT>false</TT> sonst.
     */
    private boolean isEndToken(String s) {
        if ((s != null) && (s.length() > 0) && (s.charAt(0) == '<') && (s.charAt(1) == '/')) {
            return true;
        }
        return false;
    }

    /**
     * @return <TT>true</TT>, wenn der String ein Valuetoken enth&auml;lt,<BR>
     *     <TT>false</TT> sonst.
     * /
    private boolean isValueToken(String s) {
        if ((s != null) && (s.length() > 0) && (s.charAt(0) != '<')) {
            return true;
        }
        return false;
    } */

    /**
     * Getypte Schreiboperation f&uuml;r Ganzzahlen
     *
     * @param pfad Ein Feld mit den einzelnen Feldnamen des Pfades.
     * @param l Ein Longwert, der geschrieben werden soll.
     */
    public void writeLong(String[] pfad, long l) {
        this.writeObject(pfad, "" + l);
    }

    /**
     * Getypte Schreiboperation f&uuml;r Flie&szlig;punktzahlen
     *
     * @param pfad Ein Feld mit den einzelnen Feldnamen des Pfades.
     * @param d Ein Doublewert, der geschrieben werden soll.
     */
    public void writeDouble(String[] pfad, double d) {
        this.writeObject(pfad, "" + d);
    }

    /**
     * Getypte Schreiboperation f&uuml;r Strings
     *
     * @param pfad Ein Feld mit den einzelnen Feldnamen des Pfades.
     * @param s Ein String, der geschrieben werden soll.
     */
    public void writeStr(String[] pfad, String s) {
        this.writeObject(pfad, s);
    }

    /**
     * Schreibt das &uuml;bergebene Object an die durch den ebenfalls &uuml;bergebenen
     * spezifizierte Stelle.
     *
     * @param pfad Ein Feld mit den einzelnen Feldnamen des Pfades.
     * @param obj Das zu schreibende Objekt.
     */
    public void writeObject(String[] pfad, Object obj) {
        Vector v = new Vector();
        for (int i = 0; i < pfad.length; i++) {
            v.addElement(pfad[i]);
        }
        this.writeObject(this.dateiinhalt, v, obj);
    }

    /** Tats&auml;chliche Ausf&uuml;rhung einer Schreibaktion. */
    private void writeObject(TreeMap cont, Vector pfad, Object obj) {
        Object o = null;
        String s = (String) pfad.elementAt(0);
        pfad.removeElementAt(0);
        o = cont.get(s);
        if (pfad.size() > 0) {
            if (o == null) {
                o = new TreeMap();
                cont.put(s, o);
            }
            this.writeObject((TreeMap) o, pfad, obj);
        } else {
            cont.put(s, obj);
        }
    }

    /**
     * Getypte Leseoperation f&uuml;r Ganzzahlen
     *
     * @param pfad Ein Feld mit den einzelnen Feldnamen des Pfades.
     * @param def Defaultwert, falls der angegebene Pfad nicht existiert.
     * @return Der unter dem angegebenen Namen gespeicherte Long-Wert.
     */
    public long readLong(String[] pfad, long def) {
        try {
            return Long.parseLong((String) this.readObject(pfad, "" + def).toString());
        } catch (Exception e) {
        }
        return def;
    }

    /**
     * Getypte Leseoperation f&uuml;r Flie&szlig;pubktzahlen
     *
     * @param pfad Ein Feld mit den einzelnen Feldnamen des Pfades.
     * @param def Defaultwert, falls der angegebene Pfad nicht existiert.
     * @return Der unter dem angegebenen Namen gespeicherte Double-Wert.
     */
    public double readDouble(String[] pfad, double def) {
        try {
            return Double.parseDouble(this.readObject(pfad, "" + def).toString());
        } catch (Exception e) {
        }
        return def;
    }

    /**
     * Getypte Leseoperation f&uuml;r Strings.
     *
     * @param pfad Ein Feld mit den einzelnen Feldnamen des Pfades.
     * @param def Defaultwert, falls der angegebene Pfad nicht existiert.
     * @return Der unter dem angegebenen Namen gespeicherte String-Wert.
     */
    public String readStr(String[] pfad, String def) {
        return ((String) this.readObject(pfad, def));
    }

    /**
     * Liest ein Objekt aus dem angegebenen Pfad. Falls kein passendes Objekt gefunden wird,
     * wird der angegebene Defaultwert zur&uuml;ckgeliefert.
     *
     * @param pfad Ein Feld mit den einzelnen Feldnamen des Pfades.
     * @param defobj Ein Defaultwert zum Objekt, falls dieses nicht gefunden werden kann.
     * @return Eine Objektreferenz auf das gefundene Objekt oder eine auf den Defaultwert.
     */
    public Object readObject(String[] pfad, Object defobj) {
        Vector v = new Vector();
        for (int i = 0; i < pfad.length; i++) {
            v.addElement(pfad[i]);
        }
        return this.readObject(this.dateiinhalt, v, defobj);
    }

    /** Tats&auml;chliche Ausf&uuml;rhung einer Leseaktion. */
    private Object readObject(TreeMap cont, Vector pfad, Object defobj) {
        Object o = null;
        String s = (String) pfad.elementAt(0);
        pfad.removeElementAt(0);
        o = cont.get(s);
        if (o != null) {
            if ((o instanceof TreeMap) && (pfad.size() > 0)) {
                return this.readObject((TreeMap) o, pfad, defobj);
            } else if (!(o instanceof TreeMap)) {
                return o;
            }
        }
        return defobj;
    }

    /** @return Eine geeignete Stringrepr&auml;sentation des Objects. */
    public String toString() {
        return this.toString(this.dateiinhalt, "root", "", false);
    }

    /** @return Eine geeignete Stringrepr&auml;sentation des Objects. */
    public String toString(boolean doof) {
        return this.toString(this.dateiinhalt, null, "", true);
    }

    /**
     * Der tats&auml;chliche Aufbau des Strings.
     *
     * @param ht Der Einstiegspunkt zum Generieren des Strings.
     * @param name Der Name des gerade produzieren Knotens.
     * @param indent Eine Grundeinr&uuml;ckung. Wird dieser Parameter mit dem Wert <TT>null</TT>
     *     &uuml;bergeben, so wird auf das Schreiben von Einr&uuml;ckungen und 
     *     Zeilenumbr&uuml;chen verzichtet.
     * @param file Diese Option mu&szlig; gesetzt werden, wenn der String f&uuml;r eine 
     *     Dateiausgabe produziert werden soll.
     *
     * @changed
     *     <P>OLI 16.08.2008 - Erweiterung um die Option auf Einr&uuml;ckungen und 
     *             Zeilenumbr&uuml;che zu verzichten, in dem der indent-Parameter mit 
     *             <TT>null</TT> &uuml;bergeben wird.
     *
     */
    protected String toString(TreeMap ht, String name, String indent, boolean file) {
        Object o = null;
        String key = null;
        String s = "";
        if (!file) {
            s = (indent != null ? indent : "") + name + (indent != null ? "\n" : "");
        } else if (name != null) {
            s = (indent != null ? indent : "") + "<" + name + ">" + (indent != null ? "\r\n" 
                    : "");
        }
        for (Iterator it = ht.keySet().iterator(); it.hasNext(); ) {
            key = (String) it.next();
            o = ht.get(key);
            if (o instanceof TreeMap) {
                s = s.concat(this.toString((TreeMap) o, key, (indent != null ? indent 
                        + (name != null ? "    " : "") : null), file));
            } else {
                s = s.concat((indent != null ? indent + "    " : "")  + "<" + key + ">" + (
                        this.isHTMLCoding() ? StrUtil.ToHTML(o.toString()) : o.toString()) + 
                        "</" + key + ">" + (indent != null ? (file ? "\r" : "") + "\n" : ""));
            }
        }
        if ((file) && (name != null)) {
            s = s.concat((indent != null ? indent : "") + "</" + name + ">" + (indent 
                    != null ? "\r\n" : ""));
        }
/*        
        for (Iterator it = ht.keySet().iterator(); it.hasNext(); ) {
            key = (String) it.next();
            o = ht.get(key);
            if (o instanceof TreeMap) {
                s = s.concat(this.toString((TreeMap) o, key, ident + (name != null ? "    "
                        : ""), file));
            } else {
                s = s.concat(ident + "    <" + key + ">" + (this.isHTMLCoding() ?
                        StrUtil.ToHTML(o.toString()) : o.toString()) + "</" + key + ">"
                        + (file ? "\r" : "") + "\n");
            }
        }
        if ((file) && (name != null)) {
            s = s.concat(ident + "</" + name + ">\r\n");
        }
*/      
        return s;
    }

    /**
     * Eine Ansicht des angegebenen Teilbaumes.
     *
     * @param root Die Wurzel des anzuzeigenden Teilbaumes.
     */
    public String getTeilbaumAnsicht(TreeMap root, String name, String ident) {
        Object o = null;
        String key = null;
        String s = "";
        s = new String(ident + "+ " + name + "\n");
        for (Iterator it = root.keySet().iterator(); it.hasNext(); ) {
            key = (String) it.next();
            o = root.get(key);
            if (o instanceof TreeMap) {
                s = s.concat(this.getTeilbaumAnsicht((TreeMap) o, key, ident + (name != null 
                        ? "    " : "")));
            } else {
                s = s.concat(ident + "    " + key + ": " + StrUtil.FromHTML(o.toString()) + "\n"
                        );
            }
        }
        return s;
    }

    /**
     * L&ouml;scht das Objekt zum Pfad.
     *
     * @param path Ein String-Array mit den Namen der Knoten im Pfad.
     */
    public void remove(String[] path) {
        if (path.length < 1) {
            this.dateiinhalt = new TreeMap();
            return;
        }
        Object o = null;
        String[] path0 = new String[path.length-1];
        for (int i = 0; i < path.length-1; i++) {
            path0[i] = path[i];
        }
        o = this.getNode(path0);
        if (o instanceof TreeMap) {
            ((TreeMap) o).remove(path[path.length-1]);
        }
    }

    /**
     * @param node Ein Objekt aus der Baumstruktur zu dem der Pfad ermittelt werden soll.
     * @return Der ermittelte Pfad oder <TT>null</TT>, wenn das Objekt nicht gefunden werden
     *     konnte.
     */
    public String[] getPath(Object node) {
        return searchPath(this.dateiinhalt, node);
    }

    /**
     * @param root Der Knoten, der als Wurzel, des aktuellen Teilbaumes genutzt wird.
     * @param node Ein Objekt aus der Baumstruktur zu dem der Pfad ermittelt werden soll.
     * @return Der ermittelte Pfad oder <TT>null</TT>, wenn das Objekt nicht gefunden werden
     *     konnte.
     */
    protected String[] searchPath(TreeMap root, Object node) {
        Object k = null;
        Object o = null;
        String[] path = null;
        for (Iterator it = root.keySet().iterator(); it.hasNext(); ) {
            k = it.next();
            o = root.get(k);
            if (o == node) {
                return new String[] {k.toString()};
            } else if (o instanceof TreeMap) {
                path = this.searchPath((TreeMap) o, node);
                if (path != null) {
                    String[] path0 = new String[path.length+1];
                    path0[0] = k.toString();
                    for (int i = 0; i < path.length; i++) {
                        path0[i+1] = path[i];
                    }
                    return path0;
                }
            }
        }
        return null;
    }

    /**
     * @param path Ein String-Array mit den Namen der Knoten im Pfad.
     * @return Der Knoten innerhalb des Baumes zum angegebenen Pfad bzw. <TT>null</TT>, wenn zu
     *     der Pfadangabe kein Knoten gefunden werden konnte.
     */
    public Object getNode(String[] path) {
        return this.getTreeNode(path, this.dateiinhalt);
    }

    /**
     * @param path Ein String-Array mit den Namen der Knoten im Pfad.
     * @param root Der Knoten, der als Wurzel zur weiteren Bearbeitung dienen soll.
     * @return Der Knoten innerhalb des Baumes zum angegebenen Pfad bzw. <TT>null</TT>, wenn zu
     *     der Pfadangabe kein Knoten gefunden werden konnte.
     */
    protected Object getTreeNode(String[] path, TreeMap root) {
        Object k = null;
        Object o = null;
        String[] path0 = null;
        if (path.length < 1) {
            return root;
        }
        for (Iterator it = root.keySet().iterator(); it.hasNext(); ) {
            k = it.next();
            o = root.get(k);
            if ((path.length == 1) && (k.equals(path[0]) && (!(o instanceof TreeMap))) ) {
                return o;
            } else if ((k.equals(path[0])) && (o instanceof TreeMap)) {
                path0 = new String[path.length-1];
                for (int i = 0; i < path.length-1; i++) {
                    path0[i] = path[i+1];
                }
                return getTreeNode(path0, (TreeMap) o);
            }
        }
        return null;
    }

    /**
     * Liefert einen Vector mit den Pfadangaben aller enthaltenen Felder.
     *
     * @return Vector(String[]) mit den Pfadangaben zu allen Felder des StructuredTextFile.
     */
    public Vector getPathes() {
        return this.getPathes(this.dateiinhalt, true);
    }

    /**
     * Liefert einen Vector mit den Pfadangaben aller enthaltenen Felder.
     *
     * @param root Der Knoten, der als Wurzel zur weiteren Bearbeitung dienen soll.
     * @param recurse Wird diese Option gesetzt, so werden auch die unterhalb des Knotens
     *     liegenden Knoten des Baumes beachtet.
     * @return Vector(String[]) mit den Pfadangaben zu allen Felder des StructuredTextFile.
     */
    public Vector getPathes(TreeMap root, boolean recurse) {
        return this.getPathes(root, new String[] {}, false, true, recurse);
    }

    /**
     * Liefert einen Vector mit den Pfadangaben aller enthaltenen Felder, die Bl&auml;tter sind.
     *
     * @return Vector(String[]) mit den Pfadangaben zu allen Felder des StructuredTextFile, die
     *     Bl&auml;tter sind.
     */
    public Vector getLeafPathes() {
        return this.getLeafPathes(this.dateiinhalt, true);
    }

    /**
     * Liefert einen Vector mit den Pfadangaben aller enthaltenen Felder, die Bl&auml;tter sind.
     *
     * @param root Der Knoten, der als Wurzel zur weiteren Bearbeitung dienen soll.
     * @param recurse Wird diese Option gesetzt, so werden auch die unterhalb des Knotens
     *     liegenden Knoten des Baumes beachtet.
     * @return Vector(String[]) mit den Pfadangaben zu allen Felder des StructuredTextFile, die
     *     Bl&auml;tter sind.
     */
    public Vector getLeafPathes(TreeMap root, boolean recurse) {
        return this.getPathes(root, new String[] {}, false, false, recurse);
    }

    /**
     * Liefert einen Vector mit den Pfadangaben aller enthaltenen Felder, die Bl&auml;tter sind.
     * Die Pfade sind zum Wurzelknoten absolut.
     *
     * @param root Der Knoten, der als Wurzel zur weiteren Bearbeitung dienen soll.
     * @param recurse Wird diese Option gesetzt, so werden auch die unterhalb des Knotens
     *     liegenden Knoten des Baumes beachtet.
     * @return Vector(String[]) mit den Pfadangaben zu allen Felder des StructuredTextFile, die
     *     Bl&auml;tter sind.
     */
    public Vector getLeafRootPathes(TreeMap root, boolean recurse) {
        String[] rootpath = this.getPath(root);
        return this.getPathes(root, rootpath, false, true, recurse);
    }

    /**
     * Liefert einen Vector mit den Pfadangaben aller enthaltenen Felder, die keine Bl&auml;tter
     * sind.
     *
     * @return Vector(String[]) mit den Pfadangaben zu allen Felder des StructuredTextFile, die
     *     keine Bl&auml;tter sind.
     */
    public Vector getNonLeafPathes() {
        return this.getNonLeafPathes(this.dateiinhalt, true);
    }

    /**
     * Liefert einen Vector mit den Pfadangaben aller enthaltenen Felder, die keine Bl&auml;tter
     * sind.
     *
     * @param root Der Knoten, der als Wurzel zur weiteren Bearbeitung dienen soll.
     * @param recurse Wird diese Option gesetzt, so werden auch die unterhalb des Knotens
     *     liegenden Knoten des Baumes beachtet.
     * @return Vector(String[]) mit den Pfadangaben zu allen Felder des StructuredTextFile, die
     *     keine Bl&auml;tter sind.
     */
    public Vector getNonLeafPathes(TreeMap root, boolean recurse) {
        return this.getPathes(this.dateiinhalt, new String[] {}, true, true, recurse);
    }

    /**
     * @param dateiinhalt Der Teilbaum, dessen Pfadangaben ermittelt werden sollen.
     * @param root Die Pfadangaben zur Wurzel des Teilbaumes.
     * @param exceptLeafs Diese Option klammert, so sie gesetzt ist, Pfadangaben aus, die zu
     *     Bl&auml;ttern f&uuml;hren.
     * @param rootPathes Diese Option bezieht die Pfadangabe der einzelnen Kontenwurzeln mit in
     *     die Ergebnismenge ein.
     * @param recurse Wird diese Option gesetzt, so werden auch die unterhalb des Knotens
     *     liegenden Knoten des Baumes beachtet.
     * @return Die Pfade des angegebenen Teilbaumes in einem Vector(String[]).
     */
    protected Vector getPathes(TreeMap dateiinhalt, String[] root, boolean exceptLeafs,
            boolean rootPathes, boolean recurse) {
        int i = 0;
        Object k = null;
        Object o = null;
        String[] pathes = null;
        Vector erg = new Vector();
        Vector erg0 = new Vector();
        if (rootPathes) {
            pathes = new String[root.length];
            for (i = 0; i < root.length; i++) {
                pathes[i] = root[i];
            }
            if (pathes.length > 0) {
                erg.addElement(pathes);
            }
        }
        for (Iterator it = dateiinhalt.keySet().iterator(); it.hasNext(); ) {
            k = it.next();
            o = dateiinhalt.get(k);
            pathes = new String[root.length+1];
            for (i = 0; i < root.length; i++) {
                pathes[i] = root[i];
            }
            pathes[i] = (String) k;
            if ((o instanceof TreeMap) && recurse) {
                erg0 = this.getPathes((TreeMap) o, pathes, exceptLeafs, rootPathes, recurse);
                for (i = 0; i < erg0.size(); i++) {
                    erg.addElement(erg0.elementAt(i));
                }
            } else if (!exceptLeafs) {
                erg.addElement(pathes);
            }
        }
        return erg;
    }

    public static String[] AddPath(String[] arr, String s) {
        int i = 0;
        String[] zwerg = new String[arr.length+(s != null ? 1 : 0)];
        for (i = 0; i < arr.length; i++) {
            zwerg[i] = arr[i];
        }
        if (s != null) {
            zwerg[i] = s;
        }
        return zwerg;
    }

    public static String[] TailPath(String[] arr) {
        if (arr.length <= 1) {
            return new String[0];
        }
        int i = 0;
        String[] zwerg = new String[arr.length-1];
        for (i = 1; i < arr.length; i++) {
            zwerg[i-1] = arr[i];
        }
        return zwerg;
    }

    public static String[] PrefixPath(String[] arr, String s) {
        int i = 0;
        int n = (s != null ? 1 : 0);
        String[] zwerg = new String[arr.length+n];
        if (s != null) {
            zwerg[0] = s;
        }
        for (i = 0; i < arr.length; i++) {
            zwerg[i+n] = arr[i];
        }
        return zwerg;
    }

}