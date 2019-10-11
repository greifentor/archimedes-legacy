/*
 * SchemaChangeStatementAppenderTest.java
 *
 * 24.02.2012
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
 * Tests zur Klasse <CODE>SchemaChangeStatementAppender</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
 */

public class SchemaChangeStatementAppenderTest {

	private static final String QUOTES = "'";
	private static final String SCHEMA_NAME = "Schema";

	private SQLScriptFactory factory = null;
	private SQLScript script = null;
	private SchemaChangeStatementAppender unitUnderTest = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() {
		this.factory = new PostgreSQLScriptFactory(QUOTES, mock(DataModel.class));
		this.script = new SQLScript();
		this.unitUnderTest = new SchemaChangeStatementAppender(SCHEMA_NAME, this.factory);
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testAppendDoesNothingUsingAnEmptySchemaName() {
		this.unitUnderTest = new SchemaChangeStatementAppender("", this.factory);
		this.unitUnderTest.append(script);
		assertEquals(0, script.size());
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testAppendDoesNothingUsingANullPointerAsSchemaName() {
		this.unitUnderTest = new SchemaChangeStatementAppender(null, this.factory);
		this.unitUnderTest.append(script);
		assertEquals(0, script.size());
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testAppendDoesNothingUsingAnUnsupportedDBMS() {
		this.unitUnderTest = new SchemaChangeStatementAppender(SCHEMA_NAME,
				new GenericSQLScriptFactory(QUOTES, mock(DataModel.class), DBExecMode.MSSQL));
		this.unitUnderTest.append(script);
		assertEquals(2, script.size());
		assertEquals("/* WARNING (Schema): Statement to change to schema cannot be created for " + "MS-SQL-Modus! */",
				script.getExtendingStatements()[0]);
		assertEquals("", script.getExtendingStatements()[1]);
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testAppendRegularRun() {
		this.unitUnderTest.append(script);
		assertEquals(2, script.size());
		assertEquals("SET search_path TO '" + SCHEMA_NAME + "';", script.getExtendingStatements()[0]);
		assertEquals("", script.getExtendingStatements()[1]);
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testAppendThrowsAnExceptionPassingANullPointerAsScript() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.append(null);
		});
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testConstructorThrowsAnExceptionPassingANullPointerAsFactory() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			new SchemaChangeStatementAppender(SCHEMA_NAME, null);
		});
	}

}