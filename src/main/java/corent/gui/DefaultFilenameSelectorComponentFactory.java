/*
 * DefaultFilenameSelectorComponentFactory.java
 *
 * 07.07.2008
 *
 * (c) O.Lieshoff
 *
 */
 
package corent.gui;


import corent.base.*;

import java.awt.*;

import javax.swing.*;


/**
 * Eine Musterimplementierung f&uuml;r die FilenameSelectorComponentFactory.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 07.07.2008 - Hinzugef&uuml;gt.
 *     <P>OLI 16.07.2008 - Erweiterung um den Clear-Button.
 *     <P>
 *
 */

public class DefaultFilenameSelectorComponentFactory implements FilenameSelectorComponentFactory
        { 
            
    private static PropertyRessourceManager prm = new PropertyRessourceManager();

    // REQUEST OLI 04.06.2010 - Koennen die Felder wirklich entfernt werden ?!?
    /* Referenz auf den Owner zum TextEditor. */
    // private Frame owner = null;
    /* Ein Text f&uuml;r den Label. */
    // private String labeltext = "";

    /** 
     * Generiert eine neue DefaultFilenameSelectorComponentFactory anhand der &uuml;bergebenen 
     * Parameter.
     *
     * @param labeltext Ein Labeltext zum LineTextEditor.
     */
    public DefaultFilenameSelectorComponentFactory(String labeltext) {
        super();
        // this.labeltext = labeltext;
    }
    
    /** 
     * Setzt die angegebene Komponente als neue Owner-Referenz ein.
     *
     * @param owner Die Komponente, die als neue Owner-Referenz zum LineTextEditor eingesetzt
     *     werden soll.
     */
    public void setModalParent(Frame owner) {
        // this.owner = owner;
    }
    
    
    /* Implementierung des Interfaces LineTextEditorComponentFactory. */
    
    public JButton createClearButton(FilenameSelector owner) {
        COButton b = new COButton(StrUtil.FromHTML("C"),
                "corent.djinn.DefaultFilenameSelectorComponentFactory.button.clear.text");
        b.update(DefaultFilenameSelectorComponentFactory.prm);
        return b;
    }
    
    public JButton createSelectButton(FilenameSelector owner) {
        COButton b = new COButton(StrUtil.FromHTML("&Auml;ndern"),
                "corent.djinn.DefaultFilenameSelectorComponentFactory.button.edit.text");
        b.update(DefaultFilenameSelectorComponentFactory.prm);
        return b;
    }
    
    public JTextField createTextField(FilenameSelector owner) {
        return new JTextField(50);
    }
    
}
