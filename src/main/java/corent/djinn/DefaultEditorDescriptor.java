/*
 * DefaultEditorDescriptor.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.lang.ref.*;
import javax.swing.*;

import corent.base.*;


/**
 * Hierbei handelt es sich um eine Standardimplementierung f&uuml;r einen einfachen 
 * EditorDescriptor. Er liefert drei Arten von Komponenten: 1) Checkboxen f&uuml;r 
 * Wahrheitswerte, 2) Textfelder f&uuml;r Zahlen und Strings und 3) abgeblendete Textfelder 
 * f&uuml;r den Rest (die Anzeige wird via <TT>toString()</TT> erzeugt.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 * @changed
 *     OLI 09.01.2009 - Erweiterung um die Implementierung der Methode 
 *             <TT>setObject(Attributed)</TT>.
 *     <P>
 *
 */
 
public class DefaultEditorDescriptor implements EditorDescriptor {
    
    /* 
     * Diese Flagge ist zu setzen, wenn das Attribut zum EditorDescriptor im Stapel 
     * ge&auml;ndert werden k&ouml;nnen soll.
     */
    private boolean alterInBatch = false;
    /* Diese Flagge mu&szlig; gesetzt werden, wenn die Komponente abgeblendet werden soll. */
    private boolean disabled = false;
    /* 
     * Diese Flagge ist zu setzen, wenn das durch den Descriptor beschriebene Feld ein 
     * Pflichtfeld ist.
     */
    private boolean obligation = false;
    /* Ein Zeichen f&uuml;r das Mnemonic. */
    private char mnemonic = '\0';
    /* Eine Referenz auf die relevante ComponentFactory. */
    private ComponentFactory componentFactory = null;
    /* Ein Icon zum Label des Descriptors. */
    private Icon icon = null;
    /* Hier wird die Id des zu manipulierenden Attributs gespeichert. */
    private int attributeId = -1;
    /* 
     * Hier kann eine maximale Feldl&auml;nge f&uuml;r den EditorDescriptor vereinbart werden. 
     */
    private int maxSize = 65535;
    /* Das Tab des EditorDjinns, auf dem die Eingabekomponente abgebildet werden soll. */
    private int tab = 0;
    /* 
     * Referenz auf die LabelFactory, &uuml;ber die der Descriptor seinen Label beziehen soll. 
     */
    private LabelFactory labelFactory = null;
    /* Ein String als Inhalt f&uuml;r den Label. */
    private String labeltext = null;
    /* Der Name des Descriptors. */
    private String name = "";
    /* Ein ToolTipText zum Label und zur Komponente. */
    private String toolTipText = null;
    /* Eine WeakReference auf das zu manipulierende Attribut. */
    private WeakReference attributed = null;
    
    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     */
    public DefaultEditorDescriptor(Attributed attr, int attrId, LabelFactory labelFactory, 
            ComponentFactory componentFactory) {
        this(0, attr, attrId, labelFactory, componentFactory, null, '\0', null, null);
    }
    
    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     * @param labeltext Ein Text zum Label der anzuzeigenden Komponente.
     * @param mnemonic Ein Mnemonic zum Label. 
     */
    public DefaultEditorDescriptor(Attributed attr, int attrId, LabelFactory labelFactory, 
            ComponentFactory componentFactory, String labeltext, char mnemonic) {
        this(0, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, null, null);
    }

    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
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
    public DefaultEditorDescriptor(Attributed attr, int attrId, LabelFactory labelFactory, 
            ComponentFactory componentFactory, String labeltext, char mnemonic, Icon icon) {
        this(0, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon, null);
    }

    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
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
    public DefaultEditorDescriptor(Attributed attr, int attrId, LabelFactory labelFactory, 
            ComponentFactory componentFactory, String labeltext, char mnemonic, Icon icon, 
            String toolTipText) {
        this(0, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon, 
                toolTipText);
    }
    
    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param tab Das Tab, auf dem die Komponente abgebildet werden soll. 
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param attrId Die Id des zu manipulierenden Attributes von attr.
     * @param labelFactory Die LabelFactory, &uuml;ber die sich der Descriptor mit Labels 
     *     versorgen soll. 
     * @param componentFactory Die ComponentFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten zur Manipulation des zu manipulierenden Objektes eindecken soll.
     */
    public DefaultEditorDescriptor(int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory) {
        this(tab, attr, attrId, labelFactory, componentFactory, null, '\0', null, null, false);
    }
    
    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
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
    public DefaultEditorDescriptor(int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic) {
        this(tab, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, null, null,
                false);
    }

    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
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
    public DefaultEditorDescriptor(int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic, Icon icon) {
        this(tab, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon, null,
                false);
    }

    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
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
    public DefaultEditorDescriptor(int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic, Icon icon, String toolTipText) {
        this(tab, attr, attrId, labelFactory, componentFactory, labeltext, mnemonic, icon, 
                toolTipText, false);
    }
    
    /** 
     * Generiert einen DefaultEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
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
    public DefaultEditorDescriptor(int tab, Attributed attr, int attrId, 
            LabelFactory labelFactory, ComponentFactory componentFactory, String labeltext, 
            char mnemonic, Icon icon, String toolTipText, boolean disabled) {
        super();
        this.attributed = new WeakReference(attr);
        this.attributeId = attrId;
        this.componentFactory = componentFactory;
        this.disabled = disabled;
        this.icon = icon;
        this.labelFactory = labelFactory;
        this.labeltext = labeltext;
        this.mnemonic = mnemonic;
        this.tab = tab;
        this.toolTipText = toolTipText;
    }

    
    /* Accessoren und Mutatoren. */
    
