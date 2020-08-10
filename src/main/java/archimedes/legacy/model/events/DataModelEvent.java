/*
 * DataModelEvent.java
 *
 * 23.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.events;

import archimedes.legacy.model.DataModel;

/**
 * A container class with the data of a data model event.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.10.2013 - Added.
 */

public class DataModelEvent {

	private DataModel dataModel = null;
	private DataModelEventType type = null;

	/**
	 * Creates a new data model event with the passed parameters.
	 *
	 * @param dataModel The data model which has been changed.
	 * @param type      The type of the event.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	public DataModelEvent(DataModel dataModel, DataModelEventType type) {
		super();
		this.dataModel = dataModel;
		this.type = type;
	}

	/**
	 * Returns the data model which is changed.
	 *
	 * @return The data model which is changed.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	public DataModel getDataModel() {
		return this.dataModel;
	}

	/**
	 * Returns the type of the event.
	 *
	 * @return The type of the event.
	 *
	 * @changed OLI 23.10.2013 - Added.
	 */
	public DataModelEventType getType() {
		return this.type;
	}

	/**
	 * @changed OLI 23.10.2013 - Added.
	 */
	@Override
	public String toString() {
		return "DataModel=" + this.dataModel.getName() + ",Type=" + this.getType();
	}

}