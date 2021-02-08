/*
 * GenericSQLScriptFactoryTest.java
 *
 * 25.10.2013
 *
 * (c) by ollie
 *
 */

package archimedes.legacy.sql.factories;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Vector;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import archimedes.legacy.metadata.SequenceMetaData;
import archimedes.legacy.model.ColumnMetaData;
import archimedes.legacy.model.TableMetaData;
import archimedes.legacy.scheme.Diagramm;
import archimedes.legacy.sql.SQLScriptFactory;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.SequenceModel;
import archimedes.model.TableModel;
import archimedes.model.UniqueMetaData;
import corent.db.DBExecMode;

/**
 * Tests of the class <CODE>GenericSQLScriptFactory</CODE>.
 * 
 * @author ollie
 * 
 * @changed OLI 25.10.2013 - Added.
 */

public class GenericSQLScriptFactoryTest {

	private static final DBExecMode MODE = DBExecMode.STANDARDSQL;
	private static final String QUOTES = "\"";

	private ColumnModel column = null;
	private DataModel dataModel = null;
	private DomainModel domain = null;
	private GenericSQLScriptFactory unitUnderTest = null;
	private List<UniqueMetaData> uniques = null;
	private SequenceMetaData sequenceMetaData = null;
	private SequenceModel sequence = null;
	private TableModel table = null;
	private TableMetaData tableMetaData = null;

