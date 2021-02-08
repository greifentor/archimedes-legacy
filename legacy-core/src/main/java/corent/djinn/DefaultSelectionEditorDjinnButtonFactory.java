/*
 * DefaultSelectionEditorDjinnButtonFactory.java
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
 * Diese Standardimplementierung des SelectionEditorDjinnButtonFactory-Interfaces arbeitet mit 
 * herk&ouml;mmlichen JButtons des javax.swing-Packages. Sie kann als Vorlage f&uuml;r 
 * Anwendungen mit anderen Button genutzt werden.
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultSelectionEditorDjinnButtonFactory implements SelectionDjinnButtonFactory {
    
    /** Generiert die Factory mit Defaultwerten. */
    public DefaultSelectionEditorDjinnButtonFactory() {
        super();
    }
    

    /* Implementierung des Interface SelectionDjinnPanelButtonFactory. */
    
    public JButton createButtonAbbruch() {
        JButton jb = new COButton("Abbruch", "button.cancel");
        jb.setMnemonic('B');
        String s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.cancel.button.text", 
                "");
        if (s.length() > 0) {
            jb.setText(s);
        }
        s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.cancel.button.mnemonic", 
                "");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.cancel.button.icon", 
                "iconButtonExit.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
        
    public JButton createButtonAuswahl() {
        JButton jb = new COButton("Auswahl", "button.select");
        jb.setMnemonic('A');
        String s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.select.button.text", 
                "");
        if (s.length() > 0) {
            jb.setText(s);
        }
        s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.select.button.mnemonic", 
                "");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.select.button.icon", 
                "iconButtonSelect.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }

    public JButton createButtonDuplizieren() {
        JButton jb = new COButton("Duplizieren", "button.duplicate");
        jb.setMnemonic('D');
        String s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.duplicate.button.text", 
                "");
        if (s.length() > 0) {
            jb.setText(s);
        }
        s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.duplicate.button."
                + "mnemonic", "");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.duplicate.button.icon", 
                "iconButtonDuplicate.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonNeuanlage() {
        JButton jb = new COButton("Neuanlage", "button.create");
        jb.setMnemonic('N');
        String s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.create.button.text", 
                "");
        if (s.length() > 0) {
            jb.setText(s);
        }
        s = Utl.GetProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.create.button.mnemonic", 
                "");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultSelectionEditorDjinnButtonFactory.create.button.icon", 
                "iconButtonCreate.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }

}  
