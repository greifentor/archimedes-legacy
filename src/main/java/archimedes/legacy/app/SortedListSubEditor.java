/*
 * SortedListSubEditor.java
 *
 * 09.07.2005
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.app;


import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;

import corent.base.*;
import corent.db.xs.*;
import corent.djinn.*;
import corent.files.*;
import corent.gui.*;
import corent.print.*;

import archimedes.legacy.model.*;


/**
 * Ein in einen EditorDjinn einbindbarer SubEditor zur Bearbeitung von sortierten Listen.
 * <P>&Uuml;ber die folgenden Properties kann gesteuert werden, ob der SubEditor EditorDjinns 
 * als Frame oder Dialog &ouml;ffnet:
 * <BR><B>archimedes.app.SortedListSubEditor.create.frame</B> = Wird diese Property mit dem Wert
 * <TT>true</TT> belegt, so werden die EditorDjinns, die durch das Bet&auml;tigen des
 * Neuanlage-Buttons des SubEditors erzeugt werden, als Frame instanziert. Sonst handelt es sich
 * bei den EditorDjinns um modale Dialoge. 
 * <BR><B>archimedes.app.SortedListSubEditor.edit.frame</B> = Wird diese Property mit dem Wert 
 * <TT>true</TT> belegt, so werden die EditorDjinns, die durch das Bet&auml;tigen des
 * Bearbeiten-Buttons des SubEditors erzeugt werden, als Frame instanziert. Sonst handelt es 
 * sich bei den EditorDjinns um modale Dialoge. 
 *
 * @author 
 *     ollie
 *     <P>
 *
 * @changed
 *     OLI 21.09.2007 - Bei Aufrufen von Methoden aus dem interface 
 *             <TT>SortedListSubEditorMaster</TT> werden die aufrufenden Methoden nun beendet,
 *             wenn der R&uuml;ckgabewert aus der Methode des Interfaces eine 
 *             <TT>null</TT>-Referenz zur&uuml;ckgibt.  
 *     <P>OLI 09.10.2007 - Umbau der Lockmechanismen f&uuml;r bearbeitete Elemente auf 
 *             LockDjinns.
 *     <P>OLI 24.10.2007 - Erweiterung um die Implementierung des Interfaces 
 *             <TT>TableListSubEditor</TT>.
 *     <P>OLI 26.02.2008 - &Auml;nderungen im Rahmen der Einf&uuml;hrung des EditorDjinnModes.
 *     <P>OLI 09.01.2009 - Erweiterung um die Implementierung der Methode 
 *             <TT>setObject(Attributed)</TT> im Zuge der Erweiterung der Spezifikation des 
 *             Interfaces <TT>SubEditor</TT>.
 *     <P>
 *
 */

public class SortedListSubEditor implements TableListSubEditor {

    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn die EditorDjinns im splitted-Modus erzeugt 
     * werden sollen.
     */
    private boolean splitted = false;
    /*
     * Diese Flagge mu&szlig; gesetzt werden, wenn die Objekte der Liste in InternalFrames
     * editiert werden sollen.
     */
    private boolean internalFrameEdit = false;
    /* Referenz auf die ArchimedesApplication, innerhalb derer der SubEditor arbeiten soll. */
    private ArchimedesApplication app = null;
    /* Die Liste der Components des besitzenden EditorDjinnPanels. */
    private Hashtable<String, Component> edpcomponents = null; 
    /* 
     * Eine Referenz auf die durch die ArchimedesApplication genutzte Inidatei zur 
     * Fensterrekonstruktion, bzw. null, wenn es eine solche Datei nicht gibt.
     */
    private Inifile ini = null;
    /* Der Bearbeiten-Button des Panels. */
    private JButton buttonBearbeiten = null;
    /* Der Einf&uuml;gen-Button des Panels. */
    private JButton buttonEinfuegen = null;
    /* Der Entfernen-Button des Panels. */
    private JButton buttonEntfernen = null;
    /* Der Neuanlage-Button des Panels. */
    private JButton buttonNeuanlage = null;
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
    private NReferenzModel nrm = null;
    /* Eine Instanz der in dem bearbeiteten SortedVector gespeicherten Objekte. */
    private Object bp = null;
    /* Die Instanz, zu der die Liste geh&ouml;rt. */
    private Object listowner = null;
    /* Der SortedVector, der in dem Panel manipuliert wird. */
    private SortedVector sv = null;

