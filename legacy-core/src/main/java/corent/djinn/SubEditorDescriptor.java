/**
 * SubEditorDescriptor.java
 *
 * 20.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/** 
 * Dieses Interface erweitert den EditorDescriptor zum Aufnahme ganzer Panels in den 
 * EditorDjinn.<BR>
 * <HR>
 * 
 * @author O.Lieshoff
 *
 */
 
public interface SubEditorDescriptor extends EditorDescriptor {
    
    /** 
     * @return Referenz auf die mit dem Descriptor verbundene SubEditorFactory, die die 
     *     eigentliche SubEditor-Komponente generiert.
     */
    public SubEditorFactory getSubEditorFactory();

}
