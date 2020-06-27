/*
 * DateDialog.java
 *
 * 05.01.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;

import corent.base.ReleaseUtil;
import corent.base.Releaseable;
import corent.base.Utl;
import corent.dates.Feiertag;
import corent.dates.PDate;
import corent.dates.Wochentag;
import corent.event.DateListener;

/**
 * Dieser Dialog geht auf eine &auml;ltere Version zur&uuml;ck, die bereits die
 * gleiche Funktionalit&auml;t allerdings auf die PDate-Klasse gem&uuml;nzt ist,
 * die aus dem jim-Projekt ihren Weg in die CoreLib gefunden hat.
 *
 * <P>
 * Der Dialog kann &uuml;ber den Aufruf der main-Methode der Klasse getestet
 * werden.
 *
 * <HR SIZE=3>
 * <H3>Properties</H3>
 * <HR>
 * <TABLE BORDER=1>
 * <TR VALIGN=TOP>
 * <TH>Property</TH>
 * <TH>Default</TH>
 * <TH>Typ</TH>
 * <TH>Beschreibung</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.cancel.icon</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Name der Grafikdatei in der das Icon des Abbruchbuttons hinterlegt
 * ist.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.cancel.text</TD>
 * <TD>Abbruch</TD>
 * <TD>String</TD>
 * <TD>Die Beschriftung des Abbruchbuttons des DateDialoges.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.cancel.tooltiptext</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Der Inhalt des Tooltiptextes zum Abbruchbutton des DateDialoges.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.clear.icon</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Name der Grafikdatei in der das Icon des L&ouml;schenbuttons hinterlegt
 * ist.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.clear.text</TD>
 * <TD>C</TD>
 * <TD>String</TD>
 * <TD>Die Beschriftung des L&ouml;schenbuttons des DateDialoges.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.clear.tooltiptext</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Der Inhalt des Tooltiptextes zum L&ouml;schenbutton des
 * DateDialoges.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.dec.icon</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Name der Grafikdatei in der das Icon des Verringernbuttons zu
 * Jahreseingabe hinterlegt ist.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.dec.tooltiptext</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Ein ToolTipText zum Verringernbuttons des Jahreseingabefeldes.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.inc.icon</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Name der Grafikdatei in der das Icon des Erh&ouml;henbuttons zu
 * Jahreseingabe hinterlegt ist.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.inc.tooltiptext</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Ein ToolTipText zum Erh&ouml;henbuttons des Jahreseingabefeldes.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.ok.icon</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Name der Grafikdatei in der das Icon des Ok-Buttons hinterlegt ist.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.ok.text</TD>
 * <TD>Ok</TD>
 * <TD>String</TD>
 * <TD>Die Beschriftung des Ok-Buttons des DateDialoges.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.button.ok.tooltiptext</TD>
 * <TD></TD>
 * <TD>String</TD>
 * <TD>Der Inhalt des Tooltiptextes zum Ok-Buttons des DateDialoges.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.calendarweek</TD>
 * <TD>KW</TD>
 * <TD>String</TD>
 * <TD>Der Label zur Darstellung vor der Kalenderwochennummer.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.day.[n]</TD>
 * <TD>Deutsche Tagesnamen</TD>
 * <TD>String</TD>
 * <TD>Die Tagesbezeichnungen zum Tag n. Die Z&auml;hlung der Tage beginnt mit
 * ?.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.label.month</TD>
 * <TD>Monat:</TD>
 * <TD>String</TD>
 * <TD>Die Beschriftung des Labels vor der Monatsauswahlbox des
 * DateDialoges.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.label.year</TD>
 * <TD>Jahr:</TD>
 * <TD>String</TD>
 * <TD>Die Beschriftung des Labels vor dem Jahreseingabefeld des
 * DateDialoges.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.month.[n]</TD>
 * <TD>Deutsche Monatsnamen</TD>
 * <TD>String</TD>
 * <TD>Die Monatsbezeichnungen zum Monat n. Die Z&auml;hlung der Monate beginnt
 * mit 1.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.gui.DateDialog.textfield.year.disabled</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Wenn diese Property gesetzt wird, wird das Eingabefeld f&uuml;r die
 * Jahreszahlen abgeblendet. Die Jahreszahl kann dann nur noch mit den
 * nebenstehenden Buttons manipuliert werden.</TD>
 * </TR>
 * </TABLE>
 * <P>
 * nbsp;
 *
 * @author O.Lieshoff
 *
 * @changed OLI 23.11.2007 - Einbau einer M&ouml;glichkeit, Labels etc.
 *          &uuml;ber Ressourcen zu definieren.
 * @changed OLI 02.04.2008 - Debugging des Jahresfeldproblems und Einbau einer
 *          Testfunktion &uuml;ber die <TT>main(String[])</TT>-Methode. Erstmal
 *          konnte das Problem nur durch Abblendung des Textfeldes beseitigt
 *          werden.
 * @changed OLI 19.12.2008 - Methoden
 *          <TT>addPDateDialogListener(DateListener)</TT> und
 *          <TT>removePDateDialogListener(DateListener)</TT> auf
 *          <TT>deprecated</TT> gesetzt.
 * @changed OLI 19.05.2010 - Einf&auml;rben von Feiertagen in gelber Farbe.
 *          Setzen eines Tooltiptextes mit der Bedeutung des Feiertages. Dabei:
 *          Formatanpassungen.
 */

