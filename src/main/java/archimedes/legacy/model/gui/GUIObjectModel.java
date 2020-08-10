/*
 * GUIObjectModel.java
 *
 * 14.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.gui;

import java.awt.Graphics;

import archimedes.legacy.gui.PaintMode;
import archimedes.legacy.gui.diagram.CoordinateConverter;
import archimedes.legacy.gui.diagram.ShapeContainer;
import archimedes.legacy.model.StereotypeModel;

/**
 * This interface has to be implemented by class which should be shown in a diagram GUI component.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.05.2013 - Added.
 */

public interface GUIObjectModel extends Comparable {

	/**
	 * Returns the height of the object in the GUI.
	 *
	 * @return The height of the object in the GUI.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public int getHeight();

	/**
	 * Returns the name of the GUI object.
	 *
	 * @return The name of the GUI object.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public String getName();

	/**
	 * Returns an array with all GUI relations if the object has some.
	 *
	 * @return An array with all GUI relations if the object has some.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public GUIRelationModel[] getRelations();

	/**
	 * Returns an array with the stereotypes which the object belongs to.
	 *
	 * @return An array with the stereotypes which the object belongs to.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public StereotypeModel[] getStereotypes();

	/**
	 * Returns the width of the object in the GUI.
	 *
	 * @return The width of the object in the GUI.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public int getWidth();

	/**
	 * Returns the x coordinate for the passed view model.
	 *
	 * @param view The view which the x coordinate is to get for.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public int getX(GUIViewModel view);

	/**
	 * Returns the y coordinate for the passed view model.
	 *
	 * @param view The view which the y coordinate is to get for.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public int getY(GUIViewModel view);

	/**
	 * Checks if the object is deprecated.
	 *
	 * @return <CODE>true</CODE> if the object is deprecated.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public boolean isDeprecated();

	/**
	 * Checks if the object is shown in the passed view.
	 *
	 * @param view The view which is to check.
	 * @return <CODE>true</CODE> if the object is to show in the passed view.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public boolean isInView(GUIViewModel view);

	/**
	 * Paints the object to the passed graphic context.
	 *
	 * @param converter A coordinate converter for the zoom factor computings.
	 * @param view      The view which the object is painted on.
	 * @param graphics  The graphics context where the object is painted on.
	 * @param paintMode The paint mode (printer or GUI).
	 * @return A <CODE>ShapeContainer</CODE> with a shape of the object to compute hits.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public ShapeContainer paintObject(CoordinateConverter converter, GUIViewModel view, Graphics graphics,
			PaintMode paintMode);

	/**
	 * Paints the relations in which the object acts as a referencer.
	 *
	 * @param converter A coordinate converter for the zoom factor computings.
	 * @param view      The view which the relations is painted on.
	 * @param graphics  The graphics context where the relations are painted on.
	 * @param paintMode The paint mode (printer or GUI).
	 * @return An array with the <CODE>ShapeContainers</CODE> of the painted relations.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public ShapeContainer[] paintRelations(CoordinateConverter converter, GUIViewModel view, Graphics graphics,
			PaintMode paintMode);

	/**
	 * Sets the coordinates for the GUI object in the passed view.
	 *
	 * @param view The view which the coordinates are set for.
	 * @param x    The x coordinate.
	 * @param y    The y coordinate.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public void setXY(GUIViewModel view, int x, int y);

}