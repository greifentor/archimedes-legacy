package archimedes.legacy.importer.jdbc;

import java.sql.Connection;

import archimedes.legacy.model.DiagrammModel;
import corent.db.ConnectionManager;
import corent.db.JDBCDataSourceRecord;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;

/**
 * A class which manages the import of a model from a JDBC source.
 *
 * @author ollie (24.09.2019)
 */
public class JDBCImportManager {

	/**
	 * Starts the import process.
	 * 
	 * @param connectionData A container with the necessary data for the database connection.
	 * @return A diagram from a selected JDBC connection.
	 */
	public DiagrammModel importDiagram(JDBCImportConnectionData connectionData) {
		try {
			// TODO: Collect all necessary data.
			DBObjectFactory factory = new DefaultDBObjectFactory();
			DBTypeConverter typeConverter = new DBTypeConverter();
			Class.forName(connectionData.getConnection().getDriver());
			JDBCDataSourceRecord dsr = new JDBCDataSourceRecord(connectionData.getConnection().getDriver(),
					connectionData.getConnection().getUrl(), connectionData.getConnection().getUserName(),
					connectionData.getPassword());
//			JDBCDataSourceRecord dsr = new JDBCDataSourceRecord("org.hsqldb.jdbc.JDBCDriver",
//					"jdbc:hsqldb:file:~/eclipse-workspace/restacf/src/test/resources/db/testdb", "sa", "");
			Connection connection = ConnectionManager.GetConnection(dsr);
			String schemeName = ("".equals(connectionData.getSchema()) ? null : connectionData.getSchema());
			// TODO: Import model from JDBC.
			DatabaseSO database = new JDBCModelReader(factory, typeConverter, connection, schemeName,
					connectionData.isIgnoreIndices(), connectionData.getIgnoreTablePatterns()).readModel();
			// TODO: Convert to Diagram
			return new DatabaseSOToDiagramConverter(connectionData.getAdjustment()).convert(database);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}