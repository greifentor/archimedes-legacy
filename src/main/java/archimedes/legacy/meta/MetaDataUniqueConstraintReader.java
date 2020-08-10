/*
 * MetaDataUniqueConstraintReader.java
 *
 * 08.12.2015
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import archimedes.legacy.model.UniqueMetaData;
import corent.base.SortedVector;
import corent.db.ConnectionManager;
import corent.db.DBExec;
import corent.db.DBExecMode;
import corent.db.JDBCDataSourceRecord;

/**
 * A reader for unique constraints from the database meta data.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.12.2015 - Added.
 */

public class MetaDataUniqueConstraintReader {

	/**
	 * Adds the unique constraints meta data of the data model of the passed JDBC connection.
	 *
	 * @param model              The meta model which the read unique constraints should be added to.
	 * @param connection         The connection whose unique constraint information from meta data should be read.
	 * @param excludedTableNames Names of tables whose unique constraints should be excluded from meta data read.
	 * @param dbMode             The mode of the DBMS whose data are read.
	 * @throws SQLException In case of an error while accessing the database.
	 *
	 * @changed OLI 08.12.2015 - Added.
	 */
	public void addUniqueConstraints(MetaDataModel model, JDBCDataSourceRecord connection, String[] excludedTableNames,
			DBExecMode dbMode) throws SQLException {
		Connection c = ConnectionManager.GetConnection(connection);
		this.getUniqueMetaData(model, c);
		c.close();
	}

	private void getUniqueMetaData(MetaDataModel model, Connection c) throws SQLException {
		boolean uniqueConstraint = false;
		Map<String, UniqueMetaData> m = new Hashtable<String, UniqueMetaData>();
		ResultSet rs = null;
		List<String> pkNames = new LinkedList<String>();
		SortedVector<String> sv = new SortedVector<String>();
		String s = null;
		rs = c.getMetaData().getTables(null, model.getSchemeName(), "%", new String[] { "TABLE" });
		while (rs.next()) {
			s = rs.getString("TABLE_NAME");
			if (s != null) {
				sv.addElement(s);
			}
		}
		DBExec.CloseQuery(rs);
		for (String tn : sv) {
			rs = c.getMetaData().getPrimaryKeys(null, model.getSchemeName(), tn);
			while (rs.next()) {
				s = rs.getString("PK_NAME");
				if (s != null) {
					pkNames.add(s);
				}
			}
			DBExec.CloseQuery(rs);
		}
		for (int i = 0, leni = sv.size(); i < leni; i++) {
			String tableName = sv.elementAt(i).toString();
			rs = c.getMetaData().getIndexInfo(null, model.getSchemeName(), tableName, true, false);
			while (rs.next()) {
				s = rs.getString("INDEX_NAME");
				if (pkNames.contains(s) || s.endsWith("_unique_index") || s.endsWith("_unique_index_nullable")) {
					continue;
				}
				uniqueConstraint = !rs.getBoolean("NON_UNIQUE");
				if ((s != null) && (uniqueConstraint)) {
					UniqueMetaData umd = m.get(s);
					if (umd == null) {
						umd = new UniqueMetaData(s, tableName);
						m.put(s, umd);
					}
					umd.addColumn(rs.getString("COLUMN_NAME"));
				}
			}
			DBExec.CloseQuery(rs);
		}
		ConverterToMetaDataColumn converter = new ConverterToMetaDataColumn();
		for (UniqueMetaData umd : m.values()) {
			MetaDataTable t = model.getTable(umd.getTable());
			if (umd.getColumns().length != 1) {
				t.addUniqueConstraint(new MetaDataUniqueConstraint(umd.getName(), t,
						converter.convertToMetaDataColumn(t, umd.getColumns())));
			} else {
				MetaDataColumn[] cs = converter.convertToMetaDataColumn(t, umd.getColumns());
				cs[0].setUnique(true);
			}
		}
	}

}