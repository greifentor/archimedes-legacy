package archimedes.legacy;

import java.sql.Connection;
import java.sql.ResultSet;

import corent.db.ConnectionManager;
import corent.db.JDBCDataSourceRecord;

public class CheckForeignKeysMySQL {

	public static void main(String[] args) throws Exception {
		Connection c = ConnectionManager
				.GetConnection(new JDBCDataSourceRecord("org.gjt.mm.mysql.Driver", "rsp", "rsp", "rsp"));
		ResultSet rs = c.getMetaData().getImportedKeys(null, null, "Abenteurer");
		while (rs.next()) {
			System.out.println("Imported: " + rs.getString("FKTABLE_NAME") + "." + rs.getString("FKCOLUMN_NAME"));
		}
		rs.close();
		rs = c.getMetaData().getExportedKeys(null, null, "Abenteurer");
		while (rs.next()) {
			System.out.println("Exported: " + rs.getString("FKTABLE_NAME") + "." + rs.getString("FKCOLUMN_NAME"));
		}
		rs.close();
		c.close();
	}

}