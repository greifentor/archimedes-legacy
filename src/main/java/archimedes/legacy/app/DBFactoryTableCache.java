/*
 * DBFactoryTableCache.java
 *
 * 12.10.2006
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.db.*;

import java.util.*;


/**
 * Mit Hilfe dieser Klasse k&ouml;nnen Datenbankentabellen auf einfache Weise gecachet werden.
 * Da die zum angegebenen Typ eingelesenen Daten in ihrer G&auml;nze aus der Datenbank gelesen
 * werden, ist sollte sich die Nutzung auf kleine Tabellen bzw. Datenbest&auml;nde 
 * beschr&auml;nken.
 * <P>Bei der Nutzung des this.caches mu&szlig; im Hinterkopf behalten werden, da&szlig; er sich
 * nicht mit dem DBMS abgleicht, wenn Datens&auml;tze in Tabelle oder this.cache ge&auml;ndert 
 * werden.
 *
 * <P><H3>Properties:</H3>
 * <TABLE BORDER=1>
 *     <TR VALIGN=TOP>
 *         <TH>Property</TH>
 *         <TH>Default</TH>
 *         <TH>Typ</TH>
 *         <TH>Beschreibung</TH>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>archimedes.app.DBFactoryTableCache.<BR>suppress.idLtZero</TD>
 *         <TD>false</TD>
 *         <TD>boolean</TD>
 *         <TD>
 *             &Uuml;ber diese Property kann der Lesevorgang aus dem Cache bei einstelligen
 *             (also nur aus einem Datenfeld bestehenden), numerischen Schl&uuml;sseln im Falle
 *             eines Id-Wertes kleiner als null unterbunden werden. Hierdurch werden 
 *             &uuml;berfl&uuml;ssige Cache-Refreshes vermieden.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>archimedes.app.DBFactoryTableCache.debug</TD>
 *         <TD>false</TD>
 *         <TD>boolean</TD>
 *         <TD>Schaltet eine zus&auml;tzliche Debuggingausgabe zu.</TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>archimedes.Archimedes.debug</TD>
 *         <TD>false</TD>
 *         <TD>boolean</TD>
 *         <TD>Schaltet eine zus&auml;tzliche Debuggingausgabe zu.</TD>
 *     </TR>
 * </TABLE>
 * <P>&nbsp;
 *
 * @author
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 25.10.2007 - Hinzuf&uuml;gen der Methoden <TT>update(Object, T)</TT> und 
 *             <TT>put(Object, T)</TT>.
 *     <P>OLI 27.11.2007 - Einbau des Modus zum einzelnen Einf&uuml;gen von Cache-Elementen.
 *     <P>OLI 17.01.2008 - Erweiterung um die Methode <TT>dump</TT> mit deren Hilfe der 
 *             Cache-Inhalt auf die Konsole ausgegeben werden kann. Bei der Gelegenheit sind die
 *             Methoden der Klasse synchronisiert worden.
 *     <P>OLI 11.04.2008 - Erweiterung um die Debuggingausgaben in der 
 *             <TT>get(Object)</TT>-Methode.
 *     <P>OLI 21.04.2008 - Erweiterung um die Methode <TT>remove(Object)</TT>.
 *
 */
 
public class DBFactoryTableCache<T extends ApplicationObject> {

    /* Die ArchimedesDescriptorFactory zum Cache. */
    private ArchimedesDescriptorFactory adf = null;
    /* 
     * Diese Flagge muss gesetzt, wenn die als geloescht markierten Datensaetze nicht geladen
     * werden sollen.
     */
    private boolean excluderemoved = true;
    /* 
     * Diese Flagge wird gesetzt, wenn der Cache mit grossen Datenmengen arbeiten muss und sich
     * ein Nachladen der gesamten Tabelle nicht empfiehlt.
     */
    private boolean singlemode = false;
    // Der eigentliche Tabellencache.
    private Hashtable<Object, T> cache = new Hashtable<Object, T>();
    /* Der Name der Schl&uuml;sselspalte. */
    private String kcn = "";
    /* 
     * Eine Whereklausel f&uuml;r den Zugriff auf die DBFactory beim bef&uuml;llen des Caches. 
     */
    private String where = "";
    /* Ein Objekt als Blaupause f&uuml;r den Cache. */
    private T bp = null;
    
