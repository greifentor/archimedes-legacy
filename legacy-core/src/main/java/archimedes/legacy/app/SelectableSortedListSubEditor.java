/*
 * SelectableSortedListSubEditor.java
 *
 * 10.06.2006
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;


import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import corent.base.*;
import corent.djinn.*;
import corent.files.*;
import corent.gui.*;

import archimedes.legacy.model.*;


/**
 * Ein in einen EditorDjinn einbindbarer SubEditor zur Bearbeitung von sortierten Listen. In 
 * dieser Variante werden alle Elemente der N-Seite der repr&auml;sentierten Relation zur 
 * Auswahl angeboten und k&ouml;nnen durch einfaches Anklicken selektiert werden.
 *
 * @author
 *     ollie
 *
 * @changed
 *     OLI 24.10.2007 - Erweiterung um die Implementierung des Interfaces 
 *             <TT>TableListSubEditor</TT>.<BR>
 *
 */

public class SelectableSortedListSubEditor implements TableListSubEditor {

    /* Referenz auf die ArchimedesApplication, innerhalb derer der SubEditor arbeiten soll. */
    private ArchimedesApplication app = null;
    /* 
     * Eine Referenz auf die durch die ArchimedesApplication genutzte Inidatei zur 
     * Fensterrekonstruktion, bzw. null, wenn es eine solche Datei nicht gibt.
     */
    private Inifile ini = null;
    /* Die Liste der JComponents, die durch den Djinn zugegriffen werden k&ouml;nnen. */
    private JComponent[] components = null;
    /* Liste der zu einer Component geh&ouml;renden Labels des SubEditors. */
    private JLabel[] labels = null;
    /* Das Panel, auf dem der SubEditor abgebildet wird. */
    private JPanel panel = null;
    /* Liste der JPanels, auf denen die zugreifbaren Components untergebracht werden sollen. */
    private JPanel[] panels = null;
    /* Die Tabellenansicht zur Auswahl der angezeigten Objekte. */
    private JTable anzeige = null;
    /* Das NReferenzModel, &uuml;ber das die Manipulation des Vectors definiert wird. */
    // private NReferenzModel nrm = null;
    /* Eine Instanz der in dem bearbeiteten SortedVector gespeicherten Objekte. */
    // private Object bp = null;
    /* Die Instanz, zu der die Liste geh&ouml;rt. */
    // private Object listowner = null;
    /* Der SortedVector, der in dem Panel manipuliert wird. */
    private SortedVector sv = null;
    /* Die Kopie des SortedVector, die in dem Panel manipuliert wird. */
    private SortedVector svcopy = null;

    /**
     * Generiert einen SelectableSortedListSubEditor mit den &uuml;bergebenen Parametern.
     *
     * @param app Die ArchimedesApplication, innerhalb derer der SubEditor arbeiten soll. 
     * @param sv Der sortierte Vector, dessen Inhalt manipuliert werden soll.
     * @param bp Eine Beispielinstanz der Klasse, die in dem Vector gespeichert sind oder werden
     *     sollen. Dies wird beispielsweise zum Einlesen neuer Objekte ben&ouml;tigt.
     * @param lo Die Instanz, zu der die Liste geh&ouml;rt.
     * @param nrmodel Das NReferenzModel, &uuml;ber das die Manipulation des Vectors definiert 
     *     wird.
     * @param columnnames Die Namen der Spalten in der Tabellensicht.
     */
    public SelectableSortedListSubEditor(ArchimedesApplication app, Object sv, Object bp, 
            Object lo, NReferenzModel nrmodel, ArchimedesDynamic ad, String[] columnnames) {
        super();
        this.app = app;
        // this.bp = bp;
        // this.listowner = lo;
        // this.nrm = nrmodel;
        this.sv = (SortedVector) sv;
        if (app.getFrame() instanceof ComponentWithInifile) {
            this.ini = ((ComponentWithInifile) app.getFrame()).getInifile();
        }
        Vector allelements = null;
        try {
            allelements = this.app.getDFC().read(bp.getClass(), null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.svcopy = new SortedVector();
        for (int i = 0, len = allelements.size(); i < len; i++) {
            Object o = allelements.elementAt(i);
            boolean selected = this.sv.contains(o);
            svcopy.addElement(new SelectableVectorElement(o, selected));
        }
        this.anzeige = new JTableWithInifile(new SelectableVectorTableModel(this.svcopy, 
                columnnames), this.ini, "SelectableSortedListSubEditor-" + bp.getClass());
        JPanel panelAnzeige = new JPanel(new GridLayout(1, 1, Constants.HGAP, Constants.VGAP));
        panelAnzeige.setBorder(new EmptyBorder(1, 1, 1, 1));
        panelAnzeige.add(new JScrollPane(this.anzeige));
        this.panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        this.panel.add(panelAnzeige, BorderLayout.CENTER);
        this.components = new JComponent[] {this.anzeige};
        this.labels = new JLabel[] {new JLabel()};
        this.panels = new JPanel[] {panelAnzeige};
        if (this.anzeige instanceof JTableWithInifile) {
            ((JTableWithInifile) this.anzeige).restoreFromIni();
        }
    }
    
    /*
    private void doRepaint() {
        ((AbstractTableModel) anzeige.getModel()).fireTableDataChanged();
        // this.anzeige.setModel(new TabellenspaltenTableModel(this.tabelle));
    }
    */

    
    /* Implementierung des Interfaces SubEditorDesriptor. */
    
    public void cleanupData() {
        if (this.anzeige instanceof JTableWithInifile) {
            ((JTableWithInifile) this.anzeige).saveToIni();
        }
    }
    
    public JComponent getComponent(int n) {
        return this.components[n];
    }
    
    public int getComponentCount() {
        return this.components.length;
    }

    public JPanel getComponentPanel(int n) {
        return this.panels[n];
    }
    
    public JLabel getLabel(int n) {
        return this.labels[n];
    }
    
    public JTable getJTable() {
        return this.anzeige;
    }
    
    public java.util.List getList() {
        return this.sv;
    }
    
    public JPanel getPanel() {
        return this.panel;
    }
    
    public void setObject(Attributed attr) {
        // this.listowner = attr;
    }
    
    public void transferData() {
        this.sv.removeAllElements();
        for (int i = 0, len = this.svcopy.size(); i < len; i++) {
            SelectableVectorElement sve = (SelectableVectorElement) this.svcopy.elementAt(i);
            if (sve.isSelected()) {
                this.sv.addElement((Comparable) sve.getObject());
            }
        }
    }
    
}
