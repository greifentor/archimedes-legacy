/*
 * GUIRelationModel.java
 *
 * 14.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.gui;


import corent.base.*;

import java.awt.*;
import java.util.List;


/**
 * An interface which covers the GUI relevant information of the relations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.05.2013 - Added.
 */

public interface GUIRelationModel {

    /** Radius in Pixels which is to hit if a point is treated as hit. */
    public static int HIT_TOLERANCE = Integer.getInteger(
            "archimedes.scheme.Relation.HIT_TOLERANCE", 10);

    /**
     * Adds the passed point for the passed view to the relation.
     *
     * @param view The view where the point is to add to.
     * @param point The point which is to add.
     *
     * @changed OLI 21.05.2013 - Added.
     */
    abstract public void addPoint(GUIViewModel view, Point p);

    /**
     * Adds the passed point for the passed view to the relation.
     *
     * @param view The view where the point is to add to.
     * @param x The x value of the point.
     * @param y The y value of the point.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    abstract public void addPoint(GUIViewModel view, int x, int y);

    /**
     * Checks if the passed point collides which its neighbor.
     *
     * @param view The view which the check is made for.
     * @param p The point to check.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public boolean collisionWithNeighbour(GUIViewModel view, Point p);

    /**
     * Computes a new offset of the connection between the relation and the passed object.
     *
     * @param view The view which the offset is to compute for.
     * @param object The object which the offset is to compute for.
     * @param x The x coordinate of the cursor as base for the computings.
     * @param y The y coordinate of the cursor as base for the computings.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public void computeOffset(GUIViewModel view, GUIObjectModel object, int x, int y);

    /**
     * Returns all points of the relation with the view information.
     *
     * @return A list with all information about the points of all views.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    abstract public GUIRelationPoint[] getAllPointInformation();

    /**
     * Returns the direction for the passed view and the specified end point of the relation.
     *
     * @param view The view which the direction is to get for.
     * @param i The id of the end point (0 = referencer, 1 = referenced).
     * @return The direction for the passed view and the specified end point of the relation.
     *
     * @changed OLI 17.05.2013 - Added.
     */
    abstract public Direction getDirection(GUIViewModel view, int i);

    /**
     * Returns the entity (box) which is bound to the relation by the passed point.
     *
     * @param view The view for which the entity is to find.
     * @param p The point whose entity is to find.
     * @return The entity (box) which is bound to the relation by the passed point or
     *         <CODE>null</CODE> if there is non.
     */
    abstract public GUIObjectModel getObjectPointed(GUIViewModel view, Point p);

    /**
     * Returns the offset for the passed view and the specified end point of the relation.
     *
     * @param view The view which the offset is to get for.
     * @param i The id of the end point (0 = referencer, 1 = referenced).
     * @return The offset for the passed view and the specified end point of the relation.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    abstract public int getOffset(GUIViewModel view, int i);

    /** 
     * Returns an existing point of the relation or a new one, if there is no point at the
     * passed position in the view.
     *
     * @param view The view which the point is returned for.
     * @param x The x coordinate which the point is to set or return for.
     * @param y The y coordinate which the point is to set or return for.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public Point getPoint(GUIViewModel view, int x, int y);

    /**
     * Returns a list with all point which are part of the relation in the passed view.
     *
     * @param view The view which the points are to return for.
     * @return A list with all point which are part of the relation in the passed view.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public List<Point> getPointsForView(GUIViewModel view);

    /**
     * Returns the entity (box) which is in the role of the referenced entity in the relation.
     *  
     * @return The entity (box) which is in the role of the referenced entity in the relation.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public GUIObjectModel getReferencedObject();

    /**
     * Returns the entity (box) which is in the role of the referencing entity in the relation.
     *  
     * @return The entity (box) which is in the role of the referencing entity in the relation.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public GUIObjectModel getReferencerObject();

    /**
     * Returns an array with the points which are contained by the relation.
     *
     * @param view The view which the points are to get for.
     * @return n array with the points which are contained by the relation.
     *
     * @changed OLI 21.05.2013 - Added.
     */
    abstract public Point[] getShapePointsForView(GUIViewModel view);

    /**
     * Returns a list with the view where the relation has individual preferences.
     *
     * @return A list with the view where the relation has individual preferences.
     *
     * @changed OLI 21.05.2013 - Added.
     */
    abstract public List<GUIViewModel> getViews();

    /**
     * Checks if the passed point is an end point of the relation.
     * 
     * @param view The view for which is to check for.
     * @param p The point to check.
     * @return <CODE>true</CODE> if the point is at the end of the relation, <CODE>false</CODE>
     *         otherwise.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public boolean isEndPoint(GUIViewModel view, Point p);

    /**
     * Removes the passed point on the passed view.
     *
     * @param view The view where the point is to delete from.
     * @param p The point to delete.
     */
    public void removePoint(GUIViewModel view, Point p);

    /**
     * Updates the direction of the relation for the passed view at the described point.
     *
     * @param view The view for which the direction is to update.
     * @param i The index of the end point of the relation (0 = referencer, 1 = referenced).
     * @param d The new direction.
     *
     * @changed OLI 17.05.2013 - Added.
     */
    abstract public void setDirection(GUIViewModel view, int i, Direction d);

    /**
     * Updates the offset of the relation for the passed view at the described point.
     *
     * @param view The view for which the direction is to update.
     * @param i The index of the end point of the relation (0 = referencer, 1 = referenced).
     * @param o The new offset.
     *
     * @changed OLI 17.05.2013 - Added.
     */
    abstract public void setOffset(GUIViewModel view, int i, int o);
}