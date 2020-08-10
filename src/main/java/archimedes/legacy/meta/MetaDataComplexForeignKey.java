/*
 * MetaDataComplexForeignKey.java
 *
 * 27.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.*;

import java.util.*;

import org.apache.commons.lang3.builder.*;

/**
 * A representation of a complex foreign key in the meta data model.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.09.2017 - Added.
 */

public class MetaDataComplexForeignKey extends MetaDataNamedObject {

    private MetaDataTable sourceTable = null;
    private List<MetaDataColumn> sourceColumns = new LinkedList<MetaDataColumn>();
    private MetaDataTable targetTable = null;
    private List<MetaDataColumn> targetColumns = new LinkedList<MetaDataColumn>();

    /**
     * Creates a new complex foreign key with the passed parameters.
     *
     * @param name The name of the foreign key.
     * @param sourceTable The table which references the target table.
     * @param targetTable The table which is referenced by the source table.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public MetaDataComplexForeignKey(String name, MetaDataTable sourceTable,
            MetaDataTable targetTable) {
        super(name);
        ensure(sourceTable != null, "source table cannot be null.");
        ensure(targetTable != null, "target table cannot be null.");
        this.sourceTable = sourceTable;
        this.targetTable = targetTable;
    }

    /**
     * Adds the passed source column name.
     *
     * @param sourceColumnName The new source column name to add.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public void addSourceColumnName(String sourceColumnName) {
        ensure(sourceColumnName != null, "source column name cannot be null.");
        ensure(!sourceColumnName.isEmpty(), "source column name cannot be empty.");
        MetaDataColumn c = this.sourceTable.getColumn(sourceColumnName);
        ensure(c != null, "column with name '" + sourceColumnName + "' does not exists!");
        if (!this.sourceColumns.contains(c)) {
            this.sourceColumns.add(c);
        }
    }

    /**
     * Adds the passed target column name.
     *
     * @param targetColumnName The new target column name to add.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public void addTargetColumnName(String targetColumnName) {
        ensure(targetColumnName != null, "target column name cannot be null.");
        ensure(!targetColumnName.isEmpty(), "target column name cannot be empty.");
        MetaDataColumn c = this.targetTable.getColumn(targetColumnName);
        ensure(c != null, "column with name '" + targetColumnName + "' does not exists!");
        if (!this.targetColumns.contains(c)) {
            this.targetColumns.add(c);
        }
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    @Override public boolean equals(Object o) {
        // For complex foreign key comparisons are table and column names, as well as the
        // constraint name relevant only.
        if (!(o instanceof MetaDataComplexForeignKey)) {
            return false;
        }
        MetaDataComplexForeignKey cfk = (MetaDataComplexForeignKey) o;
        if (!this.getName().equals(cfk.getName())) {
            return false;
        }
        if (!this.getSourceTable().getName().equals(cfk.getSourceTable().getName())) {
            return false;
        }
        if (!this.getTargetTable().getName().equals(cfk.getTargetTable().getName())) {
            return false;
        }
        return this.equalsInColumnNames(this.getSourceColumns(), cfk.getSourceColumns())
                && this.equalsInColumnNames(this.getTargetColumns(), cfk.getTargetColumns());
    }

    private boolean equalsInColumnNames(MetaDataColumn[] l0, MetaDataColumn[] l1) {
        int cnt = 0;
        for (MetaDataColumn c : l0) {
            if (this.containsColumnWithName(c.getName(), l1)) {
                cnt++;
            }
        }
        return (l0.length == cnt) && (l0.length == l1.length);
    }

    private boolean containsColumnWithName(String name, MetaDataColumn[] l) {
        for (MetaDataColumn c : l) {
            if (c.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns the source columns of the foreign key.
     *
     * @return The source columns of the foreign key.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public MetaDataColumn[] getSourceColumns() {
        return this.sourceColumns.toArray(new MetaDataColumn[0]);
    }

    /**
     * Returns the source table of the complex foreign key.
     *
     * @return The source table of the complex foreign key.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public MetaDataTable getSourceTable() {
        return this.sourceTable;
    }

    /**
     * Returns the target column names of the foreign key.
     *
     * @return The target column names of the foreign key.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public MetaDataColumn[] getTargetColumns() {
        return this.targetColumns.toArray(new MetaDataColumn[0]);
    }

    /**
     * Returns the target table of the complex foreign key.
     *
     * @return The target table of the complex foreign key.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public MetaDataTable getTargetTable() {
        return this.targetTable;
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /**
     * Sets a new source table for the complex foreign key.
     *
     * @param table The new source table for the complex foreign key.
     *
     * @changed OLI 28.09.2017 - Added.
     */
    public void setSourceTable(MetaDataTable table) {
        ensure(table != null, "table cannot be null.");
        for (MetaDataColumn c : table.getColumns()) {
            ensure(table.equals(c.getTable()), "table '" + table.getName() + "' does not match"
                    + " for column '" + c.getFullName() + "'.");
        }
        this.sourceTable = table;
    }

    /**
     * @changed OLI 09.10.2017 - Added.
     */
    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}