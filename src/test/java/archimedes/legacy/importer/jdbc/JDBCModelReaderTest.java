/*
 * ModelReaderTest.java
 *
 * 20.09.2018
 *
 * (c) by ollie
 */
package archimedes.legacy.importer.jdbc;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import de.ollie.archimedes.alexandrian.service.so.ColumnSO;
import de.ollie.archimedes.alexandrian.service.so.DatabaseSO;
import de.ollie.archimedes.alexandrian.service.so.ForeignKeySO;
import de.ollie.archimedes.alexandrian.service.so.IndexSO;
import de.ollie.archimedes.alexandrian.service.so.ReferenceSO;
import de.ollie.archimedes.alexandrian.service.so.SchemeSO;
import de.ollie.archimedes.alexandrian.service.so.TableSO;
import de.ollie.archimedes.alexandrian.service.so.TypeSO;

/**
 * Unit tests of class "ModelReader".
 *
 * @author O.Lieshoff
 */
public class JDBCModelReaderTest {

	private static final String COLUMN_NAME_1 = "Id";
	private static final String COLUMN_NAME_2 = "Name";
	private static final String COLUMN_NAME_3 = "Salary";
	private static final String COLUMN_NAME_4 = "field4";
	private static final String COLUMN_NAME_5 = "field5";
	private static final String COLUMN_NAME_6 = "field6";
	private static final String COLUMN_NAME_7 = "field7";
	private static final String COLUMN_NAME_8 = "field8";
	private static final String COLUMN_NAME_9 = "field9";

	private static final String INDEX_NAME = "Index1";

	private static final String SCHEME_NAME = "PUBLIC";

	private static final String TABLE_NAME_1 = "TestTable";
	private static final String TABLE_NAME_2 = "AnotherTestTable";

	private JDBCModelReader unitUnderTest = null;

//	@Rule
//	public TemporaryFolder temp = new TemporaryFolder();

	private Connection connectionSource = null;
	private DefaultDBObjectFactory factory = new DefaultDBObjectFactory();
	private String dbNameSource = "sourceDB";
	private DBTypeConverter typeConverter = new DBTypeConverter();

	private TypeSO typeInteger = new TypeSO() //
			.setSqlType(Types.INTEGER);
	private TypeSO typeVarchar100 = new TypeSO() //
			.setSqlType(Types.VARCHAR) //
			.setLength(100);
	private TypeSO typeNumeric102 = new TypeSO() //
			.setSqlType(Types.NUMERIC) //
			.setLength(10) //
			.setPrecision(2);
	private TypeSO typeChar12 = new TypeSO() //
			.setSqlType(Types.CHAR) //
			.setLength(12);
	private TypeSO typeDecimal2412 = new TypeSO() //
			.setSqlType(Types.DECIMAL) //
			.setLength(24) //
			.setPrecision(12);

