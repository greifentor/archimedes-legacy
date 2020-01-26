/*
 * Version.java
 *
 * 08.04.2018
 *
 * (c) by ollie
 *
 */

package archimedes.legacy;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.swing.JOptionPane;

/**
 * Eine Klasse mit der Versionsnummer der Software.
 * 
 * @author ollie
 * 
 * @changed OLI 08.04.2018 - Hinzugef&uuml,gt.
 */

public class Version {

	public static final Version INSTANCE = new Version();

	private static String version = "UNKNOWN";

	/*
	 * Generiert eine Instanz der Versionsklasse mit Default-Parametern.
	 */
	private Version() {
		super();
	}

	/**
	 * Liefert die Nummer der Version zur Applikation.
	 * 
	 * @return Die Versionsnummer zur Applikation.
	 */
	public String getVersion() {
		return version;
	}

	@Override
	public String toString() {
		return this.getVersion();
	}

	public static void main(String[] args) {
		System.out.println("Archimedes version is " + INSTANCE.getVersion());
		new Thread(() -> JOptionPane.showMessageDialog(null, "Version of Archimedes is: " + INSTANCE.getVersion(),
				"Archimedes version", JOptionPane.INFORMATION_MESSAGE)).start();
	}

	static {
		InputStream is = Version.class.getClassLoader().getResourceAsStream("version.properties");
		if (is != null) {
			try {
				Properties properties = new Properties();
				properties.load(is);
				version = properties.getProperty("archimedes.version");
			} catch (IOException e) {
				System.out.println("Version file could not be loaded. Kept to: " + version);
			}
		} else {
			System.out.println("Version file could not found. Kept to: " + version);
		}
		System.out.println("Archimedes version: " + version);
	}

}
