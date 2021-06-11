/*
 * DateFormatStyle.java
 *
 * 03.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.dates;

import java.text.DateFormat;

/**
 * Dieser Enum definiert Formatierungsstile f&uuml;r Datumsangaben im Zusammenspiel mit den Timestamp-Implementierungen.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 03.09.2009 - Hinzugef&uuml;gt
 *
 */

enum DateFormatStyle {

	/** Bezeichner f&uuml;r die kurze Formatierung. */
	SHORT(DateFormat.SHORT),
	/** Bezeichner f&uuml;r die mittellange Formatierung. */
	MEDIUM(DateFormat.MEDIUM);

	private int styleConstant = -1;

	private DateFormatStyle(int styleConstant) {
		this.styleConstant = styleConstant;
	}

	/**
	 * Liefert die Style-Konstante der DateFormat-Klasse.
	 *
	 * @return Die Style-Konstante der DateFormat-Klasse.
	 *
	 */
	public int getStyleConstant() {
		return this.styleConstant;
	}

}