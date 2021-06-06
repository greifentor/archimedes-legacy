package archimedes.legacy.importer.jdbc;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * A specialization of the DefaultDatabaseMetaDataAdapter class for mySQL.
 *
 * @author ollie (14.05.2021)
 */
public class MySQLDatabaseMetaDataAdapter extends DefaultDatabaseMetaDataAdapter {

	public MySQLDatabaseMetaDataAdapter(JDBCImportDatabaseMetaDataPort jdbcImportDatabaseMetaDataPort) {
		super(jdbcImportDatabaseMetaDataPort);
	}

	@Override
	public List<String> getSchemeNames(DatabaseMetaData dbmd, String schemePattern) throws SQLException {
		List<String> schemes = new ArrayList<>();
		ResultSet rs = dbmd.getCatalogs();
		while (rs.next()) {
			String catalogName = rs.getString("TABLE_CAT");
			if (catalogName.toLowerCase().equals(schemePattern)) {
				schemes.add(catalogName);
			}
		}
		rs.close();
		return schemes;
	}

}