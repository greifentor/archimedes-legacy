/*
 * DefaultSelectionDjinnButtonFactory.java
 *
 * 05.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;
import corent.gui.*;

import java.io.*;

import javax.swing.*;


/** 
 * Diese Standardimplementierung des SelectionDjinnButtonFactory-Interfaces arbeitet mit 
 * herk&ouml;mmlichen JButtons des javax.swing-Packages. Sie kann als Vorlage f&uuml;r 
 * Anwendungen mit anderen Button genutzt werden.
 * <P>Die Properties <I>corent.djinn.DefaultSelectionDjinnButtonFactory.cancel.button.text</I>,
 * <I>corent.djinn.DefaultSelectionDjinnButtonFactory.cancel.button.mnemonic</I> und
 * <I>corent.djinn.DefaultSelectionDjinnButtonFactory.cancel.button.icon</I> bzw. 
 * <I>corent.djinn.DefaultSelectionDjinnButtonFactory.select.button.text</I>,
 * <I>corent.djinn.DefaultSelectionDjinnButtonFactory.select.button.mnemonic</I> und
 * <I>corent.djinn.DefaultSelectionDjinnButtonFactory.select.button.icon</I> erlauben eine 
 * nachtr&auml;gliche Konfiguration des Buttontextes, des Mnemonics zum Button und des Icons,
 * das auf dem Button abgebildet werden soll. In der Icon-Property darf nur der eigentliche 
 * Dateiname der Datei stehen, aus dem das Icon geladen werden soll. Der Pfad wird aus der 
 * Property <I>corent.djinn.imagepath</I> gelesen. Ist diese Property nicht gesetzt, werden 
 * alternative Icons gar nicht akzeptiert.<BR>
 * Die Defaulteinstellungen sind: cancel.button(text=Abbruch, mnemonic=B, 
 * icon=iconButtonDiscard.gif) und select.button(text=Auswahl, mnemonic=A, 
 * icon=iconButtonSelect.gif).
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 30.06.2008 - Korrektur der Property-Bezeichner f&uuml;r die Beschriftungstexte.
 *     <P> 
 *
 */
 
public class DefaultSelectionDjinnButtonFactory implements SelectionDjinnButtonFactory {
    
    /** Generiert die Factory mit Defaultwerten. */
    public DefaultSelectionDjinnButtonFactory() {
        super();
    }
    

    /* Implementierung des Interface SelectionDjinnPanelButtonFactory. */
    
    public JButton createButtonAbbruch() {
        JButton jb = new COButton("Abbruch", "ButtonCancel");
        jb.setMnemonic('B');
        String s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionDjinnButtonFactory.cancel.button.text", "");
        if (s.length() > 0) {
            jb.setText(s);
        }
        s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionDjinnButtonFactory.cancel.button.mnemonic", "");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultSelectionDjinnButtonFactory.cancel.button.icon", 
                "iconButtonDiscard.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
        
    public JButton createButtonAuswahl() {
        JButton jb = new COButton("Auswahl", "ButtonSelect");
        jb.setMnemonic('A');
        String s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionDjinnButtonFactory.select.button.text", "");
        if (s.length() > 0) {
            jb.setText(s);
        }
        s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionDjinnButtonFactory.select.button.mnemonic", "");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultSelectionDjinnButtonFactory.select.button.icon", 
                "iconButtonSelect.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }

    public JButton createButtonDuplizieren() {
        return null;
    }
    
    public JButton createButtonNeuanlage() {
        return null;
    }

}  
