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
	class getAuthorizationUserInterfaceNameNameTests {

		@Test
		void getAuthorizationUserInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getAuthorizationUserInterfaceName(null));
		}

		@Test
		void getAuthorizationUserInterfaceName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("AuthorizationUser", () -> unitUnderTest.getAuthorizationUserInterfaceName(model));
		}

		@Test
		void getAuthorizationUserInterfaceName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherTextFieldFactory",
					CubeNameGenerator.ALTERNATE_AUTHORIZATION_USER_INTERFACE_NAME,
					model,
					m -> unitUnderTest.getAuthorizationUserInterfaceName(m));
		}

	}

	@Nested
	class getAuthorizationUserServiceImplClassNameests {

		@Test
		void getAuthorizationUserServiceImplClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getAuthorizationUserServiceImplClassName(null));
		}

		@Test
		void getAuthorizationUserServiceImplClassName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName(
					"AuthorizationUserServiceImpl",
					() -> unitUnderTest.getAuthorizationUserServiceImplClassName(model));
		}

		@Test
		void getAuthorizationUserServiceImplClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherClassName",
					CubeNameGenerator.ALTERNATE_AUTHORIZATION_USER_SERVICE_IMPL_CLASS_NAME,
					model,
					m -> unitUnderTest.getAuthorizationUserServiceImplClassName(m));
		}

	}

	@Nested
	class getAuthorizationUserServiceInterfaceNameNameTests {

		@Test
		void getAuthorizationUserServiceInterfaceName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getAuthorizationUserServiceInterfaceName(null));
		}

		@Test
		void getAuthorizationUserServiceInterfaceName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName(
					"AuthorizationUserService",
					() -> unitUnderTest.getAuthorizationUserServiceInterfaceName(model));
		}

		@Test
		void getAuthorizationUserServiceInterfaceName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherClassName",
					CubeNameGenerator.ALTERNATE_AUTHORIZATION_USER_SERVICE_INTERFACE_NAME,
					model,
					m -> unitUnderTest.getAuthorizationUserServiceInterfaceName(m));
		}

	}

	@Nested
	class JWTNotValidExceptionClassNameTests {

		@Test
		void getJWTNotValidExceptionClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getJWTNotValidExceptionClassName(null));
		}

		@Test
		void getJWTNotValidExceptionClassName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("JWTNotValidException", () -> unitUnderTest.getJWTNotValidExceptionClassName(model));
		}

		@Test
		void getJWTNotValidExceptionClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherClassName",
					CubeNameGenerator.ALTERNATE_JWT_NOT_VALID_EXCEPTION_CLASS_NAME,
					model,
					m -> unitUnderTest.getJWTNotValidExceptionClassName(m));
		}

	}

	@Nested
	class JWTServiceConfigurationClassNameTests {

		@Test
		void getJWTServiceConfigurationClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getJWTServiceConfigurationClassName(null));
		}

		@Test
		void getJWTServiceConfigurationClassName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName(
					"JWTServiceConfiguration",
					() -> unitUnderTest.getJWTServiceConfigurationClassName(model));
		}

		@Test
		void getJWTServiceConfigurationClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherClassName",
					CubeNameGenerator.ALTERNATE_JWT_SERVICE_CONFIGURATION_CLASS_NAME,
					model,
					m -> unitUnderTest.getJWTServiceConfigurationClassName(m));
		}

	}

	@Nested
	class JWTServiceImplClassNameTests {

		@Test
		void getJWTServiceImplClassName_passANullValueAsTableModel_returnsANullValue() {
			assertNull(unitUnderTest.getJWTServiceImplClassName(null));
		}

		@Test
		void getJWTServiceImplClassName_passAValidModel_ReturnsACorrectClassName() {
			assertCorrectClassName("JWTServiceImpl", () -> unitUnderTest.getJWTServiceImplClassName(model));
		}

		@Test
		void getJWTServiceImplClassName_passAValidModelWithAlternateComponentName_ReturnsACorrectClassName() {
			assertCorrectAlternativeClassName(
					"AnotherClassName",
					CubeNameGenerator.ALTERNATE_JWT_SERVICE_IMPL_CLASS_NAME,
					model,
					m -> unitUnderTest.getJWTServiceImplClassName(m));
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
					"AnotherClassName",
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
					"AnotherClassName",
					CubeNameGenerator.ALTERNATE_WEB_APP_CONFIGURATION_CLASS_NAME,
					model,
					m -> unitUnderTest.getWebAppConfigurationClassName(m));
		}

	}

}
