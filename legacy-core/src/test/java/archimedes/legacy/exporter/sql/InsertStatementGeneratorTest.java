package archimedes.legacy.exporter.sql;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.sql.Types;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.Domain;
import archimedes.model.ColumnModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;

@ExtendWith(MockitoExtension.class)
public class InsertStatementGeneratorTest {

	private static final String COLUMN_NAME_1 = "ColumnName1";
	private static final String COLUMN_NAME_2 = "ColumnName2";
	private static final String COLUMN_NAME_3 = "ColumnName3";
	private static final Integer INT_VALUE = 42;
	private static final String STRING_VALUE = "StringValue";
	private static final String TABLE_NAME = "TableName";

	@Mock
	private ColumnModel column1, column2, column3;
	@Mock
	private TableModel table;

	@InjectMocks
	private InsertStatementGenerator unitUnderTest;

	@Nested
	class TestsForMethod_generate_ColumnModelArr_ObjectArr {

		@Test
		void passAnEmptyArrayForColumns_throwsAnException() {
			assertThrows(
					IllegalArgumentException.class,
					() -> unitUnderTest.generate(new ColumnModel[0], new Object[] { ";op" }));
		}

		@Test
		void passANullValueAsColumnsArray_throwsAnException() {
			assertThrows(IllegalArgumentException.class, () -> unitUnderTest.generate(null, new Object[] { ";op" }));
		}

		@Test
		void passAnEmptyArrayAsValues_throwsAnException() {
			assertThrows(
					IllegalArgumentException.class,
					() -> unitUnderTest.generate(new ColumnModel[] { column1 }, new Object[0]));
		}

		@Test
		void passANullValueAsValuesArray_throwsAnException() {
			assertThrows(
					IllegalArgumentException.class,
					() -> unitUnderTest.generate(new ColumnModel[] { column1 }, null));
		}

		@Test
		void passAArraysWithDifferentLength_throwsAnException() {
			assertThrows(
					IllegalArgumentException.class,
					() -> unitUnderTest.generate(new ColumnModel[] { column1 }, new Object[] { ";op", ":o)" }));
		}

		@Test
		void passArraysWithMatchingData_oneColumn_returnsACorrectInsertStatement() {
			// Prepare
			when(column1.getName()).thenReturn(COLUMN_NAME_1);
			when(column1.getTable()).thenReturn(table);
			when(table.getName()).thenReturn(TABLE_NAME);
			String expected = "INSERT INTO " + TABLE_NAME + " (" + COLUMN_NAME_1 + ") VALUES ('" + STRING_VALUE + "');";
			// Run
			String returned = unitUnderTest.generate(new ColumnModel[] { column1 }, new Object[] { STRING_VALUE });
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
							new ColumnModel[] { column1, column2, column3 },
							new Object[] { STRING_VALUE, INT_VALUE, null });
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_createDummyData_ColumnModelArr {

		@Test
		void passANullValue_returnsANullValue() {
			assertNull(unitUnderTest.createDummyData(null));
		}

		@Test
		void passAColumnsWithDomainBIGINT_returnsATheCorrectDummyValue() {
			// Prepare
			Object expected = 0L;
			DomainModel domain = new Domain("name", Types.BIGINT, 0, 0);
			when(column1.getDomain()).thenReturn(domain);
			// Run
			Object[] returned = unitUnderTest.createDummyData(new ColumnModel[] { column1 });
			// Check
			assertEquals(1, returned.length);
			assertEquals(expected, returned[0]);
		}

		@Test
		void passAColumnsWithDomainDOUBLE_returnsATheCorrectDummyValue() {
			// Prepare
			Object expected = 0.0D;
			DomainModel domain = new Domain("name", Types.DOUBLE, 0, 0);
			when(column1.getDomain()).thenReturn(domain);
			// Run
			Object[] returned = unitUnderTest.createDummyData(new ColumnModel[] { column1 });
			// Check
			assertEquals(1, returned.length);
			assertEquals(expected, returned[0]);
		}

		@Test
		void passAColumnsWithDomainINTEGER_returnsATheCorrectDummyValue() {
			// Prepare
			Object expected = 0;
			DomainModel domain = new Domain("name", Types.INTEGER, 0, 0);
			when(column1.getDomain()).thenReturn(domain);
			// Run
			Object[] returned = unitUnderTest.createDummyData(new ColumnModel[] { column1 });
			// Check
			assertEquals(1, returned.length);
			assertEquals(expected, returned[0]);
		}

		@Test
		void passAColumnsWithDomainVARCHAR_returnsATheCorrectDummyValue() {
			// Prepare
			Object expected = "String0";
			DomainModel domain = new Domain("name", Types.VARCHAR, 50, 0);
			when(column1.getDomain()).thenReturn(domain);
			// Run
			Object[] returned = unitUnderTest.createDummyData(new ColumnModel[] { column1 });
			// Check
			assertEquals(1, returned.length);
			assertEquals(expected, returned[0]);
		}

	}

}
