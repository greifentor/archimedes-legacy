/*
 * TimeDialogComponentFactory.java
 *
 * 14.04.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.gui;


import javax.swing.*;


/**
 * Dieses Interface definiert das Verhalten einer Factory, die die f&uuml;r einen TimeDialog 
 * notwendigen Komponenten erzeugen soll.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface TimeDialogComponentFactory {
    
    /** @return Der Ok-Button des TimeDialogs. */
    public JButton createButtonOk();

    /** @return Der Abbruch-Button des TimeDialogs. */
    public JButton createButtonAbbruch();
    
}
