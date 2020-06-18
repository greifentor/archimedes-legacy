/*
 * DialogTableGeneratorOptions.java
 *
 * 22.11.2007
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Tabelle;
import baccara.gui.GUIBundle;
import corent.base.Constants;
import corent.base.SortedVector;
import corent.base.StrUtil;
import corent.djinn.EditableColumnViewable;
import corent.gui.JDialogWithInifile;
import corent.gui.JTableWithInifile;

/**
 * Dieser Dialog bietet eine &Uuml;bersicht &uuml;ber die Codegenerator-Optionen der Tabellen des Datenmodells auf einen
 * Blick an.
 *
 * @author ollie
 *
 * @changed OLI 22.11.2007 - Hinzugef&uuml;gt.<BR>
 *          OLI 28.11.2007 - &Uuml;bernahme der Tabellenparameter in die Inidatei.<BR>
 *          OLI 09.12.2007 - Erweiterung der Tabellensicht um die Flagge "Aktiv in Applikation".<BR>
 *          OLI 18.06.2020 - Einbau von Buttons zum Selektieren bzw. Deselektieren aller Tabellen.<BR>
 *
 */

public class DialogTableGeneratorOptions extends JDialogWithInifile implements ActionListener {

	private static final String RES = "DialogTableGeneratorOptions";

	private JButton buttonClose = null;
	private JButton buttonSelectAll = null;
	private JButton buttonSelectNothing = null;
	private FrameArchimedes fa = null;
	private JTableWithInifile table = null;

	/**
	 * Generiert einen modalen Dialog anhand der &uuml;bergebenen Daten.
	 *
	 * @param fa        Der FrameArchimedes, von dem aus der Dialog aufgerufen wird.
	 * @param guiBundle Das GUI-Bundle der Applikation.
	 * @param dm        Das Diagramm, dessen Tabellen in der &Uuml;bersicht angezeigt werden sollen.
	 *
	 * @changed OLI 28.11.2007 - Die Tabellenparameter werden nun auch mit den Schliessen des Fensters in die Inidatei
	 *          &uuml;bernommen.<BR>
	 *
	 */
	public DialogTableGeneratorOptions(FrameArchimedes fa, GUIBundle guiBundle, DiagrammModel dm) {
		super(fa, StrUtil.FromHTML("CodeGenerator Optionen (&Uuml;bersicht)"), true, guiBundle.getInifile());
		this.fa = fa;
		JPanel panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
		panel.setBorder(
				new CompoundBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP),
						new EtchedBorder(Constants.ETCH)));
		panel.setBorder(new CompoundBorder(panel.getBorder(),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		JPanel panelButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, Constants.HGAP, Constants.VGAP));
		panelButtons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		JPanel panelTable = new JPanel(new GridLayout(1, 3, Constants.HGAP, Constants.VGAP));
		panelTable.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		this.buttonSelectAll = guiBundle.createButton(RES + ".buttons.selectAll", "button_selectAll", this,
				panelButtons);
		this.buttonSelectNothing = guiBundle.createButton(RES + ".buttons.selectNothing", "button_selectNothing", this,
				panelButtons);
		panelButtons.add(new JLabel("     "));
		this.buttonClose = guiBundle.createButton(RES + ".buttons.close", "close", this, panelButtons);
		this.buttonClose.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					buttonClose.doClick();
				}
			}
		});
		Vector vtm = dm.getTabellen();
		SortedVector svtm = new SortedVector();
		for (int i = 0, len = vtm.size(); i < len; i++) {
			svtm.addElement(new TableContainer((Tabelle) vtm.elementAt(i), this.fa));
		}
		corent.djinn.VectorTableModel cdvtm = new corent.djinn.VectorTableModel(svtm,
				new String[] { "Tabelle", "Codieren", "Erstkodierung erfolgt", "Aktiv in Applikation" });
		this.table = new JTableWithInifile(cdvtm, ini, "TableGenerationOptions");
		this.table.restoreFromIni();
		panelTable.add(new JScrollPane(this.table), BorderLayout.CENTER);
		panelButtons.add(this.buttonClose);
		panel.add(panelTable, BorderLayout.CENTER);
		panel.add(panelButtons, BorderLayout.SOUTH);
		this.setContentPane(panel);
		this.pack();
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonClose) {
			setVisible(false);
			table.saveToIni();
			try {
				getInifile().save();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			dispose();
		} else if ((e.getSource() == this.buttonSelectAll) || (e.getSource() == this.buttonSelectNothing)) {
			boolean value = e.getSource() == this.buttonSelectAll;
			for (int i = 0, leni = table.getModel().getRowCount(); i < leni; i++) {
				this.table.getModel().setValueAt(value, i, 1);
			}
			this.table.repaint();
		}
	}

}

class TableContainer implements Comparable, EditableColumnViewable {

	private FrameArchimedes fa = null;
	private Tabelle t = null;

	public TableContainer(Tabelle t, FrameArchimedes fa) {
		super();
		this.fa = fa;
		this.t = t;
	}

	/* Implementierung des Interfaces Comparable. */

	public int compareTo(Object o) {
		TableContainer tc = (TableContainer) o;
		return this.t.getName().compareTo(tc.t.getName());
	}

	/* Implementierung des Interfaces EditableColumnViewable. */

	public int getColumnCount() {
		return 4;
	}

	public Class getColumnclass(int col) {
		if (col > 0) {
			return Boolean.class;
		}
		return String.class;
	}

	public String[] getColumnnames() {
		return new String[] { "Tabelle", "Codieren", "Erstkodierung erfolgt", "Aktiv in Applikation" };
	}

	public Object getValueAt(int col) {
		switch (col) {
		case 0:
			return this.t.getName();
		case 1:
			return this.t.isGenerateCode();
		case 2:
			return this.t.isFirstGenerationDone();
		case 3:
			return this.t.isActiveInApplication();
		}
		return "-/-";
	}

	public TableCellEditor getCellEditor(int col) {
		return null;
	}

	public TableCellRenderer getCellRenderer(int col) {
		return null;
	}

	public boolean isCellEditable(int col) {
		if ((col > 0)) {
			return true;
		}
		return false;
	}

	public void setValueAt(Object obj, int col) {
		switch (col) {
		case 1:
			this.t.setGenerateCode(((Boolean) obj).booleanValue());
			this.fa.raiseAltered();
			return;
		case 2:
			this.t.setFirstGenerationDone(((Boolean) obj).booleanValue());
			this.fa.raiseAltered();
			return;
		case 3:
			this.t.setActiveInApplication(((Boolean) obj).booleanValue());
			this.fa.raiseAltered();
			return;
		}
	}

}