public class DateDialog extends JDialog implements ActionListener, ItemListener, Releaseable {

	private boolean packed = false;
	/* Die Komponenten des Fensters. */
	private JComboBox comboBoxMonat = new JComboBox();
	/* Der Abbruch-Button <I>(Default new JButton("Abbruch"))</I>. */
	private JButton buttonAbbruch = new JButton(Utl.GetProperty("corent.gui.DateDialog.button.cancel.text", "Abbruch"));
	/* Der Clear-Button <I>(Default new JButton("C"))</I>. */
	private JButton buttonClear = new JButton(Utl.GetProperty("corent.gui.DateDialog.button.clear.text", "C"));
	private JButton buttonJahrInc = new JButton("+");
	private JButton buttonJahrDec = new JButton("-");
	/* Der Ok-Button <I>(Default new JButton("Ok"))</I>. */
	private JButton buttonOk = new JButton(Utl.GetProperty("corent.gui.DateDialog.button.ok.text", "Ok"));
	private JButton[] buttonTag = new JButton[31];
	private JLabel labelDatum = new JLabel("", SwingConstants.CENTER);
	private JLabel labelJahr = new JLabel(Utl.GetProperty("corent.gui.DateDialog.label.year", "Jahr:") + " ",
			SwingConstants.RIGHT);
	private JLabel labelMonat = new JLabel(Utl.GetProperty("corent.gui.DateDialog.label.month", "Monat:") + " ",
			SwingConstants.RIGHT);
	private JPanel panelCenter = new JPanel();
	private JPanel panelNorth = new JPanel();
	private JPanel panelSouth = new JPanel();
	private JTextField textFieldJahr = new JTextField("2000", 4);
	private PDate eingabedatum = new PDate();
	private PDate kopie = new PDate();
	private Vector pdatelistener = new Vector();

	/**
	 * Generiert einen DateDialog anhand der &uuml;bergebenen Parametern.
	 *
	 * @param title Der Titel des DateDialogs.
	 * @param d     Ein PDate als Voreinstellung f&uuml;r den Dialog.
	 */
	public DateDialog(String title, PDate d) {
		super();
		this.setTitle(title);
		this.setModal(true);
		this.construct(d);
	}

	/**
	 * Generiert einen DateDialog anhand der &uuml;bergebenen Parametern.
	 *
	 * @param owner Referenz auf den Parent-Dialog, zu dem sich der DateDialog modal
	 *              verh&auml;lt.
	 * @param title Der Titel des DateDialogs.
	 * @param d     Ein PDate als Voreinstellung f&uuml;r den Dialog.
	 */
	public DateDialog(Dialog owner, String title, PDate d) {
		super(owner, title, true);
		this.construct(d);
	}

	/**
	 * Generiert einen DateDialog anhand der &uuml;bergebenen Parametern.
	 *
	 * @param owner Referenz auf den Parent-Frame, zu dem sich der DateDialog modal
	 *              verh&auml;lt.
	 * @param title Der Titel des DateDialogs.
	 * @param d     Ein PDate als Voreinstellung f&uuml;r den Dialog.
	 */
	public DateDialog(Frame owner, String title, PDate d) {
		super(owner, title, true);
		this.construct(d);
	}

