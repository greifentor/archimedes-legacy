/*
 * STFStereotypeWriter.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.scheme.stf.handler.STFStereotypeHandler;
import archimedes.model.StereotypeModel;
import corent.files.StructuredTextFile;

/**
 * A writer for stereotypes to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 26.04.2013 - Added.
 */

public class STFStereotypeWriter extends STFStereotypeHandler {

	private static final Logger LOG = Logger.getLogger(STFStereotypeWriter.class);

	/**
	 * Writes the passed stereotypes to the STF.
	 * 
	 * @param stereotypes
	 *            The stereotypes which are to store in the passed STF.
	 * @param stf
	 *            The STF which is to update with the stereotype data.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public void write(StructuredTextFile stf, StereotypeModel[] stereotypes, DiagramSaveMode dsm) {
		stf.writeLong(this.createPath(COUNT), stereotypes.length);
		for (int i = 0; i < stereotypes.length; i++) {
			stf.writeStr(this.createPath(i, NAME), toHTML(stereotypes[i].getName()));
			if (dsm == DiagramSaveMode.REGULAR) {
				stf.writeStr(this.createPath(i, COMMENT), toHTML(stereotypes[i].getComment()));
				stf.writeStr(this.createPath(i, DO_NOT_PRINT), new Boolean(stereotypes[i].isDoNotPrint()).toString());
				stf.writeStr(this.createPath(i, HIDE_TABLE), new Boolean(stereotypes[i].isHideTable()).toString());
				stf.writeStr(this.createPath(i, HISTORY), toHTML(stereotypes[i].getHistory()));
			}
			LOG.debug("stereotype written: " + stereotypes[i].getName());
		}
	}

}