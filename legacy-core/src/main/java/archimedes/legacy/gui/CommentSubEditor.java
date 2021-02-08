/*
 * BeschreibungsSubEditor.java
 *
 * 24.02.2004
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

import archimedes.model.CommentOwner;
import baccara.gui.GUIBundle;
import corent.base.Attributed;
import corent.djinn.SubEditor;

/**
 * A sub editor for comments which is to integrate into an EditorDjinn easily.
 * 
 * @author ollie
 * 
 * @changed OLI 24.02.2004 - Added.
 * @changed OLI 09.01.2009 - Extended by an implementation of the method
 *          <CODE>setObject(Attributed)</CODE> which is done caused by an
 *          extension of the interface <CODE>SubEditor</CODE>.
 * @changed OLI 11.03.2016 - Renamed from "BeschreibungsSubEditor" to
 *          "CommentSubEditor" and changed the comments and attribute names to
 *          English. Extended by resource access.
 * 
 */

public class CommentSubEditor implements SubEditor {

	private CommentOwner commentOwner = null;
	private JComponent[] components = null;
	private GUIBundle guiBundle = null;
	private JLabel[] labels = null;
	private JPanel panel = new JPanel(new BorderLayout());
	private JPanel[] panels = null;
	private JTextArea jta = new JTextArea(5, 20);

	/**
	 * Creates a new comment sub editor with the passed parameters.
	 * 
	 * @param commentOwner
	 *            The object which owns the comment to edit.
	 * @param guiBundle
	 *            A bundle with GUI information.
	 */
	public CommentSubEditor(CommentOwner commentOwner, GUIBundle guiBundle) {
		super();
		this.commentOwner = commentOwner;
		this.guiBundle = guiBundle;
		JLabel label = new JLabel(this.guiBundle.getResourceText("archimedes.CommentSubEditor.comment.label"));
		String m = this.guiBundle.getResourceText("archimedes.CommentSubEditor.comment.mnemonic");
		label.setDisplayedMnemonic(((m != null) && !m.isEmpty() ? m.charAt(0) : '\0'));
		this.jta.setText(this.commentOwner.getComment());
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

	@Override
	public void cleanupData() {
	}

	@Override
	public JComponent getComponent(int n) {
		return this.components[n];
	}

	@Override
	public int getComponentCount() {
		return this.components.length;
	}

	@Override
	public JPanel getComponentPanel(int n) {
		return this.panels[n];
	}

	@Override
	public JLabel getLabel(int n) {
		return this.labels[n];
	}

	@Override
	public JPanel getPanel() {
		return this.panel;
	}

	public void setObject(Attributed attr) {
		this.commentOwner = (CommentOwner) attr;
	}

	@Override
	public void transferData() {
		this.commentOwner.setComment(this.jta.getText());
	}

}