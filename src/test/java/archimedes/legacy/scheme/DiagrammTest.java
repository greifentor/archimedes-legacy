/*
 * DiagrammTest.java
 *
 * 14.06.2011
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.sql.Types;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.SequenceModel;
import archimedes.legacy.model.TableModel;

/**
 * Tests of the class <CODE>Diagramm</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 14.06.2011 - Added.
 */

public class DiagrammTest {

	private Diagramm unitUnderTest = null;
	private Domain domain = null;
	private Domain domainStr = null;
	private Sequence sequence = null;
	private Tabelle table = null;
	private Tabellenspalte column = null;
	private Tabellenspalte columnStr = null;
	private View view = null;

	/**
	 * @changed OLI 14.06.2011 - Added.
	 */
	@BeforeEach
	public void setUp() {
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		this.domain = new Domain("Integer", Types.INTEGER, 0, 0);
		this.domainStr = new Domain("Description", Types.VARCHAR, 50, 0);
		this.sequence = this.createSequence();
		this.view = new View("view", ":o)", true);
		this.unitUnderTest = new Diagramm();
		this.table = new Tabelle(this.view, 0, 0, this.unitUnderTest);
		this.table.setName("TestTable");
		this.column = new Tabellenspalte("TestColumn", this.domain);
		this.table.addTabellenspalte(this.column);
		this.columnStr = new Tabellenspalte("TestColumnStr", this.domainStr);
		this.table.addTabellenspalte(this.columnStr);
	}

	// To create an equal but not the same sequence.
	private Sequence createSequence() {
		return new Sequence("Sequence", 1, 2);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testAddSequenceAddsAnEqualSequenceOnlyOnce() {
		this.unitUnderTest.addSequence(this.sequence);
		this.unitUnderTest.addSequence(this.createSequence());
		assertEquals(1, this.unitUnderTest.getSequences().length);
		assertSame(this.sequence, this.unitUnderTest.getSequences()[0]);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testAddSequenceAddsThePassedSequenceToTheSequencesOfTheModel() {
		this.unitUnderTest.addSequence(this.sequence);
		assertEquals(1, this.unitUnderTest.getSequences().length);
		assertSame(this.sequence, this.unitUnderTest.getSequences()[0]);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testAddSequenceThrowsAnExceptionPassingANullPointerAsSequence() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.addSequence(null);
		});
	}

