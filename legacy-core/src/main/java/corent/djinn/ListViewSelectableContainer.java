/*
 * ListViewSelectableContainer.java
 *
 * 28.08.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Diese Klasse bietet einen speziellen Container f&uuml;r Objekt, die in ListViewComponents zur
 * Anzeige kommen. Die Anzeige wird hierbei &uuml;ber die Methode <TT>toListViewString()</TT>
 * geregelt, wenn die dem Container &uuml;bergebenen Objekte das Interface 
 * <TT>ListViewSelectable</TT> implementieren. Sonst wird die normale 
 * <TT>toString()</TT>-Methode genutzt.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class ListViewSelectableContainer {
    
    private Object content = null;
    
    /** 
     * Generiert einen Container und nimmt das &uuml;bergebene Object als Inhalt.
     *
     * @param o Das in als Inhalt zu &uuml;bernehmende Objekt. 
     */
    public ListViewSelectableContainer(Object o) {
        super();
        this.content = o;
    }
    
    /** @return Der Inhalt des Containers. */
    public Object getContent() {
        return this.content;
    }
    
    /**
     * Setzt das &uuml;bergebene Objekt als neuen Inhalt des Containers ein.
     *
     * @param o Der neue Inhalt des Containers.
     */
    public void setContent(Object o) {
        this.content = o;
    }
    
    
    /* Ueberschreiben von Methoden der Superklasse. */
    
    public String toString() {
        if (this.content == null) {
            return "<null>";
        } else if (this.content instanceof ListViewSelectable) {
            return ((ListViewSelectable) this.content).toListViewString();
        }
        return this.content.toString();
    }
    
}
