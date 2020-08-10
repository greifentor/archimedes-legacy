/*
 * STFRelationReader.java
 *
 * 29.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme.stf.reader;

import java.awt.Point;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.RelationModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.ViewModel;
import archimedes.legacy.scheme.Relation;
import archimedes.legacy.scheme.stf.handler.STFRelationHandler;
import corent.base.Direction;
import corent.files.StructuredTextFile;

/**
 * A reader for relations from a STF.
 * 
 * @author ollie
 * 
 * @changed OLI 29.04.2013 - Added.
 */

public class STFRelationReader extends STFRelationHandler {

	/**
	 * Updates the relations in the passed data model by the information stored
	 * in the STF.
	 * 
	 * @param stf
	 *            The STF whose relations should be read to the diagram.
	 * @param model
	 *            The diagram model which is to fill with the relations.
	 * 
	 * @changed OLI 26.04.2013 - Added.
	 */
	public void read(StructuredTextFile stf, DiagrammModel model, ColumnModel column, int i, int j, ViewModel mvm) {
		String n = fromHTML(stf.readStr(this.createPathForColumn(i, j, TABLE), null));
		String sn = fromHTML(stf.readStr(this.createPathForColumn(i, j, COLUMN), null));
		int[] offset = new int[2];
		Direction[] dir = new Direction[2];
		for (int k = 0; k < 2; k++) {
			dir[k] = Direction.CreateDirection(fromHTML(stf
					.readStr(this.createPathForColumn(i, j, DIRECTION + k), "UP")));
			offset[k] = (int) stf.readLong(this.createPathForColumn(i, j, OFFSET + k), 0);
		}
		ColumnModel c0 = model.getTableByName(n).getColumnByName(sn);
		RelationModel rm = Archimedes.Factory.createRelation(mvm, column, dir[0], offset[0], c0, dir[1], offset[1]);
		ViewModel vm = mvm;
		int lenm = (int) stf.readLong(this.createPathForColumn(i, j, VIEWS, COUNT), 0);
		for (int m = 0; m < lenm; m++) {
			String viewname = fromHTML(stf.readStr(this.createPathView(i, j, m, VIEW_NAME), ""));
			vm = (ViewModel) model.getView(viewname);
			if (vm != null) {
				for (int k = 0; k < 2; k++) {
					((Relation) rm).setDirection(vm, k, Direction.CreateDirection(fromHTML(stf.readStr(this
							.createPathView(i, j, m, DIRECTION + k), null))));
					((Relation) rm).setOffset(vm, k, (int) stf.readLong(this.createPathView(i, j, m, OFFSET + k), 0));
				}
				int lenk = (int) stf.readLong(this.createPathView(i, j, m, POINTS, COUNT), 0);
				for (int k = 0; k < lenk; k++) {
					int x = (int) stf.readLong(this.createPathViewPoints(i, j, m, k + 1, X), 0);
					int y = (int) stf.readLong(this.createPathViewPoints(i, j, m, k + 1, Y), 0);
					if (!rm.getPoints(vm).contains(new Point(x, y))) {
						rm.addPoint(vm, x, y);
					}
				}
			}
		}
		column.setRelation(rm);
		TableModel tm0 = column.getTable();
		tm0.removeColumn(column);
		column.setTable(tm0);
		tm0.addColumn(column);
	}

}