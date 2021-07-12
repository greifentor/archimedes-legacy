package archimedes.legacy.exporter.sql;

import archimedes.legacy.scheme.Domain;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Types;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class InsertStatementGeneratorTest {

	private static final String COLUMN_NAME_1 = "ColumnName1";
	private static final String COLUMN_NAME_2 = "ColumnName2";
	private static final String COLUMN_NAME_3 = "ColumnName3";
	private static final Integer INT_VALUE = 42;
	private static final String STRING_VALUE = "StringValue";
	private static final String TABLE_NAME = "TableName";

	@Mock
	private ColumnModel column1, column2, column3, column4;
	@Mock
	private TableModel table;

	@InjectMocks
	private InsertStatementGenerator unitUnderTest;

	private static Stream<Arguments> getTestArgumentsForCreateDummyData() {
		return Stream.of(
				Arguments.of(Types.BIGINT, 0, 0, 0L),
				Arguments.of(Types.BOOLEAN, 0, 0, false),
				Arguments.of(Types.DECIMAL, 0, 0, 0.0D),
				Arguments.of(Types.DOUBLE, 0, 0, 0.0D),
				Arguments.of(Types.FLOAT, 0, 0, 0.0F),
				Arguments.of(Types.INTEGER, 0, 0, 0),
				Arguments.of(Types.NUMERIC, 0, 0, 0.0D),
				Arguments.of(Types.VARCHAR, 0, 0, "String0"));
	}

	@Nested
	class TestsForMethod_createDummyData_ColumnModelArr {

		@Test
		void passANullValue_returnsANullValue() {
			assertNull(unitUnderTest.createDummyData(null));
		}

		@ParameterizedTest
		@MethodSource("archimedes.legacy.exporter.sql.InsertStatementGeneratorTest#getTestArgumentsForCreateDummyData")
		void passAColumnsWithDomain_returnsATheCorrectDummyValue(int type, int length, int nks, Object expected) {
			// Prepare
			DomainModel domain = new Domain("name", type, length, nks);
			when(column1.getDomain()).thenReturn(domain);
			// Run
			Object[] returned = unitUnderTest.createDummyData(new ColumnModel[]{column1});
			// Check
			assertEquals(1, returned.length);
			assertEquals(expected, returned[0]);
		}

		@Test
		void passSomeColumnsWithDomains_returnsCorrectProgressingValues() {
			// Prepare
			Object[] expected = new Object[] {false, 1.0D, "String2", 3};
			when(column1.getDomain()).thenReturn(new Domain("name1",Types.BOOLEAN, 0, 0));
			when(column2.getDomain()).thenReturn(new Domain("name2", Types.NUMERIC,10, 2));
			when(column3.getDomain()).thenReturn(new Domain("name3", Types.VARCHAR, 50, 0));
			when(column4.getDomain()).thenReturn(new Domain("name4", Types.INTEGER, 0, 0));
			// Run
			Object[] returned = unitUnderTest.createDummyData(new ColumnModel[] {column1, column2, column3, column4});
			// Check
			assertArrayEquals(expected , returned);
		}

	}

	@Nested
	class TestsForMethod_generate_ColumnModelArr_ObjectArr {

		@Test
		void passAnEmptyArrayForColumns_throwsAnException() {
			assertThrows(
					IllegalArgumentException.class,
					() -> unitUnderTest.generate(new ColumnModel[0], new Object[]{";op"}));
		}

		@Test
		void passANullValueAsColumnsArray_throwsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.generate(null, new Object[]{";op"}));
		}

		@Test
		void passAnEmptyArrayAsValues_throwsAnException() {
			assertThrows(
					IllegalArgumentException.class,
					() -> unitUnderTest.generate(new ColumnModel[]{column1}, new Object[0]));
		}

		@Test
		void passANullValueAsValuesArray_throwsAnException() {
			assertThrows(
					IllegalArgumentException.class,
					() -> unitUnderTest.generate(new ColumnModel[]{column1}, null));
		}

		@Test
		void passAArraysWithDifferentLength_throwsAnException() {
			assertThrows(
					IllegalArgumentException.class,
					() -> unitUnderTest.generate(
							new ColumnModel[]{column1},
							new Object[]{
									";op",
									":o)"
							}));
		}

		@Test
		void passArraysWithMatchingData_oneColumn_returnsACorrectInsertStatement() {
			// Prepare
			when(column1.getName()).thenReturn(COLUMN_NAME_1);
			when(column1.getTable()).thenReturn(table);
			when(table.getName()).thenReturn(TABLE_NAME);
			String expected = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME_1 + ") VALUES ('" + STRING_VALUE +
					"');";
			// Run
			String returned = unitUnderTest.generate(new ColumnModel[]{column1}, new Object[]{STRING_VALUE});
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void passArraysWithMatchingData_moreThanOneColumn_returnsACorrectInsertStatement() {
			// Prepare
			when(column1.getName()).thenReturn(COLUMN_NAME_1);
			when(column1.getTable()).thenReturn(table);
			when(column2.getName()).thenReturn(COLUMN_NAME_2);
			when(column3.getName()).thenReturn(COLUMN_NAME_3);
			when(table.getName()).thenReturn(TABLE_NAME);
			String expected = "INSERT INTO " + TABLE_NAME
					+ " ("
					+ COLUMN_NAME_1
					+ ", "
					+ COLUMN_NAME_2
					+ ", "
					+ COLUMN_NAME_3
					+ ") VALUES ('"
					+ STRING_VALUE
					+ "', "
					+ INT_VALUE
					+ ", NULL"
					+ ");";
			// Run
			String returned = unitUnderTest
					.generate(
							new ColumnModel[]{
									column1,
									column2,
									column3
							},
							new Object[]{
									STRING_VALUE,
									INT_VALUE,
									null
							});
			// Check
			assertEquals(expected, returned);
		}

	}

}
