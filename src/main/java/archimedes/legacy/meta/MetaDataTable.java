/*
 * MetaDataTable.java
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

import corentx.util.*;


/**
 * A representation of a database table.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.12.2015 - Added.
 */

public class MetaDataTable extends MetaDataNamedObjectCouldBeDeprecated {

    private List<MetaDataColumn> columns = new SortedVector<MetaDataColumn>();
    private boolean externalTable = false;
    private List<MetaDataIndex> indices = new SortedVector<MetaDataIndex>();
    private List<MetaDataUniqueConstraint> uniques = new SortedVector<MetaDataUniqueConstraint>(
            );
    private List<MetaDataComplexForeignKey> complexFKs =
            new SortedVector<MetaDataComplexForeignKey>();

    /**
     * Creates a new table for the meta data model.
     *
     * @param name The name of the table.
     * @param externalTable Set this flag if the table is an external table.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataTable(String name, boolean externalTable) {
        this(name, externalTable, false);
    }

    /**
     * Creates a new table for the meta data model.
     *
     * @param name The name of the table.
     * @param externalTable Set this flag if the table is an external table.
     * @param isDeprecated Set this flag if the table is deprecated.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataTable(String name, boolean externalTable, boolean isDeprecated) {
        super(name, isDeprecated);
        this.setExternalTable(externalTable);
    }

    /**
     * Adds the passed column to the table.
     *
     * @param column The column to add to the table.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public void addColumn(MetaDataColumn column) {
        ensure(column != null, "column to add cannot be null.");
        ensure(this.getColumn(column.getName()) == null, "column with passed name is already "
                + "existing: " + column.getName());
        this.columns.add(column);
    }

    /**
     * Adds the passed complex foreign key to the model.
     * 
     * @param complexForeignKey The complex foreign key to add.
     *
     * @changed OLI 28.09.2017 - Added.
     */
    public void addComplexForeignKey(MetaDataComplexForeignKey complexForeignKey) {
        ensure(complexForeignKey != null, "complex foreign key to add cannot be null.");
        if (!this.complexFKs.contains(complexForeignKey)) {
            complexForeignKey.setSourceTable(this);
            this.complexFKs.add(complexForeignKey);
        }
    }

    /**
     * Adds the passed index to the model.
     *
     * @param index The index to add.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public void addIndex(MetaDataIndex index) {
        ensure(index != null, "index to add cannot be null.");
        if (!this.indices.contains(index)) {
            this.indices.add(index);
        }
    }

    /**
     * Adds the passed unique constraint to the model.
     *
     * @param uniqueConstraint The unique constraint to add.
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public void addUniqueConstraint(MetaDataUniqueConstraint uniqueConstraint) {
        ensure(uniqueConstraint != null, "unique constraint to add cannot be null.");
        if (!this.uniques.contains(uniqueConstraint)) {
            this.uniques.add(uniqueConstraint);
        }
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    @Override public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    /**
     * Returns the complex foreign key with the passed name.
     *
     * @param fkName The name of complex foreign key which is to return.
     * @return The complex foreign key with the passed name or a <CODE>null</CODE> value if
     *         there is no complex foreign key with the passed name.
     *
     * @changed OLI 28.09.2017 - Added.
     */
    public MetaDataComplexForeignKey getComplexForeignKey(String fkName) {
        for (MetaDataComplexForeignKey fk : this.complexFKs) {
            if (fk.getName().equals(fkName)) {
                return fk;
            }
        }
        return null;
    }

    /**
     * Returns the complex foreign keys of the table.
     *
     * @return The complex foreign keys of the table.
     *
     * @changed OLI 28.09.2017 - Added.
     */
    public MetaDataComplexForeignKey[] getComplexForeignKeys() {
        return this.complexFKs.toArray(new MetaDataComplexForeignKey[0]);
    }

    /**
     * Returns the column with the passed name.
     *
     * @param name The name of the column which is to return or <CODE>null</CODE> if no column
     *         exists with the passed name.
     * @return The column with the passed name.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataColumn getColumn(String name) {
        for (MetaDataColumn column : this.columns) {
            if (column.getName().equals/*IgnoreCase*/(name)) {
                return column;
            }
        }
        return null;
    }

    /**
     * Returns the columns of the table.
     *
     * @return The columns of the table.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataColumn[] getColumns() {
        return this.columns.toArray(new MetaDataColumn[0]);
    }

    /**
     * Returns the index with the passed name.
     *
     * @param name The name of the index which should be returned.
     * @return The index with the passed name or <CODE>null</CODE> if there is no index with
     *         this name..
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public MetaDataIndex getIndex(String name) {
        for (MetaDataIndex i : this.getIndices()) {
            if (i.getName().equals(name)) {
                return i;
            }
        }
        return null;
    }

    /**
     * Returns the indices of the scheme.
     *
     * @return The indices of the scheme.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public MetaDataIndex[] getIndices() {
        return this.indices.toArray(new MetaDataIndex[0]);
    }

    /**
     * Returns a list with the primary key members of the table.
     *
     * @return A list with the primary key members of the table.
     *
     * @changed OLI 15.12.2015 - Added.
     */
    public MetaDataColumn[] getPrimaryKeyMembers() {
        List<MetaDataColumn> l = new LinkedList<MetaDataColumn>();
        for (MetaDataColumn c : this.columns) {
            if (c.isPrimaryKey()) {
                l.add(c);
            }
        }
        return l.toArray(new MetaDataColumn[0]);
    }

    /**
     * Returns the unique constraint with the passed name.
     *
     * @param name The name of the unique constraint which is to return.
     * @return The unique constraint with the passed name or <CODE>null</CODE> if there is no
     *         unique constraint with the passed name.
     *
     * @changed OLI 16.12.2015 - Added.
     */
    public MetaDataUniqueConstraint getUniqueConstraint(String name) {
        for (MetaDataUniqueConstraint uc : this.getUniqueConstraints()) {
            if (uc.getName().equals(name)) {
                return uc;
            }
        }
        return null;
    }

    /**
     * Returns the unique constraints of the scheme.
     *
     * @return The unique constraints of the scheme.
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public MetaDataUniqueConstraint[] getUniqueConstraints() {
        return this.uniques.toArray(new MetaDataUniqueConstraint[0]);
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /**
     * Checks if the table is an external table.
     *
     * @return <CODE>true</CODE> if the table is an external table.
     *
     * @changed OLI 16.12.2015 - Added.
     */
    public boolean isExternalTable() {
        return this.externalTable;
    }

    /**
     * Removes the unique constraint with the passed name if there is one.
     *
     * @param name The name of the unique constraint which is to remove.
     *
     * @changed OLI 17.02.2016 - Added.
     */
    public void removeUniqueConstraint(String name) {
        MetaDataUniqueConstraint uc = this.getUniqueConstraint(name);
        if (uc != null) {
            this.uniques.remove(uc);
        }
    }

    /**
     * Sets a new value for the external table flag.
     *
     * @param externalTable The new value for the external table flag.
     *
     * @changed OLI 16.12.2015 - Added.
     */
    public void setExternalTable(boolean externalTable) {
        this.externalTable = externalTable;
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}