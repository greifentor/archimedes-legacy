/*
 * ArchimedesEditorDescriptorList.java
 *
 * 13.07.2005
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.app;


import corent.djinn.*;


/**
 * Diese Spezialisierung der DefaultEditorDescriptorList ist auf die Nutzung im Zusammenspiel 
 * mit ArchimedesEditorDescriptoren zugeschnitten. Sie bietet die M&ouml;glichkeit einen 
 * EditorDescriptor &uuml;ber seinen Namen anzusprechen.<BR>
 * <HR>
 * 
 * @author ollie
 *
 */

public class ArchimedesEditorDescriptorList extends DefaultEditorDescriptorList {
    
    /** Erzeugt eine DefaultEditorDescriptorList. */
    public ArchimedesEditorDescriptorList() {
        super();
    }
    
    /** 
     * Ermittelt den EditorDescriptor zum angegebenen Namen.
     *
     * @param name Der EditorDescriptor zum angegebenen Namen.
     * @return Der EditorDescriptor zum Namen bzw. <TT>null</TT>, falls kein EditorDescriptor 
     *     vorhanden ist.
     */
    public EditorDescriptor forName(String name) {
        for (int i = 0, len = this.size(); i < len; i++) {
            EditorDescriptor ed = this.elementAt(i);
            if ((ed instanceof ArchimedesEditorDescriptor) && (((ArchimedesEditorDescriptor) ed
                    ).getName().equals(name))) {
                return ed;
            }
        }
        return null;
    }

}
