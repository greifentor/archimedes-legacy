/*
 * DBFTableViewComponent.java
 *
 * 27.08.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.db.*;
import corent.db.xs.*;
import corent.ext.*;
import corent.files.*;
import corent.gui.*;

import java.rmi.*;
import java.util.*;

import javax.swing.*;
import javax.swing.table.*;


/**
 * Eine Musterimplementierung f&uuml;r eine auf einer JTable basierenden ViewComponent, die ihre
 * Daten aus einer DBFactory bezieht.
 *
 * @author 
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 25.04.2008 - &Auml;nderung in der Methode <TT>updateView(Object[])</TT> 
 *             bez&uuml;glich der Reihenfolge in der Zusammenstellung von Where-Klauseln zur 
 *             Beseitigung des Fehlers, der bei Preselections aufgetreten ist.
 *     <P>OLI 29.01.2009 - Einbau einer M&ouml;glichkeit den initialen Aufbau des 
 *             SelectionTableModels mit Daten aus der Datenbank. Hierdurch kann die Performanz
 *             f&uuml;r Suchdialoge, die auf Tabellen mit hohen Datensatzzahlen arbeiten, 
 *             deutlich gesteigert werden. 
 *     <P> 
 *
 */

public class DBFTableViewComponent implements ViewComponent {

    /** 
     * Diese Flagge ist gesetzt, solange die Anzeige noch nicht zum erstenmal aktualisiert
     * worden ist.
     */
    protected boolean firstTime = true;
    /** Referenz auf die Klasse, zu der eine DBFactory angezapft werden soll. */
    protected Class cls = null;    
    /** 
     * Referenz auf die DBFactoryController, aus der die Komponente ihre Daten beziehen soll. 
     */
    protected DBFactoryController dfc = null;
    /** Referenz auf das JTable-Objekt, das die eigentliche Anzeigekomponente beherbergt. */
    protected JTable anzeige = null;
    /** Eine Instanz der Klasse, auf die sich die Komponente bezieht. */
    protected Persistent instance = null;
    /** Eine Where-Klausel zu Vorauswahl von Elementen. */
    protected String preselection = null;
        
    /**
     * Generiert eine DBFTableViewComponent anhand der &uuml;bergebenen Parameter.
     *
     * @param dfc Die DBFactoryController, deren Daten bearbeitet werden sollen.
     * @param cls Die Klasse zu der die DBFactory &uuml;ber den Controller angesprochen werden 
     *         soll.
     * @param ini Die Inidatei, aus der die Konfiguration der Tabellenanzeige rekonstruiert
     *         werden soll.
     */
    public DBFTableViewComponent(DBFactoryController dfc, Class cls, Inifile ini) {
        this(dfc, cls, ini, null);
    }
    
