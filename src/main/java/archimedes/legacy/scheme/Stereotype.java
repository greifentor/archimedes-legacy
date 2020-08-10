/*
 * Stereotype.java
 *
 * 30.04.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import java.util.StringTokenizer;

import archimedes.legacy.Archimedes;
import archimedes.legacy.gui.CommentSubEditorFactory;
import archimedes.legacy.gui.HistoryOwnerSubEditorFactory;
import archimedes.legacy.model.StereotypeModel;
import corent.base.Attributed;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.DefaultSubEditorDescriptor;
import corent.djinn.DefaultTabDescriptor;
import corent.djinn.DefaultTabbedPaneFactory;
import corent.djinn.EditorDescriptorList;
import corent.djinn.TabDescriptor;
import corent.djinn.TabbedEditable;
import corent.djinn.TabbedPaneFactory;

/**
 * Diese Klasse repr&auml;sentiert eine Stereotype f&uuml;r Tabellen des
 * Archimedes-Systems.
 * 
 * @author ollie
 *         <P>
 * 
 * @changed OLI 29.08.2007 - Erweiterung um die Implementierung der Methode
 *          <TT>isHideTable</TT> nach &Auml;nderung des Interfaces
 *          <TT>StereotypeModel</TT>.
 *          <P>
 *          OLI 11.05.2008 - Erweiterung der Implementierung des Interfaces
 *          <TT>TabbedEditable</TT> um die Methode <TT>isTabEnabled(int)</TT>.
 * 
 */

public class Stereotype implements Attributed, StereotypeModel, TabbedEditable {

	/** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft Bezeichnung. */
	public static final int ID_BEZEICHNUNG = 0;
	/** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft Kommentar. */
	public static final int ID_COMMENT = 1;
	/** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft HideTable. */
	public static final int ID_HIDETABLE = 2;
	/** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft DoNotPrint. */
	public static final int ID_DONOTPRINT = 3;
	/** Bezeichner f&uuml;r den Zugriff auf die Eigenschaft Historie. */
	public static final int ID_HISTORY = 4;

	/*
	 * Die Flagge, die dar&uuml;ber entscheidet, ob die Tabelle zur Stereotype
	 * in Ausdrucken und Grafikexporten angezeigt werden soll.
	 */
	private boolean doNotPrint = false;
	/*
	 * Die Flagge, die dar&uuml;ber entscheidet, ob die Tabelle zur Stereotype
	 * angezeigt werden soll.
	 */
	private boolean hideTable = false;
	/* Eine Bezeichnung f&uuml;r die Stereotype. */
	private String bezeichnung = "";
	/* Eine Historie zur Stereotype. */
	private String historie = "";
	/* Ein Kommentar zur Stereotype. */
	private String comment = "-/-";

	/** Generiert eine Instanz der Klasse mit Defaultwerten. */
	public Stereotype() {
		super();
	}

