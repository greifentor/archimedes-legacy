/*
 * DefaultComment.java
 *
 * 06.10.2007
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import java.util.StringTokenizer;

import archimedes.legacy.app.ArchimedesDescriptorFactory;
import archimedes.legacy.model.DefaultCommentModel;
import archimedes.legacy.scheme.udschebtis.DefaultCommentUdschebti;

/**
 * Diese Tabelle enth&auml;lt Kommentare, die in der Beschreibung auf
 * Tabellenspalten angewandt werden sollen, auf deren Namen das angegebene
 * Muster pa&szlig;t.
 * 
 * @author ollie
 * 
 * @changed OLI 06.10.2007 - Einbeziehen der Klasse in die
 *          Archimedes-Applikationslogik.
 * @changed OLI 12.04.2010 - Formatanpassungen.
 */

public class DefaultComment extends DefaultCommentUdschebti implements DefaultCommentModel {

	private DefaultComment() {
		super(null);
	}

	/**
	 * Generiert eine Instanz der Klasse mit Defaultwerten.
	 * 
	 * @param adf
	 *            Die ArchimedesDescriptorFactory, aus der die Instanz ihre
	 *            Konfiguration beziehen soll.
	 * @param tn
	 *            Der Name der Tabelle, auf die sich die Klasse beziehen soll.
	 */
	protected DefaultComment(ArchimedesDescriptorFactory adf, String tn) {
		super(adf, tn);
		this.initialize();
	}

	/**
	 * Generiert eine Instanz der Klasse mit Defaultwerten.
	 * 
	 * @param adf
	 *            Die ArchimedesDescriptorFactory, aus der die Instanz ihre
	 *            Konfiguration beziehen soll.
	 */
	public DefaultComment(ArchimedesDescriptorFactory adf) {
		super(adf);
		this.initialize();
	}

	private void initialize() {
		if (this.adf != null) {
			this.setHAD(this.adf.getDynamicDescriptor("DefaultComment"));
			if (adf != null) {
				this.setComment("-/-");
				this.setPattern("");
			}
		}
	}

	/*
	 * Methoden zur Herstellung der Kompatibilit&auml;t mit der
	 * Vorg&auml;ngerklasse.
	 */

	/**
	 * @deprecated OLI 06.10.2007
	 */
	public String getKommentar() {
		return this.getComment();
	}

	/**
	 * @deprecated OLI 06.10.2007
	 */
	public void setKommentar(String kommentar) {
		this.setComment(kommentar);
	}

	/* Implementierung der abstrakten Methoden der Superklasse. */

	public Object createObject() {
		return new DefaultComment(this.adf);
	}

	/* Implementierung des Interfaces Comparable. */

	public int compareTo(Object o) {
		DefaultComment dc = (DefaultComment) o;
		return this.getPattern().compareTo(dc.getPattern());
	}

	/*
	 * Implementierung der fehlenden Methoden des Interfaces
	 * DefaultCommentModel.
	 */

	public String getDefaultComment() {
		return this.getComment();
	}

	/* Implementierung des Interfaces Selectable. */

	public boolean isSelected(Object[] criteria) throws IllegalArgumentException {
		if (((criteria != null) && (criteria.length > 0) && !(criteria[0] instanceof String))
				|| ((criteria != null) && (criteria.length > 1))) {
			throw new IllegalArgumentException("DefaultComment does only accept one " + "String-criteria!");
		} else if (criteria == null) {
			return true;
		}
		String c = null;
		String s = null;
		StringTokenizer st = new StringTokenizer((String) criteria[0]);
		while (st.hasMoreTokens()) {
			c = st.nextToken();
			s = this.getPattern().toLowerCase();
			if ((c == null) || (s.indexOf(c.toLowerCase()) < 0)) {
				return false;
			}
		}
		return true;
	}

}
