/*
 * STFAdditionalSQLCodeWriter.java
 *
 * 26.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.legacy.scheme.stf.handler.STFAdditionalSQLCodeHandler;
import archimedes.model.DataModel;
import corent.files.StructuredTextFile;

/**
 * A writer for additional SQL code to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 26.04.2013 - Added.
 */

public class STFAdditionalSQLCodeWriter extends STFAdditionalSQLCodeHandler {

	private static final Logger LOG = Logger.getLogger(STFAdditionalSQLCodeWriter.class);

	/**
	 * Writes the passed additional SQL code to the STF.
	 * 
	 * @param stf
	 *            The STF which is to update with the additional SQL code.
	 * @param model
	 *            The model whose additional SQL code fragments are to store.
	 * 
	 * @changed OLI 26.04.2013 - Added.
	 */
	public void write(StructuredTextFile stf, DataModel model) {
		stf.writeStr(this.createPath(SQL_CODE, ADDITIONAL_SQL_CODE_POST_CHANGING), toHTML(model
				.getAdditionalSQLCodePostChangingCode()));
		stf.writeStr(this.createPath(SQL_CODE, ADDITIONAL_SQL_CODE_POST_REDUCING), toHTML(model
				.getAdditionalSQLCodePostReducingCode()));
		stf.writeStr(this.createPath(SQL_CODE, ADDITIONAL_SQL_CODE_PRE_CHANGING), toHTML(model
				.getAdditionalSQLCodePreChangingCode()));
		stf.writeStr(this.createPath(SQL_CODE, ADDITIONAL_SQL_CODE_PRE_EXTENDING), toHTML(model
				.getAdditionalSQLCodePreExtendingCode()));
		LOG.debug("additional SQL code written.");
	}

}