    /**
     * Generiert einen SortedListSubEditor mit den &uuml;bergebenen Parametern.
     *
     * @param app Die ArchimedesApplication, innerhalb derer der SubEditor arbeiten soll. 
     * @param sv Der sortierte Vector, dessen Inhalt manipuliert werden soll.
     * @param blp Eine Beispielinstanz der Klasse, die in dem Vector gespeichert sind oder 
     *     werden sollen. Dies wird beispielsweise zum Einlesen neuer Objekte ben&ouml;tigt.
     * @param lo Die Instanz, zu der die Liste geh&ouml;rt.
     * @param nrmodel Das NReferenzModel, &uuml;ber das die Manipulation des Vectors definiert 
     *     wird.
     * @param vpbf Eine VectorPanelButtonFactory zum Erzeugen der Buttons des Panels.
     * @param columnnames Die Namen der Spalten in der Tabellensicht.
     * @param split Wird diese Flagge gesetzt, so werden die EditorDjinns zur gew&auml;hlten
     *     Aktion im split-Modus erzeugt.
     * @param internalFrameEdit Diese Flagge mu&szlig; gesetzt werden, wenn die Liste der 
     *     angezeigten Objekte aus selbstst&auml;ndigen Datens&auml;tzen besteht, die nicht
     *     existenzabh&auml;ngig vom Listeneigner sind. In diesem Falle werden Internal-Frames
     *     zur Manipulation der Objekte ge&ouml;ffnet.
     * @param edpcomponents Eine Hashtable mit den Kompenten des EditorDjinnPanels, in dem sich 
     *     der SubEditor befindet.
     */
    public SortedListSubEditor(ArchimedesApplication app, Object sv, Object blp, Object lo, 
            NReferenzModel nrmodel, ArchimedesDynamic ad, VectorPanelButtonFactory vpbf,
            String[] columnnames, boolean split, boolean internalFrameEdit, Hashtable<String, 
            Component> edpcomponents) {
        super();
        this.app = app;
        this.bp = blp;
        this.edpcomponents = edpcomponents;
        this.internalFrameEdit = internalFrameEdit;
        this.listowner = lo;
        this.nrm = nrmodel;
        this.splitted = split;
        this.sv = (SortedVector) sv;
        if (app.getFrame() instanceof ComponentWithInifile) {
            this.ini = ((ComponentWithInifile) app.getFrame()).getInifile();
        }
        this.buttonBearbeiten = vpbf.createButtonBearbeiten();
        this.buttonBearbeiten.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonBearbeiten();
            }
        });
        this.buttonEinfuegen = vpbf.createButtonEinfuegen();
        this.buttonEinfuegen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonEinfuegen();
            }
        });
        this.buttonEntfernen = vpbf.createButtonEntfernen();
        this.buttonEntfernen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonEntfernen();
            }
        });
        this.buttonNeuanlage = vpbf.createButtonNeuanlage();
        this.buttonNeuanlage.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doButtonNeuanlage();
            }
        });
        this.anzeige = new JTableWithInifile(new VectorTableModel(this.sv, columnnames), 
                this.ini, "SortedListSubEditor-" + blp.getClass()) {
            public TableCellRenderer getCellRenderer(int row, int column) {
                if ((bp instanceof ColumnViewable) && (((ColumnViewable) bp).getCellRenderer(
                        column) != null)) {
                    return ((ColumnViewable) bp).getCellRenderer(column);
                }
                return super.getCellRenderer(row, column);
            }
        };
        this.anzeige.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if ((e.getClickCount() == 2) && nrm.isAlterable()) {
                    doButtonBearbeiten();
                }
            }
        });
        JPanel panelButtons = new JPanel(new GridLayout(1, 4, Constants.HGAP, Constants.VGAP));
        boolean isExtendedLO = (this.listowner instanceof ExtendedListOwner);
        if ((!isExtendedLO && this.nrm.isPermitCreate()) || (isExtendedLO 
                && ((ExtendedListOwner) this.listowner).isCreateButtonEnabled(this.nrm.getId()))
                ) {
            panelButtons.add(this.buttonNeuanlage);
        } else {
            panelButtons.add(new JLabel());
        }
        if ((!isExtendedLO && this.nrm.isExtensible()) || (isExtendedLO
                && ((ExtendedListOwner) this.listowner).isInsertButtonEnabled(this.nrm.getId()))
                ) {
            panelButtons.add(this.buttonEinfuegen);
        } else {
            panelButtons.add(new JLabel());
        }
        if ((!isExtendedLO && (this.nrm.isPermitCreate() || this.nrm.isExtensible() 
                /* || !this.nrm.isAlterable() */)) || (isExtendedLO 
                && ((ExtendedListOwner) this.listowner).isDeleteButtonEnabled(this.nrm.getId()))
                ) {
            panelButtons.add(this.buttonEntfernen);
        } else {
            panelButtons.add(new JLabel());
        }
        if ((!isExtendedLO && this.nrm.isAlterable()) || (isExtendedLO 
                && ((ExtendedListOwner) this.listowner).isEditButtonEnabled(this.nrm.getId()))
                ) {
            panelButtons.add(this.buttonBearbeiten);
        } else {
            panelButtons.add(new JLabel());
        }
        JPanel panelAnzeige = new JPanel(new GridLayout(1, 1, Constants.HGAP, Constants.VGAP));
        panelAnzeige.setBorder(new EmptyBorder(1, 1, 1, 1));
        panelAnzeige.add(new JScrollPane(this.anzeige));
        this.panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        this.panel.add(panelAnzeige, BorderLayout.CENTER);
        this.panel.add(panelButtons, BorderLayout.NORTH);
        this.components = new JComponent[] {this.anzeige};
        this.labels = new JLabel[] {new JLabel()};
        this.panels = new JPanel[] {panelAnzeige};
        if (this.anzeige instanceof JTableWithInifile) {
            ((JTableWithInifile) this.anzeige).restoreFromIni();
        }
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Benutzer den Bearbeiten-Button bet&auml;tigt. 
     *
     * @changed
     *     OLI 21.09.2007 - Die Methode beendet sich nun, wenn der Aufruf der Methode
     *             <TT>beforeChangeObject</TT> aus dem 
     *             <TT>SortedListSubEditorMaster</TT>-interface eine <TT>null</TT>-Referenz
     *             zur&uuml;ckliefert.<BR>
     *     OLI 09.10.2007 - Umstellung der Lockvergabe auf LockDjinns.<BR>
     *
     */
    public void doButtonBearbeiten() {
        final SortedListSubEditor slse = this;
        if (this.anzeige.getSelectedRow() >= 0) {
            Editable e = (Editable) this.sv.elementAt(this.anzeige.getSelectedRow());
            Frame f = null;
            if ((this.listowner instanceof SortedListSubEditorMaster)) {
                e = ((SortedListSubEditorMaster) this.listowner).beforeChangeObject(
                        this.nrm.getId(), e);
                if (e == null) {
                    return;
                }
            }
            final Editable attr = e;
            String titel = "";
            if (attr instanceof ArchimedesDynamic) {
                titel = ((ArchimedesDynamic) attr).getTablename();
            }
            if (ApplicationObject.STANDARDAPP != null) {
                f = ApplicationObject.STANDARDAPP.getFrame();
            }
            if (this.internalFrameEdit) {
                boolean locked = new LockDjinn(ApplicationObject.STANDARDAPP.getDFC()).lock(attr
                        );
                new InternalFrameEditorDjinn(
                        ApplicationObject.STANDARDAPP.getDesktop(), StrUtil.FromHTML(
                        "&Auml;ndern " + titel), attr, false, (attr instanceof JasperReportable
                        ), (attr instanceof HistoryWriter), this.ini, this.splitted, locked,
                        EditorDjinnMode.EDIT, false) {
                    public void doClosed() {
                        new LockDjinn(ApplicationObject.STANDARDAPP.getDFC()).unlock(attr);
                    }
                    public void doChanged(boolean saveOnly) {
                        try {
                            sv.removeElement(attr);
                            ApplicationObject.STANDARDAPP.getDFC().write(attr);
                            if ((listowner instanceof SortedListSubEditorMaster) 
                                    && !((SortedListSubEditorMaster) listowner).isChangedObject(
                                    nrm.getId(), sv, attr)) {
                                return;
                            }
                            if ((listowner instanceof SortedListSubEditorWatcher) 
                                    && !((SortedListSubEditorWatcher) listowner).dataChanged(
                                    new SortedListSubEditorEventObject(slse, attr, 
                                    edpcomponents, SortedListSubEditorEventType.OBJECT_CHANGED,
                                    sv))) {
                                return;
                            }
                            // sv.addElement(attr);
                            doRepaint();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };
            } else if (!Boolean.getBoolean("archimedes.app.SortedListSubEditor.edit.frame")) {
                new DialogEditorDjinn(f, Utl.GetProperty(
                        "archimedes.app.ListSubEditor.edit.dialog." + e.getClass().getName() 
                        + ".title", "&Auml;ndern " + titel), attr, false, (attr instanceof 
                        HistoryWriter), this.ini, this.splitted, false, EditorDjinnMode.EDIT) {
                    public void doClosed() {
                        doRepaint();
                    }
                    public void doChanged(boolean saveOnly) {
                        sv.removeElement(attr);
                        if ((listowner instanceof SortedListSubEditorMaster) 
                                && !((SortedListSubEditorMaster) listowner).isChangedObject(
                                nrm.getId(), sv, attr)) {
                            return;
                        }
                        if ((listowner instanceof SortedListSubEditorWatcher) 
                                && !((SortedListSubEditorWatcher) listowner).dataChanged(
                                new SortedListSubEditorEventObject(slse, attr, edpcomponents, 
                                SortedListSubEditorEventType.OBJECT_CHANGED, sv))) {
                            return;
                        }
                        sv.addElement((Comparable) attr);
                        anzeige.repaint();
                    }
                };
            } else {
                new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern " 
                        + titel), attr, false, false, (attr instanceof HistoryWriter), this.ini,
                        this.splitted, false, EditorDjinnMode.EDIT) {
                    public void doClosed() {
                        doRepaint();
                    }
                    public void doChanged(boolean saveOnly) {
                        sv.removeElement(attr);
                        if ((listowner instanceof SortedListSubEditorMaster) 
                                && !((SortedListSubEditorMaster) listowner).isChangedObject(
                                nrm.getId(), sv, attr)) {
                            return;
                        }
                        if ((listowner instanceof SortedListSubEditorWatcher) 
                                && !((SortedListSubEditorWatcher) listowner).dataChanged(
                                new SortedListSubEditorEventObject(slse, attr, edpcomponents, 
                                SortedListSubEditorEventType.OBJECT_CHANGED, sv))) {
                            return;
                        }
                        sv.addElement((Comparable) attr);
                        anzeige.repaint();
                    }
                };
            }
        }        
    }

    /** 
     * Diese Methode wird aufgerufen, wenn der Benutzer den Einfuegen-Button bet&auml;tigt.
     *
     * @changed
     *     OLI 21.09.2007 - Die Methode beendet sich nun, wenn der Aufruf der Methode
     *             <TT>beforeInsertObject</TT> aus dem 
     *             <TT>SortedListSubEditorMaster</TT>-interface eine <TT>null</TT>-Referenz
     *             zur&uuml;ckliefert.
     *
     */
    public void doButtonEinfuegen() {
        final SortedListSubEditor slse = this;
        Editable attr;
        if (this.bp instanceof ArchimedesDynamic) {
            attr = (Editable) ((ArchimedesDynamic) this.bp).createObject();
        } else {
            try {
                attr = (Editable) this.bp.getClass().newInstance();
            } catch (Exception e) {
                e.printStackTrace();
                return;
            }
        }
        String preselection = "";
        if ((listowner instanceof SortedListSubEditorMaster)) {
            attr = ((SortedListSubEditorMaster) this.listowner).beforeInsertObject(
                    this.nrm.getId(), attr);
            if (attr == null) {
                return;
            }
            preselection = ((SortedListSubEditorMaster) this.listowner).getPreselection(
                    this.nrm.getId(), attr); 
        }
        String titel = "";
        if (attr instanceof ArchimedesDynamic) {
            titel = ((ArchimedesDynamic) attr).getTablename();
        }
        Class cls = attr.getClass();
        if (attr instanceof NReferenceAlternativeSelector) {
            Class acls = ((NReferenceAlternativeSelector) attr).getAlternativeSelectionClass(
                    this.nrm.getId());
            if (acls != null) {
                cls = acls;
            }
        }
        DialogSelectionDjinn dsd = new DialogSelectionDjinn(this.app.getFrame(), 
                Utl.GetProperty("archimedes.app.ListSubEditor.selection.dialog." + cls.getName()
                + ".title", "Auswahl " + titel), new DBFListViewComponentFactory(
                this.app.getDFC(), cls, (this.app.getFrame() instanceof ComponentWithInifile ? 
                ((ComponentWithInifile) this.app.getFrame()).getInifile() : null), preselection
                ), (this.app.getFrame() instanceof ComponentWithInifile ? ((ComponentWithInifile
                ) this.app.getFrame()).getInifile() : null), true);
        if (dsd.isSelected()) {
            Vector selection = dsd.getSelection();
            if ((listowner instanceof SortedListSubEditorMaster) 
                    && !((SortedListSubEditorMaster) this.listowner).isInsertObject(
                    this.nrm.getId(), sv, selection)) {
                return;
            }
            for (int i = 0, len = selection.size(); i < len; i++) {
                Object slctd = selection.elementAt(i);
                if (attr instanceof NReferenceAlternativeSelector) {
                    slctd = ((NReferenceAlternativeSelector) attr).doAfterAlternativeSelection(
                            slctd, this.nrm.getId(), this.listowner);
                }
                if ((listowner instanceof SortedListSubEditorWatcher) 
                        && !((SortedListSubEditorWatcher) listowner).dataChanged(
                        new SortedListSubEditorEventObject(slse, slctd, edpcomponents, 
                        SortedListSubEditorEventType.OBJECT_SELECTED, sv))) {
                    return;
                }
                this.sv.addElement((Comparable) slctd);
                anzeige.repaint();
            }
            this.doRepaint();
        }
    }

    /** Diese Methode wird aufgerufen, wenn der Benutzer den Entfernen-Button bet&auml;tigt. */
    public void doButtonEntfernen() {
        final SortedListSubEditor slse = this;
        if (this.anzeige.getSelectedRow() >= 0) {
            Object obj = this.sv.elementAt(this.anzeige.getSelectedRow());
            if ((listowner instanceof SortedListSubEditorMaster) 
                    && !((SortedListSubEditorMaster) this.listowner).isDeleteObject(
                    this.nrm.getId(), this.sv, obj)) {
                return;
            }
            if ((listowner instanceof SortedListSubEditorWatcher) 
                    && !((SortedListSubEditorWatcher) listowner).dataChanged(
                    new SortedListSubEditorEventObject(slse, obj, edpcomponents, 
                    SortedListSubEditorEventType.OBJECT_REMOVED, sv))) {
                return;
            }
            this.sv.removeElement(obj);
            this.doRepaint();
        }        
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Benutzer den Neuanlage-Button bet&auml;tigt. 
     *
     * @changed
     *     OLI 21.09.2007 - Die Methode beendet sich nun, wenn der Aufruf der Methode
     *             <TT>afterCreateObject</TT> aus dem 
     *             <TT>SortedListSubEditorMaster</TT>-interface eine <TT>null</TT>-Referenz
     *             zur&uuml;ckliefert.
     *
     */
    public void doButtonNeuanlage() {
        final SortedListSubEditor slse = this;
        Editable e = null;
        Frame f = null;
        if (this.listowner instanceof ListOwner) {
            e = (Editable) ((ListOwner) this.listowner).createElement(this.nrm.getId());
        } else if (this.bp instanceof ArchimedesDynamic) {
            e = (Editable) ((ArchimedesDynamic) this.bp).createObject();
        } else {
            try {
                e = (Editable) this.bp.getClass().newInstance();
            } catch (Exception ex) {
                ex.printStackTrace();
                return;
            }
        }
        if ((listowner instanceof SortedListSubEditorMaster)) {
            e = ((SortedListSubEditorMaster) this.listowner).afterCreateObject(this.nrm.getId(),
                    e);
            if (e == null) {
                return;
            }
        }
        final Editable attr = e;
        this.sv.addElement((Comparable) attr);
        String titel = "";
        if (attr instanceof ArchimedesDynamic) {
            titel = ((ArchimedesDynamic) attr).getTablename();
        }
        if (ApplicationObject.STANDARDAPP != null) {
            f = ApplicationObject.STANDARDAPP.getFrame();
        }
        if (this.internalFrameEdit) {
            new InternalFrameEditorDjinn(
                    ApplicationObject.STANDARDAPP.getDesktop(), Utl.GetProperty(
                    "archimedes.app.ListSubEditor.edit.dialog." + e.getClass().getName() 
                    + ".title", "Neuanlage " + titel), attr, false, (attr instanceof 
                    JasperReportable), false, this.ini, this.splitted, false, 
                    EditorDjinnMode.CREATE, false) {
                public void doChanged(boolean saveOnly) {
                    try {
                        sv.removeElement(attr);
                        ApplicationObject.STANDARDAPP.getDFC().write(attr);
                        if ((listowner instanceof SortedListSubEditorMaster) 
                                && !((SortedListSubEditorMaster) listowner).isNewObject(
                                nrm.getId(), sv, attr)) {
                            return;
                        }
                        if ((listowner instanceof SortedListSubEditorWatcher) 
                                && !((SortedListSubEditorWatcher) listowner).dataChanged(
                                new SortedListSubEditorEventObject(slse, attr, edpcomponents, 
                                SortedListSubEditorEventType.OBJECT_CREATED, sv))) {
                            return;
                        }
                        // sv.addElement(attr);
                        doRepaint();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
        } else if (!Boolean.getBoolean("archimedes.app.SortedListSubEditor.create.frame")) {
            new DialogEditorDjinn(f, Utl.GetProperty(
                    "archimedes.app.ListSubEditor.create.dialog." + e.getClass().getName() 
                    + ".title", "Neuanlage " + titel), attr, false, false, this.ini, 
                    this.splitted, false, EditorDjinnMode.CREATE) {
                public void doClosed() {
                    doRepaint();
                }
                public void doChanged(boolean saveOnly) {
                    sv.removeElement(attr);
                    if ((listowner instanceof SortedListSubEditorMaster) 
                            && !((SortedListSubEditorMaster) listowner).isNewObject(
                            nrm.getId(), sv, attr)) {
                        return;
                    }
                    if ((listowner instanceof SortedListSubEditorWatcher) 
                            && !((SortedListSubEditorWatcher) listowner).dataChanged(
                            new SortedListSubEditorEventObject(slse, attr, edpcomponents, 
                            SortedListSubEditorEventType.OBJECT_CREATED, sv))) {
                        return;
                    }
                    sv.addElement((Comparable) attr);
                }
                public void doDiscarded() {
                    sv.removeElement(attr);
                }
            };
        } else {
            new FrameEditorDjinn("Neuanlage " + titel, attr, false, 
                    false, false, this.ini, this.splitted, false, EditorDjinnMode.CREATE) {
                public void doClosed() {
                    doRepaint();
                }
                public void doChanged(boolean saveOnly) {
                    sv.removeElement(attr);
                    if ((listowner instanceof SortedListSubEditorMaster) 
                            && !((SortedListSubEditorMaster) listowner).isNewObject(
                                    nrm.getId(), sv, attr)) {
                        return;
                    }
                    sv.addElement((Comparable) attr);
                }
                public void doDiscarded() {
                    sv.removeElement(attr);
                }
            };
        }
    }
    
    private void doRepaint() {
        ((AbstractTableModel) anzeige.getModel()).fireTableDataChanged();
        // this.anzeige.setModel(new TabellenspaltenTableModel(this.tabelle));
    }

    
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
        this.listowner = attr;
    }
    
    public void transferData() {
    }
    
}
