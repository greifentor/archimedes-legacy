/*
 * VersionIncrementer.java
 *
 * 28.04.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.util;

import archimedes.model.DataModel;
import corent.dates.PDate;

/**
 * A class which is able to increment the version number of an diagram model.
 * 
 * @author ollie
 * 
 * @changed OLI 28.04.2014 - Added.
 */

public class VersionIncrementer {

	/** Return codes for the increment operation. */
	public enum State {
		/** Increment cleaned up the SQL scripts only. */
		SCRIPTS_CLEANED_ONLY,
		/** Increment successful. */
		SUCCESS;
	}

	/**
	 * Increments the version number and cleans up the SQL scripts for passed
	 * data model.
	 * 
	 * @param model
	 *            The data model whose version number is to increment.
	 * @return The state of success for the increment operation.
	 * 
	 * @changed OLI 28.04.2014 - Added.
	 */
	public State increment(DataModel model) {
		State result = State.SUCCESS;
		try {
			int i = Integer.valueOf(model.getVersion());
			model.setVersion(String.valueOf(i + 1));
		} catch (NumberFormatException nfe) {
			result = State.SCRIPTS_CLEANED_ONLY;
		}
		model.setAdditionalSQLCodePostChangingCode("");
		model.setAdditionalSQLCodePostReducingCode("");
		model.setAdditionalSQLCodePreChangingCode("");
		model.setAdditionalSQLCodePreExtendingCode("");
		model.setVersionDate(new PDate());
		return result;
	}

}