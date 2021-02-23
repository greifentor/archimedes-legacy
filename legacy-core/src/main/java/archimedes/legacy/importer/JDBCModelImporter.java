package archimedes.legacy.importer;

import archimedes.gui.DatabaseConnectionRecord;
import archimedes.legacy.gui.ExceptionDialog;
import archimedes.legacy.gui.FrameArchimedes;
import archimedes.legacy.gui.ModelReaderProgressMonitor;
import archimedes.legacy.gui.connections.ConnectFrame;
import archimedes.legacy.importer.jdbc.JDBCImportConnectionData;
import archimedes.legacy.importer.jdbc.JDBCImportManager;
import archimedes.legacy.importer.jdbc.JDBCImportManagerConfigurationDialog;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import baccara.gui.GUIBundle;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;
import baccara.gui.generics.EditorFrameListener;
import logging.Logger;

/**
 * A model importer from a JDBC source.
 *
 * @author ollie (23.02.2021)
 */
public class JDBCModelImporter {

	private static final Logger LOG = Logger.getLogger(JDBCModelImporter.class);

	public void importModel(DiagrammModel diagramm, GUIBundle guiBundle, final FrameArchimedes frameArchimedes) {
		JDBCImportConnectionData connectionData =
				new JDBCImportConnectionData().setConnections(diagramm.getDatabaseConnections());
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
									final Thread t = new Thread(() -> {
										ModelReaderProgressMonitor mrpm = new ModelReaderProgressMonitor(guiBundle, 5);
										try {
											Diagramm d = (Diagramm) new JDBCImportManager()
													.importDiagram(connectionData, mrpm::update);
											if (d != null) {
												frameArchimedes.setDiagramm(d);
												mrpm.enableCloseButton();
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

}