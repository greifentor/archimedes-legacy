/*
 * ListViewSelectable.java
 *
 * 28.08.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Dieses Interface erweitert die das Selectable-Interface um eine Methode zu Anzeige in einem 
 * List-View.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface ListViewSelectable extends Selectable {
    
    /**
     * Diese Methode liefert einen String, der in einer ListView-Komponente (z. B. dem 
     * DefaultListView zur Anzeige gebracht wird.
     *
     * @return Die Stringrepr&auml;sentation des f&uuml;r die ListView-Ansicht.
     */
    public String toListViewString();
    
}
