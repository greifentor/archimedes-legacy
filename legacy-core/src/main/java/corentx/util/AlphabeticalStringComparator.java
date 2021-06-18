/*
 * AlphabeticalStringComparator.java
 *
 * 10.09.2009
 *
 * (c) by O.Lieshoff
 *
 */

package corentx.util;

import java.util.Comparator;

/**
 * Dieser Comparator vergleicht Strings ohne Ber&uuml;cksichtigung der Gro&szlig;- und Kleinschreibung anhand der
 * alphabetischen Ordnung mit einander.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 10.09.2009 - Hinzugef&uuml;gt
 * @changed OLI 22.10.2009 - Herausnahme der <TT>equals(Object)</TT>-Methode.
 */

public class AlphabeticalStringComparator implements Comparator<String> {

	/** Generiert ein AlphabeticalStringComparator-Objekt mit Defaultwerten. */
	public AlphabeticalStringComparator() {
		super();
	}

	/* Implememtierung des Interfaces Comparator. */

	public int compare(String o1, String o2) {
		return o1.toLowerCase().compareTo(o2.toLowerCase());
	}

}