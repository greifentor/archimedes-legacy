/*
 * TableChangeEvent.java
 *
 * 23.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.events;

import archimedes.legacy.model.TableModel;

/**
 * A container class with the necessary data of a table change.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.10.2013 - Added.
 */

public class TableChangedEvent extends DataModelEvent {

	private TableModel table = null;

	/**
	 * Creates a table changed event for the passed parameters.
	 *
	 * @param table The table whose change causes the event.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	public TableChangedEvent(TableModel table) {
		super(table.getDataModel(), DataModelEventType.TABLE_DATA_CHANGED);
		this.table = table;
	}

	/**
	 * Returns the table whose change causes the event.
	 *
	 * @return The table whose change causes the event.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	public TableModel getTable() {
		return this.table;
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public String toString() {
		return super.toString() + ",Table=" + this.getTable().getName();
	}

}