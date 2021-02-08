/*
 * AbstractEvent.java
 *
 * 21.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.events;


/**
 * An abstract class which provides basic functionalities of an event e. g. fired in a GUI
 * environment.
 *
 * @param <T> A type for the event. This should be an enum.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 21.01.2013 - Added.
 */

public class AbstractEvent<T> {

    private T eventType = null;

    /**
     * Creates a new abstract event with the passed parameters.
     *
     * @param eventType The type of the event.
     *
     * @changed OLI 21.01.2013 - Added.
     */
    public AbstractEvent(T eventType) {
        super();
        this.eventType = eventType;
    }

    /**
     * Returns the type of the event if there is one defined.
     *
     * @return The type of the event if there is one defined.
     *
     * @changed OLI 21.01.2013 - Added.
     */
    public T getEventType() {
        return this.eventType;
    }

}