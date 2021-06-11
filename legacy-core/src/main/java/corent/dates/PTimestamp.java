/*
 * PTimestamp.java
 *
 * 06.04.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.dates;

import java.io.Serializable;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import corent.base.Direction;
import corent.base.StrUtil;
import corent.base.Utl;

/**
 * Diese Klasse bietet Ihnen einen gekapselten Zeitstempel in speicherfreundlichem Format.
 *
 * <P>
 * Die Zeitangaben werden in einer Ganzzahl in dem Format YYYYMMDDHHmmSS gespeichert und sind auch in diesem Format
 * verf&uuml;gbar. Dieses Format besticht vorallem durch performante Vergleichsm&ouml;glichkeiten und hohe
 * Speicherfreundlichkeit.
 *
 * <HR SIZE=3>
 * <H3>Arbeiten mit PTimestamp</H3>
 * <HR>
 * <BR>
 * Sie k&ouml;nnen einen PTimestamp auf normalem Wege &uuml;ber den Aufruf eines Konstruktors instanziieren. Der
 * parameterlose Konstruktor liefert Ihnen eine PTimestamp-Instanz mit der aktuellen, sekundengenauen Systemzeit.
 * <P>
 * Mit Hilfe der <TT>add(TimestampUnit, int)</TT>-Methode k&ouml;nnen Sie einen bestehenden PTimestamp manipulieren.
 * Hierbei wird eine Kopie der Instanz angelegt. Der bestehende PTimestamp beh&auml;lt seinen Wert. &Uuml;bergeben Sie
 * der Methoden eine <TT>TimestampUnit</TT> als Parameter, &uuml;ber die Sie das Attribut des PTimestamps
 * ausw&auml;hlen, das manipuliert werden soll. <BR>
 * M&ouml;chten Sie beispielsweise ein Datum um einen Tag in die Zukunft verschieben, so m&uuml;ssen Sie folgenden Code
 * implementieren:
 * 
 * <PRE>
 * PTimestamp pts = new PTimestamp();
 * pts = ((PTimestamp) pts).add(TimestampUnit.DAY, 1);
 * </PRE>
 * 
 * Der Cast auf PTimestamp ist notwendig, da die <TT>add</TT>-Methode ein TimestampModel als R&uuml;ckgabewert vorsieht.
 * PTimestamp ist lediglich eine Implementierung dieses Interfaces. Daher der Cast. <BR>
 * Um ein Datum um einen Tag in die Vergangenheit zu verschieben, h&auml;lt ein analoges Codest&uuml;ck her. Der
 * Unterschied besteht lediglich im Vorzeichen:
 * 
 * <PRE>
 * PTimestamp pts = new PTimestamp();
 * pts = ((PTimestamp) pts).add(TimestampUnit.DAY, -1);
 * </PRE>
 * 
 * Die anderen Attribute, wie Stunden, Minuten Sekunden etc. k&ouml;nnen Sie analog dazu unter Angabe anderer
 * TimestampUnits manipulieren.
 * <P>
 * Die einzelnen Attribute eines PTimestamps lassen sich &uuml;ber die <TT>set(TimestampUnit, int)</TT>- und
 * <TT>get(TimestampUnit)</TT>-Methode setzen bzw. auslesen. M&ouml;chten Sie beispielsweise nur die aktuelle Jahreszahl
 * eines PTimestamps auslesen, so k&ouml;nnen Sie dies durch die folgenden Codezeilen erreichen:
 * 
 * <PRE>
 * PTimestamp pts = new PTimestamp();
 * int jahr = pts.get(TimestampUnit.YEAR);
 * </PRE>
 * 
 * Umgekehrt k&ouml;nnen Sie eine Jahreszahl setzen, indem Sie, wie durch das folgende Codest&uuml;ck demonstriert, die
 * <TT>set</TT>-Methode aufrufen:
 * 
 * <PRE>
 * PTimestamp pts = new PTimestamp();
 * pts = ((PTimestamp) pts).set(TimestampUnit.YEAR, 1998);
 * </PRE>
 * <P>
 * <I>Hinweis:</I> Nutzen Sie die <TT>set(TimestampUnit, int)</TT>-Methode nur, um einem oder mehreren Attributen einen
 * festen Wert zuzuweisen. F&uuml;r relative &Auml;derungen an einem PTimestamp sollten Sie auf jeden Fall die
 * <TT>add(TimestampUnit, int)</TT>-Methode benutzen. Ein
 * <TT>pts.set(TimestampUnit.DAY, pts.get(TimestampUnit.DAY)-1)</TT> mu&szlig; nicht immer den Vortag ergeben ...
 * <P>
 * Die Wertebereiche f&uuml;r die Parameter der <TT>get</TT>- und der <TT>set</TT>-Methode sind wie folgt definiert:
 * <BR>
 * <A NAME="Table:TimestampUnitValues"></A>
 * <TABLE BORDER=1>
 * <TR VALIGN=TOP>
 * <TH>TimestampUnit</TH>
 * <TH>Wertebereich</TH>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>DAY</TD>
 * <TD>1..31</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>HOUR</TD>
 * <TD>0..23</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>MILLI</TD>
 * <TD>0..999</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>MINUTE</TD>
 * <TD>0..59</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>MONTH</TD>
 * <TD>1..12</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>SECOND</TD>
 * <TD>0..59</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>YEAR</TD>
 * <TD>0..Integer.MAX_VALUE</TD>
 * </TR>
 * </TABLE>
 *
 * <P>
 * Sie k&ouml;nnen PTimestamps jeder Zeit in einen <TT>long</TT>-Wert mit dem oben angegebenen Format (YYYYMMDDHHmmSS)
 * umwandeln. Dies erreichen Sie &uuml;ber den Aufruf der Methode <TT>toLong()</TT>. Umgekehrt k&ouml;nnen Sie auch
 * <TT>long</TT>-Werte in diesem Format an einen entsprechenden Konstruktor &uuml;bergeben, um daraus einen PTimestamp
 * zu generieren. Diese Methoden eignen sich sehr gut dazu, PTimestamps zu persistieren.
 * <P>
 * &nbsp;
 *
 * <HR SIZE=3>
 * <H3>Ausgabe von PTimestamps</H3>
 * <HR>
 * F&uuml;r die Ausgabe von PTimestamps bietet Ihnen die Klasse eine Reihe von Konfigurationsm&ouml;glichkeiten an.
 * &Uuml;ber den Aufruf der <TT>toString()</TT>-Methode erhalten Sie zun&auml;chst eine Ausgabe in dem folgenden Format:
 * 
 * <PRE>
 * new PTimestamp(19980206054800).toString() -&gt; 06.02.1998 05:48:00
 * </PRE>
 * 
 * Haben Sie hierbei die Property <TT>corent.dates.PTimestamp.suppress.seconds</TT> gesetzt, so &auml;ndert sich die
 * Ausgabe ein wenig:
 * 
 * <PRE>
 * new PTimestamp(19980206054800).toString() -&gt; 06.02.1998 05:48
 * </PRE>
 * 
 * Neben der herk&ouml;mmlichen <TT>toString()</TT>-Methode gibt es noch eine weitere, die einen Parameter erfordert,
 * &uuml;ber den sie den generierten String beeinflu&szlig;en k&ouml;nnen:
 * 
 * <PRE>
 * new PTimestamp(20000407231500).toString(Mode.WEEK) -&gt; KW 14  07.03.2000 05:48
 * new PTimestamp(20000407231500).toString(Mode.DAYIDENT) -&gt; Fr  07.03.2000 05:48
 * new PTimestamp(20000407231500).toString(Mode.WEEKANDDAYIDENT) -&gt; KW 14  Fr  07.03.2000 05:48
 * </PRE>
 * 
 * Mit Hilfe der Properties <TT>corent.dates.PDate.week.day.shortform.<I>x</I></TT> und
 * <TT>corent.dates.PDate.week.prefix</TT> sind sie in der Lage die Ausgabe zu beeinflu&szlig;en:
 * 
 * <PRE>
 * System.setProperty("corent.dates.PDate.week.day.shortform.5", "Fri");
 * System.setProperty("corent.dates.PDate.week.prefix", "Week");
 * new PTimestamp(20000407231500).toString(Mode.WEEK) -&gt; Week 14  07.03.2000 05:48
 * new PTimestamp(20000407231500).toString(Mode.DAYIDENT) -&gt; Fri  07.03.2000 05:48
 * new PTimestamp(20000407231500).toString(Mode.WEEKANDDAYIDENT) -&gt; Week 14  Fri  07.03.2000 05:48
 * </PRE>
 * <P>
 * &nbsp;
 *
 * <HR SIZE=3>
 * <H3>Utilities</H3>
 * <HR>
 * <P>
 * Zus&auml;tzlich zum Umfang der Methoden, die sich auf die Instanzen der Klasse beziehen, bietet PTimestamp zwei
 * statische Utility-Methoden an, die auch innerhalb der Instanzmethoden zum Einsatz kommen. <BR>
 * Um zu pr&uuml;fen, ob ein Jahr ein Schaltjahr ist, oder nicht, gen&uuml;gt ein Aufruf der Methode
 * <TT>Leapyear(int)</TT>. &Uuml;bergeben Sie als Parameter einfach die Jahreszahl des Jahres, das sie pr&uuml;fen
 * wollen:
 * 
 * <PRE>
 * System.out.println(PTimestamp.Leapyear(1999));
 * &gt; false
 *
 * System.out.println(PTimestamp.Leapyear(2004));
 * &gt; true
 * </PRE>
 * 
 * Die Anzahl der Tage, die ein Monat in einem bestimmten Jahr hat, k&ouml;nnen Sie &uuml;ber einen Aufruf der Methode
 * <TT>DayCount(int, int)</TT> ermitteln:
 * 
 * <PRE>
 * System.out.println(PTimestamp.DayCount(2, 1999)); // Tage im Februar 1999.
 * &gt; 28
 *
 * System.out.println(PTimestamp.DayCount(2, 2004)); // Tage im Februar 2004.
 * &gt; 29
 * </PRE>
 * <P>
 * &nbsp;
 * 
 * <HR SIZE=3>
 * <H3>Vergleiche</H3>
 * <HR>
 * Vergleiche von PTimestamp k&ouml;nnen Sie auf zweierlei Art durchf&uuml;hren: a) Nutzen Sie die
 * <TT>compareTo(Object)</TT>-Methode, oder b) vergleichen Sie die R&uuml;ckgabewerte der Methode <TT>toLong()</TT> der
 * einzelnen PTimestamps miteinander. Die <TT>compareTo(Object)</TT>-Methode arbeitet ebenfalls &uuml;ber einen
 * Vergleich der Ergebnisse der Methode <TT>toLong()</TT>.
 * <P>
 * &nbsp;
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
 * <TD>corent.dates.PDate.week.day.shortform.<I>x</I></TD>
 * <TD>verschiedene</TD>
 * <TD>String</TD>
 * <TD>Diese Property erm&ouml;glicht das Setzen eines alternativen Tagesk&uuml;rzels. Der Wert <I>x</I> steht f&uuml;r
 * den Wochentag. Die Woche beginnt mit dem Montag als Tag 0.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.dates.PDate.week.prefix</TD>
 * <TD>"KW"</TD>
 * <TD>String</TD>
 * <TD>Mit Hilfe dieser Property kann ein alternativer Pr&auml;fix f&uuml;r die Kalenderwoche gesetzt werden.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.dates.PTimestamp.suppress.seconds</TD>
 * <TD>false</TD>
 * <TD>Boolean</TD>
 * <TD>Wird diese Property gesetzt, so wird die Uhrzeit in der <TT>toString()</TT>-Methode ohne Sekundenangabe
 * ausgegeben.</TD>
 * </TR>
 * <TR VALIGN=TOP>
 * <TD>corent.dates.PTimestamp.undefined.string</TD>
 * <TD>"--.--.---- --:--:--"</TD>
 * <TD>String</TD>
 * <TD>Mit Hilfe dieser Tabelle kann ein alternativer String f&uuml;r die Ausgabe der Konstanten PTimestamp.NULL
 * definiert werden.</TD>
 * </TR>
 * </TABLE>
 * <P>
 * nbsp;
 *
 * @author
 *         <P>
 *         O.Lieshoff
 *         <P>
 *
 * @see corent.dates.TimestampModel
 * @see corent.dates.TimestampUnit
 *      <P>
 *
 * @changed
 *          <P>
 *          OLI 23.08.2007 - Die <TT>toString()</TT>-Methode kann nun &uuml;ber das Setzen der Property
 *          "corent.dates.PTimestamp.suppress.seconds" auch ohne Sekundenangabe erfolgen.
 *          <P>
 *          OLI 12.09.2007 - Erweiterung der Testausgabe in der main-Methode um ein paar zus&auml;tzliche Testausgaben.
 *          <P>
 *          OLI 16.09.2007 - Arbeiten an der Dokumentation der Klasse.
 *          <P>
 *          OLI 19.09.2007 - Erweiterung der Dokumentation (Wertebereiche der get- und set-Methode.
 *          <P>
 *          OLI 14.08.2008 - Erweiterung um die Implementierung der Methode <TT>getMillis()</TT>.
 *          <P>
 *          OLI 31.01.2009 - Erweiterung um einen Methode zur Umwandlung in ein PDate. Au&szlig;erdem: Debugging der
 *          Methode <TT>valueOf(java.util.Date)</TT> durch ver&auml;ndern der Reihenfolge, in der Monat und Tag
 *          zugewiesen werden.
 *          <P>
 *          OLI 24.02.2009 - Die Methoden zur Manipulation von PTimestamps werfen eine
 *          <TT>IllegalArgumentException</TT>, wenn versucht wird, sie mit der <TT>TimestampUnit.MILLI</TT> zu
 *          manipulieren.
 *          <P>
 *
 */

