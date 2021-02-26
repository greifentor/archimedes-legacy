package archimedes.legacy.importer;

import java.sql.Connection;

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
import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;
import de.ollie.dbcomp.util.TypeConverter;
import logging.Logger;

/**
 * A class which able to explore a JDBC data source.
 *
 * @author ollie (26.02.2021)
 */
public class JDBCModelExplorer {

	private static final Logger LOG = Logger.getLogger(JDBCModelExplorer.class);
	private static final TypeConverter TYPE_CONVERTER = new TypeConverter();

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
															importData.getImportOnlyTablePatterns())
																	// .addModelReaderListener(mrpm::update)
																	.readModel();
													System.out
															.println(
																	"\n\n" + createReport(
																			importData.getSchema(),
																			database) + "\n\n");
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
				.setIgnoreIndices(connectionData.isIgnoreIndices())
				.setIgnoreTablePatterns(connectionData.getIgnoreTablePatterns())
				.setImportOnlyTablePatterns(connectionData.getImportOnlyTablePatterns())
				.setPassword(connectionData.getPassword())
				.setSchema(connectionData.getSchema());
	}

	private String createReport(String schemePattern, DatabaseSO database) {
		return (schemePattern != null
				? "Scheme Pattern: " + (schemePattern.isEmpty() ? "%" : schemePattern) + "\n\n"
				: "")
				+ database
						.getSchemes()
						.stream()
						.map(this::getString)
						.reduce((s0, s1) -> s0 + "\n" + s1)
						.orElse("No Schemes");
	}

	private String getString(SchemeSO scheme) {
		return scheme.getName() + "\n"
				+ scheme
						.getTables()
						.stream()
						.map(this::getString)
						.reduce((s0, s1) -> s0 + "\n" + s1)
						.orElse("No Tables")
				+ "\n";
	}

	private String getString(TableSO table) {
		return "- " + table.getName() + "\n"
				+ table
						.getColumns()
						.stream()
						.map(this::getString)
						.reduce((s0, s1) -> s0 + "\n" + s1)
						.orElse("No Tables");
	}

	private String getString(ColumnSO column) {
		return "  + " + column.getName() + " (" + getString(column.getType()) + ")"
				+ (column.isPkMember() ? " PRIMARY KEY" : "") + (!column.isNullable() ? " NOT NULL" : "")
				+ (column.isUnique() ? " UNIQUE" : "");
	}

	private String getString(TypeSO type) {
		return TYPE_CONVERTER.convert(type.getSqlType()) + getLengthAndPrecisionString(type);
	}

	private String getLengthAndPrecisionString(TypeSO type) {
		return (type.getLength() != null) && (type.getLength() > 0)
				? " (" + type.getLength() + getPrecisionString(type) + ")"
				: "";
	}

	private String getPrecisionString(TypeSO type) {
		return (type.getPrecision() != null) && (type.getPrecision() > 0) ? ", " + type.getPrecision() : "";
	}

}