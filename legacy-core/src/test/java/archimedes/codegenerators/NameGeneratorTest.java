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
import archimedes.model.TableModel;

@ExtendWith(MockitoExtension.class)
public class NameGeneratorTest {

	@Mock
	private ColumnModel column;
	@Mock
	private TableModel table;

	@InjectMocks
	private NameGenerator unitUnderTest;

	@DisplayName("tests for column names to attribute names")
	@Nested
	class AttributeNameTests {

		@Test
		public void getAttributeName_PassColumnModelWithEmptyName_ThrowsException() {
			// Prepare
			when(column.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getAttributeName(column);
			});
		}

		@Test
		public void getAttributeName_PassANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getAttributeName(null));
		}

		@Test
		public void getAttributeName_PassAColumnNameStartsWithLowerCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("column");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getAttributeName_PassAColumnNameStartsWithUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("Column");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getAttributeName_PassAColumnNameCompleteUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "column";
			when(column.getName()).thenReturn("COLUMN");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getAttributeName_PassAColumnNameSingleUpperCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "c";
			when(column.getName()).thenReturn("C");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getAttributeName_PassAColumnNameSingleLowerCase_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "c";
			when(column.getName()).thenReturn("c");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getAttributeName_PassAColumnNameOnlyUpperCaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("COLUMN_NAME");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getAttributeName_PassAColumnNameOnlyMixedCaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("Column_Name");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getAttributeName_PassAColumnNameOnlyLowercaseWithUnderScore_ReturnsACorrectAttributeName() {
			// Prepare
			String expected = "columnName";
			when(column.getName()).thenReturn("column_name");
			// Run
			String returned = unitUnderTest.getAttributeName(column);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getAttributeName_SetUseQualifiedColumnName_ReturnsACorrectAttributeName() {
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
		public void getDBOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDBOClassName(table);
			});
		}

		@Test
		public void getDBOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDBOClassName(null));
		}

		@Test
		public void getDBOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TestTableDBO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDBOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDBO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDBOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDBOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDBOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableNameDBO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDBOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDBO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDBOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TDBO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDBOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TDBO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DTO class names")
	@Nested
	class DTOClassNameTests {

		@Test
		public void getDTOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDTOClassName(table);
			});
		}

		@Test
		public void getDTOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDTOClassName(null));
		}

		@Test
		public void getDTOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TestTableDTO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDTOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDTOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDTOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDTOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDTOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDTOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		public void getDTOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

}