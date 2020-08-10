/*
 * RelationModel.java
 *
 * 20.03.2004
 *
 * (c) by O.Lieshoff
 *
 */
 
package archimedes.legacy.model;

import java.awt.*;
import java.util.*;


/**
 * Diese Interface definiert die notwendigen Funktionen einer Relation der 
 * Archimedes-Applikation.<BR>
 * <HR>
 *
 * @author O.Lieshoff
 *
 */
 
public interface RelationModel {

    /**
     * Liefert eine Liste mit den Punkten, durch die die Relation l&auml;ft.
     *
     * @param view Der View, f&uuml;r den die Liste der Punkte erstellt werden soll.
     * @return Die Liste mit den Punkten, durch die die Relation verl&auml;ft. 
     */
    abstract public Vector getPoints(ViewModel view);

    /**
     * F&uuml;gt einen Punkt mit den &uuml;bergebenen Koordinaten an die Liste der Punkte der
     * Relation an.
     *
     * @param view Der View, zu dem der Punkt eingef&uuml;gt werden soll.
     * @param x Die X-Koordinate, zu der ein Punkt erzeugt werden soll.
     * @param y Die Y-Koordinate, zu der ein Punkt erzeugt werden soll.
     */
    abstract public void addPoint(ViewModel view, int x, int y);

    /**
     * Returns the referenced column.
     *
     * @return The referenced column.
     *
     * @changed OLI 02.06.2016 - Added.
     */
    abstract public ColumnModel getReferenced();

    /**
     * Returns the referencer column.
     *
     * @return The referencer column.
     *
     * @changed OLI 02.06.2016 - Added.
     */
    abstract public ColumnModel getReferencer();

    /**
     * Returns the table which is bound by the passed point.
     *
     * @param view The view which the point is to evaluate for.
     * @param p The point which the table is to return for.
     * @return The table which belongs to the point p or <CODE>null</CODE> if no table exists
     *         for the table.
     */
    abstract public TableModel getTablePointed(ViewModel view, Point p);

    /**
     * Returns the relation which is bound to the column for the passed table model.
     *
     * @param t The table which the column is to return for.
     * @return The relation which is bound to the column for the passed table model.
     */
    abstract public ColumnModel getTabellenspalte(TableModel t);

    /**
     * Sets the column as new referenced column.
     *
     * @param column The column which the relation start from.
     */
    abstract public void setReferencer(ColumnModel column);

    /**
     * Sets the column as new referenced column.
     *
     * @param column The column which is to set as referenced column.
     */
    abstract public void setReferenced(ColumnModel column);

}