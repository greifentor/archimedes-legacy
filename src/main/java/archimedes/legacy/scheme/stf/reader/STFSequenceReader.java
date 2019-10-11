/*
 * STFSequenceReader.java
 *
 * 23.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import java.sql.Types;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.stf.handler.STFSequenceHandler;
import archimedes.model.SequenceModel;
import corent.files.StructuredTextFile;

/**
 * A reader for sequences from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 23.04.2013 - Added.
 */

public class STFSequenceReader extends STFSequenceHandler {

	/**
	 * Updates the sequences in the passed data model by the information stored
	 * in the STF.
	 * 
	 * @param stf
	 *            The STF whose sequences should be read to the diagram.
	 * @param model
	 *            The diagram model which is to fill with the sequences.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public void read(StructuredTextFile stf, DiagrammModel model) {
		int len = (int) stf.readLong(this.createPath(COUNT), 0);
		for (int i = 0; i < len; i++) {
			String n = fromHTML(stf.readStr(this.createPath(i, NAME), null));
			String c = fromHTML(stf.readStr(this.createPath(i, COMMENT), ""));
			long inc = stf.readLong(this.createPath(i, INCREMENT), Types.INTEGER);
			long start = stf.readLong(this.createPath(i, START_VALUE), 0);
			String history = fromHTML(stf.readStr(this.createPath(i, HISTORY), ""));
			SequenceModel sm = Archimedes.Factory.createSequence(n, inc, start);
			sm.setComment(c);
			sm.setHistory(history);
			model.addSequence(sm);
		}
	}

}