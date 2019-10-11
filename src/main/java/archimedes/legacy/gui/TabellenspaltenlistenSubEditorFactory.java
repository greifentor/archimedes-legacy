/*
 * TabellenspaltenlistenSubEditorFactory.java
 *
 * 28.07.2004
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
 * Die Factory zum TabellenspaltenlistenSubEditor.
 *
 * @author ollie
 *
 */
 
public class TabellenspaltenlistenSubEditorFactory implements SubEditorFactory {

    /* 
     * Diese Flagge entscheidet &uuml;ber die Anzeige aller Datenfelder bzw. nur der der Tabelle
     * (zur Auswahl). 
     */
    private boolean allts = false;     
    /* Wird diese Flagge gesetzt, ist der Bearbeiten-Button abgeblendet. */
    private boolean nichtbearbeitbar = false;
    /* 
     * Die Nummer des Attributes, &uuml;ber das der zu bearbeitende Vector extrahiert werden 
     * soll. 
     */
    private int attribute = -1;
    /* Der Typ des Editors. */
    private int type = 0;
    /* Referenz auf die benutzte VectorPanelButtonFactory. */
    private VectorPanelButtonFactory vpbf = null;
    
    /**
     * Generiert eine TabellenspaltenlistenSubEditorFactory anhand der &uuml;bergebenen 
     * Parameter.
     *
     * @param vpbf Die VectorPanelButtonFactory, aus der die SubEditorFactory ihre Buttons 
     *     beziehen soll.
     * @param attribute Das Attribute (Vector) des &uuml;bergebenen Attributed, das bearbeitet 
     *     werden soll.
     * @param bearbeitbar Wird diese Flagge gesetzt, ist der Bearbeiten-Button aufgeblendet.
     */
    public TabellenspaltenlistenSubEditorFactory(VectorPanelButtonFactory vpbf, int attribute,
            boolean bearbeitbar) {
        this(vpbf, attribute, bearbeitbar, 0, false);
    }
            
    /**
     * Generiert eine TabellenspaltenlistenSubEditorFactory anhand der &uuml;bergebenen 
     * Parameter.
     *
     * @param vpbf Die VectorPanelButtonFactory, aus der die SubEditorFactory ihre Buttons 
     *     beziehen soll.
     * @param attribute Das Attribute (Vector) des &uuml;bergebenen Attributed, das bearbeitet 
     *     werden soll.
     * @param bearbeitbar Wird diese Flagge gesetzt, ist der Bearbeiten-Button aufgeblendet.
     * @param type Der Typ des zu erstellenden SubEditors.
     */
    public TabellenspaltenlistenSubEditorFactory(VectorPanelButtonFactory vpbf, int attribute,
            boolean bearbeitbar, int type) {
        this(vpbf, attribute, bearbeitbar, type, false);
    }
    
    /**
     * Generiert eine TabellenspaltenlistenSubEditorFactory anhand der &uuml;bergebenen 
     * Parameter.
     *
     * @param vpbf Die VectorPanelButtonFactory, aus der die SubEditorFactory ihre Buttons 
     *     beziehen soll.
     * @param attribute Das Attribute (Vector) des &uuml;bergebenen Attributed, das bearbeitet 
     *     werden soll.
     * @param bearbeitbar Wird diese Flagge gesetzt, ist der Bearbeiten-Button aufgeblendet.
     * @param type Der Typ des zu erstellenden SubEditors.
     * @param allts Wird diese Flagge gesetzt, so werden alle Tabellenspalten des Diagramms zur
     *     Auswahl angeboten.
     */
    public TabellenspaltenlistenSubEditorFactory(VectorPanelButtonFactory vpbf, int attribute,
            boolean bearbeitbar, int type, boolean allts) {
        super();
        this.vpbf = vpbf;
        this.attribute = attribute;
        this.nichtbearbeitbar = !bearbeitbar;
        this.type = type;
        this.allts = allts;
    }
    
    public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, 
            Component> components) {
        if (!(obj instanceof TabellenModel)) {
            throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
                    + "TabellenspaltenlistenSubEditorFactory");
        }
        if (!(obj.get(this.attribute) instanceof List<?>)) {
            throw new IllegalArgumentException(attribute + " is not a list. Cannot be edited "
                    + "in TabellenspaltenlistenSubEditorFactory");
        }
        return new TabellenspaltenlistenSubEditor(owner, (TabellenModel) obj, this.vpbf, 
                (List<Object>) obj.get(this.attribute), this.nichtbearbeitbar, this.type,
                this.allts);
    }

}
