/*
 * STFRelationWriter.java
 *
 * 29.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.writer;

import java.awt.Point;
import java.util.List;

import org.apache.log4j.Logger;

import archimedes.legacy.model.DiagramSaveMode;
import archimedes.legacy.scheme.Relation;
import archimedes.legacy.scheme.stf.handler.STFRelationHandler;
import archimedes.model.ColumnModel;
import archimedes.model.RelationModel;
import archimedes.model.TableModel;
import archimedes.model.ViewModel;
import archimedes.model.gui.GUIRelationModel;
import archimedes.model.gui.GUIViewModel;
import corent.files.StructuredTextFile;

/**
 * A writer for relations to STF.
 * 
 * @author ollie
 * 
 * @changed OLI 29.04.2013 - Added.
 */

public class STFRelationWriter extends STFRelationHandler {

	private static final Logger LOG = Logger.getLogger(STFSequenceWriter.class);

	/**
	 * Writes the passed sequences to the STF.
	 * 
	 * @param sequences
	 *            The sequences which are to store in the passed STF.
	 * @param stf
	 *            The STF which is to update with the sequence data.
	 * 
	 * @changed OLI 23.04.2013 - Added.
	 */
	public void write(StructuredTextFile stf, ColumnModel column, int i, int j, ViewModel mvm, DiagramSaveMode dsm) {
		RelationModel rm = column.getRelation();
		TableModel t0 = ((Relation) rm).getReferencer().getTable();
		TableModel t1 = rm.getReferenced().getTable();
		stf.writeStr(this.createPathForColumn(i, j, TABLE), toHTML(t1.getName()));
		stf.writeStr(this.createPathForColumn(i, j, COLUMN), toHTML(column.getRelation().getReferenced().getName()));
		if (dsm == DiagramSaveMode.REGULAR) {
			for (int k = 0; k < 2; k++) {
				GUIRelationModel grm = (GUIRelationModel) rm;
				stf.writeStr(this.createPathForColumn(i, j, DIRECTION + k), toHTML(grm.getDirection((GUIViewModel) mvm,
						k).toString()));
				stf.writeLong(this.createPathForColumn(i, j, OFFSET + k), grm.getOffset((GUIViewModel) mvm, k));
			}
			List<GUIViewModel> tvms = ((GUIRelationModel) rm).getViews();
			stf.writeLong(this.createPathForColumn(i, j, VIEWS, COUNT), tvms.size());
			for (int l = 0, lenl = tvms.size(); l < lenl; l++) {
				GUIViewModel vm = tvms.get(l);
				stf.writeStr(this.createPathView(i, j, l, VIEW_NAME), toHTML(vm.getName()));
				for (int k = 0; k < 2; k++) {
					GUIRelationModel grm = (GUIRelationModel) rm;
					stf.writeStr(this.createPathView(i, j, l, DIRECTION + k), toHTML(grm.getDirection(
							(GUIViewModel) vm, k).toString()));
					stf.writeLong(this.createPathView(i, j, l, OFFSET + k), grm.getOffset((GUIViewModel) vm, k));
				}
				List<Point> pts = ((GUIRelationModel) rm).getPointsForView((GUIViewModel) vm);
				int lenm = pts.size();
				stf.writeLong(this.createPathView(i, j, l, POINTS, COUNT), lenm - 2);
				for (int m = 1; m < lenm - 1; m++) {
					Point p = (Point) pts.get(m);
					stf.writeLong(this.createPathViewPoints(i, j, l, m, X), (long) p.getX());
					stf.writeLong(this.createPathViewPoints(i, j, l, m, Y), (long) p.getY());
				}
			}
		}
		LOG.debug("relation written: " + ((Relation) rm).getReferencer().getFullName() + " -> "
				+ rm.getReferenced().getFullName());
	}

}