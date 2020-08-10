/*
 * ConverterToMetaDataColumn.java
 *
 * 08.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import corentx.util.*;

import java.util.*;


/**
 * A converter which converts DBMS meta columns to meta data columns.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.12.2015 - Added.
 */

public class ConverterToMetaDataColumn {

    /**
     * Returns the meta data columns for the passed column names.
     *
     * @param table The table which the columns should belong to.
     * @param columnNames The names of the columns which are to return.
     * @return The meta data columns for the passed column names.
     *
     * @changed OLI 08.12.2015 - Added.
     */
    public MetaDataColumn[] convertToMetaDataColumn(MetaDataTable table, String[] columnNames) {
        List<MetaDataColumn> l = new SortedVector<MetaDataColumn>();
        for (String cn : columnNames) {
            cn = cn.replace("\"", "").replace("'", "");
            MetaDataColumn c = table.getColumn(cn);
            if (c != null) {
                l.add(c);
            }
        }
        return l.toArray(new MetaDataColumn[0]);
    }

}