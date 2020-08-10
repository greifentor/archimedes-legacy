/*
 * MetaDataIndexReader.java
 *
 * 07.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import archimedes.legacy.model.SimpleIndexMetaData;
import corent.db.ConnectionManager;
import corent.db.DBExec;
import corent.db.DBExecMode;
import corent.db.JDBCDataSourceRecord;

/**
 * A reader which extracts the index meta data.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 07.12.2015 - Added.
 */

public class MetaDataIndexReader {

	/**
	 * Adds the index meta data of the data model of the passed JDBC connection.
	 *
	 * @param model              The meta model which the read indices should be added to.
	 * @param connection         The connection whose index information from meta data should be read.
	 * @param excludedTableNames Names of tables whose indices should be excluded from meta data read.
	 * @param dbMode             The mode of the DBMS whose data are read.
	 * @throws SQLException In case of an error while accessing the database.
	 *
	 * @changed OLI 07.12.2015 - Added.
	 */
	public void addIndices(MetaDataModel model, JDBCDataSourceRecord connection, String[] excludedTableNames,
			DBExecMode dbMode) throws SQLException {
		Connection c = ConnectionManager.GetConnection(connection);
		Map<String, SimpleIndexMetaData> m = new Hashtable<String, SimpleIndexMetaData>();
		for (MetaDataTable table : model.getTables()) {
			ResultSet rs = c.getMetaData().getIndexInfo(null, model.getSchemeName(), table.getName(), false, false);
			while (rs.next()) {
				String s = rs.getString("INDEX_NAME");
				boolean nonUniqueConstraint = rs.getBoolean("NON_UNIQUE");
				if ((s != null) && (nonUniqueConstraint)) {
					SimpleIndexMetaData imd = m.get(s);
					if (imd == null) {
						imd = new SimpleIndexMetaData(s, table.getName());
						m.put(s, imd);
					}
					String cn = rs.getString("COLUMN_NAME");
					cn = cn.replace("\"", "").replace("'", "");
					imd.addColumn(cn);
				}
			}
			DBExec.CloseQuery(rs);
		}
		ConverterToMetaDataColumn converter = new ConverterToMetaDataColumn();
		for (SimpleIndexMetaData imd : m.values()) {
			MetaDataTable t = model.getTable(imd.getTable());
			if (imd.getColumns().length == 1) {
				MetaDataColumn col = t.getColumn(imd.getColumns()[0]);
				col.setIndexed(true);
			} else if (imd.getColumns().length > 1) {
				t.addIndex(new MetaDataIndex(imd.getName(), t, converter.convertToMetaDataColumn(t, imd.getColumns())));
			}
		}
	}

}