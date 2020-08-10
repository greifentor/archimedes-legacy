/*
 * StandardSQLScriptFactoryTest.java
 *
 * 25.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Types;
import java.util.List;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.ColumnModel;
import archimedes.legacy.model.DataModel;
import archimedes.legacy.model.DiagrammModel;
import archimedes.legacy.model.DomainModel;
import archimedes.legacy.model.SequenceModel;
import archimedes.legacy.model.TabellenModel;
import archimedes.legacy.model.TabellenspaltenModel;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.model.TableModel;
import archimedes.legacy.model.UniqueMetaData;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.scheme.Domain;
import archimedes.legacy.scheme.Sequence;
import archimedes.legacy.scheme.Tabelle;
import archimedes.legacy.scheme.Tabellenspalte;
import archimedes.legacy.scheme.View;
import archimedes.legacy.sql.SQLScriptFactory;

/**
 * Tests of the class <CODE>StandardSQLScriptFactory</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 25.10.2013 - Added.
 */

public class StandardSQLScriptFactoryTest {

	private static final String COLUMN_DESCRIPTION = "Description";
	private static final String COLUMN_ID = "Id";
	private static final String COLUMN_NUMBER = "Number";
	private static final String COMMENT = "bla bla bla";
	private static final Domain DOMAIN_IDENT = new Domain("Ident", Types.BIGINT, 0, 0);
	private static final Domain DOMAIN_NUMERIC = new Domain("NumericValue", Types.NUMERIC, 10, 2);
	private static final Domain DOMAIN_VARCHAR = new Domain("VarCharValue", Types.VARCHAR, 50, 0);
	private static final String TABLE_NAME = "Table";
	private static final String QUOTES = "\"";

	private DataModel dataModel = null;
	private List<UniqueMetaData> uniques = null;
	private SQLScriptFactory unitUnderTest = null;
	private TableModel table = null;

