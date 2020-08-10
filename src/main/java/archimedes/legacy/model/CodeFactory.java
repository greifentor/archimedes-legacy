/*
 * CodeFactory.java
 *
 * 07.07.2004
 *
 * (c) O.Lieshoff
 *
 */

package archimedes.legacy.model;

import archimedes.acf.checker.ModelChecker;
import archimedes.acf.event.CodeFactoryListener;
import archimedes.legacy.gui.checker.ModelCheckerMessageListFrameListener;
import baccara.gui.GUIBundle;

/**
 * This interface defines all necessary methods for an Archimedes code factory.
 * <P>
 * TO have an own variant of the code factory create a class which implements this interface and include the class as
 * code factory in the data model which should use it.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.07.2004 - Added.
 * @changed OLI 09.06.2016 - Took from "Archimedes" project.
 */

public interface CodeFactory {

	/**
	 * Adds the passed listener to the list of listeners which are observing the IsisCodeFactory.
	 *
	 * @param l The listener to add.
	 *
	 * @changed OLI 07.12.2016 - Added from "AbstractCodeFactory" implementation.
	 */
	abstract public void addCodeFactoryListener(CodeFactoryListener l);

	/**
	 * Creates the code for the factories data model to the "out" path.
	 *
	 * @param out The base path which the code is to create into.
	 * @return <CODE>true</CODE> if the code is create without errors.
	 */
	abstract public boolean generate(String out);

	/**
	 * Returns the GUI bundle of the code factory.
	 *
	 * @param The GUI bundle of the code factory.
	 *
	 * @changed OLI 08.12.2016 - Added.
	 */
	abstract public GUIBundle getGUIBundle();

	/**
	 * Returns the model checkers which should be executed by the code factory.
	 *
	 * @return The model checkers which should be executed by the code factory.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	abstract public ModelChecker[] getModelCheckers();

	/**
	 * Returns the name of the code factory.
	 *
	 * @return The name of the code factory.
	 *
	 * @changed OLI 18.04.2017 - Added.
	 */
	abstract public String getName();

	/**
	 * Returns the names of the resource bundles used by the code factory.
	 *
	 * @return The names of the resource bundles used by the code factory.
	 *
	 * @changed OLI 10.06.2016 - Added.
	 */
	abstract public String[] getResourceBundleNames();

	/**
	 * Returns the version of the code factory.
	 *
	 * @return The version of the code factory.
	 *
	 * @changed OLI 18.04.2017 - Added.
	 */
	abstract public String getVersion();

	/**
	 * Removes the passed listener from the list of listeners which are observing the IsisCodeFactory.
	 *
	 * @param l The listener to remove.
	 *
	 * @changed OLI 07.12.2016 - Added from "AbstractCodeFactory" implementation.
	 */
	abstract public void removeCodeFactoryListener(CodeFactoryListener l);

	/**
	 * Sets the passed data model for the code factory.
	 *
	 * @param dataModel The data model to set for the code factory.
	 * @throws IllegalArgumentException Passing a null pointer.
	 *
	 * @changed OLI 24.05.2016 - Added.
	 */
	abstract public void setDataModel(DataModel dataModel);

	/**
	 * Sets the passed GUI bundle for the code generator.
	 *
	 * @param guiBundle The GUI bundle which is to use by the code generator.
	 *
	 * @changed OLI 31.03.2016 - Added.
	 */
	abstract public void setGUIBundle(GUIBundle guiBundle);

	/**
	 * Sets the passed model checker message list frame listener in case of errors or warnings are detected.
	 *
	 * @param listeners The model checker message list frame listener to set.
	 *
	 * @changed OLI 15.06.2016 - Added.
	 */
	abstract public void setModelCheckerMessageListFrameListeners(ModelCheckerMessageListFrameListener... listeners);

}