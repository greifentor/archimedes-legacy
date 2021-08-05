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

	@DisplayName("Tests for persistence port interface names")
	@Nested
	class ApplicationClassNameTests {

		@Test
		void passAModelWithAnEmptyApplicationName_ReturnsApplicationOnly() {
			assertEquals("Application", unitUnderTest.getApplicationClassName(model));
		}

		@Test
		void passAModelWithApplicationNameSet_ReturnsApplicationWithApplicationNamePrefix() {
			// Prepare
			when(model.getApplicationName()).thenReturn("AppName");
			// Run & Check
			assertEquals("AppNameApplication", unitUnderTest.getApplicationClassName(model));
		}

		@Test
		void passAModelWithApplicationNameWithSpacesSet_ReturnsApplicationWithApplicationNamePrefix() {
			// Prepare
			when(model.getApplicationName()).thenReturn("App Name");
			// Run & Check
			assertEquals("AppNameApplication", unitUnderTest.getApplicationClassName(model));
		}

		@Test
		void passAModelWithApplicationNameWithNonASCIICharactersSet_ReturnsApplicationWithApplicationNamePrefix() {
			// Prepare
			when(model.getApplicationName()).thenReturn("+App-=Name+*/");
			// Run & Check
			assertEquals("AppNameApplication", unitUnderTest.getApplicationClassName(model));
		}

		@Test
		void passNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getApplicationClassName(null));
		}

	}

	@DisplayName("Tests for application package names")
	@Nested
	class ApplicationPackageNameTests {

		@Test
		void getApplicationPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getApplicationPackageName(null, table));
		}

		@Test
		void getApplicationPackageName_PassANullValueAsTable_ReturnsDefaultValue() {
			assertEquals("", unitUnderTest.getApplicationPackageName(model, null));
		}

		@Test
		void getApplicationPackageName_PassAValidDataModel_ReturnsACorrectPackageName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME;
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getApplicationPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getApplicationPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectPackageName() {
			// Prepare
			String expected = "";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getApplicationPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getApplicationPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectPAckageName() {
			// Prepare
			String expected = "";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getApplicationPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getApplicationPackageName_PassAValidDataModelWithSetWithAlternatePackageNameForModelClasses_ReturnsACorrectPackageName() {
			// Prepare
			String alternatePackageName = "alternate.package.name";
			String expected = alternatePackageName;
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePackageName);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_APPLICATION_PACKAGE_NAME))
					.thenReturn(option);
			// Run
			String returned = unitUnderTest.getApplicationPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

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

	@DisplayName("Tests for Service interface names")
	@Nested
	class ServiceClassNameTests {

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
		void getServiceInterfaceName_PassTableModelWithNameCamelCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TestTableService";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameUpperCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableService";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameService";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelWithNameLowerCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableService";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelNameSingleUpperCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TService";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelNameSinglelowerCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TService";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfaceName_PassTableModelEmptyAsWithAlternateClassSuffix_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TablePort";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX))
					.thenReturn(new Option(ServiceNameGenerator.ALTERNATE_SERVICE_INTERFACE_NAME_SUFFIX, "Port"));
			// Run
			String returned = unitUnderTest.getServiceInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for Service interface package names")
	@Nested
	class ServicePackageNameTests {

		@Test
		void getServiceInterfacePackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getServiceInterfacePackageName(null, table));
		}

		@Test
		void getServiceInterfacePackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("core.service", unitUnderTest.getServiceInterfacePackageName(model, null));
		}

		@Test
		void getServiceInterfacePackageName_PassAValidDataModel_ReturnsACorrecServiceName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core.service";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getServiceInterfacePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfacePackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "core.service";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getServiceInterfacePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfacePackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "core.service";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getServiceInterfacePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceInterfacePackageName_PassAValidDataModelWithSetWithAlternatePackageName_ReturnsACorrectPackageName() {
			// Prepare
			String alternatePackageName = "alternate.package.name";
			String expected = alternatePackageName;
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME))
					.thenReturn(new Option(ServiceNameGenerator.ALTERNATE_SERVICE_INTERFACE_PACKAGE_NAME, alternatePackageName));
			// Run
			String returned = unitUnderTest.getServiceInterfacePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for Service impl class names")
	@Nested
	class ServiceImplClassNameTests {

		@Test
		void getServiceImplClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getServiceImplClassName(table);
			});
		}

		@Test
		void getServiceImplClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getServiceImplClassName(null));
		}

		@Test
		void getServiceImplClassName_PassTableModelWithNameCamelCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TestTableServiceImpl";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplClassName_PassTableModelWithNameUpperCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableServiceImpl";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameServiceImpl";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameServiceImpl";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableNameServiceImpl";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplClassName_PassTableModelWithNameLowerCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TableServiceImpl";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TServiceImpl";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "TServiceImpl";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplClassName_PassTableModelEmptyAsWithAlternateClassSuffix_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableAdapter";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(ServiceNameGenerator.ALTERNATE_SERVICE_IMPL_CLASS_NAME_SUFFIX, "Adapter"));
			// Run
			String returned = unitUnderTest.getServiceImplClassName(table);
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
			assertEquals("core.service.impl", unitUnderTest.getServiceImplPackageName(model, null));
		}

		@Test
		void getServiceImplPackageName_PassAValidDataModel_ReturnsACorrecServiceName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core.service.impl";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getServiceImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "core.service.impl";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getServiceImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectServiceName() {
			// Prepare
			String expected = "core.service.impl";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getServiceImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceImplPackageName_PassAValidDataModelWithSetWithAlternatePackageName_ReturnsACorrectPackageName() {
			// Prepare
			String alternatePackageName = "alternate.package.name";
			String expected = alternatePackageName;
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_SERVICE_IMPL_PACKAGE_NAME))
					.thenReturn(new Option(ServiceNameGenerator.ALTERNATE_SERVICE_IMPL_PACKAGE_NAME, alternatePackageName));
			// Run
			String returned = unitUnderTest.getServiceImplPackageName(model, table);
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

	@DisplayName("Tests for page class names")
	@Nested
	class PageClassNameTests {

		@Test
		void getPageClassName_ReturnsACorrectPageClassName() {
			assertEquals("Page", unitUnderTest.getPageClassName());
		}

	}

	@DisplayName("Tests for page package names")
	@Nested
	class PagePackageNameTests {

		@Test
		void getPagePackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPagePackageName(null, table));
		}

		@Test
		void getPagePackageName_PassANullValueAsTable_ReturnsDefaultValue() {
			assertEquals("core.model", unitUnderTest.getPagePackageName(model, null));
		}

		@Test
		void getPagePackageName_PassAValidDataModel_ReturnsACorrecModelName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core.model";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getPagePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPagePackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectModelName() {
			// Prepare
			String expected = "core.model";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getPagePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPagePackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectModelName() {
			// Prepare
			String expected = "core.model";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getPagePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPagePackageName_PassAValidDataModelWithSetWithAlternatePackageNameForModelClasses_ReturnsACorrectModelName() {
			// Prepare
			String alternatePackageName = "alternate.package.name";
			String expected = alternatePackageName;
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePackageName);
			when(model.getOptionByName(ServiceNameGenerator.ALTERNATE_PAGE_PACKAGE_NAME)).thenReturn(option);
			// Run
			String returned = unitUnderTest.getPagePackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("Tests for page parameters class names")
	@Nested
	class PageParametersClassNameTests {

		@Test
		void getPageParametersClassName_ReturnsACorrectPageParametersClassName() {
			assertEquals("PageParameters", unitUnderTest.getPageParametersClassName());
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