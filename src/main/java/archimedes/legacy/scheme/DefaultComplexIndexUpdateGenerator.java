/*
 * DefaultComplexIndexUpdateGenerator.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import java.util.List;

import org.apache.log4j.Logger;

import archimedes.legacy.model.ComplexIndexScriptAppender;
import archimedes.legacy.model.ComplexIndexUpdateGenerator;
import archimedes.model.IndexMetaData;
import archimedes.model.SimpleIndexMetaData;

/**
 * Implementierung des Interfaces <CODE>ComplexIndexUpdateGenerator</CODE> zur
 * Nutzung in der Archimedes-Applikation.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public class DefaultComplexIndexUpdateGenerator implements ComplexIndexUpdateGenerator {

	private static final Logger LOG = Logger.getLogger(DefaultComplexIndexUpdateGenerator.class);

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void generate(List<SimpleIndexMetaData> indicesDB, List<IndexMetaData> indices,
			ComplexIndexScriptAppender... scriptAppender) throws IllegalArgumentException {
		this.checkForIndicesToRemoveFromTheDataScheme(indicesDB, indices, scriptAppender);
		this.checkForIndicesToAddToTheDataScheme(indicesDB, indices, scriptAppender);
	}

	private void checkForIndicesToRemoveFromTheDataScheme(List<SimpleIndexMetaData> indicesDB,
			List<IndexMetaData> indices, ComplexIndexScriptAppender... scriptAppender) {
		for (SimpleIndexMetaData simd : indicesDB) {
			IndexMetaData imd = this.findIndexMetaData(indices, simd.getName());
			if (imd != null) {
				for (String attr : simd.getColumns()) {
					if (!imd.isMember(attr)) {
						LOG.warn("change detected in index: " + imd.getName());
						LOG.warn("    Field expected: " + attr);
					}
				}
			} else {
				this.callRemoveInScriptAppenders(simd, scriptAppender);
			}
		}
	}

	private IndexMetaData findIndexMetaData(List<IndexMetaData> indices, String name) {
		for (IndexMetaData imd : indices) {
			if (imd.getName().toLowerCase().equals(name.toLowerCase())) {
				return imd;
			}
		}
		return null;
	}

	private void callRemoveInScriptAppenders(SimpleIndexMetaData simd, ComplexIndexScriptAppender... scriptAppenders) {
		for (ComplexIndexScriptAppender scriptAppender : scriptAppenders) {
			scriptAppender.indexRemoved(simd);
		}
	}

	private void checkForIndicesToAddToTheDataScheme(List<SimpleIndexMetaData> indicesDB, List<IndexMetaData> indices,
			ComplexIndexScriptAppender... scriptAppender) {
		for (IndexMetaData imd : indices) {
			SimpleIndexMetaData simd = this.findSimpleIndexMetaData(indicesDB, imd.getName());
			if (simd == null) {
				this.callAddInScriptAppenders(imd, scriptAppender);
			}
		}
	}

	private SimpleIndexMetaData findSimpleIndexMetaData(List<SimpleIndexMetaData> indices, String name) {
		for (SimpleIndexMetaData simd : indices) {
			if (simd.getName().toLowerCase().equals(name.toLowerCase())) {
				return simd;
			}
		}
		return null;
	}

	private void callAddInScriptAppenders(IndexMetaData imd, ComplexIndexScriptAppender... scriptAppenders) {
		for (ComplexIndexScriptAppender scriptAppender : scriptAppenders) {
			scriptAppender.indexAdded(imd);
		}
	}

}