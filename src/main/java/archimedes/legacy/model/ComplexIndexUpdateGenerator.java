/*
 * ComplexIndexUpdateScriptGenerator.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import java.util.List;

/**
 * Klassen, die diese Interface implementieren, sind in der Lage eine Liste mit
 * Index-Metadaten aus der Datenbank mit denen eines Datenmodells zu vergleichen
 * und die aufger&uuml;hrten <CODE>ComplexIndexScriptAppender</CODE> aufzurufen,
 * die die entsprechenden Aktionen erledigen. Die Liste der DB-Indices wird um
 * die verarbeiteten Indices bereinigt.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public interface ComplexIndexUpdateGenerator {

	/**
	 * Vergleicht die Metainformationen zu dern komplexen Indices aus der
	 * Datenbank mit denen aus dem Datenmodell und erzeugt ein SQL-Script, dass
	 * die Unterschiede ausgleicht.
	 * 
	 * @param indicesDB
	 *            Die Liste der Metadaten zu den komplexen Indices aus der
	 *            Datenbank.
	 * @param indices
	 *            Die Liste der komplexen Indices aus dem Datenmodell.
	 * @param scriptAppender
	 *            Eine Liste mit Implementierungen des Interfaces
	 *            <CODE>ComplexIndexScriptAppender</CODE>, die die eigentlichen
	 *            &Auml;nderungen an den Scripten vornehmen.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition indices != <CODE>null</CODE>
	 * @precondition indicesDB != <CODE>null</CODE>
	 * @precondition scriptAppender != <CODE>null</CODE>
	 * 
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	abstract public void generate(List<SimpleIndexMetaData> indicesDB, List<IndexMetaData> indices,
			ComplexIndexScriptAppender... scriptAppender) throws IllegalArgumentException;

}