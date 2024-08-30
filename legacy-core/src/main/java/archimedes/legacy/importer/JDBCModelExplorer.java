package archimedes.legacy.importer;

import java.sql.Connection;
import java.util.Arrays;
import java.util.Vector;

import archimedes.connections.DatabaseConnection;
import archimedes.gui.DatabaseConnectionRecord;
import archimedes.legacy.gui.ExceptionDialog;
import archimedes.legacy.gui.connections.ConnectFrame;
import archimedes.legacy.importer.jdbc.DBObjectFactory;
import archimedes.legacy.importer.jdbc.DBTypeConverter;
import archimedes.legacy.importer.jdbc.DefaultDBObjectFactory;
import archimedes.legacy.importer.jdbc.JDBCImportConnectionData;
import archimedes.legacy.importer.jdbc.JDBCImportData;
import archimedes.legacy.importer.jdbc.JDBCImportManagerConfigurationDialog;
import archimedes.legacy.importer.jdbc.JDBCModelReader;
import baccara.gui.GUIBundle;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;
import baccara.gui.generics.EditorFrameListener;
import corent.db.ConnectionManager;
import corent.db.JDBCDataSourceRecord;
import corent.gui.DefaultFrameTextViewerComponentFactory;
import corent.gui.FrameTextViewer;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import logging.Logger;

/**
 * A class which able to explore a JDBC data source.
 *
 * @author ollie (26.02.2021)
 */
public class JDBCModelExplorer {

	private static final Logger LOG = Logger.getLogger(JDBCModelExplorer.class);

	public void createStructureReport(DatabaseConnection[] connections, GUIBundle guiBundle) {
		JDBCImportConnectionData connectionData = new JDBCImportConnectionData().setConnections(connections);
		JDBCImportManagerConfigurationDialog connectionDialog =
				new JDBCImportManagerConfigurationDialog(connectionData, guiBundle);
		connectionDialog.setVisible(true);
		connectionDialog
				.addEditorFrameListener(
						new EditorFrameListener<EditorFrameEvent<DatabaseConnectionRecord, ConnectFrame>>() {
							@Override
							public void eventFired(
									final EditorFrameEvent<DatabaseConnectionRecord, ConnectFrame> event) {
								if (event.getEventType() == EditorFrameEventType.OK) {
									final Thread t = new Thread(
											() -> {
												// ModelReaderProgressMonitor mrpm = new
												// ModelReaderProgressMonitor(guiBundle, 5);
												try {
													JDBCImportData importData = getImportData(connectionData);
													DBObjectFactory factory = new DefaultDBObjectFactory();
													DBTypeConverter typeConverter = new DBTypeConverter();
													String schemeName = ("".equals(importData.getSchema())
															? null
															: importData.getSchema());
													DatabaseSO database = new JDBCModelReader(
															factory,
															typeConverter,
															importData.getConnection(),
															schemeName,
															importData.isIgnoreIndices(),
															importData.getIgnoreTablePatterns(),
															importData.getImportOnlyTablePatterns(),
															importData.getDbExecMode())
																	// .addModelReaderListener(mrpm::update)
																	.readModel();
													new FrameTextViewer(
															new Vector<String>(
																	Arrays
																			.asList(
																					new JDBCModelExplorerReportGenerator(
																							importData.getSchema(),
																							database,
																							importData.getOptions())
																									.createReport())),
															DefaultFrameTextViewerComponentFactory.INSTANCE,
															guiBundle.getInifile(),
															guiBundle
																	.getResourceText(
																			"JDBCModelExplorer.resultViewer.title"),
															"");
												} catch (Exception e) {
													// mrpm.setVisible(false);
													LOG
															.error(
																	"error detected while importing from JDBC connection: "
																			+ e.getMessage());
													new ExceptionDialog(
															e,
															guiBundle
																	.getResourceText(
																			"Exception.ImportModel.text",
																			e.getMessage()),
															guiBundle);
												}
											});
									t.start();
								}
							}
						});
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
				.setDbExecMode(connectionData.getConnection().getDBMode())
				.setIgnoreIndices(connectionData.isIgnoreIndices())
				.setIgnoreTablePatterns(connectionData.getIgnoreTablePatterns())
				.setImportOnlyTablePatterns(connectionData.getImportOnlyTablePatterns())
				.setOptions(connectionData.getOptions())
				.setPassword(connectionData.getPassword())
				.setSchema(connectionData.getSchema());
	}

}