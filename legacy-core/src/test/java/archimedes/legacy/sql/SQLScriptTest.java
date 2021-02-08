/*
 * SQLScriptTest.java
 *
 * 12.12.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.script.sql.SQLScript;

/**
 * Tests of the class <CODE>SQLScript</CODE>.
 *
 * @author ollie
 *
 * @changed OLI 12.12.2013 - Added.
 */

public class SQLScriptTest {

	private static final String STMT = "stmt";

	private SQLScript unitUnderTest = null;

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.unitUnderTest = new SQLScript();
	}

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@Test
	public void testAddChangingStatementAddsThePassedStmtsToTheList() {
		this.unitUnderTest.addChangingStatement(STMT + 1);
		this.unitUnderTest.addChangingStatement(STMT + 2);
		String[] a = this.unitUnderTest.getChangingStatements();
		assertEquals(2, a.length);
		assertEquals(STMT + 1, a[0]);
		assertEquals(STMT + 2, a[1]);
	}

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@Test
	public void testAddChangingStatementThrowsAIllegalArgumentExceptionPassingANullString() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.addChangingStatement(null);
		});
	}

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@Test
	public void testAddExtendingStatementAddsThePassedStmtsToTheList() {
		this.unitUnderTest.addExtendingStatement(STMT + 1);
		this.unitUnderTest.addExtendingStatement(STMT + 2);
		String[] a = this.unitUnderTest.getExtendingStatements();
		assertEquals(2, a.length);
		assertEquals(STMT + 1, a[0]);
		assertEquals(STMT + 2, a[1]);
	}

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@Test
	public void testAddExtendingStatementThrowsAIllegalArgumentExceptionPassingANullString() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.addExtendingStatement(null);
		});
	}

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@Test
	public void testAddReducingStatementAddsThePassedStmtsToTheList() {
		this.unitUnderTest.addReducingStatement(STMT + 1);
		this.unitUnderTest.addReducingStatement(STMT + 2);
		String[] a = this.unitUnderTest.getReducingStatements();
		assertEquals(2, a.length);
		assertEquals(STMT + 1, a[0]);
		assertEquals(STMT + 2, a[1]);
	}

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@Test
	public void testAddReducingStatementThrowsAIllegalArgumentExceptionPassingANullString() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.addReducingStatement(null);
		});
	}

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@Test
	public void testCreateScriptReturnsTheRightsScriptForThePassedParameters() {
		this.unitUnderTest.addChangingStatement("CHNG");
		this.unitUnderTest.addExtendingStatement("EXT");
		this.unitUnderTest.addReducingStatement("RDC");
		assertEquals("H\n\nPEXT\n\nEXT\n\nPRCHNG\n\nCHNG\n\nPSTCHNG\n\nRDC\n\nPRDC",
				this.unitUnderTest.createScript("H", "PEXT", "PRCHNG", "PSTCHNG", "PRDC"));
	}

	/**
	 * @changed OLI 12.12.2013 - Added.
	 */
	@Test
	public void testCreateScriptReturnsTheRightsScriptForThePassedParametersNoFragments() {
		this.unitUnderTest.addChangingStatement("CHNG1");
		this.unitUnderTest.addChangingStatement("CHNG2");
		this.unitUnderTest.addExtendingStatement("EXT");
		this.unitUnderTest.addReducingStatement("RDC");
		assertEquals("EXT\n\nCHNG1\nCHNG2\n\nRDC", this.unitUnderTest.createScript("", null, "", null, ""));
	}

}