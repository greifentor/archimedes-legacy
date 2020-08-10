/*
 * MetaDataUniqueConstraint.java
 *
 * 08.12.2015
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
 * A representation of a unique constraint.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.12.2015 - Added.
 */

public class MetaDataUniqueConstraint extends MetaDataNamedObject {

    private List<MetaDataColumn> columns = new SortedVector<MetaDataColumn>();
    private MetaDataTable table = null;

    /**
     * Creates a new unique constraint representation with the passed parameters.
     *
     * @param name The name of the unique constraint.
     * @param table The table which the unique constraint belongs to.
     * @param columns The columns which the unique constraint is created for.
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public MetaDataUniqueConstraint(String name, MetaDataTable table, MetaDataColumn[] columns)
            {
        super(name);
        ensure(columns != null, "columns cannot be null.");
        ensure(columns.length > 0, "columns cannot be empty.");
        this.table = columns[0].getTable();
        for (MetaDataColumn c : columns) {
            ensure(c != null, "column cannot be null.");
            ensure(this.table == c.getTable(), "column is not of the right table: "
                    + c.getFullName());
            this.columns.add(c);
        }
    }

    /**
     * Adds the passed column to the index.
     *
     * @param column The column which is to add.
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public void addColumn(MetaDataColumn column) {
        ensure(column != null, "column cannot be null.");
        if (!this.columns.contains(column)) {
            this.columns.add(column);
        }
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    @Override public boolean equals(Object o) {
        if (!(o instanceof MetaDataUniqueConstraint)) {
            return false;
        }
        MetaDataUniqueConstraint uc = (MetaDataUniqueConstraint) o;
        boolean ok = this.getName().equals(uc.getName());
        ok = ok & this.getTable().getName().equals(uc.getTable().getName());
        MetaDataColumn[] thisColumns = this.getColumns();
        MetaDataColumn[] ucColumns = uc.getColumns();
        if (thisColumns.length != ucColumns.length) {
            return false;
        }
        for (int i = 0; i < thisColumns.length; i++) {
            ok = ok & thisColumns[i].getName().equals(ucColumns[i].getName());
        }
        return ok;
    }

    /**
     * Returns the column which belong to the unique constraint.
     *
     * @return The column which belong to the unique constraint.
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public MetaDataColumn[] getColumns() {
        return this.columns.toArray(new MetaDataColumn[0]);
    }

    /**
     * Returns the table which the unique constraint is related to.
     *
     * @return The table which the unique constraint is related to.
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public MetaDataTable getTable() {
        return this.table;
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /**
     * @changed OLI 16.12.2015 - Added.
     */
    @Override public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}