/*
 * DefaultComplexIndicesFromSTFReader.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static archimedes.legacy.scheme.ComplexIndicesUtil.indexCount;
import static archimedes.legacy.scheme.ComplexIndicesUtil.params;
import static corentx.util.Checks.ensureNotNull;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;
import gengen.metadata.ModelMetaData;

import java.util.List;

import archimedes.legacy.model.ComplexIndicesFromSTFReader;
import archimedes.model.IndexMetaData;
import corent.files.StructuredTextFile;

/**
 * Eine Implementierung des Interfaces zur Nutzung in der Archimedes-Anwendung.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public class DefaultComplexIndicesFromSTFReader implements ComplexIndicesFromSTFReader {

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void read(StructuredTextFile stf, List<IndexMetaData> indices, ModelMetaData model)
			throws IllegalArgumentException {
		ensureNotNull(indices, "list of indices cannot be null.");
		ensureNotNull(model, "model cannot be null.");
		ensureNotNull(stf, "structured text file cannot be null.");
		long indexCount = stf.readLong(indexCount(), 0);
		indices.clear();
		for (int i = 0; i < indexCount; i++) {
			String indexName = stf.readStr(params(i, "Name"), null);
			String tableName = stf.readStr(params(i, "Table", "Name"), null);
			if ((indexName != null) && (tableName != null)) {
				ClassMetaData cmd = model.getClass(tableName);
				if (cmd != null) {
					IndexMetaData imd = new DefaultIndexMetaData(indexName, cmd);
					long columnCount = stf.readLong(params(i, "ColumnCount"), 0);
					for (int j = 0; j < columnCount; j++) {
						String columnName = stf.readStr(params(i, "Column" + j, "Name"), null);
						if (columnName != null) {
							AttributeMetaData amd = cmd.getAttribute(columnName);
							if (amd != null) {
								imd.addColumn(amd);
							}
						}
					}
					indices.add(imd);
				}
			}
		}
	}

}