/*
 * SimpleListener.java
 *
 * 21.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.events;


/**
 * A simple listener which is simply able to listen to events fired by a specific object.
 *
 * @param <T> The type of the fired events.
 * 
 * @author O.Lieshoff
 *
 * @changed OLI 21.01.2013 - Added.
 */

public interface SimpleListener<T> {

    /**
     * This method will be called if the observed object notifies an event.
     *
     * @param e The event notified by the object.
     *
     * @changed OLI 21.01.2013 - Added.
     */
    abstract public void eventFired(T e);

}