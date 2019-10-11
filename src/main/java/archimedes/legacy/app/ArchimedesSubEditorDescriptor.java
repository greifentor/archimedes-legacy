/*
 * AchimedesSubEditorDescriptor.java
 *
 * 13.07.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.base.*;
import corent.djinn.*;


/**
 * Diese Implementierung des SubEditorDescriptors erweitert den ArchimedesSubEditorDescriptor 
 * zur Zusammenarbeit mit der ArchimedesEditorDescriptorList um ein Namens-Attribut.<BR>
 * <HR>
 *
 * @author ollie
 *
 */
 
public class ArchimedesSubEditorDescriptor extends ArchimedesEditorDescriptor
        implements SubEditorDescriptor {
    
    /* Referenz auf die SubEditorFactory des DefaultSubEditorDescriptors. */
    private SubEditorFactory subEditorFactory = null;
    
    /** 
     * Generiert einen ArchimedesSubEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param subEditorFactory Die SubEditorFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten und Labels versorgen soll. 
     */
    public ArchimedesSubEditorDescriptor(String name, Attributed attr, 
            SubEditorFactory subEditorFactory) {
        super(name, 0, attr, -1, null, null, null, '\0', null, null);
        this.subEditorFactory = subEditorFactory;
    }
    
    /** 
     * Generiert einen ArchimedesSubEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param tab Das Tab, auf dem die Komponente abgebildet werden soll. 
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param subEditorFactory Die SubEditorFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten und Labels versorgen soll. 
     */
    public ArchimedesSubEditorDescriptor(String name, int tab, Attributed attr, 
            SubEditorFactory subEditorFactory) {
        super(name, tab, attr, -1, null, null, null, '\0', null, null);
        this.subEditorFactory = subEditorFactory;
    }
    
    
    /* Implementierung des Interfaces SubEditorDescriptor. */
    
    public SubEditorFactory getSubEditorFactory() {
        return this.subEditorFactory;
    }
    
}
