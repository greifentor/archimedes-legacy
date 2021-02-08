/*
 * FrameEditorDjinn.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;
import javax.swing.*;

import corent.base.*;
import corent.files.*;
import corent.gui.*;


/**
 * Diese Implementierung des EditorDjinns spielt sich in einem Frame ab. F&uuml;r Anwendungen, 
 * die nur zum Manipulieren eines einzelnen Datensatzes erstellt werden sollen, ist diese 
 * Variante die erste Wahl. Alternativ kann er auch genutzt werden, um 
 * InternalFrame-Applikationen zu umschiffen.
 * <P>Der Frame arbeitet eng mit dem PanelEditorDjinn zusammen, in dem die eigentliche Arbeit 
 * getan wird.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 29.10.2007 - Anpassungen an das DefaultEditorDjinnPanel mit Speichern-Button.
 *     <P>OLI 31.03.2008 - Umstellung der Info-Dialog (Verwerfen, L&ouml;schen) auf 
 *             Property-Ressourcen.
 *     <P>OLI 15.06.2008 - Erweiterung um den Aufruf der Methode <TT>doOpened()</TT> des
 *             eingebundenen DefaultEditorDjinnPanels beim &Ouml;ffnen des EditorDjinns.
 *     <P>OLI 05.01.2009 - Erweiterung um die Implementierung der Methode 
 *             <TT>setEditable(Editable)</TT>.
 *     <P>OLI 12.01.2009 - Deaktivierung der Methode <TT>setEditable(Editable)</TT>. Um das 
 *             korrekt durchzuprogrammieren fehlt im Momment die Zeit.
 *     <P>OLI 29.01.2009 - Implementierung der Erweiterung des Interfaces 
 *             <TT>EditorDjinnController</TT> durch die Methode <TT>getEditorDjinnMode()</TT>.
 *     <P>
 *
 */

public class FrameEditorDjinn extends JFrameWithInifile implements EditorDjinnController {
    
    /* Diese Flagge trifft eine Aussage dar&uuml;ber, ob das angezeigt Objekt gelockt ist. */
    private boolean locked = false;
    /* Referenz auf das EditorPanel des InternalFrames. */
    private DefaultEditorDjinnPanel panel = null;
    /* Der EditorDjinnMode, mit dem der Frame aufgerufen worden ist. */
    private EditorDjinnMode mode = null;

    /** 
     * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param e Das Editable, das in dem Frame editiert werden soll.
     */ 
    public FrameEditorDjinn(String titel, Editable e) {
        this(titel, e, true, false, false, null, false, false, EditorDjinnMode.EDIT);
    }
    
    /** 
     * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param e Das Editable, das in dem Frame editiert werden soll.
     * @param ini Eine Inidatei zur Speicherung der Fensterkoordinaten.
     */ 
    public FrameEditorDjinn(String titel, Editable e, Inifile ini) {
        this(titel, e, true, false, false, ini, false, false, EditorDjinnMode.EDIT);
    }
    
    /** 
     * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param e Das Editable, das in dem Frame editiert werden soll.
     * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der 
     *     L&ouml;schenbutton aktiviert werden soll.
     */ 
    public FrameEditorDjinn(String titel, Editable e, boolean loeschenAktiviert) {
        this(titel, e, loeschenAktiviert, false, false, null, false, false, EditorDjinnMode.EDIT
                );
    }
    
    /** 
     * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param e Das Editable, das in dem Frame editiert werden soll.
     * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der 
     *     L&ouml;schenbutton aktiviert werden soll.
     * @param ini Eine Inidatei zur Speicherung der Fensterkoordinaten.
     */ 
    public FrameEditorDjinn(String titel, Editable e, boolean loeschenAktiviert, Inifile ini) {
        this(titel, e, loeschenAktiviert, false, false, ini, false, false, EditorDjinnMode.EDIT
                );
    }
    
    /** 
     * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param e Das Editable, das in dem Frame editiert werden soll.
     * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der 
     *     L&ouml;schenbutton aktiviert werden soll.
     * @param druckenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Druckenbutton 
     *     aktiviert werden soll.
     * @param ini Eine Inidatei zur Speicherung der Fensterkoordinaten.
     */ 
    public FrameEditorDjinn(String titel, Editable e, boolean loeschenAktiviert, 
            boolean druckenAktiviert, Inifile ini) {
        this(titel, e, loeschenAktiviert, druckenAktiviert, false, ini, false, false, 
                EditorDjinnMode.EDIT);
    }
    
