/*
 * ConnectionEditorFrame.java
 *
 * 22.01.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.connections;

import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import archimedes.connections.DatabaseConnection;
import baccara.gui.GUIBundle;
import baccara.gui.generics.AbstractEditorFrame;
import baccara.gui.generics.ComponentData;
import baccara.gui.generics.EditorFrameConfiguration;
import baccara.gui.generics.EditorFrameEventType;
import corent.db.DBExecMode;

/**
 * A frame to modify a database connection.
 * 
 * @author ollie
 * 
 * @changed OLI 22.01.2015 - Added.
 */

public class ConnectionEditorFrame extends
		AbstractEditorFrame<DatabaseConnection, ConnectionEditorFrame, ConnectionEditorFrameEvent, String> {

	private static final String ID_DB_MODE = "dbMode";
	private static final String ID_DRIVER = "driver";
	private static final String ID_NAME = "name";
	private static final String ID_QUOTE = "quote";
	private static final String ID_SET_DOMAINS = "setDomains";
	private static final String ID_SET_NOT_NULL = "setNotNull";
	private static final String ID_SET_REFERENCES = "setReferences";
	private static final String ID_URL = "url";
	private static final String ID_USER_NAME = "userName";

	private ConnectionEditorFrameType editorFrameType = null;

	/**
	 * Creates a new frame to modify a database connection for Archimedes.
	 * 
	 * @param object
	 *            The object to maintain in the dialog.
	 * @param guiBundle
	 *            A GUI bundle with additional GUI information.
	 * @param editorFrameType
	 *            The type of the editor frame.
	 */
	public ConnectionEditorFrame(DatabaseConnection object, GUIBundle guiBundle,
			ConnectionEditorFrameType editorFrameType) {
		super(object, "", guiBundle, new EditorFrameConfiguration(true, false, true));
		this.setTitle(guiBundle.getResourceText(this.getMainResourceIdentifierPrefix() + ".title"));
		this.editorFrameType = editorFrameType;
	}

	/**
	 * @changed OLI 24.11.2014 - Added.
	 */
	@Override
	public ConnectionEditorFrameEvent createEvent(EditorFrameEventType type) {
		return new ConnectionEditorFrameEvent(type, this.object, this, this.editorFrameType);
	}

	/**
	 * @changed OLI 24.11.2014 - Added.
	 */
	@Override
	protected ComponentData[] getComponentData(DatabaseConnection bc) {
		baccara.gui.generics.Type type = baccara.gui.generics.Type.STRING;
		return new ComponentData[] { new ComponentData(ID_NAME, baccara.gui.generics.Type.STRING, bc.getName()),
				new ComponentData(ID_DRIVER, baccara.gui.generics.Type.STRING, bc.getDriver()),
				new ComponentData(ID_URL, baccara.gui.generics.Type.STRING, bc.getUrl()),
				new ComponentData(ID_USER_NAME, baccara.gui.generics.Type.STRING, bc.getUserName()),
				new ComponentData(ID_DB_MODE, Arrays.asList(DBExecMode.values()), bc.getDBMode(), false),
				new ComponentData(ID_SET_DOMAINS, baccara.gui.generics.Type.BOOLEAN, bc.isSetDomains()),
				new ComponentData(ID_SET_NOT_NULL, baccara.gui.generics.Type.BOOLEAN, bc.isSetNotNull()),
				new ComponentData(ID_SET_REFERENCES, baccara.gui.generics.Type.BOOLEAN, bc.isSetReferences()),
				new ComponentData(ID_QUOTE, baccara.gui.generics.Type.STRING, bc.getQuote()) };
	}

	/**
	 * @changed OLI 24.11.2014 - Added.
	 */
	@Override
	protected String getMainResourceIdentifierPrefix() {
		return "archimedes.ConnectionEditorFrame";
	}

	/**
	 * @changed OLI 24.11.2014 - Added.
	 */
	@Override
	protected void transferChangesToObject() {
		this.object.setDBMode(this.getDBExecModeFromComponent(ID_DB_MODE));
		this.object.setDriver(this.getTextFromComponent(ID_DRIVER));
		this.object.setName(this.getTextFromComponent(ID_NAME));
		this.object.setQuote(this.getTextFromComponent(ID_QUOTE));
		this.object.setSetDomains(this.isCheckBoxSet(ID_SET_DOMAINS));
		this.object.setSetNotNull(this.isCheckBoxSet(ID_SET_NOT_NULL));
		this.object.setSetReferences(this.isCheckBoxSet(ID_SET_REFERENCES));
		this.object.setUrl(this.getTextFromComponent(ID_URL));
		this.object.setUserName(this.getTextFromComponent(ID_USER_NAME));
	}

	private DBExecMode getDBExecModeFromComponent(String name) {
		return (DBExecMode) ((JComboBox) this.getEditorComponent(name)).getSelectedItem();
	}

	private boolean isCheckBoxSet(String name) {
		return ((JCheckBox) this.getEditorComponent(name)).isSelected();
	}

}