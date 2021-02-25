package archimedes.legacy.updater;

import archimedes.gui.DatabaseConnectionRecord;
import archimedes.gui.DiagramComponentPanel;
import archimedes.gui.diagram.GUIObjectTypes;
import archimedes.legacy.Archimedes;
import archimedes.legacy.gui.Counter;
import archimedes.legacy.gui.ExceptionDialog;
import archimedes.legacy.gui.FrameArchimedes;
import archimedes.legacy.gui.ModelReaderProgressMonitor;
import archimedes.legacy.gui.connections.ConnectFrame;
import archimedes.legacy.importer.jdbc.JDBCImportConnectionData;
import archimedes.legacy.importer.jdbc.JDBCImportManager;
import archimedes.legacy.importer.jdbc.JDBCImportManagerConfigurationDialog;
import archimedes.legacy.importer.jdbc.ModelReaderEvent;
import archimedes.legacy.importer.jdbc.ModelReaderEventType;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import baccara.gui.GUIBundle;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;
import baccara.gui.generics.EditorFrameListener;
import logging.Logger;

/**
 * A model update from a JDBC source.
 *
 * @author ollie (24.02.2021)
 */
public class JDBCModelUpdater {

	private static final Logger LOG = Logger.getLogger(JDBCModelUpdater.class);

	public void updateModel(DiagrammModel diagramm, GUIBundle guiBundle, final FrameArchimedes frameArchimedes,
			DiagramComponentPanel<GUIObjectTypes> component) {
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
										ModelReaderProgressMonitor mrpm = new ModelReaderProgressMonitor(guiBundle, 6);
										UpdateReport report = null;
										do {
											try {
												Diagramm d = (Diagramm) new JDBCImportManager()
														.importDiagram(connectionData, mrpm::update);
												if (d != null) {
													report = new ModelUpdater(diagramm, d, Archimedes.Factory).update();
													Counter counter = new Counter(0);
													int max = report.getActions().size();
													report
															.getActions()
															.forEach(
																	action -> mrpm
																			.update(
																					new ModelReaderEvent(
																							counter.inc(),
																							max,
																							5,
																							ModelReaderEventType.MESSAGE,
																							getActionString(
																									action,
																									guiBundle))));
													mrpm.update(new ModelReaderEvent(max, max, 6, null, null));
													mrpm.enableCloseButton();
													if (report
															.hasAtLeastOneActionInStatus(
																	UpdateReportAction.Status.DONE)) {
														diagramm.raiseAltered();
													}
													component.doRepaint();
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
										} while ((report != null)
												&& report.hasAtLeastOneActionInStatus(UpdateReportAction.Status.DONE));
									});
									t.start();
								}
							}
						});

	}

	private String getActionString(UpdateReportAction action, GUIBundle guiBundle) {
		return String
				.format(
						"%3s - %s",
						getStatusString(action.getStatus()),
						guiBundle
								.getResourceText(
										action.getType() == null
												? action.getMessage()
												: "ModelUpdater.message." + action.getType() + ".text",
										(Object[]) action.getValues()));
	}

	private String getStatusString(UpdateReportAction.Status status) {
		switch (status) {
		case MANUAL:
			return "(*)";
		case DONE:
			return "   ";
		default:
			return "!!!";
		}
	}

}