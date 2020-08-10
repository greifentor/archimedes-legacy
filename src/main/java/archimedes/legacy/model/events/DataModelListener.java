/*
 * DataModelListener.java
 *
 * 23.10.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model.events;


/**
 * This interface describes method which have to be implemented by classes which are able to
 * listen to data model events.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.10.2013 - Added.
 */

public interface DataModelListener {

    /**
     * This method will be called if a table is changed.
     *
     * @param event A table data change event.
     *
     * @changed OLI 23.10.2013 - Added.
     */
    abstract public void tableChanged(TableChangedEvent e);

}