/*
 * DialogSelectionDjinn.java
 *
 * 30.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.awt.*;
import java.util.*;

import corent.files.*;
import corent.gui.*;


/**
 * Diese Implementierung des SelectionDjinns spielt sich in einem Dialog ab. F&uuml;r 
 * Anwendungen, die nur zum Manipulieren eines einzelnen Datensatzes erstellt werden sollen, ist
 * diese Variante die erste Wahl. Alternativ kann er auch genutzt werden, um 
 * InternalDialog-Applikationen zu umschiffen.
 * <P>Der Dialog arbeitet eng mit dem SelectionDjinn auf Panel-Basis zusammen, in dem die 
 * eigentliche Arbeit getan wird.
 *
 * @author O.Lieshoff
 *
 */

public class DialogSelectionDjinn extends JDialogWithInifile {
    
    /* Diese Flagge wird gesetzt, wenn der Dialog mit dem Auswahl-Button beendet wurde. */
    private boolean selected = false;
    /* Das DefaultSelectionDjinnPanel des SelectionDjinns. */
    private DefaultSelectionDjinnPanel panel = null;
    /* Die Liste der selektierten Objekte beim Dr&uuml;cken des Auswahl-Buttons.- */
    private Vector selection = new Vector();
    /* Eine Referenz auf die benutzte ViewComponentFactory. */
    // private ViewComponentFactory vcf = null;

    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     */ 
    public DialogSelectionDjinn(String titel, ViewComponentFactory vcf) {
        this(titel, vcf, null, true);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     * @param ini Die Inidatei, aus der der Dialog seine Gestalt rekonfigurieren soll.
     */ 
    public DialogSelectionDjinn(String titel, ViewComponentFactory vcf, Inifile ini) {
        this(titel, vcf, ini, true);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     * @param ini Die Inidatei, aus der der Dialog seine Gestalt rekonfigurieren soll.
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *     von Suchkriterien eingeblendet werden soll.
     */ 
    public DialogSelectionDjinn(String titel, ViewComponentFactory vcf, Inifile ini, 
            boolean selector) {
        super(ini);
        this.setTitle(titel);
        this.construct(vcf, selector);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param owner Der Dialog, von dem aus der Dialog erzeugt wird.
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     */ 
    public DialogSelectionDjinn(Dialog owner, String titel, ViewComponentFactory vcf) {
        this(owner, titel, vcf, null, true);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param owner Der Dialog, von dem aus der Dialog erzeugt wird.
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     * @param ini Die Inidatei, aus der der Dialog seine Gestalt rekonfigurieren soll.
     */ 
    public DialogSelectionDjinn(Dialog owner, String titel, ViewComponentFactory vcf, 
            Inifile ini) {
        this(owner, titel, vcf, ini, true);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param owner Der Dialog, von dem aus der Dialog erzeugt wird.
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     * @param ini Die Inidatei, aus der der Dialog seine Gestalt rekonfigurieren soll.
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *     von Suchkriterien eingeblendet werden soll.
     */ 
    public DialogSelectionDjinn(Dialog owner, String titel, ViewComponentFactory vcf, 
            Inifile ini, boolean selector) {
        super(owner, titel, ini);
        this.construct(vcf, selector);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param owner Der Frame, von dem aus der Dialog erzeugt wird.
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     */ 
    public DialogSelectionDjinn(Frame owner, String titel, ViewComponentFactory vcf) {
        this(owner, titel, vcf, null, true);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param owner Der Frame, von dem aus der Dialog erzeugt wird.
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     * @param ini Die Inidatei, aus der der Dialog seine Gestalt rekonfigurieren soll.
     */ 
    public DialogSelectionDjinn(Frame owner, String titel, ViewComponentFactory vcf, 
            Inifile ini) {
        this(owner, titel, vcf, ini, true);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Dialogbasis anhand der &uuml;bergebenen Parameter.
     *
     * @param owner Der Frame, von dem aus der Dialog erzeugt wird.
     * @param titel Der Titel des Dialogs.
     * @param vcf Die Factory, die die Auswahlanzeige erzeugt und verwaltet. 
     * @param ini Die Inidatei, aus der der Dialog seine Gestalt rekonfigurieren soll.
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *     von Suchkriterien eingeblendet werden soll.
     */ 
    public DialogSelectionDjinn(Frame owner, String titel, ViewComponentFactory vcf, 
            Inifile ini, boolean selector) {
        super(owner, titel, ini);
        this.construct(vcf, selector);
    }
    
    /* Hier wird der eigentliche Dialoginhalt zusammengenagelt. */
    private void construct(ViewComponentFactory vcf, boolean selector) {
        // this.vcf = vcf;
        this.setModal(true);
        SelectionDjinnButtonFactory sbf = null;
        if (((vcf.getServedClass() != null) && Boolean.getBoolean(
                "corent.djinn.DialogSelectionDjinn.allow.create." 
                + vcf.getServedClass().getName())) || Boolean.getBoolean(
                "corent.djinn.allow.create")) {
            sbf = new DefaultSelectionEditorDjinnButtonFactory();
        } else {
            sbf = new DefaultSelectionDjinnButtonFactory();
        }
        this.panel = new DefaultSelectionDjinnPanel(vcf, sbf, selector) {
            public void doButtonAuswahl() {
                fireDjinnClosing();
                super.doButtonAuswahl();
                selected = true;
                fireDjinnClosed();
            }
        };
        this.panel.addSelectionDjinnListener(new SelectionDjinnAdapter() {
            public void selectionDone(Vector selected) {
                doSelected(selected);          
            }
            public void djinnClosed() {
                dispose();
                setVisible(false);
            }
        });
        this.setContentPane(this.panel);
        this.pack();
        this.setVisible(true);
    }

    // REQUEST OLI 04.06.2010 - Kann die Methode wirklich entfernt werden ?!?
    /*
    private void updateView() {
        this.panel.updateView();
    }
    */

    /** 
     * Diese Methode wird aufgerufen, wenn &uuml;ber den Dialog eine Objekt-Auswahl 
     * stattgefunden hat.
     *
     * @param values Die ausgew&auml;hlten Objekte.
     */
    public void doSelected(Vector values) {
        for (int i = 0, len = values.size(); i < len; i++) {
            this.selection.addElement(values.elementAt(i));
        }
    }
    
    /** @return Das Selektionsergebnis vor dem Schliessen des Dialoges. */
    public Vector getSelection() {
        return this.selection;
    }
    
    /** @return <TT>true</TT>, wenn der Dialog mit dem Auswahl-Button beendet worden ist. */
    public boolean isSelected() {
        return this.selected;
    }
        
}