	/**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementRegularRun() { assertEquals("ALTER TABLE TestTable ADD COLUMN
	 *       TestColumn INT;", this.unitUnderTest.createAlterStatement(this.column, DBExecMode.HSQL, false, false, ""));
	 *       }
	 * 
	 *       /**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementWithMSSQLDBMS() { assertEquals("ALTER TABLE TestTable ADD TestColumn
	 *       INT;", this.unitUnderTest.createAlterStatement(this.column, DBExecMode.MSSQL, false, false, "")); }
	 * 
	 *       /**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementWithSetDefaultValue() { this.domain.setInitialValue("4711");
	 *       assertEquals( "ALTER TABLE TestTable ADD COLUMN TestColumn INT DEFAULT 4711;",
	 *       this.unitUnderTest.createAlterStatement(this.column, DBExecMode.HSQL, false, false, "")); }
	 * 
	 *       /**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementWithSetDefaultValueStr() { this.domainStr.setInitialValue("");
	 *       assertEquals( "ALTER TABLE TestTable ADD COLUMN TestColumnStr VARCHAR(50) DEFAULT '';" ,
	 *       this.unitUnderTest.createAlterStatement(this.columnStr, DBExecMode.HSQL, false, false, "")); }
	 * 
	 *       /**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementWithSetHasDomainsFlag() { assertEquals ("ALTER TABLE TestTable ADD
	 *       COLUMN TestColumn Integer;", this.unitUnderTest.createAlterStatement(this.column, DBExecMode.HSQL, true,
	 *       false, "")); }
	 * 
	 *       /**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementWithSetNotNullFlagIfColumnNotNullIsNotSet() {
	 *       this.column.setNotNull(false); assertEquals("ALTER TABLE TestTable ADD COLUMN TestColumn INT;",
	 *       this.unitUnderTest.createAlterStatement(this.column, DBExecMode.HSQL, false, true, "")); }
	 * 
	 *       /**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementWithSetNotNullFlagIfColumnNotNullIsSet() {
	 *       this.column.setNotNull(true); assertEquals( "ALTER TABLE TestTable ADD COLUMN TestColumn INT NOT NULL;",
	 *       this.unitUnderTest.createAlterStatement(this.column, DBExecMode.HSQL, false, true, "")); }
	 * 
	 *       /**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementWithSetNotNullFlagAndPostgreSQLModeIfColumnNNNotSt () {
	 *       this.column.setNotNull(false); assertEquals("ALTER TABLE TestTable ADD COLUMN TestColumn INT;",
	 *       this.unitUnderTest.createAlterStatement(this.column, DBExecMode.POSTGRESQL, false, true, "")); }
	 * 
	 *       /**
	 * @changed OLI 14.06.2011 - Added. /
	 * @Test public void testCreateAlterStatementWithSetNotNullFlagAndPostgreSQLModeIfColumnNNIsSet () {
	 *       this.column.setNotNull(true); assertEquals( "ALTER TABLE TestTable ADD COLUMN TestColumn INT NOT NULL;",
	 *       this.unitUnderTest.createAlterStatement(this.column, DBExecMode.POSTGRESQL, false, true, "")); }
	 * 
	 *       /**
	 * @changed OLI 24.02.2012 - Added.
	 */
	@Test
	public void testGetSchemaNameReturnsEmptyStringAsDefault() {
		assertEquals("", this.unitUnderTest.getSchemaName());
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testCreateSequenceReturnsANewSequenceWithDefaultValues() {
		SequenceModel s0 = this.unitUnderTest.createSequence();
		SequenceModel s1 = this.unitUnderTest.createSequence();
		assertEquals(s0, s1);
		assertNotSame(s0, s1);
		assertEquals(100, s0.getIncrement());
		assertEquals("Sequenz", s0.getName());
		assertEquals(100, s0.getStartValue());
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Test
	public void testGetAdditionalSQLCodeReturnsTheDefaultValueIfUnset() {
		assertEquals("", this.unitUnderTest.getAdditionalSQLCodePostReducingCode());
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetAllColumnsReturnsAnArrayWithAllColumnsOfAllTablesOfTheModel() {
		this.unitUnderTest.addTabelle(this.table);
		ColumnModel[] cols = this.unitUnderTest.getAllColumns();
		assertNotNull(cols);
		assertEquals(2, cols.length);
		assertEquals(this.column, cols[0]);
		assertEquals(this.columnStr, cols[1]);
	}

	/**
	 * @changed OLI 20.12.2011 - Added.
	 */
	@Test
	public void testGetClassThrowsAnExceptionPassingAnAEmptyStringAsClassName() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.getClass("");
		});
	}