public class PTimestamp implements Comparable, Serializable, TimestampModel {

	/**
	 * Eine Referenz f&uuml;r undefinierte Timestamps.
	 *
	 * @changed OLI 23.08.2007 - Implememtierung der Reaktion auf die Property
	 *          "corent.dates.PTimestamp.suppress.seconds".<BR>
	 */
	public static final PTimestamp NULL = new PTimestamp(-1) {
		@Override
		public String toString() {
			return Utl
					.GetProperty(
							"corent.dates.PTimestamp.undefined.string",
							"--.--.---- --:--"
									+ (Boolean.getBoolean("corent.dates.PTimestamp.suppress.seconds") ? "" : ":--"));
		}
	};

	/*
	 * Hier wird die Zeitangabe des Timestamps im Format JJJJMMTTHHMMSS gespeichert.
	 */
	private long timestamp = 10101000000l;

	/** Erzeugt eine Instanz der Klasse mit Defaultwerten. */
	public PTimestamp() {
		super();
		this.timestamp = ((long) new PDate().toInt()) * 1000000 + (new PTime().toInt());
	}

	/** Erzeugt eine Instanz der Klasse mit Defaultwerten. */
	private PTimestamp(int i) {
		super();
		this.timestamp = i;
	}

	/**
	 * Erzeugt eine neue Instanz mit den Daten des &uuml;bergebenen Objekts.
	 *
	 * @param tsm Der PTimestamp, dessen Daten &uuml;bernommen werden sollen.
	 */
	public PTimestamp(TimestampModel tsm) {
		super();
		if (tsm == NULL) {
			throw new IllegalArgumentException("PTimestamp.NULL can not be cloned!");
		}
		this.timestamp = tsm.toLong();
	}

