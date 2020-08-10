/*
 * UniqueMetaData.java
 *
 * 11.06.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.model;


/**
 * The data of an unique constraint.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 11.06.2013 - Added.
 */

public class UniqueMetaData extends SimpleConstraintMetaData {

    /**
     * Creates a new <CODE>UniqueMetaData</CODE> with the passed parameters.
     *
     * @param constraintName The name of the unique constraint.
     * @param tableName The name of the table which the unique constraint is valid for.
     * @throws IllegalArgumentException In case of precondition violation.
     * @precondition constraintName != <CODE>null</CODE>
     * @precondition !constraintName.isEmpty()
     * @precondition tableName != <CODE>null</CODE>
     * @precondition !tableName.isEmpty()
     *
     * @changed OLI 11.06.2013 - Added.
     */
    public UniqueMetaData(String indexName, String tableName) {
        super(indexName, tableName);
    }

}