	/**
	 * @throws Exception If the setup routine fails.
	 * 
	 * @changed OLI 25.10.2013 - Added.
	 */
	@BeforeEach
	public void setUp() throws Exception {
		this.dataModel = new Diagramm();
		this.unitUnderTest = new GenericSQLScriptFactory(QUOTES, this.dataModel, MODE);
		this.unitUnderTest.sqlFactory = mock(SQLScriptFactory.class);
		this.column = mock(ColumnModel.class);
		this.domain = mock(DomainModel.class);
		this.sequence = mock(SequenceModel.class);
		this.table = mock(TableModel.class);
		this.uniques = new Vector<UniqueMetaData>();
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddColumnStatementDelegatedCorrectly() {
		this.unitUnderTest.alterTableAddColumnStatement(this.column, true, true);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableAddColumnStatement(this.column, true, true);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddConstraintForComplexUniquesDelegatedCorrectly() {
		this.unitUnderTest.alterTableAddConstraintForComplexUniques(this.table, this.uniques);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableAddConstraintForComplexUniques(this.table,
				this.uniques);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAddPrimaryKeyConstraintDelegatedCorrectly() {
		this.unitUnderTest.alterTableAddPrimaryKeyConstraint(this.table);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableAddPrimaryKeyConstraint(this.table);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAlterColumnSetDataTypeStatementDelegatedCorrectly() {
		this.unitUnderTest.alterTableAlterColumnSetDataTypeStatement(this.column, true);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableAlterColumnSetDataTypeStatement(this.column, true);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableAlterColumnSetDefaultStatementDelegatedCorrectly() {
		this.unitUnderTest.alterTableAlterColumnSetDefaultStatement(this.column);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableAlterColumnSetDefaultStatement(this.column);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableDropColumnDelegatedCorrectly() {
		ColumnMetaData cmd = new ColumnMetaData("A");
		this.unitUnderTest.alterTableDropColumn(this.table, cmd);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableDropColumn(this.table, cmd);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableDropConstraintForComplexUniquesDelegatedCorrectly() {
		this.unitUnderTest.alterTableDropConstraintForComplexUniques(this.dataModel, this.uniques);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableDropConstraintForComplexUniques(this.dataModel,
				this.uniques);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableDropPrimaryKeyConstraintDelegatedCorrectly() {
		this.unitUnderTest.alterTableDropPrimaryKeyConstraint(this.table);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableDropPrimaryKeyConstraint(this.table);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testAlterTableModifyConstraintNotNullDelegatedCorrectly() {
		this.unitUnderTest.alterTableModifyConstraintNotNull(this.column);
		verify(this.unitUnderTest.sqlFactory, times(1)).alterTableModifyConstraintNotNull(this.column);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testConstructorSetsDataModelCorrectly() {
		assertEquals(this.dataModel, this.unitUnderTest.getDataModel());
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testConstructorSetsDBModeCorrectly() {
		assertEquals(MODE, this.unitUnderTest.getDBMode());
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testConstructorSetsQuotesCorrectly() {
		assertEquals(QUOTES, this.unitUnderTest.getQuotes());
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testConstructorSetsSQLFactoryCorrectlyForPostgreSQL() {
		this.unitUnderTest = new GenericSQLScriptFactory(QUOTES, this.dataModel, DBExecMode.POSTGRESQL);
		assertTrue(this.unitUnderTest.sqlFactory instanceof PostgreSQLScriptFactory);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testConstructorSetsSQLFactoryCorrectlyForStandardSQL() {
		this.unitUnderTest = new GenericSQLScriptFactory(QUOTES, this.dataModel, DBExecMode.STANDARDSQL);
		assertTrue(this.unitUnderTest.sqlFactory instanceof StandardSQLScriptFactory);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testCreateDomainStatementDelegatedCorrectly() {
		this.unitUnderTest.createDomainStatement(this.domain);
		verify(this.unitUnderTest.sqlFactory, times(1)).createDomainStatement(this.domain);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testCreateSequenceStatementDelegatedCorrectly() {
		this.unitUnderTest.createSequenceStatement(this.sequence);
		verify(this.unitUnderTest.sqlFactory, times(1)).createSequenceStatement(this.sequence);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testCreateSimpleIndexStatementDelegatedCorrectly() {
		this.unitUnderTest.createSimpleIndexStatement(this.column);
		verify(this.unitUnderTest.sqlFactory, times(1)).createSimpleIndexStatement(this.column);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Disabled
	@Test
	public void testCreateTableStatementDelegatedCorrectly() {
		this.unitUnderTest.createTableStatement(this.table, true, true);
		verify(this.unitUnderTest.sqlFactory, times(1)).createTableStatement(this.table, true, true);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropForeignKeyConstraintColumnDelegatedCorrectly() {
		this.unitUnderTest.dropForeignKeyConstraint(this.column);
		verify(this.unitUnderTest.sqlFactory, times(1)).dropForeignKeyConstraint(this.column);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropForeignKeyConstraintStringStringDelegatedCorrectly() {
		this.unitUnderTest.dropForeignKeyConstraint("A", "B");
		verify(this.unitUnderTest.sqlFactory, times(1)).dropForeignKeyConstraint("A", "B");
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropSequenceStatementDelegatedCorrectly() {
		this.unitUnderTest.dropSequenceStatement(this.sequenceMetaData);
		verify(this.unitUnderTest.sqlFactory, times(1)).dropSequenceStatement(this.sequenceMetaData);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropSimpleIndexStatementDelegatedCorrectly() {
		this.unitUnderTest.dropSimpleIndexStatement(this.column);
		verify(this.unitUnderTest.sqlFactory, times(1)).dropSimpleIndexStatement(this.column);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testDropTableStatementDelegatedCorrectly() {
		this.unitUnderTest.dropTableStatement(this.tableMetaData);
		verify(this.unitUnderTest.sqlFactory, times(1)).dropTableStatement(this.tableMetaData);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testGetForeignKeyConstraintNameDelegatedCorrectly() {
		this.unitUnderTest.getForeignKeyConstraintName(this.column);
		verify(this.unitUnderTest.sqlFactory, times(1)).getForeignKeyConstraintName(this.column);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testGetUniqueConstraintNameDelegatedCorrectly() {
		this.unitUnderTest.getUniqueConstraintName(this.column);
		verify(this.unitUnderTest.sqlFactory, times(1)).getUniqueConstraintName(this.column);
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testIsPrimaryKeyIndexDelegatedCorrectly() {
		this.unitUnderTest.isPrimaryKeyIndex("A");
		verify(this.unitUnderTest.sqlFactory, times(1)).isPrimaryKeyIndex("A");
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testSetSchemaStatementDelegatedCorrectly() {
		this.unitUnderTest.setSchemaStatement("A");
		verify(this.unitUnderTest.sqlFactory, times(1)).setSchemaStatement("A");
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testSetSchemaStatementReturnsNullPassingAnEmptySchemeName() {
		assertNull(this.unitUnderTest.setSchemaStatement(""));
	}

	/**
	 * @changed OLI 25.10.2013 - Added.
	 */
	@Test
	public void testSetSchemaStatementReturnsNullPassingANullPointer() {
		assertNull(this.unitUnderTest.setSchemaStatement(null));
	}

}