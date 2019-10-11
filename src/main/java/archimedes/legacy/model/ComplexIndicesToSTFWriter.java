/*
 * ComplexIndicesToSTFWriter.java
 *
 * 19.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.model;

import archimedes.model.IndexMetaData;
import corent.files.StructuredTextFile;

/**
 * Definition der Methoden, die ein Objekt implementieren muss, um Indices in
 * ein StructuredTextFile zu &uuml;bertragen.
 * 
 * @author ollie
 * 
 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
 */

public interface ComplexIndicesToSTFWriter {

	/**
	 * &Uuml;bertr&auml;gt die Indices in das StructuredTextFile. Eventuell
	 * bestehende Informationen werden gel&ouml;scht und &uuml;berschrieben.
	 * 
	 * @param stf
	 *            Das StructuredTextFile, in das die Indices &uuml;bertragen
	 *            werden sollen.
	 * @param indices
	 *            Die Liste mit den Indices.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition stf != <CODE>null</CODE>
	 * @precondition indices != <CODE>null</CODE>
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public void write(StructuredTextFile stf, IndexMetaData[] indices) throws IllegalArgumentException;

}