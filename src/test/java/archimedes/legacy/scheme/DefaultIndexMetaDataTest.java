/*
 * DefaultIndexMetaData.java
 *
 * 14.12.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.Utils;
import gengen.metadata.AttributeMetaData;
import gengen.metadata.ClassMetaData;

/**
 * Tests zur Klasse <CODE>DefaultIndexMetaData</CODE>.
 *
 * @author ollie
 *
 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
 */

public class DefaultIndexMetaDataTest {

	private static final String ANOTHER_INDEX_NAME = "AnotherIndex";
	private static final String COLUMN_NAME0 = "Column0";
	private static final String COLUMN_NAME1 = "Column1";
	private static final String INDEX_NAME = "Index";
	private static final String TABLE_NAME = "Table";

	private AttributeMetaData column = null;
	private ClassMetaData table = null;
	private DefaultIndexMetaData unitUnderTest = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.column = Utils.createAttributeMetaDataMock(COLUMN_NAME0);
		this.table = Utils.createClassMetaDataMock(TABLE_NAME, this.column);
		this.unitUnderTest = new DefaultIndexMetaData(INDEX_NAME, this.table);
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testAddColumnThrowsAnExceptionPassingANullPointerAsColumnName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.addColumn(null);
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testAddColumnAddsThePassedColumnNameToTheList() throws Exception {
		assertEquals(0, this.unitUnderTest.getColumns().length);
		this.fillAndCheckColumnNameListWithColumnName0();
	}

	private void fillAndCheckColumnNameListWithColumnName0() {
		this.unitUnderTest.addColumn(this.column);
		assertEquals(1, this.unitUnderTest.getColumns().length);
		assertEquals(this.column, this.unitUnderTest.getColumns()[0]);
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCompareToIsEqualToItSelf() throws Exception {
		assertEquals(0, this.unitUnderTest.compareTo(this.unitUnderTest));
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCompareToPassingAnotherImplementationOfTheIndexMetaDataInterface() throws Exception {
		assertEquals(0, this.unitUnderTest.compareTo(Utils.createIndexMetaData(INDEX_NAME, this.table)));
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCompareToThrowsAnExceptionPassingANonIndexMetaDataObject() throws Exception {
		Assertions.assertThrows(ClassCastException.class, () -> {
			this.unitUnderTest.compareTo(":o)");
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCompareToWithAGreateIndexObject() throws Exception {
		assertTrue(this.unitUnderTest.compareTo(new DefaultIndexMetaData("Z", this.table)) < 0);
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCompareToWithALesserIndexObject() throws Exception {
		assertTrue(this.unitUnderTest.compareTo(new DefaultIndexMetaData("A", this.table)) > 0);
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCompareToWithAnEqualIndexObject() throws Exception {
		assertEquals(0, this.unitUnderTest.compareTo(new DefaultIndexMetaData(INDEX_NAME, this.table)));
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorThrowsExceptionPassingAnEmptyIndexName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new DefaultIndexMetaData("", this.table);
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorThrowsExceptionPassingANullPointerAsIndexName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new DefaultIndexMetaData(null, this.table);
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorThrowsExceptionPassingANullPointerAsTableName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new DefaultIndexMetaData(INDEX_NAME, null);
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetName() throws Exception {
		assertEquals(INDEX_NAME, this.unitUnderTest.getName());
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetTable() {
		assertEquals(this.table, this.unitUnderTest.getTable());
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testIsMemberThrowingAnExceptionPassingAnEmptyStringAsColumnName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.isMember("");
		});
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testIsMemberThrowingAnExceptionPassingANullPointerAsColumnName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.isMember(null);
		});
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testIsMemberRegularRunWithOnAnExistingColumn() throws Exception {
		this.unitUnderTest.addColumn(this.column);
		assertTrue(this.unitUnderTest.isMember(COLUMN_NAME0));
	}

	/**
	 * @changed OLI 20.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testIsMemberRegularRunWithOnANotExistingColumn() throws Exception {
		this.unitUnderTest.addColumn(this.column);
		assertFalse(this.unitUnderTest.isMember(COLUMN_NAME1));
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testRemoveColumnThrowsAnExceptionPassingANullPointerAsColumnName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.removeColumn(null);
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testRemoveColumnDoesNothingOnPassingANonExistingColumnName() throws Exception {
		this.fillAndCheckColumnNameListWithColumnName0();
		this.unitUnderTest.removeColumn(Utils.createAttributeMetaDataMock(":o)"));
		assertEquals(1, this.unitUnderTest.getColumns().length);
		assertEquals(this.column, this.unitUnderTest.getColumns()[0]);
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testRemoveColumnRemovesThePassedColumnNameFromTheList() throws Exception {
		this.fillAndCheckColumnNameListWithColumnName0();
		this.unitUnderTest.removeColumn(this.column);
		assertEquals(0, this.unitUnderTest.getColumns().length);
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSetNameThrowsAnExceptionPassingAnEmptyIndexName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.setName("");
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSetNameThrowsAnExceptionPassingANullPointerAsIndexName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.setName(null);
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSetNameSetsThePassedNameToTheIndex() throws Exception {
		assertEquals(INDEX_NAME, this.unitUnderTest.getName());
		this.unitUnderTest.setName(ANOTHER_INDEX_NAME);
		assertEquals(ANOTHER_INDEX_NAME, this.unitUnderTest.getName());
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSetTableThrowsAnExceptionPassingANullPointerAsTable() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.setTable(null);
		});
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testSetTableSetsThePassedTableToTheIndexObject() throws Exception {
		this.unitUnderTest.setTable(this.table);
		assertSame(this.table, this.unitUnderTest.getTable());
	}

	/**
	 * @changed OLI 14.12.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testToString() throws Exception {
		this.unitUnderTest.addColumn(this.column);
		this.unitUnderTest.addColumn(Utils.createAttributeMetaDataMock(COLUMN_NAME1));
		assertEquals("Name=" + INDEX_NAME + ", Table=" + TABLE_NAME + ", Columns=[" + COLUMN_NAME0 + "," + COLUMN_NAME1
				+ "]", this.unitUnderTest.toString());
	}

}