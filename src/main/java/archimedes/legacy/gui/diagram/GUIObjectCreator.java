/*
 * GUIObjectCreator.java
 *
 * 14.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui.diagram;

import archimedes.legacy.model.gui.GUIObjectModel;
import archimedes.legacy.model.gui.GUIViewModel;

/**
 * An interface which provides the methods for a GUI object creator.
 *
 * @param <T> An enumeration which contains the identifiers for creating objects.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.05.2013 - Added.
 */

public interface GUIObjectCreator<T extends Enum<?>> {

	/**
	 * Creates a new GUI object with the passed parameters.
	 *
	 * @param view   The view where the object is shown on.
	 * @param x      The x coordinate inside the passed view.
	 * @param y      The y coordinate inside the passed view.
	 * @param type   An identifier which decides about the type of the created object.
	 * @param filled Set this flag to generate default structure for the new object.
	 * @return A new GUI object.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public GUIObjectModel create(GUIViewModel view, int x, int y, T type, boolean filled);

	/**
	 * Creates a new GUI object with the passed parameters.
	 *
	 * @param view The view where the object is shown on.
	 * @param x    The x coordinate inside the passed view.
	 * @param y    The y coordinate inside the passed view.
	 * @param type An identifier which decides about the type of the created object.
	 * @param s    A string with properties which define a template for the new object.
	 * @return A new GUI object.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public GUIObjectModel create(GUIViewModel view, int x, int y, T type, String s);

}