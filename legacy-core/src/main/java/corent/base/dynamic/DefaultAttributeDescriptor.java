/*
 * DefaultAttributeDescriptor.java
 *
 * 05.04.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base.dynamic;


import java.io.*;


/**
 * Diese Musterimplementierung des AttributeDescriptors bietet den durch das Interface 
 * geforderte Mindestma&szlig; an Accessoren plus den dazugeh&ouml;rigen Mutatoren und einem 
 * geeigneten Konstruktor.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class DefaultAttributeDescriptor implements AttributeDescriptor, Serializable {

    /* 
     * Diese Flagge mu&szlig; gesetzt werden, wenn es sich bei dem Attribut um eine Referenz 
     * handelt. 
     */
    private boolean reference = false;
    /* Die Klasse des beschriebenen Attributes. */
    private Class cls = null;
    /* Der Initialwert zum beschriebenen Attribut. */
    private Object initialvalue = null;
    /* Der Name des beschriebenen Attributes. */
    private String name = null;
    
    /** 
     * Erzeugt einen neuen DefaultAttributeDescriptor mit <TT>null</TT>-Werten. Dieser mu&szlig; 
     * vor Gebrauch geeignet bef&uuml;llt werden.
     */
    public DefaultAttributeDescriptor() {
        super();
    }
   
    /** 
     * Erzeugt einen neuen DefaultAttributeDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param name Der Name des durch den Descriptor beschriebenen Attributes. 
     * @param cls Die Klasse des durch den Descriptor beschriebenen Attributes. 
     * @param initial Der Initialwert zum durch den Descriptor beschriebenen Attribut.
     * @param reference Diese Flagge mu&szlig; gesetzt werden, wenn das beschriebene Attribut
     *     eine Referenz ist.
     */
    public DefaultAttributeDescriptor(String name, Class cls, Object initial, boolean reference
            ) {
        super();
        this.setAttributeClass(cls);
        this.setAttributeName(name);
        this.setAttributeInitialValue(initial);
        this.setReference(reference);
    }
    
    /**
     * Setzt die &uuml;bergebene Klasse als neue Klasse zum Attribut ein.
     *
     * @param cls Die neue Klasse zum Attribut.
     */
    public void setAttributeClass(Class cls) {
        this.cls = cls;
    }
   
    /**
     * Setzt den &uuml;bergebenen Initialwert als neuen Initialwert des Attributes ein.
     *
     * @param initial Der neue Initialwert des Attributes.
     */
    public void setAttributeInitialValue(Object initial) {
        this.initialvalue = initial;
    }   
    
    /**
     * Setzt den &uuml;bergebenen Namen als neuen Namen des Attributes ein.
     *
     * @param name Der neue Name des Attributes.
     */
    public void setAttributeName(String name) {
        this.name = name;
    }
    
    /**
     * Setzt bzw. l&ouml;scht die Referenz-Flagge des Attributes.
     *
     * @param ref Der neue Wert der Flagge.
     */
    public void setReference(boolean ref) {
        this.reference = ref;
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public String toString() {
        return new StringBuffer("DefaultAttributeDescriptor[").append(this.getAttributeName()
                ).append(",").append(this.getAttributeClass().getName()).append(",").append(
                this.getAttributeInitialValue()).append("(").append((
                this.getAttributeInitialValue() != null ? this.getAttributeInitialValue(
                ).getClass().getName() : "null")).append(")]").toString();
    }

    
    /* Implementierung des Interfaces AttributeDescriptor. */
    
    public String getAttributeName() {
        return this.name;
    }
    
    public Class getAttributeClass() {
        return this.cls;
    }
    
    public Object getAttributeInitialValue() {
        return this.initialvalue; 
    }
    
    public boolean isReference() {
        return this.reference;
    }
    
}   
