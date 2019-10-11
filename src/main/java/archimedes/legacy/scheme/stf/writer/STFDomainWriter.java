/*
 * STFDomainWriter.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.scheme.stf.handler.STFDomainHandler;
import archimedes.model.DomainModel;
import corent.files.StructuredTextFile;

/**
 * A writer for domains to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 26.04.2013 - Added.
 */

public class STFDomainWriter extends STFDomainHandler {

	private static final Logger LOG = Logger.getLogger(STFDomainWriter.class);

	/**
	 * Writes the passed domains to the STF.
	 * 
	 * @param domains
	 *            The domains which are to store in the passed STF.
	 * @param stf
	 *            The STF which is to update with the domain data.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public void write(StructuredTextFile stf, DomainModel[] domains, DiagramSaveMode dsm) {
		stf.writeLong(this.createPath(COUNT), domains.length);
		for (int i = 0; i < domains.length; i++) {
			stf.writeStr(this.createPath(i, NAME), toHTML(domains[i].getName()));
			if (dsm == DiagramSaveMode.REGULAR) {
				stf.writeStr(this.createPath(i, COMMENT), toHTML(domains[i].getComment()));
				stf.writeStr(this.createPath(i, HISTORY), toHTML(domains[i].getHistory()));
			}
			stf.writeLong(this.createPath(i, DATA_TYPE), domains[i].getDataType());
			stf.writeLong(this.createPath(i, LENGTH), domains[i].getLength());
			stf.writeLong(this.createPath(i, NKS), domains[i].getDecimalPlace());
			stf.writeStr(this.createPath(i, INITIAL_VALUE), toHTML(domains[i].getInitialValue()));
			stf.writeStr(this.createPath(i, PARAMETERS), toHTML(domains[i].getParameters()));
			LOG.debug("domain written: " + domains[i].getName());
		}
	}

}