	@BeforeAll
	public static void setUpClass() {
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
		} catch (Exception e) {
			System.err.println("ERROR: failed to load HSQLDB JDBC driver.");
			e.printStackTrace();
			return;
		}
	}

	@BeforeEach
	public void setUp() throws Exception {
		this.connectionSource = getConnection(this.dbNameSource);
		this.unitUnderTest = new JDBCModelReader(this.factory, this.typeConverter, this.connectionSource, SCHEME_NAME,
				false, null);
	}

	private Connection getConnection(String dbName) throws Exception {
		return DriverManager.getConnection("jdbc:hsqldb:mem:" + dbName + ";shutdown=true", "SA", "");
	}

	@AfterEach
	public void tearDown() throws Exception {
		if (this.connectionSource != null) {
			this.connectionSource.close();
		}
	}

	@Test
	public void readModel_ValidConnectionOfAnEmptyDatabasePassed_ReturnsAnEmptyModel() throws Exception {
		// Prepare
		DatabaseSO expected = new DatabaseSO().setName("database")
				.addScheme(this.factory.createScheme(SCHEME_NAME, new ArrayList<>()));

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

	private void createDatabase(Connection connection) throws Exception {
		Statement stmt = connection.createStatement();
		stmt.execute("CREATE TABLE " + TABLE_NAME_1 + " (" //
				+ COLUMN_NAME_1 + " INTEGER, "//
				+ COLUMN_NAME_2 + " VARCHAR(100), " //
				+ COLUMN_NAME_3 + " NUMERIC(10,2))");
		stmt.close();
	}

	@Test
	public void readModel_ValidConnectionWithATablePassed_ReturnsTheModelOfTheDatabaseSuitableToTheConnection()
			throws Exception {
		// Prepare
		createDatabase(this.connectionSource);

		TableSO table = new TableSO() //
				.setName(TABLE_NAME_1.toUpperCase()) //
				.setColumns(new ArrayList<>());
		List<TableSO> tables = new ArrayList<>();
		tables.add(table);

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertEquals(TABLE_NAME_1.toUpperCase(), returned.getSchemes().get(0).getTables().get(0).getName());
	}

	@Test
	public void readModel_ValidConnectionWithATableAndColumnsPassed_ReturnsTheModelOfTheDatabaseSuitableToTheConnection()
			throws Exception {
		// Prepare
		createDatabase(this.connectionSource);

		List<ColumnSO> columns = new ArrayList<>();
		columns.add(new ColumnSO() //
				.setName(COLUMN_NAME_1.toUpperCase()) //
				.setType(new TypeSO() //
						.setSqlType(Types.INTEGER)));
		columns.add(new ColumnSO() //
				.setName(COLUMN_NAME_2.toUpperCase()) //
				.setType(new TypeSO() //
						.setSqlType(Types.VARCHAR) //
						.setLength(100)));
		columns.add(new ColumnSO() //
				.setName(COLUMN_NAME_3.toUpperCase()) //
				.setType(new TypeSO() //
						.setSqlType(Types.NUMERIC) //
						.setLength(10) //
						.setPrecision(2)));
		TableSO table = new TableSO() //
				.setName(TABLE_NAME_1.toUpperCase()) //
				.addColumns(columns.toArray(new ColumnSO[0]));
		List<TableSO> tables = new ArrayList<>();
		tables.add(table);
		DatabaseSO expected = new DatabaseSO() //
				.setName("database") //
				.addScheme(new SchemeSO() //
						.setName(SCHEME_NAME) //
						.setTables(tables));

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

	@Test
	public void readModel_ValidConnectionWithTwoTableAndColumnsPassed_ReturnsTheModelOfTheDatabaseSuitableToTheConnection()
			throws Exception {
		// Prepare
		createDatabaseWithTwoTables(this.connectionSource);

		List<ColumnSO> columns1 = new ArrayList<>();
		columns1.add(new ColumnSO() //
				.setName(COLUMN_NAME_1.toUpperCase()) //
				.setType(this.typeInteger));
		columns1.add(new ColumnSO() //
				.setName(COLUMN_NAME_2.toUpperCase()) //
				.setType(this.typeVarchar100));
		columns1.add(new ColumnSO() //
				.setName(COLUMN_NAME_3.toUpperCase()) //
				.setType(this.typeNumeric102));
		TableSO table1 = new TableSO() //
				.setName(TABLE_NAME_1.toUpperCase()).addColumns(columns1.toArray(new ColumnSO[0]));
		List<ColumnSO> columns2 = new ArrayList<>();
		columns2.add(new ColumnSO() //
				.setName(COLUMN_NAME_1.toUpperCase()) //
				.setType(this.typeInteger));
		columns2.add(new ColumnSO() //
				.setName(COLUMN_NAME_2.toUpperCase()) //
				.setType(this.typeVarchar100));
		columns2.add(new ColumnSO() //
				.setName(COLUMN_NAME_3.toUpperCase()) //
				.setType(this.typeNumeric102));
		TableSO table2 = new TableSO() //
				.setName(TABLE_NAME_2.toUpperCase()) //
				.addColumns(columns2.toArray(new ColumnSO[0]));
		List<TableSO> tables = new ArrayList<>();
		tables.add(table1);
		tables.add(table2);
		DatabaseSO expected = new DatabaseSO() //
				.setName("database") //
				.addScheme(new SchemeSO() //
						.setName(SCHEME_NAME) //
						.setTables(tables));

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

	private void createDatabaseWithTwoTables(Connection connection) throws Exception {
		Statement stmt = connection.createStatement();
		stmt.execute("CREATE TABLE " + TABLE_NAME_1 + " (" //
				+ COLUMN_NAME_1 + " INTEGER, " //
				+ COLUMN_NAME_2 + " VARCHAR(100), " //
				+ COLUMN_NAME_3 + " NUMERIC(10,2))");
		stmt.execute("CREATE TABLE " + TABLE_NAME_2 + " (" //
				+ COLUMN_NAME_1 + " INTEGER, " //
				+ COLUMN_NAME_2 + " VARCHAR(100), " //
				+ COLUMN_NAME_3 + " NUMERIC(10,2))");
		stmt.close();
	}

	@Test
	public void readModel_ValidConnectionWithATableWithFieldsOfAllTypesPassed_ReturnsTheModelOfTheDatabaseSuitableToTheConnection()
			throws Exception {
		// Prepare
		createDatabaseWithATableWithFieldsAllTypes(this.connectionSource);

		List<ColumnSO> columns = new ArrayList<>();
		columns.add(new ColumnSO().setName(COLUMN_NAME_1.toUpperCase()).setType(this.typeInteger));
		columns.add(new ColumnSO().setName(COLUMN_NAME_2.toUpperCase()).setType(this.typeVarchar100));
		columns.add(new ColumnSO().setName(COLUMN_NAME_3.toUpperCase()).setType(this.typeNumeric102));
		columns.add(new ColumnSO().setName(COLUMN_NAME_4.toUpperCase()).setType(this.typeChar12));
		columns.add(new ColumnSO().setName(COLUMN_NAME_5.toUpperCase()).setType(this.typeDecimal2412));
		TableSO table = new TableSO().setName(TABLE_NAME_1.toUpperCase()).addColumns(columns.toArray(new ColumnSO[0]));
		List<TableSO> tables = new ArrayList<>();
		tables.add(table);
		DatabaseSO expected = new DatabaseSO().setName("database")
				.addScheme(new SchemeSO().setName(SCHEME_NAME).setTables(tables));

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

	private void createDatabaseWithATableWithFieldsAllTypes(Connection connection) throws Exception {
		Statement stmt = connection.createStatement();
		stmt.execute("CREATE TABLE " + TABLE_NAME_1 + " (" + COLUMN_NAME_1 + " INTEGER, " + COLUMN_NAME_2
				+ " VARCHAR(100), " + COLUMN_NAME_3 + " NUMERIC(10,2), " + COLUMN_NAME_4 + " CHAR(12), " + COLUMN_NAME_5
				+ " DECIMAL(24,12))");
		stmt.close();
	}

	@Test
	public void readlModel_ValidConnectionWithAnIndexOnTable_ReturnsTheModelWithTheIndex() throws Exception {
		// Prepare
		Statement stmt = connectionSource.createStatement();
		stmt.execute("CREATE TABLE " + TABLE_NAME_1 + " (" + COLUMN_NAME_1 + " INTEGER, " + COLUMN_NAME_2
				+ " VARCHAR(100), " + COLUMN_NAME_3 + " NUMERIC(10,2), " + COLUMN_NAME_4 + " CHAR(12), " + COLUMN_NAME_5
				+ " DECIMAL(24,12))");
		stmt.execute("CREATE INDEX " + INDEX_NAME + " ON " + TABLE_NAME_1 + " (" + COLUMN_NAME_1 + ", " + COLUMN_NAME_2
				+ ")");
		stmt.execute("CREATE UNIQUE INDEX U" + INDEX_NAME + " ON " + TABLE_NAME_1 + " (" + COLUMN_NAME_3 + ", "
				+ COLUMN_NAME_4 + ")"); // To check, that unique indices are not respected.
		stmt.close();

		List<ColumnSO> columns = new ArrayList<>();
		columns.add(new ColumnSO().setName(COLUMN_NAME_1.toUpperCase()).setType(this.typeInteger));
		columns.add(new ColumnSO().setName(COLUMN_NAME_2.toUpperCase()).setType(this.typeVarchar100));
		columns.add(new ColumnSO().setName(COLUMN_NAME_3.toUpperCase()).setType(this.typeNumeric102));
		columns.add(new ColumnSO().setName(COLUMN_NAME_4.toUpperCase()).setType(this.typeChar12));
		columns.add(new ColumnSO().setName(COLUMN_NAME_5.toUpperCase()).setType(this.typeDecimal2412));
		TableSO table = new TableSO().setName(TABLE_NAME_1.toUpperCase()).addColumns(columns.toArray(new ColumnSO[0]));
		List<TableSO> tables = new ArrayList<>();
		tables.add(table);

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertThat(returned.getSchemes().get(0).getTables().get(0).getIndices().size(), equalTo(1));
		IndexSO index = returned.getSchemes().get(0).getTables().get(0).getIndices().get(0);
		assertThat(index.getName(), equalTo(INDEX_NAME.toUpperCase()));
		assertThat(index.getColumns().size(), equalTo(2));
		assertThat(index.getColumns().get(0),
				equalTo(new ColumnSO().setName(COLUMN_NAME_1.toUpperCase()).setType(typeInteger).setTable(table)));
		assertThat(index.getColumns().get(1),
				equalTo(new ColumnSO().setName(COLUMN_NAME_2.toUpperCase()).setType(typeVarchar100).setTable(table)));
	}

	@Test
	public void readModel_ValidConnectionWithAForeignKeyPassed_ReturnsTheModelOfTheDatabaseMatchingToTheConnection()
			throws Exception {
		// Prepare
		createDatabaseWithATableWithForeingnKey(this.connectionSource);

		String fkName = "FK_" + TABLE_NAME_2 + "_" + COLUMN_NAME_2 + "_" + TABLE_NAME_1 + "_" + COLUMN_NAME_1;
		List<ColumnSO> columns = new ArrayList<>();
		ColumnSO columnRef = new ColumnSO() //
				.setName(COLUMN_NAME_2.toUpperCase()) //
				.setType(this.typeInteger);
		columns.add(columnRef);
		TableSO tableRef = new TableSO() //
				.setName(TABLE_NAME_2.toUpperCase()).addColumns(columns.toArray(new ColumnSO[0]));
		List<TableSO> tables = new ArrayList<>();
		tables.add(tableRef);
		columns = new ArrayList<>();
		ColumnSO columnTarget = new ColumnSO() //
				.setName(COLUMN_NAME_1.toUpperCase()) //
				.setType(this.typeInteger);
		columns.add(columnTarget);
		TableSO tableTarget = new TableSO() //
				.setName(TABLE_NAME_1.toUpperCase()) //
				.addColumns(columns.toArray(new ColumnSO[0]));
		tables.add(tableTarget);
		ForeignKeySO foreignKey = new ForeignKeySO() //
				.setName(fkName.toUpperCase()) //
				.addReferences(new ReferenceSO() //
						.setReferencedColumn(columnTarget) //
						.setReferencingColumn(columnRef));
		tableRef.addForeignKeys(foreignKey);

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertEquals(foreignKey.toString(), returned.getSchemes().get(0).getTableByName(TABLE_NAME_2).get()
				.getForeignKeyByName(fkName).get().toString());
	}

	private void createDatabaseWithATableWithForeingnKey(Connection connection) throws Exception {
		Statement stmt = connection.createStatement();
		stmt.execute("CREATE TABLE " + TABLE_NAME_1 + " (" //
				+ COLUMN_NAME_1 + " INTEGER PRIMARY KEY)");
		stmt.execute("CREATE TABLE " + TABLE_NAME_2 + " (" //
				+ COLUMN_NAME_2 + " INTEGER)");
		stmt.execute("ALTER TABLE " + TABLE_NAME_2 //
				+ " ADD CONSTRAINT FK_" + TABLE_NAME_2 + "_" + COLUMN_NAME_2 + "_" + TABLE_NAME_1 + "_" + COLUMN_NAME_1 //
				+ " FOREIGN KEY (" + COLUMN_NAME_2 + ") REFERENCES " + TABLE_NAME_1 + "(" + COLUMN_NAME_1 + ")");
		stmt.close();
	}

	private void createDatabase_TableWithPrimaryKey(Connection connection) throws Exception {
		Statement stmt = connection.createStatement();
		stmt.execute("CREATE TABLE " + TABLE_NAME_1 + " (" //
				+ COLUMN_NAME_1 + " INTEGER PRIMARY KEY, "//
				+ COLUMN_NAME_2 + " VARCHAR(100))");
		stmt.close();
	}

	@Test
	public void readModel_ValidConnectionWithATableWithPrimaryKeyPassed_ReturnsTheModelOfTheDatabaseSuitableToTheConnection()
			throws Exception {
		// Prepare
		createDatabase_TableWithPrimaryKey(this.connectionSource);

		List<ColumnSO> columns = new ArrayList<>();
		columns.add(new ColumnSO() //
				.setName(COLUMN_NAME_1.toUpperCase()) //
				.setNullable(false) //
				.setPkMember(true) //
				.setType(this.typeInteger));
		columns.add(new ColumnSO() //
				.setName(COLUMN_NAME_2.toUpperCase()) //
				.setType(this.typeVarchar100));
		TableSO table = new TableSO() //
				.setName(TABLE_NAME_1.toUpperCase()) //
				.addColumns(columns.toArray(new ColumnSO[0]));
		List<TableSO> tables = new ArrayList<>();
		tables.add(table);
		DatabaseSO expected = new DatabaseSO() //
				.setName("database") //
				.addScheme(new SchemeSO(). //
						setName(SCHEME_NAME) //
						.setTables(tables));

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

	private void createDatabase_TableWithNotNullColumn(Connection connection) throws Exception {
		Statement stmt = connection.createStatement();
		stmt.execute("CREATE TABLE " + TABLE_NAME_1 + " (" //
				+ COLUMN_NAME_1 + " INTEGER NOT NULL, "//
				+ COLUMN_NAME_2 + " VARCHAR(100))");
		stmt.close();
	}

	@Test
	public void readModel_ValidConnectionWithATableWithNotNullColumnPassed_ReturnsTheModelOfTheDatabaseSuitableToTheConnection()
			throws Exception {
		// Prepare
		createDatabase_TableWithNotNullColumn(this.connectionSource);

		List<ColumnSO> columns = new ArrayList<>();
		columns.add(new ColumnSO() //
				.setName(COLUMN_NAME_1.toUpperCase()) //
				.setNullable(false) //
				.setType(this.typeInteger));
		columns.add(new ColumnSO() //
				.setName(COLUMN_NAME_2.toUpperCase()) //
				.setType(this.typeVarchar100));
		TableSO table = new TableSO() //
				.setName(TABLE_NAME_1.toUpperCase()) //
				.addColumns(columns.toArray(new ColumnSO[0]));
		List<TableSO> tables = new ArrayList<>();
		tables.add(table);
		DatabaseSO expected = new DatabaseSO() //
				.setName("database") //
				.addScheme(new SchemeSO(). //
						setName(SCHEME_NAME) //
						.setTables(tables));

		// Run
		DatabaseSO returned = this.unitUnderTest.readModel();

		// Check
		assertEquals(expected.toString(), returned.toString());
	}

}