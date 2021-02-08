/*
 * PanellistenSubEditorFactory.java
 *
 * 10.07.20045
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;


import archimedes.legacy.model.*;

import corent.base.*;
import corent.djinn.*;

import java.awt.*;
import java.util.*;


/**
 * Die Factory zum PanellistenSubEditor.
 *
 * @author ollie
 *
 */
 
public class PanellistenSubEditorFactory implements SubEditorFactory {
    
    /* Referenz auf die benutzte VectorPanelButtonFactory. */
    private VectorPanelButtonFactory vpbf = null;
    
    /**
     * Generiert eine PanellistenSubEditorFactory anhand der &uuml;bergebenen Parameter.
     *
     * @param vpbf Die VectorPanelButtonFactory, aus der die SubEditorFactory ihre Buttons 
     *     beziehen soll.
     */
    public PanellistenSubEditorFactory(VectorPanelButtonFactory vpbf) {
        super();
        this.vpbf = vpbf;
    }
    
    public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, 
            Component> components) {
        if (!(obj instanceof TabellenModel)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "PanellistenSubEditorFactory");
        }
        return new PanellistenSubEditor((TabellenModel) obj, this.vpbf);
    }

}
