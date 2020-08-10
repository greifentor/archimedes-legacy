/*
 * RelationPointContainer.java
 *
 * 22.03.2004
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui.diagram;

import java.awt.Point;

import archimedes.legacy.model.gui.GUIObjectModel;
import archimedes.legacy.model.gui.GUIRelationModel;

/**
 * Diese Klasse stellt eine Verbindung zwischen eine Relation und einem ihrer Punkte innerhalb der
 * Archimedes-Applikation her.
 *
 * @author O.Lieshoff
 *         <P>
 *
 */

public class RelationPointContainer {

	/** Der mit der Relation in Verbindung gebrachte Punkt. */
	public Point point = null;
	/** Die Relation des Containers. */
	public GUIRelationModel relation = null;

	/**
	 * Generiert einen neuen RelationPointContainer anhand der &uuml;bergebenen Parameter.
	 *
	 * @param relation Die Relation zum Container.
	 * @param point    Der Punkt zur Relation.
	 */
	public RelationPointContainer(GUIRelationModel relation, Point point) {
		super();
		this.relation = relation;
		this.point = point;
	}

	/**
	 * @changed OLI 14.05.2013 - Approved.
	 */
	@Override
	public String toString() {
		if (this.relation != null) {
			GUIObjectModel o0 = this.relation.getReferencerObject();
			GUIObjectModel o1 = this.relation.getReferencedObject();
			return (o0 != null ? o0.getName() : "<null>") + " -> " + (o1 != null ? o1.getName() : "<null>");
		}
		return "<null>";
	}

}