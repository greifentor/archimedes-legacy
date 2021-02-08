/*
 * TabellenspaltenSubEditorFactory.java
 *
 * 30.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;


import baccara.gui.*;

import java.awt.*;
import java.util.*;

import corent.base.*;
import corent.djinn.*;
import archimedes.legacy.model.*;


/**
 * Die Factory zum TabellenspaltenSubEditor.
 *
 * @author ollie
 *
 */
 
public class TabellenspaltenSubEditorFactory implements SubEditorFactory {

    private GUIBundle guiBundle = null;
    /* Referenz auf die benutzte VectorPanelButtonFactory. */
    private VectorPanelButtonFactory vpbf = null;
    
    /**
     * Generiert eine TabellenspaltenSubEditorFactory anhand der &uuml;bergebenen Parameter.
     *
     * @param vpbf Die VectorPanelButtonFactory, aus der die SubEditorFactory ihre Buttons 
     *         beziehen soll.
     * @param guiBundle A bundle with GUI information. 
     */
    public TabellenspaltenSubEditorFactory(VectorPanelButtonFactory vpbf, GUIBundle guiBundle) {
        super();
        this.guiBundle = guiBundle;
        this.vpbf = vpbf;
    }

    public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, 
            Component> components) {
        if (!(obj instanceof TabellenModel)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "TabellenspaltenSubEditorFactory");
        }
        return new TabellenspaltenSubEditor((TabellenModel) obj, this.vpbf, this.guiBundle);
    }

}