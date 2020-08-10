/*
 * IndexListCleaner.java
 *
 * 19.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

/**
 * Definition der Methoden, die eine Klasse braucht, um als IndexListCleaner
 * genutzt werden zu k&ouml;nnen. Der IndexListCleaner entfernt ung&uuml;ltige
 * Eintr&auml;ge aus den Indexdefinitionen.
 * 
 * @author ollie
 * 
 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
 */

public interface IndexListCleaner {

	/**
	 * Das Datenmodell, f&uuml;r das die Bereinigung vorgenommen werden soll.
	 * 
	 * @param model
	 *            Das Datenmodell, f&uuml;r das die Bereinigung vorgenommen
	 *            werden soll.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition model != <CODE>null</CODE>
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	abstract public void clean(DataModel dm) throws IllegalArgumentException;

}