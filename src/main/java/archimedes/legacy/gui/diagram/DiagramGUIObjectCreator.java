/*
 * DiagramGUIObjectCreator.java
 *
 * 14.05.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.diagram;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.HistoryOwner;
import archimedes.legacy.model.HistoryOwnerUtil;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.ViewModel;
import archimedes.legacy.model.gui.GUIObjectModel;
import archimedes.legacy.model.gui.GUIViewModel;

/**
 * An implementation of the <CODE>GUIObjectCreator</CODE> interface.
 * 
 * @author ollie
 * 
 * @changed OLI 14.05.2013 - Added.
 */

public class DiagramGUIObjectCreator implements GUIObjectCreator<GUIObjectTypes> {

	private DiagrammModel dm = null;

	/**
	 * Creates a new GUI object creator with the passed parameters.
	 * 
	 * @param dm
	 *            The diagram model which is to use by the creator.
	 * 
	 * @changed OLI 14.05.2013 - Added.
	 */
	public DiagramGUIObjectCreator(DiagrammModel dm) {
		super();
		this.dm = dm;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public GUIObjectModel create(GUIViewModel view, int x, int y, GUIObjectTypes type, boolean filled) {
		GUIObjectModel o = Archimedes.Factory.createTabelle((ViewModel) view, x, y, dm, filled);
		if (o instanceof HistoryOwner) {
			HistoryOwnerUtil.addChangedTag((HistoryOwner) o, "Added");
		}
		return o;
	}

	/**
	 * @changed OLI 14.05.2013 - Added.
	 */
	@Override
	public GUIObjectModel create(GUIViewModel view, int x, int y, GUIObjectTypes type, String s) {
		TabellenModel tm = Archimedes.Factory.createTabelle((ViewModel) view, x, y, dm, s);
		tm.setFirstGenerationDone(false);
		HistoryOwnerUtil.addChangedTag(tm, "Added");
		return tm;
	}

	/**
	 * Sets the passed diagram as new diagram for the creator.
	 * 
	 * @param diagram
	 *            The new diagram for the creator.
	 * 
	 * @changed OLI 22.05.2013 - Added.
	 */
	public void setDiagram(DiagrammModel diagram) {
		this.dm = diagram;
	}

}