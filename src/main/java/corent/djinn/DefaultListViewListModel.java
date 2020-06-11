/*
 * DefaultListViewModel.java
 *
 * 28.08.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
 
 
/**
 * Diese Klasse implementiert das ListModel und erlaubt die Erzeugung eines ListModels auf 
 * Vectorbasis.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */

public class DefaultListViewListModel implements ListModel {

    /* Der Inhalt des Models. */
    private Vector inhalt = new Vector();
    /* Die am Model lauschenden Listener. */
    private Vector listener = new Vector();
    
    /**
     * Generiert ein DefaultListViewListModel aus dem &uuml;bergebenen Vector.
     *
     * @param v Der Vector, der als Grundlage f&uuml;r das ListModel genutzt werden soll.
     *
     */
    public DefaultListViewListModel(Vector v) {
        super();
        for (Object o : v) {
            if (o instanceof ListViewSelectable) {
                this.inhalt.addElement(new ListViewSelectableContainer(o));
            } else {
                this.inhalt.addElement(o);
            }
        }
    }
    
    
    /* Implementierung des Interfaces ListModel. */
    
    public void addListDataListener(ListDataListener l) {
        this.listener.addElement(l);
    }
    
    public void removeListDataListener(ListDataListener l) {
        this.listener.removeElement(l);
    }
    
    public Object getElementAt(int i) {
        return this.inhalt.elementAt(i);
    }
    
    public int getSize() {
        return this.inhalt.size();
    }
    
}
