/*
 * DefaultEditorDescriptorList.java
 *
 * 04.01.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


/**
 * Diese Klasse ist eine Standardimplementierung der EditorDescriptorList.
 * 
 * @author O.Lieshoff
 *
 */

public class DefaultEditorDescriptorList implements EditorDescriptorList {
    
    /* Ein Vector zur Speicherung der Listeneintr&auml;ge. */
    private Vector liste = new Vector();
    
    /** Erzeugt eine DefaultEditorDescriptorList. */
    public DefaultEditorDescriptorList() {
        super();
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public String toString() {
        StringBuffer sb = new StringBuffer();
        for (int i = 0, len = this.size(); i < len; i++) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(liste.elementAt(i));
        }
        return "[" + sb.toString() + "]";
    }
    
    
    /* Implementierung des Interfaces EditorDescriptorList. */
    
    public void addElement(EditorDescriptor ed) {
        this.liste.addElement(ed);
    }
    
    public void removeElement(EditorDescriptor ed) {
        this.liste.removeElement(ed);
    }

    public EditorDescriptor elementAt(int n) {
        return (EditorDescriptor) this.liste.elementAt(n);
    }

    public int size() {
        return this.liste.size();
    }
    
    public boolean isAlterInBatch() {
        for (int i = 0, len = this.size(); i < len; i++) {
            EditorDescriptor ed = this.elementAt(i);
            if (ed.isAlterInBatch()) {
                return true;
            }
        }
        return false;
    }


}
