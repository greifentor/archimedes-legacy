/*
 * SequenceMetaDataReader.java
 *
 * 23.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.metadata;


import java.sql.*;
import java.util.*;

import corent.db.*;
import corentx.util.*;


/**
 * A class to read the meta data of the sequences of a data base.
 *
 * @author ollie
 *
 * @changed OLI 23.04.2013 - Added.
 */

public class SequenceMetaDataReader {

    /**
     * Returns the sequence meta data for the passed connection.
     *
     * @param dsr The data source record with the data to connect the reader with the data base
     *         whose sequence meta data should be extracted.
     * @param schemaName The name of the schema which the sequences are read for.
     * @return The sequence meta data of the passed connection.
     * @throws SQLException If an error occurs while reading the meta data.
     *
     * @changed OLI 23.04.2013 - Added.
     */
    public SequenceMetaData[] getSequenceMetaData(JDBCDataSourceRecord dsr, String schemaName)
            throws SQLException {
        Connection c = ConnectionManager.GetConnection(dsr);
        List<String> seqNames = this.getSequenceNames(schemaName, c.getMetaData());
        List<SequenceMetaData> l = new Vector<SequenceMetaData>();
        for (String sn : seqNames) {
            l.add(new SequenceMetaData(sn));
        }
        c.close();
        return l.toArray(new SequenceMetaData[0]);
    }

    private List<String> getSequenceNames(String schemaName, DatabaseMetaData dmd)
            throws SQLException {
        List<String> l = new SortedVector<String>();
        ResultSet rs = dmd.getTables(null, schemaName, "%", new String[] {"SEQUENCE"});
        while (rs.next()) {
            l.add(rs.getString("TABLE_NAME"));
        }
        DBExec.CloseQuery(rs);
        return l;
    }

}