    /**
     * Generiert eine DBFTableViewComponent anhand der &uuml;bergebenen Parameter.
     *
     * @param dfc Die DBFactoryController, deren Daten bearbeitet werden sollen.
     * @param cls Die Klasse zu der die DBFactory &uuml;ber den Controller angesprochen werden 
     *         soll.
     * @param ini Die Inidatei, aus der die Konfiguration der Tabellenanzeige rekonstruiert
     *         werden soll.
     * @param preselection Eine Where-Klausel zur Vorauswahl von Elementen.
     *
     * @changed
     *     OLI 29.01.2009 - Die Property 
     *             "corent.djinn.DBFTableViewComponent.suppress.initial.data.access.for.class.[classname]" 
     *             erm&ouml;glicht Unterdr&uuml;cken des initialen Datenzugriffs beim Erzeugen
     *             des Objekt (also im Konstruktor). Stattdessen wird eine leeres 
     *             <TT>SelectionTableModel</TT> zur&uuml;ckgegeben. 
     *     <P>
     */
    public DBFTableViewComponent(DBFactoryController dfc, Class cls, Inifile ini, 
            String preselection) {
        super();
        this.cls = cls;
        this.dfc = dfc;
        this.preselection = (preselection == null ? "" : preselection);
        try {
            this.instance = (Persistent) this.dfc.create(this.cls);
        } catch (RemoteException re) {
            re.printStackTrace();
        }
        try {
            TableSorter sorter = new TableSorter(this.dfc.getSelectionView(this.instance, 
                    this.preselection, Boolean.getBoolean(
                    "corent.djinn.DefaultSelectionDjinnPanel.suppress.first.update.for.class."
                    + this.cls.getName())));
            this.anzeige = new JTableWithInifile(sorter, ini, "table.view." + cls.getName());
            if (!(this.instance instanceof AlternateSelectionTableModelOwner)) {
                ((DefaultTableModel) ((TableSorter) this.anzeige.getModel()).getTableModel()
                        ).setColumnIdentifiers(this.instance.getPersistenceDescriptor(
                        ).getSelectionViewColumnnames());
            }
            sorter.setTableHeader(this.anzeige.getTableHeader());
            /* So l&auml;uft das nicht: Auswahl nicht mehr m&ouml;glich ...
            this.anzeige.addFocusListener(new FocusAdapter() {
               public void focusLost(FocusEvent e) {
                   anzeige.clearSelection();
               }
            });
            */
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    
    /* Implementierung des Interfaces ViewComponent. */
    
    public Vector getSelectedValues() {
        int[] rows = this.anzeige.getSelectedRows();
        Vector selected = new Vector();
        for (int i = 0; i < rows.length; i++) {
            try {
                int modelIndex = ((TableSorter) this.anzeige.getModel()).modelIndex(rows[i]);
                Object keys = ((SelectionTableModel) ((TableSorter) this.anzeige.getModel()
                        ).getTableModel()).getKey(modelIndex);
                StringBuffer where = new StringBuffer("");
                if (keys instanceof Vector) {
                    Vector vk = (Vector) keys;
                    String s = "";
                    for (int j = 0, lenj = vk.size(); j < lenj; j++) {
                        ColumnRecord cr = ((Persistent) this.instance).getPersistenceDescriptor(
                                ).getKeys().elementAt(j);
                        if (where.length() > 0) {
                            where.append(" and ");
                        }
                        Object k = vk.elementAt(j);
                        if (k instanceof String) {
                            s = k.toString();
                            where.append(cr.getFullname()).append(" like '").append(s).append(
                                    "'");
                        } else {
                            where.append(cr.getFullname()).append("=").append(k.toString());
                        }
                    }
                } else {
                    String key = ((Persistent) this.instance).getPersistenceDescriptor(
                            ).getKeys().elementAt(0).getFullname();                    
                    where.append(key).append("=").append((keys instanceof String ? "'" : "")
                            ).append(keys.toString()).append((keys instanceof String ? "'" : "")
                            );
                }
                Vector v = this.dfc.read(this.cls, where.toString());
                for (int j = 0, lenj = v.size(); j < lenj; j++) {
                    selected.addElement(v.elementAt(j));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return selected;
    }
    
    public int getSelectedValuesCount() {
        return this.anzeige.getSelectedRows().length;
    }
    
    public JComponent getView() {
        return new JScrollPane(this.anzeige);
    }
    
    public JComponent getViewComponent() {
        return this.anzeige;
    }

    /**
     * @changed
     *     OLI 25.04.2008 - &Auml;nderung der Reihenfolge in der Zusammenstellung der 
     *             Where-Klausel. Hiermit wird der Fehler, der bei Preselections beim Bau 
     *             des Selectstatements entsteht, ausgeb&uuml;gelt.
     *     <P>OLI 03.10.2008 - Erweiterung um die Logik des Additionals-Parameters.
     *     <P>
     *
     */
    public int updateView(Object[] criteria, String additions) throws IllegalArgumentException {
        if (((criteria != null) && (criteria.length > 0) && !(criteria[0] instanceof String)) 
                || ((criteria != null) && (criteria.length > 1))) {
            throw new IllegalArgumentException("ViewComponent does only except one "
                    + "String-criteria!");
        }
        SelectionTableModel stm = null;        
        try {
            String filter = this.dfc.createFilter(this.cls, criteria);
            if (this.preselection.length() > 0) {
                if (filter.length() > 0) {
                    filter = filter.concat(" and ");
                }
                filter = filter.concat("(").concat(this.preselection).concat(")"); 
            }
            if ((additions != null) && (additions.length() > 0)) {
                if (filter.length() > 0) {
                    filter = filter.concat(" and ");
                }
                filter = filter.concat("(").concat(additions).concat(")"); 
            }
            stm = this.dfc.getSelectionView(this.instance, filter, false);
            TableSorter sorter = new TableSorter(stm);
            this.anzeige.setModel(sorter);
            if (!(this.instance instanceof AlternateSelectionTableModelOwner)) {
                ((DefaultTableModel) ((TableSorter) this.anzeige.getModel()).getTableModel()
                        ).setColumnIdentifiers(this.instance.getPersistenceDescriptor(
                        ).getSelectionViewColumnnames());
            }
            sorter.setTableHeader(this.anzeige.getTableHeader());
           this.init();
        } catch (Exception e) {
            e.printStackTrace();
        }
        int count = this.anzeige.getModel().getRowCount();
        if (stm.isMoreThanLimit()) {
            count = 0 - count;
        }
        if ((count > 0) && !this.firstTime) {
            this.anzeige.getSelectionModel().setSelectionInterval(0, 0);
        }
        this.firstTime = false;
        return count;
    }
    
    public void close() {
        if (this.anzeige instanceof JTableWithInifile) {
            ((JTableWithInifile) this.anzeige).saveToIni();
        }
    }
    
    public void init() {
        if (this.anzeige instanceof JTableWithInifile) {
            ((JTableWithInifile) this.anzeige).restoreFromIni();
        }
    }

}
