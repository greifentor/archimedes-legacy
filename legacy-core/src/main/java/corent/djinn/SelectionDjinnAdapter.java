/*
 * SelectionDjinnAdapter.java
 *
 * 19.02.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;


/**
 * Mit Hilfe dieses Adapters wird ein SelectionDjinnListener implementiert. Die Ereignisse
 * werden jedoch lediglich in leeren Methoden abgefangen.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class SelectionDjinnAdapter implements SelectionDjinnListener {
    
    /** Generiert eine Instanz des SelectionDjinnAdapters. */
    public SelectionDjinnAdapter() {
        super();
    }
    
    
    /* Implementierung des Interfaces SelectionDjinnListener. */
    
    public void selectionDone(Vector selected) {
    }
        
    public void selectionAborted() {
    }
    
    public void selectionDuplicated(Vector selected) {
    }
        
    public void selectionUpdated() {
    }
    
    public void elementCreated() {
    }
        
    public void djinnClosing() {
    }
        
    public void djinnClosed() {
    }
        
}
