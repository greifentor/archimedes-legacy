/*
 * MetaDataForeignKeyConstraint.java
 *
 * 04.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.*;

import org.apache.commons.lang3.builder.*;


/**
 * A representation of a foreign key constraint.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.12.2015 - Added.
 */

public class MetaDataForeignKeyConstraint extends MetaDataNamedObject {

    private MetaDataColumn referencedColumn = null;

    /**
     * Creates a new meta data foreign key constraint.
     *
     * @param name The name of the constraint.
     * @param referencedColumn The column which is referenced by the constraint as foreign key.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataForeignKeyConstraint(String name, MetaDataColumn referencedColumn) {
        super(name);
        ensure(referencedColumn != null, "referenced column cannot be null.");
        this.referencedColumn = referencedColumn;
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    @Override public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o, false);
    }

    /**
     * Returns a string with the comma separated full names of the columns.
     *
     * @return 
     * 
     * @changed OLI 26.09.2017 - Added.
     */
    public String getReferencedColumnFullName() {
        return this.getReferencedColumn().getFullName();
    }

    /**
     * Returns the column which is referenced by the constraint.
     *
     * @return The column which is referenced by the constraint.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataColumn getReferencedColumn() {
        return this.referencedColumn;
    }

    /**
     * @changed OLI 17.12.2015 - Added.
     */
    @Override public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this, false);
    }

    /**
     * @changed OLI 15.12.2015 - Added.
     */
    @Override public String toString() {
        return "MetaDataForeignKeyConstraint(name=" + this.getName() + ", referencedColumn="
                + this.getReferencedColumnFullName();
    }

}