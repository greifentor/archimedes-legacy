package archimedes.codegenerators.service;

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
public class ServiceNameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private ServiceNameGenerator unitUnderTest;

	@DisplayName("tests for SO class names")
	@Nested
	class SOClassNameTests {

		@Test
		void getSOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getSOClassName(table);
			});
		}

		@Test
		void getSOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getSOClassName(null));
		}

		@Test
		void getSOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectSOName() {
			// Prepare
			String expected = "TestTableSO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectSOName() {
			// Prepare
			String expected = "TableSO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectSOName() {
			// Prepare
			String expected = "TableNameSO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectSOName() {
			// Prepare
			String expected = "TableNameSO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectSOName() {
			// Prepare
			String expected = "TableNameSO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectSOName() {
			// Prepare
			String expected = "TableSO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectSOName() {
			// Prepare
			String expected = "TSO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectSOName() {
			// Prepare
			String expected = "TSO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for SO package names")
	@Nested
	class SOPackageNameTests {

		@Test
		void getSOPackageName_PassANullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getSOPackageName(null));
		}

		@Test
		void getSOPackageName_PassAValidTableModel_ReturnsACorrecSOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".service.model";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getSOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecSOName() {
			// Prepare
			String expected = "service.model";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getSOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecSOName() {
			// Prepare
			String expected = "service.model";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getSOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

}