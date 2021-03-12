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

import archimedes.model.TableModel;

@ExtendWith(MockitoExtension.class)
public class NameGeneratorTest {

	@Mock
	private TableModel table;

	@InjectMocks
	private NameGenerator unitUnderTest;

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