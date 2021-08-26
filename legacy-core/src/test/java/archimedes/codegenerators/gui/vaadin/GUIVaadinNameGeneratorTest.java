package archimedes.codegenerators.gui.vaadin;

import archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator;
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
public class GUIVaadinNameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private GUIVaadinNameGenerator unitUnderTest;

	@DisplayName("tests for GO class names")
	@Nested
	class GOClassNameTests {

		@Test
		void getGOClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getGOClassName(table);
			});
		}

		@Test
		void getGOClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getGOClassName(null));
		}

		@Test
		void getGOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TestTableGO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameGO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameGO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameGO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TGO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TGO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassDataModelWithALTERNATE_GO_CLASS_NAME_SUFFIXOption_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_GO_CLASS_NAME_SUFFIX, "GO"))
					.when(model)
					.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_GO_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for GO converter class names")
	@Nested
	class GOConverterClassNameTests {

		@Test
		void getGOConverterClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getGOConverterClassName(null));
		}

		@Test
		void getGOConverterClassName_passAValidTable_ReturnsACorrectGOConverterClassName() {
			// Prepare
			String expected = "TableGOConverter";
			when(table.getName()).thenReturn("Table");
			// Run
			String returned = unitUnderTest.getGOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Disabled("TODO OLI: This should also work as described by the test.")
		@Test
		void getGOConverterClassName_passAValidTableModelWithAlternateClassName_ReturnsACorrectGOConverterClassName() {
			// Prepare
			String expected = "TableGOMapper";
			when(table.getName()).thenReturn("Table");
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX,
									"GOMapper"));
			// Run
			String returned = unitUnderTest.getGOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for GO converter package names")
	@Nested
	class GOConverterPackageNameTests {

		@Test
		void getGOConverterPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getGOConverterPackageName(null, table));
		}

		@Test
		void getGOConverterPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("persistence.converter", unitUnderTest.getGOConverterPackageName(model, null));
		}

		@Test
		void getGOConverterPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_GO_CONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_GO_CONVERTER_PACKAGE_NAME,
									"persistence.mapper"));
			// Run & Check
			assertEquals("persistence.mapper", unitUnderTest.getGOConverterPackageName(model, table));
		}

	}

	@DisplayName("tests for GO package names")
	@Nested
	class GOPackageNameTests {

		@Test
		void getGOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getGOPackageName(null, table));
		}

		@Test
		void getGOPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("persistence.entity", unitUnderTest.getGOPackageName(model, null));
		}

		@Test
		void getGOPackageName_PassAValidTableModel_ReturnsACorrecGOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".persistence.entity";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getGOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecGOName() {
			// Prepare
			String expected = "persistence.entity";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getGOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecGOName() {
			// Prepare
			String expected = "persistence.entity";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getGOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOPackageName_PassAValidTableModelWithMODULEOption_ReturnsACorrecGOName() {
			// Prepare
			String prefix = "prefix";
			String expected = "prefix.persistence.entity";
			when(model.getBasePackageName()).thenReturn(null);
			when(table.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE))
					.thenReturn(new Option(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE, prefix));
			// Run
			String returned = unitUnderTest.getGOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_ENTITY_PACKAGE_NAME))
					.thenReturn(
							new Option(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_ENTITY_PACKAGE_NAME, "persistence.GOs"));
			// Run & Check
			assertEquals("persistence.GOs", unitUnderTest.getGOPackageName(model, table));
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
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_ADAPTER_CLASS_NAME_SUFFIX,
									"PersistenceAdapter"));
			// Run
			String returned = unitUnderTest.getJPAPersistenceAdapterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for GO package names")
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
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_ADAPTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_ADAPTER_PACKAGE_NAME,
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
			String expected = "NameGORepository";
			when(table.getName()).thenReturn("Name");
			// Run
			String returned = unitUnderTest.getJPARepositoryInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getJPARepositoryInterfaceName_PassDataModelWithALTERNATE_REPOSITORY_CLASS_NAME_SUFFIXOption_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableRepo";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX, "Repo"))
					.when(model)
					.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getJPARepositoryInterfaceName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for GO package names")
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
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_PACKAGE_NAME))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_REPOSITORY_PACKAGE_NAME,
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
		void getPageConverterClassName_passAValidTable_ReturnsACorrectGOConverterClassName() {
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
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_PAGE_CONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_PAGE_CONVERTER_PACKAGE_NAME,
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
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_PAGE_MODEL_PACKAGE_NAME))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_PAGE_MODEL_PACKAGE_NAME,
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
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_MODEL_PACKAGE_NAME))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_MODEL_PACKAGE_NAME,
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
		void getPageParametersToPageableConverterClassName_passAValidTable_ReturnsACorrectGOConverterClassName() {
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
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_CONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_PAGE_PARAMETERS_CONVERTER_PACKAGE_NAME,
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

	@DisplayName("tests for toGO method name")
	@Nested
	class ToGOMethodNameTests {

		@Test
		void getToGOMethodName_PassANullValueAsTable_ReturnsADefaultName() {
			assertEquals("toGO", unitUnderTest.getToGOMethodName(null));
		}

		@Test
		void getToGOMethodName_PassAValidTable_ReturnsADefaultName() {
			assertEquals("toGO", unitUnderTest.getToGOMethodName(table));
		}

		@Test
		void getToGOMethodName_PassAValidTable_ModelAsAlternateName_ReturnsTheAlternateName() {
			// Prepare
			when(table.getDataModel()).thenReturn(model);
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_TO_GO_METHOD_NAME))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_TO_GO_METHOD_NAME,
									"toGO"));
			// Run & Check
			assertEquals("toGO", unitUnderTest.getToGOMethodName(table));
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
			when(model.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME))
					.thenReturn(
							new Option(
									PersistenceJPANameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME,
									"toSO"));
			// Run & Check
			assertEquals("toSO", unitUnderTest.getToModelMethodName(table));
		}

	}

}