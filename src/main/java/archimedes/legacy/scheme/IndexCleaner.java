/*
 * IndexCleaner.java
 *
 * 16.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import archimedes.legacy.model.TabellenModel;
import archimedes.model.IndexMetaData;

/**
 * Diese Klasse bereinigt eine Liste von komplexen Indices von Referenzen auf
 * nicht mehr existente Tabellenspalten.
 * 
 * @author ollie
 * 
 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
 */

public class IndexCleaner {

	/**
	 * Erzeugt einen neuen IndexCleaner mit den angegebenen Parametern.
	 * 
	 * @param table
	 *            Das TabellenModel mit den Daten der Tabelle.
	 * @param index
	 *            Der zu bereinigende komplexe Indices.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition index != <CODE>null</CODE>
	 * @precondition table != <CODE>null</CODE>
	 * 
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	public IndexCleaner(TabellenModel table, IndexMetaData index) {
		super();
	}

	/**
	 * Bereinigt den Index um die Felder, die sich nicht mehr im Tabellenmodel
	 * befinden.
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public void clean() {
	}

}
