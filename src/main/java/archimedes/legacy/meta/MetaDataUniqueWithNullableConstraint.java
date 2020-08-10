/*
 * MetaDataUniqueWithNullableConstraint.java
 *
 * 17.02.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.*;


/**
 * A representation of a unique constraint which is able to work with one nullable field.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.02.2016 - Added.
 */

public class MetaDataUniqueWithNullableConstraint extends MetaDataUniqueConstraint {

    private MetaDataColumn nullable = null;

    /**
     * Creates a new unique constraint representation with the passed parameters.
     *
     * @param name The name of the unique constraint.
     * @param table The table which the unique constraint belongs to.
     * @param columns The columns which the unique constraint is created for.
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public MetaDataUniqueWithNullableConstraint(String name, MetaDataTable table,
            MetaDataColumn nullable, MetaDataColumn[] columns)
            {
        super(name, table, columns);
        ensure(nullable != null, "nullable cannot be null.");
        this.nullable = nullable;
    }

    /**
     * Returns the nullable field.
     *
     * @return The nullable field.
     *
     * @changed OLI 17.02.2016 - Added.
     */
    public MetaDataColumn getNullable() {
        return this.nullable;
    }

}