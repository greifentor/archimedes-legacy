/*
 * Version.java
 *
 * 08.04.2018
 *
 * (c) by ollie
 *
 */

package archimedes.legacy;

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
		return "1.83.1";
	}

	@Override
	public String toString() {
		return this.getVersion();
	}

	public static void main(String[] args) {
		System.out.println("Archimedes version is " + INSTANCE.getVersion());
		new Thread(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, "Version of Archimedes is: " + INSTANCE.getVersion(),
						"Archimedes version", JOptionPane.INFORMATION_MESSAGE);
			}
		}).start();
	}

}
