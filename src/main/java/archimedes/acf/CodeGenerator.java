/*
 * CodeGenerator.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import java.util.List;

import archimedes.acf.io.SourceFileReader;
import archimedes.acf.io.SourceFileWriter;
import archimedes.acf.report.GenerationProcessReport;
import archimedes.model.DataModel;
import gengen.IndividualPreferences;

/**
 * An interface to cover code generations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public interface CodeGenerator {

	/**
	 * A call of this method should create the code for the passed data model in the file system.
	 *
	 * @param model                  The data model which is the base for the code generation.
	 * @param individualPreferences  The individual preferences for the code generation.
	 * @param sourceFileWriter       A class which writes the created source file to the file system.
	 * @param sourceFileReader       A class which reads an already existing source file to the file system to look up
	 *                               for changes.
	 * @param report                 A report container where the code generator could trace report events.
	 * @param postGeneratingCommands A list with commands which are to execute after all code generators have done their
	 *                               work (e. g. creating additional classes). Add your commands to the list.
	 * @throws CodeGeneratorException If an error occurs while generating the code.
	 * @return <CODE>true</CODE> if the code is generated correctly, <CODE>false</CODE> otherwise.
	 *
	 * @changed OLI 18.04.2013 - Added.
	 */
	abstract public boolean generate(DataModel dm, IndividualPreferences individualPreferences,
			SourceFileWriter sourceFileWriter, SourceFileReader sourceFileReader, GenerationProcessReport report,
			List<PostGeneratingCommand> postGeneratingCommands) throws CodeGeneratorException;

	/**
	 * Returns the type of the code generator.
	 *
	 * @return The type of the code generator.
	 *
	 * @changed OLI 27.09.2013 - Added.
	 */
	abstract public CodeGeneratorType getType();

	/**
	 * Checks if the code generator is deprecated and should only check for old source codes while code generation.
	 *
	 * @return <CODE>false</CODE> if the code generator is vital and in use. Otherwise used only for deprecated source
	 *         code file detection.
	 *
	 * @changed OLI 23.02.2015 - Added.
	 */
	abstract public boolean isDeprecated();

	/**
	 * Checks if the factory is temporarily suspended from code generation process.
	 *
	 * @return <CODE>true</CODE> if the factory is temporarily suspended from code generation process.
	 *
	 * @changed OLI 16.10.2013 - Added.
	 */
	abstract public boolean isTemporarilySuspended();

	/**
	 * Checks if the factory is only to call one times for a class.
	 *
	 * @return <CODE>true</CODE> if the factory is only to call one times for a class.
	 *
	 * @changed OLI 29.09.2013 - Added.
	 */
	abstract public boolean isOneTimeFactory();

	/**
	 * Sets the individual preferences of the user to the code generator.
	 *
	 * @param individualPreferences The individual preferences of the user for the code generator.
	 *
	 * @changed OLI 23.03.2016 - Added.
	 */
	abstract public void setIndividualPreferences(IndividualPreferences individualPreferences);

	/**
	 * Sets or resets the flag of temporarily suspension of the factory from code generation process.
	 *
	 * @param suspended The new value for the temporarily suspension flag of the factory.
	 *
	 * @changed OLI 16.10.2013 - Added.
	 */
	abstract public void setTemporarilySuspended(boolean suspended);

	/**
	 * Sets the unchanged by tag file names.
	 *
	 * @param unchangedByTagChecker An unchanged by tag checker.
	 *
	 * @changed OLI 22.04.2016 - Added.
	 * @changed OLI 20.09.2017 - Signature changed.
	 */
	abstract public void setUnchangedByTagFileInfo(UnchangedByTagChecker unchangedByTagChecker);

}