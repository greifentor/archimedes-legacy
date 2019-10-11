/*
 * STFNReferenceWriter.java
 *
 * 03.05.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.scheme.stf.handler.STFNReferenceHandler;
import archimedes.model.NReferenceModel;
import archimedes.model.TableModel;
import corent.files.StructuredTextFile;

/**
 * A writer for nReferences to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 03.05.2013 - Added.
 */

public class STFNReferenceWriter extends STFNReferenceHandler {

	private static final Logger LOG = Logger.getLogger(STFNReferenceWriter.class);

	/**
	 * Writes the passed nReferences to the STF.
	 * 
	 * @param nReferences
	 *            The nReferences which are to store in the passed STF.
	 * @param stf
	 *            The STF which is to update with the nReference data.
	 * 
	 * @changed OLI 03.05.2013 - Added.
	 */
	public void write(StructuredTextFile stf, int i, TableModel table) {
		NReferenzModel[] nReferences = (NReferenzModel[]) table.getNReferences();
		stf.writeLong(this.createPath(i, COUNT), nReferences.length);
		for (int j = 0; j < nReferences.length; j++) {
			NReferenceModel nrm = nReferences[j];
			stf.writeStr(this.createPathForNReference(i, j, ALTERABLE), new Boolean(nrm.isAlterable()).toString());
			stf.writeStr(this.createPathForNReference(i, j, DELETE_CONFIRMATION_REQUIRED), new Boolean(nrm
					.isDeleteConfirmationRequired()).toString());
			stf.writeStr(this.createPathForNReference(i, j, EXTENSIBLE), new Boolean(nrm.isExtensible()).toString());
			stf.writeStr(this.createPathForNReference(i, j, COLUMN), nrm.getColumn().getName());
			stf.writeLong(this.createPathForNReference(i, j, ID), nrm.getId());
			stf.writeLong(this.createPathForNReference(i, j, PANEL), nrm.getPanel().getPanelNumber());
			stf.writeStr(this.createPathForNReference(i, j, PANEL_TYPE), nrm.getNReferencePanelType().toString());
			stf.writeStr(this.createPathForNReference(i, j, PERMIT_CREATE), new Boolean(nrm.isPermitCreate())
					.toString());
			stf.writeStr(this.createPathForNReference(i, j, TABLE), nrm.getColumn().getTable().getName());
			LOG.debug("n-reference written: " + nrm.getPanel().getPanelNumber());
		}
	}

}