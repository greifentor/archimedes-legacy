/*
 * BaccaraFrameWithListener.java
 *
 * 19.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.components;

import static corentx.util.Checks.*;

import baccara.gui.*;

import java.util.*;

import org.apache.log4j.*;


/**
 * A frame for usage in a Baccara application with a basic listener logic.
 *
 * @param <LISTENER> The type of the class which should be able to observe the frame.
 * @param <EVENT> The type of the event which will be fired by the frame.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 19.05.2016 - Added.
 */

abstract public class BaccaraFrameWithListener<LISTENER, EVENT> extends BaccaraFrame {

    private static final Logger LOG = Logger.getLogger(BaccaraFrameWithListener.class);

    private List<LISTENER> listeners = new LinkedList<LISTENER>();

    /**
     * Creates a new Baccara frame with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @throws NullPointerException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public BaccaraFrameWithListener(GUIBundle guiBundle) {
        super(guiBundle);
    }

    /**
     * Creates a new Baccara frame with the passed parameters.
     *
     * @param guiBundle A bundle with GUI information.
     * @param titleRedId The resource id for the frame title.
     * @param replaces Replaces for the title.
     * @throws NullPointerException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public BaccaraFrameWithListener(GUIBundle guiBundle, String titleResId, Object... replaces)
            {
        super(guiBundle, titleResId, replaces);
    }

    /**
     * Adds the passed listener to the listeners which are observing the frame.
     *
     * @param listener The listener to add.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public void addBaccaraFrameListener(LISTENER listener) {
        ensure(listener != null, "listener to add cannot be null.");
        if (!this.listeners.contains(listener)) {
            this.listeners.add(listener);
        }
    }

    /**
     * Calls a fire method for all listeners with the passed event.
     *
     * @param event The event which should be fired.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    protected void fireBaccaraFrameEvent(EVENT event) {
        try {
            for (LISTENER listener : this.listeners) {
                try {
                    this.fireEvent(listener, event);
                } catch (Exception e0) {
                    LOG.error("error while processing event listener: " + listener + ", event: "
                            + event);
                }
            }
        } catch (Exception e) {
            LOG.error("error while processing event listeners for event: " + event);
        }
    }

    /**
     * This method implements the real call of the listener.
     *
     * @param listener The listener which is to call.
     * @param event The fired event.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    abstract public void fireEvent(LISTENER listener, EVENT event);

    /**
     * Removes the passed listener from the listeners which are observing the frame.
     *
     * @param listener The listener to remove.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 19.05.2016 - Added.
     */
    public void removeBaccaraFrameListener(LISTENER listener) {
        ensure(listener != null, "listener to remove cannot be null.");
        this.listeners.remove(listener);
    }

}