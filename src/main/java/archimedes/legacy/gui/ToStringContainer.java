/*
 * ToStringContainer.java
 *
 * 29.07.2004
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.gui;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.model.ToStringContainerModel;
import archimedes.legacy.scheme.Tabellenspalte;
import corent.base.Attributed;
import corent.base.StrUtil;
import corent.djinn.DefaultComponentFactory;
import corent.djinn.DefaultEditorDescriptor;
import corent.djinn.DefaultEditorDescriptorList;
import corent.djinn.DefaultLabelFactory;
import corent.djinn.Editable;
import corent.djinn.EditorDescriptorList;

/**
 * Diese Klasse dient zum Editieren der ToString-Parameter f&uuml;r die
 * Codegenerierung.<BR>
 * <HR>
 * 
 * @author ollie
 * 
 */

public class ToStringContainer implements Attributed, Editable, ToStringContainerModel {

	/** Bezeichner zum Zugriff auf die Eigenschaft Prefix. */
	public static final int ID_PREFIX = 0;
	/** Bezeichner zum Zugriff auf die Eigenschaft Suffix. */
	public static final int ID_SUFFIX = 1;
	/** Bezeichner zum Zugriff auf die Eigenschaft Tabellenspalte. */
	public static final int ID_TABELLENSPALTE = 2;

	/* Der Pr&auml;fix, der vor den String montiert werden soll. */
	private String prefix = "";
	/* Der Suffix, der vor den String montiert werden soll. */
	private String suffix = "";
	/* Die Tabelle, zu der der Container geh&ouml;ren. */
	private TabellenModel tm = null;
	/* Die Tabellenspalte deren Inhalt in den String einfliessen soll. */
	private TabellenspaltenModel tsm = null;

	/** Generiert eine Instanz der Klasse mit den Defaultwerten. */
	public ToStringContainer() {
		super();
	}

	/**
	 * Generiert eine Instanz der Klasse aus den &uuml;bergebenen Parametern.
	 * 
	 * @param tsm
	 *            Ein TabellenspaltenModel.
	 * @param tm
	 *            Das TabellenModel, zu dem der Container geh&ouml;ren soll.
	 */
	public ToStringContainer(TabellenspaltenModel tsm, TabellenModel tm) {
		super();
		this.tm = tm;
		this.tsm = tsm;
	}

	/**
	 * Generiert eine Instanz der Klasse aus den &uuml;bergebenen Parametern.
	 * 
	 * @param tsm
	 *            Ein TabellenspaltenModel.
	 * @param tm
	 *            Das TabellenModel, zu dem der Container geh&ouml;ren soll.
	 * @param prefix
	 *            Ein Default f&uuml;r das P&auml;fix des Containers.
	 * @param suffix
	 *            Ein Default f&uuml;r das Suffix des Containers.
	 */
	public ToStringContainer(TabellenspaltenModel tsm, TabellenModel tm, String prefix, String suffix) {
		super();
		this.tm = tm;
		this.tsm = tsm;
		this.setPrefix(prefix);
		this.setSuffix(suffix);
	}

	/**
	 * Creates a new toString container with the passed parameters.
	 * 
	 * @param column
	 *            The column which the container represents.
	 * @param prefix
	 *            A prefix for string generation. This will be shown on left
	 *            side of the columns value.
	 * @param suffix
	 *            A suffix for string generation. This will be shown on right
	 *            side of the columns value.
	 * 
	 * @changed OLI 12.06.2014 - Added.
	 */
	public ToStringContainer(ColumnModel column, String prefix, String suffix) {
		super();
		this.tm = (TabellenModel) column.getTable();
		this.tsm = (TabellenspaltenModel) column;
		this.setPrefix(prefix);
		this.setSuffix(suffix);
	}

	/* Accessoren und Mutatoren. */

	/** @return Der Wert der Eigenschaft Prefix. */
	public String getPrefix() {
		return this.prefix;
	}

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r die Eigenschaft
	 * Prefix.
	 * 
	 * @param prefix
	 *            Der neue Wert der Eigenschaft Prefix.
	 */
	public void setPrefix(String prefix) {
		if (prefix == null) {
			this.prefix = "";
		}
		this.prefix = prefix;
	}

