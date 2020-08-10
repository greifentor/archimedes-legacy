/*
 * DefaultIndexMetaData.java
 *
 * 14.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static corentx.util.Checks.ensureNotEmpty;
import static corentx.util.Checks.ensureNotNull;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;

import java.util.List;
import java.util.Vector;

import archimedes.legacy.model.IndexMetaData;

/**
 * Standardimplementierung des <CODE>IndexMetaData</CODE> Interfaces zur Nutzung
 * in der Archimedes-Applikation.
 * 
 * @author ollie
 * 
 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
 */

public class DefaultIndexMetaData implements IndexMetaData {

	private List<AttributeMetaData> columns = new Vector<AttributeMetaData>();
	private String name = null;
	private ClassMetaData table = null;

	/**
	 * Erzeugt ein neues IndexMetaData-Objekt mit den angegebenen Parametern.
	 * 
	 * @param name
	 *            Der Name, unter dem der Index erzeugt werden soll.
	 * @param table
	 *            Die Tabelle, f&uuml;r die der Index erzeugt werden soll.
	 * @throws IllegalArgumentException
	 *             Falls eine der Vorbedingungen verletzt wird.
	 * @precondition name != <CODE>null</CODE>
	 * @precondition !name.isEmpty()
	 * @precondition table != <CODE>null</CODE>
	 * 
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	public DefaultIndexMetaData(String name, ClassMetaData table) throws IllegalArgumentException {
		super();
		this.setName(name);
		this.setTable(table);
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void addColumn(AttributeMetaData column) throws IllegalArgumentException {
		ensureNotNull(column, "column name cannot be null.");
		this.columns.add(column);
	}

	/**
	 * @changed OLI 16.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void clearColumns() {
		this.columns.clear();
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public int compareTo(Object o) {
		IndexMetaData imd = (IndexMetaData) o;
		return this.getName().toLowerCase().compareTo(imd.getName().toLowerCase());
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public AttributeMetaData[] getColumns() {
		return this.columns.toArray(new AttributeMetaData[] {});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public String getName() {
		return this.name;
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public ClassMetaData getTable() {
		return this.table;
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public boolean isMember(String columnName) throws IllegalArgumentException {
		ensureNotEmpty(columnName, "column name can neither be null nor empty.");
		for (AttributeMetaData amd : this.columns) {
			if (amd.getName().equals(columnName)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void removeColumn(AttributeMetaData column) throws IllegalArgumentException {
		ensureNotNull(column, "column name cannot be null.");
		for (int i = 0, leni = this.columns.size(); i < leni; i++) {
			if (this.columns.get(i).getName().equals(column.getName())) {
				this.columns.remove(i);
				return;
			}
		}
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void setName(String name) throws IllegalArgumentException {
		ensureNotEmpty(name, "index name can neither be null nor empty.");
		this.name = name;
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public void setTable(ClassMetaData table) throws IllegalArgumentException {
		ensureNotNull(table, "table cannot be null.");
		this.table = table;
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("Name=").append(this.getName());
		sb.append(", Table=").append(this.getTable().getName());
		sb.append(", Columns=[");
		for (int i = 0; i < this.getColumns().length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(this.getColumns()[i].getName());
		}
		sb.append("]");
		return sb.toString();
	}

}