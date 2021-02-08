/*
 * FrameSelectionDjinn.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;

import corent.files.*;
import corent.gui.*;


/**
 * Diese Implementierung des SelectionDjinns spielt sich in einem Frame ab. F&uuml;r 
 * Anwendungen, die nur zum Manipulieren eines einzelnen Datensatzes erstellt werden sollen, ist
 * diese Variante die erste Wahl. Alternativ kann er auch genutzt werden, um 
 * InternalFrame-Applikationen zu umschiffen.
 * <P>Der Frame arbeitet eng mit dem SelectionDjinn auf Panel-Basis zusammen, in dem die 
 * eigentliche Arbeit getan wird.
 *
 * @author O.Lieshoff
 *
 */

public class FrameSelectionDjinn extends JFrameWithInifile {
    
    /** 
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param liste Die Liste, aus der die Auswahl getroffen werden soll.
     * / 
    public FrameSelectionDjinn(String titel, Vector liste) {
        this(titel, liste, true, null);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param liste Die Liste, aus der die Auswahl getroffen werden soll. 
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *     von Suchkriterien eingeblendet werden soll.
     * / 
    public FrameSelectionDjinn(String titel, Vector liste, boolean selector) {
        this(titel, liste, selector, null);
    }
    */
    
    /** 
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param liste Die Liste, aus der die Auswahl getroffen werden soll.
     * @param ini Die IniDatei, aus der der FrameSelectionDjinn seine Gestalt rekonstruieren
     *     soll.
     */ 
    public FrameSelectionDjinn(String titel, Vector liste, Inifile ini) {
        this(titel, liste, true, ini);
    }
    
    /** 
     * Generiert einen SelectionDjinn auf Framebasis anhand der &uuml;bergebenen Parameter.
     *
     * @param titel Der Titel des Frames.
     * @param liste Die Liste, aus der die Auswahl getroffen werden soll. 
     * @param selector Diese Flagge mu&szlig; gesetzt werden, wenn die Eingabezeile zur Vorgabe 
     *     von Suchkriterien eingeblendet werden soll.
     * @param ini Die IniDatei, aus der der FrameSelectionDjinn seine Gestalt rekonstruieren
     *     soll.
     */ 
    public FrameSelectionDjinn(String titel, Vector liste, boolean selector, Inifile ini) {
        super(titel, ini);
        DefaultSelectionDjinnPanel panel = new DefaultSelectionDjinnPanel(
                new DefaultListViewComponentFactory(liste), 
                new DefaultSelectionDjinnButtonFactory(), selector) {
            public void doButtonAuswahl() {
                fireDjinnClosing();
                super.doButtonAuswahl();
                fireDjinnClosed();
            }
        };
        panel.addSelectionDjinnListener(new SelectionDjinnAdapter() {
            public void selectionDone(Vector selected) {
                doSelected(selected);          
            }
            public void djinnClosed() {
                dispose();
                setVisible(false);
            }
        });
        this.setContentPane(panel);
        this.pack();
        this.setVisible(true);
    }
    
    /** 
     * Diese Methode wird aufgerufen, wenn &uuml;ber den Frame eine Objekt-Auswahl stattgefunden
     * hat.
     *
     * @param values Die ausgew&auml;hlten Objekte.
     */
    public void doSelected(Vector values) {
    }
        
}
