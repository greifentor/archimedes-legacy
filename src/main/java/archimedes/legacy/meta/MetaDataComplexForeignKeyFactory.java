/*
 * MetaDataComplexForeignKeyFactory.java
 *
 * 29.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import archimedes.grammar.cfk.*;

/**
 * A factory for meta data complex foreign keys.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 29.09.2017 - Added.
 */

public class MetaDataComplexForeignKeyFactory {

    /**
     * Creates a new meta data complex foreign key from the passed complex foreign key model.
     *
     * @param fkName The name of the foreign key.
     * @param sourceTable The table which acts as referencer.
     * @param targetTable The table which is referenced by the source table.
     * @param cfk The complex foreign key model whose data are used to create the meta data
     *         complex foreign key.
     * @return The meta data complex foreign key for the passed complex foreign key model.
     *
     * @changed OLI 29.09.2017 - Added.
     */
    public MetaDataComplexForeignKey create(String fkName, MetaDataTable sourceTable,
            MetaDataTable targetTable, ComplexForeignKeyModel cfk) {
        MetaDataComplexForeignKey mdcfk = new MetaDataComplexForeignKey(fkName, sourceTable,
                targetTable);
        for (String cn : cfk.getColumnNames()) {
            mdcfk.addSourceColumnName(cn);
        }
        for (String cn : cfk.getTargetColumnNames()) {
            mdcfk.addTargetColumnName(cn);
        }
        return mdcfk;
    }

}