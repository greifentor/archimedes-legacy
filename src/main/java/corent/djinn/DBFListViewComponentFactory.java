/*
 * DBFListViewComponentFactory.java
 *
 * 25.02.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;
import corent.db.xs.*;
import corent.files.*;

import java.lang.reflect.*;
import java.util.*;

import javax.swing.*;


/**
 * Eine Musterimplementierung der ViewComponentFactory auf Basis einer JList, die ihre Daten aus
 * einer DBFactory bezieht.
 * <P>Wird die Property <I>corent.djinn.DBFListViewComponentFactory.tablemode</I> gesetzt, wird
 * eine Tabelle als Ansichtskomponente genutzt. Anderfalls wird die Ansicht &uuml;ber eine JList
 * geregelt.
 * <P>Die Ausgaben am unteren Rand der Komponente k&ouml;nnen &uuml;ber mehrere Properties 
 * gesetzt werden. Defaulttexte in deutscher Sprache sind in der Implementierung enthalten.
 * Die Properties sind <I>corent.djinn.DBFListViewComponentFactory.text.FormatRecordCountInLimit
 * </I>, <I>corent.djinn.DBFListViewComponentFactory.text.FormatRecordsNotFound</I> und
 * <I>corent.djinn.DBFListViewComponentFactory.text.FormatRecordCountOffLimit</I> f&uuml;r die
 * drei m&ouml;glichen F&auml;lle: eine Anzahl von Datens&auml;tzen gefunden, keine 
 * Datens&auml;tze gefunden und zuviele Datens&auml;tze gefunden. F&uuml;r diese Properties 
 * werden Formatstring erwartet. 
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
 *         <TD>
 *             corent.djinn.DBFListViewComponentFactory.selection<BR>.component.for.[classname]" 
 *         </TD>
 *         <TD>null</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Hier kann zu einer Klasse der Name einer SelectionComponent-Klasse hinterlegt 
 *             werden, die in den Auswahl-Dialog eingebunden werden soll.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.ViewComponentFactory.records.none.found</TD>
 *         <TD>Es wurden keine Datens&auml;tze gefunden!</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Ein Text zur Anzeige, wenn keine Datens&auml;tze gefunden werden konnten.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.ViewComponentFactory.records.in.limit</TD>
 *         <TD>Es wurden %d Datens&auml;tze gefunden</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Ein Text zur Anzeige, wenn eine Anzahl von Datens&auml;tzen gefunden wurde, die 
 *             sich noch innerhalb eines vorgegebenen Limits bewegt (von der Anzahl her). Die 
 *             Konfiguration solcher Limits erfolgt &uuml;ber weiter oben beschriebene 
 *             Properties.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.ViewComponentFactory.records.off.limit</TD>
 *         <TD>Das Limit von %d Datens&auml;tzen wurde &uuml;berschritten!</TD>
 *         <TD>String</TD>
 *         <TD>
 *             Ein Text zur Anzeige, wenn eine Anzahl von Datens&auml;tzen gefunden wurde, die 
 *             sich au&szlig;erhalb eines vorgegebenen Limits bewegt (von der Anzahl her). Die 
 *             Konfiguration solcher Limits erfolgt &uuml;ber weiter oben beschriebene 
 *             Properties.
 *         </TD>
 *     </TR>
 * </TABLE>
 * <P>&nbsp;
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 05.06.2008 - Umstellung der Konfiguration der Limit-String auf Properties.
 *     <P>
 *
 */
 
public class DBFListViewComponentFactory implements ViewComponentFactory {
    
    /** Referenz auf die Klasse, zu der eine DBFactory angezapft werden soll. */
    protected Class cls = null;    
    /** Referenz auf die DBFactoryController, aus der die Komponente ihre Daten beziehen soll. */
    protected DBFactoryController dfc = null;
    /** Die Inidatei, aus der die Konfiguration der Tabellensicht rekonstruiert werden soll. */
    protected Inifile ini = null;
    /** 
     * Referenz auf einen Frame, mit dem die durch die Factory produzierten Komponenten in
     * Zusammenhang stehen (optional). 
     */ 
    protected JFrame f = null;
    /** Eine Where-Klausel zu Vorauswahl von Elementen. */
    protected String preselection = null;
    /** Referenz auf den Vector, dessen Inhalt die generierte ViewComponent anzeigen soll. */
    protected Vector view = null;
    
    /**
     * Generiert eine Factory anhand der &uuml;bergebenen Parameter.
     *
     * @param dfc Die DBFactoryController, deren Daten bearbeitet werden sollen.
     * @param cls Die Klasse zu der die DBFactory &uuml;ber den Controller angesprochen werden 
     *     soll.
     * @param ini Die Inidatei, aus der die Tabellenansicht ihre Konfiguration restaurieren 
     *     soll.
     */
    public DBFListViewComponentFactory(DBFactoryController dfc, Class cls, Inifile ini) {
        this(dfc, cls, ini, null, null);
    }
    