    /** 
     * Generiert einen EditorDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param e Das Editable, das in dem Frame editiert werden soll.
     * @param loeschenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der 
     *     L&ouml;schenbutton aktiviert werden soll.
     * @param druckenAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der Druckenbutton 
     *     aktiviert werden soll.
     * @param historieAktiviert Diese Flagge mu&szlig; gesetzt werden, wenn der 
     *     Historienbutton aktiviert werden soll.
     * @param ini Eine Inidatei zur Speicherung der Fensterkoordinaten.
     * @param split Wird diese Flagge gesetzt, erzeugt das EditorDjinnPanel im Falle eines
     *     TabbedEditables einen zweigeteilten Anzeigebereich. In der oberen H&auml;lfte wird 
     *     das erste Panel des TabbedEditables angezeigt. Die untere H&auml;lfte beinhaltet die
     *     restlichen Panels. Ist die Flagge nicht gesetzt, werden alle Panels in Tabs 
     *     eingef&uuml;gt.
     * @param locked Diese Flagge mu&szlig; gesetzt werden, wenn der Datensatz gesperrt ist.
     * @param mode Der Modus, in dem das Panel betrieben wird (Neuanlage, &Auml;nderung oder 
     *     Duplikation).
     */ 
    public FrameEditorDjinn(String titel, Editable e, boolean loeschenAktiviert, 
            boolean druckenAktiviert, boolean historieAktiviert, Inifile ini, boolean split, 
            boolean locked, EditorDjinnMode mode) {
        super(titel, ini);
        this.locked = locked;
        this.mode = mode;
        this.setIdentifier(this.getClass().toString() + "-" + e.getClass().toString());
        this.panel = new DefaultEditorDjinnPanel(this, e, new DefaultEditorDjinnButtonFactory(
                loeschenAktiviert, druckenAktiviert, historieAktiviert), split, locked, ini, 
                mode);
        this.panel.addEditorDjinnListener(new EditorDjinnListener() {
            public void objectReadyToPrint() {
                doReadyToPrint();
            }
            public void objectPrinted() {
                doPrinted();
            }
            public void objectDeleted() {
                doDeleted();
            }
            public void objectBatchChanged(Hashtable<Integer, Object> ht) {
                doBatchChanged(ht);
            }
            public void objectChanged(boolean saveOnly) {
                doChanged(saveOnly);
            }
            public void objectDiscarded() {
                doDiscarded();
            }
            public void djinnClosing() {
                doClosing();
            }
            public void djinnClosed() {
                dispose();
                setVisible(false);
                doClosed();
            }
        });
        this.setContentPane(this.panel);
        this.pack();
        this.setVisible(true);
        this.panel.doOpened();
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Djinn ein anstehendes Print-Ereignis 
     * signalisiert. 
     */
    public void doReadyToPrint() {
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Djinn ein abgeschlossened Print-Ereignis 
     * signalisiert. 
     */
    public void doPrinted() {
    }
    
    /** Diese Methode wird aufgerufen, wenn der Djinn ein Delete-Ereignis signalisiert. */
    public void doDeleted() {
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Djinn ein Stapel&auml;nderungs-Ereignis 
     * signalisiert.
     *
     * @param ht Eine Hashtable&lt;Integer, Object&gt; mit Attribute-Id-Wert-Paaren, die die 
     *     ge&auml;nderten Datenfelder und ihre neuen Werte enthalten. 
     */
    public void doBatchChanged(Hashtable<Integer, Object> ht) {
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Djinn ein &Auml;nderungs-Ereignis signalisiert.
     *
     * @param saveOnly Diese Flagge wird gesetzt, wenn die Daten des durch den Djinn 
     *     bearbeiteten Objektes gespeichert, der Djinn aber nicht geschlossen werdens soll.
     *
     * @changed
     *     OLI 29.10.2007 - Erweiterung um den Parameter <TT>saveOnly</TT>.<BR>
     *
     */
    public void doChanged(boolean saveOnly) {
    }
    
    /** Diese Methode wird aufgerufen, wenn der Djinn ein Abbruch-Ereignis signalisiert. */
    public void doDiscarded() {
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Djinn ein anstehendes Schliessen-Ereignis 
     * signalisiert. 
     */
    public void doClosing() {
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn der Djinn ein abgeschlossenes Schliessen-Ereignis 
     * signalisiert. 
     */
    public void doClosed() {
    }
    
    /**
     * Diese Methode liefert den Wert <TT>true</TT>, falls das in dem EditorDjinn angezeigte
     * Objekt gelockt ist (also nicht ge&auml;ndert werden darf).
     *
     * @return <TT>true</TT>, wenn das angezeigte Objekt gelockt, <TT>false</TT> sonst.
     */
    public boolean isLocked() {
        return this.locked;
    }

    /* *
     * @changed
     *     OLI 05.01.2009 - Hinzugef&uuml;gt.
     *     <P>OLI 12.01.2009 - Herausnahme wegen Zeitmangels und unvorhergesehener Probleme
     *             im Zusammenspiel mit dem <TT>DefaultEditorDjinnPanel</TT>.
     *     <P>
     *
     * /
    public void setEditable(Editable e) {
        this.panel.setEditable(e);
    }
    */


    /* Implementierung des Interfaces EditorDjinnController. */
    
    public boolean isDeleteConfirmed() {
        return (JOptionPane.showConfirmDialog(this, StrUtil.FromHTML(System.getProperty(
                "corent.djinn.infodialog.delete.text", "Soll der Datensatz "
                + "wirklich gel&ouml;scht werden?")), StrUtil.FromHTML(System.getProperty(
                "corent.djinn.infodialog.discard.title", "R&uuml;ckfrage L&ouml;schen")), 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);   
    }
    
    public boolean isDiscardConfirmed() {
        if (this.isLocked()) {
            return true;
        }
        return (JOptionPane.showConfirmDialog(this, StrUtil.FromHTML(System.getProperty(
                "corent.djinn.infodialog.discard.text", "Sollen die &Auml;nderungen am "
                + "Datensatz wirklich verworfen werden?")), StrUtil.FromHTML(System.getProperty(
                "corent.djinn.infodialog.discard.title", "R&uuml;ckfrage Verwerfen")), 
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);   
    }
    
    /**
     * @changed
     *     OLI 29.01.2009 - Hinzugef&uuml;gt.
     *     <P>
     *
     */
    public EditorDjinnMode getEditorDjinnMode() {
        return this.mode;
    }
    
    public Editable getEditable() {
        return this.panel.getEditable();
    }
    
}
