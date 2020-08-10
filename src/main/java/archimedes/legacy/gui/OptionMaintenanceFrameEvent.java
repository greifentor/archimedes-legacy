/*
 * OptionMaintenanceFrameEvent.java
 *
 * 15.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import archimedes.legacy.model.OptionModel;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;

/**
 * An event for option maintenance frames.
 * 
 * @author ollie
 * 
 * @changed OLI 15.10.2013 - Added.
 */

public class OptionMaintenanceFrameEvent extends EditorFrameEvent<OptionModel, OptionMaintenanceFrame> {

	/**
	 * Creates a new option maintenance event with the passed parameters.
	 * 
	 * @param eventType
	 *            The type of the event.
	 * @param option
	 *            The option managed by the option maintenance frame.
	 * @param source
	 *            The option maintenance frame which causes the event.
	 * @throws IllegalArgumentException
	 *             Passing a null pointer as option or type.
	 * 
	 * @changed OLI 15.10.2013 - Added.
	 */
	public OptionMaintenanceFrameEvent(EditorFrameEventType eventType, OptionModel option, OptionMaintenanceFrame source) {
		super(eventType, option, source);
	}

}