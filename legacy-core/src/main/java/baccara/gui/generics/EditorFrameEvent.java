/*
 * EditorFrameEvent.java
 *
 * 30.05.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui.generics;


import static corentx.util.Checks.*;

import java.awt.*;

import baccara.events.*;


/**
 * A generic class for editor frame events.
 *
 * @param <T> The type of the objects which are edited by the editor frame which raises these
 *         events.
 * @param <WT> The type of the window which is used as editor window.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 30.05.2013 - Added.
 */

public class EditorFrameEvent<T, WT extends Window> extends AbstractEvent<EditorFrameEventType>
        {

    private T object = null;
    private WT source = null;

    /**
     * Creates a new abstract event with the passed parameters.
     *
     * @param eventType The type of the event.
     * @param object The object managed by the editor frame.
     * @param source The editor frame which causes the event.
     * @throws IllegalArgumentException Passing a null pointer as object to edit or type. 
     *
     * @changed OLI 30.05.2013 - Added.
     */
    public EditorFrameEvent(EditorFrameEventType eventType, T object, WT source) {
        super(eventType);
        ensure(object != null, "person cannot be null.");
        ensure(source != null, "source cannot be null.");
        this.object = object;
        this.source = source;
    }

    /**
     * Returns the object which is the source of the event.
     *
     * @return The object which is the source of the event.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    public T getEditedObject() {
        return this.object;
    }

    /**
     * Returns the editor frame which caused the event.
     *
     * @return The editor frame which caused the event.
     *
     * @changed OLI 30.05.2013 - Added.
     */
    public WT getSource() {
        return this.source;
    }

}