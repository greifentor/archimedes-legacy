/*
 * ConnectionsMainDialog.java
 *
 * 20.01.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.connections;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import org.apache.log4j.Logger;

import archimedes.connections.DatabaseConnection;
import archimedes.legacy.model.DiagrammModel;
import baccara.gui.GUIBundle;
import baccara.gui.generics.EditorFrameEvent;
import baccara.gui.generics.EditorFrameEventType;
import baccara.gui.generics.EditorFrameListener;
import baccara.gui.generics.SelectionDialog;
import corent.files.Inifile;
import corent.gui.JFrameWithInifile;
import corentx.io.FileUtil;
import corentx.util.SortedVector;

/**
 * A dialog for database connections selection and actions.
 * 
 * @author ollie
 * 
 * @changed OLI 20.01.2015 - Added.
 */

public class ConnectionsMainFrame<ET extends EditorFrameEvent<?, ? extends Window>> extends JFrameWithInifile implements
		ActionListener, EditorFrameListener<ET>, MouseListener {

	private static final Logger LOG = Logger.getLogger(ConnectionsMainFrame.class);

	private JButton buttonAdd = null;
	private JButton buttonClose = null;
	private JButton buttonCopy = null;
	private JButton buttonEdit = null;
	private JButton buttonRemove = null;
	private ConnectionsTableModel tableConnectionsModel = null;
	private DiagrammModel diagram = null;
	private GUIBundle guiBundle = null;
	private JTable tableConnections = null;

	/**
	 * Creates a new main dialog for database connections.
	 * 
	 * @param owner
	 *            The frame which is used as owner of the dialog.
	 * @param guiBundle
	 *            A GUI bundle e. g. for resource access.
	 * @param ini
	 *            The ini file which contains the parameters for reconstruction.
	 * 
	 * @changed OLI 20.01.2015 - Added.
	 */
	public ConnectionsMainFrame(Frame owner, GUIBundle guiBundle, Inifile ini, DiagrammModel diagram) {
		super(guiBundle.getResourceText("archimedes.ConnectionsMainDialog.title"), ini);
		this.diagram = diagram;
		this.guiBundle = guiBundle;
		this.tableConnectionsModel = new ConnectionsTableModel(this.diagram, this.guiBundle);
		this.tableConnections = new JTable(this.tableConnectionsModel);
		this.tableConnections.addMouseListener(this);
		this.setContentPane(this.createMainPanel());
		this.pack();
		this.setVisible(true);
		LOG.debug("Connection main dialog opened.");
	}

	private JPanel createMainPanel() {
		JPanel p = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		p.setBorder(new EmptyBorder(this.guiBundle.getVGap(), this.guiBundle.getHGap(), this.guiBundle.getVGap(),
				this.guiBundle.getVGap()));
		p.add(this.createSelectionPanel(), BorderLayout.CENTER);
		p.add(this.createButtonPanel(), BorderLayout.EAST);
		return p;
	}

	private JPanel createSelectionPanel() {
		JPanel p = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		p.add(new JScrollPane(this.tableConnections));
		return p;
	}

	private JPanel createButtonPanel() {
		JPanel p = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		JPanel p0 = new JPanel(new GridLayout(4, 1, this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		this.buttonAdd = this.createButton("add");
		this.buttonClose = this.createButton("close");
		this.buttonCopy = this.createButton("copy");
		this.buttonEdit = this.createButton("edit");
		this.buttonRemove = this.createButton("remove");
		p0.add(this.buttonAdd);
		p0.add(this.buttonCopy);
		p0.add(this.buttonEdit);
		p0.add(this.buttonRemove);
		p.add(p0, BorderLayout.NORTH);
		p0 = new JPanel(new GridLayout(1, 1, this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		p0.add(this.buttonClose);
		p.add(p0, BorderLayout.SOUTH);
		return p;
	}

	private JButton createButton(String id) {
		JButton b = new JButton(this.guiBundle.getResourceText("archimedes.ConnectionsMainDialog.button." + id));
		ImageIcon icon = this.guiBundle.getImageProvider().getImageIcon(id);
		if (icon != null) {
			b.setIcon(icon);
		}
		b.addActionListener(this);
		return b;
	}

	/**
	 * @changed OLI 20.01.2015 - Added.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonAdd) {
			this.doAddConnection();
		} else if (e.getSource() == this.buttonClose) {
			this.setVisible(false);
			this.dispose();
			LOG.debug("Connection main dialog closed.");
		} else if (e.getSource() == this.buttonCopy) {
			this.doCopyConnection();
		} else if (e.getSource() == this.buttonEdit) {
			this.doEditConnection();
		} else if (e.getSource() == this.buttonRemove) {
			this.doRemoveConnection();
		}
	}

	private void doAddConnection() {
		LOG.info("add button pressed.");
		List<DatabaseConnection> ldc = this.readDatabaseConnectionTemplates();
		String prefix = "archimedes.ConnectionTemplateSelection";
		SelectionDialog sd = new SelectionDialog(this, this.guiBundle.getResourceText(prefix + ".title"),
				this.guiBundle, ldc.toArray(new DatabaseConnection[0]), this.guiBundle.getResourceText(prefix
						+ ".button.select.title"), this.guiBundle.getResourceText(prefix + ".button.cancel.title"),
				new ConnectionListCellRenderer());
		sd.setModal(true);
		sd.setVisible(true);
		List<?> selected = sd.getResult();
		if ((selected != null) && (selected.size() == 1)) {
			DatabaseConnection dc = (DatabaseConnection) selected.get(0);
			DatabaseConnection dcc = new DatabaseConnection(dc.getName(), dc.getDriver(), dc.getUrl(),
					dc.getUserName(), dc.getDBMode(), dc.isSetDomains(), dc.isSetNotNull(), dc.isSetReferences(), dc
							.getQuote());
			ConnectionEditorFrame cef = new ConnectionEditorFrame(dcc, this.guiBundle, ConnectionEditorFrameType.COPY);
			cef.addEditorFrameListener(this);
			cef.setVisible(true);
		}
	}

	private List<DatabaseConnection> readDatabaseConnectionTemplates() {
		SortedVector<DatabaseConnection> ldc = new SortedVector<DatabaseConnection>();
		String fileName = "./conf/connection_templates.js";
		if (new File(fileName).exists()) {
			try {
				ScriptEngine engine = new ScriptEngineManager().getEngineByName("JavaScript");
				String script = FileUtil.readTextFromFile(fileName);
				engine.put("ldc", ldc);
				engine.eval(script);
			} catch (FileNotFoundException e) {
				LOG.warn("connection templates file not found: " + fileName);
			} catch (Exception e) {
				LOG.warn("error while reading connection templates file: " + fileName + ", exception: "
						+ e.getMessage());
			}
		}
		return ldc;
	}

	private void doCopyConnection() {
		LOG.info("copy button pressed.");
		DatabaseConnection dc = this.getSelectedDatabaseConnection();
		if (dc != null) {
			DatabaseConnection dcc = new DatabaseConnection(dc.getName(), dc.getDriver(), dc.getUrl(),
					dc.getUserName(), dc.getDBMode(), dc.isSetDomains(), dc.isSetNotNull(), dc.isSetReferences(), dc
							.getQuote());
			ConnectionEditorFrame cef = new ConnectionEditorFrame(dcc, this.guiBundle, ConnectionEditorFrameType.COPY);
			cef.addEditorFrameListener(this);
			cef.setVisible(true);
		}
	}

	private DatabaseConnection getSelectedDatabaseConnection() {
		int i = this.tableConnections.getSelectedRow();
		if (i >= 0) {
			return this.diagram.getDatabaseConnections()[i];
		}
		return null;
	}

	private void doEditConnection() {
		LOG.info("edit button pressed.");
		DatabaseConnection dbc = this.getSelectedDatabaseConnection();
		if (dbc != null) {
			ConnectionEditorFrame cef = new ConnectionEditorFrame(dbc, this.guiBundle, ConnectionEditorFrameType.EDIT);
			cef.addEditorFrameListener(this);
			cef.setVisible(true);
		}
	}

	private void doRemoveConnection() {
		LOG.info("remove button pressed.");
		this.diagram.removeDatabaseConnection(this.getSelectedDatabaseConnection().getName());
		this.tableConnectionsModel.fireTableDataChanged();
		this.diagram.raiseAltered();
	}

	/**
	 * @changed OLI 22.01.2015 - Added.
	 */
	@Override
	public void eventFired(ET e) {
		if (e.getEventType() == EditorFrameEventType.OK) {
			DatabaseConnection dbc = (DatabaseConnection) e.getEditedObject();
			if (this.isDatabaseConnectionWithSameNameAlreadyInDiagram(dbc)) {
				JOptionPane
						.showMessageDialog(
								null,
								this.guiBundle
										.getResourceText("archimedes.ConnectionsMainDialog.error.connection.already.exists.text"),
								this.guiBundle
										.getResourceText("archimedes.ConnectionsMainDialog.error.connection.already.exists.title"),
								JOptionPane.ERROR_MESSAGE);
				return;
			}
			if ((this.getSelectedDatabaseConnection() != null)
					&& (e instanceof ConnectionEditorFrameEvent)
					&& (((ConnectionEditorFrameEvent) e).getConnectionEditorFrameType() == ConnectionEditorFrameType.EDIT)) {
				this.diagram.removeDatabaseConnection(this.getSelectedDatabaseConnection().getName());
			}
			this.diagram.addDatabaseConnection(dbc);
			this.tableConnectionsModel.fireTableDataChanged();
			this.diagram.raiseAltered();
		}
		e.getSource().dispose();
		this.tableConnections.repaint();
	}

	private boolean isDatabaseConnectionWithSameNameAlreadyInDiagram(DatabaseConnection dc) {
		for (DatabaseConnection dc0 : this.diagram.getDatabaseConnections()) {
			if (dc0.getName().equals(dc.getName()) && !(dc0 == dc)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @changed OLI 22.01.2015 - Added.
	 */
	@Override
	public void mouseClicked(MouseEvent e) {
	}

	/**
	 * @changed OLI 22.01.2015 - Added.
	 */
	@Override
	public void mouseEntered(MouseEvent e) {
	}

	/**
	 * @changed OLI 22.01.2015 - Added.
	 */
	@Override
	public void mouseExited(MouseEvent e) {
	}

	/**
	 * @changed OLI 22.01.2015 - Added.
	 */
	@Override
	public void mousePressed(MouseEvent e) {
		if ((e.getSource() == this.tableConnections) && (e.getClickCount() == 2)) {
			this.doEditConnection();
		}
	}

	/**
	 * @changed OLI 22.01.2015 - Added.
	 */
	@Override
	public void mouseReleased(MouseEvent e) {
	}

}

class ConnectionListCellRenderer implements ListCellRenderer<Object> {

	/**
	 * @changed OLI 31.01.2015 - Added.
	 */
	@Override
	public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
			boolean cellHasFocus) {
		JLabel l = new JLabel("-");
		if (value instanceof DatabaseConnection) {
			DatabaseConnection dc = (DatabaseConnection) value;
			l = new JLabel(dc.getName() + " (" + dc.getDBMode() + ")");
		}
		if (isSelected) {
			l.setBackground(Color.LIGHT_GRAY);
			l.setOpaque(true);
		}
		return l;
	}

}