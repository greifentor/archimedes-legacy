/*
 * ShapeContainer.java
 *
 * 21.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.gui.diagram;


import java.awt.*;


/**
 * Diese Klasse stellt eine Verbindung zwischen einem Shape und eine Tabelle bzw. Relation der
 * Archimedes-Applikation her.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class ShapeContainer {
    
    /** Das mit dem Shape in Verbindung gebrachte Objekt. */
    public Object obj = null;
    /** Das Shape zum ShapeContainer. */
    public Shape shape = null;
    
    /** 
     * Generiert einen neuen ShapeContainer anhand der &uuml;bergebenen Parameter.
     *
     * @param shape Das Shape zum Container.
     * @param obj Das Objekt zum Shape.
     */
    public ShapeContainer(Shape shape, Object obj) {
        super();
        this.shape = shape;
        this.obj = obj;
    }
    
}
