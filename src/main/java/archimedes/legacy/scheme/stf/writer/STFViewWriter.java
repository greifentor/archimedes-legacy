/*
 * STFViewWriter.java
 *
 * 08.05.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import org.apache.log4j.Logger;

import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.scheme.stf.handler.STFViewHandler;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import corent.files.StructuredTextFile;

/**
 * A writer for views to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 08.05.2013 - Added.
 */

public class STFViewWriter extends STFViewHandler {

	private static final Logger LOG = Logger.getLogger(STFDomainWriter.class);

	/**
	 * Writes the passed views to the STF.
	 * 
	 * @param views
	 *            The views which are to store in the passed STF.
	 * @param stf
	 *            The STF which is to update with the view data.
	 * 
	 * @changed OLI 08.05.2013 - Added.
	 */
	public void write(StructuredTextFile stf, ViewModel[] views, DiagramSaveMode dsm) {
		if (dsm == DiagramSaveMode.REGULAR) {
			stf.writeLong(createRootPath(VIEWS, COUNT), views.length);
			for (int i = 0; i < views.length; i++) {
				stf.writeStr(this.createPath(i, NAME), toHTML(views[i].getName()));
				stf.writeStr(this.createPath(i, DESCRIPTION), toHTML(views[i].getComment()));
				stf.writeStr(this.createPath(i, SHOW_REFERENCED_COLUMNS), new Boolean(views[i]
						.isShowReferencedColumns()).toString());
				stf.writeStr(this.createPath(i, HIDE_TECHNICAL_COLUMNS), new Boolean(views[i].isHideTechnicalFields())
						.toString());
				TableModel[] tables = views[i].getTables();
				stf.writeLong(this.createPath(i, TABLE_COUNT), tables.length);
				for (int j = 0; j < tables.length; j++) {
					stf.writeStr(this.createPath(i, TABLE + j), toHTML(tables[j].getName()));
				}
				LOG.debug("view written: " + views[i].getName());
			}
		}
	}

}