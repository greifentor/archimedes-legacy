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

import archimedes.legacy.model.DomainShowMode;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.model.DomainModel;

/**
 * Klasse zur Erzeugung eines individualisierten Domain-String zu einer Tabellenspalte.
 * 
 * @author ollie
 * 
 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
 */

public class DomainStringBuilder {

	private TabellenspaltenModel column = null;
	private DomainShowMode dsm;

	/**
	 * Erzeugt eine neue Instanz der Klasse anhand der &uuml;bergebenen Parameter.
	 * 
	 * @param column Die Tabellenspalte, zu der der Domain-String erzeugt werden soll.
	 * @param dsm    Der Domain Show Mode.
	 * @throws IllegalArgumentException Falls eine der Vorbedingungen verletzt wird.
	 * @precondition column != <TT>null</TT>
	 * 
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	public DomainStringBuilder(TabellenspaltenModel column, DomainShowMode dsm) throws IllegalArgumentException {
		super();
		ensure(column != null, "column cannot be null.");
		this.column = column;
		this.dsm = (dsm == null ? DomainShowMode.ALL : dsm);
	}

	/**
	 * Erzeugt einen individualisierten String zur Domain, der gegebenenfalls den Defaultwert der Tabellenspalte
	 * enth&auml;lt.
	 * 
	 * @return Ein individualisierter String zur Domain, der gegebenenfalls den Defaultwert der Tabellenspalte
	 *         enth&auml;lt.
	 * 
	 * @changed OLI 14.06.2011 - Hinzugef&uuml;gt.
	 */
	public String build() {
		DomainModel d = this.column.getDomain();
		String defaultValue = this.column.getDefaultValue();
		StringBuffer sb = new StringBuffer();
		if (d != null) {
			sb.append(getDomainAndTypeName(d));
			if ((defaultValue != null) && (defaultValue.length() > 0) && !defaultValue.equals("NULL")) {
				sb.append(" - ").append(defaultValue);
			}
		} else {
			sb.append("<null>");
		}
		return sb.toString();
	}

	private String getDomainAndTypeName(DomainModel d) {
		String s = "";
		if ((dsm == DomainShowMode.ALL) || (dsm == DomainShowMode.DOMAIN_NAME_ONLY)) {
			s = d.getName();
		}
		if ((dsm == DomainShowMode.ALL) || (dsm == DomainShowMode.SQL_TYPE_ONLY)) {
			boolean wasEmpty = true;
			if (!s.isEmpty()) {
				wasEmpty = false;
				s += " (";
			}
			s += d.getType();
			if (dsm == DomainShowMode.SQL_TYPE_ONLY) {
				s = s.toUpperCase();
			}
			if (!wasEmpty) {
				s += ")";
			}
		}
		return s;
	}

}