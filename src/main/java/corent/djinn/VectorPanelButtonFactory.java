/*
 * VectorPanelButtonFactory.java
 *
 * 30.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import javax.swing.*;


/**
 * Dieses Interface definiert die notwendigen Methoden, die eine VectorPanelButtonFactory 
 * aufweisen mu&szlig;.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface VectorPanelButtonFactory {
    
    /** Generiert einen Bearbeiten-Button f&uuml;r ein VectorPanel. */
    public JButton createButtonBearbeiten();
    
    /** Generiert einen Einf&uuml;gen-Button f&uuml;r ein VectorPanel. */
    public JButton createButtonEinfuegen();
    
    /** Generiert einen Entfernen-Button f&uuml;r ein VectorPanel. */
    public JButton createButtonEntfernen();
    
    /** Generiert einen Neuanlage-Button f&uuml;r ein VectorPanel. */
    public JButton createButtonNeuanlage();
    
    /** Generiert einen Rauf-Button f&uuml;r ein VectorPanel. */
    public JButton createButtonRauf();
    
    /** Generiert einen Runter-Button f&uuml;r ein VectorPanel. */
    public JButton createButtonRunter();
    
}
