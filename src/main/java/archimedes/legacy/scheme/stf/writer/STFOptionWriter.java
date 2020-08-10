/*
 * STFOptionWriter.java
 *
 * 15.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import archimedes.legacy.model.OptionModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.scheme.stf.handler.STFOptionHandler;
import corent.files.StructuredTextFile;

/**
 * A writer for table options to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 15.10.2013 - Added.
 */

public class STFOptionWriter extends STFOptionHandler {

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
	public void write(StructuredTextFile stf, TableModel table, int i) {
		stf.writeLong(this.createPathForOption(i, COUNT), table.getOptionCount());
		for (int j = 0, lenj = table.getOptionCount(); j < lenj; j++) {
			OptionModel option = table.getOptionAt(j);
			stf.writeStr(this.createPathForOption(i, OPTION + j, NAME), fromHTML(option.getName()));
			stf.writeStr(this.createPathForOption(i, OPTION + j, PARAMETER),
					fromHTML((option.getParameter() != null ? option.getParameter() : "")));
		}
	}

}