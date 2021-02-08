/*
 * DiagrammModelListener.java
 *
 * 19.04.2007
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.model;


/**
 * Dieses Interface kann implementiert werden, um die Ereignisse eines DiagrammModel 
 * abzuh&ouml;ren.
 *
 * @author ollie
 *
 */
 
public interface DiagrammModelListener {
    
    /**
     * Diese Methode wird aufgerufen, wenn eine &Auml;derung am Model vorgenommen worden ist.
     */
    public void diagrammModelChanged();
    
}
