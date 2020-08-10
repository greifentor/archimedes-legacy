/*
 * SQLGeneratorSupport.java
 *
 * 17.06.2016
 *
 * (c) by HealthCarion
 *
 */

package archimedes.legacy.model;


/**
 * An interface which should be implemented by objects which are used for SQL script generation.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 17.06.2016 - Added.
 */

public interface SQLGeneratorSupport {

    /**
     * Returns a string with additional lines of SQL code which are to append to the generated
     * script before the changing code.
     *
     * @return A string with additional lines of SQL code which are to append to the generated
     *         script before the changing code.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public String getAdditionalSQLCodePreChangingCode();

    /**
     * Returns a string with additional lines of SQL code which are to append to the generated
     * script after the changing code.
     *
     * @return A string with additional lines of SQL code which are to append to the generated
     *         script after the changing code.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public String getAdditionalSQLCodePostChangingCode();

    /**
     * Returns a string with additional lines of SQL code which are to append to the generated
     * script before the extending code.
     *
     * @return A string with additional lines of SQL code which are to append to the generated
     *         script before the extending code.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public String getAdditionalSQLCodePreExtendingCode();

    /**
     * Returns a string with additional lines of SQL code which are to append to the generated
     * script after the reducing code.
     *
     * @return A string with additional lines of SQL code which are to append to the generated
     *         script after the reducing code.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public String getAdditionalSQLCodePostReducingCode();

    /**
     * Returns the name an additional SQL script listener.
     *
     * @return The name an additional SQL script listener.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public String getAdditionalSQLScriptListener();

    /**
     * Returns the name of the description column in the table with the DB versions.
     *
     * @return The name of the description column in the table with the DB versions.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public String getDBVersionDescriptionColumnName();

    /**
     * Returns the name of the name of the table with the DB versions.
     *
     * @return The name of the name of the table with the DB versions.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public String getDBVersionTableName();

    /**
     * Returns the name of the DB version column in the table with the DB versions.
     *
     * @return The name of the DB version column in the table with the DB versions.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public String getDBVersionVersionColumnName();

    /**
     * Sets the passed string as new additional lines of SQL code which are to append to the
     * generated script before the extending code.
     *
     * @param sql The new additional lines of SQL code which are to append to the generated
     *         script before the extending code.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public void setAdditionalSQLCodePreExtendingCode(String sql)
            throws IllegalArgumentException;

    /**
     * Sets the passed string as new additional lines of SQL code which are to append to the
     * generated script before the changing code.
     *
     * @param sql The new additional lines of SQL code which are to append to the generated
     *         script before the changing code.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public void setAdditionalSQLCodePreChangingCode(String sql)
            throws IllegalArgumentException;

    /**
     * Sets the passed string as new additional lines of SQL code which are to append to the
     * generated script after the changing code.
     *
     * @param sql The new additional lines of SQL code which are to append to the generated
     *         script after the changing code.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public void setAdditionalSQLCodePostChangingCode(String sql)
            throws IllegalArgumentException;

    /**
     * Sets the passed string as new additional lines of SQL code which are to append to the
     * generated script after the reducing code.
     *
     * @param sql The new additional lines of SQL code which are to append to the generated
     *         script after the reducing code.
     * @throws IllegalArgumentException Passing a null pointer.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public void setAdditionalSQLCodePostReducingCode(String sql)
            throws IllegalArgumentException;

    /**
     * Sets a new name for an additional SQL script listener.
     *
     * @return listenerName The new name for an additional SQL script listener.
     *
     * @changed OLI 17.06.2016 - Added.
     */
    abstract public void setAdditionalSQLScriptListener(String listenerName);

    /**
     * Sets a new value for the description column name for the DB version table of the model.
     *
     * @param name The new description column name for the DB version table name of the model.
     *         A <CODE>null</CODE> value will be changed to an empty string.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public void setDBVersionDescriptionColumnName(String name);

    /**
     * Sets a new value for the DB version table name of the model.
     *
     * @param name The new DB version table name of the model. A <CODE>null</CODE> value will be
     *         changed to an empty string.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public void setDBVersionTableName(String name);

    /**
     * Sets a new value for the version column name for the DB version table of the model.
     *
     * @param name The new version column name for the DB version table name of the model.
     *         A <CODE>null</CODE> value will be changed to an empty string.
     *
     * @changed OLI 26.04.2013 - Added.
     */
    abstract public void setDBVersionVersionColumnName(String name);

}