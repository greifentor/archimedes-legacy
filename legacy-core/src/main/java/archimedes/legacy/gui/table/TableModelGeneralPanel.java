/*
 * TableModelGeneralPanel.java
 *
 * 17.10.2017
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.table;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionListener;

import archimedes.legacy.Archimedes;
import archimedes.model.TableModel;
import baccara.gui.GUIBundle;
import baccara.gui.generics.BaccaraEditorPanel;
import baccara.gui.generics.ComponentData;
import baccara.gui.generics.Type;
import corent.gui.ExtendedColor;

/**
 * A panel for the general table model data like the table name and comment.
 * 
 * @author ollie
 * 
 * @changed OLI 17.10.2017 - Added.
 */

public class TableModelGeneralPanel extends BaccaraEditorPanel<String, TableModel> implements
		DataInputPanel<TableModel> {

	private static final String ID_BACKGROUNDCOLOR = "BackgroundColor";
	private static final String ID_DRAFT = "Draft";
	private static final String ID_DEPRECATED = "Deprecated";
	private static final String ID_EXTERNALTABLE = "ExternalTable";
	private static final String ID_MANYTOMANYRELATION = "ManyToManyRelation";
	private static final String ID_NAME = "Name";
	private static final String ID_TEXTCOLOR = "TextColor";

	/**
	 * Creates a new editor panel for a Baccara application with the passed
	 * parameters.
	 * 
	 * @param guiBundle
	 *            A bundle with GUI information.
	 * @param parent
	 *            A reference to the component which the panel is a member of
	 *            (usually the window or frame which contains the panel).
	 * @param resourcePrefix
	 *            A prefix for the resources of the panel.
	 * @param actionListener
	 *            An action listener to get a chance of reaction on combo box
	 *            events.
	 * @param t
	 *            The object which is edited by the panel.
	 * 
	 * @changed OLI 17.10.2017 - Added.
	 */
	@SuppressWarnings("unchecked")
	public TableModelGeneralPanel(GUIBundle guiBundle, Container parent, String resourcePrefix,
			ActionListener actionListener, TableModel t) {
		super(guiBundle, parent, resourcePrefix, actionListener, t, new ComponentData<String>(ID_NAME, Type.STRING, t
				.getName()), new ComponentData<String>(ID_DEPRECATED, Type.BOOLEAN, t.isDeprecated()),
				new ComponentData<String>(ID_TEXTCOLOR, Archimedes.PALETTE.getColors(), t.getFontColor(), false),
				new ComponentData<String>(ID_BACKGROUNDCOLOR, Archimedes.PALETTE.getColors(), t.getBackgroundColor(),
						false),
				new ComponentData<String>(ID_MANYTOMANYRELATION, Type.BOOLEAN, t.isManyToManyRelation()),
				new ComponentData<String>(ID_DRAFT, Type.BOOLEAN, t.isDraft()), new ComponentData<String>(
						ID_EXTERNALTABLE, Type.BOOLEAN, t.isExternalTable()));
	}

	/**
	 * @changed OLI 17.10.2017 - Added.
	 */
	@Override
	public void setAttribute(TableModel t, ComponentData<String> cd, Object guiValue) {
		if (ID_BACKGROUNDCOLOR.equals(cd.getName())) {
			t.setBackgroundColor((ExtendedColor) guiValue);
		} else if (ID_DEPRECATED.equals(cd.getName())) {
			t.setDeprecated(Boolean.TRUE.equals((Boolean) guiValue) ? true : false);
		} else if (ID_DRAFT.equals(cd.getName())) {
			t.setDraft(Boolean.TRUE.equals((Boolean) guiValue) ? true : false);
		} else if (ID_EXTERNALTABLE.equals(cd.getName())) {
			t.setExternalTable(Boolean.TRUE.equals((Boolean) guiValue) ? true : false);
		} else if (ID_MANYTOMANYRELATION.equals(cd.getName())) {
			t.setManyToManyRelation(Boolean.TRUE.equals((Boolean) guiValue) ? true : false);
		} else if (ID_NAME.equals(cd.getName())) {
			t.setName((String) guiValue);
		} else if (ID_TEXTCOLOR.equals(cd.getName())) {
			t.setFontColor((ExtendedColor) guiValue);
		}
	}

	/**
	 * @changed OLI 17.10.2017 - Added.
	 */
	@Override
	public Component[] getDataInputComponents() {
		return new Component[] { this.getComponent(ID_NAME), this.getComponent(ID_DEPRECATED),
				this.getComponent(ID_TEXTCOLOR), this.getComponent(ID_BACKGROUNDCOLOR),
				this.getComponent(ID_MANYTOMANYRELATION), this.getComponent(ID_DRAFT),
				this.getComponent(ID_EXTERNALTABLE) };
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public void onClosed() {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 17.10.2017 - Added.
	 */
	@Override
	public void transferData(TableModel o) {
		super.transferChanges(o);
	}

}