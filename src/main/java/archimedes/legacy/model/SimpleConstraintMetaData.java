/*
 * SimpleConstraintMetaData.java
 *
 * 11.06.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


import static corentx.util.Checks.*;

import java.util.*;


/**
 * A base class for simple constraints.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.06.2013 - Added from sources <CODE>SimpleIndexMetaData</CODE>.
 */

public class SimpleConstraintMetaData implements Comparable {

    private List<String> columnNames = new Vector<String>();
    private String constraintName = null;
    private String tableName = null;

    /**
     * Creates a new <CODE>SimpleConstraintMetaData</CODE> with the passed parameters.
     *
     * @param constraintName The name of the constraint.
     * @param tableName The name of the table which the constraint is valid for.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition constraintName != <CODE>null</CODE>
     * @precondition !constraintName.isEmpty()
     * @precondition tableName != <CODE>null</CODE>
     * @precondition !tableName.isEmpty()
     *
     * @changed OLI 11.06.2013 - Added.
     */
    public SimpleConstraintMetaData(String constraintName, String tableName) {
        super();
        this.setName(constraintName);
        this.setTable(tableName);
    }

    /**
     * Adds a column to the constraint, which is part of the constraint.
     *
     * @param columnName The name of the column, which is to add to the constraint.
     * @throws IllegalArgumentException Passing an empty or a null string.
     *
     * @changed OLI 11.06.2013 - Added.
     */
    public void addColumn(String columnName) throws IllegalArgumentException {
        ensureNotEmpty(columnName, "column name can neither be null nor empty.");
        this.columnNames.add(columnName);
    }

    /**
     * Removes all columns from the constraint.
     *
     * @changed OLI 11.06.2013 - Added.
     */
    public void clearColumns() {
        this.columnNames.clear();
    }

    /**
     * @changed OLI 11.06.2013 - Added.
     */
    @Override public int compareTo(Object o) {
        SimpleConstraintMetaData cmd = (SimpleConstraintMetaData) o;
        return this.getName().compareTo(cmd.getName());
    }

    /**
     * Returns an array of names of the columns which the constraint is valid for.
     *
     * @return An array of names of the columns which the constraint is valid for.
     *
     * @changed OLI 19.12.2011 - Added.
     */
    public String[] getColumns() {
        return this.columnNames.toArray(new String[] {});
    }

    /**
     * Returns the name of the constraint.
     *
     * @return The name of the constraint.
     *
     * @changed OLI 19.12.2011 - Added.
     */
    public String getName() {
        return this.constraintName;
    }

    /**
     * Returns the name of the table which the constraint is valid for.
     *
     * @return The name of the table which the constraint is valid for.
     *
     * @changed OLI 19.12.2011 - Added.
     */
    public String getTable() {
        return this.tableName;
    }

    /**
     * Removes a column from the list of the columns which the constraint is valid for.
     *
     * @param columnName The name of the column which is to remove.
     * @throws IllegalArgumentException Passing an empty or a null string.
     *
     * @changed OLI 19.12.2011 - Added.
     */
    public void removeColumn(String columnName) throws IllegalArgumentException {
        ensureNotEmpty(columnName, "column name can neither be null nor empty.");
        this.columnNames.remove(columnName);
    }

    /**
     * Sets a new name for the constraint.
     *
     * @param constraintName The new name for the constraint.
     * @throws IllegalArgumentException Passing an empty or a null string.
     *
     * @changed OLI 19.12.2011 - Added.
     */
    public void setName(String constraintName) throws IllegalArgumentException {
        ensureNotEmpty(constraintName, "constraint name can neither be null nor empty.");
        this.constraintName = constraintName;
    }

    /**
     * Sets a new name for the table which the constraint is valid for.
     *
     * @param tableName The new name for the table.
     * @throws IllegalArgumentException Passing an empty or a null pointer as table name.
     *
     * @changed OLI 19.12.2011 - Added.
     */
    public void setTable(String tableName) throws IllegalArgumentException {
        ensureNotEmpty(tableName, "table name can neither be null nor empty.");
        this.tableName = tableName;
    }

    /**
     * @changed OLI 19.12.2011 - Added.
     */
    @Override public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("Name=").append(this.getName());
        sb.append(", Table=").append(this.getTable());
        sb.append(", Columns=[");
        for (int i = 0; i < this.getColumns().length; i++) {
            if (i != 0) {
                sb.append(",");
            }
            sb.append(this.getColumns()[i]);
        }
        sb.append("]");
        return sb.toString();
    }

}