/*
 * ArchimedesStatusBarRenderer.java
 *
 * 22.05.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.model.gui.GUIObjectModel;
import archimedes.legacy.model.gui.GUIRelationModel;
import archimedes.legacy.scheme.Relation;

/**
 * An implementation for a status bar renderer for Archimedes data scheme
 * diagrams.
 * 
 * @author ollie
 * 
 * @changed OLI 22.05.2013 - Added.
 */

public class ArchimedesStatusBarRenderer implements StatusBarRenderer {

	/**
	 * @changed OLI 22.05.2013 - Added.
	 */
	@Override
	public String renderObject(GUIObjectModel guiObjectModel) {
		return guiObjectModel.getName();
	}

	/**
	 * @changed OLI 22.05.2013 - Added.
	 */
	@Override
	public String renderRelation(GUIRelationModel guiRelationModel) {
		Relation r = (Relation) guiRelationModel;
		TabellenspaltenModel ts0 = r.getReferencer();
		TabellenModel t0 = (ts0 != null ? ts0.getTabelle() : null);
		TabellenspaltenModel ts1 = r.getReferenced();
		TabellenModel t1 = (ts1 != null ? ts1.getTabelle() : null);
		return (t0 != null ? t0.getName() : "<null>") + " -> " + (t1 != null ? t1.getName() : "<null>");
	}

}