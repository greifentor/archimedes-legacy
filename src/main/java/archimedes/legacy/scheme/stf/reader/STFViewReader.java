/*
 * STFViewReader.java
 *
 * 08.05.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.ViewModel;
import archimedes.legacy.model.gui.GUIViewModel;
import archimedes.legacy.scheme.stf.handler.STFViewHandler;
import corent.files.StructuredTextFile;

/**
 * A reader for views from a STF. The tables which are connected to the views
 * will not be restored. The tables link them self to the views which are
 * connected to while reading the tables in a specific reader.
 * 
 * @author ollie
 * 
 * @changed OLI 08.05.2013 - Added.
 */

public class STFViewReader extends STFViewHandler {

	/**
	 * Updates the views in the passed data model by the information stored in
	 * the STF.
	 * 
	 * @param stf
	 *            The STF whose views should be read to the diagram.
	 * @param model
	 *            The diagram model which is to fill with the views.
	 * @return The main view if found one, <CODE>null</CODE> otherwise.
	 * 
	 * @changed OLI 08.05.2013 - Added.
	 */
	public ViewModel read(StructuredTextFile stf, DiagrammModel model) {
		ViewModel vm = null;
		ViewModel mvm = null;
		int len = (int) stf.readLong(createRootPath(VIEWS, COUNT), 0);
		for (int i = 0; i < len; i++) {
			if (i == 0) {
				vm = Archimedes.Factory.createMainView("Gesamt", "Gesamtansicht", model.isShowReferencedColumns());
				mvm = vm;
			} else {
				vm = Archimedes.Factory.createView();
			}
			String viewname = fromHTML(stf.readStr(this.createPath(i, NAME), ""));
			String beschreibung = fromHTML(stf.readStr(this.createPath(i, DESCRIPTION), ""));
			vm.setShowReferencedColumns(new Boolean(stf.readStr(this.createPath(i, SHOW_REFERENCED_COLUMNS),
					new Boolean(model.isShowReferencedColumns()).toString())).booleanValue());
			vm.setHideTechnicalFields(new Boolean(stf.readStr(this.createPath(i, HIDE_TECHNICAL_COLUMNS), "FALSE")));
			vm.setName(viewname);
			vm.setComment(beschreibung);
			model.getViews().add((GUIViewModel) vm);
		}
		if (model.getViews().size() == 0) {
			vm = Archimedes.Factory.createMainView("Gesamt", "-", model.isShowReferencedColumns());
			mvm = vm;
			model.getViews().add((GUIViewModel) vm);
		}
		return mvm;
	}

}