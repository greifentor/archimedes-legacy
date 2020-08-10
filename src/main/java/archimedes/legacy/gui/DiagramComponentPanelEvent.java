/*
 * DiagramComponentPanelEvent.java
 *
 * 18.05.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.gui;


import static corentx.util.Checks.*;

import corentx.annotations.*;

import org.apache.commons.lang3.builder.*;


/**
 * An event container for events fired by a <CODE>DiagramComponentPanel</CODE>.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 18.05.2016 - Added.
 */

@Immutable public class DiagramComponentPanelEvent {

    /** Identifiers for event types. */
    public enum Type {
        WARNINGS_CLICKED;
    }

    private int clickCount = 0;
    private Type type = null;

    /**
     * Creates a new event with the passed parameters.
     *
     * @param type The type of the event.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public DiagramComponentPanelEvent(Type type) {
        super();
        ensure(type != null, "type cannot be null.");
        this.type = type;
    }

    /**
     * Creates a new event with the passed parameters.
     *
     * @param type The type of the event.
     * @param clickCount The number of clicks in case of a mouse event.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public DiagramComponentPanelEvent(Type type, int clickCount) {
        this(type);
        this.clickCount = clickCount;
    }

    /**
     * Returns the click count in case of a mouse event. Otherwise "0".
     *
     * @return Returns the click count in case of a mouse event. Otherwise "0".
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public int getClickCount() {
        return this.clickCount;
    }

    /**
     * Returns the type of the event.
     *
     * @return The type of the event.
     *
     * @changed OLI 18.05.2016 - Added.
     */
    public Type getType() {
        return this.type;
    }

    /**
     * @changed OLI 18.05.2016 - Added.
     */
    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}