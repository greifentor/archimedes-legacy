package archimedes.legacy.exporter;

import java.util.List;
import java.util.Vector;

import archimedes.gui.DatabaseConnectionRecord;
import archimedes.legacy.gui.ExceptionDialog;
import archimedes.legacy.gui.ModelReaderProgressMonitor;
import archimedes.legacy.gui.connections.ConnectFrame;
import archimedes.legacy.importer.jdbc.JDBCImportConnectionData;
import archimedes.legacy.importer.jdbc.JDBCImportManager;
import archimedes.legacy.importer.jdbc.JDBCImportManagerConfigurationDialog;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.updater.DataModelToCMOConverter;
import baccara.gui.GUIBundle;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;
import baccara.gui.generics.EditorFrameListener;
import corent.gui.DefaultFrameTextViewerComponentFactory;
import corent.gui.FrameTextViewer;
import de.ollie.dbcomp.comparator.DataModelComparator;
import de.ollie.dbcomp.comparator.model.ChangeActionCRO;
import logging.Logger;

/**
 * A class which is able to create a liquibase script to update a database based on a diagram.
 *
 * @author ollie (24.02.2021)
 */
public abstract class ScriptCreator {

	private static final Logger LOG = Logger.getLogger(ScriptCreator.class);

	public void createScript(DiagrammModel diagramm, GUIBundle guiBundle) {
		JDBCImportConnectionData connectionData =
				new JDBCImportConnectionData().setConnections(diagramm.getDatabaseConnections());
		JDBCImportManagerConfigurationDialog connectionDialog =
                new JDBCImportManagerConfigurationDialog(
                        connectionData,
                        guiBundle,
                        guiBundle.getResourceText("LiquibaseImportConfigurationDialog.title"));
		connectionDialog.setVisible(true);
		connectionDialog
				.addEditorFrameListener(
						new EditorFrameListener<EditorFrameEvent<DatabaseConnectionRecord, ConnectFrame>>() {
							@Override
							public void eventFired(
									final EditorFrameEvent<DatabaseConnectionRecord, ConnectFrame> event) {
								if (event.getEventType() == EditorFrameEventType.OK) {
									final Thread t = new Thread(() -> {
										ModelReaderProgressMonitor mrpm = new ModelReaderProgressMonitor(guiBundle, 5);
										try {
											Diagramm d =
													(Diagramm) new JDBCImportManager()
															.importDiagram(connectionData, mrpm::update);
											mrpm.enableCloseButton();
											if (d != null) {
												DataModelToCMOConverter converter = new DataModelToCMOConverter();
												LOG
														.debug(
																"model: " + converter
																		.convert(
																				diagramm,
																				table -> table
																						.getOptionByName(
																								"NO_DB") != null));
												LOG.debug("db:    " + converter.convert(d));
												DataModelComparator comparator = new DataModelComparator();
												List<ChangeActionCRO> changeActions =
														comparator
																.compare(
																		converter
																				.convert(
																						diagramm,
																						table -> table
																								.getOptionByName(
																										"NO_DB") != null),
																		converter.convert(d))
																.getChangeActions();
												new FrameTextViewer(
														new Vector<String>(
																createScript(
																		changeActions,
																		connectionData.getOptions())),
														DefaultFrameTextViewerComponentFactory.INSTANCE,
														guiBundle.getInifile(),
														getViewerTitel(),
														"");
											}
										} catch (Exception e) {
											mrpm.setVisible(false);
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

	protected abstract List<String> createScript(List<ChangeActionCRO> changeActions, String connectionDataOptions);

	protected abstract String getViewerTitel();

}