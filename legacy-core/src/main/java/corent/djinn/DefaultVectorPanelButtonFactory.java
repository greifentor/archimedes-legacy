/*
 * DefaultVectorPanelButtonFactory.java
 *
 * 30.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;

import java.io.*;

import javax.swing.*;


/**
 * Diese Implementierung der VectorPanelButtonFactory liefert einfache Buttons.
 *
 * @author
 *     O.Lieshoff
 *
 */
 
public class DefaultVectorPanelButtonFactory implements VectorPanelButtonFactory {
    
    /** &Ouml;ffentliche Referenz zur Nutzung dieser Factory. */
    public static final DefaultVectorPanelButtonFactory INSTANCE = 
            new DefaultVectorPanelButtonFactory();
    
    private DefaultVectorPanelButtonFactory() {
        super();
    }
    
    
    /** Implementierung des Interfaces VectorPanelButtonFactory. */
    
    public JButton createButtonBearbeiten() {
        JButton jb = new JButton(Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.edit.button.text", "Bearbeiten"));
        String s = Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.edit.button.mnemonic", "B");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.edit.button.icon", 
                "iconButtonEdit.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonEinfuegen() {
        JButton jb = new JButton(Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.insert.button.text", 
                "Einf&uuml;gen"));
        String s = Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.insert.button.mnemonic", "E");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.insert.button.icon", 
                "iconButtonInsert.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonEntfernen() {
        JButton jb = new JButton(Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.remove.button.text", "Entfernen")
                );
        String s = Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.remove.button.mnemonic", "F");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.remove.button.icon", 
                "iconButtonRemove.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonNeuanlage() {
        JButton jb = new JButton(Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.create.button.text", "Neuanlage")
                );
        String s = Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.create.button.mnemonic", "N");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.create.button.icon", 
                "iconButtonCreate.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonRauf() {
        JButton jb = new JButton(Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.up.button.text", "up"));
        String s = Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.up.button.mnemonic", "U");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.up.button.icon", 
                "iconButtonUp.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonRunter() {
        JButton jb = new JButton(Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.down.button.text", "down"));
        String s = Utl.GetProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.down.button.mnemonic", "D");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultVectorPanelButtonFactory.down.button.icon", 
                "iconButtonDown.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
}    