	/**
	 * Diese Methode montiert das Hauptpanel des Fensters zusammen und f&uuml;gt es
	 * als ContentPane in das Fenster ein.
	 *
	 * @param d Ein PDate als Voreinstellung f&uuml;r den Dialog.
	 */
	public void construct(PDate d) {
		if (Boolean.getBoolean("corent.gui.DateDialog.textfield.year.disabled")) {
			this.textFieldJahr.setEnabled(false);
			this.textFieldJahr.setForeground(Color.black);
			this.textFieldJahr.setBackground(Color.white);
		}
		this.textFieldJahr.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				try {
					int jahr = Integer.parseInt(textFieldJahr.getText());
					eingabedatum = eingabedatum.setJahr(jahr);
					if (isVisible()) {
						update(false);
					}
				} catch (NumberFormatException nfe) {
					textFieldJahr.setText(new Integer(eingabedatum.getJahr()).toString());
				}
			}
		});
		this.textFieldJahr.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					try {
						int jahr = Integer.parseInt(textFieldJahr.getText());
						eingabedatum = eingabedatum.setJahr(jahr);
						if (isVisible()) {
							update(false);
						}
					} catch (NumberFormatException nfe) {
						textFieldJahr.setText(new Integer(eingabedatum.getJahr()).toString());
					}
				}
			}
		});
		PropertyRessourceManager prm = new PropertyRessourceManager();
		buttonAbbruch.setIcon(prm.getIcon("corent.gui.DateDialog.button.cancel"));
		buttonAbbruch.setToolTipText(prm.getToolTipText("corent.gui.DateDialog.button.cancel"));
		buttonClear.setIcon(prm.getIcon("corent.gui.DateDialog.button.clear"));
		buttonClear.setToolTipText(prm.getToolTipText("corent.gui.DateDialog.button.clear"));
		buttonJahrDec.setIcon(prm.getIcon("corent.gui.DateDialog.button.dec"));
		buttonJahrDec.setToolTipText(prm.getToolTipText("corent.gui.DateDialog.button.dec"));
		buttonJahrInc.setIcon(prm.getIcon("corent.gui.DateDialog.button.inc"));
		buttonJahrInc.setToolTipText(prm.getToolTipText("corent.gui.DateDialog.button.inc"));
		buttonOk.setIcon(prm.getIcon("corent.gui.DateDialog.button.ok"));
		buttonOk.setToolTipText(prm.getToolTipText("corent.gui.DateDialog.button.ok"));
		if ((d == null) || (d.toInt() < 1)) {
			d = new PDate();
		}
		this.eingabedatum = new PDate(d);
		this.kopie = new PDate(this.eingabedatum);
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.1", "Januar"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.2", "Februar"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.3", "M&auml;rz"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.4", "April"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.5", "Mai"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.6", "Juni"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.7", "Juli"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.8", "August"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.9", "September"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.10", "Oktober"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.11", "November"));
		this.comboBoxMonat.addItem(Utl.GetProperty("corent.gui.DateDialog.month.12", "Dezember"));
		this.comboBoxMonat.setSelectedIndex(this.eingabedatum.getMonat() - 1);
		this.comboBoxMonat.addItemListener(this);
		this.buttonAbbruch.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doButtonAbbruch();
			}
		});
		this.buttonClear.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doButtonClear();
			}
		});
		this.buttonOk.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				doButtonOk();
			}
		});
		JPanel pane = new JPanel();
		pane.setLayout(new BorderLayout());
		this.panelNorth.setLayout(new GridLayout(1, 2, 50, 2));
		JPanel panelMonat = new JPanel(new GridLayout(1, 2, 2, 2));
		panelMonat.add(this.labelMonat);
		panelMonat.add(this.comboBoxMonat);
		this.panelNorth.add(panelMonat);
		JPanel panelRightSideOfLife = new JPanel(new GridLayout(1, 3, 2, 2));
		panelRightSideOfLife.add(this.labelJahr);
		panelRightSideOfLife.add(this.textFieldJahr);
		JPanel panelButtonsPlusMinus = new JPanel(new GridLayout(1, 2, 2, 2));
		panelButtonsPlusMinus.add(this.buttonJahrDec);
		panelButtonsPlusMinus.add(this.buttonJahrInc);
		panelRightSideOfLife.add(panelButtonsPlusMinus);
		this.panelNorth.add(panelRightSideOfLife);
		// this.textFieldJahr.addFocusListener(this);
		this.buttonJahrInc.addActionListener(this);
		this.buttonJahrDec.addActionListener(this);
		Dimension buttonDim = new Dimension(20, 10);
		this.buttonJahrDec.setMaximumSize(buttonDim);
		this.buttonJahrInc.setMaximumSize(buttonDim);
		this.buttonJahrDec.setSize(buttonDim);
		this.buttonJahrInc.setSize(buttonDim);
		for (int i = 1; i <= 31; i++) {
			this.buttonTag[i - 1] = new JButton((new Integer(i)).toString());
			this.buttonTag[i - 1].addActionListener(this);
			this.buttonTag[i - 1].setBackground(Color.lightGray);
		}
		this.panelSouth.setLayout(new GridLayout(1, 3));
		this.panelSouth.add(new JLabel(""));
		this.panelSouth.add(this.labelDatum);
		this.panelSouth.add(new JLabel(""));
		pane.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(5, 5, 5, 5)));
		this.panelNorth
				.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(5, 5, 5, 5)));
		this.panelCenter
				.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(5, 5, 5, 5)));
		this.panelSouth
				.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(5, 5, 5, 5)));
		pane.add(this.panelNorth, BorderLayout.NORTH);
		pane.add(this.panelCenter, BorderLayout.CENTER);
		pane.add(this.panelSouth, BorderLayout.SOUTH);
		JPanel panelButtons = new JPanel(new GridLayout(1, 5, 5, 5));
		panelButtons.setBorder(new CompoundBorder(new EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(5, 5, 5, 5)));
		panelButtons.add(this.buttonClear);
		panelButtons.add(new JLabel(""));
		panelButtons.add(new JLabel(""));
		panelButtons.add(this.buttonOk);
		panelButtons.add(this.buttonAbbruch);
		JPanel panel = new JPanel(new BorderLayout(5, 5));
		panel.setBorder(new EmptyBorder(5, 5, 5, 5));
		panel.add(pane, BorderLayout.CENTER);
		panel.add(panelButtons, BorderLayout.SOUTH);
		this.setContentPane(panel);
		this.setVisible(true);
	}

	private void updateDatum() {
		/*
		 * this.labelDatum.setText(this.eingabedatum.toString() + " - " +
		 * this.eingabedatum.getTagesnameDeutsch());
		 */
		int i = this.eingabedatum.getWochentag().ord() + 1;
		this.labelDatum.setText(this.eingabedatum.toString() + " - "
				+ Utl.GetProperty("corent.gui.DateDialog.day." + i, this.eingabedatum.getTagesnameDeutsch()));
	}

	private void update() {
		this.update(true);
	}

	private void update(boolean inclCombo) {
		if (this.panelCenter != null) {
			this.remove(this.panelCenter);
			this.panelCenter.removeAll();
			JPanel pane = this.panelCenter;// new JPanel();
			pane.setLayout(new GridLayout(7, 8));
			pane.add(new JLabel(Utl.GetProperty("corent.gui.DateDialog.calendarweek", "KW")));
			PDate d = null;
			if (eingabedatum == null) {
				d = new PDate();
			} else {
				d = (new PDate(eingabedatum)).monatsErster();
			}
			int monat = d.getMonat();
			int jahr = d.getJahr();
			while (d.getWochentag() != Wochentag.MONTAG) {
				d = d.vorherigerTag();
			}
			PDate dummy = d;
			for (int i = 1; i <= 7; i++) {
				pane.add(new JLabel(
						Utl.GetProperty("corent.gui.DateDialog.day." + i, d.getTagesnameDeutsch()).substring(0,
								Integer.getInteger("corent.gui.DateDialog.day.token.length", 2)),
						SwingConstants.CENTER));
				d = d.naechsterTag();
			}
			d = dummy;
			for (int i = 0; i < 42; i++) {
				if ((d.getWochentag() == Wochentag.MONTAG)
						&& (d.toInt() <= (new PDate(1, monat, jahr)).monatsLetzter().toInt())) {
					pane.add(new JLabel(new Integer((new PDate(d)).getWoche()).toString()));
				}
				if (d.getMonat() == monat) {
					pane.add(this.buttonTag[d.getTag() - 1]);
					if (d.getTag() == eingabedatum.getTag()) {
						this.buttonTag[d.getTag() - 1].setBackground(Color.red);
						this.buttonTag[d.getTag() - 1].setToolTipText(null);
					} else if (Feiertag.IsFeiertag(d) != null) {
						this.buttonTag[d.getTag() - 1].setBackground(Color.yellow);
						this.buttonTag[d.getTag() - 1].setToolTipText(Feiertag.IsFeiertag(d));
					} else {
						this.buttonTag[d.getTag() - 1].setBackground(Color.lightGray);
						this.buttonTag[d.getTag() - 1].setToolTipText(null);
					}
					d = d.naechsterTag();
				} else {
					pane.add(new JLabel());
					d = d.naechsterTag();
				}
			}
			if (inclCombo) {
				this.comboBoxMonat.setSelectedIndex(this.eingabedatum.getMonat() - 1);
			}
			this.textFieldJahr.setText(new Integer(this.eingabedatum.getJahr()).toString());
			this.updateDatum();
			/*
			 * this.panelCenter = pane; this.panelCenter.setBorder(new CompoundBorder(new
			 * EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(5, 5, 5, 5))); /* pane =
			 * new JPanel(new BorderLayout(5, 5)); pane.setBorder(new CompoundBorder(new
			 * EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(5, 5, 5, 5)));
			 * pane.add(this.panelNorth, BorderLayout.NORTH); pane.add(this.panelCenter,
			 * BorderLayout.CENTER); pane.add(this.panelSouth, BorderLayout.SOUTH); JPanel
			 * panelButtons = new JPanel(new GridLayout(1, 5, 5, 5));
			 * panelButtons.setBorder(new CompoundBorder(new
			 * EtchedBorder(EtchedBorder.RAISED), new EmptyBorder(5, 5, 5, 5)));
			 * panelButtons.add(new JLabel("")); panelButtons.add(new JLabel(""));
			 * panelButtons.add(new JLabel("")); panelButtons.add(buttonOk);
			 * panelButtons.add(buttonAbbruch); JPanel panel = new JPanel(new
			 * BorderLayout(5, 5)); panel.setBorder(new EmptyBorder(5, 5, 5, 5));
			 * panel.add(pane, BorderLayout.CENTER); panel.add(panelButtons,
			 * BorderLayout.SOUTH); this.setContentPane(panel);
			 */
			if (!packed) {
				packed = true;
				this.pack();
				Dimension dtd = Toolkit.getDefaultToolkit().getScreenSize();
				this.setBounds(((int) dtd.getWidth() / 2) - (this.getWidth() / 2),
						((int) dtd.getHeight() / 2) - (this.getHeight() / 2), this.getWidth(), this.getHeight());
			}
		}
	}

	/**
	 * Setzt das &uuml;bergebene PDate-Objekt in den DateDialog ein.
	 *
	 * @param p Das PDate, das an den DateDialog &uuml;bergeben und angezeigt werden
	 *          soll.
	 */
	public void setDate(PDate p) {
		this.buttonTag[this.eingabedatum.getTag() - 1].setBackground(Color.lightGray);
		this.eingabedatum = new PDate(p);
		this.kopie = new PDate(p);
		this.update();
	}

	/**
	 * Liefert eine Referenz auf das PDate-Objekt, das in dem DateDialog angezeigt
	 * werden soll.
	 *
	 * @return Referenz auf das im DateDialog angezeigte PDate-Objekt.
	 */
	public PDate getDate() {
		if (this.kopie == null) {
			return null;
		}
		return (new PDate(this.kopie));
	}

	/**
	 * F&uuml;gt den angegebenen DateListener an die Liste der an dem DateDialog
	 * horchenden Listener an.
	 *
	 * @param l Der DateListener, der an die Liste der an dem DateDialog horchenden
	 *          DateListener angef&uuml;gt werden soll.
	 *
	 * @deprecated OLI 19.12.2008 - Da der Dialog (gewollt) modal ist, kann der
	 *             Listener nicht rechtzeitig eingeh&auml;ngt werden, weil der
	 *             Methodenaufruf erst nach dem Beenden des Dialoges von au&szlig;en
	 *             erfolgen kann. Das ist also Nonsens ...
	 *             <P>
	 */
	@Deprecated
	public void addPDateDialogListener(DateListener l) {
		this.pdatelistener.addElement(l);
	}

	/**
	 * L&ouml;scht den angegebenen DateListener aus der Liste der an dem DateDialog
	 * horchenden Listener.
	 *
	 * @param l Der DateListener, der aus der Liste der an dem DateDialog horchenden
	 *          DateListener entfernt werden soll.
	 *
	 * @deprecated OLI 19.12.2008 - Die Herausnahme der Methode begr&uuml;ndet sich
	 *             aus dem geplanten Wegfall der Methode
	 *             <TT>addPDateDialogListener(DateListener)</TT>.
	 *             <P>
	 */
	@Deprecated
	public void removePDateDialogListener(DateListener l) {
		this.pdatelistener.removeElement(l);
	}

	/**
	 * Diese Methode wird ausgef&uuml;hrt, wenn der Benutzer der Clear-Button
	 * dr&uuml;ckt.
	 */
	public void doButtonClear() {
		this.eingabedatum = null;
		this.doButtonOk();
	}

	/* Implementierung der abstrakten Methoden der Superklasse. */

	public void doButtonAbbruch() {
		this.kopie = null;
		this.doClose();
	}

	public void doButtonOk() {
		this.kopie = (this.eingabedatum != null ? new PDate(this.eingabedatum) : null);
		for (int i = 0; i < this.pdatelistener.size(); i++) {
			DateListener l = (DateListener) this.pdatelistener.elementAt(i);
			l.dateChanged(this.kopie);
		}
		this.doClose();
	}

	public void doClose() {
		this.release();
		this.setVisible(false);
		this.dispose();
	}

	@Override
	public void setVisible(boolean b) {
		if (b) {
			this.update();
		}
		super.setVisible(b);
	}

	/* Implementierung des Interfaces ActionListener. */

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == this.buttonJahrInc) {
			int lastday = (new PDate(1, this.comboBoxMonat.getSelectedIndex() + 1, this.eingabedatum.getJahr() + 1))
					.monatsLetzter().getTag();
			if (lastday < this.eingabedatum.getTag()) {
				this.eingabedatum = this.eingabedatum.setTag(lastday);
			}
			this.eingabedatum = this.eingabedatum.setJahr(this.eingabedatum.getJahr() + 1);
			this.eingabedatum = this.eingabedatum.setMonat(this.comboBoxMonat.getSelectedIndex() + 1);
			this.update();
			return;
		} else if (e.getSource() == this.buttonJahrDec) {
			int lastday = (new PDate(1, this.comboBoxMonat.getSelectedIndex() + 1, this.eingabedatum.getJahr() - 1))
					.monatsLetzter().getTag();
			if (lastday < this.eingabedatum.getTag()) {
				this.eingabedatum = this.eingabedatum.setTag(lastday);
			}
			this.eingabedatum = this.eingabedatum.setJahr(this.eingabedatum.getJahr() - 1);
			this.eingabedatum = this.eingabedatum.setMonat(this.comboBoxMonat.getSelectedIndex() + 1);
			this.update();
			return;
		} else {
			for (int i = 0; i <= 31; i++) {
				if (e.getSource() == this.buttonTag[i]) {
					this.buttonTag[this.eingabedatum.getTag() - 1].setBackground(Color.lightGray);
					this.eingabedatum = this.eingabedatum.setTag(i + 1);
					this.buttonTag[i].setBackground(Color.red);
					this.updateDatum();
					return;
				}
			}
		}
	}

	/* Implementierung des Interfaces ItemListener. */

	@Override
	public void itemStateChanged(ItemEvent e) {
		if (e.getSource() == this.comboBoxMonat) {
			int lastday = (new PDate(1, this.comboBoxMonat.getSelectedIndex() + 1, this.eingabedatum.getJahr()))
					.monatsLetzter().getTag();
			if (lastday < this.eingabedatum.getTag()) {
				this.eingabedatum = this.eingabedatum.setTag(lastday);
			}
			this.eingabedatum = this.eingabedatum.setMonat(this.comboBoxMonat.getSelectedIndex() + 1);
			if (this.isVisible()) {
				this.update(false);
			}
		}
	}

	/* Implementierung des Interfaces Releaseable. */

	@Override
	public void release() {
		ReleaseUtil.ReleaseContainer(this.getContentPane());
	}

}