	/**
	 * Erzeugt eine neue Instanz mit den Daten des &uuml;bergebenen Objekts.
	 *
	 * @param l Eine long-Zahl mit dem gepackten Datum.
	 * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
	 */
	public PTimestamp(long l) throws ParseException {
		super();
		this.set(TimestampUnit.YEAR, (int) (l / 10000000000l));
		this.set(TimestampUnit.MONTH, (int) (l % 10000000000l / 100000000l));
		this.set(TimestampUnit.DAY, (int) (l % 100000000l / 1000000));
		this.set(TimestampUnit.HOUR, (int) (l % 1000000 / 10000));
		this.set(TimestampUnit.MINUTE, (int) (l % 10000 / 100));
		this.set(TimestampUnit.SECOND, (int) (l % 100));
	}

	/**
	 * Erzeugt eine Instanz der Klasse anhand der &uuml;bergebenen Parameter. Der &uuml;bergebene String darf entweder
	 * aus Datum und Zeit, oder nur aus einer Datumsangabe bestehen. Die Angaben m&uuml;ssen folgendes Format haben:
	 * 
	 * <PRE>
	 *
	 * String  Datum " " Uhrzeit
	 * Datum   (T)T.MM.J(JJJ)
	 * Uhrzeit (H)H:MM(:SS)
	 * </PRE>
	 *
	 * @param s Der String mit der Datums- und eventuell Zeitangabe.
	 * @throws ParseException Falls beim Parsen des String ein Fehler auftritt.
	 */
	public PTimestamp(String s) throws ParseException {
		super();
		String h = null;
		try {
			int jahr = 0;
			int monat = 0;
			int tag = 0;
			h = s.substring(0, s.indexOf("."));
			s = s.substring(s.indexOf(".") + 1, s.length());
			tag = Integer.parseInt(h);
			h = s.substring(0, s.indexOf("."));
			s = s.substring(s.indexOf(".") + 1, s.length());
			monat = Integer.parseInt(h);
			h = s.substring(0, s.indexOf(" "));
			s = s.substring(s.indexOf(" ") + 1, s.length()).trim();
			jahr = Integer.parseInt(h);
			this.timestamp = tag * 1000000l;
			this.set(TimestampUnit.YEAR, jahr);
			this.set(TimestampUnit.MONTH, monat);
			h = s.substring(0, s.indexOf(":"));
			s = s.substring(s.indexOf(":") + 1, s.length());
			this.set(TimestampUnit.HOUR, Integer.parseInt(h));
			h = s.substring(0, s.indexOf(":"));
			s = s.substring(s.indexOf(":") + 1, s.length());
			this.set(TimestampUnit.MINUTE, Integer.parseInt(h));
			h = s.substring(0, s.length());
			this.set(TimestampUnit.SECOND, Integer.parseInt(h));
		} catch (Exception e) {
			e.printStackTrace();
			throw new ParseException("String \"" + h + " - " + s + "\" cannot be parsed for " + "PTimestamp!", 0);
		}
	}

