/*
 * StereotypenSubEditorFactory.java
 *
 * 30.04.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;


import java.awt.*;
import java.util.*;

import corent.base.*;
import corent.djinn.*;

import archimedes.legacy.model.*;


/**
 * Die Factory zum StereotypenSubEditor.
 *
 * @author ollie
 *
 */
 
public class StereotypenSubEditorFactory implements SubEditorFactory {
    
    /* Referenz auf die benutzte VectorPanelButtonFactory. */
    private VectorPanelButtonFactory vpbf = null;
    
    /**
     * Generiert eine StereotypenSubEditorFactory anhand der &uuml;bergebenen Parameter.
     *
     * @param vpbf Die VectorPanelButtonFactory, aus der die SubEditorFactory ihre Buttons 
     *     beziehen soll.
     */
    public StereotypenSubEditorFactory(VectorPanelButtonFactory vpbf) {
        super();
        this.vpbf = vpbf;
    }
    
    public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, 
            Component> components) {
        if (!(obj instanceof TabellenModel)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "StereotypenSubEditorFactory");
        }
        return new StereotypenSubEditor(owner, (TabellenModel) obj, this.vpbf);
    }

}
