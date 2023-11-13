package archimedes.codegenerators.gui.vaadin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

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

	static void assertCorrectClassName(String expected, Supplier<String> check) {
		assertEquals(expected, check.get());
	}

	static void assertCorrectAlternativeClassName(String expected, String alternativIdentfier, DataModel model,
			Function<DataModel, String> check) {
		when(model.getOptionByName(alternativIdentfier)).thenReturn(new Option(alternativIdentfier, expected));
		assertEquals(expected, check.apply(model));
	}

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
	class ComponentFactoryClassNameTests {

		@Test
		void getComponentFactoryClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getComponentFactoryClassName(null));
		}

		@Test
		void getComponentFactoryClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "ComponentFactory";
			// Run
			String returned = unitUnderTest.getComponentFactoryClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getComponentFactoryClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherComponentFactory";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_COMPONENT_FACTORY_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(GUIVaadinNameGenerator.ALTERNATE_COMPONENT_FACTORY_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getComponentFactoryClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class ButtonFactoryConfigurationClassNameTests {

		@Test
		void getButtonFactoryConfigurationClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getButtonFactoryConfigurationClassName(null));
		}

		@Test
		void getButtonFactoryConfigurationClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "ButtonFactoryConfiguration";
			// Run
			String returned = unitUnderTest.getButtonFactoryConfigurationClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getButtonFactoryConfigurationClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherButtonFactoryConfiguration";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_BUTTON_FACTORY_CONFIGURATION_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_BUTTON_FACTORY_CONFIGURATION_CLASS_NAME_SUFFIX,
									expected));
			// Run
			String returned = unitUnderTest.getButtonFactoryConfigurationClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class ButtonGridClassNameTests {

		@Test
		void getButtonGridClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getButtonGridClassName(null));
		}

		@Test
		void getButtonGridClassName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "ButtonGrid";
			// Run
			String returned = unitUnderTest.getButtonGridClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getButtonGridClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherButtonGrid";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_BUTTON_GRID_CLASS_NAME_SUFFIX))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_BUTTON_GRID_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getButtonGridClassName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class DetailsLayoutClassNameTests extends AbstractClassNameTest {

		@Override
		protected BiFunction<DataModel, TableModel, String> calledMethod() {
			return (model, table) -> unitUnderTest.getDetailsLayoutClassName(model, table);
		}

		@Override
		protected String getAlternativeIdentifier() {
			return GUIVaadinNameGenerator.ALTERNATE_DETAILS_LAYOUT_CLASS_NAME_SUFFIX;
		}

		@Override
		protected String getExpectedClassNameSuffix() {
			return "DetailsLayout";
		}

		@Override
		protected DataModel getModel() {
			return model;
		}

		@Override
		protected TableModel getTable() {
			return table;
		}

	}

	@Nested
	class DetailsLayoutComboBoxItemLabelGeneratorInterfaceNameTests {

		@Test
		void getDetailsLayoutComboBoxItemLabelGeneratorInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getDetailsLayoutComboBoxItemLabelGeneratorInterfaceName(null));
		}

		@Test
		void getDetailsLayoutComboBoxItemLabelGeneratorInterfaceName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName(
					"DetailsLayoutComboBoxItemLabelGenerator",
					() -> unitUnderTest.getDetailsLayoutComboBoxItemLabelGeneratorInterfaceName(model));
		}

		@Test
		void getDetailsLayoutComboBoxItemLabelGeneratorInterfaceName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherDetailsLayoutComboBoxItemLabelGenerator",
					GUIVaadinNameGenerator.ALTERNATE_DETAILS_LAYOUT_COMBO_BOX_ITEM_LABEL_GENERATOR_INTERFACE_NAME,
					model,
					m -> unitUnderTest.getDetailsLayoutComboBoxItemLabelGeneratorInterfaceName(m));
		}

	}

	@Nested
	class DetailsDialogClassNameTests extends AbstractClassNameTest {

		@Override
		protected BiFunction<DataModel, TableModel, String> calledMethod() {
			return (model, table) -> unitUnderTest.getDetailsDialogClassName(model, table);
		}

		@Override
		protected String getAlternativeIdentifier() {
			return GUIVaadinNameGenerator.ALTERNATE_DETAILS_DIALOG_CLASS_NAME_SUFFIX;
		}

		@Override
		protected String getExpectedClassNameSuffix() {
			return "DetailsDialog";
		}

		@Override
		protected DataModel getModel() {
			return model;
		}

		@Override
		protected TableModel getTable() {
			return table;
		}

	}

	@Nested
	class DetailsDialogPackageNameTests extends AbstractPackageNameTest {

		@Override
		protected Function<DataModel, String> calledMethod() {
			return model -> unitUnderTest.getDetailsDialogPackageName(model);
		}

		@Override
		protected String getAlternativeIdentifier() {
			return GUIVaadinNameGenerator.ALTERNATE_DETAILS_DIALOG_PACKAGE_NAME;
		}

		@Override
		protected String getExpectedPackageName() {
			return "gui.vaadin.masterdata.dialog";
		}

		@Override
		protected DataModel getModel() {
			return model;
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
		void getGOClassName_PassTableModelWithNameCamelCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TestTableGO";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameUpperCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameGO";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameGO";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameGO";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelWithNameLowerCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TGO";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TGO";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getGOClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getGOClassName_PassDataModelWithALTERNATE_GO_CLASS_NAME_SUFFIXOption_ReturnsACorrectClassName() {
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
			when(table.findOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE))
					.thenReturn(
							Optional
									.of(
											new Option(
													archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE,
													prefix)));
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
	class GUIConfigurationClassNameTests {

		@Test
		void getGUIConfigurationClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getGUIConfigurationClassName(null));
		}

		@Test
		void getGUIConfigurationClassName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("GUIConfiguration", () -> unitUnderTest.getGUIConfigurationClassName(model));
		}

		@Test
		void getGUIConfigurationClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherGUIConfiguration",
					GUIVaadinNameGenerator.ALTERNATE_GUI_CONFIGURATION_CLASS_NAME,
					model,
					m -> unitUnderTest.getGUIConfigurationClassName(m));
		}

	}

	@Nested
	class GUIConfigurationPackageNameTests {

		@Test
		void getGUIConfigurationPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getGUIConfigurationPackageName(null));
		}

		@Test
		void getGUIConfigurationPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin", unitUnderTest.getGUIConfigurationPackageName(model));
		}

		@Test
		void getGUIConfigurationPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_GUI_CONFIGURATION_PACKAGE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_GUI_CONFIGURATION_PACKAGE_NAME,
									"vaadin.blubs"));
			// Run & Check
			assertEquals("vaadin.blubs", unitUnderTest.getGUIConfigurationPackageName(model));
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
	class ItemLabelGeneratorCollectionClassNameClassNameTests {

		@Test
		void getItemLabelGeneratorCollectionClassNameClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			});
		}

		@Test
		void getItemLabelGeneratorCollectionClassNameClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getItemLabelGeneratorCollectionClassName(model, null));
		}

		@Test
		void getItemLabelGeneratorCollectionClassNameClassName_PassTableModelWithNameCamelCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TestTableItemLabelGeneratorCollection";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getItemLabelGeneratorCollectionClassName_PassTableModelWithNameUpperCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableItemLabelGeneratorCollection";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getItemLabelGeneratorCollectionClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameItemLabelGeneratorCollection";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getItemLabelGeneratorCollectionClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameItemLabelGeneratorCollection";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getItemLabelGeneratorCollectionClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameItemLabelGeneratorCollection";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getItemLabelGeneratorCollectionClassName_PassTableModelWithNameLowerCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableItemLabelGeneratorCollection";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getItemLabelGeneratorCollectionClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TItemLabelGeneratorCollection";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getItemLabelGeneratorCollectionClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TItemLabelGeneratorCollection";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getItemLabelGeneratorCollectionClassName_PassDataModelWithALTERNATE_MAINTENANCE_LAYOUT_CLASS_NAME_SUFFIXOption_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("Table");
			doReturn(
					new Option(
							GUIVaadinNameGenerator.ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_CLASS_NAME_SUFFIX,
							"GO"))
									.when(model)
									.getOptionByName(
											GUIVaadinNameGenerator.ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getItemLabelGeneratorCollectionClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class ItemLabelGeneratorCollectionPackageNameTests extends AbstractPackageNameTest {

		@Override
		protected Function<DataModel, String> calledMethod() {
			return model -> unitUnderTest.getItemLabelGeneratorCollectionPackageName(model);
		}

		@Override
		protected String getAlternativeIdentifier() {
			return GUIVaadinNameGenerator.ALTERNATE_ITEM_LABEL_GENERATOR_COLLECTION_PACKAGE_NAME;
		}

		@Override
		protected String getExpectedPackageName() {
			return "gui.vaadin.masterdata.renderer";
		}

		@Override
		protected DataModel getModel() {
			return model;
		}

	}

	@Nested
	class ListDetailsLayoutClassNameClassNameTests {

		@Test
		void getListDetailsLayoutClassNameClassName_PassTableModelWithEmptyName_ThrowsException() {
			// Prepare
			when(table.getName()).thenReturn("");
			// Run
			assertThrows(IllegalArgumentException.class, () -> {
				unitUnderTest.getListDetailsLayoutClassName(model, table);
			});
		}

		@Test
		void getListDetailsLayoutClassNameClassName_PassNullValue_ReturnsNullValue() {
			assertNull(unitUnderTest.getListDetailsLayoutClassName(model, null));
		}

		@Test
		void getListDetailsLayoutClassNameClassName_PassTableModelWithNameCamelCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TestTableListDetailsLayout";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDetailsLayoutClassName_PassTableModelWithNameUpperCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableListDetailsLayout";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDetailsLayoutClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameListDetailsLayout";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDetailsLayoutClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameListDetailsLayout";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDetailsLayoutClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNameListDetailsLayout";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDetailsLayoutClassName_PassTableModelWithNameLowerCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableListDetailsLayout";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDetailsLayoutClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TListDetailsLayout";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDetailsLayoutClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TListDetailsLayout";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getListDetailsLayoutClassName_PassDataModelWithALTERNATE_LIST_DETAILS_LAYOUT_CLASS_NAME_SUFFIXOption_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableGO";
			when(table.getName()).thenReturn("Table");
			doReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_LIST_DETAILS_LAYOUT_CLASS_NAME_SUFFIX, "GO"))
					.when(model)
					.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_LIST_DETAILS_LAYOUT_CLASS_NAME_SUFFIX);
			// Run
			String returned = unitUnderTest.getListDetailsLayoutClassName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class ListDetailsLayoutPackageNameTests extends AbstractPackageNameTest {

		@Override
		protected Function<DataModel, String> calledMethod() {
			return model -> unitUnderTest.getListDetailsLayoutPackageName(model);
		}

		@Override
		protected String getAlternativeIdentifier() {
			return GUIVaadinNameGenerator.ALTERNATE_LIST_DETAILS_LAYOUT_PACKAGE_NAME;
		}

		@Override
		protected String getExpectedPackageName() {
			return "gui.vaadin.masterdata.layout.list";
		}

		@Override
		protected DataModel getModel() {
			return model;
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
			when(table.findOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE))
					.thenReturn(
							Optional
									.of(
											new Option(
													archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE,
													prefix)));
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
	class MaintenanceViewRendererInterfaceNameTests {

		@Test
		void getMaintenanceViewRendererInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getMaintenanceViewRendererInterfaceName(null));
		}

		@Test
		void getMaintenanceViewRendererInterfaceName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName(
					"MaintenanceViewRenderer",
					() -> unitUnderTest.getMaintenanceViewRendererInterfaceName(model));
		}

		@Test
		void getMaintenanceViewRendererInterfaceName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherMaintenanceViewRenderer",
					GUIVaadinNameGenerator.ALTERNATE_MAINTENANCE_VIEW_RENDERER_INTERFACE_NAME,
					model,
					m -> unitUnderTest.getMaintenanceViewRendererInterfaceName(m));
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
	class MasterDataGridFieldRendererInterfaceNameTests {

		@Test
		void getMasterDataGridFieldRendererInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getMasterDataGridFieldRendererInterfaceName(null));
		}

		@Test
		void getMasterDataGridFieldRendererInterfaceName_passAValidTable_ReturnsACorrectClassName() {
			// Prepare
			String expected = "MasterDataGridFieldRenderer";
			// Run
			String returned = unitUnderTest.getMasterDataGridFieldRendererInterfaceName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getMasterDataGridFieldRendererInterfaceName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherMasterDataGridFieldRenderer";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_GRID_FIELD_RENDERER_INTERFACE_NAME))
					.thenReturn(
							new Option(
									GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_GRID_FIELD_RENDERER_INTERFACE_NAME,
									expected));
			// Run
			String returned = unitUnderTest.getMasterDataGridFieldRendererInterfaceName(model);
			// Check
			assertEquals(expected, returned);
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
			assertCorrectClassName(
					"MasterDataGUIConfiguration",
					() -> unitUnderTest.getMasterDataGUIConfigurationClassName(model));
		}

		@Test
		void getMasterDataGUIConfigurationClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherMasterDataGUIConfiguration",
					GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_GUI_CONFIGURATION_CLASS_NAME,
					model,
					m -> unitUnderTest.getMasterDataGUIConfigurationClassName(m));
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
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_MASTER_DATA_VIEW_CLASS_NAME, expected));
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
			assertNull(unitUnderTest.getMasterDataPackageName(null));
		}

		@Test
		void getMasterDataViewPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui.vaadin.masterdata", unitUnderTest.getMasterDataPackageName(model));
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
			assertEquals("vaadin.mapper", unitUnderTest.getMasterDataPackageName(model));
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
		void getPageViewClassName_PassTableModelWithNameCamelCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TestTablePageView";
			when(table.getName()).thenReturn("TestTable");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameUpperCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TablePageView";
			when(table.getName()).thenReturn("TABLE");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameUnderScoreUpperCaseOnly_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNamePageView";
			when(table.getName()).thenReturn("TABLE_NAME");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameUnderScoreLowerCaseOnly_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNamePageView";
			when(table.getName()).thenReturn("table_name");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameUnderScoreMixedCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TableNamePageView";
			when(table.getName()).thenReturn("Table_Name");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelWithNameLowerCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TablePageView";
			when(table.getName()).thenReturn("table");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelNameSingleUpperCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TPageView";
			when(table.getName()).thenReturn("T");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassTableModelNameSinglelowerCase_ReturnsACorrectClassName() {
			// Prepare
			String expected = "TPageView";
			when(table.getName()).thenReturn("t");
			// Run
			String returned = unitUnderTest.getPageViewClassName(table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getPageViewClassName_PassDataModelWithALTERNATE_PAGE_LAYOUT_CLASS_NAME_SUFFIXOption_ReturnsACorrectClassName() {
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
			when(table.findOptionByName(archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE))
					.thenReturn(
							Optional
									.of(
											new Option(
													archimedes.codegenerators.persistence.jpa.PersistenceJPANameGenerator.MODULE,
													prefix)));
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
	class ServiceProviderClassNameTests {

		@Test
		void getServiceProviderClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getServiceProviderClassName(null));
		}

		@Test
		void getServiceProviderClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "ServiceProvider";
			// Run
			String returned = unitUnderTest.getServiceProviderClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getServiceProviderClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherServiceProvider";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_SERVICE_PROVIDER_CLASS_NAME_SUFFIX))
					.thenReturn(
							new Option(GUIVaadinNameGenerator.ALTERNATE_SERVICE_PROVIDER_CLASS_NAME_SUFFIX, expected));
			// Run
			String returned = unitUnderTest.getServiceProviderClassName(model);
			// Check
			assertEquals(expected, returned);
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
	class SessionIdClassNameTests {

		@Test
		void getSessionIdClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getSessionIdClassName(null));
		}

		@Test
		void getSessionIdClassName_passAValidModel_ReturnsACorrectClassName() {
			// Prepare
			String expected = "SessionId";
			// Run
			String returned = unitUnderTest.getSessionIdClassName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getSessionIdClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			// Prepare
			String expected = "AnotherSessionId";
			when(model.getOptionByName(GUIVaadinNameGenerator.ALTERNATE_SESSION_ID_CLASS_NAME))
					.thenReturn(new Option(GUIVaadinNameGenerator.ALTERNATE_SESSION_ID_CLASS_NAME, expected));
			// Run
			String returned = unitUnderTest.getSessionIdClassName(model);
			// Check
			assertEquals(expected, returned);
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
			assertCorrectClassName("TextField", () -> unitUnderTest.getTextFieldClassName(model));
		}

		@Test
		void getTextFieldClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherTextField",
					GUIVaadinNameGenerator.ALTERNATE_TEXT_FIELD_CLASS_NAME_SUFFIX,
					model,
					m -> unitUnderTest.getTextFieldClassName(m));
		}

	}

	@Nested
	class TextFieldFactoryClassNameTests {

		@Test
		void getTextFieldFactoryClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getTextFieldFactoryClassName(null));
		}

		@Test
		void getTextFieldFactoryClassName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("TextFieldFactory", () -> unitUnderTest.getTextFieldFactoryClassName(model));
		}

		@Test
		void getTextFieldFactoryClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherTextFieldFactory",
					GUIVaadinNameGenerator.ALTERNATE_TEXT_FIELD_FACTORY_CLASS_NAME_SUFFIX,
					model,
					m -> unitUnderTest.getTextFieldFactoryClassName(m));
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