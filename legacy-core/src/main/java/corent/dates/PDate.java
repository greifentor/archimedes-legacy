/*
 * PDate.java
 *
 * 02.12.2003
 *
 * (c) by O.Lieshoff
 *
 */

package corent.dates;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

import corent.base.Utl;
import logging.Logger;

/**
 * Diese Klasse ist aus der urspr&uuml;nglich im Rahmen diverser privater
 * Projekte entwickelten Klasse PDate hervorgegangen, die letztendlich im
 * corelib-Package gelandet ist. Diese Klasse weist jedoch einige Probleme in
 * bezug auf Sicherheit und Zuverl&auml;ssigkeit auf, soda&szlig; an dieser
 * Stelle neu implementiert worden ist. Die Urspr&uuml;nge der Klasse liegen in
 * einem Modula-2-Modul namens PackedDate, das im Rahmen Zahlreicher Mickey-D's
 * Programme seinen Dienst tat und tut.
 * <P>
 * Die Klasse repr&auml;sentiert ein Datum im Integer-Format (JJJJMMTT). Dies
 * ist zwar keine unbedingt nat&uuml;rliche Datumsdarstellung, hat jedoch einen
 * Wust an Vorteilen, der diese Art der Repr&auml;sentation rechtfertigt.<BR>
 * <P>
 * Die Methoden der Klasse sind stellenweise auf hohe Fehlertoleranz angelegt.
 * So errechnet das Verschieben von Daten um ganze Monate immer richtige
 * Datum-Werte, die aber eventuell nicht den Erwartungen entsprechen
 * k&ouml;nnen. Beispiel:
 * 
 * <PRE>
 *  
 * monthBefore(1) vom 31.03.2003 -> 28.02.2003.
 * </PRE>
 * 
 * <P>
 * &Uuml;ber die Property <TT>corent.dates.debug</TT> l&auml;&szlig;t sich an
 * einigen Stellen eine zus&auml;tzliche Debug-Ausgabe auf die Konsole
 * einschalten.
 * <P>
 * Mit Hilfe der Properties <TT>corent.dates.PDate.undefined.string.regular</TT>
 * und <TT>corent.dates.PDate.undefined.string.short</TT> k&ouml;nnen ein
 * alternative String zur Ausgabe eines undefinierten PDates in der
 * regul&auml;ren und der kurzen Schreibweise angegeben werden. Die Defaultwerte
 * sind "XX.XX.XXXX" und "XX.XX.XX".
 *
 * <P>
 * Mit Hilfe der Properties <I>corent.dates.debug</I> und <I>corent.debug</I>
 * k&ouml;nnen zus&auml;tzliche Debuginformationen auf der Konsole ausgegeben
 * werden.
 *
 * @author O.Lieshoff
 *         <P>
 *
 * @changed OLI 14.11.2008 - Korrektur des Konstruktors
 *          <TT>PDate(java.util.Date)</TT>. Erweiterung um zuschaltbare
 *          Debugausgaben in diesem Konstruktor.
 *          <P>
 *          OLI 16.02.2009 - Erste Schritte zur Umstellung auf ein
 *          englischsprachiges zur Klasse.
 *          <P>
 *          OLI 14.04.2009 - Debugging an der Methode
 *          <TT>GetMontagVonWoche(int, int)</TT>.
 *          <P>
 *
 */

public class PDate implements Serializable {

	private static final Logger log = Logger.getLogger(PDate.class);

	/** Das undefinierte Datum als Bezeichner. */
	public static final PDate UNDEFINIERT = new PDate(false);

	/* Hier wird der Wert des PDates gespeichert. */
	private int d = -1;

	private PDate(boolean b) {
		super();
	}

