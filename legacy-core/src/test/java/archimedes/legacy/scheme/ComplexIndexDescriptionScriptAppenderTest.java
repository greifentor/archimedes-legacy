/*
 * ComplexIndexDescriptionScriptAppenderTest.java
 *
 * 21.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Vector;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.Utils;
import archimedes.model.IndexMetaData;
import archimedes.model.SimpleIndexMetaData;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;

/**
 * Tests zur Klasse <CODE>ComplexIndexDescriptionScriptAppender</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndexDescriptionScriptAppenderTest {

	private static final String COLUMN_NAME0 = "Column0";
	private static final String COLUMN_NAME1 = "Column1";
	private static final String INDEX_NAME = "Index0";
	private static final String TABLE_NAME = "Table0";

	private AttributeMetaData column0 = null;
	private AttributeMetaData column1 = null;
	private ClassMetaData table = null;
	private ComplexIndexDescriptionScriptAppender unitUnderTest = null;
	private List<String> description = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.column0 = Utils.createAttributeMetaDataMock(COLUMN_NAME0);
		this.column1 = Utils.createAttributeMetaDataMock(COLUMN_NAME1);
		this.table = Utils.createClassMetaDataMock(TABLE_NAME, this.column0, this.column1);
		this.description = new Vector<String>();
		this.unitUnderTest = new ComplexIndexDescriptionScriptAppender(this.description);
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorThrowingAnExceptionIfPassingANullPointerAsScript() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ComplexIndexSQLScriptAppender(null);
		});
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testIndexAddedRegularRun() throws Exception {
		this.unitUnderTest.indexAdded(this.createIndexMetaData());
		assertEquals(2, this.description.size());
		assertEquals("<H2>Index " + INDEX_NAME + " angelegt</H2>", this.description.get(0));
		assertEquals("<P>Komplexer Index " + INDEX_NAME + " f&uuml;r Tabelle " + TABLE_NAME + " (" + COLUMN_NAME0 + ", "
				+ COLUMN_NAME1 + ") angelegt.", this.description.get(1));
	}

	private IndexMetaData createIndexMetaData() {
		IndexMetaData imd = new DefaultIndexMetaData(INDEX_NAME, this.table);
		imd.addColumn(this.column0);
		imd.addColumn(this.column1);
		return imd;
	}

	/**
	 * @changed OLI 21.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testIndexRemoveRegularRun() throws Exception {
		this.unitUnderTest.indexRemoved(this.createSimpleIndexMetaData());
		assertEquals("<H2>Index " + INDEX_NAME + " gel&ouml;scht</H2>", this.description.get(0));
		assertEquals("<P>Komplexer Index " + INDEX_NAME + " f&uuml;r Tabelle " + TABLE_NAME + " (" + COLUMN_NAME0 + ", "
				+ COLUMN_NAME1 + ") aus dem Modell " + "entfernt.", this.description.get(1));
	}

	private SimpleIndexMetaData createSimpleIndexMetaData() {
		SimpleIndexMetaData simd = new SimpleIndexMetaData(INDEX_NAME, TABLE_NAME);
		simd.addColumn(COLUMN_NAME0);
		simd.addColumn(COLUMN_NAME1);
		return simd;
	}

}