    /**
     * Generiert einen DBFactoryTableCache anhand der &uuml;bergebenen Parameter.
     *
     * @param adf Die ArchimedesDescriptorFactory, aus der die Daten f&uuml;r den Umgang mit den
     *     zu this.cachenden Objekten gewonnen werden.
     * @param bp Ein blueprint zum Bearbeiten der Objekte des angegebenen Typ im Cache. Der
     *     blueprint mu&szlig; ein (leeres) Objekt der mit dem Cache gespeicherten Klasse sein. 
     * @param kcn Der Name des Schl&uuml;sselattributes.
     */
    public DBFactoryTableCache(ArchimedesDescriptorFactory adf, T bp, String kcn) {
        this(adf, bp, kcn, "", false, false);
    }
    
    /**
     * Generiert einen DBFactoryTableCache anhand der &uuml;bergebenen Parameter.
     *
     * @param adf Die ArchimedesDescriptorFactory, aus der die Daten f&uuml;r den Umgang mit den
     *     zu this.cachenden Objekten gewonnen werden.
     * @param bp Ein blueprint zum Bearbeiten der Objekte des angegebenen Typ im Cache. Der
     *     blueprint mu&szlig; ein (leeres) Objekt der mit dem Cache gespeicherten Klasse sein. 
     * @param kcn Der Name des Schl&uuml;sselattributes.
     * @param where Eine Where-Klausel, die Auswahl der gecacheten Elemente beim Einlesen aus
     *     der Datenbank einschr&auml;nkt.
     */
    public DBFactoryTableCache(ArchimedesDescriptorFactory adf, T bp, String kcn, String where) 
            {
        this(adf, bp, kcn, where, false, false);
    }
    
    /**
     * Generiert einen DBFactoryTableCache anhand der &uuml;bergebenen Parameter.
     *
     * @param adf Die ArchimedesDescriptorFactory, aus der die Daten f&uuml;r den Umgang mit den
     *     zu this.cachenden Objekten gewonnen werden.
     * @param bp Ein blueprint zum Bearbeiten der Objekte des angegebenen Typ im Cache. Der
     *     blueprint mu&szlig; ein (leeres) Objekt der mit dem Cache gespeicherten Klasse sein. 
     * @param kcn Der Name des Schl&uuml;sselattributes.
     * @param where Eine Where-Klausel, die Auswahl der gecacheten Elemente beim Einlesen aus
     *     der Datenbank einschr&auml;nkt.
     * @param singlemode Diese Flagge mu&szlig; gesetzt werden, wenn der Cache im Falle eines
     *     erfolglosen Zugriffs nicht den gesamten Cache aktualisieren, sondern nur das fehlende
     *     Element nachladen soll.
     *
     * @changed
     *     OLI 27.11.2007 - Hinzugef&uuml;gt.
     *
     */
    public DBFactoryTableCache(ArchimedesDescriptorFactory adf, T bp, String kcn, String where,
            boolean singlemode) {
        this(adf, bp, kcn, where, singlemode, false);
    }
    
