/*
 * AddColumn.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops.columns;

import archimedes.legacy.meta.MetaDataColumn;
import archimedes.legacy.meta.MetaDataTable;
import archimedes.legacy.meta.chops.AbstractColumnChangeOperation;
import archimedes.legacy.meta.chops.ScriptSectionType;

/**
 * A change operation to add a column to a model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AddColumn extends AbstractColumnChangeOperation {

	/**
	 * Creates a new change operation to add a column to a model.
	 *
	 * @param table  The information about the table which the column to add belongs to.
	 * @param column The information about the column which is to add.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public AddColumn(MetaDataTable table, MetaDataColumn column) {
		super(table, column, ScriptSectionType.ADD_COLUMNS_AND_DROP_CONSTRAINTS);
	}

	/**
	 * @changed OLI 11.12.2015 - Added.
	 */
	@Override
	public String toString() {
		return "AddColumn(table=\"" + this.getTable().getName() + "\", column=\"" + this.getColumn().getName() + "\")";
	}

}