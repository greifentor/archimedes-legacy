/*
 * StatusBarOwner.java
 *
 * 22.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui;


/**
 * An interface which describes a class which shows a status bar.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 22.05.2013 - Added.
 */

public interface StatusBarOwner {

    /**
     * Returns a renderer which is used to generate the status bar content.
     *
     * @return A renderer which is used to generate the status bar content.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    abstract public StatusBarRenderer getRenderer();

    /**
     * Updates the status bar with the passed message.
     *
     * @param message The message to update in the status bar.
     *
     * @changed OLI 22.05.2013 - Added.
     */
    abstract public void updateStatusMessage(String message);

}