/*
 * PanellistenSubEditor.java
 *
 * 10.07.2005
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
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.model.PanelModel;
import corent.base.Attributed;
import corent.base.Constants;
import corent.base.StrUtil;
import corent.djinn.FrameEditorDjinn;
import corent.djinn.SubEditor;
import corent.djinn.VectorPanelButtonFactory;

/**
 * Ein in einen EditorDjinn einbindbarer SubEditor zur Bearbeitung der
 * Panellisten der Archimedes-Applikation.
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

public class PanellistenSubEditor implements SubEditor {

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
	 * Generiert einen PanellistenSubEditor mit den &uuml;bergebenen Parametern.
	 * 
	 * @param tm
	 *            Das TabellenModel, dessen Spalten manipuliert werden sollen.
	 * @param vpbf
	 *            Eine VectorPanelButtonFactory zum Erzeugen der Buttons des
	 *            Panels.
	 */
	public PanellistenSubEditor(TabellenModel tm, VectorPanelButtonFactory vpbf) {
		super();
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
		this.anzeige = new JTable(new PanellistenTableModel(tm));
		this.anzeige.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				if ((e.getClickCount() == 2) && buttonBearbeiten.isVisible()) {
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
			final archimedes.legacy.scheme.Panel p = (archimedes.legacy.scheme.Panel) this.tabelle
					.getPanelAt(this.anzeige.getSelectedRow());
			new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern Panel"), p, false, null) {
				public void doClosed() {
					doRepaint();
				}

				public void doChanged(boolean saveOnly) {
					tabelle.removePanel(p);
					tabelle.addPanel(p);
				}
			};
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Einfuegen-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEinfuegen() {
		final archimedes.legacy.scheme.Panel p = (archimedes.legacy.scheme.Panel) Archimedes.Factory.createPanel();
		this.tabelle.addPanel(p);
		new FrameEditorDjinn("Neuanlage Panel", p, false, null) {
			public void doClosed() {
				doRepaint();
			}

			public void doChanged(boolean saveOnly) {
				tabelle.removePanel(p);
				tabelle.addPanel(p);
			}

			public void doDiscarded() {
				tabelle.removePanel(p);
			}
		};
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Bearbeiten-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEntfernen() {
		if (this.anzeige.getSelectedRow() >= 0) {
			PanelModel p = (PanelModel) this.tabelle.getPanelAt(this.anzeige.getSelectedRow());
			for (int i = 0, len = this.tabelle.getTabellenspaltenCount(); i < len; i++) {
				TabellenspaltenModel tsm = this.tabelle.getTabellenspalteAt(i);
				if (tsm.getPanel() == p) {
					JOptionPane.showMessageDialog(null, StrUtil.FromHTML("Das Panel wird durch " + tsm.toString()
							+ " referenziert!\nL&ouml;schen nicht " + "m&ouml;glich!"), "Referenzproblem!",
							JOptionPane.YES_OPTION);
					System.out.println("Wird Referenziert durch " + tsm.toString());
					return;
				}
			}
			this.tabelle.removePanel(p);
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

class PanellistenTableModel extends AbstractTableModel {

	private String[] columnname = new String[] { "Nr", "Tab" };
	private TabellenModel tm = null;

	public PanellistenTableModel(TabellenModel tm) {
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
		return tm.getPanelCount();
	}

	public int getColumnCount() {
		if (columnname != null) {
			return columnname.length;
		}
		return 1;
	}

	public Object getValueAt(int row, int column) {
		PanelModel pm = (PanelModel) tm.getPanelAt(row);
		switch (column) {
		case 0:
			return "" + pm.getPanelNumber();
		case 1:
			return pm.getTabTitle();
		}
		return "<null>";
	}

}
