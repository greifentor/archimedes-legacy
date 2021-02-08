/*
 * TestDomain.java
 *
 * 08.03.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.scheme;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

import java.sql.Types;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import corent.db.DBExecMode;

/**
 * Testf&auml;lle f&uuml;r die Domain-Klasse.
 *
 * @author ollie
 *
 * @changed OLI 10.06.2010 - Hinzugef&uuml;gt.
 * @changed OLI 14.11.2011 - Modernisierung des Tests und Erweiterung um Tests f&uuml;r BLOB-Typen.
 */

public class TestDomain {

	private Domain unitUnderTest = null;

	/**
	 * @changed OLI 14.11.2011 - Hinzugef&uuml;gt.
	 */
	@BeforeEach
	public void setUp() {
		this.unitUnderTest = new Domain();
		this.unitUnderTest.setDataType(Types.INTEGER);
		this.unitUnderTest.setInitialValue("4711");
		this.unitUnderTest.setName("Test");
	}

	/**
	 * Test des Vertrags der Methode <TT>getType(DBExecMode)</TT>.
	 *
	 * @changed OLI 10.06.2010 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetTypeDBExecModeCheckContract() {
		// Kontrakt-Pruefung.
		try {
			this.unitUnderTest.getType(null);
			fail("an IllegalArgumentException should be thrown here.");
		} catch (IllegalArgumentException iae) {
		} catch (Exception e) {
			fail("an IllegalArgumentException should be thrown here.");
		}
	}

	/**
	 * @changed OLI 14.11.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetTypeWithTypeBLOB() {
		this.unitUnderTest.setDataType(Types.BLOB);
		assertEquals("longvarbinary", this.unitUnderTest.getType(DBExecMode.HSQL));
		assertEquals("varbinary(max)", this.unitUnderTest.getType(DBExecMode.MSSQL));
		assertEquals("blob", this.unitUnderTest.getType(DBExecMode.MYSQL));
		assertEquals("bytea", this.unitUnderTest.getType(DBExecMode.POSTGRESQL));
	}

	/**
	 * @changed OLI 14.11.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetTypeWithTypeDOUBLE() {
		this.unitUnderTest.setDataType(Types.DOUBLE);
		assertEquals("double", this.unitUnderTest.getType(DBExecMode.HSQL));
		assertEquals("double", this.unitUnderTest.getType(DBExecMode.MSSQL));
		assertEquals("double", this.unitUnderTest.getType(DBExecMode.MYSQL));
		assertEquals("double precision", this.unitUnderTest.getType(DBExecMode.POSTGRESQL));
	}

	/**
	 * @changed OLI 14.11.2011 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetTypeWithTypeLONGVARCHAR() {
		this.unitUnderTest.setDataType(Types.LONGVARCHAR);
		assertEquals("longvarchar", this.unitUnderTest.getType(DBExecMode.HSQL));
		assertEquals("text", this.unitUnderTest.getType(DBExecMode.MSSQL));
		assertEquals("text", this.unitUnderTest.getType(DBExecMode.MYSQL));
		assertEquals("text", this.unitUnderTest.getType(DBExecMode.POSTGRESQL));
	}

}