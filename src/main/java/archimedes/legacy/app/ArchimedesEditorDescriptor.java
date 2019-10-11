/*
 * ArchimedesEditorDescriptor.java
 *
 * 13.07.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.base.*;
import corent.djinn.*;

import javax.swing.*;


/**
 * Dieser EditorDescriptor erweitert den EditorDescriptor des corent.djinn-Packages um einen 
 * Namen. Diese F&auml;higkeit ist von einer gewissen Wichtigkeit innerhalb des 
 * Archimedes-Application-Frameworks.
 *
 * @author ollie
 *
 */
 
public class ArchimedesEditorDescriptor extends DefaultEditorDescriptor {
    
    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     */
    public ArchimedesEditorDescriptor(String name, Attributed attr, int attrId, LabelFactory 
            labelFactory, ComponentFactory componentFactory) {
        this(name, 0, attr, attrId, labelFactory, componentFactory, null, '\0', null, null);
    }
    
    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     * @param labeltext Ein Text zum Label der anzuzeigenden Komponente.
     * @param mnemonic Ein Mnemonic zum Label. 
     */
    public ArchimedesEditorDescriptor(String name, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic) {
        this(name, 0, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, null, 
                null);
    }

    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     * @param labeltext Ein Text zum Label der anzuzeigenden Komponente.
     * @param mnemonic Ein Mnemonic zum Label. 
     * @param icon Ein ImageIcon zur Anzeige im Label.
     */
    public ArchimedesEditorDescriptor(String name, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic, Icon icon) {
        this(name, 0, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon, 
                null);
    }

    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     * @param labeltext Ein Text zum Label der anzuzeigenden Komponente.
     * @param mnemonic Ein Mnemonic zum Label. 
     * @param icon Ein ImageIcon zur Anzeige im Label.
     * @param toolTipText Ein ToolTipText zur Anzeige bei Mauskontakt mit dem Label und der 
     *     Komponente.
     */
    public ArchimedesEditorDescriptor(String name, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic, Icon icon, String toolTipText) {
        this(name, 0, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon, 
                toolTipText);
    }
    
    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param tab Das Tab, auf dem die Komponente abgebildet werden soll. 
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     */
    public ArchimedesEditorDescriptor(String name, int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory) {
        this(name, tab, attr, attrId, labelFactory, componentFactory, null, '\0', null, null, 
                false);
    }
    
    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param tab Das Tab, auf dem die Komponente abgebildet werden soll. 
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     * @param labeltext Ein Text zum Label der anzuzeigenden Komponente.
     * @param mnemonic Ein Mnemonic zum Label. 
     */
    public ArchimedesEditorDescriptor(String name, int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic) {
        this(name, tab, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, null,
                null, false);
    }

    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param tab Das Tab, auf dem die Komponente abgebildet werden soll. 
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     * @param labeltext Ein Text zum Label der anzuzeigenden Komponente.
     * @param mnemonic Ein Mnemonic zum Label. 
     * @param icon Ein ImageIcon zur Anzeige im Label.
     */
    public ArchimedesEditorDescriptor(String name, int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic, Icon icon) {
        this(name, tab, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon,
                null, false);
    }

    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param tab Das Tab, auf dem die Komponente abgebildet werden soll. 
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     * @param labeltext Ein Text zum Label der anzuzeigenden Komponente.
     * @param mnemonic Ein Mnemonic zum Label. 
     * @param icon Ein ImageIcon zur Anzeige im Label.
     * @param toolTipText Ein ToolTipText zur Anzeige bei Mauskontakt mit dem Label und der 
     *     Komponente.
     */
    public ArchimedesEditorDescriptor(String name, int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic, Icon icon, String toolTipText) {
        this(name, tab, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon, 
                toolTipText, false);
    }
    
    /** 
     * Generiert einen ArchimedesEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des Descriptors.
     * @param tab Das Tab, auf dem die Komponente abgebildet werden soll. 
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     * @param labeltext Ein Text zum Label der anzuzeigenden Komponente.
     * @param mnemonic Ein Mnemonic zum Label. 
     * @param icon Ein ImageIcon zur Anzeige im Label.
     * @param toolTipText Ein ToolTipText zur Anzeige bei Mauskontakt mit dem Label und der 
     *     Komponente.
     * @param disabled Wird diese Flagge gesetzt, so wird die Komponente abgeblendet 
     *     dargestellt.
     */
    public ArchimedesEditorDescriptor(String name, int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic, Icon icon, String toolTipText, boolean disabled) {
        super(tab, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon, 
                toolTipText, disabled);
        this.setName(name);
    }

}
