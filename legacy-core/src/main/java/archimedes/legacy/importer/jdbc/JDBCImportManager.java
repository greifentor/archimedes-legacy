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
		JDBCImportData importData = getImportData(connectionData);
		DiagrammModel model = importDiagram(importData, listener);
		if (importData.getConnection() != null) {
			importData.getConnection().close();
		}
		return model;
	}

	private JDBCImportData getImportData(JDBCImportConnectionData connectionData) throws Exception {
		Class.forName(connectionData.getConnection().getDriver());
		JDBCDataSourceRecord dsr = new JDBCDataSourceRecord(
				connectionData.getConnection().getDriver(),
				connectionData.getConnection().getUrl(),
				connectionData.getConnection().getUserName(),
				connectionData.getPassword());
		Connection connection = ConnectionManager.GetConnection(dsr);
		return new JDBCImportData()
				.setAdjustment(connectionData.getAdjustment())
				.setConnection(connection)
				.setIgnoreIndices(connectionData.isIgnoreIndices())
				.setIgnoreTablePatterns(connectionData.getIgnoreTablePatterns())
				.setImportOnlyTablePatterns(connectionData.getImportOnlyTablePatterns())
				.setPassword(connectionData.getPassword())
				.setSchema(connectionData.getSchema());
	}

	/**
	 * Starts the import process.
	 * 
	 * @param importData A container with the necessary data for the database connection.
	 * @param listener   A listener to the model reader.
	 * @return A diagram from a selected JDBC connection.
	 */
	public DiagrammModel importDiagram(JDBCImportData importData, ModelReaderListener listener) throws Exception {
		DBObjectFactory factory = new DefaultDBObjectFactory();
		DBTypeConverter typeConverter = new DBTypeConverter();
		String schemeName = ("".equals(importData.getSchema()) ? null : importData.getSchema());
		DatabaseSO database = new JDBCModelReader(
				factory,
				typeConverter,
				importData.getConnection(),
				schemeName,
				importData.isIgnoreIndices(),
				importData.getIgnoreTablePatterns(),
				importData.getImportOnlyTablePatterns()).addModelReaderListener(listener).readModel();
		return new DatabaseSOToDiagramConverter(importData.getAdjustment()).convert(database);
	}

}