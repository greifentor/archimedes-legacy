/*
 * STFStereotypeReader.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.StereotypeModel;
import archimedes.legacy.scheme.stf.handler.STFStereotypeHandler;
import corent.files.StructuredTextFile;

/**
 * A reader for stereotypes from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 26.04.2013 - Added.
 */

public class STFStereotypeReader extends STFStereotypeHandler {

	/**
	 * Updates the stereotypes in the passed data model by the information
	 * stored in the STF.
	 * 
	 * @param stf
	 *            The STF whose stereotypes should be read to the diagram.
	 * @param model
	 *            The diagram model which is to fill with the stereotypes.
	 * 
	 * @changed OLI 26.04.2013 - Added.
	 */
	public void read(StructuredTextFile stf, DiagrammModel model) {
		int len = (int) stf.readLong(this.createPath(COUNT), 0);
		for (int i = 0; i < len; i++) {
			String n = fromHTML(stf.readStr(this.createPath(i, NAME), null));
			String c = fromHTML(stf.readStr(this.createPath(i, COMMENT), ""));
			String history = fromHTML(stf.readStr(this.createPath(i, HISTORY), ""));
			StereotypeModel stereotype = Archimedes.Factory.createStereotype(n, c);
			stereotype.setDoNotPrint(new Boolean(stf.readStr(this.createPath(i, DO_NOT_PRINT), "FALSE").toString())
					.booleanValue());
			stereotype.setHideTable(new Boolean(stf.readStr(this.createPath(i, HIDE_TABLE), "FALSE").toString())
					.booleanValue());
			stereotype.setHistory(history);
			model.addStereotype(stereotype);
		}
	}

}