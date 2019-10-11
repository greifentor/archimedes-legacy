/*
 * STFOptionReader.java
 *
 * 15.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import archimedes.legacy.scheme.stf.handler.STFOptionHandler;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import corent.files.StructuredTextFile;

/**
 * A reader for options from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 15.10.2013 - Added.
 */

public class STFOptionReader extends STFOptionHandler {

	/**
	 * Updates the columns in the passed data model by the information stored in
	 * the STF.
	 * 
	 * @param stf
	 *            The STF whose columns should be read to the diagram.
	 * @param table
	 *            The diagram model which is to fill with the columns.
	 * 
	 * @changed OLI 15.10.2013 - Added.
	 */
	public void read(StructuredTextFile stf, TableModel table, int i) {
		long count = stf.readLong(this.createPathForOption(i, COUNT), 0);
		for (int option = 0; option < count; option++) {
			String name = fromHTML(stf.readStr(this.createPathForOption(i, OPTION + option, NAME), null));
			String parameter = fromHTML(stf.readStr(this.createPathForOption(i, OPTION + option, PARAMETER), ""));
			Option o = new Option(name, parameter);
			table.addOption(o);
		}
	}

}