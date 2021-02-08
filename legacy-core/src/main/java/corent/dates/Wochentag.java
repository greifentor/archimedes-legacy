/*
 * Wochentag.java
 *
 * 02.12.2003
 *
 * (c) by O.Lieshoff
 *
 */

package corent.dates;

import java.util.Vector;

/**
 * Mit Hilfe dieser Implementierung eines typsicheren Enum mit Ordinalzahlen
 * werden die Wochentage repr&auml;sentiert.
 * <P>
 * Die Ordinalzahlen f&uuml;r die Wochentage sind nach dem in Deutschland
 * g&uuml;ltigen System nummeriert. Die Woche beginnt mit dem Montag
 * (Ordinalzahl 0). Hieraus ergeben sich die folgenden Ordinalzahlen f&uuml;r
 * die Wochentage (Wochentag (Ordinalzahl)):<BR>
 * <BR>
 * Montag (0)<BR>
 * Dienstag (1)<BR>
 * Mittwoch (2)<BR>
 * Donnerstag (3)<BR>
 * Freitag (4)<BR>
 * Samstag (5)<BR>
 * Sonntag (6)<BR>
 * 
 * Der Vergleich der Wochentage &uuml;ber die <TT>compareTo</TT>-Methode richtet
 * sich nach den Ordinalzahlen. Im folgenden einige Beispiele:<BR>
 * <BR>
 * SONNTAG.compareTo(MONTAG) = 6 => Sonntag > Montag<BR>
 * DIENSTAG.compareTo(FREITAG) = -3 => Dienstag < Freitag<BR>
 * MITTWOCH.compareTo(MITTWOCH) = 0 => Mittwoch = Mittwoch<BR>
 *
 * <P>
 * Die <TT>toString()</TT>-Methode liefert den Namen des Wochentages. Die
 * Methode <TT>ord()</TT> gibt die Ordinalzahl des Wochentages zur&uuml;ck.
 * Folgender Programmcode f&uuml;hrt zu nachfolgend angegebener Ausgabe:<BR>
 * 
 * <PRE>
 * System.out.println(MONTAG + "\n" + SONNTAG.toString() + "\n" + MITTWOCH.ord());
 * 
 * Montag
 * Sonntag
 * 2
 * </PRE>
 *
 * <P>
 * &Uuml;ber die statische Methode <TT>valueOf(int)</TT> kann der einer
 * Ordinalzahl zugeordnete Wochentag ermittelt werden:<BR>
 * 
 * <PRE>
 * System.out.println(Wochentag.valueOf(3));
 *
 * Donnerstag
 * </PRE>
 * 
 * <P>
 * <I>Hinweis:</I> Die Klasse gibt eine Testausgabe, wenn man ihre
 * <TT>main</TT>-Methode aufruft.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.08.2007 - Erweiterung der Dokumentation und Einbau einer
 *          <TT>main</TT>-Methode in der einige Testausgaben produziert werden.
 *
 */

public final class Wochentag implements Comparable {

	/* Liste der erzeugten Wochentage. */
	private static final Vector WOCHENTAGE = new Vector();
	private static int nextOrdinal = 0;

	private final String name;
	private final int ord = nextOrdinal++;

	private Wochentag(String name) {
		super();
		this.name = name;
		WOCHENTAGE.addElement(this);
	}

	/**
	 * Liefert den Namen des Wochentages (MONTAG.toString() liefert beispielsweise
	 * den String "Montag").
	 *
	 * @return Der Name des Wochentages (in korrekter
	 *         Gro&szlig;-Klein-Schreibweise).
	 */
	@Override
	public String toString() {
		return this.name;
	}

	@Override
	public int compareTo(Object o) {
		return this.ord - ((Wochentag) o).ord;
	}

	/**
	 * Ermittelt die Ordinalzahl des Wochentages.
	 *
	 * @return Die Ordnungszahl des Wochentages.
	 */
	public int ord() {
		return this.ord;
	}

	/**
	 * Liefert einen Wochentag zur angegebenen Ordnungszahl.
	 *
	 * @param ord Die Ordnungszahl, zu der der Wochentag geliefert werden soll.
	 * @throws IllegalArgumentException wenn eine Ordnungszahl &uuml;bergeben wird,
	 *                                  zu der kein Wochentag existiert.
	 */
	public static Wochentag valueOf(int ord) throws IllegalArgumentException {
		if ((ord >= 0) && (ord < WOCHENTAGE.size())) {
			return (Wochentag) WOCHENTAGE.elementAt(ord);
		}
		throw new IllegalArgumentException("Wochentag mit Ordnungszahl " + ord + " existiert " + "nicht!");
	}

	/** Das Wochentagsobjekt f&uuml;r den Montag. */
	public static final Wochentag MONTAG = new Wochentag("Montag");
	/** Das Wochentagsobjekt f&uuml;r den Dienstag. */
	public static final Wochentag DIENSTAG = new Wochentag("Dienstag");
	/** Das Wochentagsobjekt f&uuml;r den Mittwoch. */
	public static final Wochentag MITTWOCH = new Wochentag("Mittwoch");
	/** Das Wochentagsobjekt f&uuml;r den Donnerstag. */
	public static final Wochentag DONNERSTAG = new Wochentag("Donnerstag");
	/** Das Wochentagsobjekt f&uuml;r den Freitag. */
	public static final Wochentag FREITAG = new Wochentag("Freitag");
	/** Das Wochentagsobjekt f&uuml;r den Sonnabend (bzw. Samstag). */
	public static final Wochentag SONNABEND = new Wochentag("Samstag");
	/** Das Wochentagsobjekt f&uuml;r den Sonntag. */
	public static final Wochentag SONNTAG = new Wochentag("Sonntag");

}
