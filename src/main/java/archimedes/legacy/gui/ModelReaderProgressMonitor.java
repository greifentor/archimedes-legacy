package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

import archimedes.legacy.importer.jdbc.ModelReaderEvent;
import baccara.gui.GUIBundle;
import corent.gui.JFrameWithInifile;

/**
 * A dialog to show the model read progress.
 *
 * @author ollie (12.06.2020)
 */
public class ModelReaderProgressMonitor extends JFrameWithInifile {

	private static final String RES_BASE = "ModelReaderProgressMonitor";

	private static final int HGAP = 3;
	private static final int VGAP = 3;

	private JButton buttonClose = new JButton("Schließen");
	private GUIBundle guiBundle = null;
	private JLabel labelProgressAll = new JLabel("Progress of Model Read Process");
	private JLabel labelProgressThread = new JLabel("Progress of Thread");
	private JLabel labelMessages = new JLabel("Messages");
	private JProgressBar progressBarAll = new JProgressBar(1, 5);
	private JProgressBar progressBarThread = new JProgressBar(0, 100);
	private JTextArea textAreaMessages = new JTextArea(10, 80);

	public ModelReaderProgressMonitor(GUIBundle guiBundle) {
		super("Model Reader Progress", guiBundle.getInifile());
		this.guiBundle = guiBundle;
		this.setContentPane(createMainPanel());
		this.pack();
		this.setVisible(true);
		centerDialog();
	}

	private JPanel createMainPanel() {
		JPanel p = new JPanel(new BorderLayout(HGAP, VGAP));
		p.setBorder(guiBundle.createEmptyBorder());
		p.add(createProgressPanel(), BorderLayout.NORTH);
		p.add(createMessagesPanel(), BorderLayout.CENTER);
		p.add(createButtonPanel(), BorderLayout.SOUTH);
		return p;
	}

	private JPanel createProgressPanel() {
		JPanel p = new JPanel(new GridLayout(4, 1, HGAP, VGAP));
		p.add(this.labelProgressAll);
		p.add(this.progressBarAll);
		p.add(this.labelProgressThread);
		p.add(this.progressBarThread);
		return p;
	}

	private JPanel createMessagesPanel() {
		JPanel p = new JPanel(new BorderLayout(HGAP, VGAP));
		this.textAreaMessages.setFont(new Font("monospaced", Font.PLAIN, 12));
		p.add(this.labelMessages, BorderLayout.NORTH);
		this.textAreaMessages.setEditable(false);
		DefaultCaret caret = (DefaultCaret) this.textAreaMessages.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		p.add(new JScrollPane(this.textAreaMessages), BorderLayout.CENTER);
		return p;
	}

	private JPanel createButtonPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, HGAP, VGAP));
		this.buttonClose = this.guiBundle.createButton(RES_BASE + ".buttons.close", "close",
				event -> this.setVisible(false), p);
		this.buttonClose.setEnabled(false);
		p.add(this.buttonClose);
		return p;
	}

	private void centerDialog() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	}

	public void update(ModelReaderEvent event) {
		this.progressBarAll.setValue(event.getThread());
		this.progressBarAll.setString("" + event.getThread());
		this.progressBarThread.setMaximum(event.getMaximumProgress() - 1);
		this.progressBarThread.setValue(event.getCurrentProgress());
		this.progressBarThread.setString("" + event.getCurrentProgress());
		this.textAreaMessages.setText(this.textAreaMessages.getText() //
				+ String.format("%-30s - %s", event.getObjectName(), event.getType().name()) //
				+ "\n" //
		);
	}

	public void enableCloseButton() {
		buttonClose.setEnabled(true);
	}

}