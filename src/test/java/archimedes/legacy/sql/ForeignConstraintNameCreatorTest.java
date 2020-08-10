/*
 * ForeignConstraintNameCreatorTest.java
 *
 * 25.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.TableModel;

/**
 * Tests of the class <CODE>ForeignConstraintNameCreator</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 25.04.2013 - Added.
 */

public class ForeignConstraintNameCreatorTest {

	private static final String COLUMN_NAME = "Column";
	private static final String TABLE_NAME = "Table";

	private ColumnModel column = null;
	private ForeignConstraintNameCreator unitUnderTest = null;
	private TableModel table = null;

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.table = mock(TableModel.class);
		when(this.table.getName()).thenReturn(TABLE_NAME);
		this.column = mock(ColumnModel.class);
		when(this.column.getName()).thenReturn(COLUMN_NAME);
		when(this.column.getTable()).thenReturn(this.table);
		this.unitUnderTest = new ForeignConstraintNameCreator();
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testCreateReturnsACorrectForeignKeyConstraintName() {
		assertEquals(TABLE_NAME + "_" + COLUMN_NAME + "_fkey", this.unitUnderTest.create(this.column));
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testCreateThrowsAnExceptionPassingANullPointerAsColumn() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.create(null);
		});
	}

}