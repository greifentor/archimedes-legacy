package archimedes.legacy.importer.jdbc;

import java.awt.Color;
import java.awt.Component;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

import archimedes.connections.DatabaseConnection;
import baccara.gui.GUIBundle;
import baccara.gui.generics.AbstractEditorFrame;
import baccara.gui.generics.ComponentData;
import baccara.gui.generics.EditorFrameConfiguration;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;

/**
 * A dialog for JDBC import manager call configuration.
 *
 * @author ollie (11.10.2019)
 */
public class JDBCImportManagerConfigurationDialog extends
		AbstractEditorFrame<JDBCImportConnectionData, JFrame, EditorFrameEvent<JDBCImportConnectionData, JFrame>, String> {

	public JDBCImportManagerConfigurationDialog(JDBCImportConnectionData object, GUIBundle guiBundle) {
		super(object, "", guiBundle, new EditorFrameConfiguration(true, false, true));
		this.setTitle(guiBundle.getResourceText(this.getMainResourceIdentifierPrefix() + ".title"));
	}

	@Override
	public EditorFrameEvent<JDBCImportConnectionData, JFrame> createEvent(EditorFrameEventType type) {
		return new EditorFrameEvent<JDBCImportConnectionData, JFrame>(type, this.object, this);
	}

	@Override
	protected ComponentData<?>[] getComponentData(JDBCImportConnectionData data) {
		return new ComponentData<?>[] { //
				new ComponentData<Object>( //
						JDBCImportConnectionData.FIELD_CONNECTION, //
						Arrays.asList(data.getConnections()), //
						data.getConnection(), //
						new ConnectListCellRenderer(), //
						false),
				new ComponentData<String>( //
						JDBCImportConnectionData.FIELD_PASSWORD, //
						baccara.gui.generics.Type.STRING, //
						data.getPassword()), //
				new ComponentData<String>( //
						JDBCImportConnectionData.FIELD_IGNORE_INDICES, //
						baccara.gui.generics.Type.BOOLEAN, //
						data.isIgnoreIndices()), //
				new ComponentData<String>( //
						JDBCImportConnectionData.FIELD_SCHEMA, //
						baccara.gui.generics.Type.STRING, //
						data.getSchema()), //
				new ComponentData<String>( //
						JDBCImportConnectionData.FIELD_IGNORE_TABLES_PATTERNS, //
						baccara.gui.generics.Type.STRING, //
						data.getIgnoreTablePatterns()) //
		};
	}

	@Override
	protected String getMainResourceIdentifierPrefix() {
		return "JDBCImportManagerConfigurationDialog";
	}

	@Override
	protected void transferChangesToObject() {
		this.object.setIgnoreIndices(this.getBooleanFromComponent(JDBCImportConnectionData.FIELD_IGNORE_INDICES));
		this.object.setIgnoreTablePatterns(
				this.getTextFromComponent(JDBCImportConnectionData.FIELD_IGNORE_TABLES_PATTERNS));
		this.object.setConnection(this.getConnectionFromComponent(JDBCImportConnectionData.FIELD_CONNECTION));
		this.object.setPassword(this.getTextFromComponent(JDBCImportConnectionData.FIELD_PASSWORD));
		this.object.setSchema(this.getTextFromComponent(JDBCImportConnectionData.FIELD_SCHEMA));
	}

	private boolean getBooleanFromComponent(String name) {
		return ((JCheckBox) this.getEditorComponent(name)).isSelected();
	}

	private DatabaseConnection getConnectionFromComponent(String name) {
		return (DatabaseConnection) ((JComboBox<?>) this.getEditorComponent(name)).getSelectedItem();
	}

}

class ConnectListCellRenderer implements ListCellRenderer<Object> {

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