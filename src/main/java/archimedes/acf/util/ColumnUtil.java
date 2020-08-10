/*
 * ColumnUtil.java
 *
 * 03.06.2015
 *
 * (c) by O. Lieshoff
 *
 */

package archimedes.acf.util;

import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import archimedes.acf.param.TableParamIds;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.TableModel;
import corentx.util.SortedVector;

/**
 * Some utility methods for working with columns.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.06.2015 - Added.
 */

public class ColumnUtil extends ParameterUtilOnwer {

	private DomainUtil domainUtil = null;

	/**
	 * Creates a new column util with the passed parameters.
	 *
	 * @param parameterUtil A utility class for working with parameters.
	 * @param domainUtil    A utility class for working with domains.
	 *
	 * @changed OLI 03.06.2015 - Added.
	 */
	public ColumnUtil(ParameterUtil parameterUtil, DomainUtil domainUtil) {
		super(parameterUtil);
		this.domainUtil = domainUtil;
	}

	/**
	 * Returns a list of columns from the passed array which contains only editor member columns which are assorted by
	 * their editor positions.
	 *
	 * @param columns The array of columns where editor member columns are to separate from.
	 * @return A list of columns from the passed array which contains only editor member columns which are assorted by
	 *         their editor positions.
	 *
	 * @changed OLI 03.06.2015 - Added comment.
	 */
	public ColumnModel[] getEditorColumnsOrderedByPositions(ColumnModel[] columns) {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		List<ColumnModel> l0 = new LinkedList<ColumnModel>();
		Hashtable<Integer, SortedVector<Integer>> hpos = new Hashtable<Integer, SortedVector<Integer>>();
		for (ColumnModel c : columns) {
			if (c.isEditorMember()) {
				int pn = c.getPanel().getPanelNumber();
				SortedVector<Integer> pos = hpos.get(pn);
				if (pos == null) {
					pos = new SortedVector<Integer>();
					hpos.put(pn, pos);
				}
				pos.add(c.getEditorPosition());
				l0.add(c);
			}
		}
		for (Integer tab : hpos.keySet()) {
			SortedVector<Integer> pos = hpos.get(tab);
			for (Integer i : pos) {
				for (ColumnModel c : l0) {
					if ((c.getEditorPosition() == i.intValue()) && (tab == c.getPanel().getPanelNumber())) {
						l.add(c);
					}
				}
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	/**
	 * Returns all columns from the list which are marked as enums.
	 *
	 * @param columns The column list which is to check.
	 * @return All columns from the list which are marked as enums.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	public ColumnModel[] getEnumColumns(ColumnModel[] columns) {
		List<ColumnModel> l = new LinkedList<ColumnModel>();
		for (ColumnModel c : columns) {
			if (this.isEnumColumn(c)) {
				l.add(c);
			}
		}
		return l.toArray(new ColumnModel[0]);
	}

	/**
	 * Returns the first column whose global id flag is set.
	 *
	 * @param tm The name of the table whose columns are to search for.
	 * @return The first column whose global id flag is set.
	 *
	 * @changed OLI 12.06.2015 - Added.
	 */
	public ColumnModel getGlobalIdSet(TableModel tm) {
		for (ColumnModel c : tm.getColumns()) {
			if (c.isSynchronizeId()) {
				return c;
			}
		}
		return null;
	}

	/**
	 * Returns the order column for the passed table model or <CODE>null</CODE> if none is defined.
	 *
	 * @param tm The table model which is to check for a order column.
	 * @return The order column for the passed table model or <CODE>null</CODE> if none is defined.
	 *
	 * @changed OLI 03.06.2015 - Added.
	 */
	public ColumnModel getOrderColumn(TableModel tm) {
		String p = this.parameterUtil.getValueParameterStartsWith(TableParamIds.ORDER_BY, tm);
		if (p != null) {
			return tm.getColumnByName(p);
		}
		return null;
	}

	/**
	 * Returns the list of all primary key columns from the passed list of columns.
	 *
	 * @param columns The list of columns whose primary key columns should be returned.
	 * @return The list of all primary key columns from the passed list.
	 *
	 * @changed OLI 11.10.2013 - Added.
	 */
	public ColumnModel[] getPrimaryKeyColumns(ColumnModel[] columns) {
		List<ColumnModel> pks = new LinkedList<ColumnModel>();
		for (ColumnModel c : columns) {
			if (c.isPrimaryKey()) {
				pks.add(c);
			}
		}
		return pks.toArray(new ColumnModel[0]);
	}

	/**
	 * Checks if the passed column is of an enum domain.
	 *
	 * @param c The column to check.
	 * @return <CODE>true</CODE> if column is of an enum domain.
	 *
	 * @changed OLI 10.10.2013 - Added.
	 */
	public boolean isEnumColumn(ColumnModel c) {
		return this.domainUtil.isEnumDomain(c.getDomain());
	}

}