    /**
     * Accessor f&uuml;r die Eigenschaft Disabled
     *
     * @return Der Wert der Eigenschaft Disabled
     */
    public boolean isDisabled() {
        return this.disabled;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Disabled
     *
     * @param disabled Der neue Wert f&uuml;r die Eigenschaft Disabled.
     */
    public void setDisabled(boolean disabled) {
        this.disabled = disabled;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Mnemonic
     *
     * @return Der Wert der Eigenschaft Mnemonic
     */
    public char getMnemonic() {
        return this.mnemonic;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Mnemonic
     *
     * @param mnemonic Der neue Wert f&uuml;r die Eigenschaft Mnemonic.
     */
    public void setMnemonic(char mnemonic) {
        this.mnemonic = mnemonic;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Icon
     *
     * @return Der Wert der Eigenschaft Icon
     */
    public Icon getIcon() {
        return this.icon;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Icon
     *
     * @param icon Der neue Wert f&uuml;r die Eigenschaft Icon.
     */
    public void setIcon(Icon icon) {
        this.icon = icon;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Tab
     *
     * @return Der Wert der Eigenschaft Tab
     */
    public int getTab() {
        return this.tab;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Tab
     *
     * @param tab Der neue Wert f&uuml;r die Eigenschaft Tab.
     */
    public void setTab(int tab) {
        this.tab = tab;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft Labeltext
     *
     * @return Der Wert der Eigenschaft Labeltext
     */
    public String getLabeltext() {
        return this.labeltext;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Labeltext
     *
     * @param labeltext Der neue Wert f&uuml;r die Eigenschaft Labeltext.
     */
    public void setLabeltext(String labeltext) {
        this.labeltext = labeltext;
    }

    /**
     * Accessor f&uuml;r die Eigenschaft ToolTipText
     *
     * @return Der Wert der Eigenschaft ToolTipText
     */
    public String getToolTipText() {
        return this.toolTipText;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft ToolTipText
     *
     * @param toolTipText Der neue Wert f&uuml;r die Eigenschaft ToolTipText.
     */
    public void setToolTipText(String toolTipText) {
        this.toolTipText = toolTipText;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft AttributeId.
     *
     * @param id Setzt die &uuml;bergebene ID als neuen wert f&uuml;r die Eigenschaft 
     *     AttributeId.
     */
    public void setAttributeId(int id) {
        this.attributeId = id;
    }
    
    /**
     * Setzt die ComponentFactory auf den angegebenen Wert.
     *
     * @param cf Die neue ComponentFactory zum DefaultEditorDescriptor.
     */
    public void setComponentFactory(ComponentFactory cf) {
        this.componentFactory = cf;
    }
    

    /* Accessoren und Mutatoren. */
    
    /**
     * Accessor f&uuml;r die Eigenschaft Name
     *
     * @return Der Wert der Eigenschaft Name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Mutator f&uuml;r die Eigenschaft Name
     *
     * @param name Der neue Wert f&uuml;r die Eigenschaft Name.
     */
    public void setName(String name) {
        this.name = name;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft Obligation
     *
     * @param obligation Der neue Wert f&uuml;r die Eigenschaft Obligation.
     */
    public void setObligation(boolean obligation) {
        this.obligation = obligation;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft MaxSize.
     *
     * @param m Der neue Wert f&uuml;r die Eigenschaft MaxSize.
     */
    public void setMaxSize(int m) {
        this.maxSize = m;
    }
    
    /**
     * Mutator f&uuml;r die Eigenschaft AlterInBatch.
     *
     * @param aib Der neue Wert f&uuml;r die Eigenschaft AlterInBatch.
     */
    public void setAlterInBatch(boolean aib) {
        this.alterInBatch = aib;
    }

    /* Ueberschreiben von Methoden der Superklasse. */
    
    public String toString() {
        return "DefaultEditorDescriptor(attributedId=" + this.getAttributeId() + ", disabled="
                + this.isDisabled() + ", labeltext=" + this.getLabeltext() + ", mnemonic=" 
                + this.getMnemonic() + ", tab=" + this.getTab() + ", toolTipText=" 
                + this.getToolTipText() + ", name=" + this.getName() + ", obligation=" 
                + this.isObligation() + ")"; 
    }

    
    /* Implementierung des interfaces EditorDescriptor. */
    
    public ComponentFactory getComponentFactory() {
        return this.componentFactory;
    }
    
    public int getAttributeId() {
        return this.attributeId;
    }

    public LabelFactory getLabelFactory() {
        return this.labelFactory;
    }
    
    public int getMaxSize() {
        return this.maxSize;
    }

    public Attributed getObject() {
        return (Attributed) this.attributed.get();
    }
    
    public boolean isAlterInBatch() {
        return this.alterInBatch;
    }
    
    public boolean isObligation() {
        return this.obligation;
    }
    
    public void setObject(Attributed attr) {
        this.attributed = new WeakReference(attr);
    }
    
}
