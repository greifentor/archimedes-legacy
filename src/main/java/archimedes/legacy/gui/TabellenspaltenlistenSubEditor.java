/*
 * TabellenspaltenlistenSubEditor.java
 *
 * 28.07.2004
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
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.border.EmptyBorder;
import javax.swing.table.AbstractTableModel;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.scheme.OrderMember;
import archimedes.model.OrderMemberModel;
import archimedes.model.SelectionMemberModel;
import archimedes.scheme.SelectionMember;
import corent.base.Attributed;
import corent.base.Constants;
import corent.base.StrUtil;
import corent.djinn.DefaultListViewComponentFactory;
import corent.djinn.DialogSelectionDjinn;
import corent.djinn.FrameEditorDjinn;
import corent.djinn.SubEditor;
import corent.djinn.VectorPanelButtonFactory;

/**
 * Ein SubEditor zum Editieren einer Zusammenstellung von Tabellenspalten.
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

public class TabellenspaltenlistenSubEditor implements SubEditor {

	private boolean allts = false;
	/*
	 * Referenz auf die Component, in der der &uuml;bergeordnete EditorDjinn
	 * angezeigt wird.
	 */
	private Component owner = null;
	/* Der Typ des SubEditors. */
	private int type = 0;
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
	/* Die Tabellenansicht zur Auswahl der Tabellenspalten. */
	private JTable anzeige = null;

	/* Das TabellenModel, das in dem SubEditor manipuliert werden soll. */
	private TabellenModel tabelle = null;
	/* Der zu editierende Vector mit Tabellenspalten. */
	private List<Object> tabellenspalten = null;

	/**
	 * Generiert einen TabellenspaltenlistenSubEditor mit den &uuml;bergebenen
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
	 * @param tse
	 *            Die Liste mit den Tabellenspalten, die editiert werden soll.
	 * @param nichtbearbeitbar
	 *            Wird diese Flagge gesetzt, ist der Bearbeiten-Button
	 *            abgeblendet.
	 * @param type
	 *            Der Typ des SubEditors: 1=ToStringContainer, 2=OrderContainer.
	 * @param allts
	 *            Wird diese Flagge gesetzt, so werden alle Tabellenspalten des
	 *            Diagramms zur Auswahl angeboten.
	 */
	public TabellenspaltenlistenSubEditor(Component owner, TabellenModel tm, VectorPanelButtonFactory vpbf,
			List<Object> tse, boolean nichtbearbeitbar, int type, boolean allts) {
		super();
		this.owner = owner;
		this.tabelle = tm;
		this.tabellenspalten = tse;
		this.type = type;
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
		this.anzeige = new JTable(new ListTableModel(this.tabellenspalten));
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
			switch (this.getType()) {
			case 0:
				final TabellenspaltenModel ts = (TabellenspaltenModel) this.tabelle.getTabellenspalteAt(this.anzeige
						.getSelectedRow());
				new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern Tabellenspalte ") + ts.getName(), ts, false, null) {
					public void doClosed() {
					}

					public void doChanged(boolean saveOnly) {
						tabelle.removeTabellenspalte(ts);
						ts.setTabelle(tabelle);
						tabelle.addTabellenspalte(ts);
						doRepaint();
					}
				};
				break;
			case 1:
				final ToStringContainer tsc = (ToStringContainer) this.tabelle.getToStringMembers()[this.anzeige
						.getSelectedRow()];
				new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern ToStringContainer"), tsc, false, null) {
					public void doClosed() {
					}

					public void doChanged(boolean saveOnly) {
						doRepaint();
					}
				};
				break;
			case 2:
				final ToStringContainer tsc0 = (ToStringContainer) this.tabelle.getComboStringMembers()[this.anzeige
						.getSelectedRow()];
				new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern ToStringContainer"), tsc0, false, null) {
					public void doClosed() {
					}

					public void doChanged(boolean saveOnly) {
						doRepaint();
					}
				};
				break;
			case 3:
				final OrderMember omm = (OrderMember) this.tabelle.getSelectionViewOrderMembers()[this.anzeige
						.getSelectedRow()];
				new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern OrderMember"), omm, false, null) {
					public void doClosed() {
					}

					public void doChanged(boolean saveOnly) {
						doRepaint();
					}
				};
				break;
			case 4:
				final SelectionMember smm = (SelectionMember) this.tabelle.getAuswahlMembers().elementAt(
						this.anzeige.getSelectedRow());
				new FrameEditorDjinn(StrUtil.FromHTML("&Auml;ndern SelectionMember"), smm, false, null) {
					public void doClosed() {
					}

					public void doChanged(boolean saveOnly) {
						doRepaint();
					}
				};
				break;
			}
		}
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Einfuegen-Button
	 * bet&auml;tigt.
	 */
	public void doButtonEinfuegen() {
		DialogSelectionDjinn dsd = new DialogSelectionDjinn((Frame) this.owner, "Auswahl Tabellenspalte",
				new DefaultListViewComponentFactory((allts ? this.tabelle.getDiagramm().getFieldCache()
						: new Vector<Object>(Arrays.asList(this.tabelle.getColumns())))));
		if (dsd.isSelected()) {
			Vector v = dsd.getSelection();
			Vector values = new Vector();
			for (int i = 0, len = v.size(); i < len; i++) {
				if (this.getType() == 0) {
					values.addElement(v.elementAt(i));
				} else if ((this.getType() == 1) || (this.getType() == 2)) {
					values.addElement(new ToStringContainer((TabellenspaltenModel) v.elementAt(i), this.tabelle));
				} else if (this.getType() == 3) {
					values.addElement(Archimedes.Factory.createOrderMember((TabellenspaltenModel) v.elementAt(i)));
				} else if (this.getType() == 4) {
					values.addElement(Archimedes.Factory.createSelectionMember((TabellenspaltenModel) v.elementAt(i)));
				}
			}
			for (int i = 0, len = values.size(); i < len; i++) {
				boolean found = false;
				for (int j = 0, lenj = this.tabellenspalten.size(); j < lenj; j++) {
					if (this.getType() == 0) {
						TabellenspaltenModel tsm = (TabellenspaltenModel) this.tabellenspalten.get(j);
						if (values.elementAt(i) == tsm) {
							found = true;
							break;
						}
					} else if ((this.getType() == 1) || (this.getType() == 2)) {
						ToStringContainer tsc = (ToStringContainer) this.tabellenspalten.get(j);
						if (((ToStringContainer) values.elementAt(i)).getTabellenspalte() == tsc) {
							found = true;
							break;
						}
					} else if (this.getType() == 3) {
						OrderMemberModel omm = (OrderMemberModel) this.tabellenspalten.get(j);
						if (((OrderMemberModel) values.elementAt(i)).getOrderColumn() == omm) {
							found = true;
							break;
						}
					} else if (this.getType() == 4) {
						SelectionMemberModel smm = (SelectionMemberModel) this.tabellenspalten.get(j);
						if (((SelectionMemberModel) values.elementAt(i)).getColumn() == smm) {
							found = true;
							break;
						}
					}
				}
				if (!found) {
					if (this.anzeige.getSelectedRow() >= 0) {
						this.tabellenspalten.add(this.anzeige.getSelectedRow(), values.get(i));
					} else {
						this.tabellenspalten.add(values.get(i));
					}
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
			this.tabellenspalten.remove(this.anzeige.getSelectedRow());
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
			Object o = this.tabellenspalten.get(sel);
			this.tabellenspalten.remove(sel);
			this.tabellenspalten.add(sel - 1, o);
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
		if ((sel >= 0) & (sel < this.tabellenspalten.size() - 1)) {
			Object o = this.tabellenspalten.get(sel);
			this.tabellenspalten.remove(sel);
			this.tabellenspalten.add(sel + 1, o);
			this.doRepaint();
			this.anzeige.setRowSelectionInterval(sel + 1, sel + 1);
		}
	}

	private void doRepaint() {
		((AbstractTableModel) anzeige.getModel()).fireTableDataChanged();
	}

	/**
	 * Diese Methode zeigt den Typ des SubEditors an. Sie mu&szlig;
	 * &uuml;berschrieben werden, wenn Listen anderer Objekte, als
	 * Tabellenspalten, editiert werden sollen: 1=ToStringContainer,
	 * 2=OrderContainer.
	 */
	public int getType() {
		return this.type;
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
