package archimedes.legacy.acf.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import archimedes.legacy.acf.event.CodeFactoryProgressionEvent;
import baccara.gui.GUIBundle;
import corent.gui.JFrameWithInifile;

/**
 * A frame to show the progression of the generation process.
 *
 * @author ollie (12.01.2020)
 */
public class CodeFactoryProgressionFrame extends JFrameWithInifile implements ActionListener {

	private JButton buttonClose = null;
	private JLabel labelFactory = new JLabel();
	private JLabel labelProcess = new JLabel();
	private JLabel labelStep = new JLabel();
	private JProgressBar progressBarFactory = new JProgressBar();
	private JProgressBar progressBarProcesses = new JProgressBar();
	private JProgressBar progressBarSteps = new JProgressBar();
	private JTextArea textArea = new JTextArea(40, 10);

	private GUIBundle guiBundle = null;

	public CodeFactoryProgressionFrame(GUIBundle guiBundle) {
		super(guiBundle.getInifile());
		this.guiBundle = guiBundle;
		setTitle(this.guiBundle.getResourceText("archimedes.CodeFactoryProgressionFrame.title"));
		setContentPane(getMainPanel());
		pack();
		setVisible(true);
	}

	private JPanel getMainPanel() {
		JPanel panel = new JPanel(new BorderLayout(this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		panel.setBorder(new EmptyBorder(this.guiBundle.getVGap(), this.guiBundle.getHGap(), this.guiBundle.getVGap(),
				this.guiBundle.getHGap()));
		panel.add(getProgressBarPanel(), BorderLayout.NORTH);
		panel.add(getMessagePanel(), BorderLayout.CENTER);
		panel.add(getButtonPanel(), BorderLayout.SOUTH);
		return panel;
	}

	private JPanel getProgressBarPanel() {
		JPanel panel = new JPanel(new GridLayout(6, 1, this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		this.progressBarFactory.setMinimum(0);
		this.progressBarProcesses.setMinimum(1);
		this.progressBarSteps.setMinimum(1);
		this.progressBarFactory.setStringPainted(true);
		this.progressBarProcesses.setStringPainted(true);
		this.progressBarSteps.setStringPainted(true);
		panel.add(this.labelFactory);
		panel.add(this.progressBarFactory);
		panel.add(this.labelStep);
		panel.add(this.progressBarSteps);
		panel.add(this.labelProcess);
		panel.add(this.progressBarProcesses);
		return panel;
	}

	private JPanel getMessagePanel() {
		JPanel panel = new JPanel(new GridLayout(1, 1, this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		this.textArea.setEditable(false);
		panel.add(new JScrollPane(this.textArea));
		return panel;
	}

	private JPanel getButtonPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, this.guiBundle.getHGap(), this.guiBundle.getVGap()));
		this.buttonClose = this.guiBundle.createButton("archimedes.CodeFactoryProgressionFrame.button.close", "close",
				this, panel);
		this.buttonClose.setEnabled(false);
		panel.add(this.buttonClose);
		return panel;
	}

	@Override
	public void actionPerformed(ActionEvent event) {
		if (event.getSource() == this.buttonClose) {
			setVisible(false);
			dispose();
		}
	}

	public void enableCloseButton() {
		this.buttonClose.setEnabled(true);
	}

	public void processEvent(CodeFactoryProgressionEvent event) {
		if (event.getMaximumProcessCount() != null) {
			this.progressBarProcesses.setMaximum(event.getMaximumProcessCount());
		}
		if (event.getMaximumStepCount() != null) {
			this.progressBarSteps.setMaximum(event.getMaximumStepCount());
		}
		if (event.getCurrentProcess() != null) {
			this.progressBarProcesses.setValue(event.getCurrentProcess());
			this.progressBarProcesses
					.setString(event.getCurrentProcess() + " (" + progressBarProcesses.getMaximum() + ")");
		}
		if (event.getCurrentStep() != null) {
			this.progressBarSteps.setValue(event.getCurrentStep());
			this.progressBarSteps.setString(event.getCurrentStep() + " (" + progressBarSteps.getMaximum() + ")");
		}
		if (event.getFactoryName() != null) {
			this.labelStep.setText(event.getFactoryName());
		}
		if (event.getMessage() != null) {
			this.textArea.setText(
					this.textArea.getText() + (this.textArea.getText().isEmpty() ? "" : "\n") + event.getMessage());
		}
		if (event.getProcessName() != null) {
			this.labelProcess.setText(event.getProcessName());
		}
	}

	public void updateFactory(Integer current, Integer max, String factoryName, String message) {
		if (max != null) {
			this.progressBarFactory.setMaximum(max);
		}
		if (current != null) {
			this.progressBarFactory.setValue(current);
			this.progressBarFactory.setString(current + " (" + this.progressBarFactory.getMaximum() + ")");
		}
		if (factoryName != null) {
			this.labelFactory.setText(factoryName);
		}
		if (message != null) {
			this.textArea.setText(this.textArea.getText() + (this.textArea.getText().isEmpty() ? "" : "\n") + message);
		}
	}

}