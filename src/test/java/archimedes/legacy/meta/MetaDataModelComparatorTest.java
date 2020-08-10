/*
 * MetaDataModelComparatorTest.java
 *
 * 14.12.2015
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.meta;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import archimedes.legacy.meta.chops.AbstractChangeOperation;
import archimedes.legacy.meta.chops.columns.AddColumn;
import archimedes.legacy.meta.chops.columns.AlterColumnAddConstraint;
import archimedes.legacy.meta.chops.columns.AlterColumnDataType;
import archimedes.legacy.meta.chops.columns.AlterColumnDropConstraint;
import archimedes.legacy.meta.chops.columns.AlterColumnDropDefault;
import archimedes.legacy.meta.chops.columns.AlterColumnSetDefaultValue;
import archimedes.legacy.meta.chops.columns.ColumnConstraintType;
import archimedes.legacy.meta.chops.columns.DropColumn;
import archimedes.legacy.meta.chops.foreignkeys.AddForeignKeyConstraint;
import archimedes.legacy.meta.chops.foreignkeys.DropForeignKeyConstraint;
import archimedes.legacy.meta.chops.primarykeys.AddPrimaryKeyConstraint;
import archimedes.legacy.meta.chops.primarykeys.DropPrimaryKeyConstraint;
import archimedes.legacy.meta.chops.sequences.AlterSequence;
import archimedes.legacy.meta.chops.sequences.CreateSequence;
import archimedes.legacy.meta.chops.sequences.DropSequence;
import archimedes.legacy.meta.chops.tables.CreateTable;
import archimedes.legacy.meta.chops.tables.DropTable;
import archimedes.legacy.meta.chops.uniques.AddComplexUniqueConstraint;
import archimedes.legacy.util.NameGenerator;
import corent.db.DBType;

/**
 * Tests of the class <CODE>MetaDataModelComparator</CODE>
 * 
 * @author ollie
 * 
 * @changed OLI 14.12.2015 - Added.
 */

public class MetaDataModelComparatorTest {

	private MetaDataModelComparator unitUnderTest = null;

	/**
	 * @changed OLI 14.12.2015 - Added.
	 */
	@BeforeEach
	public void setUp() {
		this.unitUnderTest = new MetaDataModelComparator(new NameGenerator(null));
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testDefaultValueAdditionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").setDefaultValue("4711");
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnSetDefaultValue r0 = (AlterColumnSetDefaultValue) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Author"), r0.getColumn());
		assertEquals(mdm.getTable("Book"), r0.getTable());
	}

	private MetaDataModel createTestModel() {
		MetaDataModel m = new MetaDataModel("TST");
		MetaDataTable tA = new MetaDataTable("Book", false);
		tA.addColumn(new MetaDataColumn(tA, "Id", "Ident", DBType.BIGINT, 0, 0, true, false, null, false));
		tA.addColumn(new MetaDataColumn(tA, "Author", "Ident", DBType.BIGINT, 0, 0, false, false, null, false));
		MetaDataTable tB = new MetaDataTable("Author", false);
		tB.addColumn(new MetaDataColumn(tB, "Id", "Ident", DBType.BIGINT, 0, 0, true, false, null, false));
		tB.addColumn(new MetaDataColumn(tB, "Name", "Name", DBType.VARCHAR, 100, 0, false, false, null, false));
		m.addTable(tA);
		m.addTable(tB);
		return m;
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testDefaultValueChangeDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").setDefaultValue("815");
		mdb.getTable("Book").getColumn("Author").setDefaultValue("4711");
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnSetDefaultValue r0 = (AlterColumnSetDefaultValue) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Author"), r0.getColumn());
		assertEquals(mdm.getTable("Book"), r0.getTable());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testDefaultValueReductionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdb.getTable("Book").getColumn("Author").setDefaultValue("4711");
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnDropDefault r0 = (AlterColumnDropDefault) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Author").getName(), r0.getColumn().getName());
		assertEquals(mdm.getTable("Book").getName(), r0.getTable().getName());
	}

	/**
	 * @changed OLI 16.12.2015 - Added.
	 */
	@Test
	public void testDeprecatedColumnInModelIgnoredCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").setDeprecated(true);
		mdm.getTable("Book").getColumn("Author").setNotNull(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(0, r.length);
	}

	/**
	 * @changed OLI 16.12.2015 - Added.
	 */
	@Test
	public void testDeprecatedTableInDatabaseIgnoredCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdb.getTable("Book").setDeprecated(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(0, r.length);
	}

