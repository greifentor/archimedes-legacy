/*
 * DefaultComplexIndicesToSTFConverter.java
 *
 * 19.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static archimedes.legacy.scheme.ComplexIndicesUtil.params;
import static corentx.util.Checks.ensureNotNull;
import gengen.metadata.AttributeMetaData;

import java.util.Vector;

import archimedes.legacy.model.ComplexIndicesToSTFWriter;
import archimedes.model.IndexMetaData;
import corent.files.StructuredTextFile;

/**
 * Diese Klasse f&uuml;gt den Inhalt einer Liste von komplexen Indices in ein
 * StructuredTextFile ein.
 * 
 * @author ollie
 * 
 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
 */

public class DefaultComplexIndicesToSTFWriter implements ComplexIndicesToSTFWriter {

	/**
	 * Erzeugt einen neuen Writer anhand der angebenen Parameter.
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public DefaultComplexIndicesToSTFWriter() {
		super();
	}

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void write(StructuredTextFile stf, IndexMetaData[] indices) throws IllegalArgumentException {
		ensureNotNull(indices, "list of indices cannot be null.");
		ensureNotNull(stf, "structured text file cannot be null.");
		this.clean(stf);
		this.fill(stf, indices);
	}

	private void clean(StructuredTextFile stf) {
		Vector<String[]> pathes = stf.getPathes();
		for (String[] path : pathes) {
			if ((path.length > 1) && path[0].equals("Diagramm") && path[1].equals("ComplexIndices")) {
				stf.remove(path);
			}
		}
	}

	private void fill(StructuredTextFile stf, IndexMetaData[] indices) {
		stf.writeLong(new String[] { "Diagramm", "ComplexIndices", "IndexCount" }, indices.length);
		for (int i = 0, leni = indices.length; i < leni; i++) {
			IndexMetaData imd = indices[0];
			stf.writeStr(params(i, "Name"), imd.getName());
			stf.writeStr(params(i, "Table", "Name"), imd.getTable().getName());
			AttributeMetaData[] attributes = imd.getColumns();
			stf.writeLong(params(i, "ColumnCount"), attributes.length);
			for (int j = 0; j < attributes.length; j++) {
				stf.writeStr(params(i, "Column" + j, "Name"), attributes[j].getName());
			}
		}
	}

}