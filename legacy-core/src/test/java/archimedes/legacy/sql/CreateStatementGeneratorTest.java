/*
 * CreateStatementGeneratorTest.java
 *
 * 06.10.2009
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.FileInputStream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Tabelle;
import corent.db.DBExecMode;
import corent.files.StructuredTextFile;

/**
 * Tests f&uuml;r die Klasse CreateStatementGenerator.
 *
 * @author ollie
 *
 * @changed OLI 06.10.2009 - Hinzugef&uuml;gt
 */

public class CreateStatementGeneratorTest {

	private DiagrammModel dm = null;

	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERCOLUMN", "99");
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERROW", "99");
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		// InputStream in = Archimedes.Factory.getClass().getClassLoader().getResourceAsStream(
		// "dm/TestModel.ads");
		FileInputStream in = new FileInputStream("src/test/resources/dm/TestModel.ads");
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
	 * Test der Methode <TT>getCreateStatement(TabellenModel, DBExecMode, boolean, boolean,
	 * boolean, String)</TT>.
	 */
	@Test
	public void testGetCreateStatementTabellenModelDBExecModeBooleanBooleanBooleanString() {
		String stmt = null;
		String stmtAlt1 = null;
		String stmtAlt2 = null;
		String stmtAlt3 = null;
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		assertNotNull(t);
		stmt = "CREATE TABLE TestTabelle (\n" + "    Id BIGINT DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    Referenz BIGINT DEFAULT -1,\n" + "    Deleted INT DEFAULT 0,\n"
				+ "    Description VARCHAR(100) NOT NULL,\n" + "    Token VARCHAR(20) DEFAULT ':o)',\n"
				+ "    UNIQUE(Token)\n" + ");";
		stmtAlt1 = "CREATE TABLE TestTabelle (\n" + "    Id BIGINT NOT NULL DEFAULT -1 PRIMARY KEY,\n"
				+ "    Referenz BIGINT DEFAULT -1,\n" + "    Deleted INT DEFAULT 0,\n"
				+ "    Description VARCHAR(100) NOT NULL,\n" + "    Token VARCHAR(20) DEFAULT ':o)' UNIQUE\n" + ");";
		stmtAlt2 = "CREATE TABLE TestTabelle (\n" + "    Id BIGINT DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    Referenz BIGINT DEFAULT -1,\n" + "    Deleted INT DEFAULT 0,\n"
				+ "    Description VARCHAR(100) NOT NULL,\n" + "    Token VARCHAR(20) DEFAULT ':o)' UNIQUE\n" + ");";
		stmtAlt3 = "CREATE TABLE TestTabelle (\n" + "    Id BIGINT DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    Referenz BIGINT DEFAULT -1,\n" + "    Deleted BOOLEAN DEFAULT false,\n"
				+ "    Description VARCHAR(100) NOT NULL,\n" + "    Token VARCHAR(20) DEFAULT ':o)' UNIQUE\n" + ");";
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.HSQL, false, null, false));
		assertEquals(stmtAlt1, CreateStatementGenerator.getCreateStatement(t, DBExecMode.MYSQL, false, null, false));
		assertEquals(stmtAlt2, CreateStatementGenerator.getCreateStatement(t, DBExecMode.MSSQL, false, null, false));
		assertEquals(stmtAlt3,
				CreateStatementGenerator.getCreateStatement(t, DBExecMode.POSTGRESQL, false, null, false));
		stmt = "CREATE TABLE TestTabelle (\n" + "    Id Ident DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    Referenz Ident DEFAULT -1,\n" + "    Deleted Boolean DEFAULT 0,\n"
				+ "    Description Description NOT NULL,\n" + "    Token Token DEFAULT ':o)',\n" + "    UNIQUE(Token)\n"
				+ ");";
		stmtAlt1 = "CREATE TABLE TestTabelle (\n" + "    Id Ident NOT NULL DEFAULT -1 PRIMARY KEY,\n"
				+ "    Referenz Ident DEFAULT -1,\n" + "    Deleted Boolean DEFAULT 0,\n"
				+ "    Description Description NOT NULL,\n" + "    Token Token DEFAULT ':o)' UNIQUE\n" + ");";
		stmtAlt2 = "CREATE TABLE TestTabelle (\n" + "    Id Ident DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    Referenz Ident DEFAULT -1,\n" + "    Deleted Boolean DEFAULT 0,\n"
				+ "    Description Description NOT NULL,\n" + "    Token Token DEFAULT ':o)' UNIQUE\n" + ");";
		stmtAlt3 = "CREATE TABLE TestTabelle (\n" + "    Id Ident DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    Referenz Ident DEFAULT -1,\n" + "    Deleted Boolean DEFAULT false,\n"
				+ "    Description Description NOT NULL,\n" + "    Token Token DEFAULT ':o)' UNIQUE\n" + ");";
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.HSQL, true, null, false));
		assertEquals(stmtAlt1, CreateStatementGenerator.getCreateStatement(t, DBExecMode.MYSQL, true, null, false));
		assertEquals(stmtAlt2, CreateStatementGenerator.getCreateStatement(t, DBExecMode.MSSQL, true, null, false));
		assertEquals(stmtAlt3,
				CreateStatementGenerator.getCreateStatement(t, DBExecMode.POSTGRESQL, true, null, false));
		stmt = "CREATE TABLE StereotypeSchemeTableScheme (\n" + "    StereotypeScheme Ident DEFAULT -1 NOT NULL,\n"
				+ "    TableScheme Ident DEFAULT -1 NOT NULL,\n" + "    Active Boolean,\n" + "    Deleted Boolean,\n"
				+ "    LastModificationDate PTimestamp,\n" + "    LastModificationPCName PCName,\n"
				+ "    LastModificationUser Ident DEFAULT -1,\n" + "    PRIMARY KEY(StereotypeScheme, TableScheme)\n"
				+ ");";
		stmtAlt1 = "CREATE TABLE StereotypeSchemeTableScheme (\n" + "    StereotypeScheme Ident NOT NULL DEFAULT -1,\n"
				+ "    TableScheme Ident NOT NULL DEFAULT -1,\n" + "    Active Boolean,\n" + "    Deleted Boolean,\n"
				+ "    LastModificationDate PTimestamp,\n" + "    LastModificationPCName PCName,\n"
				+ "    LastModificationUser Ident DEFAULT -1,\n" + "    PRIMARY KEY(StereotypeScheme, TableScheme)\n"
				+ ");";
		t = (Tabelle) this.dm.getTabelle("StereotypeSchemeTableScheme");
		assertNotNull(t);
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.HSQL, true, null, false));
		assertEquals(stmtAlt1, CreateStatementGenerator.getCreateStatement(t, DBExecMode.MYSQL, true, null, false));
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.MSSQL, true, null, false));
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.POSTGRESQL, true, null, false));
		/*
		 * Hier fehlt der Test auf Setzen von referenziellen Integrit&auml;ten.
		 */
		t = (Tabelle) this.dm.getTabelle("TestTabelle");
		assertNotNull(t);
		stmt = "CREATE TABLE 'TestTabelle' (\n" + "    'Id' Ident DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    'Referenz' Ident DEFAULT -1,\n" + "    'Deleted' Boolean DEFAULT 0,\n"
				+ "    'Description' Description NOT NULL,\n" + "    'Token' Token DEFAULT ':o)',\n"
				+ "    UNIQUE('Token')\n" + ");";
		stmtAlt1 = "CREATE TABLE 'TestTabelle' (\n" + "    'Id' Ident NOT NULL DEFAULT -1 PRIMARY KEY,\n"
				+ "    'Referenz' Ident DEFAULT -1,\n" + "    'Deleted' Boolean DEFAULT 0,\n"
				+ "    'Description' Description NOT NULL,\n" + "    'Token' Token DEFAULT ':o)' UNIQUE\n" + ");";
		stmtAlt2 = "CREATE TABLE 'TestTabelle' (\n" + "    'Id' Ident DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    'Referenz' Ident DEFAULT -1,\n" + "    'Deleted' Boolean DEFAULT 0,\n"
				+ "    'Description' Description NOT NULL,\n" + "    'Token' Token DEFAULT ':o)' UNIQUE\n" + ");";
		stmtAlt3 = "CREATE TABLE 'TestTabelle' (\n" + "    'Id' Ident DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    'Referenz' Ident DEFAULT -1,\n" + "    'Deleted' Boolean DEFAULT false,\n"
				+ "    'Description' Description NOT NULL,\n" + "    'Token' Token DEFAULT ':o)' UNIQUE\n" + ");";
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.HSQL, true, "'", false));
		assertEquals(stmtAlt1, CreateStatementGenerator.getCreateStatement(t, DBExecMode.MYSQL, true, "'", false));
		assertEquals(stmtAlt2, CreateStatementGenerator.getCreateStatement(t, DBExecMode.MSSQL, true, "'", false));
		assertEquals(stmtAlt3, CreateStatementGenerator.getCreateStatement(t, DBExecMode.POSTGRESQL, true, "'", false));
	}

	/**
	 * @changed OLI 30.03.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetCreateStatementThrowsAnExceptionPassingANullPointerAsDBExecMode() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
			CreateStatementGenerator.getCreateStatement(t, null, true, "'", true);
		});
	}

	/**
	 * @changed OLI 30.03.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetCreateStatementThrowsAnExceptionPassingANullPointerAsTableModel() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			CreateStatementGenerator.getCreateStatement(null, DBExecMode.HSQL, true, "'", true);
		});
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetCreateStatementWithBooleanDefaultTrue() throws Exception {
		String stmt = "CREATE TABLE TestTabelle (\n" + "    Id Ident DEFAULT -1 PRIMARY KEY,\n"
				+ "    Referenz Ident DEFAULT -1,\n" + "    Deleted Boolean DEFAULT true,\n"
				+ "    Description Description,\n" + "    Token Token DEFAULT ':o)' UNIQUE\n" + ");";
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		t.getTabellenspalte("Deleted").setIndividualDefaultValue("1");
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.POSTGRESQL, true, null, true));
	}

	/**
	 * @changed OLI 08.05.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetCreateStatementWithSimpleUniqueConstraint() throws Exception {
		String stmt = "CREATE TABLE TestTabelle (\n" + "    Id BIGINT DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    Referenz BIGINT DEFAULT -1,\n" + "    Deleted BOOLEAN DEFAULT false,\n"
				+ "    Description VARCHAR(100) NOT NULL,\n" + "    Token VARCHAR(20) DEFAULT ':o)' UNIQUE\n" + ");";
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.POSTGRESQL, false, null, false));
	}

	/**
	 * @changed OLI 18.06.2013 - Added.
	 */
	@Test
	public void testGetCreateStatementWithSimpleUniqueConstraintForHSQL() throws Exception {
		String stmt = "CREATE TABLE TestTabelle (\n" + "    Id BIGINT DEFAULT -1 NOT NULL PRIMARY KEY,\n"
				+ "    Referenz BIGINT DEFAULT -1,\n" + "    Deleted INT DEFAULT 0,\n"
				+ "    Description VARCHAR(100) NOT NULL,\n" + "    Token VARCHAR(20) DEFAULT ':o)',\n"
				+ "    UNIQUE(Token)\n" + ");";
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.HSQL, false, null, false));
	}

	/**
	 * @changed OLI 23.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetCreateStatementWithSuppressionOfNotNull() throws Exception {
		String stmt = "CREATE TABLE TestTabelle (\n" + "    Id Ident DEFAULT -1 PRIMARY KEY,\n"
				+ "    Referenz Ident DEFAULT -1,\n" + "    Deleted Boolean DEFAULT false,\n"
				+ "    Description Description,\n" + "    Token Token DEFAULT ':o)' UNIQUE\n" + ");";
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.POSTGRESQL, true, null, true));
	}

	/**
	 * @changed OLI 24.02.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetCreateStatementWithWithSetUniqueClauseAndComplexPK() throws Exception {
		String stmt = "CREATE TABLE TestTabelle (\n" + "    Id Ident DEFAULT -1,\n" + "    Referenz Ident DEFAULT -1,\n"
				+ "    Deleted Boolean DEFAULT false,\n" + "    Description Description,\n"
				+ "    Token Token DEFAULT ':o)' UNIQUE,\n" + "    PRIMARY KEY(Id, Referenz),\n"
				+ "    UNIQUE(Description, Deleted)\n" + ");";
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		t.getTabellenspalte("Referenz").setPrimarykey(true);
		t.setComplexUniqueSpecification("Description & Deleted");
		assertEquals(stmt, CreateStatementGenerator.getCreateStatement(t, DBExecMode.POSTGRESQL, true, null, true));
	}

	/** Test der Methode <TT>getPKMemberCount(TabellenModel)</TT>. */
	@Test
	public void testGetPKMemberCountTabellenModel() {
		Tabelle t = (Tabelle) this.dm.getTabelle("TestTabelle");
		assertNotNull(t);
		assertEquals(1, CreateStatementGenerator.getPKMembersCount(t));
		t = (Tabelle) this.dm.getTabelle("StereotypeSchemeTableScheme");
		assertNotNull(t);
		assertEquals(2, CreateStatementGenerator.getPKMembersCount(t));
	}

	/**
	 * @changed OLI 30.03.2012 - Hinzugef&uuml;gt.
	 */
	@Test
	public void testGetPKMemberCountTabellenModelThrowsAnExceptionPassingANullPointerAsTable() throws Exception {
		Assertions.assertThrows(IllegalArgumentException.class, () -> {
			CreateStatementGenerator.getPKMembersCount(null);
		});
	}

}