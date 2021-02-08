/*
 * SetSchemeVersionAppenderTest.java
 *
 * 28.08.2014
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.script.sql.SQLScript;
import archimedes.legacy.sql.SQLScriptFactory;
import archimedes.legacy.sql.factories.GenericSQLScriptFactory;
import archimedes.legacy.sql.factories.PostgreSQLScriptFactory;
import archimedes.model.DataModel;
import corent.db.DBExecMode;

/**
 * Tests of the class <CODE>SetSchemeVersionAppender</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 28.08.2014 - Added.
 */

public class SetSchemeVersionAppenderTest {

	private static final String MODEL_VERION = "47.11";
	private static final String QUOTES = "'";
	private static final String SCHEME_NAME = "scheme";

	private SQLScriptFactory factory = null;
	private SQLScript script = null;
	private SetSchemeVersionAppender unitUnderTest = null;

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@BeforeEach
	public void setUp() {
		this.factory = new PostgreSQLScriptFactory(QUOTES, mock(DataModel.class));
		this.script = new SQLScript();
		this.unitUnderTest = new SetSchemeVersionAppender(MODEL_VERION, SCHEME_NAME, this.factory);
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Test
	public void testAppendDoesNothingUsingAnEmptyModelversion() {
		this.unitUnderTest = new SetSchemeVersionAppender("", SCHEME_NAME, this.factory);
		this.unitUnderTest.append(script);
		assertEquals(0, script.size());
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Test
	public void testAppendDoesNothingUsingAnEmptySchemaName() {
		this.unitUnderTest = new SetSchemeVersionAppender(MODEL_VERION, "", this.factory);
		this.unitUnderTest.append(script);
		assertEquals(0, script.size());
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Test
	public void testAppendDoesNothingUsingANullPointerAsModelVersion() {
		this.unitUnderTest = new SetSchemeVersionAppender(MODEL_VERION, null, this.factory);
		this.unitUnderTest.append(script);
		assertEquals(0, script.size());
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Test
	public void testAppendDoesNothingUsingANullPointerAsSchemaName() {
		this.unitUnderTest = new SetSchemeVersionAppender(null, SCHEME_NAME, this.factory);
		this.unitUnderTest.append(script);
		assertEquals(0, script.size());
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Test
	public void testAppendDoesNothingUsingAnUnsupportedDBMS() {
		this.unitUnderTest = new SetSchemeVersionAppender(MODEL_VERION, SCHEME_NAME,
				new GenericSQLScriptFactory(QUOTES, mock(DataModel.class), DBExecMode.MSSQL));
		this.unitUnderTest.append(script);
		assertEquals(2, script.size());
		assertEquals("/* WARNING (47.11): Statement to set model version cannot be created for " + "MS-SQL-Modus! */",
				script.getExtendingStatements()[0]);
		assertEquals("", script.getExtendingStatements()[1]);
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Test
	public void testAppendRegularRun() {
		this.unitUnderTest.append(script);
		assertEquals(2, script.size());
		assertEquals("COMMENT ON SCHEMA " + QUOTES + SCHEME_NAME + QUOTES + " IS 'Version " + MODEL_VERION + "';",
				script.getExtendingStatements()[0]);
		assertEquals("", script.getExtendingStatements()[1]);
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Test
	public void testAppendThrowsAnExceptionPassingANullPointerAsScript() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.append(null);
		});
	}

	/**
	 * @changed OLI 28.08.2014 - Added.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsFactory() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SetSchemeVersionAppender(MODEL_VERION, SCHEME_NAME, null);
		});
	}

}