/*
 * SelectionDjinnButtonFactory.java
 *
 * 05.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.*;


/**
 * Dieses Interface definiert das Verhalten einer ButtonFactory, die die notwendigen Buttons
 * f&uuml;r einen SelectionDjinn produzieren kann.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface SelectionDjinnButtonFactory {
    
    /** @return Generiert einen Abbruch-Button. */
    public JButton createButtonAbbruch();
        
    /** @return Generiert einen Auswahl-Button. */
    public JButton createButtonAuswahl();

    /** @return Generiert einen Duplizieren-Button. */
    public JButton createButtonDuplizieren();
    
    /** @return Generiert einen Neuanlage-Button. */
    public JButton createButtonNeuanlage();

}