	/** Erzeugt ein PDate mit dem aktuellen Datum der ausf&uuml;hrenden Maschine. */
	public PDate() {
		super();
		Calendar dt = Calendar.getInstance();
		d = (dt.get(Calendar.YEAR) * 10000) + ((dt.get(Calendar.MONTH) + 1) * 100) + dt.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * Erzeugt ein PDate aus dem &uuml;bergebenen Integer-Wert, falls dieser ein
	 * g&uuml;ltiges PDate enth&auml;t.
	 *
	 * @param pd Ein int-Wert im PDate-Format.
	 * @throws DateFormatException wenn der int-Wert kein g&uuml;ltiges Datum
	 *                             enth&auml;lt.
	 */
	public PDate(int pd) throws DateFormatException {
		this(pd % 100, (pd / 100) % 100, pd / 10000);
	}

	/**
	 * Erzeugt ein PDate als Kopie des &uuml;bergebenen PDates.
	 *
	 * @param pd Das zu kopierende PDate.
	 * @throws DateFormatException wenn der Inhalt des &uuml;bergebenen PDates
	 *                             fehlerhaft ist.
	 */
	public PDate(PDate pd) {
		this(pd.getTag(), pd.getMonat(), pd.getJahr());
	}

	/**
	 * Erzeugt ein PDate aus den &uuml;bergebenen Parametern.
	 *
	 * @param tag   Der Tag des zu pr&uuml;fenden Datums.
	 * @param monat Der Monat des zu pr&uuml;fenden Datums.
	 * @param jahr  Der Jahr des zu pr&uuml;fenden Datums.
	 */
	public PDate(int tag, int monat, int jahr) {
		super();
		if (CheckDate(tag, monat, jahr)) {
			this.d = jahr * 10000 + monat * 100 + tag;
		} else {
			throw new DateFormatException("date not valid: " + tag + "." + monat + "." + jahr);
		}
	}

	/**
	 * Erzeugt aus dem angegeben String ein PDate.
	 *
	 * @param s Der String, aus dem das PDate generiert werden soll.
	 * @throws DateFormatException wenn der Inhalt des &uuml;bergebenen String
	 *                             fehlerhaft ist.
	 */
	public static PDate valueOf(String s) throws DateFormatException {
		int a = 0;
		int jahr = 0;
		int monat = 0;
		int p = 0;
		int tag = 0;
		char c;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if ((c >= '0') && (c <= '9')) {
				a = a * 10;
				a += (c - '0');
			} else if (c == '.') {
				p++;
				switch (p) {
				case 1:
					tag = a;
					break;
				case 2:
					monat = a;
					break;
				case 3:
					jahr = a;
					break;
				default:
					throw new DateFormatException("to many arguments");
				}
				a = 0;
			} else {
				throw new DateFormatException("invalid digit: " + c);
			}
		}
		jahr = a;
		if (!CheckDate(tag, monat, jahr)) {
			throw new DateFormatException("date not valid: " + tag + "." + monat + "." + jahr);
		}
		return new PDate(tag, monat, jahr);
	}

