/*
 * ComplexIndexWriter.java
 *
 * 16.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;
import archimedes.legacy.model.ComplexIndicesToSTFWriter;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.IndexListCleaner;
import corent.files.StructuredTextFile;

/**
 * Mit Hilfe dieser Klasse kann sich die Liste der komplexen Indices eine
 * DiagramModels in einem StructuredTextFile verewigen.
 * 
 * @author ollie
 * 
 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndexWriter {

	private IndexListCleaner cleaner = null;
	private ComplexIndicesToSTFWriter writer = null;

	/**
	 * Erzeugt ein neues Object mit den angegebenen Parametern.
	 * 
	 * @param cleaner
	 *            Die Klasse, die f&uuml;r das Bereinigen der Indexliste
	 *            zust&auml;ndig ist.
	 * @param writer
	 *            Die Klasse, &uuml;ber die die Indices in das
	 *            StructuredTextFile &uuml;bertragen werden sollen.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition cleaner != <CODE>null</CODE>
	 * @precondition writer != <CODE>null</CODE>
	 * 
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	public ComplexIndexWriter(IndexListCleaner cleaner, ComplexIndicesToSTFWriter writer)
			throws IllegalArgumentException {
		super();
		ensure(cleaner != null, "writer cannot be null.");
		ensure(writer != null, "writer cannot be null.");
		this.cleaner = cleaner;
		this.writer = writer;
	}

	/**
	 * &Uuml;bertr&auml;gt die komplexen Indices des DiagramModel in das
	 * StructuredTextFile. Hierbei werden die Indices, die auf nicht mehr
	 * vorhandene Tabellenspalten zeigen, bereinigt.
	 * 
	 * @param stf
	 *            Das StructuredTextFile, in das die Daten der Indexliste
	 *            &uuml;bertragen werden sollen.
	 * @param diagram
	 *            Eine Referenz auf das Diagramm dessen Liste komplexer Indices
	 *            in das StructuredTextFile &uuml;bertragen werden soll.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition diagram != <CODE>null</CODE>
	 * @precondition stf != <CODE>null</CODE>
	 * 
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	public void store(StructuredTextFile stf, DataModel dm) {
		ensure(dm != null, "data model cannot be null.");
		ensure(stf != null, "structured text file cannot be null.");
		this.cleaner.clean(dm);
		this.writer.write(stf, dm.getComplexIndices());
	}

}