package archimedes.codegenerators;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;

@ExtendWith(MockitoExtension.class)
public class NameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private NameGenerator unitUnderTest;

	@DisplayName("tests for column names to attribute names")
	@Nested
	class AttributeNameTests {

		@Test
		void getAttributeName_PassColumnModelWithEmptyName_ThrowsException() {
			// Prepare
			when(column.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getAttributeName(column);
			});
		}

		@Test
		void getAttributeName_PassANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getAttributeName(null));
		}

		@Test
		void getAttributeName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("column");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("Column");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameCompleteUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("COLUMN");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameSingleUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "c";
			when(column.getName()).thenReturn("C");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameSingleLowerCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "c";
			when(column.getName()).thenReturn("c");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("COLUMN_NAME");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("Column_Name");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("column_name");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getAttributeName_SetUseQualifiedColumnName_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "tableNameColumnName";
			when(table.getName()).thenReturn("TableName");
			when(column.getTable()).thenReturn(table);
			when(column.getName()).thenReturn("column_name");
			// Run
			String returned = unitUnderTest.getAttributeName(column, true);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DBO class names")
	@Nested
	class DBOClassNameTests {

		@Test
		void getDBOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDBOClassName(table);
			});
		}

		@Test
		void getDBOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDBOClassName(null));
		}

		@Test
		void getDBOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTableDBO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDBO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDBO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TDBO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TDBO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

}