/*
 * MetaDataIndex.java
 *
 * 07.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.*;

import corentx.util.*;

import java.util.*;

import org.apache.commons.lang3.builder.*;


/**
 * A representation of a database index.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.12.2015 - Added.
 */

public class MetaDataIndex extends MetaDataNamedObject {

    private List<String> columnNames = new SortedVector<String>();
    private MetaDataTable table = null;

    /**
     * Creates a new index representation with the passed parameters.
     *
     * @param name The name of the index.
     * @param table The table which the index belongs to.
     * @param columns The columns which the index is created for.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public MetaDataIndex(String name, MetaDataTable table, MetaDataColumn[] columns) {
        super(name);
        ensure(columns != null, "columns cannot be null.");
        ensure(table != null, "table cannot be null.");
        for (MetaDataColumn c : columns) {
            this.addColumn(c);
        }
        this.table = table;
    }

    /**
     * Creates a new index representation for a single field with the passed parameters.
     *
     * @param name The name of the index.
     * @param table The table which the index belongs to.
     * @param column The column which the index is created for.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public MetaDataIndex(String name, MetaDataTable table, MetaDataColumn column) {
        this(name, table, new MetaDataColumn[] {column});
    }

    /**
     * Adds the passed column to the index.
     *
     * @param column The column which is to add.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public void addColumn(MetaDataColumn column) {
        ensure(column != null, "column cannot be null.");
        this.addColumn(column.getName());
    }

    /**
     * Adds the passed column to the index.
     *
     * @param column The column which is to add.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public void addColumn(String columnName) {
        ensure(columnName != null, "column cannot be null.");
        if (!this.columnNames.contains(columnName)) {
            this.columnNames.add(columnName);
        }
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    @Override public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
        /*
        if (o.getClass() != MetaDataIndex.class) {
            return false;
        }
        MetaDataIndex id = (MetaDataIndex) o;
        boolean ok = this.getName().equals(id.getName());
        ok = ok && this.getTable().getName().equals(id.getTable().getName());
        String[] thisCols = this.getColumnNames();
        String[] idCols = id.getColumnNames();
        if (thisCols.length != idCols.length) {
            for (int i = 0; i < thisCols.length; i++) {
                ok = ok && (thisCols[i].equals(idCols[i]));
            }
        }
        return ok;
        */
    }

    /**
     * Returns the column which belong to the index.
     *
     * @return The column which belong to the index.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public String[] getColumnNames() {
        return this.columnNames.toArray(new String[0]);
    }

    /**
     * Returns the table which the index is related to.
     *
     * @return The table which the index is related to.
     *
     * @changed OLI 07.12.2015 - Added.
     */
    public MetaDataTable getTable() {
        return this.table;
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /**
     * @changed OLI 14.12.2015 - Added.
     */
    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.MULTI_LINE_STYLE);
    }

}