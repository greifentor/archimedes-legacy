/*
 * DomainStringBuilder.java
 *
 * 14.06.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensure;

import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.TabellenspaltenModel;

/**
 * Klasse zur Erzeugung eines individualisierten Domain-String zu einer
 * Tabellenspalte.
 * 
 * @author ollie
 * 
 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
 */

public class DomainStringBuilder {

	private TabellenspaltenModel column = null;

	/**
	 * Erzeugt eine neue Instanz der Klasse anhand der &uuml;bergebenen
	 * Parameter.
	 * 
	 * @param column
	 *            Die Tabellenspalte, zu der der Domain-String erzeugt werden
	 *            soll.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition column != <TT>null</TT>
	 * 
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	public DomainStringBuilder(TabellenspaltenModel column) throws IllegalArgumentException {
		super();
		ensure(column != null, "column cannot be null.");
		this.column = column;
	}

	/**
	 * Erzeugt einen individualisierten String zur Domain, der gegebenenfalls
	 * den Defaultwert der Tabellenspalte enth&auml;lt.
	 * 
	 * @return Ein individualisierter String zur Domain, der gegebenenfalls den
	 *         Defaultwert der Tabellenspalte enth&auml;lt.
	 * 
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	public String build() {
		DomainModel d = this.column.getDomain();
		String defaultValue = this.column.getDefaultValue();
		StringBuffer sb = new StringBuffer();
		if (d != null) {
			sb.append(d.getName()).append(" (").append(d.getType()).append(")");
			if ((defaultValue != null) && (defaultValue.length() > 0) && !defaultValue.equals("NULL")) {
				sb.append(" - ").append(defaultValue);
			}
		} else {
			sb.append("<null>");
		}
		return sb.toString();
	}

}