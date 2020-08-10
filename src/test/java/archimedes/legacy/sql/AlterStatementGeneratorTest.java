/*
 * AlterStatementGeneratorTest.java
 *
 * 06.10.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.Archimedes;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Tabelle;
import archimedes.legacy.scheme.Tabellenspalte;
import corent.db.DBExecMode;
import corent.files.StructuredTextFile;

/**
 * Tests f&uuml;r die Klasse AlterStatementGenerator.
 * 
 * @author ollie
 * 
 * @changed OLI 06.10.2009 - Hinzugef&uuml;gt.
 * @changed OLI 17.02.2010 - Erweiterung um den Test zur Methode <TT>getAlterDataType(...)</TT>.
 * @changed OLI 09.03.2010 - Erweiterung um den Test zur Datentyp-&Auml;nderung im HSQL-Modus.
 */

public class AlterStatementGeneratorTest {

	private DiagrammModel dm = null;

	@BeforeEach
	void setUp() throws Exception {
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERCOLUMN", "99");
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERROW", "99");
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		InputStream in = Archimedes.Factory.getClass().getClassLoader().getResourceAsStream("dm/TestModel.ads");
		StructuredTextFile stf = new StructuredTextFile(in);
		try {
			stf.load();
			this.dm = new Diagramm();
			this.dm = (Diagramm) dm.createDiagramm(stf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.setProperty("archimedes.gui.ArchimedesCommandProcessor.suppress.gui", "true");
	}

	/**
	 * Test der Methode <TT>getAlterDataType(TabellenModel, DBExceMode)</TT>.
	 * 
	 * @changed OLI 17.02.2010 - Hinzugef&uuml;gt.
	 * @changed OLI 09.03.2010 - Erweiterung um den Test zum HSQL-Modus.
	 */
	@Test
	public void testGetAlterDataTypeTabellenModelDBExceMode() {
		DomainModel dom = null;
		String s = null;
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		Tabellenspalte ts = (Tabellenspalte) t.getTabellenspalte("Deleted");
		// Kontrakt-Pruefung.
		try {
			AlterStatementGenerator.getAlterDataType(null, DBExecMode.MYSQL, "");
			fail("a NullPointerException should be thrown here.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			fail("a NullPointerException should be thrown here, but was a " + e.getClass().getName());
		}
		try {
			AlterStatementGenerator.getAlterDataType(ts, null, "");
			fail("a NullPointerException should be thrown here.");
		} catch (NullPointerException e) {
		} catch (Exception e) {
			fail("a NullPointerException should be thrown here, but was a " + e.getClass().getName());
		}
		try {
			AlterStatementGenerator.getAlterDataType(ts, DBExecMode.MSSQL, "");
			fail("an IllegalArgumentException should be thrown here.");
		} catch (IllegalArgumentException e) {
		} catch (Exception e) {
			fail("an IllegalArgumentException should be thrown here, but was a " + e.getClass().getName());
		}
		// Nutzung.
		try {
			dom = this.dm.getDomain("PCName");
			ts.setDomain(dom);
			s = AlterStatementGenerator.getAlterDataType(ts, DBExecMode.MYSQL, "");
			assertEquals("ALTER TABLE TestTabelle MODIFY COLUMN Deleted VARCHAR(40);", s);
			s = AlterStatementGenerator.getAlterDataType(ts, DBExecMode.HSQL, "");
			assertEquals("ALTER TABLE TestTabelle ALTER COLUMN Deleted SET DATA TYPE " + "VARCHAR(40);", s);
			s = AlterStatementGenerator.getAlterDataType(ts, DBExecMode.POSTGRESQL, "");
			assertEquals("ALTER TABLE TestTabelle ALTER COLUMN Deleted SET DATA TYPE " + "VARCHAR(40);", s);
		} catch (Exception e) {
			e.printStackTrace();
			fail("no exception should be thrown here, but was a " + e.getClass().getName());
		}
	}

	/** Test der Methode <TT>getAlterDefault(TabellenModel, DBExecMode)</TT>. */
	@Test
	public void testGetAlterDefaultTabellenModelDBExecMode() {
		String s = null;
		String sAlt0 = null;
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		Tabellenspalte ts = (Tabellenspalte) t.getTabellenspalte("Deleted");
		// Negativbeispiele.
		try {
			AlterStatementGenerator.getAlterDefault(null, DBExecMode.HSQL, "");
			fail();
		} catch (AssertionError ae) {
			assertTrue(true);
		} catch (NullPointerException npe) {
			assertTrue(true);
		}
		try {
			AlterStatementGenerator.getAlterDefault(t.getTabellenspalteAt(0), null, "");
			fail();
		} catch (AssertionError ae) {
			assertTrue(true);
		} catch (NullPointerException npe) {
			assertTrue(true);
		}
		// Positivbeispiele.
		s = "ALTER TABLE TestTabelle ALTER COLUMN Deleted SET DEFAULT -1;";
		assertNotNull(ts);
		((Domain) ts.getDomain()).setInitialValue("-1");
		assertEquals(s, AlterStatementGenerator.getAlterDefault(ts, DBExecMode.HSQL, ""));
		assertEquals(s, AlterStatementGenerator.getAlterDefault(ts, DBExecMode.MSSQL, ""));
		assertEquals(s, AlterStatementGenerator.getAlterDefault(ts, DBExecMode.MYSQL, ""));
		assertEquals(s, AlterStatementGenerator.getAlterDefault(ts, DBExecMode.POSTGRESQL, ""));
		s = "ALTER TABLE TestTabelle ALTER COLUMN Deleted DROP DEFAULT;";
		sAlt0 = "ALTER TABLE TestTabelle ALTER COLUMN Deleted SET DEFAULT NULL;";
		assertNotNull(ts);
		((Domain) ts.getDomain()).setInitialValue("NULL");
		assertEquals(sAlt0, AlterStatementGenerator.getAlterDefault(ts, DBExecMode.HSQL, ""));
		assertEquals(s, AlterStatementGenerator.getAlterDefault(ts, DBExecMode.MSSQL, ""));
		assertEquals(s, AlterStatementGenerator.getAlterDefault(ts, DBExecMode.MYSQL, ""));
		assertEquals(s, AlterStatementGenerator.getAlterDefault(ts, DBExecMode.POSTGRESQL, ""));
	}

	/** Test der Methode <TT>getAlterNull(TabellenModel, DBExecMode)</TT>. */
	@Test
	public void testGetAlterNullTabellenModelDBExecMode() {
		String s = null;
		String sAlt0 = null;
		String sAlt1 = null;
		String sAlt2 = null;
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		Tabellenspalte ts = (Tabellenspalte) t.getTabellenspalte("Deleted");
		// Negativbeispiele.
		try {
			AlterStatementGenerator.getAlterNull(null, DBExecMode.HSQL, false, "");
			fail();
		} catch (AssertionError ae) {
			assertTrue(true);
		} catch (NullPointerException npe) {
			assertTrue(true);
		}
		try {
			AlterStatementGenerator.getAlterNull(t.getTabellenspalteAt(0), null, false, "");
			fail();
		} catch (AssertionError ae) {
			assertTrue(true);
		} catch (NullPointerException npe) {
			assertTrue(true);
		}
		// Positivbeispiele.
		s = "ALTER TABLE TestTabelle ALTER COLUMN Deleted SET NOT NULL;";
		sAlt0 = "ALTER TABLE TestTabelle ALTER COLUMN Deleted NOT NULL;";
		sAlt1 = "ALTER TABLE TestTabelle MODIFY Deleted INT NOT NULL;";
		assertNotNull(ts);
		ts.setNotNull(true);
		assertEquals(s, AlterStatementGenerator.getAlterNull(ts, DBExecMode.HSQL, false, ""));
		assertEquals(sAlt0, AlterStatementGenerator.getAlterNull(ts, DBExecMode.MSSQL, false, ""));
		assertEquals(sAlt1, AlterStatementGenerator.getAlterNull(ts, DBExecMode.MYSQL, false, ""));
		assertEquals(s, AlterStatementGenerator.getAlterNull(ts, DBExecMode.POSTGRESQL, false, ""));
		s = "ALTER TABLE TestTabelle ALTER COLUMN Deleted SET NULL;";
		sAlt0 = "ALTER TABLE TestTabelle ALTER COLUMN Deleted NULL;";
		sAlt1 = "ALTER TABLE TestTabelle MODIFY Deleted INT NULL;";
		sAlt2 = "ALTER TABLE TestTabelle ALTER COLUMN Deleted DROP NOT NULL;";
		assertNotNull(ts);
		ts.setNotNull(false);
		assertEquals(s, AlterStatementGenerator.getAlterNull(ts, DBExecMode.HSQL, false, ""));
		assertEquals(sAlt0, AlterStatementGenerator.getAlterNull(ts, DBExecMode.MSSQL, false, ""));
		assertEquals(sAlt1, AlterStatementGenerator.getAlterNull(ts, DBExecMode.MYSQL, false, ""));
		assertEquals(sAlt2, AlterStatementGenerator.getAlterNull(ts, DBExecMode.POSTGRESQL, false, ""));
		s = "ALTER TABLE TestTabelle ALTER COLUMN Deleted SET NOT NULL;";
		sAlt0 = "ALTER TABLE TestTabelle ALTER COLUMN Deleted NOT NULL;";
		sAlt1 = "ALTER TABLE TestTabelle MODIFY Deleted Boolean NOT NULL;";
		assertNotNull(ts);
		ts.setNotNull(true);
		assertEquals(s, AlterStatementGenerator.getAlterNull(ts, DBExecMode.HSQL, true, ""));
		assertEquals(sAlt0, AlterStatementGenerator.getAlterNull(ts, DBExecMode.MSSQL, true, ""));
		assertEquals(sAlt1, AlterStatementGenerator.getAlterNull(ts, DBExecMode.MYSQL, true, ""));
		assertEquals(s, AlterStatementGenerator.getAlterNull(ts, DBExecMode.POSTGRESQL, true, ""));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetAlterStatementReturnsCorrectStmtNoDomainDefaultSetCanBeNull() {
		ColumnModel column = this.createColumnModel("true", false, DBExecMode.MYSQL);
		assertEquals("ALTER TABLE 'Table' ADD COLUMN 'Column' BOOLEAN DEFAULT true;",
				AlterStatementGenerator.getAlterStatement(column, DBExecMode.MYSQL, false, false, "'"));
	}

	private ColumnModel createColumnModel(String defaultValue, boolean notNull, DBExecMode dbmode, String domainType) {
		ColumnModel column = mock(ColumnModel.class);
		DomainModel domain = mock(DomainModel.class);
		TableModel table = mock(TableModel.class);
		when(column.getDefaultValue()).thenReturn(defaultValue);
		when(column.getDomain()).thenReturn(domain);
		when(column.getTable()).thenReturn(table);
		when(column.getName()).thenReturn("Column");
		when(column.isNotNull()).thenReturn(notNull);
		when(domain.getName()).thenReturn("Domain");
		when(domain.getType(dbmode)).thenReturn(domainType);
		when(table.getName()).thenReturn("Table");
		return column;
	}

	private ColumnModel createColumnModel(String defaultValue, boolean notNull, DBExecMode dbmode) {
		return this.createColumnModel(defaultValue, notNull, dbmode, "Boolean");
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetAlterStatementReturnsCorrectStmtNoDomainDefaultSetCanNotBeNull() {
		ColumnModel column = this.createColumnModel("true", true, DBExecMode.MYSQL);
		assertEquals("ALTER TABLE 'Table' ADD COLUMN 'Column' BOOLEAN NOT NULL DEFAULT true;",
				AlterStatementGenerator.getAlterStatement(column, DBExecMode.MYSQL, false, true, "'"));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetAlterStatementReturnsCorrectStmtNoDomainDefaultSetCanNotBeNullNullSuppr() {
		ColumnModel column = this.createColumnModel("true", true, DBExecMode.MYSQL);
		assertEquals("ALTER TABLE 'Table' ADD COLUMN 'Column' BOOLEAN DEFAULT true;",
				AlterStatementGenerator.getAlterStatement(column, DBExecMode.MYSQL, false, false, "'"));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetAlterStatementReturnsCorrectStmtNoDomainNoDefaultCanBeNull() {
		ColumnModel column = this.createColumnModel(null, false, DBExecMode.MYSQL);
		assertEquals("ALTER TABLE 'Table' ADD COLUMN 'Column' BOOLEAN;",
				AlterStatementGenerator.getAlterStatement(column, DBExecMode.MYSQL, false, false, "'"));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetAlterStatementReturnsCorrectStmtNoDomainNoDefaultCanBeNullMSSQL() {
		ColumnModel column = this.createColumnModel(null, true, DBExecMode.MSSQL);
		assertEquals("ALTER TABLE 'Table' ADD 'Column' BOOLEAN NOT NULL;",
				AlterStatementGenerator.getAlterStatement(column, DBExecMode.MSSQL, false, true, "'"));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetAlterStatementReturnsCorrectStmtNoDomainNoDefaultCanNotBeNull() {
		ColumnModel column = this.createColumnModel(null, true, DBExecMode.MYSQL);
		assertEquals("ALTER TABLE 'Table' ADD COLUMN 'Column' BOOLEAN NOT NULL;",
				AlterStatementGenerator.getAlterStatement(column, DBExecMode.MYSQL, false, true, "'"));
	}

	/**
	 * @changed OLI 26.07.2013 - Added.
	 */
	@Test
	public void testGetAlterStatementReturnsCorrectStmtStringDomainDefaultSetCanBeNullWithDomain() {
		ColumnModel column = this.createColumnModel("true", false, DBExecMode.MYSQL, "TEXT");
		assertEquals("ALTER TABLE 'Table' ADD COLUMN 'Column' Domain DEFAULT 'true';",
				AlterStatementGenerator.getAlterStatement(column, DBExecMode.MYSQL, true, false, "'"));
	}

}