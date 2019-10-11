/*
 * VersionStringBuilder.java
 *
 * 18.04.2017
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.util;

import archimedes.legacy.Archimedes;
import archimedes.model.CodeFactory;
import corent.Corent;
import corentx.CoreNTX;

/**
 * Creates a version string for the application (e. g. for bug ticket purpose).
 * 
 * @author ollie
 * 
 * @changed OLI 18.04.2017 - Added.
 */

public class VersionStringBuilder {

	/**
	 * Returns the version string for the application.
	 * 
	 * @param codeFactoryName
	 *            The name of the current code factory.
	 * @param codeFactoryVersion
	 *            The version of the current code factory.
	 * 
	 * @return The version string for the application.
	 * 
	 * @changed OLI 18.04.2017 - Added.
	 */
	public String getVersions(CodeFactory codeFactory) {
		String s = "Archimedes: " + Archimedes.GetVersion() + "\n\n";
		if (codeFactory != null) {
			s += codeFactory.getName() + ": " + codeFactory.getVersion() + "\n\n";
		}
		s += "Baccara: " + baccara.Version.INSTANCE.getVersion() + "\n";
		s += "CoreNT: " + Corent.GetVersion() + "\n";
		s += "CoreNTX: " + CoreNTX.GetVersion() + "\n";
		return s;
	}

}