/*
 * GUIViewModel.java
 *
 * 14.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.gui;


/**
 * A view from the point of GUI.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 14.05.2013 - Added.
 */

public interface GUIViewModel {

    /**
     * Adds the passed GUIObjectModel to the list of the GUI object models which are shown in
     * the view.
     *
     * @param object The object to add to the view.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public void addObject(GUIObjectModel object);

    /**
     * Returns the name of the view.
     *
     * @return The name of the view.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public String getName();

    /**
     * Returns an array with object which are shown on the view.
     *
     * @return An array with object which are shown on the view.
     *
     * @changed OLI 14.05.2013 - Added.
     */
    abstract public GUIObjectModel[] getObjects();

    /**
     * Checks if the current view is the main view. The diagram / data model should have only
     * one main view.
     *
     * @return <CODE>true</CODE> if the current view is the main view.
     *
     * @changed OLI 09.06.2016 - Added.
     */
    abstract public boolean isMainView();

}