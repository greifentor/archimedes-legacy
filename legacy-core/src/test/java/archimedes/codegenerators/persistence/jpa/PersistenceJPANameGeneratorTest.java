package archimedes.codegenerators.persistence.jpa;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

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
	class DBBConverterClassNameTests {

		@Test
		void getDBOConverterClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getDBOConverterClassName(null));
		}

		@Test
		void getDBOConverterClassName_passAValidTable_ReturnsACorrectDBOConverterClassName() {
			// Prepare
			String expected = "TableDBOConverter";
			when(table.getName()).thenReturn("Table");
			// Run
			String returned = unitUnderTest.getDBOConverterClassName(table);
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
			String returned = unitUnderTest.getDBOConverterClassName(table);
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

	@DisplayName("tests for toDBO method name")
	@Nested
	class ToDBOMethodNameTests {

		@Test
		void getToDBOMethodName_PassANullValueAsTable_ReturnsADefaultName() {
			assertEquals("toDBO", unitUnderTest.getToDBOMethodName(null));
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
			assertEquals("toModel", unitUnderTest.getToModelMethodName(null));
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