    /**
     * Generiert eine Factory anhand der &uuml;bergebenen Parameter.
     *
     * @param dfc Die DBFactoryController, deren Daten bearbeitet werden sollen.
     * @param cls Die Klasse zu der die DBFactory &uuml;ber den Controller angesprochen werden 
     *     soll.
     * @param ini Die Inidatei, aus der die Tabellenansicht ihre Konfiguration restaurieren 
     *     soll.
     * @param preselection Eine Where-Klausel zur Vorauswahl von Elementen.
     *
     * @changed
     *     OLI 28.01.2009 - Herabstufung in einen untergeordneten Konstruktor, der den 
     *             Hauptkonstruktor aufruft.
     *     <P>
     *
     */
    public DBFListViewComponentFactory(DBFactoryController dfc, Class cls, Inifile ini, 
            String preselection) {
        this(dfc, cls, ini, preselection, null);
    }
    
    /**
     * Generiert eine Factory anhand der &uuml;bergebenen Parameter.
     *
     * @param dfc Die DBFactoryController, deren Daten bearbeitet werden sollen.
     * @param cls Die Klasse zu der die DBFactory &uuml;ber den Controller angesprochen werden 
     *     soll.
     * @param ini Die Inidatei, aus der die Tabellenansicht ihre Konfiguration restaurieren 
     *     soll.
     * @param f Referenz auf einen Frame, mit dem die durch die Factory produzierte Komponente
     *     in Verbindung steht. Dies wird im Regelfall das Hauptfenster der Applikation sein.
     *
     * @changed
     *     OLI 28.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public DBFListViewComponentFactory(DBFactoryController dfc, Class cls, Inifile ini, JFrame f
            ) {
        this(dfc, cls, ini, null, f);
    }
    
    /**
     * Generiert eine Factory anhand der &uuml;bergebenen Parameter.
     *
     * @param dfc Die DBFactoryController, deren Daten bearbeitet werden sollen.
     * @param cls Die Klasse zu der die DBFactory &uuml;ber den Controller angesprochen werden 
     *     soll.
     * @param ini Die Inidatei, aus der die Tabellenansicht ihre Konfiguration restaurieren 
     *     soll.
     * @param preselection Eine Where-Klausel zur Vorauswahl von Elementen.
     * @param f Referenz auf einen Frame, mit dem die durch die Factory produzierte Komponente
     *     in Verbindung steht. Dies wird im Regelfall das Hauptfenster der Applikation sein.
     *
     * @changed
     *     OLI 28.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public DBFListViewComponentFactory(DBFactoryController dfc, Class cls, Inifile ini, 
            String preselection, JFrame f) {
        super();
        this.cls = cls;
        this.dfc = dfc;
        this.f = f;
        this.ini = ini;
        this.preselection = preselection;
    }
    
    /**
     * Liefert eine Referenz auf den DBFactoryController, &uuml;ber den die Factory mit der 
     * Datenbank kommuniziert.
     *
     * @return Der DBFactoryController, &uuml;ber den die Factory mit dem DBMS kommuniziert.
     */
    public DBFactoryController getDFC() {
        return this.dfc;
    }
    
    
    /* Implementierung des Interfaces ViewComponentFactory. */
    
    /**
     * @changed
     *     OLI 03.10.2008 - Hinzugef&uuml;gt.
     *     <P>OLI 28.01.2009 - Die Methode reagiert nun auf die Property 
     *             "corent.djinn.DBFListViewComponentFactory.selection.component.for.[clsname]".
     *             &Uuml;ber diese Property kann eine SelectionComponent f&uuml;r die 
     *             selektierte Klasse erzeugt werden.
     *     <P>
     *
     */
    public SelectionComponent createSelectionComponent() {
        Class cls = null;
        Constructor c = null;
        SelectionComponent sc = null;
        String classname = System.getProperty(
                "corent.djinn.DBFListViewComponentFactory.selection.component.for." 
                + this.cls.getName());
        if (classname != null) {
            try {
                cls = Class.forName(classname);
                c = cls.getConstructor(JFrame.class, DBFactoryController.class);
                sc = (SelectionComponent) c.newInstance(this.f, this.dfc);
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("\nERROR: selection component has not been created for class"
                        + " " + this.cls.getName() + "!");
            }
        }
        return sc;
    }
    
    public ViewComponent createViewComponent() {
        if (Boolean.getBoolean("corent.djinn.DBFListViewComponentFactory.tablemode")) {
            return new DBFTableViewComponent(this.dfc, this.cls, this.ini, this.preselection);
        }
        return new DBFListViewComponent(this.dfc, this.cls, this.preselection);
    }
    
    public Class getServedClass() {
        return this.cls;
    }

    /**
     * @changed
     *     OLI 05.06.2008 - Konfiguration des Ausgabetextes auf Property umgestellt.
     *     <P>
     *
     */
    public String getFormatNoRecordsFound() {
        return Utl.GetProperty("corent.djinn.ViewComponentFactory.records.none.found", 
                "Es wurden keine Datens&auml;tze gefunden!");
    }
     
    /**
     * @changed
     *     OLI 05.06.2008 - Konfiguration des Ausgabetextes auf Property umgestellt.
     *     <P>
     *
     */
    public String getFormatRecordCountInLimit() {
        return Utl.GetProperty("corent.djinn.ViewComponentFactory.records.in.limit", 
                "Es wurden %d Datens&auml;tze gefunden");
    }
     
    /**
     * @changed
     *     OLI 05.06.2008 - Konfiguration des Ausgabetextes auf Property umgestellt.
     *     <P>
     *
     */
    public String getFormatRecordCountOffLimit() {
        return Utl.GetProperty("corent.djinn.ViewComponentFactory.records.off.limit",
                "Das Limit von %d Datens&auml;tzen wurde &uuml;berschritten!");
    }
}
