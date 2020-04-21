/*
 * Feiertag.java
 *
 * 17.03.2003
 *
 * (c) by O.Lieshoff
 *
 * Benutzung durch MediSys erfolgt mit freundlicher Genehmigung des Autors.
 *
 */

package corent.dates;


import java.io.*;
import java.util.*;


/**
 * Mit Hilfe dieser Klasse kann ermittelt werden, ob es sich bei einem angegebenen Datum um
 * einen Feiertag handelt. Die Standardeinstellung gilt f&uuml;r Berlin.
 *
 *
 * <H2>Konfiguration &uuml;ber Datei</H2>
 *
 * <P>Die Feiertagsklasse kann durch eine Konfigurationsdatei um weitere Feiertage erweitert
 * werden oder sogar mit vollkommen neuen Feiertagen konfiguriert werden. Es handelt sich um 
 * eine Textdatei die folgendes Format akzeptiert:
 * <TABLE BORDER=1>
 *     <TR>
 *         <TD>Kommando</TD>
 *         <TD>Beschreibung</TD>
 *     </TR>
 *     <TR>
 *         <TD>"#" Kommentar</TD>
 *         <TD>
 *             Beginnt eine Zeile der Datei mit der Raute, so wird sie als Kommentar 
 *             &uuml;bersprungen
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD>"clear"</TD>
 *         <TD>
 *             L&ouml;scht die bereits definierten Feiertagsregeln. Geben Sie dieses Kommando
 *             immer am Anfang der Datei. Davor in der Datei angegebene Regeln werden ebenfalls
 *             zur&uuml;ckgesetzt.
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD>"os" TT.MM.JJJJ Bezeichnung</TD>
 *         <TD>
 *             Legt einen Feiertag fest, zu dem andere Feiertage in Beziehung gesetzt werden
 *             k&ouml;nnen (variable Feiertage). In der westlichen Welt wird es sich 
 *             normalerweise um den Ostersonntag handeln.
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD>"vf" Tage Bezeichnung</TD>
 *         <TD>
 *             Definition eines variablen Feiertages. Die Angabe der Tage ist das Offset zum 
 *             definierten "Ostersonntag". F&uuml;r variable Feiertage, die vor dem 
 *             Bezugsfeiertag liegen, wird hier ein negativer Wert angegeben.
 *         </TD>
 *     </TR>
 *     <TR>
 *         <TD>"ff" TT MM Bezeichnung</TD>
 *         <TD>
 *             Angabe eines festen Feiertags, der in jedem Jahr am selben Datum zelebriert wird.
 *         </TD>
 *     </TR>
 * </TABLE>
 * <BR>&nbsp;
 *
 * <P>Anbei ein Beispiel:
 * <PRE>
 * # Ein Kommentar
 *
 * clear
 *
 * os 06.02.1998   Bezugstag 98
 * os 07.04.2000   Bezugstag 2000
 * vf 10           10 Tage nach dem Bezugstag
 *
 * ff 15 03        Todestag Caesar 
 * </PRE>
 *
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 19.05.2009 - &Uuml;berarbeitung der Dokumentation.
 *     <P>
 *
 * @deprecated
 *     OLI 19.05.2009 - Bitte Nutzen Sie die Klasse <TT>Holiday</TT> anstattdessen.
 *     <P>
 *
 * @see Holiday
 *     <P>
 *
 */

public class Feiertag {

    /* Statische Standardinstanz. */
    private static Feiertag Instanz = new Feiertag();
    /* Liste mit den Regeln zur Bildung der Feiertagstabelle <I>(Default new Vector())</I>. */
    private Vector regeln = new Vector();
    /* Tabelle mit den Feiertagen eines Jahres <I>(Default new Hashtable())</I>. */
    private Hashtable feiertage = new Hashtable();

