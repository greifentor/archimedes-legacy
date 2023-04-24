package archimedes.codegenerators.localization;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

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
public class LocalizationNameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private LocalizationNameGenerator unitUnderTest;

	@Nested
	class FileBasedResourceManagerConfigurationClassNameTests {

		@Test
		void returnsTheCorrectClassName() {
			assertEquals(
					"FileBasedResourceManagerConfiguration",
					unitUnderTest.getFileBasedResourceManagerConfigurationClassName());
		}

	}

	@Nested
	class FileBasedResourceManagerImplClassNameTests {

		@Test
		void returnsTheCorrectClassName() {
			assertEquals("FileBasedResourceManagerImpl", unitUnderTest.getFileBasedResourceManagerImplClassName());
		}

	}

	@Nested
	class LocalizationSOClassNameTests {

		@Test
		void returnsTheCorrectClassName() {
			assertEquals("LocalizationSO", unitUnderTest.getLocalizationSOClassName());
		}

	}

	@Nested
	class LocalizationSOPackageNameTests {

		@Test
		void getLocalizationSOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getLocalizationSOPackageName(null));
		}

		@Test
		void getLocalizationSOPackageName_PassAValidDataModel_ReturnsACorrectPackageName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core.model.localization";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getLocalizationSOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getLocalizationSOPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectPackageName() {
			// Prepare
			String expected = "core.model.localization";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getLocalizationSOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getLocalizationSOPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectPAckageName() {
			// Prepare
			String expected = "core.model.localization";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getLocalizationSOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getLocalizationSOPackageName_PassAValidDataModelWithSetWithAlternatePackageNameForModelClasses_ReturnsACorrectPackageName() {
			// Prepare
			String alternatePackageName = "alternate.package.name.core.model.localization";
			String expected = alternatePackageName;
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePackageName);
			when(model.getOptionByName(LocalizationNameGenerator.ALTERNATE_LOCALIZATION_SO_PACKAGE_NAME))
					.thenReturn(option);
			// Run
			String returned = unitUnderTest.getLocalizationSOPackageName(model);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class ResourceManagerInterfaceNameTests {

		@Test
		void returnsTheCorrectClassName() {
			assertEquals("ResourceManager", unitUnderTest.getResourceManagerInterfaceName());
		}

	}

	@Nested
	class ResourceManagerImplPackageNameTests {

		@Test
		void getLocalizationSOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getResourceManagerImplPackageName(null, table));
		}

		@Test
		void getResourceManagerImplPackageName_PassANullValueAsTable_ReturnsDefaultValue() {
			assertEquals(
					"core.service.impl.localization",
					unitUnderTest.getResourceManagerImplPackageName(model, null));
		}

		@Test
		void getResourceManagerImplPackageName_PassAValidDataModel_ReturnsACorrectPackageName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core.service.impl.localization";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getResourceManagerImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getLocalizationSOPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectPackageName() {
			// Prepare
			String expected = "core.service.impl.localization";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getResourceManagerImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getResourceManagerImplPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectPAckageName() {
			// Prepare
			String expected = "core.service.impl.localization";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getResourceManagerImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getResourceManagerImplPackageName_PassAValidDataModelWithSetWithAlternatePackageNameForModelClasses_ReturnsACorrectPackageName() {
			// Prepare
			String alternatePackageName = "alternate.package.name.core.service.impl.localization";
			String expected = alternatePackageName;
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePackageName);
			when(model.getOptionByName(LocalizationNameGenerator.ALTERNATE_RESOURCE_MANAGER_IMPL_PACKAGE_NAME))
					.thenReturn(option);
			// Run
			String returned = unitUnderTest.getResourceManagerImplPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class ResourceManagerPackageNameTests {

		@Test
		void getLocalizationSOPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getResourceManagerPackageName(null, table));
		}

		@Test
		void getResourceManagerPackageName_PassANullValueAsTable_ReturnsDefaultValue() {
			assertEquals("core.service.localization", unitUnderTest.getResourceManagerPackageName(model, null));
		}

		@Test
		void getResourceManagerPackageName_PassAValidDataModel_ReturnsACorrectPackageName() {
			// Prepare
			String expected = BASE_PACKAGE_NAME + ".core.service.localization";
			when(model.getBasePackageName()).thenReturn(BASE_PACKAGE_NAME);
			// Run
			String returned = unitUnderTest.getResourceManagerPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getLocalizationSOPackageName_PassAValidDataModelWithEmptyBasePackageName_ReturnsACorrectPackageName() {
			// Prepare
			String expected = "core.service.localization";
			when(model.getBasePackageName()).thenReturn("");
			// Run
			String returned = unitUnderTest.getResourceManagerPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getResourceManagerPackageName_PassAValidDataModelWithNullBasePackageName_ReturnsACorrectPAckageName() {
			// Prepare
			String expected = "core.service.localization";
			when(model.getBasePackageName()).thenReturn(null);
			// Run
			String returned = unitUnderTest.getResourceManagerPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void getResourceManagerPackageName_PassAValidDataModelWithSetWithAlternatePackageNameForModelClasses_ReturnsACorrectPackageName() {
			// Prepare
			String alternatePackageName = "alternate.package.name.core.service.localization";
			String expected = alternatePackageName;
			OptionModel option = mock(OptionModel.class);
			when(option.getParameter()).thenReturn(alternatePackageName);
			when(model.getOptionByName(LocalizationNameGenerator.ALTERNATE_RESOURCE_MANAGER_PACKAGE_NAME))
					.thenReturn(option);
			// Run
			String returned = unitUnderTest.getResourceManagerPackageName(model, table);
			// Check
			assertEquals(expected, returned);
		}

	}

}