    /**
     * Generiert einen DBFactoryTableCache anhand der &uuml;bergebenen Parameter.
     *
     * @param adf Die ArchimedesDescriptorFactory, aus der die Daten f&uuml;r den Umgang mit den
     *     zu this.cachenden Objekten gewonnen werden.
     * @param bp Ein blueprint zum Bearbeiten der Objekte des angegebenen Typ im Cache. Der
     *     blueprint mu&szlig; ein (leeres) Objekt der mit dem Cache gespeicherten Klasse sein. 
     * @param kcn Der Name des Schl&uuml;sselattributes.
     * @param where Eine Where-Klausel, die Auswahl der gecacheten Elemente beim Einlesen aus
     *     der Datenbank einschr&auml;nkt.
     * @param singlemode Diese Flagge mu&szlig; gesetzt werden, wenn der Cache im Falle eines
     *     erfolglosen Zugriffs nicht den gesamten Cache aktualisieren, sondern nur das fehlende
     *     Element nachladen soll.
     * @param excluderemoved Diese Flagge mu&szlig; gesetzt werden, wenn der Cache ohne die als
     *     gel&ouml;scht gekennzeichneten Datens&auml;ze geladen werden soll.
     *
     * @changed
     *     OLI 08.02.2008 - Hinzugef&uuml;gt.
     *
     */
    public DBFactoryTableCache(ArchimedesDescriptorFactory adf, T bp, String kcn, String where,
            boolean singlemode, boolean excluderemoved) {
         super();
         this.adf = adf;
         this.bp = bp;
         bp.setADF(adf);
         this.excluderemoved = excluderemoved;
         this.kcn = kcn;
         this.singlemode = singlemode;
         this.where = (where != null ? where : "");
    }
    
    /** Leert den Cache. */
    public synchronized void clear() {
        this.cache.clear();
    }
    
    /**
     * Gibt den Cache-Inhalt auf die Konsole aus.
     *
     * @changed
     *     OLI 17.01.2008 - Hinzugef&uuml;gt.
     *
     */
    public synchronized void dump() {
        Object c = null;
        Object k = null;
        for (Enumeration e = this.getKeys(); e.hasMoreElements(); ) {
            k = e.nextElement();
            c = this.get(k);
            System.out.println(k + " - " + c);
        }
    }
    
    /**
     * Liefert ein Objekt von Typ T zum angegebenen Schl&uuml;ssel k. 
     *
     * @param k Der Schl&uuml;ssel, zu dem das Objekt aus dem this.cache gelesen werden soll.
     *
     * @changed
     *     OLI 27.11.2007 - Erweiterung um die Behandlung des 'SingleModes'.
     *     <P>OLI 11.04.2008 - Erweiterung um Debuggingausgabe. Mit Hilfe dieser Debugausgabe
     *             werden die Elemente angezeigt, die in den Cache eingelagert werden sollen. 
     *             Dieser Zustand kann &uuml;ber das Setzen der Properties 
     *             <I>archimedes.Archimedes.debug</I> oder 
     *             <I>archimedes.app.DBFactoryTableCache</I> erreicht werden.
     *
     */
    public synchronized T get(Object k) {
        synchronized (this.cache) {
            boolean debug = Boolean.getBoolean("archimedes.Archimedes.debug") 
                    || Boolean.getBoolean("archimedes.app.DBFactoryTableCache.debug");
            if (Boolean.getBoolean("archimedes.app.DBFactoryTableCache.suppress.idLtZero") 
                    && (k instanceof Number) && (((Number) k).longValue() < 1)) {
                return null;
            }
            T t = this.cache.get(k);
            if (t == null) {
                try {
                    String fkcn = kcn;
                    String s = null;
                    String w = this.where;
                    if (this.singlemode) {
                        if (w.length() > 0) {
                            w = w.concat(" and");
                        }
                        s = (k instanceof Number ? k.toString() : DBUtil.DBString(k.toString())
                                );
                        fkcn = kcn;
                        if (debug) {
                            System.out.println("\n\n>>>>" + bp.getTablename());
                        }
                        if ((bp.getTablename() != null) && (bp.getTablename().length() > 0)) {
                            fkcn = bp.getTablename().concat(".").concat(kcn);
                        }
                        w = w.concat(" (").concat(fkcn).concat("=").concat(s).concat(")");
                    }
                    if (debug) {
                        System.out.println("adf - " + this.adf); 
                        System.out.println("adf.getApplication() - " + this.adf.getApplication()
                                ); 
                        System.out.println("adf.getApplication().getDFC() - " 
                                + this.adf.getApplication().getDFC()); 
                        System.out.println("bp.getClass() - " + bp.getClass()); 
                    }
                    Vector<T> vt = this.adf.getApplication().getDFC().read(bp.getClass(), w, 
                            null, false, !this.excluderemoved);
                    if (vt.size() > 0) {
                        if (!this.singlemode) {
                            this.cache.clear();
                        }
                        if (debug) {
                            System.out.println("\n\nDBFactoryTableCache red ...");
                        }                            
                        for (int i = 0, len = vt.size(); i < len; i++) {
                            T t0 = vt.elementAt(i);
                            if (debug) {
                                System.out.println("    " + i + " - " + t0);
                            }
                            t0.setADF(this.adf);
                            t0.setHAD(this.adf.getDynamicDescriptor(this.bp.getTablename()));
                            Object k0 = t0.get(this.kcn);
                            this.cache.put(k0, t0);
                        }
                    }
                    t = this.cache.get(k);
                } catch (Exception e) {
                    e.printStackTrace();
                    System.out.println("WARNUNG: Problem beim Einlesen des this.caches fuer den"
                            + " Typ " + this.bp.getClass().getName() + "!");
                }
            }
            return t;
        }       
    }
    
