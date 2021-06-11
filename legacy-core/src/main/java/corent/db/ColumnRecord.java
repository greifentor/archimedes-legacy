/*
 * ColumnRecord.java
 *
 * 23.04.2004
 *
 * (c) by O.Lieshoff
 *
 */

package corent.db;

import java.io.Serializable;

/**
 * Diese Klasse enth&auml;lt die zu einer Tabellenspalte notwendigen Informationen und Methoden, um diese Informationen
 * darzustellen.
 *
 * @author O.Lieshoff
 *
 */

public class ColumnRecord implements Serializable {

	/*
	 * Diese Flagge mu&szlig; gesetzt werden, wenn die Daten der Spalte kodiert gespeichert sind.
	 */
	private boolean kodiert = false;
	/* Diese Flagge mu&szlig; gesetzt werden, wenn die Spalte ein PK-Member ist. */
	private boolean pkmember = false;
	/*
	 * Hier kann eine Klasse angegeben werden, in der Wert der Tabellenspalte beim Lesen konvertiert werden soll.
	 */
	private Class convertto = null;
	/*
	 * Der Attributbezeichner des Attributes, das in der Tabellenspalte gespeichert werden soll.
	 */
	private int attribute = -1;
	/* Der Name der Spalte. */
	private String columnname = null;
	/* Der Name der Tabelle, zu der die Tabellenspalte geh&ouml;rt. */
	private String tablename = null;

	/**
	 * Generiert einen ColumnRecord anhand der &uuml;bergebenen Werte.
	 *
	 * @param attr       Der Attributbezeichner des in der Tabellenspalte untergebrachten Attributs.
	 * @param tablename  Der Name der Tabelle, zu der die Tabellenspalte geh&ouml;rt.
	 * @param columnname Der eigentliche Spaltenname.
	 * @throws NullPointerException Falls einer der beiden Werte mit einem <TT>null</TT>-Wert belegt werden soll.
	 */
	public ColumnRecord(int attr, String tablename, String columnname) {
		this(attr, tablename, columnname, false);
	}

	/**
	 * Generiert einen ColumnRecord anhand der &uuml;bergebenen Werte.
	 *
	 * @param attr       Der Attributbezeichner des in der Tabellenspalte untergebrachten Attributs.
	 * @param tablename  Der Name der Tabelle, zu der die Tabellenspalte geh&ouml;rt.
	 * @param columnname Der eigentliche Spaltenname.
	 * @param pkmember   Diese Flagge mu&szlig; gesetzt werden, wenn der ColumnRecord ein Teil des
	 *                   Prim&auml;rschl&uuml;ssels der Tabelle ist.
	 * @throws NullPointerException Falls einer der beiden Werte mit einem <TT>null</TT>-Wert belegt werden soll.
	 */
	public ColumnRecord(int attr, String tablename, String columnname, boolean pkmember) {
		super();
		this.setAttribute(attr);
		this.setColumnname(columnname);
		this.setPkMember(pkmember);
		this.setTablename(tablename);
	}

	/**
	 * Liefert den Wert der Eigenschaft Kodiert.
	 *
	 * @return Der Wert der Eigenschaft Kodiert.
	 */
	public boolean isKodiert() {
		return this.kodiert;
	}

	/**
	 * &Auml;ndert den Wert der Eigenschaft Kodiert.
	 *
	 * @param kodiert Der neue Wert f&uuml;r die Eigenschaft Kodiert.
	 */
	public void setKodiert(boolean kodiert) {
		this.kodiert = kodiert;
	}

	/**
	 * Liefert den Wert der Eigenschaft Pkmember.
	 *
	 * @return Der Wert der Eigenschaft Pkmember.
	 */
	public boolean isPkMember() {
		return this.pkmember;
	}

	/**
	 * &Auml;ndert den Wert der Eigenschaft Pkmember.
	 *
	 * @param pkmember Der neue Wert f&uuml;r die Eigenschaft Pkmember.
	 */
	public void setPkMember(boolean pkmember) {
		this.pkmember = pkmember;
	}

