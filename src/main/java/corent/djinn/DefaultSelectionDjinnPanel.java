/*
 * DefaultSelectionDjinnPanel.java
 *
 * 04.02.2004
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;
import corent.gui.*;
import corent.util.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;


/**
 * Diese Klasse bietet eine Musterimplementierung eines SelectionDjinns auf Basis eines Panels,
 * das sie bequem an verschiedene Gegebenheiten anpassen und in verschiedene Komponenten 
 * einbauen l&auml;&szlig;t.
 * <P>Die Anzahl der maximalen Treffer k&ouml;nnen &uuml;ber die Property 
 * <I>corent.djinn.ViewComponent.maximum</I> gesetzt werden.
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
 *         <TD>corent.djinn.CreateCycle.vk.down</TD>
 *         <TD>VK_DOWN</TD>
 *         <TD>String</TD>
 *         <TD>
 *             &Uuml;ber diese Property k&ouml;nnen kann der Tastatuscode zum Weiterschalten des
 *             Focus innerhalb der EditorDjinn-Logik konfiguriert werden (vorw&auml;rts).
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.CreateCycle.vk.escape</TD>
 *         <TD>VK_ESCAPE</TD>
 *         <TD>String</TD>
 *         <TD>
 *             &Uuml;ber diese Property k&ouml;nnen kann der Tastatuscode zum Verwerfen 
 *             der &Auml;nderungen im Dialog konfiguriert werden.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.CreateCycle.vk.f12</TD>
 *         <TD>VK_F12</TD>
 *         <TD>String</TD>
 *         <TD>
 *             &Uuml;ber diese Property k&ouml;nnen kann der Tastatuscode zum &Uuml;bernehmen 
 *             der &Auml;nderungen im Dialog konfiguriert werden.
 *         </TD>
 *     </TR>
 *     <TR VALIGN=TOP>
 *         <TD>corent.djinn.CreateCycle.vk.up</TD>
 *         <TD>VK_UP</TD>
 *         <TD>String</TD>
 *         <TD>
 *             &Uuml;ber diese Property k&ouml;nnen kann der Tastatuscode zum Weiterschalten des
 *             Focus innerhalb der EditorDjinn-Logik konfiguriert werden (r&uuml;ckw&auml;rts).
 *         </TD>
 *     </TR>
 * </TABLE>
 * <P>nbsp;
 *
 * @author 
 *     <P>O.Lieshoff
 *
 * @changed
 *     OLI 03.10.2008 - Erweiterung um die M&ouml;glichkeit eine SelectionComponent an den
 *             Konstruktor des Djinns zu &uuml;bergeben.
 *     <P>OLI 06.10.2008 - Debugging an der Auswertung der Suchkriterien (updateView).
 *     <P>
 *
 */ 

public class DefaultSelectionDjinnPanel extends JPanel implements SelectionDjinn, Runnable {
    
    /* Flagge zum Stoppen des Aktualisierungs-Threads. */
    private boolean stopped = false;
    /* Der Abbruch-Button des SelectionPanels. */
    private JButton buttonAbbruch = null;
    /* Der Auswahl-Button des SelectionPanels. */
    private JButton buttonAuswahl = null;
    /* Der Duplizieren-Button des SelectionPanels. */
    private JButton buttonDuplizieren = null;
    /* Der Neuanlage-Button des SelectionPanels. */
    private JButton buttonNeuanlage = null;
    /* Label f&uuml;r die Meldung &uuml;ber die Anzahl der gefundenen Datens&auml;tze. */
    private JLabel labelAnzahlDatensaetze = null;
    /* Eingabefeld f&uuml;r die Einschr&auml;nkungskriterien. */
    private SelectionComponent criteriaInput = null;
    /* Der Thread zur Aktualisierung der Markierung f&uuml;r die fokussierte Komponente. */
    private Thread thread = null;
    /* Die Liste der an dem SelectionDjinn horchenden Listener. */
    private Vector listener = new Vector();
    /* 
     * Die Komponente, die in die Mitte des Panels eingebunden wird und die Listenanzeige
     * enth&auml;lt.
     */
    private ViewComponent view = null;
    /* Referenz auf die ViewComponentFactory, aus der das Panel seine Komponenten bezieht. */
    private ViewComponentFactory vcf = null;
    
