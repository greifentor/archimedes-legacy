/*
 * BeschreibungsSubEditorFactory.java
 *
 * 24.02.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import archimedes.legacy.Archimedes;
import archimedes.model.CommentOwner;
import archimedes.model.DataModel;
import corent.base.Attributed;
import corent.djinn.SubEditor;
import corent.djinn.SubEditorFactory;

import java.awt.*;
import java.util.Hashtable;

/**
 * Die Factory zum ModelCheckerScriptSubEditor.
 * 
 * @author ollie
 * 
 */

public class ModelCheckerScriptSubEditorFactory implements SubEditorFactory {

	public ModelCheckerScriptSubEditorFactory() {
		super();
	}

	public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, Component> components) {
		if (!(obj instanceof DataModel)) {
			throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
					+ "ModelCheckerScriptSubEditorFactory");
		}
		return new ModelCheckScriptSubEditor((DataModel) obj, Archimedes.guiBundle);
	}

}