	private void setUncontroled(TimestampUnit tsu, int value) {
		if (tsu == TimestampUnit.MILLI) {
			throw new IllegalArgumentException("TimestampUnit.MILLI is not valid for PTimetamps.");
		}
		if (tsu == TimestampUnit.YEAR) {
			this.timestamp = 10000000000l * value + this.timestamp % 10000000000l;
		} else if (tsu == TimestampUnit.MONTH) {
			this.timestamp =
					this.timestamp / 10000000000l * 10000000000l + value * 100000000l + this.timestamp % 100000000l;
		} else if (tsu == TimestampUnit.DAY) {
			this.timestamp = this.timestamp / 100000000l * 100000000l + value * 1000000l + this.timestamp % 1000000l;
		} else if (tsu == TimestampUnit.HOUR) {
			this.timestamp = this.timestamp / 1000000l * 1000000l + value * 10000l + this.timestamp % 10000l;
		} else if (tsu == TimestampUnit.MINUTE) {
			this.timestamp = this.timestamp / 10000l * 10000l + value * 100l + this.timestamp % 100l;
		} else {
			this.timestamp = this.timestamp / 100l * 100l + value;
		}
	}

	@Override
	public long toLong() {
		return this.timestamp;
	}

	/* Ueberschreiben von Methoden der Superklasse. */

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof PTimestamp)) {
			return false;
		}
		PTimestamp pts = (PTimestamp) o;
		return this.toLong() == pts.toLong();
	}

	@Override
	public int hashCode() {
		return (int) this.toLong();
	}

	/**
	 * @changed OLI 23.08.2007 - Implememtierung der Reaktion auf die Property
	 *          "corent.dates.PTimestamp.suppress.seconds".<BR>
	 */
	@Override
	public String toString() {
		return StrUtil.PumpUp("" + this.get(TimestampUnit.DAY), "0", 2, Direction.LEFT) + "."
				+ StrUtil.PumpUp("" + this.get(TimestampUnit.MONTH), "0", 2, Direction.LEFT) + "."
				+ StrUtil.PumpUp("" + this.get(TimestampUnit.YEAR), "0", 4, Direction.LEFT) + " "
				+ StrUtil.PumpUp("" + this.get(TimestampUnit.HOUR), "0", 2, Direction.LEFT) + ":"
				+ StrUtil.PumpUp("" + this.get(TimestampUnit.MINUTE), "0", 2, Direction.LEFT)
				+ (Boolean.getBoolean("corent.dates.PTimestamp.suppress.seconds")
						? ""
						: (":" + StrUtil.PumpUp("" + this.get(TimestampUnit.SECOND), "0", 2, Direction.LEFT)));
	}

	/* Implementierung des Interfaces Comparable. */

	@Override
	public int compareTo(Object o) {
		long l = (this.toLong() - ((PTimestamp) o).toLong());
		if (l < 0) {
			return -1;
		}
		if (l > 0) {
			return 1;
		}
		return 0;
	}

	/* Implementierung des Interfaces TimestampModel. */

	/**
	 * Einen Hinweis zum Wertebereich der einzelnen TimestampUnits finden Sie
	 * <A HREF="Table:TimestampUnitValues">hier</A>.
	 *
	 * @throws IllegalArgumentException falls versucht wird, &uuml;ber die <TT>TimestampUnit.MILLI</TT> den Inhalt des
	 *                                  PTimestamps zu &auml;ndern.
	 *
	 * @changed OLI 19.09.2007 - Erweiterung der Dokumentation um den Link zur Tabelle mit den Wertebereichen der
	 *          TimestampUnits.
	 *          <P>
	 *          OLI 24.02.2009 - Die &Uuml;bergabe einer <TT>TimestampUnit.MILLI</TT> f&uuml;hrt nun zu einer
	 *          <TT>IllegalArgumentException</TT>.
	 *          <P>
	 *
	 */
	@Override
	public void set(TimestampUnit tsu, int value) throws IllegalArgumentException {
		if (tsu == TimestampUnit.MILLI) {
			throw new IllegalArgumentException("TimestampUnit.MILLI is not valid for PTimetamps.");
		}
		if (tsu == TimestampUnit.YEAR) {
			if (value > 0) {
				this.timestamp = 10000000000l * value + this.timestamp % 10000000000l;
				return;
			}
			throw new IllegalArgumentException("Year must be a positive value (1+)!");
		} else if (tsu == TimestampUnit.MONTH) {
			int daycount = DayCount(value, this.get(TimestampUnit.YEAR));
			if ((value > 0) && (value < 13) && (this.get(TimestampUnit.DAY) <= daycount)) {
				this.timestamp =
						this.timestamp / 10000000000l * 10000000000l + value * 100000000l + this.timestamp % 100000000l;
				return;
			}
			if ((value < 1) || (value > 12)) {
				throw new IllegalArgumentException("Value " + value + " is not valid for month!");
			}
			throw new IllegalArgumentException(
					"Collision with daycount. Month has " + daycount + " days only (actual day="
							+ this.get(TimestampUnit.DAY) + ")!");
		} else if (tsu == TimestampUnit.DAY) {
			if ((value > 0) && (value <= DayCount(this.get(TimestampUnit.MONTH), this.get(TimestampUnit.YEAR)))) {
				this.timestamp =
						this.timestamp / 100000000l * 100000000l + value * 1000000l + this.timestamp % 1000000l;
				return;
			}
			throw new IllegalArgumentException(
					"Value " + value + " is not valid for day (month" + " " + this.get(TimestampUnit.MONTH) + ", max="
							+ DayCount(this.get(TimestampUnit.MONTH), this.get(TimestampUnit.YEAR)) + ")!");
		} else if (tsu == TimestampUnit.HOUR) {
			if ((value >= 0) && (value < 24)) {
				this.timestamp = this.timestamp / 1000000l * 1000000l + value * 10000l + this.timestamp % 10000l;
				return;
			}
			throw new IllegalArgumentException("Value " + value + " is not valid for hour!");
		} else if (tsu == TimestampUnit.MINUTE) {
			if ((value >= 0) && (value < 60)) {
				this.timestamp = this.timestamp / 10000l * 10000l + value * 100l + this.timestamp % 100l;
				return;
			}
			throw new IllegalArgumentException("Value " + value + " is not valid for minute!");
		}
		if ((value >= 0) && (value < 60)) {
			this.timestamp = this.timestamp / 100l * 100l + value;
			return;
		}
		throw new IllegalArgumentException("Value " + value + " is not valid for second!");
	}

	/**
	 * Einen Hinweis zum Wertebereich der einzelnen TimestampUnits finden Sie
	 * <A HREF="Table:TimestampUnitValues">hier</A>.
	 *
	 * @throws IllegalArgumentException falls versucht wird, &uuml;ber die <TT>TimestampUnit.MILLI</TT> den Inhalt des
	 *                                  PTimestamps zu lesen.
	 *
	 * @changed OLI 19.09.2007 - Erweiterung der Dokumentation um den Link zur Tabelle mit den Wertebereichen der
	 *          TimestampUnits.
	 *          <P>
	 *          OLI 24.02.2009 - Die &Uuml;bergabe einer <TT>TimestampUnit.MILLI</TT> f&uuml;hrt nun zu einer
	 *          <TT>IllegalArgumentException</TT>.
	 *          <P>
	 *
	 */
	@Override
	public int get(TimestampUnit tsu) {
		if (tsu == TimestampUnit.MILLI) {
			throw new IllegalArgumentException("TimestampUnit.MILLI is not valid for PTimetamps.");
		}
		if (tsu == TimestampUnit.YEAR) {
			return (int) (this.timestamp / 10000000000l);
		} else if (tsu == TimestampUnit.MONTH) {
			return (int) ((this.timestamp % 10000000000l) / 100000000l);
		} else if (tsu == TimestampUnit.DAY) {
			return (int) ((this.timestamp % 100000000l) / 1000000);
		} else if (tsu == TimestampUnit.HOUR) {
			return (int) ((this.timestamp % 1000000) / 10000);
		} else if (tsu == TimestampUnit.MINUTE) {
			return (int) ((this.timestamp % 10000) / 100);
		}
		return (int) (this.timestamp % 100);
	}

	/**
	 * Die Implementierung der Methode wurde anhand einer Vorarbeit von Volodymyr Medvid durchgef&uuml;hrt.
	 *
	 * @changed
	 *          <P>
	 *          OLI 14.08.2008 - Hinzugef&uuml;gt.
	 *
	 */
	@Override
	public long getMillis() {
		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.YEAR, this.get(TimestampUnit.YEAR));
		c.set(Calendar.MONTH, this.get(TimestampUnit.MONTH) - 1);
		c.set(Calendar.DAY_OF_MONTH, this.get(TimestampUnit.DAY));
		c.set(Calendar.HOUR_OF_DAY, this.get(TimestampUnit.HOUR));
		c.set(Calendar.MINUTE, this.get(TimestampUnit.MINUTE));
		c.set(Calendar.SECOND, this.get(TimestampUnit.SECOND));
		c.set(Calendar.MILLISECOND, 0);
		return c.getTimeInMillis();
	}

	/**
	 * @throws IllegalArgumentException falls versucht wird, &uuml;ber die <TT>TimestampUnit.MILLI</TT> den Inhalt des
	 *                                  PTimestamps zu &auml;ndern.
	 *
	 * @changed
	 *          <P>
	 *          OLI 24.02.2009 - Die &Uuml;bergabe einer <TT>TimestampUnit.MILLI</TT> f&uuml;hrt nun zu einer
	 *          <TT>IllegalArgumentException</TT>.
	 *          <P>
	 *
	 */
	@Override
	public TimestampModel add(TimestampUnit tsu, int value) throws IllegalArgumentException {
		if (tsu == TimestampUnit.MILLI) {
			throw new IllegalArgumentException("TimestampUnit.MILLI is not valid for PTimetamps.");
		}
		TimestampModel pts = new PTimestamp(this);
		if (tsu == TimestampUnit.YEAR) {
			pts.set(TimestampUnit.YEAR, pts.get(TimestampUnit.YEAR) + value);
			if ((pts.get(TimestampUnit.MONTH) == 2)
					&& (DayCount(pts.get(TimestampUnit.MONTH), pts.get(TimestampUnit.YEAR)) < pts
							.get(TimestampUnit.DAY))) {
				pts.set(TimestampUnit.DAY, 1);
				pts.set(TimestampUnit.MONTH, 3);
			}
		} else if (tsu == TimestampUnit.MONTH) {
			pts = pts.add(TimestampUnit.YEAR, (value / 12));
			value = value % 12;
			int count = (value > 0 ? value : 0 - value);
			long inc = (value > 0 ? 1 : -1);
			for (int i = 0; i < count; i++) {
				if (pts.get(TimestampUnit.MONTH) + inc < 1) {
					pts = pts.add(TimestampUnit.YEAR, -1);
					pts.set(TimestampUnit.MONTH, 12);
				} else if (pts.get(TimestampUnit.MONTH) + inc > 12) {
					pts = pts.add(TimestampUnit.YEAR, 1);
					pts.set(TimestampUnit.MONTH, 1);
				} else {
					((PTimestamp) pts).setUncontroled(TimestampUnit.MONTH, (int) (pts.get(TimestampUnit.MONTH) + inc));
				}
			}
			if ((DayCount(pts.get(TimestampUnit.MONTH), pts.get(TimestampUnit.YEAR)) < pts.get(TimestampUnit.DAY))) {
				pts = pts
						.add(
								TimestampUnit.DAY,
								(DayCount(pts.get(TimestampUnit.MONTH), pts.get(TimestampUnit.YEAR))
										- pts.get(TimestampUnit.DAY)));
				pts = pts.add(TimestampUnit.MONTH, 1);
			}
		} else if (tsu == TimestampUnit.DAY) {
			int count = (value > 0 ? value : 0 - value);
			long inc = (value > 0 ? 1 : -1);
			for (int i = 0; i < count; i++) {
				if (pts.get(TimestampUnit.DAY) + inc < 1) {
					pts = pts.add(TimestampUnit.MONTH, -1);
					pts.set(TimestampUnit.DAY, DayCount(pts.get(TimestampUnit.MONTH), pts.get(TimestampUnit.YEAR)));
				} else if (pts.get(TimestampUnit.DAY)
						+ inc > DayCount(pts.get(TimestampUnit.MONTH), pts.get(TimestampUnit.YEAR))) {
					((PTimestamp) pts).setUncontroled(TimestampUnit.MONTH, pts.get(TimestampUnit.MONTH) + 1);
					if (pts.get(TimestampUnit.MONTH) > 12) {
						pts.set(TimestampUnit.MONTH, 1);
						pts = pts.add(TimestampUnit.YEAR, 1);
					}
					pts.set(TimestampUnit.DAY, 1);
				} else {
					((PTimestamp) pts).setUncontroled(TimestampUnit.DAY, (int) (pts.get(TimestampUnit.DAY) + inc));
				}
			}
		} else if (tsu == TimestampUnit.HOUR) {
			pts = pts.add(TimestampUnit.DAY, (value / 24));
			value = value % 24;
			int count = (value > 0 ? value : 0 - value);
			long inc = (value > 0 ? 1 : -1);
			for (int i = 0; i < count; i++) {
				if (pts.get(TimestampUnit.HOUR) + inc < 0) {
					pts = pts.add(TimestampUnit.DAY, -1);
					pts.set(TimestampUnit.HOUR, 23);
				} else if (pts.get(TimestampUnit.HOUR) + inc > 23) {
					pts = pts.add(TimestampUnit.DAY, 1);
					pts.set(TimestampUnit.HOUR, 0);
				} else {
					((PTimestamp) pts).setUncontroled(TimestampUnit.HOUR, (int) (pts.get(TimestampUnit.HOUR) + inc));
				}
			}
		} else if (tsu == TimestampUnit.MINUTE) {
			pts = pts.add(TimestampUnit.HOUR, (value / 60));
			value = value % 60;
			int count = (value > 0 ? value : 0 - value);
			long inc = (value > 0 ? 1 : -1);
			for (int i = 0; i < count; i++) {
				if (pts.get(TimestampUnit.MINUTE) + inc < 0) {
					pts = pts.add(TimestampUnit.HOUR, -1);
					pts.set(TimestampUnit.MINUTE, 59);
				} else if (pts.get(TimestampUnit.MINUTE) + inc > 59) {
					pts = pts.add(TimestampUnit.HOUR, 1);
					pts.set(TimestampUnit.MINUTE, 0);
				} else {
					((PTimestamp) pts)
							.setUncontroled(TimestampUnit.MINUTE, (int) (pts.get(TimestampUnit.MINUTE) + inc));
				}
			}
		} else {
			pts = pts.add(TimestampUnit.HOUR, (value / 3600));
			value = value % 3600;
			pts = pts.add(TimestampUnit.MINUTE, (value / 60));
			value = value % 60;
			int count = (value > 0 ? value : 0 - value);
			long inc = (value > 0 ? 1 : -1);
			for (int i = 0; i < count; i++) {
				if (pts.get(TimestampUnit.SECOND) + inc < 0) {
					pts = pts.add(TimestampUnit.MINUTE, -1);
					pts.set(TimestampUnit.SECOND, 59);
				} else if (pts.get(TimestampUnit.SECOND) + inc > 59) {
					pts = pts.add(TimestampUnit.MINUTE, 1);
					pts.set(TimestampUnit.SECOND, 0);
				} else {
					((PTimestamp) pts)
							.setUncontroled(TimestampUnit.SECOND, (int) (pts.get(TimestampUnit.SECOND) + inc));
				}
			}
		}
		return pts;
	}

	@Override
	public java.util.Date toDate() {
		Calendar c = GregorianCalendar.getInstance();
		c.set(Calendar.YEAR, this.get(TimestampUnit.YEAR));
		c.set(Calendar.MONTH, this.get(TimestampUnit.MONTH) - 1);
		c.set(Calendar.DAY_OF_MONTH, this.get(TimestampUnit.DAY));
		c.set(Calendar.HOUR_OF_DAY, this.get(TimestampUnit.HOUR));
		c.set(Calendar.MINUTE, this.get(TimestampUnit.MINUTE));
		c.set(Calendar.SECOND, this.get(TimestampUnit.SECOND));
		c.set(Calendar.MILLISECOND, 0);
		return c.getTime();
	}

	/* Statische Methoden. */

	/**
	 * Pr&uuml;ft, ob die angegebenen Jahresangabe ein Schaltjahr ist.
	 *
	 * @param year Das zu pr&uuml;fende Jahr.
	 * @return <TT>true</TT>, wenn es sich bei dem angegebenen Jahr um ein Schaltjahr handelt.
	 */
	private static boolean Leapyear(int year) {
		if (year % 400 == 0) {
			return true;
		} else if (year % 100 == 0) {
			return false;
		} else if (year % 4 == 0) {
			return true;
		}
		return false;
	}

	/**
	 * Ermittelt die Anzahl der Tage des angegebenen Monats im ebenfalls angegebenen Jahr.
	 *
	 * @param month Der Monat, zu dem die Tagesanzahl ermittelt werden soll.
	 * @param year  Das Jahr zu Monat.
	 * @return Die Anzahl der Tage des Monats im angegebenen Jahr.
	 */
	private static int DayCount(int month, int year) {
		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			return 31;
		case 2:
			if (Leapyear(year)) {
				return 29;
			}
			return 28;
		}
		return 30;
	}

}
