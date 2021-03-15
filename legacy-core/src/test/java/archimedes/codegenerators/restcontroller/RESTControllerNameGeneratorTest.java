package archimedes.codegenerators.restcontroller;

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
public class RESTControllerNameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private RESTControllerNameGenerator unitUnderTest;

	@DisplayName("tests for DTO class names")
	@Nested
	class DTOClassNameTests {

		@Test
		void getDTOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDTOClassName(table);
			});
		}

		@Test
		void getDTOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDTOClassName(null));
		}

		@Test
		void getDTOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TestTableDTO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDTOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DTO package names")
	@Nested
	class DTOPackageNameTests {

		@Test
		void getDTOPackageName_PassANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getDTOPackageName(null));
		}

		@Test
		void getDTOPackageName_PassAValidTableModel_ReturnsACorrecDTOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".rest.dto";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getDTOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecDTOName() {
			// Prepare
			String expected = "rest.dto";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getDTOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecDTOName() {
			// Prepare
			String expected = "rest.dto";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getDTOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DTO converter class names")
	@Nested
	class DTOConverterClassNameTests {

		@Test
		void getDTOConverterClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDTOConverterClassName(table);
			});
		}

		@Test
		void getDTOConverterClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDTOConverterClassName(null));
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameCamelCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TestTableDTOConverter";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTOConverter";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableNameDTOConverter";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelWithNameLowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TableDTOConverter";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTOConverter";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectDTOName() {
			// Prepare
			String expected = "TDTOConverter";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDTOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DTO converter package names")
	@Nested
	class DTOConverterPackageNameTests {

		@Test
		void getDTOConverterPackageName_PassANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getDTOConverterPackageName(null));
		}

		@Test
		void getDTOConverterPackageName_PassAValidTableModel_ReturnsACorrecDTOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".rest.converter";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getDTOConverterPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecDTOName() {
			// Prepare
			String expected = "rest.converter";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getDTOConverterPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDTOConverterPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecDTOName() {
			// Prepare
			String expected = "rest.converter";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getDTOConverterPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

}