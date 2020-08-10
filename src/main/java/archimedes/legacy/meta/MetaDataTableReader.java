/*
 * MetaDataTableReader.java
 *
 * 04.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;


import static corentx.util.Checks.*;

import java.sql.*;

import corent.db.*;


/**
 * A reader for the table information of the data model meta data.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 04.12.2015 - Added.
 */

public class MetaDataTableReader {

    /**
     * Adds the table meta data of the data model of the passed JDBC connection.
     *
     * @param model The meta model which the read tables should be added to.
     * @param connection The connection whose table information from meta data should be read.
     * @param excludedTableNames Names of tables which should be excluded from meta data read.
     * @param dbMode The mode of the DBMS whose data are read.
     * @throws SQLException In case of an error while accessing the database.
     *
     * @changed OLI 04.12.2015 - Added.
     */
    public void addTables(MetaDataModel model, JDBCDataSourceRecord connection,
            String[] excludedTableNames, DBExecMode dbMode) throws SQLException {
        ensure(connection != null, "JDBC connection cannot be null.");
        ensure(excludedTableNames != null, "excluded table names cannot be null.");
        ensure(model != null, "model cannot be null.");
        this.getTablesFromMetaData(model, connection, excludedTableNames);
        this.fillTables(model, connection, excludedTableNames, dbMode);
        this.addForeignKeyConstraints(model, connection);
    }

    private void getTablesFromMetaData(MetaDataModel model, JDBCDataSourceRecord connection,
            String[] excludedTableNames) throws SQLException {
        Connection c = ConnectionManager.GetConnection(connection);
        DatabaseMetaData dmd = c.getMetaData();
        ResultSet rs = dmd.getTables(null, model.getSchemeName(), "%", new String[] {"TABLE"});
        while (rs.next()) {
            String n = rs.getString("TABLE_NAME");
            if (!this.isTableExcluded(n, excludedTableNames)) {
                model.addTable(new MetaDataTable(n, false));
            }
        }
        DBExec.CloseQuery(rs);
        c.close();
    }

    private boolean isTableExcluded(String name, String[] excludedTableNames) {
        for (String en : excludedTableNames) {
            if (name.equalsIgnoreCase(en)) {
                return true;
            }
        }
        return false;
    }

    private void fillTables(MetaDataModel model, JDBCDataSourceRecord connection,
            String[] excludedTableNames, DBExecMode dbMode) throws SQLException {
        for (MetaDataTable table : model.getTables()) {
            Connection c = ConnectionManager.GetConnection(connection);
            DatabaseMetaData dmd = c.getMetaData();
            ResultSet rs = dmd.getColumns(null, model.getSchemeName(), table.getName(), "%");
            while (rs.next()) {
                String n = rs.getString("COLUMN_NAME");
                short d = rs.getShort("DATA_TYPE");
                String t = rs.getString("TYPE_NAME");
                int colsize = rs.getInt("COLUMN_SIZE");
                int nks = rs.getInt("DECIMAL_DIGITS");
                boolean nn = (rs.getInt("NULLABLE") == DatabaseMetaData.columnNoNulls);
                String df = rs.getString("COLUMN_DEF");
                if (dbMode == DBExecMode.POSTGRESQL) {
                    if (t.equalsIgnoreCase("bool")) {
                        t = "boolean";
                    }
                    if (d == -2) {
                        d = Types.BLOB;
                    } else if ((d == 12) && (colsize == Integer.MAX_VALUE)
                            && t.equalsIgnoreCase("text")) {
                        d = Types.LONGVARCHAR;
                        colsize = 0;
                    }
                    if ((df != null) && df.endsWith("::character varying")) {
                        df = df.replace("::character varying", "");
                    }
                }
                DBType dbtype = DBType.Convert(d);
                if (!dbtype.hasLength()) {
                    colsize = 0;
                }
                if (!dbtype.hasNKS()) {
                    nks = 0;
                }
                table.addColumn(new MetaDataColumn(table, n, t, dbtype, colsize, nks, false, nn,
                        df, false));
            }
            DBExec.CloseQuery(rs);
            try {
                rs = dmd.getPrimaryKeys(null, null, table.getName());
                while (rs.next()) {
                    MetaDataColumn column = table.getColumn(rs.getString("COLUMN_NAME"));
                    if (column != null) {
                        column.setPrimaryKey(true);
                    }
                }
                DBExec.CloseQuery(rs);
            } catch (java.sql.SQLException sqle) {
            }
            c.close();
        }
    }

    private void addForeignKeyConstraints(MetaDataModel model, JDBCDataSourceRecord connection
            ) throws SQLException {
        MetaDataForeignKeyCollector fkc = new MetaDataForeignKeyCollector();
        for (MetaDataTable table : model.getTables()) {
            Connection c = ConnectionManager.GetConnection(connection);
            DatabaseMetaData dmd = c.getMetaData();
            ResultSet rs = dmd.getExportedKeys(null, model.getSchemeName(), table.getName());
            while (rs.next()) {
                String pkTableName = rs.getString("PKTABLE_NAME");
                String pkColumnName = rs.getString("PKCOLUMN_NAME");
                String fkTableName = rs.getString("FKTABLE_NAME");
                String fkColumnName = rs.getString("FKCOLUMN_NAME");
                String fkName = rs.getString("FK_NAME");
                fkc.addMember(fkName, model.getTable(fkTableName), fkColumnName,
                        model.getTable(pkTableName), pkColumnName);
            }
            DBExec.CloseQuery(rs);
            c.close();
        }
        fkc.transferToModel(model);
    }

}