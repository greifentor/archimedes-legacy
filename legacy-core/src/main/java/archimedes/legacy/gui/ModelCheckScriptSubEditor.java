/*
 * BeschreibungsSubEditor.java
 *
 * 24.02.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import archimedes.model.CommentOwner;
import archimedes.model.DataModel;
import baccara.gui.GUIBundle;
import corent.base.Attributed;
import corent.djinn.SubEditor;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

/**
 * A sub editor for model checker scripts which is to integrate into an EditorDjinn easily.
 * 
 * @author ollie
 * 
 * @changed OLI 26.07.2021 - Added (based on CommentSubEditor).
 * 
 */

public class ModelCheckScriptSubEditor implements SubEditor {

	private DataModel dataModel = null;
	private JComponent[] components = null;
	private JLabel[] labels = null;
	private JPanel panel = new JPanel(new BorderLayout());
	private JPanel[] panels = null;
	private JTextArea jta = new JTextArea(5, 20);

	/**
	 * Creates a new model checker script sub editor with the passed parameters.
	 *
	 * @param dataModel
	 *            The object which owns the comment to edit.
	 * @param guiBundle
	 *            A bundle with GUI information.
	 */
	public ModelCheckScriptSubEditor(DataModel dataModel, GUIBundle guiBundle) {
		super();
		this.dataModel = dataModel;
		JLabel label = new JLabel(guiBundle.getResourceText("archimedes.ModelCheckerScriptSubEditor.script.label"));
		String m = guiBundle.getResourceText("archimedes.ModelCheckerScriptSubEditor.script.mnemonic");
		label.setDisplayedMnemonic(((m != null) && !m.isEmpty() ? m.charAt(0) : '\0'));
		jta.setText(dataModel.getModelCheckerScript());
		jta.setFont(new Font("monospaced", Font.PLAIN, 12));
		jta.setLineWrap(true);
		jta.setWrapStyleWord(true);
		JScrollPane jsp = new JScrollPane(jta);
		jsp.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		JPanel panelTA = new JPanel(new GridLayout(1, 1));
		panelTA.add(jsp);
		panelTA.setBorder(new EmptyBorder(1, 1, 1, 1));
		panel.add(panelTA, BorderLayout.CENTER);
		panel.add(label, BorderLayout.NORTH);
		components = new JComponent[] { jta };
		labels = new JLabel[] { label };
		panels = new JPanel[] { panelTA };
	}

	@Override
	public void cleanupData() {
	}

	@Override
	public JComponent getComponent(int n) {
		return components[n];
	}

	@Override
	public int getComponentCount() {
		return components.length;
	}

	@Override
	public JPanel getComponentPanel(int n) {
		return panels[n];
	}

	@Override
	public JLabel getLabel(int n) {
		return labels[n];
	}

	@Override
	public JPanel getPanel() {
		return panel;
	}

	public void setObject(Attributed attr) {
		dataModel = (DataModel) attr;
	}

	@Override
	public void transferData() {
		dataModel.setModelCheckerScript(jta.getText());
	}

}