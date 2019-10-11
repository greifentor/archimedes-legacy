/*
 * Utils.java
 *
 * 19.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy;

import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import org.easymock.EasyMock;

import archimedes.legacy.scheme.DefaultIndexMetaData;
import archimedes.model.IndexMetaData;

/**
 * Eine Sammlung von Utility-Methoden, die in mehreren Packages Anwendung
 * finden.
 * 
 * @author ollie
 * 
 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
 */

public class Utils {

	/**
	 * Erzeugt ein AttributeMetaDataMock mit dem angegebenen Namen.
	 * 
	 * @param name
	 *            Der Name der Tabellespalte zu der das Mock erzeugt werden
	 *            soll.
	 * @return Ein Mock-Objekt mit dem angegebenen Namen.
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public static AttributeMetaData createAttributeMetaDataMock(String name) {
		AttributeMetaData amd = EasyMock.createMock(AttributeMetaData.class);
		EasyMock.expect(amd.getName()).andReturn(name).anyTimes();
		EasyMock.replay(amd);
		return amd;
	}

	/**
	 * Erzeugt ein ClassMetaDataMock mit dem angegebenen Namen.
	 * 
	 * @param name
	 *            Der Name der Tabelle zu der das Mock erzeugt werden soll.
	 * @return Ein Mock-Objekt mit dem angegebenen Namen.
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public static ClassMetaData createClassMetaDataMock(String name) {
		return createClassMetaDataMock(name, new Vector<AttributeMetaData>());
	}

	/**
	 * Erzeugt ein ClassMetaDataMock mit dem angegebenen Namen und den
	 * angegebenen Attributen.
	 * 
	 * @param name
	 *            Der Name der Tabelle zu der das Mock erzeugt werden soll.
	 * @param attributes
	 *            Die Attribute, die an die Tabelle angeh&auml;ngt werden
	 *            sollen.
	 * @return Ein Mock-Objekt mit dem angegebenen Namen und den Attributen der
	 *         Liste.
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public static ClassMetaData createClassMetaDataMock(String name, AttributeMetaData... attributes) {
		return createClassMetaDataMock(name, Arrays.asList(attributes));
	}

	/**
	 * Erzeugt ein ClassMetaDataMock mit dem angegebenen Namen und den
	 * angegebenen Attributen.
	 * 
	 * @param name
	 *            Der Name der Tabelle zu der das Mock erzeugt werden soll.
	 * @param attributes
	 *            Die Attribute, die an die Tabelle angeh&auml;ngt werden
	 *            sollen.
	 * @return Ein Mock-Objekt mit dem angegebenen Namen und den Attributen der
	 *         Liste.
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public static ClassMetaData createClassMetaDataMock(String name, List<AttributeMetaData> attributes) {
		ClassMetaData cmd = EasyMock.createMock(ClassMetaData.class);
		EasyMock.expect(cmd.getName()).andReturn(name).anyTimes();
		for (int i = 0, leni = attributes.size(); i < leni; i++) {
			EasyMock.expect(cmd.getAttribute(i)).andReturn(attributes.get(i)).anyTimes();
			EasyMock.expect(cmd.getAttribute(attributes.get(i).getName())).andReturn(attributes.get(i)).anyTimes();
		}
		EasyMock.expect(cmd.getAttributes()).andReturn(attributes).anyTimes();
		EasyMock.replay(cmd);
		return cmd;
	}

	/**
	 * Erzeugt ein IndexMetaData-Mock mit dem angegebenen Namen zur angegebenen
	 * Tabelle mit den entsprechenden, gesetzten Spalten.
	 * 
	 * @param indexName
	 *            Der Name des Index.
	 * @param table
	 *            Die Tabelle, zu der der Index erzeugt werden soll.
	 * @param columnsSet
	 *            Die Spalten, die in den Index mit einbezogen werden sollen.
	 * 
	 * @changed OLI 19.12.2011 - Hinzugef&uuml;gt.
	 */
	public static IndexMetaData createIndexMetaData(String indexName, ClassMetaData table,
			AttributeMetaData... columnsSet) {
		IndexMetaData imd = new DefaultIndexMetaData(indexName, table);
		for (int i = 0; i < columnsSet.length; i++) {
			imd.addColumn(columnsSet[i]);
		}
		return imd;
	}

}