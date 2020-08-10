/*
 * ComponentDiagramListener.java
 *
 * 14.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui.diagram;

import archimedes.legacy.model.gui.GUIObjectModel;

/**
 * An interface which allow to get information about events of the component diagram.
 *
 * @param <T> An enumeration which contains the identifiers for creating objects.
 * 
 * @author O.Lieshoff
 *
 * @changed OLI 14.05.2013 - Added.
 */

public interface ComponentDiagramListener<T extends Enum<?>> {

	/**
	 * This method will be called if the user double clicks an entity (box) in the diagram.
	 *
	 * @param object    The object which has been double clicked.
	 * @param component A reference of the GUI component which shows the diagram.
	 * @param buttonId  The id of the button which is clicked.
	 *
	 * @changed OLI 14.05.2013 - Added.
	 */
	abstract public void objectDoubleClicked(GUIObjectModel object, ComponentDiagramm<T> component, int buttonId);

}