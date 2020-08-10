/*
 * ViewProvider.java
 *
 * 09.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.model;


/**
 * An interface for classes which provide view.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 09.06.2016 - Added.
 */

public interface ViewProvider {

    /**
     * Returns the table model for the passed name if there is one.
     *
     * @param name The name which the table is to return for.
     * @return The table model for the passed name if there is one.
     *
     * @changed OLI 09.06.2016 - Added.
     */
    abstract public TableModel getTableByName(String name);

    /**
     * Returns the view model for the passed name if there is one.
     * 
     * @param name The name which the view is to return for.
     * @return The view model for the passed name if there is one.
     *
     * @changed OLI 09.06.2016 - Added.
     */
    abstract public ViewModel getViewByName(String name);

}