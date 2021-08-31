package archimedes.codegenerators.gui.vaadin;

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
			assertEquals("gui.vaadin.converter", unitUnderTest.getGOConverterPackageName(model, null));
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
			assertEquals("gui.vaadin.go", unitUnderTest.getGOPackageName(model, null));
		}

		@Test
		void getGOPackageName_PassAValidTableModel_ReturnsACorrecGOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".gui.vaadin.go";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getGOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecGOName() {
			// Prepare
			String expected = "gui.vaadin.go";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getGOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecGOName() {
			// Prepare
			String expected = "gui.vaadin.go";
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
			String expected = "prefix.gui.vaadin.go";
			when(model.getBasePackageName()).thenReturn(null);
			when(table.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE))
					.thenReturn(new Option(
							archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE,
							prefix));
			// Run
			String returned = unitUnderTest.getGOPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_GO_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_GO_PACKAGE_NAME,
									"vaadin.gos"));
			// Run & Check
			assertEquals("vaadin.gos", unitUnderTest.getGOPackageName(model, table));
		}

	}

	@DisplayName("tests for page GO converter class names")
	@Nested
	class PageGOConverterClassNameTests {

		@Test
		void getPageGOConverterClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageGOConverterClassName(null));
		}

		@Test
		void getPageGOConverterClassName_passAValidTable_ReturnsACorrectGOConverterClassName() {
			// Prepare
			String expected = "PageGOConverter";
			// Run
			String returned = unitUnderTest.getPageGOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page GO converter package names")
	@Nested
	class PageGOConverterPackageNameTests {

		@Test
		void getPageGOConverterPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageGOConverterPackageName(null, table));
		}

		@Test
		void getPageGOConverterPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.converter", unitUnderTest.getPageGOConverterPackageName(model, null));
		}

		@Test
		void getPageGOConverterPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_GO_CONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_PAGE_GO_CONVERTER_PACKAGE_NAME,
									"gui.vaadin.mapper"));
			// Run & Check
			assertEquals("gui.vaadin.mapper", unitUnderTest.getPageGOConverterPackageName(model, table));
		}

	}

	@DisplayName("tests for page GO class names")
	@Nested
	class PageGOClassNameTests {

		@Test
		void getPageGOClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageGOClassName(null));
		}

		@Test
		void getPageGOClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "PageGO";
			// Run
			String returned = unitUnderTest.getPageGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page GO package names")
	@Nested
	class PageGOPackageNameTests {

		@Test
		void getPageGOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageGOPackageName(null, table));
		}

		@Test
		void getPageGOPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.go", unitUnderTest.getPageGOPackageName(model, null));
		}

		@Test
		void getPageGOPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_GO_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_PAGE_GO_PACKAGE_NAME,
									"vaadin.go"));
			// Run & Check
			assertEquals("vaadin.go", unitUnderTest.getPageGOPackageName(model, table));
		}

	}

	@DisplayName("tests for page parameters GO class names")
	@Nested
	class PageParametersGOClassNameTests {

		@Test
		void getPageParametersGOClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageParametersGOClassName(null));
		}

		@Test
		void getPageParametersGOClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "PageParametersGO";
			// Run
			String returned = unitUnderTest.getPageParametersGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page parameters GO package names")
	@Nested
	class PageParametersGOPackageNameTests {

		@Test
		void getPageParametersGOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageParametersGOPackageName(null, table));
		}

		@Test
		void getPageParametersGOPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.go", unitUnderTest.getPageParametersGOPackageName(model, null));
		}

		@Test
		void getPageParametersGOPackageName_PassAValidTableButModelAsAlternateGONameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME,
									"vaadin.go"));
			// Run & Check
			assertEquals("vaadin.go", unitUnderTest.getPageParametersGOPackageName(model, table));
		}

	}

	@DisplayName("tests for page parameters GO to page parameters converter class names")
	@Nested
	class PageParametersGOToPageParametersConverterClassNameTests {

		@Test
		void getPageParametersGOToPageParametersConverterClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageParametersGOToPageParametersConverterClassName(null));
		}

		@Test
		void getPageParametersGOToPageParametersConverterClassName_passAValidTable_ReturnsACorrectGOConverterClassName() {
			// Prepare
			String expected = "PageParametersGOToPageParametersConverter";
			// Run
			String returned = unitUnderTest.getPageParametersGOToPageParametersConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page parameters GO to page parameters converter package names")
	@Nested
	class PageParametersGOToPageParametersConverterPackageNameTests {

		@Test
		void getPageParametersGOToPageParametersConverterPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageParametersGOToPageParametersConverterPackageName(null, table));
		}

		@Test
		void getPageParametersGOToPageParametersConverterPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals(
					"gui.vaadin.converter",
					unitUnderTest.getPageParametersGOToPageParametersConverterPackageName(model, null));
		}

		@Test
		void getPageParametersGOToPageParametersConverterPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME,
									"vaadin.mapper"));
			// Run & Check
			assertEquals(
					"vaadin.mapper",
					unitUnderTest.getPageParametersGOToPageParametersConverterPackageName(model, table));
		}

	}

	@DisplayName("tests for to GO converter interface names")
	@Nested
	class ToGOConverterInterfaceNameTests {

		@Test
		void getToGOConverterInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getToGOConverterInterfaceName(null));
		}

		@Test
		void getToGOConverterInterfaceName_passAValidTable_ReturnsACorrectInterfaceName() {
			// Prepare
			String expected = "ToGOConverter";
			// Run
			String returned = unitUnderTest.getToGOConverterInterfaceName(table);
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
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_TO_GO_METHOD_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_TO_GO_METHOD_NAME,
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
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME,
									"toSO"));
			// Run & Check
			assertEquals("toSO", unitUnderTest.getToModelMethodName(table));
		}

	}

}