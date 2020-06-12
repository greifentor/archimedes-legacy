package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import archimedes.legacy.importer.jdbc.ModelReaderEvent;
import corent.files.Inifile;
import corent.gui.JFrameWithInifile;

/**
 * A dialog to show the model read progress.
 *
 * @author ollie (12.06.2020)
 */
public class ModelReaderProgressMonitor extends JFrameWithInifile {

	private static final int HGAP = 3;
	private static final int VGAP = 3;

	private JButton buttonClose = new JButton("Schließen");
	private JLabel labelProgressAll = new JLabel("Progress of Model Read Process");
	private JLabel labelProgressThread = new JLabel("Progress of Thread");
	private JLabel labelMessages = new JLabel("Messages");
	private JProgressBar progressBarAll = new JProgressBar(1, 5);
	private JProgressBar progressBarThread = new JProgressBar(0, 100);
	private JTextArea textAreaMessages = new JTextArea(10, 80);

	public ModelReaderProgressMonitor(JFrame f, Inifile ini) {
		super("Model Reader Progress", ini);
		this.setContentPane(createMainPanel());
		this.pack();
		this.setVisible(true);
		centerDialog();
	}

	private JPanel createMainPanel() {
		JPanel p = new JPanel(new BorderLayout(HGAP, VGAP));
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
		p.add(new JScrollPane(this.textAreaMessages), BorderLayout.CENTER);
		return p;
	}

	private JPanel createButtonPanel() {
		JPanel p = new JPanel(new FlowLayout(FlowLayout.RIGHT, HGAP, VGAP));
		this.buttonClose.addActionListener(event -> this.setVisible(false));
		p.add(this.buttonClose);
		return p;
	}

	private void centerDialog() {
		Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
		this.setLocation(dim.width / 2 - this.getSize().width / 2, dim.height / 2 - this.getSize().height / 2);
	}

	public void update(ModelReaderEvent event) {
		this.progressBarAll.setValue(event.getThread());
		this.progressBarThread.setMaximum(event.getMaximumProgress() - 1);
		this.progressBarThread.setValue(event.getCurrentProgress());
		this.textAreaMessages.setText(this.textAreaMessages.getText() //
				+ String.format("%-30s - %s", event.getObjectName(), event.getType().name()) //
				+ "\n" //
		);
	}

}