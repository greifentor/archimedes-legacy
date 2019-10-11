/*
 * DefaultSchemaChangeStatementGeneratorTest.java
 *
 * 24.02.2012
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import corent.db.DBExecMode;

/**
 * Tests zur Klasse <CODE>DefaultSchemaChangeStatementGenerator</CODE>.
 *
 * @author ollie
 *
 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
 */

public class DefaultSchemaChangeStatementGeneratorTest {

	private static final DBExecMode DBMS = DBExecMode.POSTGRESQL;
	private static final String SCHEMA_NAME = "Schema";

	private DefaultSchemaChangeStatementGenerator unitUnderTest = null;

	/**
	 * @changed OLI 21.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.unitUnderTest = new DefaultSchemaChangeStatementGenerator();
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateRegularRun() throws Exception {
		assertEquals("SET search_path TO " + SCHEMA_NAME + ";", this.unitUnderTest.create(SCHEMA_NAME, DBMS));
	}

	@Test
	public void testCreateRegularRunWithQuotedSchemaName() throws Exception {
		assertEquals("SET search_path TO \"" + SCHEMA_NAME + "\";",
				this.unitUnderTest.create("\"" + SCHEMA_NAME + "\"", DBMS));
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateReturnsANullPointerPassingSchemaNameAsEmptyString() throws Exception {
		assertNull(this.unitUnderTest.create("", DBMS));
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateReturnsANullPointerPassingSchemaNameAsNull() throws Exception {
		assertNull(this.unitUnderTest.create(null, DBMS));
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateThrowsAnExceptionPassingANullPointerAsDBMS() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			this.unitUnderTest.create(SCHEMA_NAME, null);
		});
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateThrowsAnExceptionPassingAnUnsupportedDBMSHSQL() throws Exception {
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			this.unitUnderTest.create(SCHEMA_NAME, DBExecMode.HSQL);
		});
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateThrowsAnExceptionPassingAnUnsupportedDBMSMSSQL() throws Exception {
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			this.unitUnderTest.create(SCHEMA_NAME, DBExecMode.MSSQL);
		});
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testCreateThrowsAnExceptionPassingAnUnsupportedDBMSMYSQL() throws Exception {
		Assertions.assertThrows(UnsupportedOperationException.class, () -> {
			this.unitUnderTest.create(SCHEMA_NAME, DBExecMode.MYSQL);
		});
	}

}