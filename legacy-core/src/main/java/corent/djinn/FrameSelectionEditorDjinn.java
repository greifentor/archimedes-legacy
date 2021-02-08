/*
 * FrameSelectionEditorDjinn.java
 *
 * 19.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;

import corent.base.*;
import corent.files.*;
import corent.gui.*;


/**
 * Diese Implementierung des SelectionEditorDjinns spielt sich in einem Frame ab. F&uuml;r 
 * Anwendungen, die nur zum Manipulieren eines einzelnen Datensatzes erstellt werden sollen, ist
 * diese Variante die erste Wahl. Alternativ kann er auch genutzt werden, um 
 * InternalFrame-Applikationen zu umschiffen.
 * <P>Der Frame arbeitet eng mit dem SelectionEditorDjinn auf Panel-Basis zusammen, in dem die 
 * eigentliche Arbeit getan wird.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2017 - Set the editable references to final and removed the instance field
 *         "edi".
 */

public class FrameSelectionEditorDjinn extends JFrameWithInifile {

    /* Eine Vorlage zur Generierung neuer Listenelemente. */
    private Editable bp = null;
    /* Referenz auf das Selektionspanel. */
    private DefaultSelectionDjinnPanel panel = null;
    /* Referenz auf die Liste, die editiert werden soll. */
    private Vector l = null;

    /** 
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param liste Die Liste, aus der die Auswahl getroffen werden soll. 
     */ 
    public FrameSelectionEditorDjinn(String titel, Vector liste, Editable blueprint) {
        this(titel, liste, blueprint, null, true);
    }

    /** 
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param liste Die Liste, aus der die Auswahl getroffen werden soll.
     * @param blueprint Eine Vorlage zum Duplizieren von Objekten.
     * @param ini Eine Inidatei zu Speicherung der Fensterdaten.
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *     von Suchkriterien eingeblendet werden soll.
     */ 
    public FrameSelectionEditorDjinn(String titel, Vector liste, Editable blueprint, 
            Inifile ini, boolean selector) {
        super(titel, ini);
        final Inifile inf = ini;
        this.setIdentifier(this.getClass().toString() + "-" + blueprint.getClass().toString());
        this.bp = blueprint;
        this.l = liste;
        this.panel = new DefaultSelectionDjinnPanel(new DefaultListViewComponentFactory(this.l), 
                new DefaultSelectionEditorDjinnButtonFactory(), selector);
        this.panel.addSelectionDjinnListener(new SelectionDjinnAdapter() {
            @Override public void selectionDone(Vector selected) {
                final Editable edi = (Editable) selected.elementAt(0);
                new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern"), 
                        edi, inf) {
                    @Override public void doChanged(boolean saveOnly) {
                        l.removeElement(edi);
                        l.addElement(edi);
                        panel.updateView();
                    }
                    @Override public void doDeleted() {
                        if (permitDelete(edi)) {
                            l.removeElement(edi);
                            panel.updateView();
                        }
                    }
                };
            }
            @Override public void selectionDuplicated(Vector selected) {
                final Editable edi = (Editable) bp.createObject(selected.elementAt(0));
                new FrameEditorDjinn("Duplikat", edi, false, inf) {
                    @Override public void doChanged(boolean saveOnly) {
                        l.addElement(edi);
                        panel.updateView();
                    }
                };
            }
            @Override public void elementCreated() {
                final Editable edi = (Editable) bp.createObject();
                new FrameEditorDjinn("Neuanlage", edi, false, inf) {
                    @Override public void doChanged(boolean saveOnly) {
                        l.addElement(edi);
                        panel.updateView();
                    }
                };
            }
            @Override public void djinnClosed() {
                dispose();
                setVisible(false);
            }
        });
        this.setContentPane(this.panel);
        this.pack();
        this.setVisible(true);
    }

    /**
     * F&uuml;gt den &uuml;bergebenen SelectionDjinnListener an die Liste der den Djinn 
     * abh&ouml;renden Listener an.
     *
     * @param listener Der anzuf&uuml;gende Listener.
     */
    public void addSelectionDjinnListener(SelectionDjinnListener listener) {
        this.panel.addSelectionDjinnListener(listener);
    }    

    /**
     * L&ouml;scht den &uuml;bergebenen SelectionDjinnListener aus der Liste der den Djinn 
     * abh&ouml;renden Listener an.
     *
     * @param listener Der zu l&ouml;schende Listener.
     */
    public void removeSelectionDjinnListener(SelectionDjinnListener listener) {
        this.panel.removeSelectionDjinnListener(listener);
    }    

    /**
     * Diese Methode wird aufgerufen, wenn der Benutzer den L&ouml;schen-Button im 
     * &Auml;ndern-Dialog dr&uuml;ckt.
     *
     * @param obj Das Objekt, welches gel&ouml;scht werden soll.
     * @return <TT>true</TT>, wenn das Objekt zum Abschu&szlig; freigegeben werden soll.
     */
    public boolean permitDelete(Object obj) {
        return true;
    }

}