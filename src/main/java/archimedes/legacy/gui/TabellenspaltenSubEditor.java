/*
 * TabellenspaltenSubEditor.java
 *
 * 20.03.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.HistoryOwnerUtil;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.scheme.DefaultIndexListCleaner;
import archimedes.legacy.scheme.DomainStringBuilder;
import archimedes.model.ColumnModel;
import archimedes.model.NReferenceModel;
import archimedes.model.OrderMemberModel;
import archimedes.model.SelectionMemberModel;
import archimedes.model.TableModel;
import archimedes.model.ToStringContainerModel;
import baccara.gui.GUIBundle;
import corent.base.Attributed;
import corent.base.Constants;
import corent.base.StrUtil;
import corent.djinn.FrameEditorDjinn;
import corent.djinn.SubEditor;
import corent.djinn.VectorPanelButtonFactory;

/**
 * Ein in einen EditorDjinn einbindbarer SubEditor zur Bearbeitung von
 * Tabellenspalten der Archimedes-Applikation.
 * 
 * @author ollie
 * 
 * @changed OLI 09.01.2009 - Erweiterung um die Implementierung der Methode
 *          <CODE>setObject(Attributed)</CODE> im Zuge der Erweiterung der
 *          Spezifikation des Interfaces <CODE>SubEditor</CODE>.
 * @changed OLI 20.12.2011 - Einf&uuml;gen der Bereinigung der komplexen Indices
 *          beim L&ouml;schen einer Tabellenspalte.
 */

public class TabellenspaltenSubEditor implements SubEditor {

	/* Der Bearbeiten-Button des Panels. */
	private JButton buttonBearbeiten = null;
	/* Der Einf&uuml;gen-Button des Panels. */
	private JButton buttonEinfuegen = null;
	/* Der Entfernen-Button des Panels. */
	private JButton buttonEntfernen = null;
	/*
	 * Die Liste der JComponents, die durch den Djinn zugegriffen werden
	 * k&ouml;nnen.
	 */
	private JComponent[] components = null;
	private GUIBundle guiBundle = null;
	/* Liste der zu einer Component geh&ouml;renden Labels des SubEditors. */
	private JLabel[] labels = null;
	/* Das Panel, auf dem der SubEditor abgebildet wird. */
	private JPanel panel = null;
	/*
	 * Liste der JPanels, auf denen die zugreifbaren Components untergebracht
	 * werden sollen.
	 */
	private JPanel[] panels = null;
	/* Die Tabellenansicht zur Auswahl der Tabellenspalten. */
	private JTable anzeige = null;

	/* Das TabellenModel, das in dem SubEditor manipuliert werden soll. */
	private TabellenModel tabelle = null;

