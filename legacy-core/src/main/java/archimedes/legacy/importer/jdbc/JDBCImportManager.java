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
	 * @param listener       A listener to the model reader.
	 * @return A diagram from a selected JDBC connection.
	 */
	public DiagrammModel importDiagram(JDBCImportConnectionData connectionData, ModelReaderListener listener)
			throws Exception {
		DBObjectFactory factory = new DefaultDBObjectFactory();
		DBTypeConverter typeConverter = new DBTypeConverter();
		Class.forName(connectionData.getConnection().getDriver());
		JDBCDataSourceRecord dsr = new JDBCDataSourceRecord(
				connectionData.getConnection().getDriver(),
				connectionData.getConnection().getUrl(),
				connectionData.getConnection().getUserName(),
				connectionData.getPassword());
		Connection connection = ConnectionManager.GetConnection(dsr);
		String schemeName = ("".equals(connectionData.getSchema()) ? null : connectionData.getSchema());
		DatabaseSO database = new JDBCModelReader(
				factory,
				typeConverter,
				connection,
				schemeName,
				connectionData.isIgnoreIndices(),
				connectionData.getIgnoreTablePatterns(),
				connectionData.getImportOnlyTablePatterns()).addModelReaderListener(listener).readModel();
		DiagrammModel model = new DatabaseSOToDiagramConverter(connectionData.getAdjustment()).convert(database);
		connection.close();
		return model;
	}

}