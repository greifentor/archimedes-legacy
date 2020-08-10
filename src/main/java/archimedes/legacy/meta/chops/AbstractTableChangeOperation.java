/*
 * AbstractTableChangeOperation.java
 *
 * 11.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta.chops;

import static corentx.util.Checks.ensure;

import archimedes.legacy.meta.MetaDataTable;

/**
 * An abstract base class for table change operations.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.12.2015 - Added.
 */

public class AbstractTableChangeOperation extends AbstractChangeOperation {

	private MetaDataTable table = null;

	/**
	 * Creates a new change operation for a table.
	 *
	 * @param table   The information about the table which is to change.
	 * @param section The type of the section which the change operation is related to.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public AbstractTableChangeOperation(MetaDataTable table, ScriptSectionType section) {
		super(section);
		ensure(table != null, "table cannot be null.");
		this.table = table;
	}

	/**
	 * Returns the information about the table which is to change.
	 *
	 * @return The information about the table which is to change.
	 *
	 * @changed OLI 11.12.2015 - Added.
	 */
	public MetaDataTable getTable() {
		return this.table;
	}

}