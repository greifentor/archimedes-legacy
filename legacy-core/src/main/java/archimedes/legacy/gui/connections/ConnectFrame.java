/*
 * ConnectFrame.java
 *
 * 02.02.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.connections;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import archimedes.connections.DatabaseConnection;
import archimedes.gui.DatabaseConnectionRecord;
import baccara.gui.GUIBundle;
import baccara.gui.generics.AbstractEditorFrame;
import baccara.gui.generics.ComponentData;
import baccara.gui.generics.EditorFrameConfiguration;
import baccara.gui.generics.EditorFrameEventType;

/**
 * A frame to connect to a database.
 * 
 * @author ollie
 * 
 * @changed OLI 02.02.2015 - Added.
 */

public class ConnectFrame extends
		AbstractEditorFrame<DatabaseConnectionRecord, ConnectFrame, ConnectFrameEvent, String> {

	private static final String ID_DATABASE_CONNECTION = "databaseConnection";
	private static final String ID_PASSWORD = "password";

	/**
	 * Creates a new frame to connect to a database.
	 * 
	 * @param object
	 *            The object to maintain in the dialog.
	 * @param guiBundle
	 *            A GUI bundle with additional GUI information.
	 * 
	 * @changed OLI 02.02.2015 - Added.
	 */
	public ConnectFrame(DatabaseConnectionRecord object, GUIBundle guiBundle) {
		super(object, "", guiBundle, new EditorFrameConfiguration(true, false, true));
		this.setTitle(guiBundle.getResourceText(this.getMainResourceIdentifierPrefix() + ".title"));
	}

	/**
	 * @changed OLI 02.02.2015 - Added.
	 */
	@Override
	public ConnectFrameEvent createEvent(EditorFrameEventType type) {
		return new ConnectFrameEvent(type, this.object, this);
	}

	/**
	 * @changed OLI 02.02.2015 - Added.
	 */
	@Override
	protected ComponentData<?>[] getComponentData(DatabaseConnectionRecord bcr) {
		return new ComponentData<?>[] {
				new ComponentData<Object>(ID_DATABASE_CONNECTION, Arrays.asList(bcr.getDatabaseConnections()), bcr
						.getDatabaseConnection(), new ConnectListCellRenderer(), false),
				new ComponentData<String>(ID_PASSWORD, baccara.gui.generics.Type.STRING, bcr.getPassword()) };
	}

	/**
	 * @changed OLI 02.02.2015 - Added.
	 */
	@Override
	protected String getMainResourceIdentifierPrefix() {
		return "archimedes.ConnectFrame";
	}

	/**
	 * @changed OLI 02.02.2015 - Added.
	 */
	@Override
	protected void transferChangesToObject() {
		this.object.setDatabaseConnection(this.getDatabaseConnectionFromComponent(ID_DATABASE_CONNECTION));
		this.object.setPassword(this.getTextFromComponent(ID_PASSWORD));
	}

	private DatabaseConnection getDatabaseConnectionFromComponent(String name) {
		return (DatabaseConnection) ((JComboBox<?>) this.getEditorComponent(name)).getSelectedItem();
	}

}

class ConnectListCellRenderer implements ListCellRenderer<Object> {

	/**
	 * @changed OLI 31.01.2015 - Added.
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel l = new JLabel("-");
		if (value instanceof DatabaseConnection) {
			DatabaseConnection dc = (DatabaseConnection) value;
			l = new JLabel(dc.getName() + " (" + dc.getDBMode() + ")");
		}
		if (isSelected) {
			l.setBackground(Color.LIGHT_GRAY);
			l.setOpaque(true);
		}
		return l;
	}

}