	/**
	 * @changed OLI 16.12.2015 - Added.
	 */
	@Test
	public void testDeprecatedTableInModelIgnoredCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").setDeprecated(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(0, r.length);
	}

	/**
	 * @changed OLI 16.12.2015 - Added.
	 */
	@Test
	public void testExternalTableInModelIgnoredCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").setExternalTable(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(0, r.length);
	}

	/**
	 * @changed OLI 14.12.2015 - Added.
	 */
	@Test
	public void testForeignKeyAdditionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Book_Author", mdm.getTable("Author").getColumn("Id")));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AddForeignKeyConstraint r0 = (AddForeignKeyConstraint) r[0];
		assertEquals("FK_Book_Author", r0.getForeignKeyConstraint().getName());
		assertEquals(mdm.getTable("Author").getColumn("Id"), r0.getForeignKeyConstraint().getReferencedColumn());
	}

	/**
	 * @changed OLI 14.12.2015 - Added.
	 */
	@Test
	public void testForeignKeyColumnChangeDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Book_Author", mdm.getTable("Author").getColumn("Id")));
		mdb.getTable("Author").getColumn("Id").setName("Author");
		mdb.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Book_Author", mdb.getTable("Author").getColumn("Author")));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdb, mdm);
		assertNotNull(r);
		assertEquals(7, r.length);
		DropForeignKeyConstraint r0 = (DropForeignKeyConstraint) r[0];
		assertEquals("FK_Book_Author", r0.getForeignKeyConstraint().getName());
		assertEquals("Author.Id", r0.getForeignKeyConstraint().getReferencedColumn().getFullName());
		AddColumn r1 = (AddColumn) r[1];
		assertEquals("Author", r1.getTable().getName());
		assertEquals("Author", r1.getColumn().getName());
		DropPrimaryKeyConstraint r2 = (DropPrimaryKeyConstraint) r[2];
		assertEquals("Author", r2.getTable().getName());
		AlterColumnAddConstraint r3 = (AlterColumnAddConstraint) r[3];
		assertEquals("Author", r3.getTable().getName());
		assertEquals("Author", r3.getColumn().getName());
		assertEquals(ColumnConstraintType.NOT_NULL, r3.getConstraintType());
		AddPrimaryKeyConstraint r4 = (AddPrimaryKeyConstraint) r[4];
		assertEquals("Author", r4.getTable().getName());
		AddForeignKeyConstraint r5 = (AddForeignKeyConstraint) r[5];
		assertEquals("FK_Book_Author", r5.getForeignKeyConstraint().getName());
		assertEquals("Author.Author", r5.getForeignKeyConstraint().getReferencedColumn().getFullName());
		DropColumn r6 = (DropColumn) r[6];
		assertEquals("Author", r6.getTable().getName());
		assertEquals("Id", r6.getColumn().getName());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testForeignKeyNameChangeDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Book_Author_2_Author_Id", mdm.getTable("Author").getColumn("Id")));
		mdb.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Book_Author", mdb.getTable("Author").getColumn("Id")));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(2, r.length);
		DropForeignKeyConstraint r0 = (DropForeignKeyConstraint) r[0];
		assertEquals("FK_Book_Author", r0.getForeignKeyConstraint().getName());
		assertEquals("Author.Id", r0.getForeignKeyConstraint().getReferencedColumn().getFullName());
		AddForeignKeyConstraint r1 = (AddForeignKeyConstraint) r[1];
		assertEquals("FK_Book_Author_2_Author_Id", r1.getForeignKeyConstraint().getName());
		assertEquals("Author.Id", r1.getForeignKeyConstraint().getReferencedColumn().getFullName());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testForeignKeyNoChangesDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Book_Author", mdm.getTable("Author").getColumn("Id")));
		mdb.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Book_Author", mdb.getTable("Author").getColumn("Id")));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(0, r.length);
	}

	/**
	 * @changed OLI 14.12.2015 - Added.
	 */
	@Test
	public void testForeignKeyReductionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Book_Author", mdm.getTable("Author").getColumn("Id")));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdb, mdm);
		assertNotNull(r);
		assertEquals(1, r.length);
		DropForeignKeyConstraint r0 = (DropForeignKeyConstraint) r[0];
		assertEquals("FK_Book_Author", r0.getForeignKeyConstraint().getName());
		assertEquals(mdm.getTable("Author").getColumn("Id"), r0.getForeignKeyConstraint().getReferencedColumn());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testNotNullAdditionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").setNotNull(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnAddConstraint r0 = (AlterColumnAddConstraint) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Author"), r0.getColumn());
		assertEquals(ColumnConstraintType.NOT_NULL, r0.getConstraintType());
		assertEquals(mdm.getTable("Book"), r0.getTable());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testNotNullReductionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdb.getTable("Book").getColumn("Author").setNotNull(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnDropConstraint r0 = (AlterColumnDropConstraint) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Author").getName(), r0.getColumn().getName());
		assertEquals(ColumnConstraintType.NOT_NULL, r0.getConstraintType());
		assertEquals(mdm.getTable("Book").getName(), r0.getTable().getName());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testSequenceAdditionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.addSequence(new MetaDataSequence("NewSequence", 100, 50));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		CreateSequence r0 = (CreateSequence) r[0];
		assertEquals(50, r0.getSequence().getIncrementBy());
		assertEquals("NewSequence", r0.getSequence().getName());
		assertEquals(100, r0.getSequence().getStartValue());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testSequenceChangeDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.addSequence(new MetaDataSequence("NewSequence", 100, 51));
		mdb.addSequence(new MetaDataSequence("NewSequence", 100, 50));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterSequence r0 = (AlterSequence) r[0];
		assertEquals(51, r0.getSequence().getIncrementBy());
		assertEquals("NewSequence", r0.getSequence().getName());
		assertEquals(100, r0.getSequence().getStartValue());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testSequenceNoChangeDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.addSequence(new MetaDataSequence("NewSequence", 100, 50));
		mdb.addSequence(new MetaDataSequence("NewSequence", 100, 50));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(0, r.length);
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testSequenceReductionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdb.addSequence(new MetaDataSequence("OldSequence", 100, 50));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		DropSequence r0 = (DropSequence) r[0];
		assertEquals(50, r0.getSequence().getIncrementBy());
		assertEquals("OldSequence", r0.getSequence().getName());
		assertEquals(100, r0.getSequence().getStartValue());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testTableAdditionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		MetaDataTable mdt = new MetaDataTable("NewTable", false);
		mdm.addTable(mdt);
		mdt.addColumn(new MetaDataColumn(mdt, "Id", "Ident", DBType.BIGINT, 0, 0, true, false, null, false));
		mdt.addColumn(new MetaDataColumn(mdt, "Name", "Name", DBType.VARCHAR, 100, 0, false, true, null, false));
		mdt.getColumn("Name").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Name", mdm.getTable("Author").getColumn("Name")));
		mdt.addUniqueConstraint(
				new MetaDataUniqueConstraint("NewTable_Name_key", mdt, new MetaDataColumn[] { mdt.getColumn("Name") }));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(6, r.length);
		CreateTable r0 = (CreateTable) r[0];
		assertEquals(mdm.getTable("NewTable"), r0.getTable());
		AlterColumnAddConstraint r1 = (AlterColumnAddConstraint) r[1];
		assertEquals(mdm.getTable("NewTable").getColumn("Id"), r1.getColumn());
		assertEquals(ColumnConstraintType.PRIMARY_KEY, r1.getConstraintType());
		assertEquals(mdm.getTable("NewTable"), r1.getTable());
		AlterColumnAddConstraint r2 = (AlterColumnAddConstraint) r[2];
		assertEquals("Id", r2.getColumn().getName());
		assertEquals(ColumnConstraintType.NOT_NULL, r2.getConstraintType());
		assertEquals(mdm.getTable("NewTable").getName(), r2.getTable().getName());
		AlterColumnAddConstraint r3 = (AlterColumnAddConstraint) r[3];
		assertEquals(mdm.getTable("NewTable").getColumn("Name"), r3.getColumn());
		assertEquals(ColumnConstraintType.NOT_NULL, r3.getConstraintType());
		assertEquals(mdm.getTable("NewTable"), r3.getTable());
		AddComplexUniqueConstraint r4 = (AddComplexUniqueConstraint) r[4];
		assertEquals(1, r4.getUniqueConstraint().getColumns().length);
		assertEquals("Name", r4.getUniqueConstraint().getColumns()[0].getName());
		assertEquals("NewTable", r4.getUniqueConstraint().getColumns()[0].getTable().getName());
		AddForeignKeyConstraint r5 = (AddForeignKeyConstraint) r[5];
		assertEquals("FK_Name", r5.getForeignKeyConstraint().getName());
		assertEquals(mdm.getTable("Author").getColumn("Name"), r5.getForeignKeyConstraint().getReferencedColumn());
		assertEquals(mdm.getTable("NewTable").getColumn("Name").getFullName(), r5.getColumns()[0].getFullName());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testTableReductionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		MetaDataTable mdt = new MetaDataTable("NewTable", false);
		mdm.addTable(mdt);
		mdt.addColumn(new MetaDataColumn(mdt, "Id", "Ident", DBType.BIGINT, 0, 0, true, false, null, false));
		mdt.addColumn(new MetaDataColumn(mdt, "Name", "Name", DBType.VARCHAR, 100, 0, false, true, null, false));
		mdt.getColumn("Name").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Name", mdm.getTable("Author").getColumn("Name")));
		mdt.getColumn("Name").setUnique(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdb, mdm);
		assertNotNull(r);
		assertEquals(1, r.length);
		DropTable r0 = (DropTable) r[0];
		assertEquals(mdm.getTable("NewTable"), r0.getTable());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testTableReductionWithForeignKeyReferencesDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		MetaDataTable mdt = new MetaDataTable("NewTable", false);
		mdm.addTable(mdt);
		mdt.addColumn(new MetaDataColumn(mdt, "Id", "Ident", DBType.BIGINT, 0, 0, true, false, null, false));
		mdt.addColumn(new MetaDataColumn(mdt, "Name", "Name", DBType.VARCHAR, 100, 0, false, true, null, false));
		mdt.getColumn("Name").setUnique(true);
		mdm.getTable("Book").getColumn("Author").addForeignKeyConstraint(
				new MetaDataForeignKeyConstraint("FK_Author", mdm.getTable("NewTable").getColumn("Id")));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdb, mdm);
		assertNotNull(r);
		assertEquals(2, r.length);
		DropForeignKeyConstraint r0 = (DropForeignKeyConstraint) r[0];
		assertEquals(mdm.getTable("NewTable").getColumn("Id"), r0.getForeignKeyConstraint().getReferencedColumn());
		assertEquals("FK_Author", r0.getForeignKeyConstraint().getName());
		assertEquals(mdb.getTable("Book").getColumn("Author").getFullName(), r0.getColumns()[0].getFullName());
		DropTable r1 = (DropTable) r[1];
		assertEquals(mdm.getTable("NewTable"), r1.getTable());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testTypeChangeDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").setDataType(DBType.INTEGER);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnDataType r0 = (AlterColumnDataType) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Author"), r0.getColumn());
		assertEquals(mdm.getTable("Book"), r0.getTable());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testTypeDecimalPlaceChangeDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").addColumn(new MetaDataColumn(mdm.getTable("Book"), "Price", "Currency", DBType.NUMERIC, 10,
				3, false, false, null, false));
		mdb.getTable("Book").addColumn(new MetaDataColumn(mdb.getTable("Book"), "Price", "Currency", DBType.NUMERIC, 10,
				2, false, false, null, false));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnDataType r0 = (AlterColumnDataType) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Price"), r0.getColumn());
		assertEquals(mdm.getTable("Book"), r0.getTable());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testTypeLengthChangeDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").addColumn(new MetaDataColumn(mdm.getTable("Book"), "Price", "Currency", DBType.NUMERIC, 12,
				2, false, false, null, false));
		mdb.getTable("Book").addColumn(new MetaDataColumn(mdb.getTable("Book"), "Price", "Currency", DBType.NUMERIC, 10,
				2, false, false, null, false));
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnDataType r0 = (AlterColumnDataType) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Price"), r0.getColumn());
		assertEquals(mdm.getTable("Book"), r0.getTable());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testUniqueAdditionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdm.getTable("Book").getColumn("Author").setUnique(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnAddConstraint r0 = (AlterColumnAddConstraint) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Author"), r0.getColumn());
		assertEquals(ColumnConstraintType.UNIQUE, r0.getConstraintType());
		assertEquals(mdm.getTable("Book"), r0.getTable());
	}

	/**
	 * @changed OLI 15.12.2015 - Added.
	 */
	@Test
	public void testUniqueReductionDetectedCorrectly() {
		MetaDataModel mdm = this.createTestModel();
		MetaDataModel mdb = this.createTestModel();
		mdb.getTable("Book").getColumn("Author").setUnique(true);
		AbstractChangeOperation[] r = this.unitUnderTest.compare(mdm, mdb);
		assertNotNull(r);
		assertEquals(1, r.length);
		AlterColumnDropConstraint r0 = (AlterColumnDropConstraint) r[0];
		assertEquals(mdm.getTable("Book").getColumn("Author").getName(), r0.getColumn().getName());
		assertEquals(ColumnConstraintType.UNIQUE, r0.getConstraintType());
		assertEquals(mdm.getTable("Book").getName(), r0.getTable().getName());
	}

}