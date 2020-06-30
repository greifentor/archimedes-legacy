/*
 * GUIDiagramModel.java
 *
 * 14.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.model.gui;

import java.util.List;

import archimedes.gui.diagram.PropertyGetter;
import archimedes.model.ChangeObserver;
import corentx.dates.PDate;

/**
 * This interface provides the methods which are to implement for classes which contains the base data for a diagram
 * shown in the GUI.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.05.2013 - Added.
 * @changed OLI 30.06.2020 - Added the getter for the additional diagram info. Removed "abstract public" from method
 *          definitions.
 */

public interface GUIDiagramModel extends ChangeObserver, PropertyGetter {

	/**
	 * Adds the passed listener to the listeners which are listening to the GUI diagram model.
	 *
	 * @param l The new listener.
	 *
	 * @changed OLI 22.05.2013 - Added.
	 */
	void addGUIDiagramModelListener(GUIDiagramModelListener l);

	/**
	 * Adds the passed object to the model.
	 *
	 * @param guiObject The GUI object to add to the model.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	void addGUIObject(GUIObjectModel guiObject);

	/**
	 * Adds the passed view to the model.
	 *
	 * @param view The view to add to the model.
	 *
	 * @changed OLI 23.05.2013 - Added.
	 */
	void addView(GUIViewModel view);

	/**
	 * Clears the altered flag for the diagram model.
	 *
	 * @changed OLI 22.05.2013 - Added.
	 */
	void clearAltered();

	/**
	 * Returns the additional diagram info.
	 * 
	 * @return The additional diagram info.
	 * 
	 * @changed OLI 29.06.2020 - Added.
	 */
	String getAdditionalDiagramInfo();

	/**
	 * Returns the name of the author of the diagram.
	 *
	 * @return The name of the author of the diagram.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	String getAuthor();

	/**
	 * Returns the date of the creation of the current version.
	 *
	 * @return The date of the creation of the current version.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	PDate getDateOfCreation();

	/**
	 * Returns the font size of the diagram headline.
	 *
	 * @return The font size of the diagram headline.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	int getFontSizeDiagramHeadline();

	/**
	 * Returns the font size of the diagram headlines subtitle.
	 *
	 * @return The font size of the diagram headlines subtitle.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	int getFontSizeSubtitles();

	/**
	 * Returns GUI object with the passed name.
	 *
	 * @param name The name of the GUI object which is to return.
	 * @return The GUI object with the passed name or <CODE>null</CODE> if there is no GUI object with the passed name.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	GUIObjectModel getGUIObject(String name);

	/**
	 * Returns an array with the GUI objects which are contained by the diagram.
	 *
	 * @return An array with the GUI objects which are contained by the diagram.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	GUIObjectModel[] getGUIObjects();

	/**
	 * Returns the main view if there is one.
	 *
	 * @return The view if there is one, otherwise <CODE>null</CODE>.
	 *
	 * @changed OLI 19.02.2016 - Added.
	 */
	GUIViewModel getMainView();

	/**
	 * Returns the name of the diagram.
	 *
	 * @return The name of the diagram.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	String getName();

	/**
	 * Returns the version of the diagram.
	 *
	 * @return The version of the diagram.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	String getVersion();

	/**
	 * Returns the view with the passed name.
	 *
	 * @param name The view with the passed name.
	 * @return The view with the passed name (or <CODE>null</CODE> if there is no view with the passed name).
	 *
	 * @changed OLI 23.05.2013 - Added.
	 */
	GUIViewModel getView(String name);

	/**
	 * Returns a list with the views of the model.
	 *
	 * @return A list with the views of the model.
	 *
	 * @changed OLI 23.05.2013 - Added.
	 */
	List<GUIViewModel> getViews();

	/**
	 * Returns a list of views which contains the GUI object with the passed name.
	 *
	 * @param guiObjectName The name of the GUI object whose view should be returned.
	 * @return A list of views which contains the GUI object with the passed name or an empty list if the object with
	 *         the passed name is in no view.
	 *
	 * @changed OLI 23.05.2013 - Added.
	 */
	List<GUIViewModel> getViewsForObject(String guiObjectName);

	/**
	 * Checks if the deprecated tables should be hidden in the GUI.
	 *
	 * @return <CODE>true</CODE> if deprecated tables should not be shown in the GUI.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	boolean isDeprecatedTablesHidden();

	/**
	 * Checks if the altered flag is raised for the diagram model.
	 *
	 * @return <CODE>true</CODE> if the diagram model is altered.
	 *
	 * @changed OLI 22.05.2013 - Added.
	 */
	boolean isAltered();

	/**
	 * Raises the altered flag for the diagram model.
	 *
	 * @changed OLI 22.05.2013 - Added.
	 */
	void raiseAltered();

	/**
	 * Removes the passed listener from the listeners which are listening to the GUI diagram model.
	 *
	 * @param l The listener to remove.
	 *
	 * @changed OLI 22.05.2013 - Added.
	 */
	void removeGUIDiagramModelListener(GUIDiagramModelListener l);

	/**
	 * Removes the passed object from the model.
	 *
	 * @param guiObject The GUI object to remove from the model.
	 *
	 * @changed OLI 30.05.2013 - Added.
	 */
	void removeGUIObject(GUIObjectModel guiObject);

	/**
	 * Sets a new value for the hide deprecated tables flag.
	 *
	 * @param b The new value for the hide deprecated tables flag.
	 *
	 * @changed OLI 11.03.2016 - Added.
	 */
	void setDeprecatedTablesHidden(boolean b);

}