/*
 * TableModelFrame.java
 *
 * 11.10.2017
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
import java.awt.event.KeyEvent;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import archimedes.legacy.gui.FrameArchimedes;
import archimedes.model.TableModel;
import baccara.gui.BorderToRedWrapper;
import baccara.gui.GUIBundle;
import baccara.gui.KeyListenerUtil;
import corent.gui.JFrameWithInifile;

/**
 * A frame for table model maintenance.
 * 
 * @author ollie
 * 
 * @changed OLI 11.10.2017 - Added.
 */

public class TableModelFrame extends JFrameWithInifile implements ActionListener {

	private static final String RES_ID_PREF = "TableModelFrame.";
	private static final String RES_ID_PREF_BUTTONS = RES_ID_PREF + "Buttons.";
	private static final String RES_ID_PREF_TABS = RES_ID_PREF + "Tabs.";
	private static final String RES_ID_TITLE = RES_ID_PREF + "title";

	private GUIBundle guiBundle = null;
	private JButton buttonCancel = null;
	private JButton buttonOk = null;
	private JButton buttonRemove = null;
	private FrameArchimedes fa = null;
	private JPanel mainPanel = null;
	private JTabbedPane tabbedPane = null;
	private TableModel tm = null;

	/**
	 * Creates a new frame for a table model maintenance with the passed
	 * parameters.
	 * 
	 * @param guiBundle
	 *            A GUI bundle with different information.
	 * @param tableModel
	 *            The table model which is to maintain by the frame.
	 * @param fa
	 *            A reference of the application frame.
	 * 
	 * @changed OLI 11.10.2017 - Added.
	 */
	public TableModelFrame(GUIBundle guiBundle, TableModel tm, FrameArchimedes fa) {
		super(guiBundle.getResourceText(RES_ID_TITLE, tm.getName()), guiBundle.getInifile());
		this.fa = fa;
		this.guiBundle = guiBundle;
		this.tm = tm;
		this.mainPanel = new JPanel(this.guiBundle.createBorderLayout());
		/*
		 * Tabs fuer wie in urspruenglicher Sicht aber:
		 * 
		 * - Listen oeffnen keinen separaten Dialog. - Dabei alle Informationen
		 * ausklammern, die nicht im TableModel enthalten sind.
		 */
		this.mainPanel.add(this.createInputPanel(tm), BorderLayout.CENTER);
		this.mainPanel.add(this.createButtonPanel(), BorderLayout.SOUTH);
		this.setContentPane(this.mainPanel);
		this.pack();
		this.setVisible(true);
		new KeyListenerUtil().createCycle(this.getFocusableComponents(), this.buttonOk, this.buttonCancel, true,
				KeyEvent.VK_UP, KeyEvent.VK_DOWN, KeyEvent.VK_ENTER + KeyEvent.VK_SHIFT, KeyEvent.VK_F12,
				KeyEvent.VK_CANCEL);
	}

	private JTabbedPane createInputPanel(TableModel tm) {
		this.tabbedPane = new JTabbedPane();
		this.tabbedPane.add(this.guiBundle.getResourceText(RES_ID_PREF_TABS + "General.title"),
				new TableModelGeneralPanel(this.guiBundle, this, "TableModelGeneralPanel.Labels", this, tm));
		this.tabbedPane.add(this.guiBundle.getResourceText(RES_ID_PREF_TABS + "Columns.title"),
				new TableModelColumnsPanel(this.guiBundle, tm));
		return this.tabbedPane;
	}

	private JPanel createButtonPanel() {
		JPanel p0 = new JPanel(new FlowLayout(FlowLayout.LEFT));
		this.buttonRemove = this.guiBundle.createButton(RES_ID_PREF_BUTTONS + "Remove.text", "remove", this, null);
		p0.add(new BorderToRedWrapper(this.buttonRemove));
		JPanel p1 = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		this.buttonCancel = this.guiBundle.createButton(RES_ID_PREF_BUTTONS + "Cancel.text", "cancel", this, null);
		this.buttonOk = this.guiBundle.createButton(RES_ID_PREF_BUTTONS + "Ok.text", "ok", this, null);
		p1.add(new BorderToRedWrapper(this.buttonOk));
		p1.add(new BorderToRedWrapper(this.buttonCancel));
		JPanel p = new JPanel(this.guiBundle.createBorderLayout());
		p.add(p0, BorderLayout.WEST);
		p.add(p1, BorderLayout.EAST);
		return p;
	}

	private Component[] getFocusableComponents() {
		List<Component> l = new LinkedList<Component>();
		for (Component p : this.tabbedPane.getComponents()) {
			if (p instanceof DataInputPanel<?>) {
				for (Component c : ((DataInputPanel<?>) p).getDataInputComponents()) {
					l.add(c);
				}
			}
		}
		l.add(this.buttonOk);
		l.add(this.buttonCancel);
		return l.toArray(new Component[0]);
	}

	/**
	 * @changed OLI 13.10.2017 - Added.
	 */
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonCancel) {
			this.dispose();
		} else if (e.getSource() == this.buttonOk) {
			for (Component p : this.tabbedPane.getComponents()) {
				if (p instanceof DataInputPanel<?>) {
					((DataInputPanel<TableModel>) p).transferData(this.tm);
				}
			}
			this.fa.raiseAltered();
		} else if (e.getSource() == this.buttonRemove) {
			this.fa.deleteTable(this.tm);
			this.fa.raiseAltered();
		}
		this.dispose();
		this.fa.doRepaint();
	}

}