/*
 * DatabaseConnectionProvider.java
 *
 * 09.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.connections;


/**
 * Classes which implement this interface provide methods to maintain a collection of
 * database connections.
 *
 * @author O. Lieshoff
 *
 * @changed OLI 09.06.2016 - Added.
 */

public interface DatabaseConnectionProvider {

    /**
     * Adds a new database connection to the list (if it is not already contained).
     *
     * @param dc The database connection which is to add.
     *
     * @changed OLI 15.01.2015 - Added.
     * @changed OLI 09.06.2016 - Took from "Archimedes.DiagrammModel".
     */
    abstract public void addDatabaseConnection(DatabaseConnection dc);

    /**
     * Returns the database connection with the passed name.
     *
     * @param name The name of the database connection which is to return.
     * @Return The database connection with the passed name or <CODE>null</CODE> if no
     *         connections with the passed name is existing.
     *
     * @changed OLI 15.01.2015 - Added.
     * @changed OLI 09.06.2016 - Took from "Archimedes.DiagrammModel".
     */
    abstract public DatabaseConnection getDatabaseConnection(String name);

    /**
     * Returns a list of all database connections stored in the diagram.
     *
     * @Return A list of all database connections stored in the diagram.
     *
     * @changed OLI 15.01.2015 - Added.
     * @changed OLI 09.06.2016 - Took from "Archimedes.DiagrammModel".
     */
    abstract public DatabaseConnection[] getDatabaseConnections();

    /**
     * Removes the database connection with the passed name (if there is one in the diagram).
     *
     * @param name The name of the database connection which is remove form the diagram.
     * @return <CODE>true</CODE> if the database connection is removed.
     *
     * @changed OLI 15.01.2015 - Added.
     * @changed OLI 09.06.2016 - Took from "Archimedes.DiagrammModel".
     */
    abstract public boolean removeDatabaseConnection(String name);

}