package archimedes.codegenerators.temporal;

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
public class TemporalDataNameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private TemporalDataNameGenerator unitUnderTest;

	@DisplayName("Tests for id SO class names")
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

	@DisplayName("Tests for IdSO package names")
	@Nested
	class IdSOClassPackageNameTests {

		@Test
		void getIdSOClassPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getIdSOClassPackageName(null, table));
		}

		@Test
		void getIdSOClassPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("service", unitUnderTest.getIdSOClassPackageName(model, null));
		}

		@Test
		void getIdSOClassPackageName_PassAValidDataModel_ReturnsACorrecIdSOClassPackageName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".service";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getIdSOClassPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectIdSOClassPackageName() {
			// Prepare
			String expected = "service";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getIdSOClassPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdSOClassPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectIdSOClassPackageName() {
			// Prepare
			String expected = "service";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getIdSOClassPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for persistence port names")
	@Nested
	class PersistencePortInterfaceNameTests {

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getPersistencePortInterfaceName(table);
			});
		}

		@Test
		void getPersistencePortInterfaceName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getPersistencePortInterfaceName(null));
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameCamelCase_ReturnsACorrectPersistencePortName() {
			// Prepare
			String expected = "TestTablePersistencePort";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameUpperCase_ReturnsACorrectPersistencePortName() {
			// Prepare
			String expected = "TablePersistencePort";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectPersistencePortName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectPersistencePortName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectPersistencePortName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameLowerCase_ReturnsACorrectPersistencePortName() {
			// Prepare
			String expected = "TablePersistencePort";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelNameSingleUpperCase_ReturnsACorrectPersistencePortName() {
			// Prepare
			String expected = "TPersistencePort";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelNameSinglelowerCase_ReturnsACorrectPersistencePortName() {
			// Prepare
			String expected = "TPersistencePort";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for PersistencePort package names")
	@Nested
	class PersistencePortPackageNameTests {

		@Test
		void getPersistencePortPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPersistencePortPackageName(null, table));
		}

		@Test
		void getPersistencePortPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("service.ports", unitUnderTest.getPersistencePortPackageName(model, null));
		}

		@Test
		void getPersistencePortPackageName_PassAValidDataModel_ReturnsACorrecPersistencePortPackageName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".service.ports";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getPersistencePortPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectPersistencePortPackageName() {
			// Prepare
			String expected = "service.ports";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getPersistencePortPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectPersistencePortPackageName() {
			// Prepare
			String expected = "service.ports";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getPersistencePortPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for service interface names")
	@Nested
	class ServiceInterfaceNameTests {

		@Test
		void getServiceInterfaceName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getServiceInterfaceName(table);
			});
		}

		@Test
		void getServiceInterfaceName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getServiceInterfaceName(null));
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameCamelCase_ReturnsACorrectServiceInterfaceName() {
			// Prepare
			String expected = "TestTableService";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameUpperCase_ReturnsACorrectServiceInterfaceName() {
			// Prepare
			String expected = "TableService";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectServiceInterfaceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectServiceInterfaceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectServiceInterfaceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameLowerCase_ReturnsACorrectServiceInterfaceName() {
			// Prepare
			String expected = "TableService";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelNameSingleUpperCase_ReturnsACorrectServiceInterfaceName() {
			// Prepare
			String expected = "TService";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelNameSinglelowerCase_ReturnsACorrectServiceInterfaceName() {
			// Prepare
			String expected = "TService";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for service interface names")
	@Nested
	class ServiceImplGeneratedClassNameTests {

		@Test
		void getServiceImplGeneratedClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getServiceImplGeneratedClassName(table);
			});
		}

		@Test
		void getServiceImplGeneratedClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getServiceImplGeneratedClassName(null));
		}

		@Test
		void getServiceImplGeneratedClassName_PassTableModelWithNameCamelCase_ReturnsACorrectServiceImplGeneratedClassName() {
			// Prepare
			String expected = "TestTableServiceImplGenerated";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getServiceImplGeneratedClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplGeneratedClassName_PassTableModelWithNameUpperCase_ReturnsACorrectServiceImplGeneratedClassName() {
			// Prepare
			String expected = "TableServiceImplGenerated";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getServiceImplGeneratedClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplGeneratedClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectServiceImplGeneratedClassName() {
			// Prepare
			String expected = "TableNameServiceImplGenerated";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getServiceImplGeneratedClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplGeneratedClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectServiceImplGeneratedClassName() {
			// Prepare
			String expected = "TableNameServiceImplGenerated";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getServiceImplGeneratedClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplGeneratedClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectServiceImplGeneratedClassName() {
			// Prepare
			String expected = "TableNameServiceImplGenerated";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getServiceImplGeneratedClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplGeneratedClassName_PassTableModelWithNameLowerCase_ReturnsACorrectServiceImplGeneratedClassName() {
			// Prepare
			String expected = "TableServiceImplGenerated";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getServiceImplGeneratedClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplGeneratedClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectServiceImplGeneratedClassName() {
			// Prepare
			String expected = "TServiceImplGenerated";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getServiceImplGeneratedClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplGeneratedClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectServiceImplGeneratedClassName() {
			// Prepare
			String expected = "TServiceImplGenerated";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getServiceImplGeneratedClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for Service impl package names")
	@Nested
	class ServiceImplPackageNameTests {

		@Test
		void getServiceImplPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getServiceImplPackageName(null, table));
		}

		@Test
		void getServiceImplPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("service.impl", unitUnderTest.getServiceImplPackageName(model, null));
		}

		@Test
		void getServiceImplPackageName_PassAValidDataModel_ReturnsACorrecServiceName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".service.impl";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getServiceImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "service.impl";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getServiceImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "service.impl";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getServiceImplPackageName(model, table);
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

}