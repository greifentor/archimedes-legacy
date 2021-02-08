/*
 * DefaultTimestampFieldFactory.java
 *
 * 14.04.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.gui;

import java.awt.Font;
import java.lang.reflect.Constructor;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import corent.base.Utl;
import corent.dates.PTime;
import corent.dates.PTimestamp;
import corent.dates.TimestampModel;
import corent.files.Inifile;
import logging.Logger;

/**
 * Diese Factory bietet Standardkomponenten zur Benutzung in einem
 * TimestampField.
 * <P>
 * Mit Hilfe der Property
 * <I>corent.gui.DefaultTimestampFieldComponentFactory.alternateClass </I> kann
 * eine alternative Komponente zur Eingabe der Uhrzeit festgelegt werden. Es
 * mu&szlig; sich um einen qualifizierten Klassennamen handeln. Die angegebene
 * Klasse mu&szlig; das Interface TimeInputDialog implementieren.
 *
 * @author O.Lieshoff
 *         <P>
 *
 * @changed OLI 02.12.2007 - Erweiterung um die Konfigurierbarkeit der
 *          Komponenten.
 *          <P>
 *          OLI 24.02.2009 - Erweiterung um die Methoden zur Erstellung der
 *          Buttons zum Verschieben des Tagesdatums um einen Tag nach vorn bzw.
 *          hinten.
 *          <P>
 * 
 */

public class DefaultTimestampFieldComponentFactory implements TimestampFieldComponentFactory {

	private static final Logger log = Logger.getLogger(DefaultTimestampFieldComponentFactory.class);

	/** Statische Instanz zur projektweiten Verwendung. */
	public static final DefaultTimestampFieldComponentFactory INSTANCE = new DefaultTimestampFieldComponentFactory();

	/* Die Flagge f&uuml;r die Anzeige des Datums. */
	private boolean dateEnabled = true;
	/* Die Flagge f&uuml;r die Anzeige der Uhrzeit. */
	private boolean timeEnabled = true;
	/* Die Inidatei zur Weitergabe an die Dialoge. */
	private Inifile ini = null;
	/*
	 * Der RessourcenManager, anhand dessen die Komponenten generiert werden sollen.
	 */
	private PropertyRessourceManager prm = new PropertyRessourceManager();

	/** Generiert eine Instanz mit Defaultwerten. */
	public DefaultTimestampFieldComponentFactory() {
		super();
	}

	/**
	 * Generiert eine Instanz anhand der &uuml;bergebenen Parameter.
	 *
	 * @param dateEnabled Diese Flagge mu&szlig; gel&ouml;scht werden, wenn die
	 *                    Anzeige und Editierbarkeit des Datums des Timestamps
	 *                    unterbunden werden soll.
	 * @param timeEnabled Diese Flagge mu&szlig; gel&ouml;scht werden, wenn die
	 *                    Anzeige und Editierbarkeit der Uhrzeit des Timestamps
	 *                    unterbunden werden soll.
	 * @param ini         Die Inidatei zur Weitergabe an die Dialoge zur Zeit- und
	 *                    Datumseingabe.
	 */
	public DefaultTimestampFieldComponentFactory(boolean dateEnabled, boolean timeEnabled, Inifile ini) {
		super();
		this.dateEnabled = dateEnabled;
		this.ini = ini;
		this.timeEnabled = timeEnabled;
	}

	/* Implementierung des Interfaces TimestampFieldComponentFactory. */

	@Override
	public JLabel createLabel() {
		return new JLabel();
	}

	@Override
	public char getMnemonic() {
		return '\0';
	}

	@Override
	public JTextField createTextField() {
		JTextField tf = new JTextField("", 6);
		tf.setFont(new Font("monospaced", Font.PLAIN, 12));
		return tf;
	}

	/**
	 * @changed OLI 02.12.2007 - Erweiterung um die Konfigurierbarkeit des
	 *          Datumsbuttons.
	 *          <P>
	 *
	 */
	@Override
	public JButton createButtonDatum() {
		COButton button = new COButton("Datum", "corent.gui.DefaultTimestampFieldComponentFactory.button.date");
		button.update(this.prm);
		return button;
	}

	/**
	 * @changed OLI 24.02.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	@Override
	public JButton createButtonTagMinus() {
		COButton button = new COButton("", "corent.gui.DefaultTimestampFieldComponentFactory.button.day.minus");
		button.update(this.prm);
		return button;
	}

	/**
	 * @changed OLI 24.02.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	@Override
	public JButton createButtonTagPlus() {
		COButton button = new COButton("", "corent.gui.DefaultTimestampFieldComponentFactory.button.day.plus");
		button.update(this.prm);
		return button;
	}

	/**
	 * @changed OLI 02.12.2007 - Erweiterung um die Konfigurierbarkeit des
	 *          Uhrzeitbuttons.
	 *          <P>
	 *
	 */
	@Override
	public JButton createButtonUhrzeit() {
		COButton button = new COButton("Uhrzeit", "corent.gui.DefaultTimestampFieldComponentFactory.button.time");
		button.update(this.prm);
		return button;
	}

	/**
	 * @changed OLI 02.12.2007 - Erweiterung um die Konfigurierbarkeit des
	 *          Clearbuttons.
	 *          <P>
	 *
	 */
	@Override
	public JButton createButtonClear() {
		COButton button = new COButton("C", "corent.gui.DefaultTimestampFieldComponentFactory.button.clear");
		button.update(this.prm);
		return button;
	}

	@Override
	public TimeInputDialog createTimeInputDialog(TimestampModel pt) {
		String cn = System.getProperty("corent.gui.DefaultTimestampFieldComponentFactory.alternateClass");
		if (cn != null) {
			try {
				Class cls = Class.forName(cn);
				Constructor cst = cls.getDeclaredConstructor(new String().getClass(), new PTime().getClass(),
						new Inifile("").getClass());
				PTime ptm = new PTime(
						(int) (((pt == PTimestamp.NULL ? new PTimestamp() : (PTimestamp) pt)).toLong() % 1000000));
				return (TimeInputDialog) cst.newInstance(
						Utl.GetProperty("corent.gui.DefaultTimestampFieldComponentFactory.timedialog.title",
								"Zeit &Auml;ndern"),
						ptm, this.ini);
			} catch (Exception e) {
				log.error("cannot create time dialog from class: " + cn);
				log.error("maybe constructor(String, PTime, Inifile) is missing.");
				log.error("using standard class TimeDialog ...", e);
			}
		}
		return new TimeDialog(
				Utl.GetProperty("corent.gui.DefaultTimestampFieldComponentFactory.timedialog.title",
						"Zeit &auml;ndern"),
				this.ini, (pt == PTimestamp.NULL ? new PTimestamp() : (PTimestamp) pt),
				DefaultTimeDialogComponentFactory.INSTANCE);
		/*
		 * return new corelib.basics.gui.TimeDialogCorent(new Frame(), "Zeit ändern",
		 * (pt == PTimestamp.NULL ? new PTime() : new PTime((int) ((PTimestamp)
		 * pt).toLong() % 100000)), this.ini);
		 */
	}

	@Override
	public boolean isTimeEnabled() {
		return this.timeEnabled;
	}

	@Override
	public boolean isDateEnabled() {
		return this.dateEnabled;
	}
}
