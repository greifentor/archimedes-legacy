/*
 * ComplexIndicesFromSTFReader.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import gengen.metadata.ModelMetaData;

import java.util.List;

import corent.files.StructuredTextFile;

/**
 * Definition der Methoden, die ein Objekt implementieren muss, um Indices aus
 * einem StructuredTextFile in die angegebene Indexliste zu &uuml;bertragen.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public interface ComplexIndicesFromSTFReader {

	/**
	 * &Uuml;bertr&auml;gt die Indices aus dem StructuredTextFile in die
	 * angegebene Indexliste. Die Indexliste wird vor der Bearbeitung
	 * gel&ouml;scht. Referenzen auf nicht mehr existente Tabellen und
	 * Tabellenspalten werden bereinigt.
	 * 
	 * @param stf
	 *            Das StructuredTextFile, aus dem die Indices &uuml;bertragen
	 *            werden sollen.
	 * @param indices
	 *            Referenz auf die Liste, die mit den Indices bef&uuml;llt
	 *            werden soll.
	 * @param model
	 *            Das Datenmodell, aus dem die Verkn&uuml;pfungen zu Tabellen
	 *            und Tabellenspalten gelesen werden sollen.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition stf != <CODE>null</CODE>
	 * @precondition indices != <CODE>null</CODE>
	 * @precondition model != <CODE>null</CODE>
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public void read(StructuredTextFile stf, List<IndexMetaData> indices, ModelMetaData model)
			throws IllegalArgumentException;

}