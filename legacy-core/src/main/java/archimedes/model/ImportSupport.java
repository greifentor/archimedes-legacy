/*
 * ImportSupport.java
 *
 * 17.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.model;

import corent.db.*;

/**
 * An interface which should be implemented by class which are able to import data models.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 17.06.2016 - Added.
 */

public interface ImportSupport {

    /**
     * Sets a new data source record for the import connection.
     *
     * @param dsr The new data source record for the import connection.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setImportDataSourceRecord(JDBCDataSourceRecord dsr);

}