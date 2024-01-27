package archimedes.codegenerators.gui.vaadin.cube;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.gui.vaadin.AbstractGUIVaadinClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class AuthorizationUserServiceImplClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private AuthorizationUserServiceImplClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_DataModel_DataModel {

		@Nested
		class SimpleClass {

			private String getExpected() {
				return "package base.pack.age.name.core.service.impl;\n" //
						+ "\n" //
						+ "import javax.inject.Named;\n" //
						+ "\n" //
						+ "import com.auth0.jwt.interfaces.DecodedJWT;\n" //
						+ "\n" //
						+ "import base.pack.age.name.core.model.AuthorizationUser;\n" //
						+ "import base.pack.age.name.core.service.AuthorizationUserService;\n" //
						+ "\n" //
						+ "import lombok.RequiredArgsConstructor;\n" //
						+ "\n" //
						+ "/**\n" //
						+ " * Implementation for the AuthorizationUserService interface.\n" //
						+ " * \n" //
						+ " * FEEL FREE TO OVERRIDE !!!\n" //
						+ " */\n" //
						+ "@Named\n" //
						+ "@RequiredArgsConstructor\n" //
						+ "public class AuthorizationUserServiceImpl implements AuthorizationUserService {\n" //
						+ "\n" //
						+ "//	private final UserService userService;\n" //
						+ "\n" //
						+ "	@Override\n" //
						+ "	public AuthorizationUser findByGlobalIdOrCreate(DecodedJWT decodedJWT) {\n" //
						+ "//		return userService\n" //
						+ "//				.findByGlobalId(getGlobalIdFromClaimString(decodedJWT))\n" //
						+ "//				.orElseGet(() -> createUser(decodedJWT));\n" //
						+ "		return null;\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private UUID getGlobalIdFromClaimString(DecodedJWT decodedJWT) {\n" //
						+ "		String globalId = getClaimAsString(decodedJWT, JWTService.CLAIM_NAME_USER_GLOBAL_ID);\n" //
						+ "		return globalId;\n" //
						+ "		// return (globalId != null) && !globalId.isEmpty() ? UUID.fromString(globalId) : null;\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private String getClaimAsString(DecodedJWT decodedJWT, String claimIdentifier) {\n" //
						+ "		return decodedJWT.getClaims().get(claimIdentifier).asString();\n" //
						+ "	}\n" //
						+ "\n" //
						+ "//	private User createUser(DecodedJWT decodedJWT) {\n" //
						+ "//		return userService\n" //
						+ "//				.update(\n" //
						+ "//						userService\n" //
						+ "//								.create(\n" //
						+ "//										new User()\n" //
						+ "//												.setGlobalId(getGlobalIdFromClaimString(decodedJWT))\n" //
						+ "//												.setName(getClaimAsString(decodedJWT, JWTService.CLAIM_NAME_USER_NAME))\n" //
						+ "//												.setToken(\n" //
						+ "//														getClaimAsString(\n" //
						+ "//																decodedJWT,\n" //
						+ "//																JWTService.CLAIM_NAME_USER_TOKEN))));\n" //
						+ "//	}\n" //
						+ "\n" //
						+ "}";
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected();
				DataModel dataModel = readDataModel("Model.xml");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

	@Nested
	class TestsOfMethod_isToIgnoreFor_DataModel_DataModel {

		@Test
		void returnsFalse_whenCUBE_APPLICATIONIsSetForModel() {
			// prepare
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractGUIVaadinClassCodeGenerator.CUBE_APPLICATION));
			// Run & Check
			assertFalse(unitUnderTest.isToIgnoreFor(dataModel, null));
		}

		@Test
		void returnsTrue_whenCUBE_APPLICATIONIsNOTSetForModel() {
			// prepare
			DataModel dataModel = readDataModel("Model.xml");
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(dataModel, null));
		}

	}

}