	/**
	 * Generiert eine Instanz der Klasse mit Defaultwerten.
	 * 
	 * @param bezeichnung
	 *            Eine Bezeichnung f&uuml;r die Stereotype.
	 * @param comment
	 *            A comment to the Stereotype.
	 */
	public Stereotype(String bezeichnung, String comment) {
		super();
		this.setBezeichnung(bezeichnung);
		this.setComment(comment);
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public String getComment() {
		return this.comment;
	}

	/**
	 * @changed OLI 11.03.2016 - Added.
	 */
	@Override
	public void setComment(String comment) {
		if (comment == null) {
			comment = "-/-";
		}
		this.comment = comment;
	}

	/* Accessoren und Mutatoren. */

	/**
	 * Accessor f&uuml;r das Attribut Bezeichnung.
	 * 
	 * @return Der gegenw&auml;rtige Wert des Attributs Bezeichnung.
	 */
	public String getBezeichnung() {
		if (this.bezeichnung == null) {
			return null;
		}
		return this.bezeichnung;
	}

	/**
	 * Mutator f&uuml;r das Attribut Bezeichnung.
	 * 
	 * @param bezeichnung
	 *            Der neue Wert des Attributs Bezeichnung.
	 */
	public void setBezeichnung(String bezeichnung) {
		if (bezeichnung == null) {
			bezeichnung = "";
		}
		this.bezeichnung = bezeichnung;
	}

	/**
	 * Accessor f&uuml;r das Attribut DoNotPrint.
	 * 
	 * @return Der gegenw&auml;rtige Wert des Attributs DoNotPrint.
	 */
	public boolean isDoNotPrint() {
		return this.doNotPrint;
	}

	/**
	 * Mutator f&uuml;r das Attribut DoNotPrint.
	 * 
	 * @param doNotPrint
	 *            Der neue Wert des Attributs DoNotPrint.
	 */
	public void setDoNotPrint(boolean doNotPrint) {
		this.doNotPrint = doNotPrint;
	}

	/**
	 * Accessor f&uuml;r das Attribut HideTable.
	 * 
	 * @return Der gegenw&auml;rtige Wert des Attributs HideTable.
	 */
	public boolean isHideTable() {
		return this.hideTable;
	}

	/**
	 * Mutator f&uuml;r das Attribut HideTable.
	 * 
	 * @param hideTable
	 *            Der neue Wert des Attributs HideTable.
	 */
	public void setHideTable(boolean hideTable) {
		this.hideTable = hideTable;
	}

	@Override
	public String getHistory() {
		return this.historie;
	}

	@Override
	public void setHistory(String newHistory) {
		this.historie = newHistory;
	}

	/* Ueberschreiben von Methoden der Superklasse. */

	public boolean equals(Object o) {
		if (!(o instanceof Stereotype)) {
			return false;
		}
		Stereotype s = (Stereotype) o;
		return this.getBezeichnung().equals(s.getBezeichnung());
	}

	public int hashCode() {
		int result = 17;
		result = 37 * result + this.getComment().hashCode();
		return result;
	}

	public String toString() {
		StringBuffer sb = new StringBuffer("").append(getBezeichnung());
		return sb.toString();
	}

	/* Implementierung des Interfaces StereotypeModel. */

	public String getName() {
		return this.getBezeichnung();
	}

	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_BEZEICHNUNG:
			return this.getBezeichnung();
		case ID_COMMENT:
			return this.getComment();
		case ID_HIDETABLE:
			return new Boolean(this.isHideTable());
		case ID_DONOTPRINT:
			return new Boolean(this.isDoNotPrint());
		case ID_HISTORY:
			return this.getHistory();
		}
		throw new IllegalArgumentException("Klasse Stereotype verfuegt nicht ueber ein Attribut" + " " + id + " (get)!");
	}

	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_BEZEICHNUNG:
			this.setBezeichnung((String) value);
			return;
		case ID_COMMENT:
			this.setComment((String) value);
			return;
		case ID_HIDETABLE:
			this.setHideTable((((Boolean) value).booleanValue()));
			return;
		case ID_DONOTPRINT:
			this.setDoNotPrint((((Boolean) value).booleanValue()));
			return;
		case ID_HISTORY:
			this.setHistory((String) value);
			return;
		}
		throw new IllegalArgumentException("Klasse Stereotype verfuegt nicht ueber ein Attribut" + " " + id + " (set)!");
	}

	public int compareTo(Object o) {
		Stereotype st = (Stereotype) o;
		return this.getBezeichnung().compareTo(st.getBezeichnung());
	}

	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_BEZEICHNUNG, dlf, dcf, "Bezeichnung", 'B', null,
				"Der Name der Stereotype"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_HIDETABLE, dlf, dcf, "Tabelle verbergen", 'V', null,
				"Setzen Sie diese Flagge, wenn die Tabelle " + "im Diagramm verborgen werden soll."));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_DONOTPRINT, dlf, dcf, "Tabelle nicht drucken", 'D',
				null, "Setzen Sie diese Flagge, wenn die Tabelle "
						+ "nur in Ausdrucken und Grafikexporten verborgen werden soll."));
		dedl.addElement(new DefaultSubEditorDescriptor(1, this, new CommentSubEditorFactory()));
		dedl.addElement(new DefaultSubEditorDescriptor(2, this, new HistoryOwnerSubEditorFactory()));
		return dedl;
	}

	public Object createObject() {
		// return new Stereotype();
		return Archimedes.Factory.createStereotype();
	}

	public Object createObject(Object blueprint) throws ClassCastException {
		if (!(blueprint instanceof Stereotype)) {
			throw new ClassCastException("Instance of Stereotype required!");
		}
		Stereotype st = (Stereotype) this.createObject();
		st.setBezeichnung(this.getBezeichnung());
		st.setComment(this.getComment());
		return st;
	}

	public TabbedPaneFactory getTabbedPaneFactory() {
		return new DefaultTabbedPaneFactory(new TabDescriptor[] { new DefaultTabDescriptor("Daten", 'A', null),
				new DefaultTabDescriptor("Beschreibung", 'B', null), new DefaultTabDescriptor("Historie", 'H', null) });
	}

	public boolean isTabEnabled(int no) {
		return true;
	}

	public boolean isSelected(Object[] criteria) throws IllegalArgumentException {
		if (((criteria != null) && (criteria.length > 0) && !(criteria[0] instanceof String))
				|| ((criteria != null) && (criteria.length > 1))) {
			throw new IllegalArgumentException("Stereotype does only except one " + "String-criteria!");
		} else if (criteria == null) {
			return true;
		}
		StringTokenizer st = new StringTokenizer((String) criteria[0]);
		while (st.hasMoreTokens()) {
			String c = st.nextToken();
			String s = this.getBezeichnung().toLowerCase();
			if ((c == null) || (s.indexOf(c.toLowerCase()) < 0)) {
				return false;
			}
		}
		return true;
	}

}