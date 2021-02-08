/*
 * DefaultTimeDialogComponentFactory.java
 *
 * 14.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import javax.swing.*;


/**
 * Diese Factory erzeugt Standard-Komponenten f&uuml;r einen TimeDialog.
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultTimeDialogComponentFactory implements TimeDialogComponentFactory{
    
    /** Eine benutzbare Standardinstanz der Factory. */
    public static final TimeDialogComponentFactory INSTANCE 
            = new DefaultTimeDialogComponentFactory(); 
    
    /* Generiert eine Factory. */
    public DefaultTimeDialogComponentFactory() {
        super();
    }
    
    
    /* Implementierung des Interfaces TimeDialogComponentFactory. */
    
    public JButton createButtonOk() {
        JButton button = new JButton("Ok");
        button.setMnemonic('O');
        return button;
    }

    public JButton createButtonAbbruch() {
        JButton button = new JButton("Abbruch");
        button.setMnemonic('A');
        return button;
    }
    
}
