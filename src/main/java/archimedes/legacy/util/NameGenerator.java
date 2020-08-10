/*
 * NameGenerator.java
 *
 * 10.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.util;

import static corentx.util.Checks.*;

import corent.db.*;


/**
 * A generator for different object names in the database.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.12.2015 - Added.
 */

public class NameGenerator {

    private DBExecMode dbMode = null;

    /**
     * Creates a new name generator which the passed parameters.
     *
     * @param dbMode The type of the DBMS which the names should be generated for or
     *         <CODE>null</CODE> if the DBMS is irrelevant.
     *
     * @changed OLI 22.12.2015 - Added.
     */
    public NameGenerator(DBExecMode dbMode) {
        super();
        this.dbMode = dbMode;
    }

    /**
     * Returns the name of an foreign key constraint for the passed field.
     *
     * @param tableName The table which the field belongs to.
     * @param columnName The column which the constraint is created for.
     * @returns The name of an foreign key constraint for the passed field.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public String getForeignKeyName(String tableName, String columnName) {
        ensure(columnName != null, "column name cannot be null.");
        ensure(!columnName.isEmpty(), "column name cannot be empty.");
        ensure(tableName != null, "table name cannot be null.");
        ensure(!tableName.isEmpty(), "table name cannot be empty.");
        return this.finish(tableName + "_" + columnName + "_fkey");
    }

    private String finish(String s) {
        if (this.dbMode == DBExecMode.POSTGRESQL) {
            if (s.length() > 63) { 
                s = s.substring(0, 63);
            }
        }
        return s;
    }

    /**
     * Returns the name of an foreign key constraint for the passed field.
     *
     * @param tableName The table which the field belongs to.
     * @param columnName The column which the constraint is created for.
     * @returns The name of an foreign key constraint for the passed field.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public String getForeignKeyName(String tableName, String[] columnNames) {
        ensure(columnNames != null, "column names cannot be null.");
        ensure(columnNames.length > 0, "column names cannot be empty.");
        ensure(tableName != null, "table name cannot be null.");
        ensure(!tableName.isEmpty(), "table name cannot be empty.");
        String s = "";
        for (String cn : columnNames) {
            s += "_" + cn;
        }
        return this.finish(tableName + s + "_fkey");
    }

    /**
     * Returns the name of an index for a single field.
     *
     * @param tableName The name of the table whose member the index field is.
     * @param columnName The name of the column which the index name is to create for.
     * @return The name of an index for the passed column of the passed table.
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public String getIndexName(String tableName, String columnName) {
        ensure(columnName != null, "column name cannot be null.");
        ensure(!columnName.isEmpty(), "column name cannot be empty.");
        ensure(tableName != null, "table name cannot be null.");
        ensure(!tableName.isEmpty(), "table name cannot be empty.");
        return this.finish("I" + tableName + columnName);
    }

    /**
     * Returns the name of an index for a single field.
     *
     * @param tableName The name of the table whose member the index field is.
     * @param columnNames A list of name of the columns which the index name is to create for.
     * @return The name of an index for the passed column of the passed table.
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public String getIndexName(String tableName, String[] columnNames) {
        ensure(columnNames != null, "columns name cannot be null.");
        ensure(columnNames.length > 0, "columns name cannot be empty.");
        ensure(tableName != null, "table name cannot be null.");
        ensure(!tableName.isEmpty(), "table name cannot be empty.");
        String s = "";
        for (String cn : columnNames) {
            s += cn;
        }
        return this.finish("I" + tableName + s);
    }

    /**
     * Returns the name of an primary key constraint for the passed parameters.
     *
     * @param tableName The table which the field belongs to.
     * @param columnNames The names of the columns which are primary key members.
     * @returns The name of an primary key constraint for the passed field.
     *
     * @changed OLI 15.12.2015 - Added.
     */
    public String getPrimaryKeyName(String tableName, String[] columnNames) {
        ensure(columnNames != null, "column names cannot be null.");
        ensure(columnNames.length > 0, "column names cannot be empty.");
        ensure(tableName != null, "table name cannot be null.");
        ensure(!tableName.isEmpty(), "table name cannot be empty.");
        return this.finish(tableName + "_pkey");
    }

    /**
     * Returns the name of an unique constraint for the passed parameters.
     *
     * @param tableName The table which the field belongs to.
     * @param columnNames The names of the columns which are unique members.
     * @returns The name of an unique constraint for the passed field.
     *
     * @changed OLI 15.12.2015 - Added.
     */
    public String getUniqueName(String tableName, String[] columnNames) {
        ensure(columnNames != null, "column names cannot be null.");
        ensure(columnNames.length > 0, "column names cannot be empty.");
        ensure(tableName != null, "table name cannot be null.");
        ensure(!tableName.isEmpty(), "table name cannot be empty.");
        String s = tableName;
        for (String cn : columnNames) {
            s += "_" + cn;
        }
        return this.finish(s + "_key");
    }

}