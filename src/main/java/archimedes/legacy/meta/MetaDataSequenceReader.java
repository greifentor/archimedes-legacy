/*
 * MetaDataSequenceReader.java
 *
 * 10.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import static corentx.util.Checks.*;

import corent.db.*;
import corentx.util.*;

import java.sql.*;
import java.util.*;


/**
 * A reader for the sequence meta data.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.12.2015 - Added.
 */

public class MetaDataSequenceReader {

    /**
     * Adds the sequence meta data of the data model of the passed JDBC connection.
     *
     * @param model The meta model which the read sequences should be added to.
     * @param connection The connection whose sequence information from meta data should be
     *         read.
     * @param dbMode The mode of the DBMS whose data are read.
     * @throws SQLException In case of an error while accessing the database.
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public void addSequences(MetaDataModel model, JDBCDataSourceRecord connection,
            DBExecMode dbMode) throws SQLException {
        ensure(connection != null, "JDBC connection cannot be null.");
        ensure(model != null, "model cannot be null.");
        MetaDataSequence[] sequences = this.getSequenceMetaData(connection, model.getSchemeName(
                ), dbMode);
        for (MetaDataSequence s : sequences) {
            model.addSequence(s);
        }
    }

    private MetaDataSequence[] getSequenceMetaData(JDBCDataSourceRecord dsr, String schemaName,
            DBExecMode dbMode) throws SQLException {
        Connection c = ConnectionManager.GetConnection(dsr);
        List<String> seqNames = this.getSequenceNames(schemaName, c.getMetaData());
        List<MetaDataSequence> l = new Vector<MetaDataSequence>();
        for (String sn : seqNames) {
            l.add(this.createMetaDataSequence(schemaName, sn, c, dbMode));
        }
        c.close();
        return l.toArray(new MetaDataSequence[0]);
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

    private MetaDataSequence createMetaDataSequence(String schemaName, String name,
            Connection c, DBExecMode dbMode) throws SQLException {
        long incrementBy = -1;
        long startValue = -1;
        if (dbMode == DBExecMode.POSTGRESQL) {
            ResultSet rs = DBExec.Query(c, "SELECT \"start_value\", \"increment_by\" FROM \""
                    + (schemaName != null ? schemaName + "\".\"" : "") + name + "\"");
            if (rs.next()) {
                startValue = rs.getLong(1);
                incrementBy = rs.getLong(2);
            }
        }
        return new MetaDataSequence(name, startValue, incrementBy); 
    }

}