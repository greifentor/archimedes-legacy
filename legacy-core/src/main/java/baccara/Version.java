/*
 * Version.java
 *
 * 30.03.2018
 *
 * (c) by O.Lieshoff
 *
 */

package baccara;

import javax.swing.JOptionPane;

/**
 * Eine Klasse mit der Versionsnummer der Software.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 30.03.2018 - Hinzugef&uuml,gt.
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
		return "1.10.1 (Embedded)";
	}

	@Override
	public String toString() {
		return this.getVersion();
	}

	public static void main(String[] args) {
		System.out.println("Baccara version is " + INSTANCE.getVersion());
		new Thread(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, "Version of Baccara is: " + INSTANCE.getVersion(),
						"Baccara version", JOptionPane.INFORMATION_MESSAGE);
			}
		}).start();
	}

}
