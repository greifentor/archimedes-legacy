/*
 * PostgreSQLScriptFactoryTest.java
 *
 * 01.08.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileInputStream;
import java.util.LinkedList;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.SequenceModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.UniqueMetaData;
import archimedes.legacy.scheme.Diagramm;
import corent.db.DBExecMode;
import corent.files.StructuredTextFile;

/**
 * Tests of the class <CODE>PostgreSQLScriptFactory</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 01.08.2013 - Added.
 */

public class PostgreSQLScriptFactoryTest {

	private static final String QUOTES = "\"";

	private DataModel dataModel = null;
	private DomainModel domain = null;
	private SequenceModel sequence = null;
	private List<UniqueMetaData> uniques = null;
	private PostgreSQLScriptFactory unitUnderTest = null;

	/**
	 * @throws Exception If the setup routine fails.
	 * 
	 * @changed OLI 01.08.2013 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERCOLUMN", "99");
		System.setProperty("archimedes.gui.ComponentDiagramm.PAGESPERROW", "99");
		System.setProperty("corent.base.StrUtil.suppress.html.note", "true");
		FileInputStream in = new FileInputStream("src/test/resources/dm/TestModel.ads");
		StructuredTextFile stf = new StructuredTextFile(in);
		Diagramm dm = null;
		try {
			stf.load();
			dm = new Diagramm();
			dm = (Diagramm) dm.createDiagramm(stf);
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.setProperty("archimedes.gui.ArchimedesCommandProcessor.suppress.gui", "true");
		this.dataModel = (DataModel) dm;
		this.domain = mock(DomainModel.class);
		this.sequence = mock(SequenceModel.class);
		this.unitUnderTest = new PostgreSQLScriptFactory(QUOTES, this.dataModel);
		this.uniques = new LinkedList<UniqueMetaData>();
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAddColumnStatementReturnsACorrectStatement() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Referenz");
		c.setNotNull(true);
		assertEquals(
				"ALTER TABLE " + QUOTES + "TestTabelle" + QUOTES + " ADD " + QUOTES + "Referenz" + QUOTES
						+ " Ident NOT NULL DEFAULT -1;",
				this.unitUnderTest.alterTableAddColumnStatement(c, true, true));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAddColumnStatementReturnsACorrectStatementForStringField() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Description");
		c.setIndividualDefaultValue("':o)'");
		assertEquals(
				"ALTER TABLE " + QUOTES + "TestTabelle" + QUOTES + " ADD " + QUOTES + "Description" + QUOTES
						+ " Description NOT NULL DEFAULT ':o)';",
				this.unitUnderTest.alterTableAddColumnStatement(c, true, true));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAddColumnStatementReturnsACorrectStatementNoDomainsNDefault() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Referenz");
		c.setIndividualDefaultValue("NULL");
		assertEquals(
				"ALTER TABLE " + QUOTES + "TestTabelle" + QUOTES + " ADD " + QUOTES + "Referenz" + QUOTES + " BIGINT;",
				this.unitUnderTest.alterTableAddColumnStatement(c, false, false));
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAddConstraintForComplexUniquesReturnsEmptyStmtIfNoConstrain() {
		this.uniques.add(new UniqueMetaData("TestTabelle_Unique_Id_Deleted", "TestTabelle"));
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("");
		String[] s = this.unitUnderTest.alterTableAddConstraintForComplexUniques(t, this.uniques);
		assertNotNull(s);
		assertEquals(0, s.length);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAddConstraintForComplexUniquesReturnsEmptyStmtIfCnstrntExist() {
		this.uniques.add(new UniqueMetaData("AX_ComplexUnique_TestTabelle_Deleted_Id", "TestTabelle"));
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("Id & Deleted");
		String[] s = this.unitUnderTest.alterTableAddConstraintForComplexUniques(t, this.uniques);
		assertNotNull(s);
		assertEquals(0, s.length);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAddConstraintForComplexUniquesReturnsStmtForUniqueCnstrnt1() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("Id & Deleted");
		String[] s = this.unitUnderTest.alterTableAddConstraintForComplexUniques(t, this.uniques);
		assertNotNull(s);
		assertEquals(1, s.length);
		assertEquals("ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"AX_ComplexUnique_TestTabelle"
				+ "_Deleted_Id\" UNIQUE (\"Deleted\", \"Id\");", s[0]);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAddConstraintForComplexUniquesReturnsStmtForUniqueCnstrnt2() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("Id & Deleted | Id & Token");
		String[] s = this.unitUnderTest.alterTableAddConstraintForComplexUniques(t, this.uniques);
		assertNotNull(s);
		assertEquals(2, s.length);
		assertEquals("ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"AX_ComplexUnique_TestTabelle"
				+ "_Deleted_Id\" UNIQUE (\"Deleted\", \"Id\");", s[0]);
		assertEquals("ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"AX_ComplexUnique_TestTabelle"
				+ "_Id_Token\" UNIQUE (\"Id\", \"Token\");", s[1]);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddPrimaryKeyConstraintReturnsACorrectStatement() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		t.getColumnByName("Description").setPrimaryKey(true);
		String s = this.unitUnderTest.alterTableAddPrimaryKeyConstraint(t);
		assertEquals("ALTER TABLE \"TestTabelle\" ADD PRIMARY KEY(\"Description\", \"Id\");", s);
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAlterColumnSetDataTypeStatementReturnsACorrectStatement() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Description");
		assertEquals(
				"ALTER TABLE " + QUOTES + "TestTabelle" + QUOTES + " ALTER COLUMN " + QUOTES + "Description" + QUOTES
						+ " SET DATA TYPE Description;",
				this.unitUnderTest.alterTableAlterColumnSetDataTypeStatement(c, true));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAlterColumnSetDefaultStatementDropTheDefault() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Description");
		c.setIndividualDefaultValue("");
		c.getDomain().setInitialValue("NULL");
		assertEquals("ALTER TABLE " + QUOTES + "TestTabelle" + QUOTES + " ALTER COLUMN " + QUOTES + "Description"
				+ QUOTES + " DROP DEFAULT;", this.unitUnderTest.alterTableAlterColumnSetDefaultStatement(c));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testAlterTableAlterColumnSetDefaultStatementSetTheDefault() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Description");
		c.setIndividualDefaultValue("':op'");
		assertEquals("ALTER TABLE " + QUOTES + "TestTabelle" + QUOTES + " ALTER COLUMN " + QUOTES + "Description"
				+ QUOTES + " SET DEFAULT ':op';", this.unitUnderTest.alterTableAlterColumnSetDefaultStatement(c));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableDropColumnReturnsACorrectStatement() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnMetaData cmd = new ColumnMetaData("Abc");
		String s = this.unitUnderTest.alterTableDropColumn(t, cmd);
		assertNotNull(s);
		assertEquals(s,
				"ALTER TABLE " + QUOTES + "TestTabelle" + QUOTES + " DROP COLUMN " + QUOTES + "Abc" + QUOTES + ";");
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableDropConstraintForComplexUniqueReturnsEmptyStmtIfCnstrntNExst() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("Id & Deleted");
		String[] s = this.unitUnderTest.alterTableDropConstraintForComplexUniques(this.dataModel, this.uniques);
		assertNotNull(s);
		assertEquals(0, s.length);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableDropConstraintForComplexUniqueReturnsEmptyStmtIfCnstrAndUniq() {
		this.uniques.add(new UniqueMetaData("AX_ComplexUnique_TestTabelle_Deleted_Id", "TestTabelle"));
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("Id & Deleted");
		String[] s = this.unitUnderTest.alterTableDropConstraintForComplexUniques(this.dataModel, this.uniques);
		assertNotNull(s);
		assertEquals(0, s.length);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableDropConstraintForComplexUniqueReturnsStmtCnstrntIsToDelete0() {
		this.uniques.add(new UniqueMetaData("AX_ComplexUnique_TestTabelle_Deleted_Id", "TestTabelle"));
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("");
		String[] s = this.unitUnderTest.alterTableDropConstraintForComplexUniques(this.dataModel, this.uniques);
		assertNotNull(s);
		assertEquals(1, s.length);
		assertEquals("ALTER TABLE \"TestTabelle\" DROP CONSTRAINT \"AX_ComplexUnique_" + "TestTabelle_Deleted_Id\";",
				s[0]);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableDropConstraintForComplexUniqueReturnsStmtCnstrntIsToDelete1() {
		this.uniques.add(new UniqueMetaData("AX_ComplexUnique_TestTabelle_Deleted_Id", "TestTabelle"));
		this.uniques.add(new UniqueMetaData("AX_ComplexUnique_TestTabelle_Id_Token", "TestTabelle"));
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("");
		String[] s = this.unitUnderTest.alterTableDropConstraintForComplexUniques(this.dataModel, this.uniques);
		assertEquals(2, s.length);
		assertEquals("ALTER TABLE \"TestTabelle\" DROP CONSTRAINT \"AX_ComplexUnique_" + "TestTabelle_Deleted_Id\";",
				s[0]);
		assertEquals("ALTER TABLE \"TestTabelle\" DROP CONSTRAINT \"AX_ComplexUnique_" + "TestTabelle_Id_Token\";",
				s[1]);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableDropConstraintForComplexUniqueReturnsStmtCnstrntIsToDelete2() {
		this.uniques.add(new UniqueMetaData("AX_ComplexUnique_TestTabelle_Deleted_Id", "TestTabelle"));
		this.uniques.add(new UniqueMetaData("AX_ComplexUnique_TestTabelle_Id_Token", "TestTabelle"));
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		((TabellenModel) t).setComplexUniqueSpecification("Id & Token");
		String[] s = this.unitUnderTest.alterTableDropConstraintForComplexUniques(this.dataModel, this.uniques);
		assertEquals(1, s.length);
		assertEquals("ALTER TABLE \"TestTabelle\" DROP CONSTRAINT \"AX_ComplexUnique_" + "TestTabelle_Deleted_Id\";",
				s[0]);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableDropPrimaryKeyConstraintReturnsCorrectStatement() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		String s = this.unitUnderTest.alterTableDropPrimaryKeyConstraint(t);
		assertNotNull(s);
		assertEquals("ALTER TABLE \"TestTabelle\" DROP CONSTRAINT \"TestTabelle_pkey\";", s);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableModifyConstraintNotNullReturnsCorrectStatementForNotNullCol() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Description");
		String s = this.unitUnderTest.alterTableModifyConstraintNotNull(c);
		assertNotNull(s);
		assertEquals("ALTER TABLE \"TestTabelle\" ALTER COLUMN \"Description\" SET NOT NULL;", s);
	}

	/**
	 * @changed OLI 05.08.2013 - Added.
	 */
	@Test
	public void testAlterTableModifyConstraintNotNullReturnsCorrectStatementForNullableCol() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Description");
		c.setNotNull(false);
		String s = this.unitUnderTest.alterTableModifyConstraintNotNull(c);
		assertNotNull(s);
		assertEquals("ALTER TABLE \"TestTabelle\" ALTER COLUMN \"Description\" DROP NOT NULL;", s);
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testCreateDomainStatementReturnsACorrectStatementForThePassedDomain() {
		when(this.domain.getInitialValue()).thenReturn("4711");
		when(this.domain.getName()).thenReturn("Domain");
		when(this.domain.getType(DBExecMode.POSTGRESQL)).thenReturn("int");
		assertEquals("CREATE DOMAIN " + QUOTES + "Domain" + QUOTES + " AS int DEFAULT 4711;",
				this.unitUnderTest.createDomainStatement(this.domain));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testCreateDomainStatementThrowsANullPointerExceptionPassingANullDomain() {
		Assertions.assertThrows(NullPointerException.class, () -> {
			this.unitUnderTest.createDomainStatement(null);
		});
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testCreateSequenceStatementReturnsACorrectStatementForThePassedSequence() {
		when(this.sequence.getIncrement()).thenReturn(7L);
		when(this.sequence.getName()).thenReturn("Sequence");
		when(this.sequence.getStartValue()).thenReturn(8L);
		assertEquals("CREATE SEQUENCE " + QUOTES + "Sequence" + QUOTES + " INCREMENT BY 7 START" + " WITH 8;",
				this.unitUnderTest.createSequenceStatement(this.sequence));
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsAnUniqueConstraintCollision() throws Exception {
		String stmt = "CREATE TABLE \"TestTabelle\" (\n" + "    \"Id\" Ident DEFAULT -1,\n"
				+ "    \"Referenz\" Ident DEFAULT -1,\n" + "    \"Deleted\" Boolean DEFAULT false,\n"
				+ "    \"Description\" Description,\n" + "    \"Token\" Token DEFAULT ':o)'\n" + ");\n"
				+ "ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_pkey\" PRIMARY KEY "
				+ "(\"Id\", \"Referenz\");\n"
				+ "ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_Description_key\" "
				+ "UNIQUE (\"Description\");";
		/*
		 * + "ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_Description_key1\" " +
		 * "UNIQUE (\"Description\", \"Deleted\");\n" +
		 * "ALTER TABLE \"TestTabelle\" ADD FOREIGN KEY (\"Referenz\") REFERENCES " + "\"DomainScheme\";";
		 */
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		t.getColumnByName("Description").setUnique(true);
		t.getColumnByName("Referenz").setPrimaryKey(true);
		t.getColumnByName("Token").setUnique(false);
		((TabellenModel) t).setComplexUniqueSpecification("Description & Deleted");
		assertEquals(stmt, this.unitUnderTest.createTableStatement(t, true, true));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsACorrectStmtForAMultipleUniqueAndPKTable() throws Exception {
		String stmt = "CREATE TABLE \"TestTabelle\" (\n" + "    \"Id\" Ident DEFAULT -1,\n"
				+ "    \"Referenz\" Ident DEFAULT -1,\n" + "    \"Deleted\" Boolean DEFAULT false,\n"
				+ "    \"Description\" Description,\n" + "    \"Token\" Token DEFAULT ':o)'\n"
				// + " PRIMARY KEY(\"Id\", \"Referenz\")\n"
				// + " UNIQUE(\"Description\", \"Deleted\")\n"
				+ ");\n" + "ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_pkey\" PRIMARY KEY "
				+ "(\"Id\", \"Referenz\");";
		/*
		 * + "ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_Description_key\" " +
		 * "UNIQUE (\"Description\", \"Deleted\");\n" +
		 * "ALTER TABLE \"TestTabelle\" ADD FOREIGN KEY (\"Referenz\") REFERENCES " + "\"DomainScheme\";";
		 */
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		t.getColumnByName("Referenz").setPrimaryKey(true);
		t.getColumnByName("Token").setUnique(false);
		((TabellenModel) t).setComplexUniqueSpecification("Description & Deleted");
		assertEquals(stmt, this.unitUnderTest.createTableStatement(t, true, true));
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsACorrectStmtForMultiKeyForeignKeys() {
		String stmt = "CREATE TABLE \"MultiKeyReferenzTabelle\" (\n" + "    \"Id\" Ident DEFAULT -1 NOT NULL,\n"
				+ "    \"Ref0\" Description,\n" + "    \"Ref1\" Token DEFAULT ':o)'\n" + ");\n"
				+ "ALTER TABLE \"MultiKeyReferenzTabelle\" ADD CONSTRAINT "
				+ "\"MultiKeyReferenzTabelle_pkey\" PRIMARY KEY (\"Id\");";
		/*
		 * + "ALTER TABLE \"MultiKeyReferenzTabelle\" ADD FOREIGN KEY (\"Ref0\", \"Ref1\")" +
		 * " REFERENCES \"MultiKeyTabelle\";";
		 */
		assertEquals(stmt, this.unitUnderTest
				.createTableStatement(this.dataModel.getTableByName("MultiKeyReferenzTabelle"), true, false));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsACorrectStmtForThePassedTable() {
		String stmt = "CREATE TABLE \"TestTabelle\" (\n" + "    \"Id\" Ident DEFAULT -1 NOT NULL,\n"
				+ "    \"Referenz\" Ident DEFAULT -1,\n" + "    \"Deleted\" Boolean DEFAULT false,\n"
				+ "    \"Description\" Description NOT NULL,\n" + "    \"Token\" Token DEFAULT ':o)'\n" + ");\n"
				+ "ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_pkey\" PRIMARY " + "KEY (\"Id\");\n"
				+ "ALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_Token_key\" UNIQUE " + "(\"Token\");";
		/*
		 * + "ALTER TABLE \"TestTabelle\" ADD FOREIGN KEY (\"Referenz\") REFERENCES " + "\"DomainScheme\";";
		 */
		assertEquals(stmt,
				this.unitUnderTest.createTableStatement(this.dataModel.getTableByName("TestTabelle"), true, false));
	}

	/**
	 * @changed OLI 08.11.2013 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsACorrectStmtForAdditionalCreateConstaints() {
		this.dataModel.getTableByName("TestTabelle").setAdditionalCreateConstraints(
				"CHECK(\"" + "From\" IS_NULL & \"To\" IS_NULL | \"From\" IS_NOT_NULL & \"To\" IS_NOT_NULL)");
		String stmt = "CREATE TABLE \"TestTabelle\" (\n" + "    \"Id\" Ident DEFAULT -1 NOT NULL,\n"
				+ "    \"Referenz\" Ident DEFAULT -1,\n" + "    \"Deleted\" Boolean DEFAULT false,\n"
				+ "    \"Description\" Description NOT NULL,\n" + "    \"Token\" Token DEFAULT ':o)',\n"
				+ "    CHECK(\"From\" IS NULL AND \"To\" IS NULL OR \"From\" IS NOT NULL AND \"To\"" + " IS NOT NULL)\n"
				+ ");" + "\nALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_pkey\" PRIMARY KEY " + "(\"Id\");"
				+ "\nALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_Token_key\" UNIQUE " + "(\"Token\");";
		/*
		 * + "ALTER TABLE \"TestTabelle\" ADD FOREIGN KEY (\"Referenz\") REFERENCES " + "\"DomainScheme\";";
		 */
		assertEquals(stmt,
				this.unitUnderTest.createTableStatement(this.dataModel.getTableByName("TestTabelle"), true, false));
	}

	/**
	 * @changed OLI 08.11.2013 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsACorrectStmtForAdditionalCreateConstaintsWB() {
		this.dataModel.getTableByName("TestTabelle").setAdditionalCreateConstraints(
				"CHECK(\"" + "From\" IS_NULL & \"To\" IS_NULL | \"From\" IS_NOT_NULL & \"To\" IS_NOT_NULL)");
		String stmt = "CREATE TABLE \"TestTabelle\" (\n" + "    \"Id\" Ident DEFAULT -1 NOT NULL,\n"
				+ "    \"Referenz\" Ident DEFAULT -1,\n" + "    \"Deleted\" Boolean DEFAULT true,\n"
				+ "    \"Description\" Description NOT NULL,\n" + "    \"Token\" Token DEFAULT ':o)',\n"
				+ "    CHECK(\"From\" IS NULL AND \"To\" IS NULL OR \"From\" IS NOT NULL AND \"To\"" + " IS NOT NULL)\n"
				+ ");" + "\nALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_pkey\" PRIMARY KEY " + "(\"Id\");"
				+ "\nALTER TABLE \"TestTabelle\" ADD CONSTRAINT \"TestTabelle_Token_key\" UNIQUE " + "(\"Token\");";
		/*
		 * + "ALTER TABLE \"TestTabelle\" ADD FOREIGN KEY (\"Referenz\") REFERENCES " + "\"DomainScheme\";";
		 */
		this.dataModel.getTableByName("TestTabelle").getColumnByName("Deleted").setIndividualDefaultValue("true");
		assertEquals(stmt,
				this.unitUnderTest.createTableStatement(this.dataModel.getTableByName("TestTabelle"), true, false));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testDropForeignKeyConstraintReturnACorrectStatement() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Referenz");
		assertEquals("ALTER TABLE " + QUOTES + "TestTabelle" + QUOTES + " DROP CONSTRAINT "
				+ "\"TestTabelle_Referenz_fkey\";", this.unitUnderTest.dropForeignKeyConstraint(c));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testDropSequenceStatementReturnACorrectStatement() {
		SequenceMetaData smd = mock(SequenceMetaData.class);
		when(smd.getName()).thenReturn("sequence");
		assertEquals("DROP SEQUENCE IF EXISTS \"sequence\";", this.unitUnderTest.dropSequenceStatement(smd));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testDropTableStatementReturnACorrectStatement() {
		TableMetaData tmd = new TableMetaData("table");
		assertEquals("DROP TABLE \"table\" CASCADE;", this.unitUnderTest.dropTableStatement(tmd));
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testDropSimpleIndexStatementReturnsACorrectStatement() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Referenz");
		assertEquals("DROP INDEX IF EXISTS ITestTabelleReferenz;", this.unitUnderTest.dropSimpleIndexStatement(c));
	}

	/**
	 * @changed OLI 08.04.2014 - Added.
	 */
	@Test
	public void testGetForeignKeyConstraintNameReturnsCorrectNamesForMetaData() {
		TableMetaData table = new TableMetaData("Table");
		ColumnMetaData column = new ColumnMetaData("Column");
		assertEquals("Table_Column_fkey", this.unitUnderTest.getForeignKeyConstraintName(table, column));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testGetUniqueConstraintNameReturnsTheRightNameForThePassedColumn() {
		TableModel t = this.dataModel.getTableByName("TestTabelle");
		ColumnModel c = t.getColumnByName("Referenz");
		assertEquals("TestTabelle_Referenz_key", this.unitUnderTest.getUniqueConstraintName(c));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testIsPrimaryKeyIndexReturnsFalseIfTheIndexNameEndsNotWithPKEY() {
		assertFalse(this.unitUnderTest.isPrimaryKeyIndex("endsWith"));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testIsPrimaryKeyIndexReturnsTrueIfTheIndexNameEndsWithPKEY() {
		assertTrue(this.unitUnderTest.isPrimaryKeyIndex("endsWith_pkey"));
	}

	/**
	 * @changed OLI 01.08.2013 - Added.
	 */
	@Test
	public void testSetSchemaStatementReturnACorrectStatement() {
		assertEquals("SET search_path TO \"schema\";", this.unitUnderTest.setSchemaStatement("schema"));
	}

}