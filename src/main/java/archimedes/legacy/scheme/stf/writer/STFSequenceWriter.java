/*
 * STFSequenceWriter.java
 *
 * 23.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.scheme.stf.handler.STFSequenceHandler;
import archimedes.model.SequenceModel;
import corent.files.StructuredTextFile;

/**
 * A writer for sequences to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 23.04.2013 - Added.
 */

public class STFSequenceWriter extends STFSequenceHandler {

	private static final Logger LOG = Logger.getLogger(STFSequenceWriter.class);

	/**
	 * Writes the passed sequences to the STF.
	 * 
	 * @param sequences
	 *            The sequences which are to store in the passed STF.
	 * @param stf
	 *            The STF which is to update with the sequence data.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public void write(StructuredTextFile stf, SequenceModel[] sequences, DiagramSaveMode dsm) {
		stf.writeLong(this.createPath(COUNT), sequences.length);
		for (int i = 0; i < sequences.length; i++) {
			stf.writeStr(this.createPath(i, NAME), toHTML(sequences[i].getName()));
			if (dsm == DiagramSaveMode.REGULAR) {
				stf.writeStr(this.createPath(i, COMMENT), toHTML(sequences[i].getComment()));
				stf.writeStr(this.createPath(i, HISTORY), toHTML(sequences[i].getHistory()));
			}
			stf.writeLong(this.createPath(i, INCREMENT), sequences[i].getIncrement());
			stf.writeLong(this.createPath(i, START_VALUE), sequences[i].getStartValue());
			LOG.debug("sequence written: " + sequences[i].getName());
		}
	}

}