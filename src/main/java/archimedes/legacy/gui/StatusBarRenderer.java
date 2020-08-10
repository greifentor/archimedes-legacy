/*
 * StatusBarRenderer.java
 *
 * 22.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui;

import archimedes.legacy.model.gui.GUIObjectModel;
import archimedes.legacy.model.gui.GUIRelationModel;

/**
 * An interface which describes the methods for a renderer to print GUI objects in the status bar.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.05.2013 - Added.
 */

public interface StatusBarRenderer {

	/**
	 * Returns a string with the information of an object.
	 *
	 * @param guiObjectModel The object which is to render.
	 *
	 * @changed OLI 22.05.2013 - Added.
	 */
	abstract public String renderObject(GUIObjectModel guiObjectModel);

	/**
	 * Returns a string with the information of a relation.
	 *
	 * @param guiRelationModel The relation which is to render.
	 *
	 * @changed OLI 22.05.2013 - Added.
	 */
	abstract public String renderRelation(GUIRelationModel guiRelationModel);

}