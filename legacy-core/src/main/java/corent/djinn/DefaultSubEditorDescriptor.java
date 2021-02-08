/*
 * DefaultSubEditorDescriptor.java
 *
 * 25.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import corent.base.*;


/**
 * Diese Beispielimplementierung des SubEditorDescriptors als Vorlage f&uuml;r eigene 
 * Implementierungen dienen.
 *
 * @author
 *     O.Lieshoff
 *     <P>
 *
 */
 
public class DefaultSubEditorDescriptor extends DefaultEditorDescriptor 
        implements SubEditorDescriptor {
    
    /* Referenz auf die SubEditorFactory des DefaultSubEditorDescriptors. */
    private SubEditorFactory subEditorFactory = null;
    
    /** 
     * Generiert einen DefaultSubEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param subEditorFactory Die SubEditorFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten und Labels versorgen soll. 
     */
    public DefaultSubEditorDescriptor(Attributed attr, SubEditorFactory subEditorFactory) {
        super(0, attr, -1, null, null, null, '\0', null, null);
        this.subEditorFactory = subEditorFactory;
    }
    
    /** 
     * Generiert einen DefaultSubEditorDescriptor anhand der &uuml;bergebenen Parameter.
     *
     * @param tab Das Tab, auf dem die Komponente abgebildet werden soll. 
     * @param attr Das Attributed-Objekt, das durch den Descriptor manipuliert werden soll.
     * @param subEditorFactory Die SubEditorFactory, &uuml;ber die sich der Descriptor mit
     *     Komponenten und Labels versorgen soll. 
     */
    public DefaultSubEditorDescriptor(int tab, Attributed attr, 
            SubEditorFactory subEditorFactory) {
        super(tab, attr, -1, null, null, null, '\0', null, null);
        this.subEditorFactory = subEditorFactory;
    }
    
    
    /* Implementierung des Interfaces SubEditorDescriptor. */
    
    public SubEditorFactory getSubEditorFactory() {
        return this.subEditorFactory;
    }
    
}
