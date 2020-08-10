/*
 * ConnectFrameEvent.java
 *
 * 02.02.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.connections;

import archimedes.legacy.gui.DatabaseConnectionRecord;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;

/**
 * An event for getting thrown by the connect frame.
 * 
 * @author ollie
 * 
 * @changed OLI 02.02.2015 - Added.
 */

public class ConnectFrameEvent extends EditorFrameEvent<DatabaseConnectionRecord, ConnectFrame> {

	/**
	 * Creates a new event with the passed parameters.
	 * 
	 * @param eventType
	 *            The type of the event.
	 * @param object
	 *            The object which is the cause of the event.
	 * @param source
	 *            The database connection frame which is the source of the
	 *            event.
	 */
	public ConnectFrameEvent(EditorFrameEventType eventType, DatabaseConnectionRecord object, ConnectFrame source) {
		super(eventType, object, source);
	}

}