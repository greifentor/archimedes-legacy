/*
 * ComplexIndexScriptAppender.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

/**
 * Implementierungen dieses Interfaces sind in der Lage bestehende Scripte um
 * &Auml;nderungsinformationen zu erweitern, die sich auf die Unterschiede
 * zwischen den Definitionen komplexer Indices in einer Datenbank und einem
 * Datenmodell beziehen.
 * 
 * <P>
 * Die Implementierungen des Scripts werden durch einen entsprechenden Updater
 * aufgerufen, der die eigentliche Aufgabe des Vergleichs von Modell und Schema
 * &uuml;bernimmt.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public interface ComplexIndexScriptAppender {

	/**
	 * Diese Methode wird aufgerufen, wenn zum Datenschema der Datenbank ein
	 * komplexer Index hinzugef&uuml;gt werden muss.
	 * 
	 * @param index
	 *            Die Daten des zu erzeugenden Index.
	 * 
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	abstract public void indexAdded(IndexMetaData index);

	/**
	 * Diese Methode wird aufgerufen, wenn aus dem Datenschema der Datenbank ein
	 * komplexer Index entfernt werden muss.
	 * 
	 * @param index
	 *            Die Daten des zu erzeugenden Index.
	 * 
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	abstract public void indexRemoved(SimpleIndexMetaData index);

}