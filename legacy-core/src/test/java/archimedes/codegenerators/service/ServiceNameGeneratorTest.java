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
import archimedes.scheme.Option;

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

	@DisplayName("Tests for IdModel class names")
	@Nested
	class IdModelClassNameTests {

		@Test
		void getIdModelClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getIdModelClassName(table);
			});
		}

		@Test
		void getIdModelClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getIdModelClassName(null));
		}

		@Test
		void getIdModelClassName_PassTableModelWithNameCamelCase_ReturnsACorrectIdModelName() {
			// Prepare
			String expected = "TestTableId";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getIdModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdModelClassName_PassTableModelWithNameUpperCase_ReturnsACorrectIdModelName() {
			// Prepare
			String expected = "TableId";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getIdModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdModelClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectIdModelName() {
			// Prepare
			String expected = "TableNameId";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getIdModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdModelClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectIdModelName() {
			// Prepare
			String expected = "TableNameId";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getIdModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdModelClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectIdModelName() {
			// Prepare
			String expected = "TableNameId";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getIdModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdModelClassName_PassTableModelWithNameLowerCase_ReturnsACorrectIdModelName() {
			// Prepare
			String expected = "TableId";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getIdModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdModelClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectIdModelName() {
			// Prepare
			String expected = "TId";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getIdModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getIdModelClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectIdModelName() {
			// Prepare
			String expected = "TId";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getIdModelClassName(table);
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
			assertEquals("core", unitUnderTest.getServicePackageName(model, null));
		}

		@Test
		void getServicePackageName_PassAValidDataModel_ReturnsACorrecServiceName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getServicePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServicePackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "core";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getServicePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServicePackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "core";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getServicePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for Model class names")
	@Nested
	class ModelClassNameTests {

		@Test
		void getModelClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getModelClassName(table);
			});
		}

		@Test
		void getModelClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getModelClassName(null));
		}

		@Test
		void getModelClassName_PassTableModelWithNameCamelCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TestTable";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelWithNameUpperCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "Table";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TableName";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TableName";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TableName";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelWithNameLowerCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "Table";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "T";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "T";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelWithAlternateModelClassSuffix_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TableModel";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX, "Model"));
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelNullAsWithAlternateModelClassSuffix_ReturnsACorrectModelName() {
			// Prepare
			String expected = "Table";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX))
					.thenReturn(null);
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelClassName_PassTableModelEmptyAsWithAlternateModelClassSuffix_ReturnsACorrectModelName() {
			// Prepare
			String expected = "Table";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(ServiceNameGenerator.ALTERNATE_MODEL_CLASS_NAME_SUFFIX, ""));
			// Run
			String returned = unitUnderTest.getModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for model package names")
	@Nested
	class ModelPackageNameTests {

		@Test
		void getModelPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getModelPackageName(null, table));
		}

		@Test
		void getModelPackageName_PassANullValueAsTable_ReturnsDefaultValue() {
			assertEquals("core.model", unitUnderTest.getModelPackageName(model, null));
		}

		@Test
		void getModelPackageName_PassAValidDataModel_ReturnsACorrecModelName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core.model";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getModelPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectModelName() {
			// Prepare
			String expected = "core.model";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getModelPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectModelName() {
			// Prepare
			String expected = "core.model";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getModelPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getModelPackageName_PassAValidDataModelWithSetWithAlternatePackageNameForModelClasses_ReturnsACorrectModelName() {
			// Prepare
			String alternatePackageName = "alternate.package.name";
			String expected = alternatePackageName;
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePackageName);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_MODEL_PACKAGE_NAME)).thenReturn(option);
			// Run
			String returned = unitUnderTest.getModelPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for persistence port interface names")
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
		void getPersistencePortInterfaceName_PassTableModelWithNameCamelCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TestTablePersistencePort";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameUpperCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TablePersistencePort";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TableNamePersistencePort";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithNameLowerCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TablePersistencePort";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelNameSingleUpperCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TPersistencePort";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelNameSinglelowerCase_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TPersistencePort";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelWithAlternateModelClassSuffix_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TablePP";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX))
					.thenReturn(
							new Option(ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX, "PP"));
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelNullAsWithAlternateModelClassSuffix_ReturnsACorrectModelName() {
			// Prepare
			String expected = "TablePersistencePort";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX))
					.thenReturn(null);
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortInterfaceName_PassTableModelEmptyAsWithAlternateModelClassSuffix_ReturnsACorrectModelName() {
			// Prepare
			String expected = "Table";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX))
					.thenReturn(new Option(ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_INTERFACE_NAME_SUFFIX, ""));
			// Run
			String returned = unitUnderTest.getPersistencePortInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for persistence port package names")
	@Nested
	class PersistencePortPackageNameTests {

		@Test
		void getPersistencePortPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPersistencePortPackageName(null, table));
		}

		@Test
		void getPersistencePortPackageName_PassANullValueAsTable_ReturnsDefaultValue() {
			assertEquals("core.service.port.persistence", unitUnderTest.getPersistencePortPackageName(model, null));
		}

		@Test
		void getPersistencePortPackageName_PassAValidDataModel_ReturnsACorrecModelName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core.service.port.persistence";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getPersistencePortPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectModelName() {
			// Prepare
			String expected = "core.service.port.persistence";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getPersistencePortPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectModelName() {
			// Prepare
			String expected = "core.service.port.persistence";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getPersistencePortPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPersistencePortPackageName_PassAValidDataModelWithSetWithAlternatePackageNameForModelClasses_ReturnsACorrectModelName() {
			// Prepare
			String alternatePackageName = "alternate.package.name";
			String expected = alternatePackageName;
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePackageName);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_PERSISTENCE_PORT_PACKAGE_NAME))
					.thenReturn(option);
			// Run
			String returned = unitUnderTest.getPersistencePortPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

}