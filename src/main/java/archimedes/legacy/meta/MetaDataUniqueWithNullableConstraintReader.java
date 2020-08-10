/*
 * MetaDataUniqueWithNullableConstraintReader.java
 *
 * 17.02.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.legacy.meta;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import corent.base.SortedVector;
import corent.db.ConnectionManager;
import corent.db.DBExec;
import corent.db.DBExecMode;
import corent.db.JDBCDataSourceRecord;

/**
 * A reader for special unique constraints with a nullable field.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.02.2016 - Added.
 */

public class MetaDataUniqueWithNullableConstraintReader {

	/**
	 * Adds the unique constraints with a nullable field meta data of the data model
	 * of the passed JDBC connection.
	 *
	 * @param model              The meta model which the read unique constraints
	 *                           should be added to.
	 * @param connection         The connection whose unique constraint information
	 *                           from meta data should be read.
	 * @param excludedTableNames Names of tables whose unique constraints should be
	 *                           excluded from meta data read.
	 * @param dbMode             The mode of the DBMS whose data are read.
	 * @throws SQLException In case of an error while accessing the database.
	 *
	 * @changed OLI 17.02.2016 - Added.
	 */
	public void addUniqueWithNullableConstraints(MetaDataModel model, JDBCDataSourceRecord connection,
			String[] excludedTableNames, DBExecMode dbMode) throws SQLException {
		Connection c = ConnectionManager.GetConnection(connection);
		this.getUniqueWithNullableMetaData(model, c, Arrays.asList(excludedTableNames));
		c.close();
	}

	private void getUniqueWithNullableMetaData(MetaDataModel model, Connection c, List<String> excludeTableNames)
			throws SQLException {
		ResultSet rs = DBExec.Query(c, "SELECT n.nspname scheme_name, t.relname table_name, "
				+ "ix.relname index_name, a.attname attr_name, indisunique, indisprimary, "
				+ "regexp_replace(pg_get_indexdef(indexrelid), '.*\\((.*)\\)', '\\1') columns\n" + "FROM pg_index i\n"
				+ "JOIN pg_class t ON t.oid = i.indrelid\n" + "LEFT JOIN pg_namespace n ON n.oid = t.relnamespace\n"
				+ "JOIN pg_class ix ON ix.oid = i.indexrelid\n"
				+ "JOIN pg_attribute a ON a.attrelid = t.oid AND a.attnum = ANY(i.indkey)\n"
				+ "WHERE t.relname LIKE '%' AND indisprimary = 'f' AND n.nspname = '" + model.getSchemeName() + "'\n"
				+ "ORDER BY table_name, index_name, attr_name");
		Map<String, UniqueIndexData> m = new HashMap<String, UniqueIndexData>();
		while (rs.next()) {
			String tn = rs.getString("table_name");
			if (excludeTableNames.contains(tn) || (model.getTable(tn) == null)) {
				continue;
			}
			String an = rs.getString("attr_name");
			String ixn = rs.getString("index_name");
			String cond = rs.getString("columns");
			if (!cond.endsWith("\" IS NOT NULL") && !cond.endsWith("\" IS NULL")) {
				continue;
			}
			UniqueIndexData uid = m.get(ixn);
			if (uid == null) {
				uid = new UniqueIndexData();
				MetaDataTable table = model.getTable(tn);
				uid.table = table;
				cond = cond.replace("IS NOT NULL", "").replace("IS NULL", "").replace("\"", "").trim();
				uid.nullable = table.getColumn(cond);
				m.put(ixn, uid);
			}
			uid.columns.add(uid.table.getColumn(an));
		}
		for (String ixn : m.keySet()) {
			UniqueIndexData uid = m.get(ixn);
			uid.table.removeUniqueConstraint(ixn);
			uid.table.addUniqueConstraint(new MetaDataUniqueWithNullableConstraint(ixn, uid.table, uid.nullable,
					uid.columns.toArray(new MetaDataColumn[0])));
		}
	}

}

class UniqueIndexData {

	List<MetaDataColumn> columns = new SortedVector<MetaDataColumn>();
	MetaDataColumn nullable = null;
	MetaDataTable table = null;

}