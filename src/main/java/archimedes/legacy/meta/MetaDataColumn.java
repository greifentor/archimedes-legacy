/*
 * MetaDataColumn.java
 *
 * 04.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.*;

import java.util.*;

import org.apache.commons.lang3.builder.*;

import corent.db.*;
import corentx.util.*;


/**
 * A representation of the column of the meta data model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.12.2015 - Added.
 */

public class MetaDataColumn extends MetaDataNamedObjectCouldBeDeprecated {

    private int colSize = 0;
    private DBType dataType = null;
    private int decimalPlace = 0;
    private String defaultValue = null;
    private List<MetaDataForeignKeyConstraint> fkConstraints =
            new SortedVector<MetaDataForeignKeyConstraint>(); 
    private boolean indexed = false;
    private boolean notNull = false;
    private boolean primaryKey = false;
    private boolean unique = false;
    private MetaDataTable table = null;
    private String typeName = null;

    /**
     * Creates a new meta data column.
     *
     * @param table The table which the column belongs to.
     * @param name The name of the meta data column.
     * @param typeName The name of the type.
     * @param dataType The data type of the column.
     * @param colSize The size of the column.
     * @param decimalPlace The number of positions after the decimal point.
     * @param primaryKey Set this flag if the column is a primary key.
     * @param notNull Set this flag if the column is not nullable.
     * @param defaultValue A default value for the column or <CODE>null</CODE> if there is no
     *         value defined as default.
     * @param isDeprecated Set this flag if the column is marked as deprecated.
     */
    public MetaDataColumn(MetaDataTable table, String name, String typeName, DBType dataType,
            int colSize, int decimalPlace, boolean primaryKey, boolean notNull,
            String defaultValue, boolean isDeprecated) {
        super(name, isDeprecated);
        ensure(table != null, "table cannot be null.");
        this.colSize = colSize;
        this.setDataType(dataType);
        this.setDefaultValue(defaultValue);
        this.decimalPlace = decimalPlace;
        this.setNotNull(notNull);
        this.setPrimaryKey(primaryKey);
        this.table = table;
        this.typeName = typeName;
    }

    /**
     * Adds the passed foreign key constraint.
     *
     * @param fkc The foreign key constraint to add.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public void addForeignKeyConstraint(MetaDataForeignKeyConstraint fkc) {
        if (!this.fkConstraints.contains(fkc)) {
            this.fkConstraints.add(fkc);
        }
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    @Override public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    /**
     * Returns the default value for the column in string format or <CODE>null</CODE> if there
     * is no default for the column.
     *
     * @return The default value for the column in string format or <CODE>null</CODE> if there
     *         is no default for the column.
     *
     * @changed OLI 11.12.2015 - Added.
     */
    public String getDefaultValue() {
        return this.defaultValue;
    }

    /**
     * Returns the foreign key constraint which is referenced by the passed column.
     *
     * @param column The column which the returned foreign key constraint should point to.
     * @return The foreign key constraint which is referenced by the passed column or
     *         <CODE>null</CODE> if no foreign key constraint is pointing to the passed column.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public MetaDataForeignKeyConstraint getForeignKeyConstraint(MetaDataColumn column) {
        for (MetaDataForeignKeyConstraint fkc : this.fkConstraints) {
            if (fkc.getReferencedColumnFullName().equals(column.getFullName())) {
                return fkc;
            }
        }
        return null;
    }

    /**
     * Returns the foreign key constraints for the column.
     *
     * @return The foreign key constraints for the column.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataForeignKeyConstraint[] getForeignKeyConstraints() {
        return this.fkConstraints.toArray(new MetaDataForeignKeyConstraint[0]);
    }

    /**
     * Returns the qualified name of the column.
     *
     * @return The qualified name of the column.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public String getFullName() {
        return this.table.getName() + "." + this.getName();
    }

    /**
     * Returns the table of the column belongs to.
     *
     * @return The table of the column belongs to.
     *
     * @changed OLI 14.12.2015 - Added.
     */
    public MetaDataTable getTable() {
        return this.table;
    }

    /**
     * Returns the name of the type of the column.
     *
     * @return The name of the type of the column.
     *
     * @changed OLI 11.03.2016 - Added.
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * Returns the SQL type name of the column.
     *
     * @param dbMode The mode of the DBMS whose model is to read.
     * @return The SQL type name of the column.
     */
    public String getSQLType(DBExecMode dbMode) {
        if (dbMode == DBExecMode.POSTGRESQL) {
            if (this.getTypeName().equalsIgnoreCase("Boolean")) {
                return "boolean";
            } else if (DBType.GetSQLType(this.dataType, dbMode).equalsIgnoreCase("double")) {
                return "double precision";
            }
        }
        return DBType.GetSQLType(this.dataType, dbMode) + (this.dataType.hasLength() ? "("
                + new Integer(this.colSize).toString() + (this.dataType.hasNKS() ? ", "
                + new Integer(this.decimalPlace).toString() : "") + ")" : "");
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /**
     * Checks if the column is indexed.
     *
     * @return <CODE>true</CODE> if the column is indexed.
     *
     * @changed OLI 17.12.2015 - Added.
     */
    public boolean isIndexed() {
        return this.indexed;
    }

    /**
     * Checks if the column is not nullable.
     *
     * @return <CODE>true</CODE> if the column is not nullable.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public boolean isNotNull() {
        if (this.primaryKey) {
            return true;
        }
        return this.notNull;
    }

    /**
     * Checks if the column is a primary key.
     *
     * @return <CODE>true</CODE> if the column is a primary key.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public boolean isPrimaryKey() {
        return this.primaryKey;
    }

    /**
     * Checks if the passed table name the one of the table which referenced by the column.
     *
     * @param tableName The table name to check.
     * @return <CODE>true</CODE> if the column references the table with the passed name.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public boolean isReferencingToTable(String tableName) {
        for (MetaDataForeignKeyConstraint fkc : this.fkConstraints) {
            if (fkc.getName().equalsIgnoreCase(tableName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the column is unique (inside single unique statement).
     *
     * @return <CODE>true</CODE> if the column is unique (inside single unique statement).
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public boolean isUnique() {
        return this.unique;
    }

    /**
     * Sets a new data type for the column.
     *
     * @param dataType A new data type for the column.
     *
     * @changed OLI 15.12.2015 - Added.
     */
    public void setDataType(DBType dataType) {
        this.dataType = dataType;
    }

    /**
     * Sets a new default value for the column.
     *
     * @param defaultValue A new default value for the column.
     *
     * @changed OLI 15.12.2015 - Added.
     */
    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }

    /**
     * Sets a new value for the indexed flag.
     *
     * @param indexed The new value for the indexed flag.
     *
     * @changed OLI 17.12.2015 - Added.
     */
    public void setIndexed(boolean indexed) {
        this.indexed = indexed;
    }

    /**
     * Sets the not null flag.
     *
     * @param notNull The new value for the not null flag of the column.
     *
     * @changed OLI 15.12.2015 - Added.
     */
    public void setNotNull(boolean notNull) {
        this.notNull = notNull;
    }

    /**
     * Sets the primary key flag.
     *
     * @param pk The new value for the primary key flag of the column.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public void setPrimaryKey(boolean pk) {
        this.primaryKey = pk;
    }

    /**
     * Sets the unique flag.
     *
     * @param unique The new value for the unique flag of the column.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public void setUnique(boolean unique) {
        this.unique = unique;
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}