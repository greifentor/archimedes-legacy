/*
 * DBListViewComponent.java
 *
 * 25.02.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.db.xs.*;

import java.awt.*;
import java.util.*;

import javax.swing.*;


/**
 * Eine Musterimplementierung f&uuml;r eine auf einer JList basierenden ViewComponent, die ihre
 * Daten aus einer DBFactory bezieht.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public class DBFListViewComponent implements ViewComponent {

    /* Referenz auf die Klasse, zu der eine DBFactory angezapft werden soll. */
    private Class cls = null;    
    /* Referenz auf die DBFactoryController, aus der die Komponente ihre Daten beziehen soll. */
    private DBFactoryController dfc = null;
    /* Referenz auf das JList-Objekt, das die eigentliche Anzeigekomponente beherbergt. */
    private JList anzeige = null;
    /* Eine Where-Klausel zu Vorauswahl von Elementen. */
    private String preselection = null;
    
    /**
     * Generiert eine DBFListViewComponent anhand der &uuml;bergebenen Parameter.
     *
     * @param dfc Die DBFactoryController, deren Daten bearbeitet werden sollen.
     * @param cls Die Klasse zu der die DBFactory &uuml;ber den Controller angesprochen werden 
     *     soll.
     */
    public DBFListViewComponent(DBFactoryController dfc, Class cls) {
        this(dfc, cls, null);
    }
    
    /**
     * Generiert eine DBFListViewComponent anhand der &uuml;bergebenen Parameter.
     *
     * @param dfc Die DBFactoryController, deren Daten bearbeitet werden sollen.
     * @param cls Die Klasse zu der die DBFactory &uuml;ber den Controller angesprochen werden 
     *     soll.
     * @param preselection Eine Where-Klausel zur Vorauswahl von Elementen. 
     */
    public DBFListViewComponent(DBFactoryController dfc, Class cls, String preselection) {
        super();
        this.cls = cls;
        this.dfc = dfc;
        this.preselection = (preselection == null ? "" : preselection);
        try {
            this.anzeige = new JList(new DefaultListViewListModel(dfc.read(cls, 
                    this.preselection)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.anzeige.setFont(new Font("monospaced", Font.PLAIN, 14));
    }
    
    
    /* Implementierung des Interfaces ViewComponent. */
    
    public Vector getSelectedValues() {
        Object[] values = this.anzeige.getSelectedValues();
        Vector selected = new Vector();
        for (int i = 0; i < values.length; i++) {
            if (values[i] instanceof ListViewSelectableContainer) {
                selected.addElement(((ListViewSelectableContainer) values[i]).getContent());
            } else {
                selected.addElement(values[i]);
            }
        }
        return selected;
    }
    
    public int getSelectedValuesCount() {
        return this.anzeige.getSelectedValues().length;
    }
    
    public JComponent getView() {
        return new JScrollPane(this.anzeige);
    }
    
    public JComponent getViewComponent() {
        return this.anzeige;
    }

    /**
     * @changed
     *     OLI 03.10.2008 - Erweiterung um die Logik des Additionals-Parameters.
     *     <P>
     *
     */
    public int updateView(Object[] criteria, String additions) throws IllegalArgumentException {
        if (((criteria != null) && (criteria.length > 0) && !(criteria[0] instanceof String)) 
                || ((criteria != null) && (criteria.length > 1))) {
            throw new IllegalArgumentException("ViewComponent does only except one "
                    + "String-criteria!");
        }
        try {
            String filter = this.dfc.createFilter(this.cls, criteria);
            if (this.preselection.length() > 0) {
                if (filter.length() > 0) {
                    filter = filter.concat(" and ");
                }
                filter = "(".concat(this.preselection).concat((filter.length() > 0 ? ") and " 
                        : "")).concat(filter); 
            }
            if ((additions != null) && (additions.length() > 0)) {
                if (filter.length() > 0) {
                    filter = filter.concat(" and ");
                }
                filter = "(".concat(additions).concat((filter.length() > 0 ? ") and " : "")
                        ).concat(filter); 
            }
            this.anzeige.setModel(new DefaultListViewListModel(this.dfc.read(this.cls, filter))
                    );
        } catch (Exception e) {
            e.printStackTrace();
        }
        int lim = Integer.getInteger("corent.djinn.ViewComponent.maximum", 0);
        if (Integer.getInteger("corent.djinn.ViewComponent.maximum." + this.cls.getName(), 0) 
                > 0) {
            lim = Integer.getInteger("corent.djinn.ViewComponent.maximum." + this.cls.getName(),
                    0);
        }
        int count = this.anzeige.getModel().getSize();
        if (count > lim) {
            count = 0 - count;
        }
        return count;
    }
    
    public void close() {
    }

    public void init() {
    }

}
