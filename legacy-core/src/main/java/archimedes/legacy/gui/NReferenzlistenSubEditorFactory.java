/*
 * NReferenzlistenSubEditorFactory.java
 *
 * 28.10.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;


import java.awt.*;
import java.util.*;
import java.util.List;

import corent.base.*;
import corent.djinn.*;
import archimedes.legacy.model.*;


/**
 * Die Factory zum NReferenzenlistenSubEditor.
 *
 * @author ollie
 *
 */
 
public class NReferenzlistenSubEditorFactory implements SubEditorFactory {

    /* Wird diese Flagge gesetzt, ist der Bearbeiten-Button abgeblendet. */
    private boolean nichtbearbeitbar = false;
    /* 
     * Die Nummer des Attributes, &uuml;ber das der zu bearbeitende Vector extrahiert werden 
     * soll. 
     */
    private int attribute = -1;
    /* Referenz auf die benutzte VectorPanelButtonFactory. */
    private VectorPanelButtonFactory vpbf = null;
    
    /**
     * Generiert eine NReferenzlistenSubEditorFactory anhand der &uuml;bergebenen Parameter.
     *
     * @param vpbf Die VectorPanelButtonFactory, aus der die SubEditorFactory ihre Buttons 
     *     beziehen soll.
     * @param attribute Das Attribute (Vector) des &uuml;bergebenen Attributed, das bearbeitet 
     *     werden soll.
     * @param bearbeitbar Wird diese Flagge gesetzt, ist der Bearbeiten-Button aufgeblendet.
     */
    public NReferenzlistenSubEditorFactory(VectorPanelButtonFactory vpbf, int attribute,
            boolean bearbeitbar) {
        super();
        this.vpbf = vpbf;
        this.attribute = attribute;
        this.nichtbearbeitbar = !bearbeitbar;
    }
    
    public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, 
            Component> components) {
        if (!(obj instanceof TabellenModel)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "NReferenzlistenSubEditorFactory");
        }
        if (!(obj.get(this.attribute) instanceof List<?>)) {
            throw new IllegalArgumentException(attribute + " is not a list. Cannot be edited "
                    + "in NReferenzlistenSubEditorFactory");
        }
        return new NReferenzenlistenSubEditor(owner, (TabellenModel) obj, this.vpbf, 
                (List<Object>) obj.get(this.attribute), this.nichtbearbeitbar);
    }

}
