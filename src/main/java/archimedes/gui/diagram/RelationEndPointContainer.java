/*
 * RelationEndPointContainer.java
 *
 * 22.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.gui.diagram;


import java.awt.*;

import archimedes.model.gui.*;


/**
 * Diese Klasse stellt eine Verbindung zwischen eine Relation und einem ihrer End-Punkte 
 * innerhalb der Archimedes-Applikation her.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public class RelationEndPointContainer extends RelationPointContainer {

    public GUIObjectModel object = null;

    /** 
     * Generiert einen neuen RelationPointContainer anhand der &uuml;bergebenen Parameter.
     *
     * @param relation Die Relation zum Container.
     * @param point Der Punkt zur Relation.
     * @param table Die Tabelle, die an dem angegebenen Punkt mit der Relation verbunden ist.
     */
    public RelationEndPointContainer(GUIRelationModel relation, Point point,
            GUIObjectModel object) {
        super(relation, point);
        this.object = object;
    }
    
}