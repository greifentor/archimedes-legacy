package archimedes.legacy.gui.codepath;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import baccara.gui.GUIBundle;
import corent.gui.JDialogWithInifile;

/**
 * A dialog to edit a code path.
 *
 * @author ollie (26.01.2020)
 */
public class CodePathDialog extends JDialogWithInifile implements ActionListener, WindowListener { // NOSONAR

	private JButton buttonOk = null;
	private JButton buttonSelectPath = null;
	private CodePathEditorPanel codePathEditorPanel = null;
	private GUIBundle guiBundle = null;
	private CodePath path = null;

	public CodePathDialog(GUIBundle guiBundle, String path) {
		super(guiBundle.getInifile());
		this.guiBundle = guiBundle;
		this.addWindowListener(this);
		this.setTitle(guiBundle.getResourceText("CodePathDialog.title"));
		this.path = new CodePath().setPath(path);
		this.setContentPane(createMainPanel());
		this.pack();
		this.setModal(true);
		this.setVisible(true);
	}

	private JPanel createMainPanel() {
		JPanel p = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		p.setBorder(new EmptyBorder(this.guiBundle.getVGap(), this.guiBundle.getHGap(), this.guiBundle.getVGap(),
				this.guiBundle.getHGap()));
		this.codePathEditorPanel = new CodePathEditorPanel(guiBundle, this, this, this.path);
		p.add(this.codePathEditorPanel, BorderLayout.NORTH);
		p.add(createButtonPanel(), BorderLayout.SOUTH);
		return p;
	}

	private JPanel createButtonPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		this.buttonOk = this.guiBundle.createButton("CodePathDialog.button.ok", "ok", this, p);
		p.add(this.buttonOk);
		return p;
	}

	/**
	 * Returns the path from the dialog.
	 * 
	 * @return The path from the dialog.
	 */
	public String getPath() {
		return this.path.getPath();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonOk) {
			this.codePathEditorPanel.transferChangesUnchecked(this.path);
			this.setVisible(false);
		}
	}

	@Override
	public void windowActivated(WindowEvent e) { // NOSONAR OLI Is empty ...
	}

	@Override
	public void windowClosed(WindowEvent e) {
		this.path.setPath("");
	}

	@Override
	public void windowClosing(WindowEvent e) {
		this.path.setPath("");
	}

	@Override
	public void windowDeactivated(WindowEvent e) { // NOSONAR OLI Is empty ...
	}

	@Override
	public void windowDeiconified(WindowEvent e) { // NOSONAR OLI Is empty ...
	}

	@Override
	public void windowIconified(WindowEvent e) { // NOSONAR OLI Is empty ...
	}

	@Override
	public void windowOpened(WindowEvent e) { // NOSONAR OLI Is empty ...
	}

}