/*
 * TextAreaDialog.java
 *
 * 06.10.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import corent.base.Constants;
import corent.base.StrUtil;
import corent.files.Inifile;
import logging.Logger;

/**
 * Dieser Texteditor erlaubt das Editieren eines Strings in einer TextArea.
 * <P>
 * &Uuml;ber die Property <I>corent.gui.TextAreaDialog.ctrl</I> lassen sich zwei
 * unterschiedliche Komfortmodi f&uuml;r das Eingabefeld einstellen. Wird der
 * Wert "HTML" gesetzt so kann &uuml;ber die Tastenkombinationen
 * <TT>CTRL-B</TT>, <TT>CTRL-I</TT> und <TT>CTRL-U</TT> ein entsprechendes
 * HTML-Tag an der Cursorposition eingef&uuml;gt werden. Wird hingegen der Wert
 * "$HTML" gesetzt, so werden &uuml;ber die gleichen Tastenkombinationen
 * entsprechende $-Tags gesetzt (z. B. $&lt;B$&gt;).
 *
 * @author O.Lieshoff
 *
 */

public class TextAreaDialog extends JDialogWithInifile {

	private static final Logger log = Logger.getLogger(TextAreaDialog.class);

	/* Die TextArea, in der der Text editiert werden soll. */
	private JTextArea textArea = null;
	/* Speichert den urspr&uuml;nglichen Text. */
	private String text = null;

	/**
	 * Generiert einen TextAreaDialog aus den &uuml;bergebenen Parametern.
	 *
	 * @param tadcf Eine TextAreaDialogComponentFactory, die die Komponenten des
	 *              TextArea-Dialoges herstellt.
	 * @param text  Der Text, der in der TextArea editiert werden soll.
	 */
	public TextAreaDialog(TextAreaDialogComponentFactory tadcf, String text) {
		this(tadcf, text, true, null);
	}

	/**
	 * Generiert einen TextAreaDialog aus den &uuml;bergebenen Parametern.
	 *
	 * @param tadcf   Eine TextAreaDialogComponentFactory, die die Komponenten des
	 *                TextArea-Dialoges herstellt.
	 * @param text    Der Text, der in der TextArea editiert werden soll.
	 * @param enabled Diese Flagge ist zu setzen, wenn das Texteingabefeld nicht
	 *                abgeblendet werden soll.
	 */
	public TextAreaDialog(TextAreaDialogComponentFactory tadcf, String text, boolean enabled) {
		this(tadcf, text, enabled, null);
	}

	/**
	 * Generiert einen TextAreaDialog aus den &uuml;bergebenen Parametern.
	 *
	 * @param tadcf   Eine TextAreaDialogComponentFactory, die die Komponenten des
	 *                TextArea-Dialoges herstellt.
	 * @param text    Der Text, der in der TextArea editiert werden soll.
	 * @param enabled Diese Flagge ist zu setzen, wenn das Texteingabefeld nicht
	 *                abgeblendet werden soll.
	 * @param ini     Die Inidatei, aus der der TextAreaDialog seine Gestalt
	 *                rekonstruieren soll.
	 */
	public TextAreaDialog(TextAreaDialogComponentFactory tadcf, String text, boolean enabled, Inifile ini) {
		this(tadcf, text, enabled, ini, null);
	}

