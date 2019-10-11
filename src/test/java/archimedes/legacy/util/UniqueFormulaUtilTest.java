/*
 * UniqueFormulaUtilTest.java
 *
 * 30.03.2012
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for the class <CODE>UniqueFormulaUtil</CODE>.
 *
 * @author ollie
 *
 * @changed OLI 30.03.2012 - Added.
 */

public class UniqueFormulaUtilTest {

	private static final String FIELD_NAME_1 = "Field1";
	private static final String FIELD_NAME_2 = "Field2";
	private static final String UNIQUE_FORMULA = FIELD_NAME_2 + " & " + FIELD_NAME_1;

	private UniqueFormulaUtil unitUnderTest = null;

	/**
	 * @changed OLI 21.11.2011 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.unitUnderTest = new UniqueFormulaUtil(UNIQUE_FORMULA);
	}

	/**
	 * @changed OLI 30.03.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsUniqueFormula() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new UniqueFormulaUtil(null);
		});
	}

	/**
	 * @changed OLI 30.03.2012 - Added.
	 */
	@Test
	public void testGetFieldNamesReturnsACorruptedListForAnInvalidFormula() {
		this.unitUnderTest = new UniqueFormulaUtil(FIELD_NAME_2 + " | " + FIELD_NAME_1);
		String[] result = this.unitUnderTest.getFieldNames();
		assertNotNull(result);
		assertEquals(3, result.length);
		assertEquals(FIELD_NAME_2, result[0]);
		assertEquals("|", result[1]);
		assertEquals(FIELD_NAME_1, result[2]);
	}

	/**
	 * @changed OLI 30.03.2012 - Added.
	 */
	@Test
	public void testGetFieldNamesReturnsAnEmptyArrayForAnEmptyFormula() {
		this.unitUnderTest = new UniqueFormulaUtil("");
		String[] result = this.unitUnderTest.getFieldNames();
		assertNotNull(result);
		assertEquals(0, result.length);
	}

	/**
	 * @changed OLI 30.03.2012 - Added.
	 */
	@Test
	public void testGetFieldNamesReturnsExpectedFieldNames() {
		String[] result = this.unitUnderTest.getFieldNames();
		assertNotNull(result);
		assertEquals(2, result.length);
		assertEquals(FIELD_NAME_2, result[0]);
		assertEquals(FIELD_NAME_1, result[1]);
	}

}