/*
 * STFNReferenceReader.java
 *
 * 06.05.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import org.apache.log4j.Logger;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.scheme.stf.handler.STFNReferenceHandler;
import archimedes.model.NReferencePanelType;
import archimedes.model.TableModel;
import corent.files.StructuredTextFile;

/**
 * A reader for nReferences from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 06.05.2013 - Added.
 */

public class STFNReferenceReader extends STFNReferenceHandler {

	private static final Logger LOG = Logger.getLogger(STFNReferenceReader.class);

	/**
	 * Updates the relations in the passed data model by the information stored
	 * in the STF.
	 * 
	 * @param stf
	 *            The STF whose relations should be read to the diagram.
	 * @param table
	 *            The table where the nReference belongs to.
	 * 
	 * @changed OLI 06.05.2013 - Added.
	 */
	public void read(StructuredTextFile stf, TableModel table, int i) {
		int len = (int) stf.readLong(this.createPath(i, COUNT), 0);
		for (int j = 0; j < len; j++) {
			NReferenzModel nrm = Archimedes.Factory.createNReferenz((TabellenModel) table);
			nrm.setId((int) stf.readLong(this.createPathForNReference(i, j, ID), 0));
			String columnName = stf.readStr(this.createPathForNReference(i, j, COLUMN), "");
			String tableName = stf.readStr(this.createPathForNReference(i, j, TABLE), table.getName());
			try {
				nrm.setColumn(table.getDataModel().getTableByName(tableName).getColumnByName(columnName));
				nrm.setAlterable(new Boolean(stf.readStr(this.createPathForNReference(i, j, ALTERABLE), "FALSE"))
						.booleanValue());
				nrm.setDeleteConfirmationRequired(new Boolean(stf.readStr(this.createPathForNReference(i, j,
						DELETE_CONFIRMATION_REQUIRED), "FALSE")).booleanValue());
				nrm.setExtensible(new Boolean(stf.readStr(this.createPathForNReference(i, j, EXTENSIBLE), "FALSE"))
						.booleanValue());
				nrm
						.setPermitCreate(new Boolean(stf.readStr(this.createPathForNReference(i, j, PERMIT_CREATE),
								"FALSE")).booleanValue());
				nrm.setPanel(table.getPanelAt((int) stf.readLong(this.createPathForNReference(i, j, PANEL), 0)));
				nrm.setNReferencePanelType(NReferencePanelType.valueOf2(stf.readStr(this.createPathForNReference(i, j,
						PANEL_TYPE), "standard")));
				table.addNReference(nrm);
			} catch (NullPointerException npe) {
				LOG.error("error while reading n-reference: " + nrm.getId(), npe);
			}
		}
	}

}