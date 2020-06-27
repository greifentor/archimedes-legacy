/*
 * PathFinder.java
 *
 * 24.07.2008
 *
 * (c) O.Lieshoff
 *
 */

package corent.files;

import java.io.File;

import logging.Logger;

/**
 * Diese Klasse erm&ouml;glicht das Auffinden einer Datei in einem
 * Verzeichnisbaum.
 *
 * <P>
 * <I>Hinweis:</I> Die Klasse kann bei gro&szlig;en Dateib&auml;umen kurzfristig
 * einen hohen Speicherbedarf haben.
 *
 * @author O.Lieshoff
 *         <P>
 *
 * @changed OLI 24.08.2008 - Hinzugef&uuml;gt.
 *          <P>
 *
 */

public class PathFinder {

	private static final Logger log = Logger.getLogger(PathFinder.class);

	/** Generiert eine Instanz des PathFinders. */
	public PathFinder() {
		super();
	}

	/**
	 * Ermittelt den Pfad zum angegebenen Dateinamen. Es wird immer der erste
	 * Treffer zur&uuml;ckgegeben.
	 *
	 * @param f    Das Fectory, in dem die Suche starten soll.
	 * @param fn   Der Dateiname, nach dem gesucht werden soll.
	 * @param echo Wird diese Flagge gesetzt, wird der Name der aktuell
	 *             gepr&uuml;ften Datei auf der Konsole ausgegeben.
	 * @param gcs  Wird diese Flagge gestetzt, so wird nach dem Durcharbeiten eines
	 *             jeden Unterverzeichnisses eine Garbage-Collection
	 *             durchgef&uuml;hrt. Wird zudem die echo-Flagge gesetzt, so wird
	 *             hier auch der freie Speicher in bytes ausgegeben.
	 * @param pfl  Referenz auf einen PathFinderListener, der &uuml;ber das gerade
	 *             untersuchte File informiert wird (z. B. zur Realisation einer
	 *             Anzeigefunktion).
	 * @return Der vollst&auml;ndige Pfad der Datei, sofern eine des angegebenen
	 *         Namens gefunden wird. Sonst <TT>null</TT>.
	 *
	 * @changed OLI 24.07.2008 - Hinzugef&uuml;gt und Inhalt aus
	 *          corent.util.FindPath(File, String, boolean) &uuml;bernommen.
	 *          <P>
	 *
	 */
	public synchronized String find(File f, String fn, boolean echo, boolean gcs, PathFinderListener pfl) {
		if (echo) {
			log.info("" + f);
		}
		if (pfl != null) {
			pfl.fileChanged(new PathFinderEvent(PathFinderEvent.Type.FILECHANGED, f.toString()));
		}
		if (f.isDirectory()) {
			File[] files = f.listFiles();
			if (files != null) {
				String s = null;
				for (int i = 0; i < files.length; i++) {
					s = this.find(files[i], fn, echo, gcs, pfl);
					if (s != null) {
						return s;
					}
				}
			}
			if (gcs) {
				System.gc();
				if (echo) {
					log.info("free mem: " + Runtime.getRuntime().freeMemory() + " bytes");
				}
			}
		} else if (f.getName().equals(fn)) {
			return f.getAbsolutePath();
		}
		return null;
	}

}