    /** Standardkonstruktor mit den Einstellungen f&uuml;r Berlin. */
    public Feiertag() {
        super();
        this.regeln.addElement(new FesterFeiertag(01, 01, "Neujahr"));
        this.regeln.addElement(new FesterFeiertag(01, 05, "1.Maifeiertag"));
        this.regeln.addElement(new FesterFeiertag(03, 10, "Tag der Deutschen Einheit"));
        this.regeln.addElement(new FesterFeiertag(25, 12, "1.Weihnachtsfeiertag"));
        this.regeln.addElement(new FesterFeiertag(26, 12, "2.Weihnachtsfeiertag"));
        this.regeln.addElement(new Ostersonntag("23.04.2000", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("15.04.2001", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("31.03.2002", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("20.04.2003", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("11.04.2004", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("27.03.2005", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("16.04.2006", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("08.04.2007", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("23.03.2008", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("12.04.2009", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("04.04.2010", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("24.04.2011", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("08.04.2012", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("31.03.2013", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("20.04.2014", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("05.04.2015", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("27.03.2016", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("16.04.2017", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("01.04.2018", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("21.04.2019", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("12.04.2020", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("04.04.2021", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("17.04.2022", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("09.04.2023", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("31.03.2024", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("20.04.2025", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("05.04.2026", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("28.03.2027", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("16.04.2028", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("01.04.2029", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("21.04.2030", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("13.04.2031", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("28.03.2032", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("17.04.2033", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("09.04.2034", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("25.03.2035", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("13.04.2036", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("05.04.2037", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("25.04.2038", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("10.04.2039", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("01.04.2040", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("21.04.2041", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("06.04.2042", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("29.03.2043", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("17.04.2044", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("09.04.2045", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("25.03.2046", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("14.04.2047", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("05.04.2048", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("18.04.2049", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("10.04.2050", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("02.04.2051", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("21.04.2052", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("06.04.2053", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("29.03.2054", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("18.04.2055", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("02.04.2056", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("22.04.2057", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("14.04.2058", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("30.03.2059", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("18.04.2060", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("10.04.2061", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("26.03.2062", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("15.04.2063", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("06.04.2064", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("29.03.2065", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("11.04.2066", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("03.04.2067", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("22.04.2068", "Ostersonntag"));
        // Der Termin f&uuml;r das Jahr 2069 ist fraglich. Grotefend gibt f&uuml;r dieses Jahr 
        // nichts an.
        this.regeln.addElement(new Ostersonntag("14.04.2069", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("30.03.2070", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("19.04.2071", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("10.04.2072", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("26.03.2073", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("15.04.2074", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("07.04.2075", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("19.04.2076", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("11.04.2077", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("03.04.2078", "Ostersonntag"));
        this.regeln.addElement(new Ostersonntag("23.04.2079", "Ostersonntag"));
        this.regeln.addElement(new VariablerFeiertag(-2, "Karfreitag"));
        this.regeln.addElement(new VariablerFeiertag(1, "Ostermontag"));
        this.regeln.addElement(new VariablerFeiertag(39, "Christi Himmelfahrt"));
        this.regeln.addElement(new VariablerFeiertag(49, "Pfingstsonntag"));
        this.regeln.addElement(new VariablerFeiertag(50, "Pfingstmontag"));
    }

    /**
     * Konstruktor, der seine Daten aus der Angegebenen Konfigurationsdatei ermittelt.
     *
     * @param dateiname Der Name der Datei, aus der die Konfigurationsdaten ermittelt werden
     *     sollen.
     */
    public Feiertag(String dateiname) {
        this();
        if ((dateiname != null) && (new File(dateiname).exists())) {
            try {
                FileReader fr = new FileReader(dateiname);
                BufferedReader reader = new BufferedReader(fr);
                int zeilennum = 0;
                String s = null;
                String zeile = new String();
                try {
                    zeile = reader.readLine();
                    while (zeile != null) {
                        StringTokenizer st = new StringTokenizer(zeile, "\n\r\t ");
                        while (st.hasMoreTokens()) {
                            s = st.nextToken().toLowerCase();
                            if (s.equals("#")) {
                                break;
                            } else if (s.equals("clear")) {
                                this.regeln = new Vector();
                                break;
                            } else if (s.equals("os")) {
                                s = st.nextToken();
                                PDate pd = null;
                                try {
                                    pd = PDate.valueOf(s);
                                } catch (DateFormatException dfe) {
                                    throw new IllegalArgumentException("date-string " 
                                            + s + " cannot be converted to corent.dates.PDate!",
                                            dfe);
                                }
                                s = st.nextToken("\n\r").trim();
                                this.regeln.addElement(new Ostersonntag(pd.toInt(), s));
                                break;
                            } else if (s.equals("ff")) {
                                s = st.nextToken();
                                int tag = -1;
                                int monat = -1;
                                try {
                                    tag = Integer.parseInt(s);
                                    monat = Integer.parseInt(st.nextToken());
                                } catch (NumberFormatException nfe) {
                                    nfe.printStackTrace();
                                }
                                if ((tag > 0) && (monat > 0)) {
                                    s = st.nextToken("\n\r").trim();
                                    this.regeln.addElement(new FesterFeiertag(tag, monat, s));
                                }
                                break;
                            } else if (s.equals("vf")) {
                                s = st.nextToken();
                                int diff = 0;
                                try {
                                    diff = Integer.parseInt(s);
                                } catch (NumberFormatException nfe) {
                                    nfe.printStackTrace();
                                }
                                if (diff != 0) {
                                    s = st.nextToken("\n\r").trim();
                                    this.regeln.addElement(new VariablerFeiertag(diff, s));
                                }
                                break;
                            }
                        }
                        zeilennum++;
                        zeile = reader.readLine();
                    }
                    reader.close();
                    fr.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }

    /**
     * Pr&uuml;ft, ob das angegebene Datum ein Feiertag ist.
     *
     * @param datum Das zupr&uuml;fende Datum.
     * @return null, wenn es sich nicht um einen Feiertag handelt,
     *     die Bezeichnung des Feiertages sonst.
     */
    public String isFeiertag(PDate datum) {
        int jahr = (int) datum.getJahr();
        int monatUndTag = (int) (datum.getMonat() * 100 + datum.getTag());
        Hashtable ft = (Hashtable) this.feiertage.get(new Integer(jahr));
        String bezeichnung = null;
        if (ft == null) {
            ft = this.feiertageErzeugen(jahr);
            this.feiertage.put(new Integer(jahr), ft);
        }
        bezeichnung = (String) ft.get(new Integer(monatUndTag));
        return bezeichnung;
    }

    /*
     * Erzeugt f&uuml;r das angegebene Jahr eine Feiertagsliste aus den Regeln des Objekts.
     *
     * @param jahr Das Jahr, f&uuml;r das die Feiertage erzeugt werden sollen.
     */
    private Hashtable feiertageErzeugen(int jahr) {
        FesterFeiertag ff = null;
        Hashtable ft = new Hashtable();
        int monatUndTag = 0;
        Object regel = null;
        Ostersonntag os = null;
        VariablerFeiertag vf = null;
        for (int i = 0; i < this.regeln.size(); i++) {
            regel = this.regeln.elementAt(i);
            if ((regel instanceof Ostersonntag) && (((Ostersonntag) regel).datum != null)
                    && ((int) ((Ostersonntag) regel).datum.getJahr() == jahr)) {
                os = (Ostersonntag) regel;
                monatUndTag = os.datum.toInt() % 10000;
                ft .put(new Integer(monatUndTag), os.bezeichnung);
            }
        }
        for (int i = 0; i < this.regeln.size(); i++) {
            regel = this.regeln.elementAt(i);
            if (regel instanceof FesterFeiertag) {
                ff = (FesterFeiertag) regel;
                monatUndTag = ff.monat * 100 + ff.tag;
                ft.put(new Integer(monatUndTag), ff.bezeichnung);
            } else if ((regel instanceof VariablerFeiertag) && (os != null)) {
                vf = (VariablerFeiertag) regel;
                PDate hd = new PDate(os.datum);
                if (vf.differenzZuOstersonntag > 0) {
                    hd = hd.naechsterTag(vf.differenzZuOstersonntag);
                } else if (vf.differenzZuOstersonntag < 0) {
                    hd = hd.vorherigerTag(0 - vf.differenzZuOstersonntag);
                }
                monatUndTag = hd.toInt() % 10000;
                ft.put(new Integer(monatUndTag), vf.bezeichnung);
            }
        }
        return ft;
    }

    /**
     * Pr&uuml;ft, ob das angegebene Datum ein Feiertag ist.
     *
     * @param datum Das zupr&uuml;fende Datum.
     * @return null, wenn es sich nicht um einen Feiertag handelt,
     *     die Bezeichnung des Feiertages sonst.
     */
    public static String IsFeiertag(PDate datum) {
        return Instanz.isFeiertag(datum);
    }

    /**
     * Ersetzt die statische Instanz durch eine neue, die mit Hilfe der angegebenen Datei
     * konfiguriert werden kann. Soll nur eine Standardinstanz erzeugt werden, so mu&szlig; der
     * Dateiname als <TT>null</TT> &uuml;bergeben werden.
     *
     * @param dateiname Der Name einer Konfigurationsdatei.
     */
    public static void Aktualisieren(String dateiname) {
        Instanz = new Feiertag(dateiname);
    }

}


/** Klasse zur Darstellung der Ostersonntagsregel. */

class Ostersonntag {

    /* Das genaue Datum des Ostersonntags <I>(Default null)</I>. */
    PDate datum = null;
    /* Name des Feiertages <I>(Default "")</I>. */
    String bezeichnung = "";

    /**
     * Konstruktor, der ein Ostersonntagsregelobjekt mit dem angegebenen Datum generiert.
     *
     * @param ostersonntag Das Datum des Ostersonntages in einem String.
     * @param bezeichnung Die Bezeichnung des Ostersonntages (eventuell in fremder Sprache).
     */
    public Ostersonntag(String ostersonntag, String bezeichnung) {
        super();
        try {
            this.datum = PDate.valueOf(ostersonntag);
        } catch (DateFormatException dfe) {
            throw new IllegalArgumentException("date-string " + ostersonntag + " cannot be "
                    + "converted to corent.dates.PDate!", dfe);
        }
        this.bezeichnung = bezeichnung;
    }

    /**
     * Konstruktor, der ein Ostersonntagsregelobjekt mit dem angegebenen Datum generiert.
     *
     * @param ostersonntag Das Datum des Ostersonntages.
     * @param bezeichnung Die Bezeichnung des Ostersonntages (eventuell in fremder Sprache).
     */
    public Ostersonntag(int ostersonntag, String bezeichnung) {
        super();
        this.datum = new PDate(ostersonntag);
        this.bezeichnung = bezeichnung;
    }

}


/** Klasse zur Darstellung eines festen Feiertages. */

class FesterFeiertag {

    /* Tag des Feiertages <I>(Default 1)</I>. */
    int tag = 1;
    /* Monat des Feiertages <I>(Default 1)</I>. */
    int monat = 1;
    /* Name des Feiertages <I>(Default "")</I>. */
    String bezeichnung = "";

    /**
     * Konstruktor, der mit Hilfe der &uuml;bergebenen Daten ein neues Regelobjekt bildet.
     *
     * @param tag Der Tag des Feiertages.
     * @param monat Der Monat des Feiertages.
     * @param bezeichnung Eine Bezeichnung zum Feiertag.
     */
    public FesterFeiertag(int tag, int monat, String bezeichnung) {
        super();
        this.tag = tag;
        this.monat = monat;
        this.bezeichnung = bezeichnung;
    }

}


/** Klasse zur Darstellung eines variablen Feiertages. */

class VariablerFeiertag {

    /* Anzahl der Tage vor (-) bzw. nach (+)", "Ostersonntag")); <I>(Default 0)</I>. */
    int differenzZuOstersonntag = 0;
    /* Name des Feiertages <I>(Default "")</I>. */
    String bezeichnung = "";

    /**
     * Konstruktor f&uuml;r einen variablen Feiertag.
     *
     * @param diff Die Anzahl der Tage in Differenz zum", "Ostersonntag"));.
     * @param bezeichnung Eine Bezeichnung zum Feiertag.
     */
    public VariablerFeiertag(int diff, String bezeichnung) {
        super();
        this.differenzZuOstersonntag = diff;
        this.bezeichnung = bezeichnung;
    }

}