    /**
     * Generiert ein DefaultSelectionDjinnPanel anhand der &uuml;bergebenen Factories.
     *
     * @param viewComponentFactory ViewComponentFactory zum Herstellen einer ViewComponent zur 
     *     Anzeige der Listenauswahl.
     * @param sdbf Eine Factory zum Erstellen der Buttons des Djinns.
     */
    public DefaultSelectionDjinnPanel(ViewComponentFactory viewComponentFactory, 
            SelectionDjinnButtonFactory sdbf) {
        this(viewComponentFactory, sdbf, false, null);
    }
    
    /**
     * Generiert ein DefaultSelectionDjinnPanel anhand der &uuml;bergebenen Factories.
     *
     * @param viewComponentFactory ViewComponentFactory zum Herstellen einer ViewComponent zur 
     *     Anzeige der Listenauswahl.
     * @param sdbf Eine Factory zum Erstellen der Buttons des Djinns.
     * @param criteria Diese Flagge mu&szlig; mit dem Wert <TT>true<TT> &uuml;bergeben werden,
     *     um die Anzeige eines Eingabefeldes f&uuml;r das Einschr&auml;nken der Suche zu 
     *     erm&ouml;glichen.
     */
    public DefaultSelectionDjinnPanel(ViewComponentFactory viewComponentFactory, 
            SelectionDjinnButtonFactory sdbf, boolean criteria) {
        this(viewComponentFactory, sdbf, criteria, null);
    }
    
