package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
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
import archimedes.model.OptionModel;
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

	@DisplayName("Tests for IdSO class names")
	@Nested
	class IdSOClassNameTests {

		@Test
		void getIdSOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getIdSOClassName(table);
			});
		}

		@Test
		void getIdSOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getIdSOClassName(null));
		}

		@Test
		void getIdSOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectIdSOName() {
			// Prepare
			String expected = "TestTableIdSO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getIdSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectIdSOName() {
			// Prepare
			String expected = "TableIdSO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getIdSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectIdSOName() {
			// Prepare
			String expected = "TableNameIdSO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getIdSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectIdSOName() {
			// Prepare
			String expected = "TableNameIdSO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getIdSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectIdSOName() {
			// Prepare
			String expected = "TableNameIdSO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getIdSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectIdSOName() {
			// Prepare
			String expected = "TableIdSO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getIdSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectIdSOName() {
			// Prepare
			String expected = "TIdSO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getIdSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectIdSOName() {
			// Prepare
			String expected = "TIdSO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getIdSOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for Service class names")
	@Nested
	class ServiceClassNameTests {

		@Test
		void getServiceClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getServiceClassName(table);
			});
		}

		@Test
		void getServiceClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getServiceClassName(null));
		}

		@Test
		void getServiceClassName_PassTableModelWithNameCamelCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TestTableService";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getServiceClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceClassName_PassTableModelWithNameUpperCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableService";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getServiceClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getServiceClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getServiceClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getServiceClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceClassName_PassTableModelWithNameLowerCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableService";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getServiceClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TService";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getServiceClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TService";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getServiceClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for Service package names")
	@Nested
	class ServicePackageNameTests {

		@Test
		void getServicePackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getServicePackageName(null, table));
		}

		@Test
		void getServicePackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("service", unitUnderTest.getServicePackageName(model, null));
		}

		@Test
		void getServicePackageName_PassAValidDataModel_ReturnsACorrecServiceName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".service";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getServicePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServicePackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "service";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getServicePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServicePackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "service";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getServicePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for SO class names")
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

	@DisplayName("Tests for SO package names")
	@Nested
	class SOPackageNameTests {

		@Test
		void getSOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getSOPackageName(null, table));
		}

		@Test
		void getSOPackageName_PassANullValueAsTable_ReturnsDefaultValue() {
			assertEquals("service.model", unitUnderTest.getSOPackageName(model, null));
		}

		@Test
		void getSOPackageName_PassAValidDataModel_ReturnsACorrecSOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".service.model";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getSOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectSOName() {
			// Prepare
			String expected = "service.model";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getSOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectSOName() {
			// Prepare
			String expected = "service.model";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getSOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSOPackageName_PassAValidDataModelWithSetWithAlternatePackageNameForSOClasses_ReturnsACorrectSOName() {
			// Prepare
			String alternatePackageName = "alternate.package.name";
			String expected = alternatePackageName;
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePackageName);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_PACKAGE_SO_CLASS)).thenReturn(option);
			// Run
			String returned = unitUnderTest.getSOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

}