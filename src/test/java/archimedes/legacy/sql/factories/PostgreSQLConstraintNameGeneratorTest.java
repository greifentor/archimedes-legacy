/*
 * PostgreSQLConstraintNameGeneratorTest.java
 *
 * 08.04.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.model.ColumnModel;
import archimedes.model.TableModel;

/**
 * Tests of the class <CODE>PostgreSQLConstraintNameGenerator</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 08.04.2014 - Added.
 */

public class PostgreSQLConstraintNameGeneratorTest {

	private static final String COLUMN_NAME_0 = "COLUMN_0";
	private static final String COLUMN_NAME_1 = "COLUMN_1";
	private static final String REFERENCED_COLUMN_NAME_0 = "REFERENCED_COLUMN_0";
	private static final String TABLE_NAME = "TABLE";

	private ColumnModel column0 = null;
	private ColumnModel column1 = null;
	private ColumnModel referencedColumn0 = null;
	private PostgreSQLConstraintNameGenerator unitUnderTest = null;
	private TableModel table = null;

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.referencedColumn0 = this.createColumn(REFERENCED_COLUMN_NAME_0);
		this.column0 = this.createColumn(COLUMN_NAME_0);
		this.column1 = this.createColumn(COLUMN_NAME_1);
		this.table = this.createTable(TABLE_NAME, new ColumnModel[] { this.column0, this.column1 });
		this.unitUnderTest = new PostgreSQLConstraintNameGenerator();
	}

	private ColumnModel createColumn(String name) {
		return this.createColumn(name, null);
	}

	private ColumnModel createColumn(String name, ColumnModel referencedColumn) {
		ColumnModel cm = mock(ColumnModel.class);
		when(cm.getName()).thenReturn(name);
		if (referencedColumn != null) {
			when(cm.getReferencedColumn()).thenReturn(referencedColumn);
		}
		return cm;
	}

	private TableModel createTable(String name, ColumnModel[] columns) {
		TableModel tm = mock(TableModel.class);
		when(tm.getName()).thenReturn(name);
		when(tm.getColumns()).thenReturn(columns);
		for (ColumnModel cm : columns) {
			when(cm.getTable()).thenReturn(tm);
		}
		return tm;
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testCreateForeignKeyConstraintNameReturnsACorrectNameForOneMember() {
		when(this.column0.getReferencedColumn()).thenReturn(this.referencedColumn0);
		String s = this.unitUnderTest.createForeignKeyConstraintName(this.column0);
		assertEquals(TABLE_NAME + "_" + COLUMN_NAME_0 + "_fkey", s);
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testCreatePrimaryKeyConstraintNameReturnsACorrectNameForOneMember() {
		String s = this.unitUnderTest.createPrimaryKeyConstraintName(this.table);
		assertEquals(TABLE_NAME + "_pkey", s);
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testCreatePrimaryKeyConstraintNameReturnsACorrectNameForTwoMembers() {
		String s = this.unitUnderTest.createPrimaryKeyConstraintName(this.table);
		assertEquals(TABLE_NAME + "_pkey", s);
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testCreateUniqueConstraintNameReturnsACorrectNameForOneMember() {
		String s = this.unitUnderTest.createUniqueConstraintName(this.table.getName(), this.column0.getName());
		assertEquals(TABLE_NAME + "_" + COLUMN_NAME_0 + "_key", s);
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testCreateUniqueConstraintNameReturnsACorrectNameForTwoMembers() {
		String s = this.unitUnderTest.createUniqueConstraintName(this.table.getName(), this.column0.getName(),
				this.column1.getName());
		assertEquals(TABLE_NAME + "_" + COLUMN_NAME_0 + "_key", s);
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testCreateUniqueConstraintNameThrowsAnExceptionIfNoUniqueColumnNamesArePassed() {
		Assertions.assertThrows(ArrayIndexOutOfBoundsException.class, () -> {
			this.unitUnderTest.createUniqueConstraintName(this.table.getName());
		});
	}

}