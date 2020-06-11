/*
 * EventManager.java
 *
 * 21.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.events;


import static corentx.util.Checks.*;

import java.util.*;


/**
 * A manager which manages a list of listeners.
 *
 * @param <E> The type of events which are fired by the event manager.
 * @param <L> The type of the listeners which are managed by the event manager. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.01.2013 - Added.
 */

public class EventManager<E extends AbstractEvent, L extends SimpleListener> {

    private List<L> listeners = new Vector<L>();

    /**
     * Creates a new event manager.
     *
     * @changed OLI 21.01.2013 - Added.
     */
    public EventManager() {
        super();
    }

    /**
     * Adds a new listener to the list of the managed listeners.
     *
     * @param l The new listener to add to the listeners which are managed by the event manager.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 21.01.2013 - Added.
     */
    public void add(L l) throws IllegalArgumentException {
        ensure(l != null, "listener cannot be null.");
        this.listeners.add(l);
    }

    /**
     * Fires the passed event to all the listeners which are managed.
     *
     * @param e The event which is to fire.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 21.01.2013 - Added.
     */
    public void fire(E e) {
        ensure(e != null, "event cannot be null.");
        for (int i = 0, leni = this.listeners.size(); i < leni; i++) {
            try {
                this.listeners.get(i).eventFired(e);
            } catch (Exception ex) {
            }
        }
    }

    /**
     * This method will be called if an exception is occurred during event firing. Override this
     * method if you want to be notified by those situations.
     *
     * @param e The exception which is occurred.
     * @param l The listener whose firing leaded to the exception.
     *
     * @changed OLI 21.01.2013 - Added.
     */
    public void exceptionDuringFireEventDetected(Exception e, L l) {
    }

    /**
     * Removes the passed listener from the list of the managed listeners.
     *
     * @param l The listener to remove from the listeners which are managed by the event
     *         manager.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 21.01.2013 - Added.
     */
    public void remove(L l) throws IllegalArgumentException {
        ensure(l != null, "listener cannot be null.");
        if (this.listeners.contains(l)) {
            this.listeners.remove(l);
        }
    }

}