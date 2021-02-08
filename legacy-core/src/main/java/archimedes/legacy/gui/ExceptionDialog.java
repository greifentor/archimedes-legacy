package archimedes.legacy.gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import baccara.gui.GUIBundle;
import corent.base.Constants;
import corent.base.Utl;
import corent.gui.JDialogWithInifile;

/**
 * A dialog to show exceptions in the GUI.
 *
 * @author ollie (14.06.2020) - Based on the class corent.gui.JDialogThrowable
 */
public class ExceptionDialog extends JDialogWithInifile implements ActionListener {

	private static final String LS = System.getProperty("line.separator");

	private JButton buttonCancel = null;
	private JButton buttonCopy = null;
	private GUIBundle guiBundle = null;
	private Icon icon = null;
	private JLabel labelExceptionClass = new JLabel();
	private JTextArea textAreaThrowableMessage = new JTextArea(2, 60);
	private JTextArea textAreaThrowableStackTrace = new JTextArea(20, 60);
	private JTextArea textAreaUserMessage = new JTextArea(2, 60);
	private String userMessage = null;
	private Throwable thrwble = null;

	public ExceptionDialog(Throwable thrwble, String userMessage, GUIBundle guiBundle) {
		this(thrwble, userMessage, guiBundle, true);
	}

	public ExceptionDialog(Throwable thrwble, String userMessage, GUIBundle guiBundle, boolean modal) {
		super(guiBundle.getInifile());
		int i = 0;
		int leni = 0;
		this.guiBundle = guiBundle;
		JPanel pButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, Constants.HGAP, Constants.VGAP));
		JPanel pHeader = this.buildPanel();
		JPanel pHeaderSub = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
		JPanel pMain = this.buildPanel();
		JPanel pMessages = new JPanel(new GridLayout(2, 1, Constants.HGAP, Constants.VGAP));
		JPanel pStackTrace = this.buildPanel();
		StackTraceElement[] ste = thrwble.getStackTrace();
		StringBuilder t = new StringBuilder();
		this.thrwble = thrwble;
		this.userMessage = (userMessage != null ? userMessage
				: guiBundle.getResourceText("ExceptionDialog.userMessage.text"));
		this.setModal(modal);
		this.setTitle(guiBundle.getResourceText("ExceptionDialog.title"));
		for (i = 0, leni = ste.length; i < leni; i++) {
			t.append(ste[i].toString()).append(LS);
		}
		pButtons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		pMain.setBorder(new CompoundBorder(
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP), pMain.getBorder()));
		this.buttonCancel = guiBundle.createButton("ExceptionDialog.buttons.cancel", "cancel", this, pButtons);
		this.buttonCopy = guiBundle.createButton("ExceptionDialog.buttons.copy", "copy", this, pButtons);
		this.labelExceptionClass.setText(thrwble.getClass().getName());
		this.labelExceptionClass
				.setFont(new Font(Utl.GetProperty("ExceptionDialog.exceptionClass.font", "sansserif"), Font.BOLD, 18));
		this.textAreaThrowableMessage.setText(thrwble.getMessage());
		this.textAreaThrowableMessage.setEditable(false);
		this.textAreaThrowableStackTrace.setText(t.toString());
		this.textAreaThrowableStackTrace.setEditable(false);
		this.textAreaUserMessage.setText(this.userMessage);
		this.textAreaUserMessage.setEditable(false);
		this.textAreaUserMessage
				.setFont(new Font(Utl.GetProperty("ExceptionDialog.userMessage.font", "sansserif"), Font.BOLD, 14));
		pButtons.add(this.buttonCopy);
		pButtons.add(this.buttonCancel);
		pHeaderSub.add(this.labelExceptionClass, BorderLayout.NORTH);
		pMessages.add(new JScrollPane(this.textAreaUserMessage));
		pMessages.add(new JScrollPane(this.textAreaThrowableMessage));
		pHeaderSub.add(pMessages, BorderLayout.CENTER);
		pHeader.add(pHeaderSub, BorderLayout.CENTER);
		if (this.icon != null) {
			pHeader.add(new JLabel(this.icon), BorderLayout.WEST);
		}
		pStackTrace.add(new JScrollPane(this.textAreaThrowableStackTrace), BorderLayout.CENTER);
		JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
		splitPane.add(pHeader);
		splitPane.add(pStackTrace);
		pMain.add(splitPane, BorderLayout.CENTER);
		pMain.add(pButtons, BorderLayout.SOUTH);
		this.setContentPane(pMain);
		this.pack();
		this.setVisible(true);
	}

	private JPanel buildPanel() {
		JPanel p = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
		p.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		return p;
	}

	public void cancel() {
		this.setVisible(false);
	}

	public void copy() {
		Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard();
		int i = 0;
		int leni = 0;
		StackTraceElement[] ste = thrwble.getStackTrace();
		StringBuilder throwabletext = new StringBuilder();
		StringSelection ssel = null;
		throwabletext
				.append(guiBundle.getResourceText("ExceptionDialog.report.prefix.message", this.thrwble.getMessage()))
				.append(LS).append(LS);
		throwabletext.append(guiBundle.getResourceText("ExceptionDialog.report.prefix.userMessage", this.userMessage))
				.append(LS).append(LS);
		throwabletext.append(
				guiBundle.getResourceText("ExceptionDialog.report.prefix.class", this.thrwble.getClass().getName()))
				.append(LS).append(LS);
		throwabletext.append(guiBundle.getResourceText("ExceptionDialog.report.prefix.stacktrace"));
		for (i = 0, leni = ste.length; i < leni; i++) {
			throwabletext.append(LS).append(ste[i].toString());
		}
		ssel = new StringSelection(throwabletext.toString());
		cb.setContents(ssel, ssel);
	}

	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == this.buttonCancel) {
			this.cancel();
		} else if (source == this.buttonCopy) {
			this.copy();
		}
	}

}