/*
 * ComplexIndexSQLScriptAppenderTest.java
 *
 * 20.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.Utils;
import archimedes.legacy.model.IndexMetaData;
import archimedes.legacy.model.SimpleIndexMetaData;
import archimedes.legacy.script.sql.SQLScript;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;

/**
 * Tests zur Klasse <CODE>ComplexIndexSQLScriptAppender</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
 */

public class ComplexIndexSQLScriptAppenderTest {

	private static final String COLUMN_NAME0 = "Column0";
	private static final String COLUMN_NAME1 = "Column1";
	private static final String INDEX_NAME = "Index0";
	private static final String TABLE_NAME = "Table0";

	private AttributeMetaData column0 = null;
	private AttributeMetaData column1 = null;
	private ClassMetaData table = null;
	private ComplexIndexSQLScriptAppender unitUnderTest = null;
	private SQLScript script = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.column0 = Utils.createAttributeMetaDataMock(COLUMN_NAME0);
		this.column1 = Utils.createAttributeMetaDataMock(COLUMN_NAME1);
		this.table = Utils.createClassMetaDataMock(TABLE_NAME, this.column0, this.column1);
		this.script = new SQLScript();
		this.unitUnderTest = new ComplexIndexSQLScriptAppender(this.script);
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorThrowingAnExceptionIfPassingANullPointerAsScript() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new ComplexIndexSQLScriptAppender(null);
		});
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testIndexAddedRegularRun() throws Exception {
		this.unitUnderTest.indexAdded(this.createIndexMetaData());
		assertEquals(1, this.script.size());
		assertEquals("CREATE INDEX Index0 ON Table0 (Column0, Column1);", this.script.getReducingStatements()[0]);
	}

	private IndexMetaData createIndexMetaData() {
		IndexMetaData imd = new DefaultIndexMetaData(INDEX_NAME, this.table);
		imd.addColumn(this.column0);
		imd.addColumn(this.column1);
		return imd;
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testIndexRemoveRegularRun() throws Exception {
		this.unitUnderTest.indexRemoved(this.createSimpleIndexMetaData());
		assertEquals(1, this.script.size());
		assertEquals("DROP INDEX Index0;", this.script.getExtendingStatements()[0]);
	}

	private SimpleIndexMetaData createSimpleIndexMetaData() {
		SimpleIndexMetaData simd = new SimpleIndexMetaData(INDEX_NAME, TABLE_NAME);
		simd.addColumn(COLUMN_NAME0);
		simd.addColumn(COLUMN_NAME1);
		return simd;
	}

}