/*
 * StereotypenSubEditor.java
 *
 * 30.04.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import archimedes.legacy.model.HistoryOwnerUtil;
import archimedes.legacy.model.StereotypeModel;
import archimedes.legacy.model.TabellenModel;
import corent.base.Attributed;
import corent.base.Constants;
import corent.djinn.DefaultListViewComponentFactory;
import corent.djinn.DialogSelectionDjinn;
import corent.djinn.SubEditor;
import corent.djinn.VectorPanelButtonFactory;

/**
 * Ein in einen EditorDjinn einbindbarer SubEditor zur Bearbeitung
 * Stereotypenzuordnung zu Tabellen der Archimedes-Applikation.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed OLI 09.01.2009 - Erweiterung um die Implementierung der Methode
 *          <TT>setObject(Attributed)</TT> im Zuge der Erweiterung der
 *          Spezifikation des Interfaces <TT>SubEditor</TT>.
 *          <P>
 * 
 */

public class StereotypenSubEditor implements SubEditor {

	/*
	 * Referenz auf die Component, in der der &uuml;bergeordnete EditorDjinn
	 * angezeigt wird.
	 */
	private Component owner = null;
	/* Der Einf&uuml;gen-Button des Panels. */
	private JButton buttonEinfuegen = null;
	/* Der Entfernen-Button des Panels. */
	private JButton buttonEntfernen = null;
	/*
	 * Die Liste der JComponents, die durch den Djinn zugegriffen werden
	 * k&ouml;nnen.
	 */
	private JComponent[] components = null;
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
	 * Generiert einen StereotypenSubEditor mit den &uuml;bergebenen Parametern.
	 * 
	 * @param owner
	 *            Referenz auf die Component, in der der EditorDjinn abgebildet
	 *            wird, zu dem der SubEditor geh&ouml;rt.
	 * @param tm
	 *            Das TabellenModel, dessen Stereotypen manipuliert werden
	 *            sollen.
	 * @param vpbf
	 *            Eine VectorPanelButtonFactory zum Erzeugen der Buttons des
	 *            Panels.
	 */
	public StereotypenSubEditor(Component owner, TabellenModel tm, VectorPanelButtonFactory vpbf) {
		super();
		this.owner = owner;
		this.tabelle = tm;
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
		this.anzeige = new JTable(new StereotypenTableModel(tm));
		JPanel panelButtons = new JPanel(new GridLayout(1, 3, Constants.HGAP, Constants.VGAP));
		panelButtons.add(new JLabel(""));
		panelButtons.add(this.buttonEinfuegen);
		panelButtons.add(this.buttonEntfernen);
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
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Einfuegen-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEinfuegen() {
		DialogSelectionDjinn dsd = new DialogSelectionDjinn((Frame) this.owner, "Auswahl Stereotype",
				new DefaultListViewComponentFactory(this.tabelle.getDiagramm().getStereotypen()));
		if (dsd.isSelected()) {
			Vector<?> values = dsd.getSelection();
			for (int i = 0, len = values.size(); i < len; i++) {
				boolean found = false;
				for (int j = 0, lenj = this.tabelle.getStereotypenCount(); j < lenj; j++) {
					StereotypeModel stm = this.tabelle.getStereotypeAt(j);
					if (values.elementAt(i) == stm) {
						found = true;
						break;
					}
				}
				if (!found) {
					this.tabelle.addStereotype((StereotypeModel) values.elementAt(i));
					HistoryOwnerUtil.addChangedTag(this.tabelle, "Added stereotype: "
							+ ((StereotypeModel) values.elementAt(i)).getName());
				}
			}
			this.doRepaint();
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Bearbeiten-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEntfernen() {
		if (this.anzeige.getSelectedRow() >= 0) {
			StereotypeModel stm = this.tabelle.getStereotypeAt(this.anzeige.getSelectedRow());
			this.tabelle.removeStereotype(stm);
			HistoryOwnerUtil.addChangedTag(this.tabelle, "Removed stereotype: " + stm.getName());
			this.doRepaint();
		}
	}

	private void doRepaint() {
		((AbstractTableModel) anzeige.getModel()).fireTableDataChanged();
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

class StereotypenTableModel extends AbstractTableModel {

	private String[] columnname = new String[] { "Stereotype" };
	private TabellenModel tm = null;

	public StereotypenTableModel(TabellenModel tm) {
		super();
		this.tm = tm;
	}

	public Class<?> getColumnClass(int column) {
		return new String().getClass();
	}

	public String getColumnName(int column) {
		if ((columnname != null) && (column >= 0) && (column < columnname.length) && (columnname[column] != null)) {
			return (String) columnname[column];
		}
		return "";
	}

	public int getRowCount() {
		return tm.getStereotypenCount();
	}

	public int getColumnCount() {
		if (columnname != null) {
			return columnname.length;
		}
		return 1;
	}

	public Object getValueAt(int row, int column) {
		StereotypeModel stm = tm.getStereotypeAt(row);
		return stm.toString();
	}

}
