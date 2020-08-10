/*
 * HistoryOwnerSubEditorFactory.java
 *
 * 01.11.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.Component;
import java.util.Hashtable;

import archimedes.legacy.model.HistoryOwner;
import corent.base.Attributed;
import corent.djinn.SubEditor;
import corent.djinn.SubEditorFactory;

/**
 * Die Factory zum HistoryOwnerSubEditor.
 * 
 * @author ollie
 * 
 * @changed OLI 01.11.2011 - Hinzugef&uuml;gt.
 */

public class HistoryOwnerSubEditorFactory implements SubEditorFactory {

	public HistoryOwnerSubEditorFactory() {
		super();
	}

	public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, Component> components) {
		if (!(obj instanceof HistoryOwner)) {
			throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
					+ "HistoryOwnerSubEditorFactory");
		}
		return new HistoryOwnerSubEditor((HistoryOwner) obj);
	}

}