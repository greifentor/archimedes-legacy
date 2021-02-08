/*
 * Version.java
 *
 * 27.07.2017
 *
 * (c) by O.Lieshoff
 *
 */

package corentx;

import javax.swing.JOptionPane;

/**
 * Eine Klasse mit der Versionsnummer der Software.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.07.2017 - Hinzugef&uuml,gt.
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
		return "1.46.1 (Embedded)";
	}

	@Override
	public String toString() {
		return this.getVersion();
	}

	public static void main(String[] args) {
		System.out.println("CoreNTX version is " + INSTANCE.getVersion());
		new Thread(new Runnable() {
			public void run() {
				JOptionPane.showMessageDialog(null, "Version of CoreNTX is: " + INSTANCE.getVersion(),
						"CoreNTX version", JOptionPane.INFORMATION_MESSAGE);
			}
		}).start();
	}

}
