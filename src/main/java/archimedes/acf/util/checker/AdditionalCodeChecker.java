/*
 * AdditionalCodeChecker.java
 *
 * 04.09.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.util.checker;

import archimedes.acf.CodeGenerator;
import archimedes.acf.io.SourceFileReader;
import archimedes.acf.report.GenerationProcessReport;
import archimedes.legacy.model.TableModel;
import gengen.IndividualPreferences;

/**
 * An additional code checker which is executed after the code has been generated for the code generator and table. It
 * is only able to notify potential problems to the generation report.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.09.2015 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public interface AdditionalCodeChecker {

	/**
	 * Executes the additional code checker.
	 *
	 * @param report                The generation report which could be extended by checker notifications.
	 * @param fileName              The name of the code file.
	 * @param code                  The created code. This can be <CODE>null</CODE> if there is no code created.
	 * @param individualPreferences Some user preferences for the generator.
	 * @param sourceFileReader      An access to the source files.
	 * @param codeGenerator         The code generator which has created the passed code.
	 * @param tableModel            The table model which the code has been generated for.
	 *
	 * @changed OLI 04.09.2015 - Added.
	 */
	abstract public void execute(GenerationProcessReport report, String fileName, String code,
			IndividualPreferences individualPreferences, SourceFileReader sourceFileReader, CodeGenerator codeGenerator,
			TableModel tableModel);

}