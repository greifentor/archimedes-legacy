/*
 * Version.java
 *
 * 27.04.2018
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes;

import javax.swing.JOptionPane;

/**
 * Eine Klasse mit der Versionsnummer der Software.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.04.2018 - Hinzugef&uuml,gt.
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
		return "1.23.1 (Embedded)";
	}

	@Override
	public String toString() {
		return this.getVersion();
	}

	public static void main(String[] args) {
		System.out.println("Archimedes-Core version is " + INSTANCE.getVersion());
		new Thread(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, "Version of Archimedes-Core is: " + INSTANCE.getVersion(),
						"Archimedes-Core version", JOptionPane.INFORMATION_MESSAGE);
			}
		}).start();
	}

}
