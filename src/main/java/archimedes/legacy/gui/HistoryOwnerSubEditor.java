/*
 * HistoryOwnerSubEditor.java
 *
 * 01.11.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import archimedes.legacy.model.HistoryOwner;
import corent.base.Attributed;
import corent.djinn.SubEditor;

/**
 * Ein in einen EditorDjinn einbindbarer SubEditor.
 * 
 * @author ollie
 * 
 * @changed OLI 01.11.2011 - Hinzugef&uuml;gt.
 */

public class HistoryOwnerSubEditor implements SubEditor {

	/* Das Objekt, dessen Hiostorie in dem Panel editiert werden soll. */
	private HistoryOwner historyOwner = null;
	/* Liste der Komponenten des Panels. */
	private JComponent[] components = null;
	/* Liste der Labels des Subeditors. */
	private JLabel[] labels = null;
	/* Referenz auf das Panel des SubEditors. */
	private JPanel panel = new JPanel(new BorderLayout());
	/* Liste der Komponentenpanels des SubEditors. */
	private JPanel[] panels = null;
	/* Die TextArea, in der die Beschreibung angelegt werden kann. */
	private JTextArea jta = new JTextArea(5, 20);

	/**
	 * Generiert einen BeschreibungsSubEditor mit den &uuml;bergebenen
	 * Parametern.
	 * 
	 * @param historyOwner
	 *            Das Objekt, das in dem Panel angezeigt werden soll.
	 */
	public HistoryOwnerSubEditor(HistoryOwner historyOwner) {
		super();
		this.historyOwner = historyOwner;
		JLabel label = new JLabel("Beschreibung");
		label.setDisplayedMnemonic('S');
		this.jta.setText(this.historyOwner.getHistory());
		this.jta.setLineWrap(true);
		this.jta.setWrapStyleWord(true);
		JScrollPane jsp = new JScrollPane(this.jta);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel panelTA = new JPanel(new GridLayout(1, 1));
		panelTA.add(jsp);
		panelTA.setBorder(new EmptyBorder(1, 1, 1, 1));
		this.panel.add(panelTA, BorderLayout.CENTER);
		this.panel.add(label, BorderLayout.NORTH);
		this.components = new JComponent[] { this.jta };
		this.labels = new JLabel[] { label };
		this.panels = new JPanel[] { panelTA };
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
		this.historyOwner = (HistoryOwner) attr;
	}

	public void transferData() {
		this.historyOwner.setHistory(this.jta.getText());
	}

}