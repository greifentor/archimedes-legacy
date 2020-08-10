/*
 * GUIDiagramModelListener.java
 *
 * 22.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.gui;


/**
 * An interface which describes a listener for a GUI diagram model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.05.2013 - Added.
 */

public interface GUIDiagramModelListener {

    /**
     * The method will be called if the altered flag state is changed.
     *
     * @param newState The new state of the altered flag.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    abstract public void stateChanged(boolean newState);

}