	/**
	 * Liefert den Wert der Eigenschaft ConvertTo.
	 *
	 * @return Der Wert der Eigenschaft ConvertTo.
	 */
	public Class getConvertTo() {
		return this.convertto;
	}

	/**
	 * &Auml;ndert den Wert der Eigenschaft ConvertTo.
	 *
	 * @param convertto Der neue Wert f&uuml;r die Eigenschaft ConvertTo.
	 */
	public void setConvertTo(Class convertto) {
		this.convertto = convertto;
	}

	/**
	 * Liefert den Wert der Eigenschaft Attribute.
	 *
	 * @return Der Wert der Eigenschaft Attribute.
	 */
	public int getAttribute() {
		return this.attribute;
	}

	/**
	 * &Auml;ndert den Wert der Eigenschaft Attribute.
	 *
	 * @param attribute Der neue Wert f&uuml;r die Eigenschaft Attribute.
	 * @throws IllegalArgumentException Wenn versucht wird, der Eigenschaft einen Wert kleiner 0 zu &uum;bergeben.
	 */
	public void setAttribute(int attribute) {
		if (attribute < 0) {
			throw new NullPointerException("ColumnRecord.Attribute can not be negative!");
		}
		this.attribute = attribute;
	}

	/**
	 * Liefert den Wert der Eigenschaft Columnname.
	 *
	 * @return Der Wert der Eigenschaft Columnname.
	 */
	public String getColumnname() {
		return this.columnname;
	}

	/**
	 * &Auml;ndert den Wert der Eigenschaft Columnname.
	 *
	 * @param columnname Der neue Wert f&uuml;r die Eigenschaft Columnname.
	 * @throws IllegalArgumentException Wenn versucht wird, der Eigenschaft einen leeren String zu &uum;bergeben.
	 * @throws NullPointerException     Wenn versucht wird, der Eigenschaft einen <TT>null</TT>-Wert zu &uum;bergeben.
	 */
	public void setColumnname(String columnname) {
		if (columnname == null) {
			throw new NullPointerException("ColumnRecord.Columnname can not be null!");
		}
		if (columnname.length() < 1) {
			throw new IllegalArgumentException(
					"ColumnRecord.Columnname is able to have a " + "minimum length of 1 character!");
		}
		this.columnname = columnname;
	}

	/**
	 * Liefert den Wert der Eigenschaft Tablename.
	 *
	 * @return Der Wert der Eigenschaft Tablename.
	 */
	public String getTablename() {
		return this.tablename;
	}

	/**
	 * &Auml;ndert den Wert der Eigenschaft Tablename.
	 *
	 * @param tablename Der neue Wert f&uuml;r die Eigenschaft Tablename.
	 * @throws IllegalArgumentException Wenn versucht wird, der Eigenschaft einen leeren String zu &uum;bergeben.
	 * @throws NullPointerException     Wenn versucht wird, der Eigenschaft einen <TT>null</TT>-Wert zu &uum;bergeben.
	 */
	public void setTablename(String tablename) {
		if (tablename == null) {
			throw new NullPointerException("ColumnRecord.Tablename can not be null!");
		}
		if (tablename.length() < 1) {
			throw new IllegalArgumentException(
					"ColumnRecord.Tablename is able to have a " + "minimum length of 1 character!");
		}
		this.tablename = tablename;
	}

	/** @return Der volle Name der beschriebenen Tabellenspalte. */
	public String getFullname() {
		return this.getTablename() + "." + this.getColumnname();
	}

	/* Ueberschreiben von Methoden der Superklasse. */

	public boolean equals(Object o) {
		if (!(o instanceof ColumnRecord)) {
			return false;
		}
		ColumnRecord cr = (ColumnRecord) o;
		return this.getTablename().equals(cr.getTablename()) && this.getColumnname().equals(cr.getColumnname());
	}

	public String toString() {
		return new StringBuffer(this.getTablename()).append(".").append(this.getColumnname()).toString();
	}

}