	/**
	 * Generiert einen TextAreaDialog aus den &uuml;bergebenen Parametern.
	 *
	 * @param tadcf       Eine TextAreaDialogComponentFactory, die die Komponenten
	 *                    des TextArea-Dialoges herstellt.
	 * @param text        Der Text, der in der TextArea editiert werden soll.
	 * @param enabled     Diese Flagge ist zu setzen, wenn das Texteingabefeld nicht
	 *                    abgeblendet werden soll.
	 * @param ini         Die Inidatei, aus der der TextAreaDialog seine Gestalt
	 *                    rekonstruieren soll.
	 * @param keyListener Ein KeyListener, der die Aktionen im Dialog belauscht.
	 */
	public TextAreaDialog(TextAreaDialogComponentFactory tadcf, String text, boolean enabled, Inifile ini,
			KeyListener keyListener) {
		super(tadcf.getModalParent(), tadcf.getTitel(), true, ini);
		this.text = text;
		JPanel panel = new JPanel(new BorderLayout(Constants.HGAP, Constants.VGAP));
		panel.setBorder(
				new CompoundBorder(new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP),
						new EtchedBorder(Constants.ETCH)));
		panel.setBorder(new CompoundBorder(panel.getBorder(),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		JPanel panelButtons = new JPanel(new GridLayout(1, 5, Constants.HGAP, Constants.VGAP));
		panelButtons.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		JButton buttonOk = tadcf.createButtonOk(this);
		buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doButtonOk();
			}
		});
		JButton buttonVerwerfen = tadcf.createButtonVerwerfen(this);
		buttonVerwerfen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doButtonVerwerfen();
			}
		});
		panelButtons.add(new JLabel(""));
		panelButtons.add(new JLabel(""));
		panelButtons.add(new JLabel(""));
		if (enabled) {
			panelButtons.add(buttonOk);
		} else {
			panelButtons.add(new JLabel(""));
		}
		panelButtons.add(buttonVerwerfen);
		JPanel panelAnzeige = new JPanel(new GridLayout(1, 1, Constants.HGAP, Constants.VGAP));
		panelAnzeige.setBorder(new CompoundBorder(new EtchedBorder(Constants.ETCH),
				new EmptyBorder(Constants.HGAP, Constants.VGAP, Constants.HGAP, Constants.VGAP)));
		this.textArea = tadcf.createTextArea(this.text, this);
		this.textArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.isControlDown() && (System.getProperty("corent.gui.TextAreaDialog.ctrl") != null)) {
					if (e.getKeyCode() == KeyEvent.VK_B) {
						int pos = textArea.getCaretPosition();
						if (System.getProperty("corent.gui.TextAreaDialog.ctrl").equalsIgnoreCase("HTML")) {
							textArea.setText(StrUtil.InsertStr(textArea.getText(), "<B></B>", pos));
							textArea.setCaretPosition(pos + 3);
						} else if (System.getProperty("corent.gui.TextAreaDialog.ctrl").equalsIgnoreCase("$HTML")) {
							textArea.setText(StrUtil.InsertStr(textArea.getText(), "$<B$>$</B$>", pos));
							textArea.setCaretPosition(pos + 5);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_I) {
						int pos = textArea.getCaretPosition();
						if (System.getProperty("corent.gui.TextAreaDialog.ctrl").equalsIgnoreCase("HTML")) {
							textArea.setText(StrUtil.InsertStr(textArea.getText(), "<I></I>", pos));
							textArea.setCaretPosition(pos + 3);
						} else if (System.getProperty("corent.gui.TextAreaDialog.ctrl").equalsIgnoreCase("$HTML")) {
							textArea.setText(StrUtil.InsertStr(textArea.getText(), "$<I$>$</I$>", pos));
							textArea.setCaretPosition(pos + 5);
						}
					} else if (e.getKeyCode() == KeyEvent.VK_U) {
						int pos = textArea.getCaretPosition();
						if (System.getProperty("corent.gui.TextAreaDialog.ctrl").equalsIgnoreCase("HTML")) {
							textArea.setText(StrUtil.InsertStr(textArea.getText(), "<U></U>", pos));
							textArea.setCaretPosition(pos + 3);
						} else if (System.getProperty("corent.gui.TextAreaDialog.ctrl").equalsIgnoreCase("$HTML")) {
							textArea.setText(StrUtil.InsertStr(textArea.getText(), "$<U$>$</U$>", pos));
							textArea.setCaretPosition(pos + 5);
						}
					}
				}
			}
		});
		if (keyListener != null) {
			this.textArea.addKeyListener(keyListener);
		}
		this.textArea.setEnabled(enabled);
		panelAnzeige.add(new JScrollPane(textArea));
		panel.add(panelAnzeige, BorderLayout.CENTER);
		panel.add(panelButtons, BorderLayout.SOUTH);
		this.setContentPane(panel);
		this.pack();
		if (ini == null) {
			Rectangle r = new Rectangle(Toolkit.getDefaultToolkit().getScreenSize());
			this.setBounds((r.width / 2) - (this.getWidth() / 2) + r.x, (r.height / 2) - (this.getHeight() / 2) + r.y,
					this.getWidth(), this.getHeight());
		}
		if (System.getProperty("corent.resource.type", "none").equalsIgnoreCase("properties")) {
			PropertyRessourceManager prm = new PropertyRessourceManager();
			String cn = COUtil.GetFullContext(this);
			String t = prm.getTitle(cn);
			if ((t != null) && (t.length() > 0)) {
				log.info("title for -> " + cn);
				this.setTitle(t);
			}
		}
		this.setVisible(true);
	}

	private void doClose() {
		this.setVisible(false);
		this.dispose();
	}

	/** @return Der editierte Text als Ergebnis des Editordialoges. */
	public String getText() {
		return this.text;
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Ok-Button dr&uuml;ckt.
	 */
	public void doButtonOk() {
		this.text = this.textArea.getText();
		this.doClose();
	}

	/**
	 * Diese Methode wird aufgerufen, wenn der Benutzer den Verwerfen-Button
	 * dr&uuml;ckt.
	 */
	public void doButtonVerwerfen() {
		this.doClose();
	}

}
