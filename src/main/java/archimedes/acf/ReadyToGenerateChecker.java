/*
 * ReadyToGenerateChecker.java
 *
 * 09.06.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import archimedes.legacy.model.DataModel;

/**
 * This interface is used to detect if a data model is ready to generate.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 09.06.2017 - Added.
 */

public interface ReadyToGenerateChecker {

	/**
	 * Checks if the passed data model is ready to generate.
	 *
	 * @param dataModel The data model which is check for being ready to generate.
	 * @return <CODE>true</CODE> if the data model is ready to generate code.
	 *
	 * @changed OLI 09.06.2017 - Added.
	 */
	abstract public boolean isReadyToGenerate(DataModel dataModel);

}