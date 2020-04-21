/*
 * DefaultEditorDjinnButtonFactory.java
 *
 * 10.01.2004
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
 * Diese Standardimplementierung des EditorDjinnButtonFactory-Interfaces arbeitet mit 
 * herk&ouml;mmlichen JButtons des javax.swing-Packages. Sie kann als Vorlage f&uuml;r 
 * Anwendungen mit anderen Button genutzt werden.
 *
 * @author
 *     O.Lieshoff
 *
 * @changed
 *     OLI 29.10.2007 - Anpassung an die Erweiterungen des Interfaces
 *             <TT>EditorDjinnButtonFactory</TT>.<BR>
 *
 */
 
public class DefaultEditorDjinnButtonFactory implements EditorDjinnButtonFactory {
    
    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn der Druckenbutton aktiviert werden soll.
     */
    private boolean druckenAktiviert = true;
    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn der Historienbutton aktiviert werden soll.
     */
    private boolean historieAktiviert = true;
    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn der L&ouml;schenbutton aktiviert werden soll.
     */
    private boolean loeschenAktiviert = true;
    
    /** Generiert die Factory mit Defaultwerten. */
    public DefaultEditorDjinnButtonFactory() {
        this(true, false, false);
    }

    /** 
     * Generiert die Factory mit Defaultwerten.
     *
     * @param loeschenAktiviert Wird diese Flagge gesetzt &uuml;bergeben, so wird der 
     *     L&ouml;schenbutton aktiviert. 
     */
    public DefaultEditorDjinnButtonFactory(boolean loeschenAktiviert) {
        this(loeschenAktiviert, false, false);
    }
    
    /** 
     * Generiert die Factory mit Defaultwerten.
     *
     * @param loeschenAktiviert Wird diese Flagge gesetzt &uuml;bergeben, so wird der 
     *     L&ouml;schenbutton aktiviert. 
     * @param druckenAktiviert Wird diese Flagge gesetzt &uuml;bergeben, so wird der 
     *     Druckenbutton aktiviert. 
     * @param historieAktiviert Wird diese Flagge gesetzt &uuml;bergeben, so wird der 
     *     Historienbutton aktiviert. 
     */
    public DefaultEditorDjinnButtonFactory(boolean loeschenAktiviert, boolean druckenAktiviert,
            boolean historieAktiviert) {
        super();
        this.druckenAktiviert = druckenAktiviert;
        this.historieAktiviert = historieAktiviert;
        this.loeschenAktiviert = loeschenAktiviert;
    }


    /* Implementierung des Interfaces EditorDjinnButtonFactory. */    
    
    public JButton createButtonDrucken() {
        if (!this.druckenAktiviert) {
            return null;
        }
        JButton jb = new COButton(Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.print.button.text", "Drucken"),
                "button.print");
        String s = System.getProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.print.button.mnemonic", "D");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.print.button.icon", 
                "iconButtonPrint.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonHistorie() {
        if (!this.historieAktiviert) {
            return null;
        }
        JButton jb = new COButton(Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.history.button.text", "Historie")
                , "button.history");
        String s = Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.history.button.mnemonic", "H");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.history.button.icon", 
                "iconButtonHistory.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonLoeschen() {
        if (!this.loeschenAktiviert) {
            return null;
        }
        JButton jb = new COButton(Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.delete.button.text", 
                "L&ouml;schen"), "button.delete");
        String s = Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.delete.button.mnemonic", "L");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.delete.button.icon", 
                "iconButtonDelete.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonSpeichern() {
        JButton jb = new COButton(Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.save.button.text", "Speichern"), 
                "button.save");
        String s = Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.save.button.mnemonic", "P");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.save.button.icon", 
                "iconButtonSave.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonUebernehmen() {
        JButton jb = new COButton(Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.accept.button.text", 
                "&Uuml;bernehmen"), "button.accept");
        String s = Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.accept.button.mnemonic", "N");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.accept.button.icon", 
                "iconButtonAccept.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
    public JButton createButtonVerwerfen(){
        JButton jb = new COButton(Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.discard.button.text", "Verwerfen"
                ), "button.cancel");
        String s = Utl.GetProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.discard.button.mnemonic", "V");
        if (s.length() > 0) {
            jb.setMnemonic(s.charAt(0));
        }
        String path = System.getProperty("corent.djinn.imagepath", null);
        String fn = (path == null ? "" : path + File.separator) + System.getProperty(
                "corent.djinn.DefaultEditorDjinnButtonFactory.discard.button.icon", 
                "iconButtonDiscard.gif");
        if (new File(fn).exists()) {
           jb.setIcon(new ImageIcon(fn));
        }
        return jb;
    }
    
}  
