/*
 * SysUtil.java
 *
 * 19.08.2007
 *
 * (c) by O.Lieshoff
 *
 */

package corent.util;

import java.awt.event.KeyEvent;
import java.lang.reflect.Field;

import javax.swing.JButton;

import logging.Logger;

/**
 * Diese Klasse enth&auml;lt eine Sammlung von Methoden zum Zugriff auf
 * bestimmte Systemparameter (z. B. den Rechnernamen), die hier
 * plattform&uuml;bergreifend implementiert sind.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 26.11.2007 - Erweiterung um die Methode
 *          <TT>Execute(String)</TT>.
 * @changed OLI 10.08.2008 - Erweiterung um die Methode
 *          <TT>GetKeyCode(String)</TT>.
 * @changed OLI 05.01.2009 - Bei FreeBSD wird die Betriebssystemvariable in der
 *          Methode <TT>GetHostname</TT> nur noch dann gesetzt, wenn in der
 *          Property <I>hostname</I> kein Wert enthalten ist.
 * @changed OLI 05.01.2009 - Debugging in der Methode <TT>GetHostname()</TT>:
 *          Falls die Betriebssystemmethodik nicht funktioniert, wird der Wert
 *          aus der Property <I>hostname</I> gelesen. Nur wenn hier auch kein
 *          Wert gefunden wird, wird die Fehlermeldung angezeigt.
 * @changed OLI 17.07.2009 - Erweiterung um die Methode
 *          <TT>equalsRef(Object, Object)</TT>. Dabei: Formatanpassungen.
 *
 */

public class SysUtil {

	private static final Logger log = Logger.getLogger(SysUtil.class);

	/**
	 * Diese Methode liefert den Namen des Rechners zur&uuml;ck, auf dem die JVM
	 * l&auml;ft.
	 * <P>
	 * Im Fall, da&szlig; die Implementierung f&uuml;r das genutzte Betriebssystem
	 * nicht erfolgt ist, wird die Property <TT>hostname</TT> abgefragt. Ist auch
	 * diese nicht gesetzt wird eine aussagekr&auml;ftige Fehlermeldung ausgegeben
	 * und ein Nullpointer zur&uuml;ckgereicht.
	 * <P>
	 * <I><B>Wichtig:</B> F&uuml;r FreeBSD-Rechner ist das Setzen der Property
	 * "hostname" notwendig, da das Betriebssystem ein Auslesen der
	 * Betriebssystemvariablen nicht zuzulassen scheint.</I>
	 *
	 * @return Der Rechnername der Maschine auf dem die JVM l&auml;ft, bzw. der
	 *         Inhalt der Property "hostname", oder <TT>null</TT>, wenn diese auch
	 *         nicht gesetzt ist.
	 *
	 * @changed OLI 05.01.2009 - Debugging: Falls die Betriebssystemmethodik nicht
	 *          funktioniert, wird der Wert aus der Property <I>hostname</I>
	 *          gelesen. Nur wenn hier auch kein Wert gefunden wird, wird die
	 *          Fehlermeldung angezeigt.
	 *
	 */
	// TODO OLI 10.06.2010 - Holen des Namens durch java.net.InetAddress pruefen.
	public static String GetHostname() {
		String osn = System.getProperty("os.name").toLowerCase();
		String hn = null;
		if (osn.startsWith("windows")) {
			hn = System.getenv("COMPUTERNAME");
		} else if (osn.equals("freebsd")) {
			// Das funktioniert leider nicht. Offenbar lassen sich OS-Variablen nicht
			// auslesen.
			// (Zugriffsschutz ?!?).
			// OLI 28.05.2008
			hn = System.getenv("HOSTNAME");
		} else if (osn.equals("linux")) {
			hn = System.getenv("HOST");
		}
		if (hn == null) {
			hn = System.getProperty("hostname");
		}
		if (hn == null) {
			log.info("-------------------------------------------------------------------------------");
			log.info("SysUtil.GetHostname() returns null or is not implemented for " + osn + " (" + hn + ")");
			log.info("Set property \"hostname\" with the hosts name!");
			log.info("-------------------------------------------------------------------------------");
		}
		return hn;
	}

	/**
	 * Liefert einen VK-Tastaturcode zum angegebenen String. Der String muss den
	 * Bezeichner des VK-Tastaturcodes (z. B. VK_ESCAPE) enthalten.
	 *
	 * @param s Der String mit dem VK-Tastaturcode.
	 * @return Der int-Wert zum angegebenen VK-Tastaturcode.
	 * @throws IllegalArgumentException Falls der angegebene String keinen
	 *                                  VK-Tastaturcode zugeordnet ist.
	 *
	 * @changed OLI 10.08.2008 - Hinzugef&uuml;gt.
	 *
	 */
	public synchronized static int GetKeyCode(String s) throws IllegalArgumentException {
		try {
			KeyEvent ke = new KeyEvent(new JButton(), KeyEvent.KEY_TYPED, 0, 0, KeyEvent.VK_UNDEFINED, '\0');
			Field f = KeyEvent.class.getField(s);
			return f.getInt(ke);
		} catch (Exception e) {
			e.printStackTrace();
		}
		throw new IllegalArgumentException("ERROR: There is no key code for \"" + s + "\".");
	}

}