	/** @return Der Wert der Eigenschaft Suffix. */
	public String getSuffix() {
		return this.suffix;
	}

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r die Eigenschaft
	 * Suffix.
	 * 
	 * @param suffix
	 *            Der neue Wert der Eigenschaft Suffix.
	 */
	public void setSuffix(String suffix) {
		if (suffix == null) {
			this.suffix = "";
		}
		this.suffix = suffix;
	}

	/**
	 * @return Der Wert der Eigenschaft Tabellenspalte.
	 * 
	 * @deprecated OLI 05.10.2015 - Use "getColumn()" instead of.
	 */
	@Deprecated
	public TabellenspaltenModel getTabellenspalte() {
		return this.tsm;
	}

	/**
	 * Returns the column which the to string container represents.
	 * 
	 * @return The column which the to string container represents.
	 * 
	 * @changed OLI 05.10.2015 - Added.
	 */
	public ColumnModel getColumn() {
		return this.tsm;
	}

	/**
	 * Setzt den &uuml;bergebenen Wert als neuen Wert f&uuml;r die Eigenschaft
	 * Tabellenspalte.
	 * 
	 * @param tabellenspalte
	 *            Der neue Wert der Eigenschaft Tabellenspalte.
	 */
	public void setTabellenspalte(TabellenspaltenModel tabellenspalte) {
		this.tsm = tabellenspalte;
	}

	/* Ueberschreiben von Methoden der Superklasse. */

	public boolean equals(Object o) {
		if (!(o instanceof ToStringContainer)) {
			return false;
		}
		ToStringContainer tsc = (ToStringContainer) o;
		return this.getPrefix().equals(tsc.getPrefix()) && this.getSuffix().equals(tsc.getSuffix())
				&& (this.getTabellenspalte().equals(tsc.getTabellenspalte()));
	}

	public String toString() {
		return this.getPrefix() + this.getTabellenspalte() + this.getSuffix();
	}

	/* Implementierung des Interfaces Attributed. */

	public Object get(int id) throws IllegalArgumentException {
		switch (id) {
		case ID_PREFIX:
			return this.getPrefix();
		case ID_SUFFIX:
			return this.getSuffix();
		case ID_TABELLENSPALTE:
			return this.getTabellenspalte();
		}
		throw new IllegalArgumentException("Klasse ToStringContainer verfuegt nicht ueber ein " + "Attribut " + id
				+ " (get)!");
	}

	public void set(int id, Object value) throws ClassCastException, IllegalArgumentException {
		switch (id) {
		case ID_PREFIX:
			this.setPrefix((String) value);
			return;
		case ID_SUFFIX:
			this.setSuffix((String) value);
			return;
		case ID_TABELLENSPALTE:
			this.setTabellenspalte((TabellenspaltenModel) value);
			return;
		}
		throw new IllegalArgumentException("Klasse ToStringContainer verfuegt nicht ueber ein " + "Attribut " + id
				+ " (set)!");
	}

	/* Implementierung des Interfaces Editable. */

	public EditorDescriptorList getEditorDescriptorList() {
		DefaultComponentFactory dcf = DefaultComponentFactory.INSTANZ;
		DefaultComponentFactory dcfts = new DefaultComponentFactory(this.tm.getTabellenspalten());
		DefaultEditorDescriptorList dedl = new DefaultEditorDescriptorList();
		DefaultLabelFactory dlf = DefaultLabelFactory.INSTANZ;
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_PREFIX, dlf, dcf, StrUtil.FromHTML("Pr&auml;fix"), 'P',
				null, StrUtil.FromHTML("Der Pr&auml;fix " + "bei der Stringbildung")));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_TABELLENSPALTE, dlf, dcfts, "Tabellenspalte", 'T',
				null, "Die Tabellenspalte, deren Daten in den "
						+ "Stringbildung (an dieser Position) einfliessen sollen"));
		dedl.addElement(new DefaultEditorDescriptor(0, this, ID_SUFFIX, dlf, dcf, "Suffix", 'S', null,
				"Der Suffix bei der Stringbildung"));
		return dedl;
	}

	public Object createObject() {
		return new ToStringContainer();
	}

	public Object createObject(Object blueprint) throws ClassCastException {
		if (!(blueprint instanceof Tabellenspalte)) {
			throw new ClassCastException("Instance of ToStringContainer required!");
		}
		return null;
	}

}
