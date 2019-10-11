/*
 * NReferenzenlistenSubEditor.java
 *
 * 28.07.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.NReferenzModel;
import archimedes.legacy.model.TabellenModel;
import corent.base.Attributed;
import corent.base.Constants;
import corent.base.StrUtil;
import corent.djinn.FrameEditorDjinn;
import corent.djinn.SubEditor;
import corent.djinn.VectorPanelButtonFactory;

/**
 * Ein SubEditor zum Editieren einer Zusammenstellung von N-Referenzen.
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

public class NReferenzenlistenSubEditor implements SubEditor {

	/* Der Bearbeiten-Button des Panels. */
	private JButton buttonBearbeiten = null;
	/* Der Einf&uuml;gen-Button des Panels. */
	private JButton buttonEinfuegen = null;
	/* Der Entfernen-Button des Panels. */
	private JButton buttonEntfernen = null;
	/* Der Rauf-Button des Panels. */
	private JButton buttonRauf = null;
	/* Der Runter-Button des Panels. */
	private JButton buttonRunter = null;
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
	/* Die Tabellenansicht zur Auswahl der n-Referenzen. */
	private JTable anzeige = null;

	/* Das TabellenModel, das in dem SubEditor manipuliert werden soll. */
	private TabellenModel tabelle = null;
	/* Der zu editierende Vector mit n-Referenzen. */
	private List<Object> nreferenzen = null;

	/**
	 * Generiert einen NReferenzenlistenSubEditor mit den &uuml;bergebenen
	 * Parametern.
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
	 * @param nrme
	 *            Die Liste mit den n-Referenzen, die editiert werden soll.
	 * @param nichtbearbeitbar
	 *            Wird diese Flagge gesetzt, ist der Bearbeiten-Button
	 *            abgeblendet.
	 */
	public NReferenzenlistenSubEditor(Component owner, TabellenModel tm, VectorPanelButtonFactory vpbf,
			List<Object> nrme, boolean nichtbearbeitbar) {
		super();
		this.tabelle = tm;
		this.nreferenzen = nrme;
		this.buttonBearbeiten = vpbf.createButtonBearbeiten();
		this.buttonBearbeiten.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doButtonBearbeiten();
			}
		});
		if (nichtbearbeitbar) {
			this.buttonBearbeiten.setEnabled(false);
		}
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
		this.buttonRauf = vpbf.createButtonRauf();
		this.buttonRauf.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doButtonRauf();
			}
		});
		this.buttonRunter = vpbf.createButtonRunter();
		this.buttonRunter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				doButtonRunter();
			}
		});
		this.anzeige = new JTable(new ListTableModel(this.nreferenzen, new String[] { "n-Referenz" }));
		JPanel panelNavigation = new JPanel(new GridLayout(1, 2, Constants.HGAP, Constants.VGAP));
		panelNavigation.add(this.buttonRauf);
		panelNavigation.add(this.buttonRunter);
		JPanel panelButtons = new JPanel(new GridLayout(1, 4, Constants.HGAP, Constants.VGAP));
		panelButtons.add(panelNavigation);
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
			final NReferenzModel nrm = (NReferenzModel) this.tabelle.getNReferenzModelAt(this.anzeige.getSelectedRow());
			new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern n-Referenz"), nrm, false, null) {
				public void doClosed() {
				}

				public void doChanged(boolean saveOnly) {
					tabelle.removeNReferenz(nrm);
					tabelle.addNReference(nrm);
				}
			};
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Einfuegen-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEinfuegen() {
		this.nreferenzen.add(Archimedes.Factory.createNReferenz(this.tabelle));
		this.doRepaint();
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Bearbeiten-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEntfernen() {
		if (this.anzeige.getSelectedRow() >= 0) {
			this.nreferenzen.remove(this.anzeige.getSelectedRow());
			this.doRepaint();
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Rauf-Button
	 * bet&auml;tigt.
	 */
	public void doButtonRauf() {
		int sel = this.anzeige.getSelectedRow();
		if (sel > 0) {
			Object o = this.nreferenzen.get(sel);
			this.nreferenzen.remove(sel);
			this.nreferenzen.add(sel - 1, o);
			this.doRepaint();
			this.anzeige.setRowSelectionInterval(sel - 1, sel - 1);
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Runter-Button
	 * bet&auml;tigt.
	 */
	public void doButtonRunter() {
		int sel = this.anzeige.getSelectedRow();
		if ((sel >= 0) & (sel < this.nreferenzen.size() - 1)) {
			Object o = this.nreferenzen.get(sel);
			this.nreferenzen.remove(sel);
			this.nreferenzen.add(sel + 1, o);
			this.doRepaint();
			this.anzeige.setRowSelectionInterval(sel + 1, sel + 1);
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
		this.tabelle = (TabellenModel) tabelle;
	}

	public void transferData() {
	}

}