	/**
	 * @throws Exception If the setup routine fails.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.dataModel = new Diagramm();
		this.dataModel.addDomain(DOMAIN_IDENT);
		this.dataModel.addDomain(DOMAIN_NUMERIC);
		this.dataModel.addDomain(DOMAIN_VARCHAR);
		this.dataModel.setAdditionalSQLCodePostReducingCode("/* THE CODE */");
		this.table = this.createTable(TABLE_NAME, this.createStandardColumns(), COMMENT);
		this.dataModel.addTable(this.table);
		this.unitUnderTest = new StandardSQLScriptFactory(QUOTES, this.dataModel, null);
	}

	private TabellenspaltenModel[] createStandardColumns() {
		return new TabellenspaltenModel[] { this.createColumn(COLUMN_ID, true, DOMAIN_IDENT, false, false, false),
				this.createColumn(COLUMN_DESCRIPTION, false, DOMAIN_VARCHAR, true, false, false),
				this.createColumn(COLUMN_NUMBER, false, DOMAIN_NUMERIC, false, false, false) };
	}

	private TabellenModel createTable(String name, TabellenspaltenModel[] columns, String comment) {
		TabellenModel t = new Tabelle(new View("", "bla", false), 0, 0, (DiagrammModel) this.dataModel);
		t.setName(name);
		t.setComment(comment);
		t.setActiveInApplication(true);
		t.setDraft(false);
		for (TabellenspaltenModel tsm : columns) {
			t.addColumn(tsm);
			tsm.setTable(t);
		}
		return t;
	}

	private TabellenspaltenModel createColumn(String name, boolean primaryKey, Domain domain, boolean notNull,
			boolean hasIndex, boolean unique) {
		TabellenspaltenModel c = new Tabellenspalte(name, domain);
		c.setNotNull(notNull);
		c.setPrimaryKey(primaryKey);
		c.setIndex(hasIndex);
		c.setUnique(unique);
		return c;
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddColumnStatementTheCorrectStatementToAddAColumnNNllsDmns() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		DomainModel domain = column.getDomain();
		assertEquals("ALTER TABLE \"" + TABLE_NAME + "\" ADD COLUMN \"" + COLUMN_DESCRIPTION + "\" " + domain.getName()
				+ ";", this.unitUnderTest.alterTableAddColumnStatement(column, true, false));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddColumnStatementTheCorrectStatementToAddAColumnNNllsNoDmns() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		DomainModel domain = column.getDomain();
		assertEquals(
				"ALTER TABLE \"" + TABLE_NAME + "\" ADD COLUMN \"" + COLUMN_DESCRIPTION + "\" "
						+ domain.getType().toUpperCase() + " NOT NULL;",
				this.unitUnderTest.alterTableAddColumnStatement(column, false, true));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddColumnStatementTheCorrectStatementToAddAColumnNullsNoDmns() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		DomainModel domain = column.getDomain();
		assertEquals(
				"ALTER TABLE \"" + TABLE_NAME + "\" ADD COLUMN \"" + COLUMN_DESCRIPTION + "\" "
						+ domain.getType().toUpperCase() + ";",
				this.unitUnderTest.alterTableAddColumnStatement(column, false, false));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddConstraintForComplexUniquesReturnsAWarningComment() {
		this.table.setComplexUniqueSpecification(":o)");
		assertEquals(
				"/* WARNING (" + TABLE_NAME + "): Complex unique constraints cannot be "
						+ "created for Standard-SQL-Modus! */",
				this.unitUnderTest.alterTableAddConstraintForComplexUniques(this.table, this.uniques)[0]);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddPrimaryKeyConstraintReturnsACorrectStatement() {
		String s = this.unitUnderTest.alterTableAddPrimaryKeyConstraint(this.table);
		assertEquals(
				"ALTER TABLE " + QUOTES + TABLE_NAME + QUOTES + " ADD PRIMARY KEY(" + QUOTES + "Id" + QUOTES + ");", s);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAlterColumnSetDataTypeStatementToChangeAColumnNoDomains() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		DomainModel domain = column.getDomain();
		assertEquals(
				"ALTER TABLE \"" + TABLE_NAME + "\" ALTER COLUMN \"" + COLUMN_DESCRIPTION + "\" "
						+ domain.getType().toUpperCase() + ";",
				this.unitUnderTest.alterTableAlterColumnSetDataTypeStatement(column, false));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAlterColumnSetDataTypeStatementToChangeAColumnWithDomains() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		DomainModel domain = column.getDomain();
		assertEquals("ALTER TABLE \"" + TABLE_NAME + "\" ALTER COLUMN \"" + COLUMN_DESCRIPTION + "\" "
				+ domain.getName() + ";", this.unitUnderTest.alterTableAlterColumnSetDataTypeStatement(column, true));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAlterColumnSetDefaultStatementReturnsAWarningComment() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		assertEquals(
				"/* WARNING (" + TABLE_NAME + "." + COLUMN_DESCRIPTION + "): Default "
						+ "constraints cannot be created for standard Standard-SQL-Modus! */",
				this.unitUnderTest.alterTableAlterColumnSetDefaultStatement(column));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableDropColumnReturnsACorrectStatement() {
		TableModel t = this.dataModel.getTableByName(TABLE_NAME);
		ColumnMetaData cmd = new ColumnMetaData("Abc");
		String s = this.unitUnderTest.alterTableDropColumn(t, cmd);
		assertNotNull(s);
		assertEquals(s,
				"ALTER TABLE " + QUOTES + TABLE_NAME + QUOTES + " DROP COLUMN " + QUOTES + "Abc" + QUOTES + ";");
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableDropConstraintForComplexUniquesReturnsAWarningComment() {
		this.uniques = new Vector<UniqueMetaData>();
		this.uniques.add(new UniqueMetaData("DUMMY", "DUMMY"));
		assertEquals("/* WARNING: Complex constraints cannot be dropped for Standard-SQL-Modus" + "! */",
				this.unitUnderTest.alterTableDropConstraintForComplexUniques(this.dataModel, this.uniques)[0]);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableDropPrimaryKeyConstraintReturnsAWarningComment() {
		assertEquals("ALTER TABLE " + QUOTES + this.table.getName() + QUOTES + " DROP PRIMARY " + "KEY;",
				this.unitUnderTest.alterTableDropPrimaryKeyConstraint(this.table));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableModifyConstraintNotNullReturnsAWarningComment() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		assertEquals(
				"/* WARNING (" + TABLE_NAME + "." + COLUMN_DESCRIPTION + "): Statement to "
						+ "modify not null cannot be created for Standard-SQL-Modus! */",
				this.unitUnderTest.alterTableModifyConstraintNotNull(column));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testCreateDomainStatementReturnsAWarningComment() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		DomainModel domain = column.getDomain();
		assertEquals("/* WARNING (" + domain.getName() + "): Domain cannot be created for " + "Standard-SQL-Modus! */",
				this.unitUnderTest.createDomainStatement(domain));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testCreateSequenceStatementReturnsAWarningComment() {
		SequenceModel s = new Sequence("A", 1, 2);
		assertEquals("/* WARNING (" + s.getName() + "): Sequence cannot be created for Standard" + "-SQL-Modus! */",
				this.unitUnderTest.createSequenceStatement(s));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsACorrectStatementDomainsNotIgnoreNotNull() {
		assertEquals("CREATE TABLE \"" + TABLE_NAME + "\" (\n" + "    \"" + COLUMN_ID
				+ "\" Ident NOT NULL PRIMARY KEY,\n" + "    \"" + COLUMN_DESCRIPTION + "\" VarCharValue NOT NULL,\n"
				+ "    \"" + COLUMN_NUMBER + "\" NumericValue\n" + ");",
				this.unitUnderTest.createTableStatement(this.table, true, false));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsACorrectStatementNoDomainsIgnoreNotNull() {
		assertEquals("CREATE TABLE \"" + TABLE_NAME + "\" (\n" + "    \"" + COLUMN_ID + "\" BIGINT PRIMARY KEY,\n"
				+ "    \"" + COLUMN_DESCRIPTION + "\" VARCHAR(50),\n" + "    \"" + COLUMN_NUMBER + "\" NUMERIC(10, 2)\n"
				+ ");", this.unitUnderTest.createTableStatement(this.table, false, true));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testCreateTableStatementReturnsACorrectStatementNoDomainsNotIgnoreNotNull() {
		assertEquals(
				"CREATE TABLE \"" + TABLE_NAME + "\" (\n" + "    \"" + COLUMN_ID + "\" BIGINT NOT NULL PRIMARY KEY,\n"
						+ "    \"" + COLUMN_DESCRIPTION + "\" VARCHAR(50) NOT NULL,\n" + "    \"" + COLUMN_NUMBER
						+ "\" NUMERIC(10, 2)\n" + ");",
				this.unitUnderTest.createTableStatement(this.table, false, false));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropForeignKeyConstraintColumnReturnsAWarningComment() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		assertEquals(
				"/* WARNING (" + TABLE_NAME + "." + COLUMN_DESCRIPTION + "): Foreign key "
						+ "contraint removing statement cannot be created for Standard-SQL-Modus! */",
				this.unitUnderTest.dropForeignKeyConstraint(column));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropForeignKeyConstraintTableNameConstraintNameReturnsAWarningComment() {
		assertEquals(
				"/* WARNING (" + TABLE_NAME + ".A): Foreign key contraint removing "
						+ "statement cannot be created for Standard-SQL-Modus! */",
				this.unitUnderTest.dropForeignKeyConstraint(TABLE_NAME, "A"));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropSequenceStatementReturnsAWarningComment() {
		SequenceMetaData smd = new SequenceMetaData("A");
		assertEquals("/* WARNING (A): Sequence dropping statement cannot be created for " + "Standard-SQL-Modus! */",
				this.unitUnderTest.dropSequenceStatement(smd));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropTableStatementReturnsACorrectStatement() {
		TableMetaData tmd = new TableMetaData("A");
		assertEquals("DROP TABLE \"A\";", this.unitUnderTest.dropTableStatement(tmd));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testGetForeignKeyConstraintNameReturnsNull() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		assertNull(this.unitUnderTest.getForeignKeyConstraintName(column));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testGetUniqueConstraintNameReturnsNull() {
		ColumnModel column = this.dataModel.getTableByName(TABLE_NAME).getColumnByName(COLUMN_DESCRIPTION);
		assertNull(this.unitUnderTest.getUniqueConstraintName(column));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testIsPrimaryKeyIndexThrowsAnUnsupportedOperationException() {
		assertTrue(this.unitUnderTest.isPrimaryKeyIndex("A"));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testSetSchemaStatementReturnsAWarningComment() {
		assertEquals("/* WARNING (A): Statement to change to schema cannot be created for " + "Standard-SQL-Modus! */",
				this.unitUnderTest.setSchemaStatement("A"));
	}

}