/*
 * AbstractColumnChangeOperation.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops;

import static corentx.util.Checks.ensure;

import archimedes.legacy.meta.MetaDataColumn;
import archimedes.legacy.meta.MetaDataTable;

/**
 * An abstract class as base for column change operations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AbstractColumnChangeOperation extends AbstractTableChangeOperation {

	private MetaDataColumn column = null;

	/**
	 * Creates a new change operation for a column.
	 *
	 * @param table   The information about the table which the column to change belongs to.
	 * @param column  The information about the column which is to change.
	 * @param section The type of the section which the change operation is related to.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public AbstractColumnChangeOperation(MetaDataTable table, MetaDataColumn column, ScriptSectionType section) {
		super(table, section);
		ensure(column != null, "column cannot be null.");
		this.column = column;
	}

	/**
	 * Returns the information about the column which is to change.
	 *
	 * @return The information about the column which is to change.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public MetaDataColumn getColumn() {
		return this.column;
	}

}