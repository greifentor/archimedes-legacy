/*
 * ComplexIndicesAdministrationFrame.java
 *
 * 14.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.indices;

import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import org.apache.log4j.Logger;

import archimedes.legacy.scheme.DefaultIndexMetaData;
import archimedes.model.ChangeObserver;
import archimedes.model.ComplexIndexListProvider;
import archimedes.model.IndexMetaData;
import corent.files.Inifile;
import corent.gui.COButton;
import corent.gui.COLabel;
import corent.gui.JFrameWithInifile;
import corent.gui.PropertyRessourceManager;
import corent.gui.RessourceManager;

/**
 * Dieses Fenster erm&ouml;glicht die Wartung und Bearbeitung von komplexen
 * Indices in der GUI des Archimedesprogrammes.
 * 
 * @author ollie
 * 
 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndicesAdministrationFrame extends JFrameWithInifile implements ActionListener, ItemListener,
		ListSelectionListener, WindowListener {

	private static final int ETCH = EtchedBorder.RAISED;
	private static final int HGAP = 3;
	private static final int VGAP = 3;
	private static final Logger LOG = Logger.getLogger(ComplexIndicesAdministrationFrame.class);
	private static final String CONTEXTNAME = ".archimedes.gui.indices.ComplexIndicesAdministrationFrame";

	private ComplexIndexListProvider cilp = null;
	private IndexMetaData currentShownObject = null;
	private ChangeObserver model = null;
	private COButton closeButton = new COButton("Close", CONTEXTNAME + ".buttonClose");
	private COButton newButton = new COButton("New", CONTEXTNAME + ".buttonNew");
	private COButton removeButton = new COButton("Remove", CONTEXTNAME + ".buttonRemove");
	private COButton storeButton = new COButton("Store", CONTEXTNAME + ".buttonStore");
	private COLabel labelIndexName = new COLabel("Name des Index:", CONTEXTNAME + ".labelIndexName");
	private COLabel labelTables = new COLabel("Tabelle:", CONTEXTNAME + ".labelTable");
	private JComboBox<ClassMetaData> tableComboBox = null;
	private JList<IndexMetaData> indicesViewList = null;
	private JPanel mainPanel = null;
	private JSplitPane splitPanel = null;
	private JTextField indexNameTextField = null;
	private JTable columnTable = null;
	private List<ClassMetaData> tables = null;
	private RessourceManager rm = new PropertyRessourceManager();

	/**
	 * Erzeugt einen neuen Frame zur Anzeige und Bearbeitung von komplexen
	 * Indices.
	 * 
	 * @param title
	 *            Der Titel des Fensters, der in der Kopfzeile angezeigt werden
	 *            soll.
	 * @param ini
	 *            Eine Inidatei, aus der die Gestalt des Frames gegebenenfalls
	 *            rekonstruiert werden kann.
	 * @param indices
	 *            Die Liste der komplexen Indices, die durch den Frame
	 *            bearbeitet werden soll.
	 * @param tables
	 *            Eine Liste mit den Tabellen, die in der Auswahlbox angezeigt
	 *            werden sollen.
	 * @param model
	 *            Das Datenmodell, zu dem die Indices definiert werden als
	 *            <CODE>ChangeObserver</CODE> zur Bekanntgabe von
	 *            &Auml;nderungen an den Indices (damit das Datenmodell
	 *            ebenfalls als ge&auml;ndert markiert wird).
	 * 
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	public ComplexIndicesAdministrationFrame(String title, Inifile ini, ComplexIndexListProvider cilp,
			List<ClassMetaData> tables, ChangeObserver model) {
		super(title, ini);
		this.cilp = cilp;
		this.model = model;
		this.tables = tables;
		this.sortTables();
		this.addWindowListener(this);
		this.splitPanel = this.createSplitPanel();
		if (ini != null) {
			this.splitPanel.setDividerLocation(ini.readInt("SplitPane", "DividerLocation", 100));
		}
		this.mainPanel = this.createMainPanel(this.splitPanel);
		this.setContentPane(this.mainPanel);
		this.updateComponents();
		this.setVisible(true);
		this.checkEnabled();
	}

	private void sortTables() {
		ClassMetaData[] tableArr = this.tables.toArray(new ClassMetaData[] {});
		Arrays.sort(tableArr, new TableComparator());
		this.tables = Arrays.asList(tableArr);
	}

	private JPanel createMainPanel(JSplitPane splitPanel) {
		JPanel panel = new JPanel(new BorderLayout(HGAP, VGAP));
		panel.setBorder(new CompoundBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP), new EtchedBorder(ETCH)));
		panel.add(splitPanel, BorderLayout.CENTER);
		panel.add(this.createButtonPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private JSplitPane createSplitPanel() {
		JSplitPane panel = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, this.createSelectionPanel(), this
				.createIndexDetailsPanel());
		panel.setBorder(new CompoundBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP), new EtchedBorder(ETCH)));
		return panel;
	}

	private JPanel createIndexDetailsPanel() {
		JPanel panel = new JPanel(new BorderLayout(HGAP, VGAP));
		panel.add(this.createEditPanel(), BorderLayout.NORTH);
		panel.add(this.createButtonPanel(), BorderLayout.CENTER);
		return panel;
	}

	private JPanel createSelectionPanel() {
		JPanel panel = new JPanel(new GridLayout(1, 1, HGAP, VGAP));
		panel.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP));
		this.indicesViewList = new JList<IndexMetaData>(new IndexMetaDataListModel(this.cilp.getComplexIndices()));
		this.indicesViewList.addListSelectionListener(this);
		this.indicesViewList.setCellRenderer(new IndexMetaDataCellRenderer());
		this.indicesViewList.setPreferredSize(new Dimension(150, 200));
		panel.add(new JScrollPane(indicesViewList));
		return panel;
	}

	private JPanel createEditPanel() {
		JPanel panel = new JPanel(new BorderLayout(HGAP, VGAP));
		panel.add(this.createIndexNameAndTablePanel(), BorderLayout.NORTH);
		panel.add(this.createColumnTablePanel(), BorderLayout.CENTER);
		return panel;
	}

	private JPanel createIndexNameAndTablePanel() {
		JPanel panel = new JPanel(new BorderLayout(HGAP, VGAP));
		panel.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP));
		JPanel panelFields = new JPanel(new GridLayout(2, 1, HGAP, VGAP));
		JPanel panelLabels = new JPanel(new GridLayout(2, 1, HGAP, VGAP));
		panelLabels.setBorder(new EmptyBorder(0, 0, 0, HGAP));
		panelFields.add(this.createInputFieldIndexName());
		panelFields.add(this.createComboBoxTables());
		panelLabels.add(this.labelIndexName);
		panelLabels.add(this.labelTables);
		panel.add(panelFields, BorderLayout.CENTER);
		panel.add(panelLabels, BorderLayout.WEST);
		return panel;
	}

	private JTextField createInputFieldIndexName() {
		this.indexNameTextField = new JTextField(40);
		return this.indexNameTextField;
	}

	private JComboBox<ClassMetaData> createComboBoxTables() {
		this.tableComboBox = new JComboBox<ClassMetaData>();
		this.tableComboBox.addActionListener(this);
		this.tableComboBox.addItemListener(this);
		this.tableComboBox.setRenderer(new ClassMetaDataCellRenderer());
		for (ClassMetaData cmd : this.tables) {
			this.tableComboBox.addItem(cmd);
		}
		return this.tableComboBox;
	}

	private JPanel createColumnTablePanel() {
		JPanel panel = new JPanel(new BorderLayout(HGAP, VGAP));
		panel.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, VGAP));
		this.columnTable = new JTable(new DefaultTableModel());
		panel.add(new JScrollPane(this.columnTable), BorderLayout.CENTER);
		return panel;
	}

	private JPanel createButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, HGAP, VGAP));
		panel.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP));
		panel.setBorder(new EmptyBorder(VGAP, HGAP, VGAP, HGAP));
		panel.add(this.createRemoveButton());
		panel.add(new JLabel("    "), BorderLayout.SOUTH);
		panel.add(this.createStoreButton());
		panel.add(new JLabel("    "), BorderLayout.SOUTH);
		panel.add(this.createNewButton());
		panel.add(new JLabel("    "), BorderLayout.SOUTH);
		panel.add(this.createCloseButton());
		return panel;
	}

	private JButton createCloseButton() {
		this.prepareButton(this.closeButton);
		return this.closeButton;
	}

	private JButton createNewButton() {
		this.prepareButton(this.newButton);
		return this.newButton;
	}

	private JButton createRemoveButton() {
		this.prepareButton(this.removeButton);
		return this.removeButton;
	}

	private JButton createStoreButton() {
		this.prepareButton(this.storeButton);
		return this.storeButton;
	}

	private void prepareButton(COButton button) {
		button.addActionListener(this);
	}

	private void checkEnabled() {
		boolean enabled = (this.currentShownObject != null);
		if (this.columnTable != null) {
			this.columnTable.setEnabled(enabled);
		}
		this.indexNameTextField.setEnabled(enabled);
		this.tableComboBox.setEnabled(enabled);
	}

	/*
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	private void updateComponents() {
		this.closeButton.update(this.rm);
		this.labelIndexName.update(this.rm);
		this.labelTables.update(this.rm);
		this.newButton.update(this.rm);
		this.removeButton.update(this.rm);
		this.storeButton.update(this.rm);
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.closeButton) {
			this.setVisible(false);
			this.dispose();
		} else if (e.getSource() == this.newButton) {
			this.currentShownObject = new DefaultIndexMetaData("Neu", ((ClassMetaData) this.tableComboBox
					.getSelectedItem()));
			this.indexNameTextField.setText("Neu");
			this.tableComboBox.setSelectedIndex(0);
		} else if (e.getSource() == this.removeButton) {
			this.cilp.removeComplexIndex(this.currentShownObject);
			this.updateListView();
			this.model.raiseAltered();
		} else if (e.getSource() == this.storeButton) {
			this.currentShownObject.setName(this.indexNameTextField.getText());
			this.currentShownObject.setTable(((ClassMetaData) this.tableComboBox.getSelectedItem()));
			this.currentShownObject.clearColumns();
			for (int i = 0, leni = this.columnTable.getRowCount(); i < leni; i++) {
				if (((Boolean) this.columnTable.getValueAt(i, 1)).booleanValue()) {
					this.currentShownObject.addColumn((AttributeMetaData) this.columnTable.getValueAt(i, 0));
				}
			}
			this.cilp.removeComplexIndex(this.currentShownObject);
			this.cilp.addComplexIndex(this.currentShownObject);
			this.updateListView();
			this.model.raiseAltered();
		} else if (e.getSource() == this.tableComboBox) {
			if (this.columnTable != null) {
				this.columnTable.setModel(new ColumnSelectionTableModel((ClassMetaData) this.tableComboBox
						.getSelectedItem()));
			}
		}
		this.checkEnabled();
	}

	private void updateListView() {
		this.indicesViewList.setModel(new IndexMetaDataListModel(this.cilp.getComplexIndices()));
		this.indicesViewList.repaint();
	}

	/**
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void itemStateChanged(ItemEvent e) {
		if ((e.getSource() == this.tableComboBox) && (e.getStateChange() == ItemEvent.SELECTED)) {
			if (this.currentShownObject != null) {
				this.fillColumnNameTableAndSelectIndexFields();
			}
		}
		this.checkEnabled();
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void windowActivated(WindowEvent e) {
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void windowClosed(WindowEvent e) {
		try {
			this.getInifile().writeInt("SplitPane", "DividerLocation", this.splitPanel.getDividerLocation());
		} catch (Exception ex) {
			LOG.warn("divider location couldn't be stored.");
		}
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void windowClosing(WindowEvent e) {
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void windowDeactivated(WindowEvent e) {
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void windowDeiconified(WindowEvent e) {
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void windowIconified(WindowEvent e) {
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void windowOpened(WindowEvent e) {
	}

	/**
	 * @changed OLI 15.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void valueChanged(ListSelectionEvent e) {
		System.out.println(e);
		if ((e.getSource() == this.indicesViewList) && (!this.indicesViewList.isSelectionEmpty())) {
			this.currentShownObject = (IndexMetaData) this.indicesViewList.getSelectedValue();
			this.fillIndexNameTextField();
			this.fillTablesComboBoxAndSelectTable();
			this.fillColumnNameTableAndSelectIndexFields();
		}
		this.checkEnabled();
	}

	private void fillIndexNameTextField() {
		this.indexNameTextField.setText(this.currentShownObject.getName());
	}

	private void fillTablesComboBoxAndSelectTable() {
		if (this.tableComboBox.getModel().getSize() > 0) {
			for (ClassMetaData cmd : this.tables) {
				if (cmd.getName().equals(this.currentShownObject.getTable().getName())) {
					this.tableComboBox.setSelectedItem(cmd);
				}
			}
		}
	}

	private void fillColumnNameTableAndSelectIndexFields() {
		List<AttributeMetaData> columns = Arrays.asList(this.currentShownObject.getColumns());
		this.columnTable.setModel(new ColumnSelectionTableModel((ClassMetaData) this.tableComboBox.getSelectedItem()));
		for (int i = 0, leni = this.columnTable.getRowCount(); i < leni; i++) {
			if (this.isColumnSet(columns, (AttributeMetaData) this.columnTable.getValueAt(i, 0))) {
				this.columnTable.setValueAt(Boolean.TRUE, i, 1);
			}
		}
	}

	private boolean isColumnSet(List<AttributeMetaData> columns, AttributeMetaData columnSet) {
		for (AttributeMetaData column : columns) {
			if (column.getName().equals(columnSet.getName())) {
				return true;
			}
		}
		return false;
	}

}