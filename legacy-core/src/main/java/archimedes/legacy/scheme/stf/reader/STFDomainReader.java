/*
 * STFDomainReader.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import java.sql.Types;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.stf.handler.STFDomainHandler;
import archimedes.model.DomainModel;
import corent.files.StructuredTextFile;

/**
 * A reader for domains from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 26.04.2013 - Added.
 */

public class STFDomainReader extends STFDomainHandler {

	/**
	 * Updates the domains in the passed data model by the information stored in
	 * the STF.
	 * 
	 * @param stf
	 *            The STF whose domains should be read to the diagram.
	 * @param model
	 *            The diagram model which is to fill with the domains.
	 * 
	 * @changed OLI 26.04.2013 - Added.
	 */
	public void read(StructuredTextFile stf, DiagrammModel model) {
		int len = (int) stf.readLong(this.createPath(COUNT), 0);
		for (int i = 0; i < len; i++) {
			String n = fromHTML(stf.readStr(this.createPath(i, NAME), null));
			String c = fromHTML(stf.readStr(this.createPath(i, COMMENT), ""));
			int dt = (int) stf.readLong(this.createPath(i, DATA_TYPE), Types.INTEGER);
			int l = (int) stf.readLong(this.createPath(i, LENGTH), 0);
			int nks = (int) stf.readLong(this.createPath(i, NKS), 0);
			String initialValue = fromHTML(stf.readStr(this.createPath(i, INITIAL_VALUE), ""));
			String history = fromHTML(stf.readStr(this.createPath(i, HISTORY), ""));
			String parameters = fromHTML(stf.readStr(this.createPath(i, PARAMETERS), ""));
			DomainModel dm = Archimedes.Factory.createDomain(n, dt, l, nks);
			dm.setComment(c);
			dm.setHistory(history);
			dm.setInitialValue(initialValue);
			dm.setParameters(parameters);
			model.addDomain(dm);
		}
	}

}