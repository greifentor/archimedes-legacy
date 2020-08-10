/*
 * MetaDataForeignKeyCollector.java
 *
 * 27.09.2017
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.*;

import java.util.*;

/**
 * A class which collects and groups foreign keys by their name.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 27.09.2017 - Added.
 */

public class MetaDataForeignKeyCollector {

    private Map<String, MetaDataComplexForeignKey> m =
            new Hashtable<String, MetaDataComplexForeignKey>();

    /**
     * Adds the passed foreign key member to the collector.
     *
     * @param fkName The name of the foreign key.
     * @param sourceTableName The name of the source table.
     * @param sourceColumnName The name of the source column.
     * @param targetTableName The name of the target table.
     * @param targetColumnName The name of the target column.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public void addMember(String fkName, MetaDataTable sourceTable, String sourceColumnName,
            MetaDataTable targetTable, String targetColumnName) {
        MetaDataComplexForeignKey fk = this.m.get(fkName);
        if (fk == null) {
            fk = new MetaDataComplexForeignKey(fkName, sourceTable, targetTable);
            this.m.put(fkName, fk);
        } else {
            ensure(fk.getSourceTable().equals(sourceTable), "source tables are different for "
                    + "foreign key '" + fkName + "': " + fk.getSourceTable().getName() 
                    + " != " + sourceTable.getName());
            ensure(fk.getTargetTable().equals(targetTable), "target tables are different for "
                    + "foreign key '" + fkName + "': " + fk.getTargetTable().getName()
                    + " != " + targetTable.getName());
        }
        fk.addSourceColumnName(sourceColumnName);
        fk.addTargetColumnName(targetColumnName);
        ensure(fk.getSourceColumns().length == fk.getTargetColumns().length,
                "columns count of source and target table differs: "
                + fk.getSourceColumns().length + " != " + fk.getTargetColumns().length
                + ", fkName=" + fkName);
    }

    /**
     * Creates the necessary structures in the passed meta data model to represent the foreign
     * key relations of the collector.
     *
     * @param model The meta data model which the foreign keys are to add.
     *
     * @changed OLI 27.09.2017 - Added.
     */
    public void transferToModel(MetaDataModel model) {
        for (String fkName : this.m.keySet()) {
            MetaDataComplexForeignKey fk = m.get(fkName);
            MetaDataTable table = fk.getSourceTable();
            if (table != null) {
                if (fk.getSourceColumns().length == 1) {
                    MetaDataColumn column = fk.getSourceColumns()[0];
                    if (column != null) {
                        MetaDataColumn referencedColumn = fk.getTargetColumns()[0];
                        column.addForeignKeyConstraint(new MetaDataForeignKeyConstraint(fkName,
                                referencedColumn));
                    }
                } else if (fk.getSourceColumns().length > 1) {
                    table.addComplexForeignKey(fk);
                }
            }
        }
    }

}