	/**
	 * Generiert einen TabellenspaltenSubEditor mit den &uuml;bergebenen
	 * Parametern.
	 * 
	 * @param tm
	 *            Das TabellenModel, dessen Spalten manipuliert werden sollen.
	 * @param vpbf
	 *            Eine VectorPanelButtonFactory zum Erzeugen der Buttons des
	 *            Panels.
	 * @param guiBundle
	 *            A bundle with GUI information.
	 */
	public TabellenspaltenSubEditor(TabellenModel tm, VectorPanelButtonFactory vpbf, GUIBundle guiBundle) {
		super();
		this.guiBundle = guiBundle;
		this.tabelle = tm;
		this.buttonBearbeiten = vpbf.createButtonBearbeiten();
		this.buttonBearbeiten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doButtonBearbeiten();
			}
		});
		this.buttonEinfuegen = vpbf.createButtonEinfuegen();
		this.buttonEinfuegen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doButtonEinfuegen();
			}
		});
		this.buttonEntfernen = vpbf.createButtonEntfernen();
		this.buttonEntfernen.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doButtonEntfernen();
			}
		});
		this.anzeige = new JTable(new TabellenspaltenTableModel(tm));
		this.anzeige.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) {
					doButtonBearbeiten();
				}
			}
		});
		JPanel panelButtons = new JPanel(new GridLayout(1, 3, Constants.HGAP, Constants.VGAP));
		panelButtons.add(this.buttonEinfuegen);
		panelButtons.add(this.buttonEntfernen);
		panelButtons.add(this.buttonBearbeiten);
		JPanel panelAnzeige = new JPanel(new GridLayout(1, 1, Constants.HGAP, Constants.VGAP));
		panelAnzeige.setBorder(new EmptyBorder(1, 1, 1, 1));
		panelAnzeige.add(new JScrollPane(this.anzeige));
		this.panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
		this.panel.add(panelAnzeige, BorderLayout.CENTER);
		this.panel.add(panelButtons, BorderLayout.NORTH);
		this.components = new JComponent[] { this.anzeige };
		this.labels = new JLabel[] { new JLabel() };
		this.panels = new JPanel[] { panelAnzeige };
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Bearbeiten-Button
	 * bet&auml;tigt.
	 */
	public void doButtonBearbeiten() {
		if (this.anzeige.getSelectedRow() >= 0) {
			final TabellenspaltenModel ts = (TabellenspaltenModel) this.tabelle.getTabellenspalteAt(this.anzeige
					.getSelectedRow());
			new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern " + "Tabellenspalte ") + ts.getName(), ts, false, null) {
				@Override
				public void doClosed() {
				}

				@Override
				public void doChanged(boolean saveOnly) {
					tabelle.removeTabellenspalte(ts);
					ts.setTabelle(tabelle);
					tabelle.addTabellenspalte(ts);
					doRepaint();
				}
			};
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Einfuegen-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEinfuegen() {
		final TabellenspaltenModel ts = Archimedes.Factory.createTabellenspalte("neu", null, false);
		this.tabelle.addTabellenspalte(ts);
		new FrameEditorDjinn("Neuanlage Tabellenspalte", ts, false, null) {
			@Override
			public void doClosed() {
				doRepaint();
			}

			@Override
			public void doChanged(boolean saveOnly) {
				tabelle.removeTabellenspalte(ts);
				if (tabelle.getTabellenspalte(ts.getName()) == null) {
					ts.setTabelle(tabelle);
					tabelle.addTabellenspalte(ts);
					HistoryOwnerUtil.addChangedTag(ts, "Added");
					HistoryOwnerUtil.addChangedTag(tabelle, "Added column: " + ts.getName());
				} else {
					JOptionPane.showMessageDialog(null, StrUtil.FromHTML("Es existiert bereits "
							+ " eine Tabellenspalte mit dem Namen " + ts.getName() + "!"), "DoppelteTabellenspalte",
							JOptionPane.OK_OPTION);
				}
			}

			@Override
			public void doDiscarded() {
				tabelle.removeTabellenspalte(ts);
			}
		};
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Bearbeiten-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEntfernen() {
		if (this.anzeige.getSelectedRow() >= 0) {
			TabellenspaltenModel ts = (TabellenspaltenModel) this.tabelle.getTabellenspalteAt(this.anzeige
					.getSelectedRow());
			for (TableModel tm : this.tabelle.getDiagramm().getTables()) {
				for (ColumnModel c : tm.getColumns()) {
					if ((c.getRelation() != null) && (c.getRelation().getReferenced() == ts)) {
						JOptionPane.showMessageDialog(null, this.guiBundle.getResourceText(
								"archimedes.TableDialog.error.column.is.referenced.by.another." + "column.label")
								.replace("{0}", c.toString()), this.guiBundle
								.getResourceText("archimedes.TableDialog.error."
										+ "column.is.referenced.by.another.column.title"), JOptionPane.YES_OPTION);
						System.out.println("Is referenced by column: " + c.getFullName());
						return;
					}
				}
				for (NReferenceModel nrm : tm.getNReferences()) {
					if (nrm.getColumn() == ts) {
						JOptionPane.showMessageDialog(null, this.guiBundle.getResourceText(
								"archimedes.TableDialog.error.column.is.referenced.by." + "nReference.label").replace(
								"{0}", tm.getName()), this.guiBundle.getResourceText("archimedes.TableDialog.error."
								+ "column.is.referenced.by.nReference.title"), JOptionPane.YES_OPTION);
						System.out.println("Is referenced by n-reference of table: " + tm.getName());
						return;
					}
				}
			}
			if (ts.isPrimaryKey()
					&& (JOptionPane.showConfirmDialog(null, this.guiBundle
							.getResourceText("archimedes.TableDialog.warning.column.is." + "primary.key.label"),
							this.guiBundle.getResourceText("archimedes."
									+ "TableDialog.warning.column.is.primary.key.title"), JOptionPane.YES_NO_OPTION) != JOptionPane.YES_OPTION)) {
				return;
			}
			this.tabelle.getAuswahlMembers().removeElement(ts);
			for (SelectionMemberModel smm : this.tabelle.getSelectableColumns()) {
				if (smm.getColumn().equals(ts)) {
					this.tabelle.removeSelectableColumn(smm);
				}
			}
			this.tabelle.removeCompareToMember(ts);
			this.tabelle.removeEqualsMember(ts);
			this.tabelle.removeHashCodeMember(ts);
			for (OrderMemberModel om : this.tabelle.getSelectionViewOrderMembers()) {
				if (ts.equals(om.getOrderColumn())) {
					this.tabelle.removeSelectionViewOrderMember(om);
				}
			}
			for (ToStringContainerModel tsc : this.tabelle.getComboStringMembers()) {
				if (ts.equals(tsc.getColumn())) {
					this.tabelle.removeComboStringMember(tsc);
				}
			}
			for (ToStringContainerModel tsc : this.tabelle.getToStringMembers()) {
				if (ts.equals(tsc.getColumn())) {
					this.tabelle.removeToStringMember(tsc);
				}
			}
			this.tabelle.removeTabellenspalte(ts);
			HistoryOwnerUtil.addChangedTag(this.tabelle, "Removed column: " + ts.getName());
			new DefaultIndexListCleaner().clean(this.tabelle.getDiagramm());
			this.doRepaint();
		}
	}

	private void doRepaint() {
		((AbstractTableModel) anzeige.getModel()).fireTableDataChanged();
		// this.anzeige.setModel(new TabellenspaltenTableModel(this.tabelle));
	}

	/* Implementierung des Interfaces SubEditorDesriptor. */

	public void cleanupData() {
	}

	public JComponent getComponent(int n) {
		return this.components[n];
	}

	public int getComponentCount() {
		return this.components.length;
	}

	public JPanel getComponentPanel(int n) {
		return this.panels[n];
	}

	public JLabel getLabel(int n) {
		return this.labels[n];
	}

	public JPanel getPanel() {
		return this.panel;
	}

	public void setObject(Attributed attr) {
		this.tabelle = (TabellenModel) attr;
	}

	public void transferData() {
	}

}

class TabellenspaltenTableModel extends AbstractTableModel {

	private String[] columnname = new String[] { "Name", "Domain (SQL-Typ)", "PK", "FK", "Index", "Not Null", "Unique",
			"Transient", "Globale Id", "Default", "Constraints", "Editorpos.", "Parameter" };
	private TabellenModel tm = null;

	public TabellenspaltenTableModel(TabellenModel tm) {
		super();
		this.tm = tm;
	}

	@Override
	public Class getColumnClass(int column) {
		switch (column) {
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
			return new Boolean(false).getClass();
		}
		return new String().getClass();
	}

	@Override
	public String getColumnName(int column) {
		if ((columnname != null) && (column >= 0) && (column < columnname.length) && (columnname[column] != null)) {
			return (String) columnname[column];
		}
		return "";
	}

	public int getRowCount() {
		return tm.getTabellenspaltenCount();
	}

	public int getColumnCount() {
		if (columnname != null) {
			return columnname.length;
		}
		return 1;
	}

	public Object getValueAt(int row, int column) {
		TabellenspaltenModel tsm = (TabellenspaltenModel) tm.getTabellenspalteAt(row);
		switch (column) {
		case 0:
			String n = tsm.getName();
			if (n == null) {
				break;
			}
			return n;
		case 1:
			if (tsm.getDomain() != null) {
				return new DomainStringBuilder(tsm).build();
			}
			return "<null>";
		case 2:
			return new Boolean(tsm.isPrimarykey());
		case 3:
			return new Boolean((tsm.getRelation() != null));
		case 4:
			return new Boolean(tsm.isIndexed());
		case 5:
			return new Boolean(tsm.isNotNull());
		case 6:
			return new Boolean(tsm.isUnique());
		case 7:
			return new Boolean(tsm.isTransient());
		case 8:
			return new Boolean(tsm.isGlobalId());
		case 9:
			return tsm.getDefaultValue();
		case 10:
			String s = "";
			if (tsm.isKodiert()) {
				if (s.length() > 0) {
					s = s.concat(", ");
				}
				s = s.concat("CODED");
			}
			return s;
		case 11:
			String erg = "";
			if (tsm.isEditormember()) {
				String panel = "";
				if (tsm.getPanel().getPanelNumber() > 0) {
					panel = "-P" + tsm.getPanel().getPanelNumber();
				}
				erg = erg.concat((tsm.isWriteablemember() ? "*" : "") + tsm.getEditorPosition()).concat(panel).concat(
						", \"").concat(tsm.getLabelText()).concat("\"");
				if (tsm.getMnemonic().length() > 0) {
					erg = erg.concat("(").concat(tsm.getMnemonic()).concat(")");
				}
				if (tsm.isAlterInBatch()) {
					erg = erg.concat("-B");
				}
				if (tsm.isGlobal()) {
					erg = erg.concat("-G");
				}
				if (tsm.isGlobal()) {
					erg = erg.concat("-GID");
				}
				if (tsm.isDisabled()) {
					erg = erg.concat(" ABGEBLENDET");
				}
				if (tsm.isIndexSearchMember()) {
					erg = erg.concat(" (INDS)");
				}
			}
			return erg;
		case 12:
			return tsm.getParameters();
		}
		return "<null>";
	}

}
