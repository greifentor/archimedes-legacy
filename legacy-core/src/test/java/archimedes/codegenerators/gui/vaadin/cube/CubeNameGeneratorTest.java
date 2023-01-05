package archimedes.codegenerators.gui.vaadin.cube;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

import java.util.function.Function;
import java.util.function.Supplier;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;

@ExtendWith(MockitoExtension.class)
public class CubeNameGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Mock
	private ColumnModel column;
	@Mock
	private DataModel model;
	@Mock
	private TableModel table;

	@InjectMocks
	private CubeNameGenerator unitUnderTest;

	static void assertCorrectClassName(String expected, Supplier<String> check) {
		assertEquals(expected, check.get());
	}

	static void assertCorrectAlternativeClassName(String expected, String alternativIdentfier, DataModel model,
			Function<DataModel, String> check) {
		when(model.getOptionByName(alternativIdentfier)).thenReturn(new Option(alternativIdentfier, expected));
		assertEquals(expected, check.apply(model));
	}

	@Nested
	class AccessCheckerInterfaceNameTests {

		@Test
		void getAccessCheckerInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getAccessCheckerInterfaceName(null));
		}

		@Test
		void getAccessCheckerInterfaceName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("AccessChecker", () -> unitUnderTest.getAccessCheckerInterfaceName(model));
		}

		@Test
		void getAccessCheckerInterfaceName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherTextFieldFactory",
					CubeNameGenerator.ALTERNATE_ACCESS_CHECKER_INTERFACE_NAME,
					model,
					m -> unitUnderTest.getAccessCheckerInterfaceName(m));
		}

	}

	@Nested
	class AccessCheckerPackageNameTests {

		@Test
		void getAccessCheckerPackageName_PassANullValueAsModel_ReturnsANullValue() {
			assertNull(unitUnderTest.getAccessCheckerPackageName(null));
		}

		@Test
		void getAccessCheckerPackageName_PassANullValueAsTable_ReturnsANullValue() {
			assertEquals("gui", unitUnderTest.getAccessCheckerPackageName(model));
		}

		@Test
		void getAccessCheckerPackageName_PassAValidTableButModelAsAlternateNameOption_ReturnsACorrectPackageName() {
			// Prepare
			String alternative = "alternative.package.name";
			when(model.getOptionByName(CubeNameGenerator.ALTERNATE_ACCESS_CHECKER_PACKAGE_NAME))
					.thenReturn(
							new Option(CubeNameGenerator.ALTERNATE_ACCESS_CHECKER_PACKAGE_NAME, alternative));
			// Run & Check
			assertEquals(alternative, unitUnderTest.getAccessCheckerPackageName(model));
		}

	}

	@Nested
	class AuthorizationDataClassNameTests {

		@Test
		void getAuthorizationDataClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getAuthorizationDataClassName(null));
		}

		@Test
		void getAuthorizationDataClassName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("AuthorizationData", () -> unitUnderTest.getAuthorizationDataClassName(model));
		}

		@Test
		void getAuthorizationDataClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherTextFieldFactory",
					CubeNameGenerator.ALTERNATE_AUTHORIZATION_DATA_CLASS_NAME,
					model,
					m -> unitUnderTest.getAuthorizationDataClassName(m));
		}

	}

	@Nested
	class JWTServiceInterfaceNameTests {

		@Test
		void getJWTServiceInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getJWTServiceInterfaceName(null));
		}

		@Test
		void getJWTServiceInterfaceName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("JWTService", () -> unitUnderTest.getJWTServiceInterfaceName(model));
		}

		@Test
		void getJWTServiceInterfaceName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherTextFieldFactory",
					CubeNameGenerator.ALTERNATE_JWT_SERVICE_INTERFACE_NAME,
					model,
					m -> unitUnderTest.getJWTServiceInterfaceName(m));
		}

	}

	@Nested
	class WebAppConfigurationClassNameTests {

		@Test
		void getWebAppConfigurationClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getWebAppConfigurationClassName(null));
		}

		@Test
		void getWebAppConfigurationClassName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("WebAppConfiguration", () -> unitUnderTest.getWebAppConfigurationClassName(model));
		}

		@Test
		void getWebAppConfigurationClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherTextFieldFactory",
					CubeNameGenerator.ALTERNATE_WEB_APP_CONFIGURATION_CLASS_NAME,
					model,
					m -> unitUnderTest.getWebAppConfigurationClassName(m));
		}

	}

}
