/*
 * BeschreibungsSubEditorFactory.java
 *
 * 24.02.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.Component;
import java.util.Hashtable;

import archimedes.legacy.Archimedes;
import archimedes.model.CommentOwner;
import corent.base.Attributed;
import corent.djinn.SubEditor;
import corent.djinn.SubEditorFactory;

/**
 * Die Factory zum BeschreibungsSubEditor.
 * 
 * @author ollie
 * 
 */

public class CommentSubEditorFactory implements SubEditorFactory {

	public CommentSubEditorFactory() {
		super();
	}

	public SubEditor createSubEditor(Component owner, Attributed obj, Hashtable<String, Component> components) {
		if (!(obj instanceof CommentOwner)) {
			throw new IllegalArgumentException(obj.getClass() + " is not a valid parameter for "
					+ "CommentSubEditorFactory");
		}
		return new CommentSubEditor((CommentOwner) obj, Archimedes.guiBundle);
	}

}