    /**
     * Generiert ein DefaultSelectionDjinnPanel anhand der &uuml;bergebenen Factories.
     *
     * @param viewComponentFactory ViewComponentFactory zum Herstellen einer ViewComponent zur 
     *     Anzeige der Listenauswahl.
     * @param sdbf Eine Factory zum Erstellen der Buttons des Djinns.
     * @param criteria Diese Flagge mu&szlig; mit dem Wert <TT>true<TT> &uuml;bergeben werden,
     *     um die Anzeige eines Eingabefeldes f&uuml;r das Einschr&auml;nken der Suche zu 
     *     erm&ouml;glichen.
     * @param sc Eine SelectionComponent zur Einschr&auml;nkung der angezeigten Datens&auml;tze.
     *     Diese Angabe &uuml;berschreibt etwaige Angaben aus der ViewComponentFactory.
     *
     * @changed
     *     OLI 03.10.2008 - Erweiterung um die M&ouml;glichkeit eine SelectionComponent an den
     *             Djinn zu &uuml;bergeben.
     *     <P>OLI 30.01.2009 - Abfangen von ViewComponentFactories, die keine ServedClass kennen
     *             im Zusammenhang mit der Abpr&uuml;fung einer Unterdr&uuml;ckung der initialen
     *             Beladung des Panels mit Daten aus der Datenbank.
     *     <P>
     *
     */
    public DefaultSelectionDjinnPanel(ViewComponentFactory viewComponentFactory, 
            SelectionDjinnButtonFactory sdbf, boolean criteria, SelectionComponent sc) {
        super(new BorderLayout(Constants.HGAP, Constants.VGAP));
        this.vcf = viewComponentFactory;
        JPanel criteriaPanel = null;
        this.view = viewComponentFactory.createViewComponent();
        this.setBorder(new CompoundBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, 
                Constants.HGAP, Constants.VGAP), new EtchedBorder(Constants.ETCH)));
        this.setBorder(new CompoundBorder(this.getBorder(), new EmptyBorder(Constants.HGAP, 
                Constants.VGAP, Constants.HGAP, Constants.VGAP)));
        this.labelAnzahlDatensaetze = new JLabel("", JLabel.CENTER);
        JPanel list = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
        list.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), new EmptyBorder(
                Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
        list.add(this.view.getView(), BorderLayout.CENTER);
        list.add(this.labelAnzahlDatensaetze, BorderLayout.SOUTH);
        if (criteria) {
            if (sc != null) {
                this.criteriaInput = sc;
            } else if (this.vcf.createSelectionComponent() != null) {
                this.criteriaInput = this.vcf.createSelectionComponent();
            } else {
                this.criteriaInput = new SimpleSelector(20);
            }
            this.criteriaInput.addKeyListener(new KeyAdapter() {
                public void keyPressed(KeyEvent e) {
                    if ((e.getKeyCode() == KeyEvent.VK_ENTER) 
                            || (e.getKeyCode() == KeyEvent.VK_DOWN)) {
                        updateView(new String[] {criteriaInput.getFastCriteria()}, 
                                criteriaInput.getAdditions());
                        if (view.getSelectedValues().size() > 0) {
                            if (view.getViewComponent() instanceof JList) {
                                ((JList) view.getViewComponent()).setSelectedIndex(0); 
                            }
                        }
                        view.getViewComponent().requestFocus();
                    } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                        if (buttonAbbruch != null) {
                            buttonAbbruch.requestFocus();
                        }
                    }
                }
            });
            this.criteriaInput.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    updateView(new String[] {criteriaInput.getFastCriteria()}, 
                            criteriaInput.getAdditions());
                }
            });
            this.criteriaInput.addMassiveListSelectorListener(new MassiveListSelectorListener()
                    {
                public void selectionAltered(MassiveListSelectorEvent e) {
                    updateView(new String[] {criteriaInput.getFastCriteria()}, 
                            criteriaInput.getAdditions());
                }
                public void selectionCleared(MassiveListSelectorEvent e) {
                    updateView(new String[] {criteriaInput.getFastCriteria()}, 
                            criteriaInput.getAdditions());
                }
            });
            criteriaPanel = new JPanel(new GridLayout(1, 1, Constants.HGAP, Constants.VGAP));
            criteriaPanel.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), 
                    new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, 
                    Constants.VGAP)));
            criteriaPanel.add((JComponent) this.criteriaInput);
        }
        this.view.getViewComponent().addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    doButtonAuswahl();
                }
            }
        });
        this.view.getViewComponent().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    doButtonAuswahl();
                }
            }
        });
        // Die Buttons einbauen.
        this.buttonAbbruch = sdbf.createButtonAbbruch(); 
        this.buttonAuswahl = sdbf.createButtonAuswahl(); 
        this.buttonDuplizieren = sdbf.createButtonDuplizieren(); 
        this.buttonNeuanlage = sdbf.createButtonNeuanlage();
        JPanel buttons = new JPanel(new GridLayout(1, 5, Constants.HGAP, Constants.VGAP));
        buttons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH), new EmptyBorder(
                Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
        if (this.buttonNeuanlage != null) {                
            buttons.add(this.buttonNeuanlage);
            this.buttonNeuanlage.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doButtonNeuanlage();
                }
            });
        } else {
            buttons.add(new JLabel(""));
        }
        if (this.buttonDuplizieren != null) {
            buttons.add(this.buttonDuplizieren);
            this.buttonDuplizieren.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doButtonDuplizieren();
                }
            });
        } else {
            buttons.add(new JLabel(""));
        }
        buttons.add(new JLabel(""));
        if (this.buttonAuswahl != null) {
            buttons.add(this.buttonAuswahl);
            this.buttonAuswahl.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doButtonAuswahl();
                }
            });
        } else {
            buttons.add(new JLabel(""));
        }
        if (this.buttonAbbruch != null) {
            buttons.add(this.buttonAbbruch);
            this.buttonAbbruch.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    doButtonAbbruch();
                }
            });
        } else {
            buttons.add(new JLabel(""));
        }
        Vector komponenten = new Vector();
        Vector buttonliste = new Vector();
        if (this.buttonNeuanlage != null) {
            buttonliste.addElement(this.buttonNeuanlage);
        }
        if (this.buttonDuplizieren != null) {
            buttonliste.addElement(this.buttonDuplizieren);
        }
        if (this.buttonAuswahl != null) {
            buttonliste.addElement(this.buttonAuswahl);
        }
        if (this.buttonAbbruch != null) {
            buttonliste.addElement(this.buttonAbbruch);
        }
        // komponenten.addElement(this.view.getViewComponent());
        KeyListenerDjinn.CreateRow(buttonliste, true, KeyEvent.VK_LEFT, KeyEvent.VK_RIGHT);
        KeyListenerDjinn.CreateCycle(komponenten, this.buttonAuswahl, this.buttonAbbruch, false, 
                   SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.up", 
                   "VK_UP")),
                   SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.down", 
                   "VK_DOWN")), 
                   SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.f12", 
                   "VK_F12")), 
                   SysUtil.GetKeyCode(System.getProperty("corent.djinn.CreateCycle.vk.excape", 
                   "VK_ESCAPE")));
        this.view.getViewComponent().addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                if ((view.getViewComponent().isFocusOwner()) 
                        && (e.getKeyCode() == KeyEvent.VK_ESCAPE) && (criteriaInput != null)) {
                    ((JComponent) criteriaInput).requestFocus();
                }
            }
        });
        // Panel zusammennageln.
        if (criteria) {
            this.add(criteriaPanel, BorderLayout.NORTH);
        }
        this.add(list, BorderLayout.CENTER);
        this.add(buttons, BorderLayout.SOUTH);
        // Button-Controller-Thread starten.   
        this.thread = new Thread(this);
        this.thread.start();
        if ((this.vcf.getServedClass() != null) && (!Boolean.getBoolean(
                "corent.djinn.DefaultSelectionDjinnPanel.suppress.first.update.for.class."
                + this.vcf.getServedClass().getName()))) {
            this.updateView();
        }
        this.view.getViewComponent().transferFocus();
        if (this.criteriaInput != null) {
            ((JComponent) this.criteriaInput).requestFocus();
        }
    }
        
    /** Diese Methode wird aufgerufen, wenn der Neuanlage-Button bet&auml;tigt wird. */
    public void doButtonNeuanlage() {
        this.fireElementCreated();
    }

    /** Diese Methode wird aufgerufen, wenn der Duplizieren-Button bet&auml;tigt wird. */
    public void doButtonDuplizieren() {
        this.fireSelectionDuplicated(this.view.getSelectedValues());
    }

    /** Diese Methode wird aufgerufen, wenn der Auswahl-Button bet&auml;tigt wird. */
    public void doButtonAuswahl() {
        this.fireSelectionDone(this.view.getSelectedValues());
    }

    /** Diese Methode wird aufgerufen, wenn der Abbruch-Button bet&auml;tigt wird. */
    public void doButtonAbbruch() {
        this.view.close();
        this.fireDjinnClosing();
        this.fireSelectionAborted();
        this.fireDjinnClosed();
    }
    
    /** 
     * Diese Methode aktualisiert die Listenanzeige des Frames.
     *
     * @changed
     *     OLI 06.10.2008 - Debugging. Additions und normale Kriterien hatten sich gegenseitig
     *             behindert.
     *     <P>
     * 
     */
    public void updateView() {
        String[] criteria = null;
        if ((this.criteriaInput != null) && (this.criteriaInput.getFastCriteria().length() > 0))
                {
            criteria = new String[] {this.criteriaInput.getFastCriteria()};
        }
        this.updateView(criteria, ((this.criteriaInput != null) 
                && (this.criteriaInput.getAdditions() != null)
                && (this.criteriaInput.getAdditions().length() > 0) 
                ? this.criteriaInput.getAdditions() : null));
    }
    
    /** 
     * Diese Methode aktualisiert die Listenanzeige des Frames.
     *
     * @param criteria Die Auswahlkriterien, die auf die Liste angewandt werden sollen, um den 
     *     View einzuschr&auml;ken.
     * @param additions Zus&auml;tzliche Angaben (gegebenenfalls in SQL) zur Where-Klausel des 
     *     Views.
     *
     * @changed
     *     OLI 03.01.2008 - Erweiterung um den Additions-Parameter.
     *     <P>
     *
     */
    public void updateView(Object[] criteria, String additions) {   
        try {
            int lim = Integer.getInteger("corent.djinn.ViewComponent.maximum", 0);
            if ((this.vcf.getServedClass() != null) && (Integer.getInteger(
                    "corent.djinn.ViewComponent.maximum." + this.vcf.getServedClass().getName(
                    ), 0) > 0)) {
                lim = Integer.getInteger("corent.djinn.ViewComponent.maximum." 
                        + this.vcf.getServedClass().getName(), 0);
            }
            int count = this.view.updateView(criteria, additions);
            String s = "";            
            if (count > 0) {
                s = String.format(this.vcf.getFormatRecordCountInLimit(), count); 
            } else if (count == 0) {
                s = this.vcf.getFormatNoRecordsFound();
            } else {
                s = String.format(this.vcf.getFormatRecordCountOffLimit(), lim, 0 - count); 
            }
            this.labelAnzahlDatensaetze.setText(s);
            this.fireSelectionUpdated();
        } catch (Exception e) {
            e.printStackTrace();
            // throw new IllegalArgumentException(e.toString());
        }
    }
    
    /** Initialisiert den View. */
    public void initView() {
        this.view.init();
    }
    
    /** @return Inhalt des Kriterieneingabefeldes. */
    public String getSelector() {
        return this.criteriaInput.getFastCriteria();
    }
    
    
    /* Implementierung des Interfaces SelectionDjinn. */

    public void addSelectionDjinnListener(SelectionDjinnListener sdl) {
        this.listener.addElement(sdl);        
    }

    public void removeSelectionDjinnListener(SelectionDjinnListener sdl) {
        this.listener.removeElement(sdl);        
    }

    public void fireSelectionDone(Vector selected) {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((SelectionDjinnListener) this.listener.elementAt(i)).selectionDone(selected);
        }
    }
    
    public void fireSelectionAborted() {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((SelectionDjinnListener) this.listener.elementAt(i)).selectionAborted();
        }
    }
                
    public void fireSelectionDuplicated(Vector selected) {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((SelectionDjinnListener) this.listener.elementAt(i)).selectionDuplicated(selected);
        }
    }

    public void fireSelectionUpdated() {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((SelectionDjinnListener) this.listener.elementAt(i)).selectionUpdated();
        }
    }

    public void fireElementCreated() {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((SelectionDjinnListener) this.listener.elementAt(i)).elementCreated();
        }
    }
                
    public void fireDjinnClosing() {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((SelectionDjinnListener) this.listener.elementAt(i)).djinnClosing();
        }
    }
    
    public void fireDjinnClosed() {
        for (int i = 0, len = this.listener.size(); i < len; i++) {
            ((SelectionDjinnListener) this.listener.elementAt(i)).djinnClosed();
        }
    }    

    
    /* Implementierung des Interfaces Runnable. */
    
    public void run() {
        boolean[] enabled = new boolean[] {true, true, true, true};
        enabled[0] = (this.buttonAbbruch != null ? this.buttonAbbruch.isEnabled() : false);
        enabled[1] = (this.buttonAuswahl != null ? this.buttonAuswahl.isEnabled() : false);
        enabled[2] = (this.buttonDuplizieren != null ? this.buttonDuplizieren.isEnabled() 
                : false);
        enabled[3] = (this.buttonNeuanlage != null ? this.buttonNeuanlage.isEnabled() : false);
        while (!this.stopped) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
            try {
                if (this.view.getSelectedValuesCount() > 0) {
                    if (enabled[1] && (this.buttonAuswahl != null)) {
                        this.buttonAuswahl.setEnabled(true);
                        // Java 1.4.1
                        // this.buttonAuswahl.setFocusable(true);
                    }
                    if (enabled[2] && (this.buttonDuplizieren != null)) {
                        this.buttonDuplizieren.setEnabled(true);
                        // this.buttonDuplizieren.setFocusable(true);
                    }
                    if (enabled[3] && (this.buttonNeuanlage != null)) {
                        this.buttonNeuanlage.setEnabled(true);
                    }
                } else {
                    if (this.buttonAuswahl != null) {
                        this.buttonAuswahl.setEnabled(false);
                        // this.buttonAuswahl.setFocusable(false);
                    }
                    if (this.buttonDuplizieren != null) {
                        this.buttonDuplizieren.setEnabled(false);
                        // this.buttonDuplizieren.setFocusable(false);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    
}


class SimpleSelector extends JTextField implements SelectionComponent {
    
    public SimpleSelector(int len) {
        super(len);
    }
    
    
    /* Implementierung des Interfaces SelectionComponent. */
    
    public void addActionListener(ActionListener l) {
    }
    
    public void addMassiveListSelectorListener(MassiveListSelectorListener l) {
    }
    
    public String getAdditions() {
        return null;
    }
    
    public String getFastCriteria() {
        return this.getText();
    }
    
    public void removeActionListener(ActionListener l) {
    }
    
    public void removeMassiveListSelectorListener(MassiveListSelectorListener l) {
    }
    
}
