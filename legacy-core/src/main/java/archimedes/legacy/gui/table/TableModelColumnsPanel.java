/*
 * TableModelColumnsPanel.java
 *
 * 18.10.2017
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui.table;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import archimedes.model.ColumnModel;
import archimedes.model.TableModel;
import baccara.gui.GUIBundle;
import corent.gui.JTableWithInifile;
import corentx.util.SortedVector;

/**
 * A panel which allows to maintain the columns of a table.
 * 
 * @author ollie
 * 
 * @changed OLI 18.10.2017 - Added.
 */

public class TableModelColumnsPanel extends JPanel implements ActionListener, DataInputPanel<TableModel> {

	private static final String RES_ID_PREF = "TableModelColumnsPanel.";
	private static final String RES_ID_PREF_BUTTONS = RES_ID_PREF + "Buttons.";

	private JButton buttonAddColumn = null;
	private JButton buttonDeleteColumn = null;
	private java.util.List<ColumnModel> columns = null;
	private GUIBundle guiBundle = null;
	private JTableWithInifile tableColumns = null;
	private javax.swing.table.TableModel tableModelColumns = null;
	private TableModel tm = null;

	/**
	 * Creates a new column panel for table model.
	 * 
	 * @param guiBundle
	 *            A bundle of GUI resources.
	 * @param tm
	 *            The table model whose columns are to maintain.
	 * 
	 * @changed OLI 18.10.2017 - Added.
	 */
	public TableModelColumnsPanel(GUIBundle guiBundle, TableModel tm) {
		super(guiBundle.createBorderLayout());
		this.guiBundle = guiBundle;
		this.tm = tm;
		this.createColumnsList();
		this.add(this.createButtonPanel(), BorderLayout.NORTH);
		this.add(this.createColumnTablePanel(), BorderLayout.CENTER);
	}

	private void createColumnsList() {
		this.columns = new SortedVector<ColumnModel>();
		for (ColumnModel c : this.tm.getColumns()) {
			this.columns.add(c.createCopy());
		}
	}

	private JPanel createButtonPanel() {
		JPanel p = new JPanel(this.guiBundle.createFlowLayout(FlowLayout.LEFT));
		this.buttonAddColumn = this.guiBundle.createButton(RES_ID_PREF_BUTTONS + "Add.text", "listAdd", this, null);
		this.buttonDeleteColumn = this.guiBundle.createButton(RES_ID_PREF_BUTTONS + "Delete.text", "listDelete", this,
				null);
		p.add(this.buttonAddColumn);
		p.add(this.buttonDeleteColumn);
		return p;
	}

	private JPanel createColumnTablePanel() {
		JPanel p = new JPanel(this.guiBundle.createBorderLayout());
		this.tableModelColumns = new TableModelColumns(this.columns, this.guiBundle);
		this.tableColumns = new JTableWithInifile(this.tableModelColumns, this.guiBundle.getInifile(),
				"TableModelColumnsPanel.TableColumns");
		this.tableColumns.restoreFromIni();
		p.add(new JScrollPane(this.tableColumns));
		return p;
	}

	/**
	 * @changed OLI 18.10.2017 - Added.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub

	}

	/**
	 * @changed OLI 18.10.2017 - Added.
	 */
	@Override
	public Component[] getDataInputComponents() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @changed OLI 19.10.2017 - Added.
	 */
	@Override
	public void onClosed() {
		this.tableColumns.setVisible(false);
	}

	/**
	 * @changed OLI 18.10.2017 - Added.
	 */
	@Override
	public void transferData(TableModel o) {
		// TODO Auto-generated method stub

	}

}