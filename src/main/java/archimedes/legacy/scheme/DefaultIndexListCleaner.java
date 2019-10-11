/*
 * DefaultIndexListCleaner.java
 *
 * 19.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;
import gengen.metadata.AttributeMetaData;

import org.apache.log4j.Logger;

import archimedes.legacy.model.IndexListCleaner;
import archimedes.model.DataModel;
import archimedes.model.IndexMetaData;
import archimedes.model.TableModel;

/**
 * Eine Implementierung des Interfaces zur Nutzung in der
 * Archimedes-Applikation.
 * 
 * @author ollie
 * 
 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
 */

public class DefaultIndexListCleaner implements IndexListCleaner {

	private static final Logger LOG = Logger.getLogger(DefaultIndexListCleaner.class);

	/**
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void clean(DataModel dm) throws IllegalArgumentException {
		ensure(dm != null, "data model cannot be null.");
		IndexMetaData[] indices = dm.getComplexIndices();
		TableModel tm = null;
		for (int i = indices.length - 1; i >= 0; i--) {
			IndexMetaData imd = indices[i];
			tm = dm.getTableByName(imd.getTable().getName());
			if (tm == null) {
				dm.removeComplexIndex(imd);
				LOG.info("index removed (table deleted): " + imd.getName() + ", table: " + imd.getTable().getName());
			} else {
				for (AttributeMetaData amd : imd.getColumns()) {
					if (tm.getColumnByName(amd.getName()) == null) {
						imd.removeColumn(amd);
						LOG.info("index cleaned (column deleted): " + imd.getName() + ", table: " + tm.getName()
								+ ", column: " + amd.getName());
					}
				}
			}
		}
	}

}