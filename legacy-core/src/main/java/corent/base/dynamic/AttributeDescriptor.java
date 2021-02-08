/*
 * AttributeDescriptor.java
 *
 * 05.04.2005
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.base.dynamic;


/**
 * Mit Hilfe dieses Interfaces wird das notwendige Verhalten eines AttributeDescriptors 
 * festgelegt. Er dient zur Beschreibung von Attributen dynamischer Objekte.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface AttributeDescriptor {
    
    /** @return Der Name, unter dem das Attribut angelegt werden soll. */
    public String getAttributeName();
    
    /** @return Die Klasse des Attributes. */
    public Class getAttributeClass();
    
    /** @return Der Initialwert, den das Attribut beim Erzeugen annehmen soll. */
    public Object getAttributeInitialValue();
    
    /** @return <TT>true</TT>, wenn es sich bei dem Attribut um eine Referenz handelt. */
    public boolean isReference();
    
}
