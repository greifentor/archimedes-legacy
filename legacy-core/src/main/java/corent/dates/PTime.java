/*
 * PTime.java
 *
 * 04.01.2005
 *
 * (c) by O.Lieshoff
 *
 */

package corent.dates;

import java.io.Serializable;
import java.util.Calendar;

import corent.base.Utl;

/**
 * &Auml;hnlich der PTime-Klasse bildet diese Klasse eine Uhrzeit als Ganzzahl ab und bietet entsprechende Methoden zur
 * Manipulation an.
 * <P>
 * Die Property <TT>corent.dates.PTime.undefined.string</TT> erm&ouml;glicht die Angabe eines alternativen String bei
 * der Ausgabe undefinierter PTime-Angaben. Der Defaultwert ist "XX:XX:XX".
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.03.2010 - Korrektur: Bei &Uuml;bergabe des Wertes <TT>0</TT> wird nun keine Exception mehr von den
 *          Methoden <TT>naechsteMinute(int)</TT>, <TT>naechsteSekunde(int)</TT>, <TT>vorherigeMinute(int)</TT> und
 *          <TT>vorherigeSekunde(int)</TT> geworfen.
 */

public class PTime implements Serializable {

	/** Das undefinierte Datum als Bezeichner. */
	public static final PTime UNDEFINIERT = new PTime(false);

	/* Hier wird der Wert der PTime gespeichert. */
	private int t = -1;

	private PTime(boolean b) {
		super();
	}

	/** Erzeugt eine PTime mit der aktuellen Uhrzeit der ausf&uuml;hrenden Maschine. */
	public PTime() {
		super();
		Calendar dt = Calendar.getInstance();
		t = (dt.get(Calendar.HOUR_OF_DAY) * 10000) + (dt.get(Calendar.MINUTE) * 100) + dt.get(Calendar.SECOND);
	}

	/**
	 * Erzeugt eine PTime aus dem &uuml;bergebenen Integer-Wert, falls dieser eine g&uuml;ltige PTime enth&auml;t.
	 *
	 * @param pt Ein int-Wert im PTime-Format.
	 * @throws TimeFormatException wenn der int-Wert keine g&uuml;ltige Uhrzeit enth&auml;lt.
	 */
	public PTime(int pt) throws TimeFormatException {
		this(pt / 10000, (pt / 100) % 100, pt % 100);
	}

	/**
	 * Erzeugt eine PTime aus den &uuml;bergebenen Parametern.
	 *
	 * @param stunde  Die Stunde der &uuml;bergebenen Uhrzeit.
	 * @param minute  Die Minute der &uuml;bergebenen Uhrzeit.
	 * @param sekunde Die Sekunde der &uuml;bergebenen Uhrzeit.
	 */
	public PTime(int stunde, int minute, int sekunde) {
		super();
		if (CheckTime(stunde, minute, sekunde)) {
			this.t = stunde * 10000 + minute * 100 + sekunde;
		} else {
			throw new TimeFormatException("time not valid: " + stunde + ":" + minute + ":" + sekunde);
		}
	}

	/**
	 * Erzeugt aus dem angegeben String eine PTime.
	 *
	 * @param s Der String, aus dem die PTime generiert werden soll.
	 * @throws TimeFormatException wenn der Inhalt des &uuml;bergebenen String fehlerhaft ist.
	 */
	public static PTime valueOf(String s) throws TimeFormatException {
		char c;
		int a = 0;
		int stunde = 0;
		int minute = 0;
		int p = 0;
		int sekunde = 0;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i);
			if ((c >= '0') && (c <= '9')) {
				a = a * 10;
				a += (c - '0');
			} else if (c == ':') {
				p++;
				switch (p) {
				case 1:
					stunde = a;
					break;
				case 2:
					minute = a;
					break;
				case 3:
					sekunde = a;
					break;
				default:
					throw new TimeFormatException("to many arguments");
				}
				a = 0;
			} else {
				throw new TimeFormatException("invalid digit: " + c);
			}
		}
		sekunde = a;
		if (!CheckTime(stunde, minute, sekunde)) {
			throw new TimeFormatException("time not valid: " + stunde + ":" + minute + ":" + sekunde);
		}
		return new PTime(stunde, minute, sekunde);
	}

	/**
	 * Checkt die &uuml;bergebenen Parameter auf Darstellung einer g&uuml;tigen Uhrzeit.
	 *
	 * @param stunde  Die Stunde der zu pr&uuml;fenden Uhrzeit.
	 * @param minute  Die Minute der zu pr&uuml;fenden Uhrzeit.
	 * @param sekunde Die Sekunde der zu pr&uuml;fenden Uhrzeit.
	 * @return <TT>true</TT>, wenn die &uuml;bergebenen Parameter eine g&uuml;ltige Uhrzeit ergeben,<BR>
	 *         <TT>false</TT> sonst.
	 */
	private static boolean CheckTime(int stunde, int minute, int sekunde) {
		if ((sekunde < 0) || (sekunde > 59)) {
			return false;
		}
		if ((minute < 0) || (minute > 59)) {
			return false;
		}
		if ((stunde < 0) || (stunde > 23)) {
			return false;
		}
		return true;
	}

	/**
	 * Wandelt eine PTime in einen int-Wert um.
	 *
	 * @return Der int-Wert (HHMMSS) zur PTime.
	 */
	public int toInt() {
		return this.t;
	}

	/** @return Die Sekunde der Uhrzeit. */
	public int getSekunde() {
		return this.t % 100;
	}

	/** @return Die Minute der Uhrzeit. */
	public int getMinute() {
		return (this.t % 10000) / 100;
	}

	/** @return Die Stunde der Uhrzeit. */
	public int getStunde() {
		return this.t / 10000;
	}

	public boolean equals(Object o) {
		if (!(o instanceof PTime)) {
			return false;
		}
		PTime p = (PTime) o;
		return (this.toInt() == p.toInt());
	}

	public int hashCode() {
		return this.toInt();
	}

	public String toString() {
		String s = "";
		if (this.toInt() == -1) {
			s = s + Utl.GetProperty("corent.dates.PTime.undefined.string", "XX:XX:XX");
		} else {
			String h = "";
			h = "" + this.getStunde();
			if (h.length() < 2) {
				s = "0";
			}
			s = s + h + ":";
			h = "" + this.getMinute();
			if (h.length() < 2) {
				s = s + "0";
			}
			s = s + h + ":";
			h = "" + this.getSekunde();
			while (h.length() < 2) {
				h = "0" + h;
			}
			s = s + h;
		}
		return s;
	}

}