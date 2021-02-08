/*
 * BaseConfigurationFrame.java
 *
 * 24.11.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.configuration;

import baccara.gui.GUIBundle;
import baccara.gui.generics.AbstractEditorFrame;
import baccara.gui.generics.ComponentData;
import baccara.gui.generics.EditorFrameConfiguration;
import baccara.gui.generics.EditorFrameEventType;

/**
 * A frame to modify the base configuration of Archimedes.
 * 
 * @author ollie
 * 
 * @changed OLI 24.11.2014 - Added.
 */

public class BaseConfigurationFrame extends
		AbstractEditorFrame<BaseConfiguration, BaseConfigurationFrame, BaseConfigurationFrameEvent, String> {

	private static final String ID_DB_NAME = "dbName";
	private static final String ID_DB_SERVER_NAME = "dbServerName";
	private static final String ID_DB_USER_NAME = "dbUserName";
	private static final String ID_USER_COMPANY = "userCompany";
	private static final String ID_USER_LANGUAGE = "userLanguage";
	private static final String ID_USER_NAME = "userName";
	private static final String ID_USER_TOKEN = "userToken";

	/**
	 * Creates a new frame to modify a base configuration for Archimedes.
	 * 
	 * @param object
	 *            The object to maintain in the dialog.
	 * @param title
	 *            A title for the dialog.
	 * @param guiBundle
	 *            A GUI bundle with additional GUI information.
	 */
	public BaseConfigurationFrame(BaseConfiguration object, String title, GUIBundle guiBundle) {
		super(object, title, guiBundle, new EditorFrameConfiguration(true, false, true));
	}

	/**
	 * @changed OLI 24.11.2014 - Added.
	 */
	@Override
	public BaseConfigurationFrameEvent createEvent(EditorFrameEventType type) {
		return new BaseConfigurationFrameEvent(type, this.object, this);
	}

	/**
	 * @changed OLI 24.11.2014 - Added.
	 */
	@Override
	protected ComponentData<?>[] getComponentData(BaseConfiguration bc) {
		baccara.gui.generics.Type type = baccara.gui.generics.Type.STRING;
		return new ComponentData<?>[] { new ComponentData<String>(ID_USER_COMPANY, type, bc.getCompany()),
				new ComponentData<String>(ID_USER_NAME, type, bc.getUserName()),
				new ComponentData<String>(ID_USER_TOKEN, type, bc.getUserToken()),
				new ComponentData<String>(ID_USER_LANGUAGE, type, bc.getLanguage()),
				new ComponentData<String>(ID_DB_NAME, type, bc.getDBName()),
				new ComponentData<String>(ID_DB_SERVER_NAME, type, bc.getDBServerName()),
				new ComponentData<String>(ID_DB_USER_NAME, type, bc.getDBUserName()) };
	}

	/**
	 * @changed OLI 24.11.2014 - Added.
	 */
	@Override
	protected String getMainResourceIdentifierPrefix() {
		return "BaseConfigurationFrame";
	}

	/**
	 * @changed OLI 24.11.2014 - Added.
	 */
	@Override
	protected void transferChangesToObject() {
		this.object.setCompany(this.getTextFromComponent(ID_USER_COMPANY));
		this.object.setDBName(this.getTextFromComponent(ID_DB_NAME));
		this.object.setDBServerName(this.getTextFromComponent(ID_DB_SERVER_NAME));
		this.object.setDBUserName(this.getTextFromComponent(ID_DB_USER_NAME));
		this.object.setLanguage(this.getTextFromComponent(ID_USER_LANGUAGE));
		this.object.setUserName(this.getTextFromComponent(ID_USER_NAME));
		this.object.setUserToken(this.getTextFromComponent(ID_USER_TOKEN));
	}

}