/*
 * TableTest.java
 *
 * 25.04.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.TableModel;

/**
 * Tests of the <CODE>TableModel</CODE> methods in the class <CODE>Tabellen</CODE>:
 * 
 * @author ollie
 * 
 * @changed OLI 25.04.2013 - Added.
 */

public class TableTest {

	private static final String COLUMN_NAME_0 = "column 0";
	private static final String COLUMN_NAME_1 = "column 1";
	private static final String TABLE_NAME = "table";

	private ColumnModel column0 = null;
	private ColumnModel column1 = null;
	private TableModel unitUnderTest = null;

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@BeforeEach
	public void setUp() {
		Diagramm model = new Diagramm();
		View view = new View();
		this.column0 = this.createColumn(COLUMN_NAME_0);
		this.column1 = this.createColumn(COLUMN_NAME_1);
		this.unitUnderTest = new Tabelle(view, 0, 0, model);
		((Tabelle) this.unitUnderTest).setName(TABLE_NAME);
		this.unitUnderTest.addColumn(this.column0);
		this.unitUnderTest.addColumn(this.column1);
	}

	private ColumnModel createColumn(String name) {
		ColumnModel c = mock(Tabellenspalte.class);
		when(c.getName()).thenReturn(name);
		return c;
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetColumnByNameReturnANullPointerPassingANullPointer() {
		assertNull(this.unitUnderTest.getColumnByName(null));
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetColumnByNameReturnANullPointerPassingAnUnknownColumnName() {
		assertNull(this.unitUnderTest.getColumnByName("UNKNOWN_COLUMN_NAME"));
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetColumnByNameReturnTheColumnWithThePassedNameIfExists() {
		ColumnModel c = this.unitUnderTest.getColumnByName(COLUMN_NAME_0);
		assertNotNull(c);
		assertEquals(c, this.column0);
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testConstructorSetsTheDeprecatedFlagToFalse() {
		assertFalse(this.unitUnderTest.isDeprecated());
	}

	/**
	 * @changed OLI 25.04.2013 - Added.
	 */
	@Test
	public void testGetNameReturnsTheNameOfTheTable() {
		assertEquals(TABLE_NAME, this.unitUnderTest.getName());
	}

}