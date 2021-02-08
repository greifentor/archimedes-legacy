/*
 * SelectableVectorElement.java
 *
 * 11.06.2006
 *
 * (c) by O.Lieshoff
 *
 */
 
package corent.djinn;


/**
 * Ein Object-Wrapper f&uuml;r die Zusammenarbeit mit dem SelectableVectorTableModel.
 *
 * @author O.Lieshoff
 *
 */

public class SelectableVectorElement implements Comparable {
    
    private Object o = null;
    private boolean selected = false; 
    
    /**
     * Generiert ein SelectableVectorElement mit den &uuml;bergebenen Parametern.
     *
     * @param o Das zuwrappende Objekt.
     * @param selected Diese Flagge ist zu setzen, wenn das gewrapte Objekt selektiert ist.
     */
    public SelectableVectorElement(Object o, boolean selected) {
        super();
        this.o = o;
        this.setSelected(selected);
    }
    
    /** @return Referenz auf das gewrapte Objekt. */
    public Object getObject() {
        return this.o;
    }
    
    /** @return <TT>true</TT>, falls das gewrapte Objekt selektiert ist. */
    public boolean isSelected() {
        return this.selected;
    }
    
    /** 
     * Selektiert bzw. deselektiert das gewrapte Objekt.
     *
     * @param selected Der neue Wert f&uuml;r den Selektionstatus des gewrapten Objektes.
     */
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
    
    
    /* Implementierung des Interfaces Comparable. */
    
    public int compareTo(Object obj) {
        if (!(this.o instanceof Comparable)) {
            return 0;
        }
        return ((Comparable) this.getObject()).compareTo(((SelectableVectorElement) obj
                ).getObject());
    }
    
}