    /** 
     * Liefert eine Enumeration der in dem Cache hinterlegten Sch&uuml;ssel-Objekte.
     * 
     * @return Eine ungetypte Enumeration mit Referenzen auf die im Cache hinterlegten 
     *     Sch&uuml;ssel-Objekten.
     */
    public synchronized Enumeration getKeys() {
        return this.cache.keys();
    }
    
    /**
     * F&uuml;gt das Objekt unter dem angegebenen Schl&uuml;ssel in den Cache ein.
     *
     * @param k Der Schl&uuml;ssel, unter dem das Objekt in den Cache eingef&uuml;gt werden 
     *     soll.
     * @param o Das Objekt, das in den Cache eingf&uuml;gt werden soll.
     *
     * @changed
     *     OLI 25.10.2007 - Hinzugef&uuml;gt.
     *
     */
    public synchronized void put(Object k, T o) {
        this.cache.put(k, o);
    }
    
    /**
     * Entfernt das Objekt mit dem angegebenen Schl&uuml;ssel aus dem Cache.
     *
     * @param k Der Schl&uuml;ssel zu dem Objekt, das aus dem Cache entfernt werden soll.
     * @return Das aus dem Cache entfernte Objekt, bzw. <TT>null</TT>, wenn kein Objekt zu dem 
     *     angegebenen Schl&uuml;ssel gefunden und entfernt werden konnte, oder wenn als 
     *     Schl&uuml;ssel eine null-Referenz angegeben worden ist.
     *
     * @changed
     *     OLI 21.04. 2008 - Hinzugef&uuml;gt.
     *
     */
    public synchronized T remove(Object k) {
        if (k != null) {
            return this.cache.remove(k);
        }
        return null;
    }
    
    /**
     * Aktualisiert das Objekt mit dem angegebenen Schl&uuml;ssel, sofern es bereits in dem 
     * Cache enthalten und vom selben Typ ist.
     *
     * @param k Der Schl&uuml;ssel, zu dem das Objekt aktualisiert werden soll.
     * @param o Das Objekt, das aktualisiert werden soll.
     * @return <TT>true</TT>, falls der Aktualisierungvorgang gegl&uuml;ckt ist, <TT>false</TT>
     *     sonst.
     *
     * @changed
     *     OLI 25.10.2007 - Hinzugef&uuml;gt.
     *
     */
    public synchronized boolean update(Object k, T o) {
        if (o != null) { 
            Object o0 = this.cache.get(k);
            if (o0 != null) {
                this.cache.put(k, o);
                return true;
            }
        }
        return false;
    }
    
}
