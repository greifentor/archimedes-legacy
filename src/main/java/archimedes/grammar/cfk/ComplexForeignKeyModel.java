/*
 * ComplexForeignKeyModel.java
 *
 * 25.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.grammar.cfk;

import static corentx.util.Checks.*;

/**
 * A representation of a complex foreign key.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.09.2017 - Added.
 */

public class ComplexForeignKeyModel {

    private String[] columnNames = null;
    private String tableName = null;
    private String[] targetColumnNames = null;
    private String targetTableName = null;

    /**
     * Creates a new complex foreign key model with the passed parameters.
     *
     * @param tableName The name of the table which acts as referencer.
     * @param columnNames The names of the foreign key columns which are referencing the target
     *         table.
     * @param targetTableName The name of the target table. 
     * @param targetColumnNames The names of the target columns.
     * @throws IllegalArgumentException Passing a null pointer or an empty string (also in the
     *         arrays).
     * @throws IllegalStateException Passing arrays with different count of column names.
     *
     * @changed OLI 25.09.2017 - Added.
     */
    public ComplexForeignKeyModel(String tableName, String[] columnNames,
            String targetTableName, String[] targetColumnNames) {
        super();
        ensure(columnNames != null, "column names cannot be null.");
        ensure(columnNames.length > 0, "column names cannot be empty.");
        ensure(tableName != null, "table name cannot be null.");
        ensure(!tableName.isEmpty(), "table name cannot be empty.");
        ensure(targetColumnNames != null, "column names cannot be null.");
        ensure(targetColumnNames.length > 0, "column names cannot be empty.");
        ensure(targetTableName != null, "target table name cannot be null.");
        ensure(!targetTableName.isEmpty(), "target table name cannot be empty.");
        for (String s : columnNames) {
            ensure(s != null, "column name in the array cannot be null.");
            ensure(!s.isEmpty(), "column name in the array cannot be empty.");
        }
        for (String s : targetColumnNames) {
            ensure(s != null, "target column name in the array cannot be null.");
            ensure(!s.isEmpty(), "target column name in the array cannot be empty.");
        }
        ensure(columnNames.length == targetColumnNames.length, "length of column names (" 
                + columnNames.length + ") differs "
                + "from length of target column names (" + targetColumnNames.length + ").");
        this.columnNames = columnNames;
        this.tableName = tableName;
        this.targetColumnNames = targetColumnNames;
        this.targetTableName = targetTableName;
    }

    /**
     * Returns the column names of the complex foreign keys referencer.
     *
     * @return The column names of the complex foreign keys referencer.
     *
     * @changed OLI 25.09.2017 - Added.
     */
    public String[] getColumnNames() {
        return this.columnNames;
    }

    /**
     * Returns the table name of the complex foreign keys referencer.
     *
     * @return The table name of the complex foreign keys referencer.
     *
     * @changed OLI 25.09.2017 - Added.
     */
    public String getTableName() {
        return this.tableName;
    }

    /**
     * Returns the target column names of the complex foreign keys referencer.
     *
     * @return The target column names of the complex foreign keys referencer.
     *
     * @changed OLI 25.09.2017 - Added.
     */
    public String[] getTargetColumnNames() {
        return this.targetColumnNames;
    }

    /**
     * Returns the target table name of the complex foreign keys referencer.
     *
     * @return The target table name of the complex foreign keys referencer.
     *
     * @changed OLI 25.09.2017 - Added.
     */
    public String getTargetTableName() {
        return this.targetTableName;
    }

}