	/*
	 * Checkt die &uuml;bergebenen Parameter auf Darstellung eines g&uuml;tigen Datums.
	 *
	 * @param tag Der Tag des zu pr&uuml;fenden Datums.
	 * 
	 * @param monat Der Monat des zu pr&uuml;fenden Datums.
	 * 
	 * @param jahr Der Jahr des zu pr&uuml;fenden Datums.
	 * 
	 * @return <TT>true</TT>, wenn die &uuml;bergebenen Parameter ein g&uuml;ltiges Datum ergeben,<BR> <TT>false</TT>
	 * sonst.
	 */
	private static boolean CheckDate(int tag, int monat, int jahr) {
		if (monat < 1) {
			return false;
		}
		if (monat > 12) {
			return false;
		}
		if (tag < 1) {
			return false;
		}
		switch (monat) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			if (tag > 31) {
				return false;
			}
			break;
		case 2:
			int ld = 28;
			if (jahr % 400 == 0) {
				ld++;
			} else if (jahr % 100 == 0) {
			} else if (jahr % 4 == 0) {
				ld++;
			}
			if (tag > ld) {
				return false;
			}
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			if (tag > 30) {
				return false;
			}
			break;
		}
		return true;
	}

	/**
	 * Wandelt ein PDate in einen int-Wert um.
	 *
	 * @return Der int-Wert (JJJJMMTT) zum PDate.
	 */
	public int toInt() {
		return d;
	}

	/** @return Der Tag des Datums (Ordnungszahl in bezug zum Monat). */
	public int getTag() {
		return d % 100;
	}

	/** @return Der Monat zum Datum (Ordnungszahl in bezug zum Jahr). */
	public int getMonat() {
		return (d % 10000) / 100;
	}

	/** @return Die Jahreszahl zum Datum. */
	public int getJahr() {
		return d / 10000;
	}

	/** @return Liefert ein neues Datum mit dem angegeben Tag. */
	public PDate setTag(int tag) {
		return new PDate(tag, this.getMonat(), this.getJahr());
	}

	/** @return Liefert ein neues Datum mit dem angegeben Monat. */
	public PDate setMonat(int monat) {
		return new PDate(this.getTag(), monat, this.getJahr());
	}

	/** @return Liefert ein neues Datum mit dem angegeben Jahr. */
	public PDate setJahr(int jahr) {
		return new PDate(this.getTag(), this.getMonat(), jahr);
	}

	/**
	 * @return <TT>true</TT>, wenn das angegebene Datum in einem Schaltjahr liegt.
	 */
	public boolean isSchaltjahr() {
		if (this.getJahr() % 400 == 0) {
			return true;
		} else if (this.getJahr() % 100 == 0) {
			return false;
		} else if (this.getJahr() % 4 == 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PDate)) {
			return false;
		}
		PDate p = (PDate) o;
		return (this.toInt() == p.toInt());
	}

	@Override
	public int hashCode() {
		return this.toInt();
	}

	@Override
	public String toString() {
		String s = "";
		if (this.toInt() == -1) {
			s = s + Utl.GetProperty("corent.dates.PDate.undefined.string.regular", "XX.XX.XXXX");
		} else {
			String h = "";
			h = new String("" + this.getTag());
			if (h.length() < 2) {
				s = "0";
			}
			s = s + h + ".";
			h = new String("" + this.getMonat());
			if (h.length() < 2) {
				s = s + "0";
			}
			s = s + h + ".";
			h = new String("" + this.getJahr());
			while (h.length() < 4) {
				h = "0" + h;
			}
			s = s + h;
		}
		return s;
	}

	/** @return Datum des Folgetages zum vorliegenden Datum. */
	public PDate naechsterTag() {
		int day = this.getTag() + 1;
		int month = this.getMonat();
		int year = this.getJahr();
		if (day > 28) {
			switch (month) {
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:
				if (day > 31) {
					month++;
					day = 1;
				}
				break;
			case 2:
				int ld = 28;
				if (year % 400 == 0) {
					ld++;
				} else if (year % 100 == 0) {
				} else if (year % 4 == 0) {
					ld++;
				}
				if (day > ld) {
					month++;
					day = 1;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				if (day > 30) {
					month++;
					day = 1;
				}
				break;
			default:
				// NOP
			}
			if (month > 12) {
				year++;
				month = 1;
				day = 1;
			}
		}
		return new PDate(day, month, year);
	}

	/**
	 * Ermittelt das Datum, das n Tage nach dem vorliegenden Datum liegt.
	 *
	 * @param n Anzahl der Tage.
	 * @return Datum das n Tage nach dem vorliegenden Datum liegt.
	 * @throws IllegalArgumentException wenn n kleiner als 0 ist.
	 */
	PDate naechsterTag(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
		}
		PDate next = new PDate(this);
		for (int i = 0; i < n; i++) {
			next = next.naechsterTag();
		}
		return next;
	}

	/** @return Das Datum des Vortages vom vorliegenden Datum. */
	public PDate vorherigerTag() {
		int day = this.getTag() - 1;
		int month = this.getMonat();
		int year = this.getJahr();
		if (day == 0) {
			month--;
			switch (month) {
			case 0:
				year--;
				month = 12;
				day = 31;
				break;
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
				day = 31;
				break;
			case 2:
				day = 28;
				if (year % 400 == 0) {
					day++;
				} else if (year % 100 == 0) {
				} else if (year % 4 == 0) {
					day++;
				}
				break;
			case 4:
			case 6:
			case 9:
			case 11:
				day = 30;
				break;
			default:
				// NOP
			}
		}
		return new PDate(day, month, year);
	}

	/**
	 * Ermittelt das Datum, das n Tage vor dem vorliegenden Datum liegt.
	 *
	 * @param n Anzahl der Tage.
	 * @return Das Datum, da&szlig; n Tage vor dem vorliegenden liegt.
	 * @throws IllegalArgumentException wenn n kleiner oder gleich null ist).
	 */
	public PDate vorherigerTag(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
		}
		PDate previous = new PDate(this);
		for (int i = 0; i < n; i++) {
			previous = previous.vorherigerTag();
		}
		return previous;
	}

	/** @return Das Datum des ersten Tages des Monats des vorliegenden Datums. */
	public PDate monatsErster() {
		return new PDate(1, this.getMonat(), this.getJahr());
	}

	/** @return Das Datum des letzten Tages des Monats des vorliegenden Datums. */
	public PDate monatsLetzter() {
		PDate d = new PDate(28, this.getMonat(), this.getJahr());
		int m = d.getMonat();
		while (d.getMonat() == m) {
			d = d.naechsterTag();
		}
		d = d.vorherigerTag();
		return d;
	}

	/**
	 * Ermittelt das Datum, das n Monate vor dem vorliegenden Datum liegt.
	 *
	 * @param n Anzahl der Monate.
	 * @return Das Datum, das n Monate vor dem vorliegenden liegt.
	 * @throws IllegalArgumentException wenn n kleiner oder gleich null ist).
	 */
	public PDate monateVorher(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
		}
		PDate d = new PDate(this);
		for (int i = 0; i < n; i++) {
			d = d.monatsErster();
			d = d.vorherigerTag();
		}
		while (d.getTag() > this.getTag()) {
			d = d.vorherigerTag();
		}
		return d;
	}

	/**
	 * Ermittelt das Datum, das n Monate nach dem vorliegenden Datum liegt.
	 *
	 * @param n Anzahl der Monate.
	 * @return Das Datum, das n Monate nach dem vorliegenden liegt.
	 * @throws IllegalArgumentException wenn n kleiner oder gleich null ist).
	 */
	public PDate monateNachher(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
		}
		PDate d = new PDate(this);
		int lastday = 0;
		for (int i = 0; i < n; i++) {
			d = d.monatsLetzter();
			d = d.naechsterTag();
			d = d.monatsLetzter();
			lastday = d.getTag();
			d = d.monatsErster();
			while ((d.getTag() < this.getTag()) && (d.getTag() < lastday)) {
				d = d.naechsterTag();
			}
		}
		return d;
	}

	/**
	 * Ermittelt das Datum, das n Jahre vor dem vorliegenden Datum liegt.
	 *
	 * @param n Anzahl der Jahre.
	 * @return Das Datum, das n Jahre vor dem vorliegenden liegt.
	 * @throws IllegalArgumentException wenn n kleiner oder gleich null ist).
	 */
	public PDate jahreVorher(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
		}
		return this.shiftJahr(0 - n);
	}

	/**
	 * Ermittelt das Datum, das n Jahre vor dem vorliegenden Datum liegt.
	 *
	 * @param n Anzahl der Jahre.
	 * @return Das Datum, das n Jahre nach dem vorliegenden liegt.
	 * @throws IllegalArgumentException wenn n kleiner oder gleich null ist).
	 */
	public PDate jahreNachher(int n) throws IllegalArgumentException {
		if (n <= 0) {
			throw new IllegalArgumentException("n muss groesser oder gleich 0 sein!");
		}
		return this.shiftJahr(n);
	}

	private PDate shiftJahr(int n) {
		PDate d = new PDate(this);
		d = d.setJahr(d.getJahr() + n);
		if (this.isSchaltjahr() && (!d.isSchaltjahr()) && (this.getMonat() == 2) && (this.getTag() == 29)) {
			d = d.setTag(28);
		}
		return d;
	}

	/** @return Der deutsche Name f&uuml;r den Wochentag des vorliegenden Datums. */
	public String getTagesnameDeutsch() {
		if (this.getWochentag() == Wochentag.MONTAG) {
			return "Montag";
		} else if (this.getWochentag() == Wochentag.DIENSTAG) {
			return "Dienstag";
		} else if (this.getWochentag() == Wochentag.MITTWOCH) {
			return "Mittwoch";
		} else if (this.getWochentag() == Wochentag.DONNERSTAG) {
			return "Donnerstag";
		} else if (this.getWochentag() == Wochentag.FREITAG) {
			return "Freitag";
		} else if (this.getWochentag() == Wochentag.SONNABEND) {
			return "Sonnabend";
		} else if (this.getWochentag() == Wochentag.SONNTAG) {
			return "Sonntag";
		}
		return "UNBEKANNT";
	}

	public String getMonatsnameDeutsch() {
		switch (this.getMonat()) {
		case 1:
			return "Januar";
		case 2:
			return "Februar";
		case 3:
			return "M\344rz";
		case 4:
			return "April";
		case 5:
			return "Mai";
		case 6:
			return "Juni";
		case 7:
			return "Juli";
		case 8:
			return "August";
		case 9:
			return "September";
		case 10:
			return "Oktober";
		case 11:
			return "November";
		case 12:
			return "Dezember";
		}
		return "UNBEKANNT";
	}

	/**
	 * Ermittelt den Abstand zwischen dem vorliegenden und dem angegebenen Datum in
	 * Jahren.
	 * 
	 * @param d Das Datum, zu dem der Jahresabstand ermittelt werden soll.
	 * @return Anzahl der Jahre zwischen dem vorliegenden und dem angegebenen Datum.
	 */
	public int jahreabstandZu(PDate d) {
		PDate d0 = new PDate(this);
		int erg = -1;
		if (d0.toInt() > d.toInt()) {
			PDate h = new PDate(d0);
			d0 = new PDate(d);
			d = h;
		}
		while (d0.toInt() <= d.toInt()) {
			if ((d0.getMonat() == d.getMonat()) && (d0.getTag() == d.getTag())) {
				erg++;
			}
			d0 = d0.naechsterTag();
		}
		return erg;
	}

	/**
	 * Ermittelt den Abstand zwischen dem vorliegenden und dem angegebenen Datum in
	 * Tagen.
	 * 
	 * @param d Das Datum, zu dem der Tagesabstand ermittelt werden soll.
	 * @return Anzahl der Tage zwischen dem vorliegenden und dem angegebenen Datum.
	 */
	public int tagesabstandZu(PDate d) {
		int erg = 0;
		PDate dh = new PDate(this);
		if (dh.toInt() > d.toInt()) {
			dh = new PDate(d);
			d = new PDate(this);
		}
		while (dh.toInt() < d.toInt()) {
			dh = dh.naechsterTag();
			erg++;
		}
		return erg;
	}

	/** @return Die Nummer der Woche des Jahres zum Datum. */
	public int getWoche() {
		log.debug("\nEntrypoint: PDate -> getWoche()");
		GregorianCalendar dt = new GregorianCalendar();
		dt.set(this.getJahr(), this.getMonat() - 1, this.getTag());
		log.debug("    dt=" + dt);
		log.debug("    dt.get(Calendar.WEEK_OF_YEAR)=" + dt.get(Calendar.WEEK_OF_YEAR));
		return dt.get(Calendar.WEEK_OF_YEAR);
	}

	/**
	 * Ermittelt, ob sich das vorliegende Datum zwischen den beiden angegebenen
	 * Daten befindet. Liegt das Datum d0 hinter dem Datum d1, so werden die Daten
	 * vertauscht.
	 *
	 * @param d0 Beginn des Zeitraums.
	 * @param d1 Ende des Zeitraumes.
	 * @return <TT>true</TT>, wenn das vorliegende Datum in dem angegebenen Zeitraum
	 *         liegt.
	 */
	public boolean isImZeitraum(PDate d0, PDate d1) {
		if (d0.toInt() > d1.toInt()) {
			PDate dh = new PDate(d0);
			d0 = new PDate(d1);
			d1 = new PDate(dh);
		}
		if ((this.toInt() >= d0.toInt()) && (this.toInt() <= d1.toInt())) {
			return true;
		}
		return false;
	}

	/** @return Der Wochentag zum Datum. */
	public Wochentag getWochentag() {
		GregorianCalendar dt = new GregorianCalendar();
		dt.set(this.getJahr(), this.getMonat() - 1, this.getTag());
		switch (dt.get(Calendar.DAY_OF_WEEK)) {
		case Calendar.MONDAY:
			return Wochentag.MONTAG;
		case Calendar.TUESDAY:
			return Wochentag.DIENSTAG;
		case Calendar.WEDNESDAY:
			return Wochentag.MITTWOCH;
		case Calendar.THURSDAY:
			return Wochentag.DONNERSTAG;
		case Calendar.FRIDAY:
			return Wochentag.FREITAG;
		case Calendar.SATURDAY:
			return Wochentag.SONNABEND;
		}
		return Wochentag.SONNTAG;
	}

	/** @return Nummer des Tages im Jahr des Datums. */
	public int tagDesJahres() {
		GregorianCalendar dt = new GregorianCalendar();
		dt.set(this.getJahr(), this.getMonat() - 1, this.getTag());
		return dt.get(Calendar.DAY_OF_YEAR);
	}

	/**
	 * Der Montag zur angegebenen Woche des angegebenen Jahres.
	 *
	 * @param woche Die Nummer der Woche, zu der der Montag ermittelt werden soll
	 *              (sollte zwischen 1 und 53 liegen).
	 * @param jahr  Die Jahreszahl, um die Woche eindeutig zu beschreiben.
	 * @return Das Datum des Montags der Woche als PDate.
	 * @throws IllegalArgumentException falls die Woche au&szlig;erhalb der
	 *                                  zul&auml;ssigen Parameter liegt.
	 *
	 * @changed OLI 14.04.2009 - Hinzugef&uuml;gt.
	 *          <P>
	 *
	 */
	public static PDate GetMontagVonWoche(int woche, int jahr) throws IllegalArgumentException {
		PDate pd = new PDate(31, 12, jahr);
		int maxwoche = pd.getWoche();
		while (maxwoche < 2) {
			pd = pd.vorherigerTag(7);
			maxwoche = pd.getWoche();
		}
		log.debug("\nEntrypoint: PDate.GetMontagVonWoche(" + woche + ", " + jahr + ")");
		log.debug("    maxwoche=" + maxwoche);
		if ((woche < 1) || (woche > maxwoche)) {
			throw new IllegalArgumentException("FEHLER: Die Wochennummer (" + woche + ") liegt "
					+ "nicht innerhalb der erlaubten Parameter (1.." + maxwoche + ")");
		}
		pd = new PDate(1, 1, jahr);
		log.debug("    pd=" + pd);
		log.debug("    pd.getWoche()=" + pd.getWoche());
		if (pd.getWoche() == 1) {
			while (pd.getWochentag() != Wochentag.MONTAG) {
				pd = pd.vorherigerTag();
			}
		} else {
			while ((pd.getWochentag() != Wochentag.MONTAG) && (pd.getWoche() != 1)) {
				pd = pd.naechsterTag();
			}
		}
		while (woche > pd.getWoche()) {
			pd = pd.naechsterTag(7);
		}
		return pd;
	}

	/* Neue, englischsprachige Methoden. */

	/**
	 * Liefert den Tag des Datums.
	 *
	 * @return Der Tag zum Datum.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public int getDay() {
		return this.getTag();
	}

	/**
	 * Liefert den Monat des Datums.
	 *
	 * @return Der Monat zum Datum.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public int getMonth() {
		return this.getMonat();
	}

	/**
	 * Liefert das Jahr des Datums.
	 *
	 * @return Das Jahr zum Datum.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public int getYear() {
		return this.getJahr();
	}

	/**
	 * Liefert das Datum des letzten Tages des Monats des vorliegenden Datums.
	 *
	 * @return Das Datum des letzten Tages des Monats des vorliegenden Datums.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public PDate lastOfMonth() {
		return this.monatsLetzter();
	}

	/**
	 * Liefert das Datum des Folgetages zum vorliegenden Tag.
	 *
	 * @return Das Datum des Folgetages zum vorliegenden Tag.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public PDate nextDay() {
		return this.naechsterTag();
	}

	/**
	 * Liefert das Datum des n-ten Folgetages zum vorliegenden Tag.
	 *
	 * @return Das Datum des n-ten Folgetages zum vorliegenden Tag.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public PDate nextDay(int n) {
		return this.naechsterTag(n);
	}

	/**
	 * Setzt den &uuml;bergebenen Tag als neuen Tag f&uuml;r das Datum ein.
	 *
	 * @param day Der neue Tag zum Datum.
	 * @return Ein PDate mit der ge&auml;nderten Tagesangabe.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public PDate setDay(int day) {
		return this.setTag(day);
	}

	/**
	 * Setzt den &uuml;bergebenen Monat als neuen Monat f&uuml;r das Datum ein.
	 *
	 * @param month Der neue Monat zum Datum.
	 * @return Ein PDate mit der ge&auml;nderten Monatsangabe.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public PDate setMonth(int month) {
		return this.setMonat(month);
	}

	/**
	 * Setzt das &uuml;bergebene Jahr als neues Jahr f&uuml;r das Datum ein.
	 *
	 * @param year Das neue Jahr zum Datum.
	 * @return Ein PDate mit der ge&auml;nderten Jahresangabe.
	 *
	 * @changed OLI 16.02.2009 - Hinzugef&uuml;gt
	 *          <P>
	 *
	 */
	public PDate setYear(int year) {
		return this.setJahr(year);
	}

}
