package archimedes.legacy.gui.codepath;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;

import baccara.gui.GUIBundle;
import corent.gui.JDialogWithInifile;

/**
 * A dialog to edit a code path.
 *
 * @author ollie (26.01.2020)
 */
public class CodePathDialog extends JDialogWithInifile implements ActionListener {

	private CodePath path = null;
	private JButton buttonOk = null;
	private CodePathEditorPanel codePathEditorPanel = null;

	public CodePathDialog(GUIBundle guiBundle, String path) {
		super(guiBundle.getInifile());
		this.path = new CodePath().setPath(path);
		this.setContentPane(createMainPanel(guiBundle));
		this.pack();
		this.setModal(true);
		this.setVisible(true);
	}

	private JPanel createMainPanel(GUIBundle guiBundle) {
		JPanel p = new JPanel(new BorderLayout(guiBundle.getHGap(), guiBundle.getVGap()));
		this.codePathEditorPanel = new CodePathEditorPanel(guiBundle, this, this, this.path);
		p.add(this.codePathEditorPanel, BorderLayout.NORTH);
		p.add(createButtonPanel(guiBundle), BorderLayout.SOUTH);
		return p;
	}

	private JPanel createButtonPanel(GUIBundle guiBundle) {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, guiBundle.getHGap(), guiBundle.getVGap()));
		this.buttonOk = guiBundle.createButton("CodePathDialog.button.ok", "ok", this, p);
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

}