/*
 * DataDiagramModelListener.java
 *
 * 26.04.2008
 *
 * (c) by ollie
 *
 */
 
package archimedes.legacy.model;


/**
 * Dieses Interface kann implementiert werden, um die Ereignisse eines DiagrammModel 
 * abzuh&ouml;ren.
 *
 * @author 
 *     ollie
 *     <P>
 * 
 * @changed
 *     OLI 26.04.2008 - Hinzugef&uuml;gt.
 *
 */
 
public interface DataDiagramModelListener {
    
    /**
     * Diese Methode wird aufgerufen, wenn eine &Auml;derung am Model vorgenommen worden ist.
     */
    public void dataDiagramModelChanged();
    
}
