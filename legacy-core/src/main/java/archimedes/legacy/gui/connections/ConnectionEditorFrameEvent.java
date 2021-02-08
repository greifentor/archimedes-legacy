/*
 * ConnectionEditorFrameEvent.java
 *
 * 22.01.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.connections;

import archimedes.connections.DatabaseConnection;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;

/**
 * A frame event for the database connection frame for Archimedes.
 * 
 * @author ollie
 * 
 * @changed OLI 22.01.2015 - Added.
 */

public class ConnectionEditorFrameEvent extends EditorFrameEvent<DatabaseConnection, ConnectionEditorFrame> {

	private ConnectionEditorFrameType editorFrameType = null;

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
	 * @param editorFrameType
	 *            The type of the editor frame.
	 */
	public ConnectionEditorFrameEvent(EditorFrameEventType eventType, DatabaseConnection object,
			ConnectionEditorFrame source, ConnectionEditorFrameType editorFrameType) {
		super(eventType, object, source);
		this.editorFrameType = editorFrameType;
	}

	/**
	 * Returns the editor frame type.
	 * 
	 * @return The editor frame type.
	 * 
	 * @changed OLI 30.01.2015 - Added.
	 */
	public ConnectionEditorFrameType getConnectionEditorFrameType() {
		return this.editorFrameType;
	}

}