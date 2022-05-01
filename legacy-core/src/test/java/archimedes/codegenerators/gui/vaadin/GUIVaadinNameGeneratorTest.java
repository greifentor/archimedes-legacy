package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.NameGenerator;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;

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

	@Nested
	class AbstractMasterDataDetailLayoutClassNameTests {

		@Test
		void getAbstractMasterDataDetailLayoutClassName_returnsTheCorrectClassName() {
			assertEquals("AbstractMasterDataDetailLayout", unitUnderTest.getAbstractMasterDataDetailLayoutClassName());
		}

	}

	@Nested
	class VaadinComponentPackageNameTests {

		@Test
		void getVaadinComponentPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getVaadinComponentPackageName(null));
		}

		@Test
		void getVaadinComponentPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_VAADIN_COMPONENT_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_VAADIN_COMPONENT_PACKAGE_NAME,
									"web.components"));
			// Run & Check
			assertEquals("web.components", unitUnderTest.getVaadinComponentPackageName(model));
		}

	}

	@Nested
	class ButtonClassNameTests {

		@Test
		void getButtonClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getButtonClassName(null));
		}

		@Test
		void getButtonClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "Button";
			// Run
			String returned = unitUnderTest.getButtonClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getButtonClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherButton";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_BUTTON_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_BUTTON_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getButtonClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class ButtonFactoryClassNameTests {

		@Test
		void getButtonFactoryClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getButtonFactoryClassName(null));
		}

		@Test
		void getButtonFactoryClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "ButtonFactory";
			// Run
			String returned = unitUnderTest.getButtonFactoryClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getButtonFactoryClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherButtonFactory";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_BUTTON_FACTORY_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(GUIVaadinNameGenerator.ALTERNATE_BUTTON_FACTORY_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getButtonFactoryClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class DetailsLayoutClassNameTests {

		@Test
		void getDetailsLayoutClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getDetailsLayoutClassName(model, table);
			});
		}

		@Test
		void getDetailsLayoutClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getDetailsLayoutClassName(model, null));
		}

		@Test
		void getDetailsLayoutClassName_PassTableModelWithNameCamelCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TestTableDetailsLayout";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDetailsLayoutClassName_PassTableModelWithNameUpperCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableDetailsLayout";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDetailsLayoutClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameDetailsLayout";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDetailsLayoutClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameDetailsLayout";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDetailsLayoutClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameDetailsLayout";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDetailsLayoutClassName_PassTableModelWithNameLowerCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableDetailsLayout";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDetailsLayoutClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TDetailsLayout";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDetailsLayoutClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TDetailsLayout";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getDetailsLayoutClassName_PassDataModelWithALTERNATE_DETAILS_LAYOUT_CLASS_NAME_SUFFIXOption_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_DETAILS_LAYOUT_CLASS_NAME_SUFFIX, "GO"))
					.when(model)
					.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_DETAILS_LAYOUT_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

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
							new Option(GUIVaadinNameGenerator.ALTERNATE_GO_CONVERTER_CLASS_NAME_SUFFIX, "GOMapper"));
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
					.thenReturn(
							new Option(
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
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_GO_PACKAGE_NAME, "vaadin.gos"));
			// Run & Check
			assertEquals("vaadin.gos", unitUnderTest.getGOPackageName(model, table));
		}

	}

	@Nested
	class HeaderLayoutClassNameTests {

		@Test
		void getHeaderLayoutClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getHeaderLayoutClassName(null));
		}

		@Test
		void getHeaderLayoutClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "HeaderLayout";
			// Run
			String returned = unitUnderTest.getHeaderLayoutClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getHeaderLayoutClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherHeaderLayout";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_HEADER_LAYOUT_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_HEADER_LAYOUT_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getHeaderLayoutClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class HeaderLayoutPackageNameTests {

		@Test
		void getHeaderLayoutPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getHeaderLayoutPackageName(null));
		}

		@Test
		void getHeaderLayoutPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.component", unitUnderTest.getHeaderLayoutPackageName(model));
		}

		@Test
		void getHeaderLayoutPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_HEADER_LAYOUT_PACKAGE_NAME))
					.thenReturn(
							new Option(GUIVaadinNameGenerator.ALTERNATE_HEADER_LAYOUT_PACKAGE_NAME, "vaadin.mapper"));
			// Run & Check
			assertEquals("vaadin.mapper", unitUnderTest.getHeaderLayoutPackageName(model));
		}

	}

	@Nested
	class ImageClassNameTests {

		@Test
		void getImageClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getImageClassName(null));
		}

		@Test
		void getImageClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "Image";
			// Run
			String returned = unitUnderTest.getImageClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getImageClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherImage";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_IMAGE_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_IMAGE_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getImageClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class MaintenanceViewClassNameTests {

		@Test
		void getMaintenanceViewClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getMaintenanceViewClassName(model, table);
			});
		}

		@Test
		void getMaintenanceViewClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getMaintenanceViewClassName(model, null));
		}

		@Test
		void getMaintenanceViewClassName_PassTableModelWithNameCamelCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TestTableMaintenanceView";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewClassName_PassTableModelWithNameUpperCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableMaintenanceView";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameMaintenanceView";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameMaintenanceView";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNameMaintenanceView";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewClassName_PassTableModelWithNameLowerCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableMaintenanceView";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TMaintenanceView";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TMaintenanceView";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewClassName_PassDataModelWithALTERNATE_MAINTENANCE_LAYOUT_CLASS_NAME_SUFFIXOption_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_MAINTENANCE_VIEW_CLASS_NAME_SUFFIX, "GO"))
					.when(model)
					.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MAINTENANCE_VIEW_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getMaintenanceViewClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class MaintenanceViewPackageNameTests {

		@Test
		void getMaintenanceViewPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getMaintenanceViewPackageName(null, table));
		}

		@Test
		void getMaintenanceViewPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.masterdata", unitUnderTest.getMaintenanceViewPackageName(model, null));
		}

		@Test
		void getMaintenanceViewPackageName_PassAValidTableModel_ReturnsACorrecGOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".gui.vaadin.masterdata";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getMaintenanceViewPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecGOName() {
			// Prepare
			String expected = "gui.vaadin.masterdata";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getMaintenanceViewPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecGOName() {
			// Prepare
			String expected = "gui.vaadin.masterdata";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getMaintenanceViewPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewPackageName_PassAValidTableModelWithMODULEOption_ReturnsACorrecGOName() {
			// Prepare
			String prefix = "prefix";
			String expected = "prefix.gui.vaadin.masterdata";
			when(model.getBasePackageName()).thenReturn(null);
			when(table.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE,
									prefix));
			// Run
			String returned = unitUnderTest.getMaintenanceViewPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMaintenanceViewPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MAINTENANCE_VIEW_PACKAGE_NAME))
					.thenReturn(
							new Option(GUIVaadinNameGenerator.ALTERNATE_MAINTENANCE_VIEW_PACKAGE_NAME, "vaadin.gos"));
			// Run & Check
			assertEquals("vaadin.gos", unitUnderTest.getMaintenanceViewPackageName(model, table));
		}

	}

	@Nested
	class MasterDataButtonLayoutClassNameTests {

		@Test
		void getMasterDataButtonLayoutClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getMasterDataButtonLayoutClassName(null));
		}

		@Test
		void getMasterDataButtonLayoutClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "MasterDataButtonLayout";
			// Run
			String returned = unitUnderTest.getMasterDataButtonLayoutClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMasterDataButtonLayoutClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherMasterDataButtonLayout";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_CLASS_NAME_SUFFIX,
									expected));
			// Run
			String returned = unitUnderTest.getMasterDataButtonLayoutClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class MasterDataButtonLayoutPackageNameTests {

		@Test
		void getMasterDataButtonLayoutPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getMasterDataButtonLayoutPackageName(null));
		}

		@Test
		void getMasterDataButtonLayoutPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.component", unitUnderTest.getMasterDataButtonLayoutPackageName(model));
		}

		@Test
		void getMasterDataButtonLayoutPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_BUTTON_LAYOUT_PACKAGE_NAME,
									"vaadin.mapper"));
			// Run & Check
			assertEquals("vaadin.mapper", unitUnderTest.getMasterDataButtonLayoutPackageName(model));
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

	@Nested
	class MasterDataGUIConfigurationClassNameTests {

		@Test
		void getMasterDataGUIConfigurationClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getMasterDataGUIConfigurationClassName(null));
		}

		@Test
		void getMasterDataGUIConfigurationClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "MasterDataGUIConfiguration";
			// Run
			String returned = unitUnderTest.getMasterDataGUIConfigurationClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMasterDataGUIConfigurationClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherMasterDataGUIConfiguration";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_CLASS_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_CLASS_NAME,
									expected));
			// Run
			String returned = unitUnderTest.getMasterDataGUIConfigurationClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class MasterDataGUIConfigurationPackageNameTests {

		@Test
		void getMasterDataGUIConfigurationPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getMasterDataGUIConfigurationPackageName(null));
		}

		@Test
		void getMasterDataGUIConfigurationPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.masterdata", unitUnderTest.getMasterDataGUIConfigurationPackageName(model));
		}

		@Test
		void getMasterDataGUIConfigurationPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_PACKAGE_NAME,
									"vaadin.mapper"));
			// Run & Check
			assertEquals("vaadin.mapper", unitUnderTest.getMasterDataGUIConfigurationPackageName(model));
		}

	}

	@Nested
	class MasterDataViewClassNameTests {

		@Test
		void getMasterDataViewClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getMasterDataViewClassName(null));
		}

		@Test
		void getMasterDataViewClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "MasterDataView";
			// Run
			String returned = unitUnderTest.getMasterDataViewClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMasterDataViewClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherMasterDataView";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_VIEW_CLASS_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_VIEW_CLASS_NAME,
									expected));
			// Run
			String returned = unitUnderTest.getMasterDataViewClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class MasterDataViewPackageNameTests {

		@Test
		void getMasterDataViewPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getMasterDataViewPackageName(null));
		}

		@Test
		void getMasterDataViewPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.masterdata", unitUnderTest.getMasterDataViewPackageName(model));
		}

		@Test
		void getMasterDataViewPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_VIEW_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_VIEW_PACKAGE_NAME,
									"vaadin.mapper"));
			// Run & Check
			assertEquals("vaadin.mapper", unitUnderTest.getMasterDataViewPackageName(model));
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
			assertEquals("gui.vaadin.go.converter", unitUnderTest.getPageGOPackageName(model, null));
		}

		@Test
		void getPageGOPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_GO_PACKAGE_NAME))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_PAGE_GO_PACKAGE_NAME, "vaadin.go"));
			// Run & Check
			assertEquals("vaadin.go", unitUnderTest.getPageGOPackageName(model, table));
		}

	}

	@Nested
	class PageViewClassNameTests {

		@Test
		void getPageViewClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getPageViewClassName(table);
			});
		}

		@Test
		void getPageViewClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getPageViewClassName(null));
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameCamelCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TestTablePageView";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameUpperCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TablePageView";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNamePageView";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNamePageView";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableNamePageView";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameLowerCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TablePageView";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TPageView";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TPageView";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassDataModelWithALTERNATE_PAGE_LAYOUT_CLASS_NAME_SUFFIXOption_ReturnsACorrectGOName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("Table");
			when(table.getDataModel()).thenReturn(model);
			doReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_PAGE_VIEW_CLASS_NAME_SUFFIX, "GO"))
					.when(model)
					.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_VIEW_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class PageViewPackageNameTests {

		@Test
		void getPageViewPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageViewPackageName(null, table));
		}

		@Test
		void getPageViewPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.masterdata", unitUnderTest.getPageViewPackageName(model, null));
		}

		@Test
		void getPageViewPackageName_PassAValidTableModel_ReturnsACorrecGOName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".gui.vaadin.masterdata";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getPageViewPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewPackageName_PassAValidTableModelWithEmptyBasePackageName_ReturnsACorrecGOName() {
			// Prepare
			String expected = "gui.vaadin.masterdata";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getPageViewPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewPackageName_PassAValidTableModelWithNullBasePackageName_ReturnsACorrecGOName() {
			// Prepare
			String expected = "gui.vaadin.masterdata";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getPageViewPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewPackageName_PassAValidTableModelWithMODULEOption_ReturnsACorrecGOName() {
			// Prepare
			String prefix = "prefix";
			String expected = "prefix.gui.vaadin.masterdata";
			when(model.getBasePackageName()).thenReturn(null);
			when(table.getOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE))
					.thenReturn(
							new Option(
									archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE,
									prefix));
			// Run
			String returned = unitUnderTest.getPageViewPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewPackageName_PassAValidTableButModelAsAlternateRepositoryNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_VIEW_PACKAGE_NAME))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_PAGE_VIEW_PACKAGE_NAME, "vaadin.gos"));
			// Run & Check
			assertEquals("vaadin.gos", unitUnderTest.getPageViewPackageName(model, table));
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
			assertEquals("gui.vaadin.go.converter", unitUnderTest.getPageParametersGOPackageName(model, null));
		}

		@Test
		void getPageParametersGOPackageName_PassAValidTableButModelAsAlternateGONameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME))
					.thenReturn(
							new Option(GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_PACKAGE_NAME, "vaadin.go"));
			// Run & Check
			assertEquals("vaadin.go", unitUnderTest.getPageParametersGOPackageName(model, table));
		}

	}

	@DisplayName("tests for page parameters GO to page parameters converter class names")
	@Nested
	class PageParametersGOConverterClassNameTests {

		@Test
		void getPageParametersGOConverterClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getPageParametersGOConverterClassName(null));
		}

		@Test
		void getPageParametersGOConverterClassName_passAValidTable_ReturnsACorrectGOConverterClassName() {
			// Prepare
			String expected = "PageParametersGOConverter";
			// Run
			String returned = unitUnderTest.getPageParametersGOConverterClassName(table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@DisplayName("tests for page parameters GO to page parameters converter package names")
	@Nested
	class PageParametersGOConverterPackageNameTests {

		@Test
		void getPageParametersGOConverterPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getPageParametersGOConverterPackageName(null, table));
		}

		@Test
		void getPageParametersGOConverterPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.converter", unitUnderTest.getPageParametersGOConverterPackageName(model, null));
		}

		@Test
		void getPageParametersGOConverterPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_PAGE_PARAMETERS_GO_CONVERTER_PACKAGE_NAME,
									"vaadin.mapper"));
			// Run & Check
			assertEquals("vaadin.mapper", unitUnderTest.getPageParametersGOConverterPackageName(model, table));
		}

	}

	@Nested
	class PluralNameClassNameTests {

		@Test
		void passANullValue_returnsANullValue() {
			assertNull(unitUnderTest.getPluralName(null));
		}

		@Test
		void passATableWithNoConfiguration_returnsAnEnglishDefault() {
			// Prepare
			String name = "name";
			when(table.getName()).thenReturn(name);
			// Run & Check
			assertEquals(name + "s", unitUnderTest.getPluralName(table));
		}

		@Test
		void passATableWithNoConfiguration_nameEndsOnY_returnsACorrectEnglishPlural() {
			// Prepare
			String name = "bunny";
			when(table.getName()).thenReturn(name);
			// Run & Check
			assertEquals(name.replace("y", "ies"), unitUnderTest.getPluralName(table));
		}

		@Test
		void passATableWithConfiguration_returnsTheConfiguredValue() {
			// Prepare
			String name = "configuredValue";
			when(table.isOptionSet(NameGenerator.PLURAL_NAME)).thenReturn(true);
			when(table.getOptionByName(NameGenerator.PLURAL_NAME))
					.thenReturn(new Option(NameGenerator.PLURAL_NAME, name));
			// Run & Check
			assertEquals(name, unitUnderTest.getPluralName(table));
		}

	}

	@Nested
	class SelectionDialogClassNameTests {

		@Test
		void getSelectionDialogClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getSelectionDialogClassName(null));
		}

		@Test
		void getSelectionDialogClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "SelectionDialog";
			// Run
			String returned = unitUnderTest.getSelectionDialogClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSelectionDialogClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherSelectionDialog";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_SELECTION_DIALOG_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(GUIVaadinNameGenerator.ALTERNATE_SELECTION_DIALOG_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getSelectionDialogClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class SelectionDialogPackageNameTests {

		@Test
		void getSelectionDialogPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getSelectionDialogPackageName(null));
		}

		@Test
		void getSelectionDialogPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.component", unitUnderTest.getSelectionDialogPackageName(model));
		}

		@Test
		void getSelectionDialogPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_SELECTION_DIALOG_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_SELECTION_DIALOG_PACKAGE_NAME,
									"vaadin.mapper"));
			// Run & Check
			assertEquals("vaadin.mapper", unitUnderTest.getSelectionDialogPackageName(model));
		}

	}

	@Nested
	class SessionDataClassNameTests {

		@Test
		void getSessionDataClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getSessionDataClassName(null));
		}

		@Test
		void getSessionDataClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "SessionData";
			// Run
			String returned = unitUnderTest.getSessionDataClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSessionDataClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherSessionData";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_SESSION_DATA_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_SESSION_DATA_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getSessionDataClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class SessionDataPackageNameTests {

		@Test
		void getSessionDataPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getSessionDataPackageName(null));
		}

		@Test
		void getSessionDataPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui", unitUnderTest.getSessionDataPackageName(model));
		}

		@Test
		void getSessionDataPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_SESSION_DATA_PACKAGE_NAME))
					.thenReturn(
							new Option(GUIVaadinNameGenerator.ALTERNATE_SESSION_DATA_PACKAGE_NAME, "vaadin.mapper"));
			// Run & Check
			assertEquals("vaadin.mapper", unitUnderTest.getSessionDataPackageName(model));
		}

	}

	@Nested
	class TextFieldClassNameTests {

		@Test
		void getTextFieldClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getTextFieldClassName(null));
		}

		@Test
		void getTextFieldClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TextField";
			// Run
			String returned = unitUnderTest.getTextFieldClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getTextFieldClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherTextField";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getTextFieldClassName(model);
			// Check
			assertEquals(expected, returned);
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

	@Nested
	class UserAuthorizationCheckerClassNameTests {

		@Test
		void getUserAuthorizationCheckerClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getUserAuthorizationCheckerClassName(null));
		}

		@Test
		void getUserAuthorizationCheckerClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "UserAuthorizationChecker";
			// Run
			String returned = unitUnderTest.getUserAuthorizationCheckerClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getUserAuthorizationCheckerClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherUserAuthorizationChecker";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_USER_AUTHORIZATION_CHECKER_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_USER_AUTHORIZATION_CHECKER_CLASS_NAME_SUFFIX,
									expected));
			// Run
			String returned = unitUnderTest.getUserAuthorizationCheckerClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class UserAuthorizationCheckerPackageNameTests {

		@Test
		void getUserAuthorizationCheckerPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getUserAuthorizationCheckerPackageName(null));
		}

		@Test
		void getUserAuthorizationCheckerPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin", unitUnderTest.getUserAuthorizationCheckerPackageName(model));
		}

		@Test
		void getUserAuthorizationCheckerPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_USER_AUTHORIZATION_CHECKER_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_USER_AUTHORIZATION_CHECKER_PACKAGE_NAME,
									"vaadin.mapper"));
			// Run & Check
			assertEquals("vaadin.mapper", unitUnderTest.getUserAuthorizationCheckerPackageName(model));
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
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_TO_GO_METHOD_NAME, "toGO"));
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
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_TO_MODEL_METHOD_NAME, "toSO"));
			// Run & Check
			assertEquals("toSO", unitUnderTest.getToModelMethodName(table));
		}

	}

}