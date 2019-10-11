/*
 * BaseConfigurationFrameEvent.java
 *
 * 24.11.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.configuration;


import baccara.gui.generics.*;


/**
 * A frame event for the base configuration frame for Archimedes.
 *
 * @author ollie
 *
 * @changed OLI 24.11.2014 - Added.
 */

public class BaseConfigurationFrameEvent
        extends EditorFrameEvent<BaseConfiguration, BaseConfigurationFrame> {

    /**
     * Creates a new event with the passed parameters.
     *
     * @param eventType The type of the event.
     * @param object The object which is the cause of the event.
     * @param source The base configuration frame which is the source of the event.
     */
    public BaseConfigurationFrameEvent(EditorFrameEventType eventType,
            BaseConfiguration object, BaseConfigurationFrame source) {
        super(eventType, object, source);
    }

}