	/**
	 * @changed OLI 20.12.2011 - Added.
	 */
	@Test
	public void testGetClassThrowsAnExceptionPassingANullPointerAsClassName() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.getClass(null);
		});
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Test
	public void testSetAdditionalSQLCodeSetsThePassedStringAsnewAdditionalSQLCode() {
		String s = ";op";
		this.unitUnderTest.setAdditionalSQLCodePostReducingCode(s);
		assertEquals(s, this.unitUnderTest.getAdditionalSQLCodePostReducingCode());
	}

	/**
	 * @changed OLI 26.04.2013 - Added.
	 */
	@Test
	public void testSetAdditionalSQLCodeThrowsAnExceptionPassingANullPointer() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.setAdditionalSQLCodePostReducingCode(null);
		});
	}

	/**
	 * @changed OLI 24.02.2012 - Added.
	 */
	@Test
	public void testSetSchemaNameStoresAFilledString() {
		this.unitUnderTest.setSchemaName(":o)");
		assertEquals(":o)", this.unitUnderTest.getSchemaName());
	}

	/**
	 * @changed OLI 24.02.2012 - Added.
	 */
	@Test
	public void testSetSchemaNameStoresAnEmptyString() {
		this.unitUnderTest.setSchemaName("");
		assertEquals("", this.unitUnderTest.getSchemaName());
	}

	/**
	 * @changed OLI 24.02.2012 - Added.
	 */
	@Test
	public void testSetSchemaNameThrowsAnExceptionPassingANullPointer() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.setSchemaName(null);
		});
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetSequencesByReferenceReturnsAReferenceOfTheSequenceListOfTheModel() {
		this.unitUnderTest.addSequence(new Sequence("Sequence1", 1, 2));
		this.unitUnderTest.addSequence(new Sequence("Sequence2", 3, 4));
		assertEquals(2, this.unitUnderTest.getSequences().length);
		this.unitUnderTest.getSequencesByReference().remove(new Sequence("Sequence1", 1, 2));
		assertEquals(1, this.unitUnderTest.getSequences().length);
		assertEquals("Sequence2", this.unitUnderTest.getSequences()[0].getName());
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetTableByNameReturnsNullPassingANullPointer() {
		this.unitUnderTest.addTabelle(this.table);
		TableModel tm = this.unitUnderTest.getTableByName(null);
		assertNull(tm);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetTableByNameReturnsNullPassingAnUnknownTableName() {
		this.unitUnderTest.addTabelle(this.table);
		TableModel tm = this.unitUnderTest.getTableByName("UNKNOWN_TABLE_NAME");
		assertNull(tm);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetTableByNameReturnsTheTableWithThePassedName() {
		this.unitUnderTest.addTabelle(this.table);
		TableModel tm = this.unitUnderTest.getTableByName(this.table.getName());
		assertNotNull(tm);
		assertSame(tm, this.table);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testRemoveSequenceDeletesNothingPassingANullPointer() {
		this.unitUnderTest.addSequence(new Sequence("Sequence1", 1, 2));
		this.unitUnderTest.addSequence(new Sequence("Sequence2", 3, 4));
		assertEquals(2, this.unitUnderTest.getSequences().length);
		this.unitUnderTest.removeSequence(null);
		assertEquals(2, this.unitUnderTest.getSequences().length);
		assertEquals("Sequence1", this.unitUnderTest.getSequences()[0].getName());
		assertEquals("Sequence2", this.unitUnderTest.getSequences()[1].getName());
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testRemoveSequenceDeletesNothingPassingASequenceWhichIsNotInTheModel() {
		this.unitUnderTest.addSequence(new Sequence("Sequence1", 1, 2));
		this.unitUnderTest.addSequence(new Sequence("Sequence2", 3, 4));
		assertEquals(2, this.unitUnderTest.getSequences().length);
		this.unitUnderTest.removeSequence(new Sequence("Sequence3", 5, 6));
		assertEquals(2, this.unitUnderTest.getSequences().length);
		assertEquals("Sequence1", this.unitUnderTest.getSequences()[0].getName());
		assertEquals("Sequence2", this.unitUnderTest.getSequences()[1].getName());
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testRemoveSequenceDeletesThePassedSequenceFromTheModel() {
		this.unitUnderTest.addSequence(new Sequence("Sequence1", 1, 2));
		this.unitUnderTest.addSequence(new Sequence("Sequence2", 3, 4));
		assertEquals(2, this.unitUnderTest.getSequences().length);
		this.unitUnderTest.removeSequence(new Sequence("Sequence1", 1, 2));
		assertEquals(1, this.unitUnderTest.getSequences().length);
		assertEquals("Sequence2", this.unitUnderTest.getSequences()[0].getName());
	}

}