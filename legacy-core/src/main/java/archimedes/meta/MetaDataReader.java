/*
 * MetaDataReader.java
 *
 * 04.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.meta;

import archimedes.acf.param.*;
import archimedes.model.*;

import java.sql.*;

import corent.db.*;


/**
 * This class reads the meta data from a JDBC connection.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.12.2015 - Added.
 */

public class MetaDataReader {

    /**
     * Reads the meta data from the passed JDBC connection and returns this meta data.
     *
     * @param connection The connection whose meta data are to read.
     * @param schemeName The name of the scheme to read.
     * @param dbMode The mode of the DBMS whose meta data are to read.
     * @return A meta data model.
     * @throws SQLException If an error occurs while reading the meta data.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public MetaDataModel read(JDBCDataSourceRecord connection, String schemeName,
            DBExecMode dbMode) throws SQLException {
        String[] excl = new String[0];
        MetaDataModel m = new MetaDataModel(schemeName);
        MetaDataTableReader tr = new MetaDataTableReader();
        tr.addTables(m, connection, excl, dbMode);
        MetaDataIndexReader ir = new MetaDataIndexReader();
        ir.addIndices(m, connection, excl, dbMode);
        MetaDataUniqueConstraintReader ucr = new MetaDataUniqueConstraintReader();
        ucr.addUniqueConstraints(m, connection, excl, dbMode);
        if (dbMode == DBExecMode.POSTGRESQL) {
            MetaDataUniqueWithNullableConstraintReader uwncr =
                    new MetaDataUniqueWithNullableConstraintReader();
            uwncr.addUniqueWithNullableConstraints(m, connection, excl, dbMode);
        }
        MetaDataSequenceReader usr = new MetaDataSequenceReader();
        usr.addSequences(m, connection, dbMode);
        return m;
    }

    /**
     * Reads the meta data from the passed data model.
     *
     * @param dataModel The data model whose meta data are to read.
     * @param dbMode A DBMS mode which the meta data model is to compare with or
     *         <CODE>null</CODE> if there is information is irrelevant.
     * @return A meta data model.
     * @throws SQLException If an error occurs while reading the meta data.
     *
     * @changed OLI 10.12.2015 - Added.
     */
    public MetaDataModel read(DataModel dataModel, DBExecMode dbMode) {
        DataModelToMetaDataConverter converter = new DataModelToMetaDataConverter(dbMode);
        MetaDataModel m = new MetaDataModel(dataModel.getName(),
                this.getTemporaryPrefixIfExists(dataModel));
        converter.addTables(m, dataModel.getTables());
        converter.addComplexIndices(m, dataModel.getComplexIndices());
        converter.addSequences(m, dataModel.getSequences());
        return m;
    }

    private String getTemporaryPrefixIfExists(DataModel dataModel) {
        if (dataModel.isOptionSet(ModelParamIds.TEMPORARY)) {
            return dataModel.getOptionByName(ModelParamIds.TEMPORARY).getParameter();
        }
        return null;
    }

}