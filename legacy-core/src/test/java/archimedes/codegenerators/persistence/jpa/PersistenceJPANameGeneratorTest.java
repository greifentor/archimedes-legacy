package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.DomainModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;

@ExtendWith(MockitoExtension.class)
public class PersistenceJPANameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private PersistenceJPANameGenerator unitUnderTest;

	@Nested
	class DBOClassNameTests_forDomains {

		@Test
		void passANullValue_returnsANullValue() {
			assertNull(unitUnderTest.getDBOClassName((DomainModel) null, null));
		}

		@Test
		void passADomainModel_returnsTheDomainNameAsClassNameWithDBOExtension() {
			// Prepare
			String domainName = "DomainName";
			DomainModel domain = mock(DomainModel.class);
			when(domain.getName()).thenReturn(domainName);
			// Run & Check
			assertEquals(domainName + "DBO", unitUnderTest.getDBOClassName(domain, model));
		}

	}

	@DisplayName("tests for DBO class names for tables")
	@Nested
	class DBOClassNameTests_forTables {

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
			assertNull(unitUnderTest.getDBOClassName((TableModel) null));
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

		@Test
		void getDBOClassName_PassDataModelWithALTERNATE_ENTITY_CLASS_NAME_SUFFIXOption_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableDbo";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(PersistenceJPANameGenerator.ALTERNATE_ENTITY_CLASS_NAME_SUFFIX, "Dbo"))
					.when(model)
					.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_ENTITY_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getDBOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DBO converter class names")
	@Nested
	class DBOConverterClassNameTests {

		@Test
		void getDBOConverterClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getDBOConverterClassName(null, model));
		}

		@Test
		void getDBOConverterClassName_passAValidTable_ReturnsACorrectDBOConverterClassName() {
			// Prepare
			String expected = "TableDBOConverter";
			when(table.getName()).thenReturn("Table");
			// Run
			String returned = unitUnderTest.getDBOConverterClassName(table.getName(), model);
			// Check
			assertEquals(expected, returned);
		}

		@Disabled("TODO OLI: This should also work as described by the test.")
		@Test
		void getDBOConverterClassName_passAValidTableModelWithAlternateClassName_ReturnsACorrectDBOConverterClassName() {
			// Prepare
			String expected = "TableDBOMapper";
			when(table.getName()).thenReturn("Table");
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_DBOCONVERTER_CLASS_NAME_SUFFIX,
									"DBOMapper"));
			// Run
			String returned = unitUnderTest.getDBOConverterClassName(table.getName(), model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DBO converter package names")
	@Nested
	class DBOConverterPackageNameTests {

		@Test
		void getDBOConverterPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getDBOConverterPackageName(null, table));
		}

		@Test
		void getDBOConverterPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("persistence.converter", unitUnderTest.getDBOConverterPackageName(model, null));
		}

		@Test
		void getDBOConverterPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_DBOCONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_DBOCONVERTER_PACKAGE_NAME,
									"persistence.mapper"));
			// Run & Check
			assertEquals("persistence.mapper", unitUnderTest.getDBOConverterPackageName(model, table));
		}

	}

	@DisplayName("tests for DBO package names")
	@Nested
	class DBOPackageNameTests {

		@Test
		void getDBOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getDBOPackageName(null, table));
		}

		@Test
		void getDBOPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("persistence.entity", unitUnderTest.getDBOPackageName(model, null));
		}

		@Test
		void getDBOPackageName_PassAValidTableModel_ReturnsACorrecDBOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".persistence.entity";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getDBOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecDBOName() {
			// Prepare
			String expected = "persistence.entity";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getDBOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecDBOName() {
			// Prepare
			String expected = "persistence.entity";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getDBOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOPackageName_PassAValidTableModelWithMODULEOption_ReturnsACorrecDBOName() {
			// Prepare
			String prefix = "prefix";
			String expected = "prefix.persistence.entity";
			when(model.getBasePackageName()).thenReturn(null);
			when(table.getOptionByName(PersistenceJPANameGenerator.MODULE))
					.thenReturn(new Option(PersistenceJPANameGenerator.MODULE, prefix));
			// Run
			String returned = unitUnderTest.getDBOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDBOPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_ENTITY_PACKAGE_NAME))
					.thenReturn(
							new Option(PersistenceJPANameGenerator.ALTERNATE_ENTITY_PACKAGE_NAME, "persistence.dbos"));
			// Run & Check
			assertEquals("persistence.dbos", unitUnderTest.getDBOPackageName(model, table));
		}

	}

	@DisplayName("tests of the JPA persistence adapter class names.")
	@Nested
	class GeneratedJPAPersistenceAdapterClassNameTests {

		@Test
		void getGeneratedJPAPersistenceAdapterClassName_PassNullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getGeneratedJPAPersistenceAdapterClassName(null));
		}

		@Test
		void getGeneratedJPAPersistenceAdapterClassName_ATableWithName_ReturnsTheCorrectClassName() {
			// Prepare
			String expected = "NameGeneratedJPAPersistenceAdapter";
			when(table.getName()).thenReturn("Name");
			// Run
			String returned = unitUnderTest.getGeneratedJPAPersistenceAdapterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGeneratedJPAPersistenceAdapterClassName_passAValidTableModelWithAlternateClassName_ReturnsACorrectGeneratedJPAPersistenceAdapterClassName() {
			// Prepare
			String expected = "TablePersistenceAdapter";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_GENERATED_ADAPTER_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_GENERATED_ADAPTER_CLASS_NAME_SUFFIX,
									"PersistenceAdapter"));
			// Run
			String returned = unitUnderTest.getGeneratedJPAPersistenceAdapterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests of the generated JPA repository class names.")
	@Nested
	class GeneratedJPARepositoryInterfaceNameTests {

		@Test
		void getGeneratedJPARepositoryInterfaceName_PassNullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getGeneratedJPARepositoryInterfaceName(null));
		}

		@Test
		void getGeneratedJPARepositoryInterfaceName_ATableWithName_ReturnsTheCorrectClassName() {
			// Prepare
			String expected = "NameGeneratedDBORepository";
			when(table.getName()).thenReturn("Name");
			// Run
			String returned = unitUnderTest.getGeneratedJPARepositoryInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGeneratedJPARepositoryInterfaceName_PassDataModelWithALTERNATE_REPOSITORY_CLASS_NAME_SUFFIXOption_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableGeneratedRepo";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX, "Repo"))
					.when(model)
					.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getGeneratedJPARepositoryInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for generated DBO package names")
	@Nested
	class GeneratedJPARepositoryPackageNameTests {

		@Test
		void getGeneratedJPARepositoryPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getGeneratedJPARepositoryPackageName(null, table));
		}

		@Test
		void getGeneratedJPARepositoryPackageName_PassAValidTable_ReturnsACorrectPackageName() {
			assertEquals("persistence.repository", unitUnderTest.getGeneratedJPARepositoryPackageName(model, table));
		}

		@Test
		void getGeneratedJPARepositoryPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_PACKAGE_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_PACKAGE_NAME,
									"persistence.repos"));
			// Run & Check
			assertEquals("persistence.repos", unitUnderTest.getGeneratedJPARepositoryPackageName(model, table));
		}

	}

	@DisplayName("tests of the JPA persistence adapter class names.")
	@Nested
	class JPAPersistenceAdapterClassNameTests {

		@Test
		void getJPAPersistenceAdapterClassName_PassNullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getJPAPersistenceAdapterClassName(null));
		}

		@Test
		void getJPAPersistenceAdapterClassName_ATableWithName_ReturnsTheCorrectClassName() {
			// Prepare
			String expected = "NameJPAPersistenceAdapter";
			when(table.getName()).thenReturn("Name");
			// Run
			String returned = unitUnderTest.getJPAPersistenceAdapterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getJPAPersistenceAdapterClassName_passAValidTableModelWithAlternateClassName_ReturnsACorrectJPAPersistenceAdapterClassName() {
			// Prepare
			String expected = "TablePersistenceAdapter";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX,
									"PersistenceAdapter"));
			// Run
			String returned = unitUnderTest.getJPAPersistenceAdapterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DBO package names")
	@Nested
	class JPAPersistenceAdapterPackageNameTests {

		@Test
		void getJPAPersistenceAdapterPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getJPAPersistenceAdapterPackageName(null, table));
		}

		@Test
		void getJPAPersistenceAdapterPackageName_PassAValidTable_ReturnsACorrectPackageName() {
			assertEquals("persistence", unitUnderTest.getJPAPersistenceAdapterPackageName(model, table));
		}

		@Test
		void getJPAPersistenceAdapterPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_ADAPTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_ADAPTER_PACKAGE_NAME,
									"persistence.adapters"));
			// Run & Check
			assertEquals("persistence.adapters", unitUnderTest.getJPAPersistenceAdapterPackageName(model, table));
		}

	}

	@DisplayName("tests of the JPA repository class names.")
	@Nested
	class JPARepositoryInterfaceNameTests {

		@Test
		void getJPARepositoryInterfaceName_PassNullValue_ReturnsANullValue() {
			assertNull(unitUnderTest.getJPARepositoryInterfaceName(null));
		}

		@Test
		void getJPARepositoryInterfaceName_ATableWithName_ReturnsTheCorrectClassName() {
			// Prepare
			String expected = "NameDBORepository";
			when(table.getName()).thenReturn("Name");
			// Run
			String returned = unitUnderTest.getJPARepositoryInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getJPARepositoryInterfaceName_PassDataModelWithALTERNATE_REPOSITORY_CLASS_NAME_SUFFIXOption_ReturnsACorrectDBOName() {
			// Prepare
			String expected = "TableRepo";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX, "Repo"))
					.when(model)
					.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getJPARepositoryInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for DBO package names")
	@Nested
	class JPARepositoryPackageNameTests {

		@Test
		void getJPARepositoryPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getJPARepositoryPackageName(null, table));
		}

		@Test
		void getJPARepositoryPackageName_PassAValidTable_ReturnsACorrectPackageName() {
			assertEquals("persistence.repository", unitUnderTest.getJPARepositoryPackageName(model, table));
		}

		@Test
		void getJPARepositoryPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_PACKAGE_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_PACKAGE_NAME,
									"persistence.repos"));
			// Run & Check
			assertEquals("persistence.repos", unitUnderTest.getJPARepositoryPackageName(model, table));
		}

	}

	@DisplayName("tests for page converter class names")
	@Nested
	class PageConverterClassNameTests {

		@Test
		void getPageConverterClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageConverterClassName(null));
		}

		@Test
		void getPageConverterClassName_passAValidTable_ReturnsACorrectDBOConverterClassName() {
			// Prepare
			String expected = "PageConverter";
			// Run
			String returned = unitUnderTest.getPageConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page converter package names")
	@Nested
	class PageConverterPackageNameTests {

		@Test
		void getPageConverterPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageConverterPackageName(null, table));
		}

		@Test
		void getPageConverterPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("persistence.converter", unitUnderTest.getPageConverterPackageName(model, null));
		}

		@Test
		void getPageConverterPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_PAGE_CONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_PAGE_CONVERTER_PACKAGE_NAME,
									"persistence.mapper"));
			// Run & Check
			assertEquals("persistence.mapper", unitUnderTest.getPageConverterPackageName(model, table));
		}

	}

	@DisplayName("tests for page model class names")
	@Nested
	class PageModelClassNameTests {

		@Test
		void getPageModelClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageModelClassName(null));
		}

		@Test
		void getPageModelClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "Page";
			// Run
			String returned = unitUnderTest.getPageModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page model package names")
	@Nested
	class PageModelPackageNameTests {

		@Test
		void getPageModelPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageModelPackageName(null, table));
		}

		@Test
		void getPageModelPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("core.model", unitUnderTest.getPageModelPackageName(model, null));
		}

		@Test
		void getPageModelPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_PAGE_MODEL_PACKAGE_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_PAGE_MODEL_PACKAGE_NAME,
									"core.page.model"));
			// Run & Check
			assertEquals("core.page.model", unitUnderTest.getPageModelPackageName(model, table));
		}

	}

	@DisplayName("tests for page parameters model class names")
	@Nested
	class PageParametersModelClassNameTests {

		@Test
		void getPageParametersModelClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageParametersModelClassName(null));
		}

		@Test
		void getPageParametersModelClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "PageParameters";
			// Run
			String returned = unitUnderTest.getPageParametersModelClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page parameters model package names")
	@Nested
	class PageParametersModelPackageNameTests {

		@Test
		void getPageParametersModelPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageParametersModelPackageName(null, table));
		}

		@Test
		void getPageParametersModelPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("core.model", unitUnderTest.getPageParametersModelPackageName(model, null));
		}

		@Test
		void getPageParametersModelPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_MODEL_PACKAGE_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_MODEL_PACKAGE_NAME,
									"core.page.model"));
			// Run & Check
			assertEquals("core.page.model", unitUnderTest.getPageParametersModelPackageName(model, table));
		}

	}

	@DisplayName("tests for page parameters to pageable converter class names")
	@Nested
	class PageParametersToPageableConverterClassNameTests {

		@Test
		void getPageParametersToPageableConverterClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageParametersToPageableConverterClassName(null));
		}

		@Test
		void getPageParametersToPageableConverterClassName_passAValidTable_ReturnsACorrectDBOConverterClassName() {
			// Prepare
			String expected = "PageParametersToPageableConverter";
			// Run
			String returned = unitUnderTest.getPageParametersToPageableConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page parameters to pageable converter package names")
	@Nested
	class PageParametersToPageableConverterPackageNameTests {

		@Test
		void getPageParametersToPageableConverterPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageParametersToPageableConverterPackageName(null, table));
		}

		@Test
		void getPageParametersToPageableConverterPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("persistence.converter", unitUnderTest.getPageParametersToPageableConverterPackageName(model, null));
		}

		@Test
		void getPageParametersToPageableConverterPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_CONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_CONVERTER_PACKAGE_NAME,
									"persistence.mapper"));
			// Run & Check
			assertEquals("persistence.mapper", unitUnderTest.getPageParametersToPageableConverterPackageName(model, table));
		}

	}

	@DisplayName("tests for to model converter interface names")
	@Nested
	class ToModelConverterInterfaceNameTests {

		@Test
		void getToModelConverterInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getToModelConverterInterfaceName(null));
		}

		@Test
		void getToModelConverterInterfaceName_passAValidTable_ReturnsACorrectInterfaceName() {
			// Prepare
			String expected = "ToModelConverter";
			// Run
			String returned = unitUnderTest.getToModelConverterInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for toDBO method name")
	@Nested
	class ToDBOMethodNameTests {

		@Test
		void getToDBOMethodName_PassANullValueAsTable_ReturnsADefaultName() {
			assertEquals("toDBO", unitUnderTest.getToDBOMethodName((DataModel) null));
		}

		@Test
		void getToDBOMethodName_PassAValidTable_ReturnsADefaultName() {
			assertEquals("toDBO", unitUnderTest.getToDBOMethodName(table));
		}

		@Test
		void getToDBOMethodName_PassAValidTable_ModelAsAlternateName_ReturnsTheAlternateName() {
			// Prepare
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_TO_DBO_METHOD_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_TO_DBO_METHOD_NAME,
									"toDbo"));
			// Run & Check
			assertEquals("toDbo", unitUnderTest.getToDBOMethodName(table));
		}

	}

	@DisplayName("tests for toModel method name")
	@Nested
	class ToModelMethodNameTests {

		@Test
		void getToModelMethodName_PassANullValueAsTable_ReturnsADefaultName() {
			assertEquals("toModel", unitUnderTest.getToModelMethodName((DataModel) null));
		}

		@Test
		void getToModelMethodName_PassAValidTable_ReturnsADefaultName() {
			assertEquals("toModel", unitUnderTest.getToModelMethodName(table));
		}

		@Test
		void getToModelMethodName_PassAValidTable_ModelAsAlternateName_ReturnsTheAlternateName() {
			// Prepare
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(PersistenceJPANameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME,
									"toSO"));
			// Run & Check
			assertEquals("toSO", unitUnderTest.getToModelMethodName(table));
		}

	}

}