package archimedes.legacy.importer.jdbc;

import javax.swing.JFrame;

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
		return new ComponentData<?>[] {
				new ComponentData<Object>(JDBCImportConnectionData.FIELD_DRIVER_NAME, baccara.gui.generics.Type.STRING,
						data.getDriverName()),
				new ComponentData<Object>(JDBCImportConnectionData.FIELD_URL, baccara.gui.generics.Type.STRING,
						data.getUrl()),
				new ComponentData<Object>(JDBCImportConnectionData.FIELD_USER_NAME, baccara.gui.generics.Type.STRING,
						data.getUserName()),
				new ComponentData<Object>(JDBCImportConnectionData.FIELD_PASSWORD, baccara.gui.generics.Type.STRING,
						data.getPassword()) };
	}

	@Override
	protected String getMainResourceIdentifierPrefix() {
		return "JDBCImportManagerConfigurationDialog";
	}

	@Override
	protected void transferChangesToObject() {
		this.object.setDriverName(this.getTextFromComponent(JDBCImportConnectionData.FIELD_DRIVER_NAME));
		this.object.setPassword(this.getTextFromComponent(JDBCImportConnectionData.FIELD_PASSWORD));
		this.object.setUrl(this.getTextFromComponent(JDBCImportConnectionData.FIELD_URL));
		this.object.setUserName(this.getTextFromComponent(JDBCImportConnectionData.FIELD_USER_NAME));
	}

}