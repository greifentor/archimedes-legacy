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
public class JWTServiceImplClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private JWTServiceImplClassCodeGenerator unitUnderTest;

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
						+ "import static base.pack.age.name.util.Check.ensure;\n" //
						+ "\n" //
						+ "import java.time.LocalDateTime;\n" //
						+ "import java.time.format.DateTimeFormatter;\n" //
						+ "\n" //
						+ "import javax.inject.Named;\n" //
						+ "\n" //
						+ "import org.apache.logging.log4j.LogManager;\n" //
						+ "import org.apache.logging.log4j.Logger;\n" //
						+ "\n" //
						+ "import com.auth0.jwt.JWT;\n" //
						+ "import com.auth0.jwt.algorithms.Algorithm;\n" //
						+ "import com.auth0.jwt.interfaces.DecodedJWT;\n" //
						+ "import com.auth0.jwt.interfaces.JWTVerifier;\n" //
						+ "\n" //
						+ "import base.pack.age.name.core.service.AuthorizationUserService;\n" //
						+ "import base.pack.age.name.core.service.JWTService;\n" //
						+ "import base.pack.age.name.core.service.exception.JWTNotValidException;\n" //
						+ "import lombok.Generated;\n" //
						+ "import lombok.RequiredArgsConstructor;\n" //
						+ "\n" //
						+ "/**\n" //
						+ " * An implementation for the JWT service interface.\n" //
						+ " *\n" //
						+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
						+ " */\n" //
						+ "@Generated\n" //
						+ "@Named\n" //
						+ "@RequiredArgsConstructor\n" //
						+ "public class JWTServiceImpl implements JWTService {\n" //
						+ "\n" //
						+ "	private static final Logger LOGGER = LogManager.getLogger(JWTServiceImpl.class);\n" //
						+ "\n" //
						+ "	private final JWTServiceConfiguration configuration;\n" //
						+ "	private final AuthorizationUserService authorizationUserService;\n" //
						+ "\n" //
						+ "	@Override\n" //
						+ "	public AuthorizationData getAuthorizationData(String jwt) {\n" //
						+ "		ensure(jwt != null, \"jwt cannot be null.\");\n" //
						+ "		verifyJWT(jwt);\n" //
						+ "		DecodedJWT decodedJWT = decodeJWT(jwt);\n" //
						+ "		ensureJWTContainsDataForAllField(decodedJWT);\n" //
						+ "		ensureEndOfValidityIsInRange(decodedJWT);\n" //
						+ "		return createAuthorizationData(decodedJWT);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private void verifyJWT(String jwt) {\n" //
						+ "		Algorithm algorithm = Algorithm.HMAC512(configuration.getSecret());\n" //
						+ "		JWTVerifier verifier = JWT.require(algorithm).build();\n" //
						+ "		verifier.verify(jwt);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private DecodedJWT decodeJWT(String jwt) {\n" //
						+ "		return JWT.decode(jwt);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private void ensureJWTContainsDataForAllField(DecodedJWT decodedJWT) {\n" //
						+ "		ensure(decodedJWT.getClaims().get(CLAIM_NAME_APPLICATION_NAME) != null, new JWTNotValidException());\n" //
						+ "		ensure(decodedJWT.getClaims().get(CLAIM_NAME_APPLICATION_RIGHTS) != null, new JWTNotValidException());\n" //
						+ "		ensure(decodedJWT.getClaims().get(CLAIM_NAME_LOGIN_DATE) != null, new JWTNotValidException());\n" //
						+ "		ensure(decodedJWT.getClaims().get(CLAIM_NAME_USER_GLOBAL_ID) != null, new JWTNotValidException());\n" //
						+ "		ensure(decodedJWT.getClaims().get(CLAIM_NAME_USER_NAME) != null, new JWTNotValidException());\n" //
						+ "		ensure(decodedJWT.getClaims().get(CLAIM_NAME_USER_TOKEN) != null, new JWTNotValidException());\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private void ensureEndOfValidityIsInRange(DecodedJWT decodedJWT) {\n" //
						+ "		if (!configuration.isTestMode()) {\n" //
						+ "			LocalDateTime loginDate = getLoginDate(decodedJWT);\n" //
						+ "			ensure(\n" //
						+ "					!loginDate.minusMinutes(configuration.getBaseValidityInMinutes()).isAfter(LocalDateTime.now()),\n" //
						+ "					new JWTNotValidException());\n" //
						+ "		} else {\n" //
						+ "			LOGGER.info(\"token end of validtity ignored by test mode!\");\n" //
						+ "		}\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private LocalDateTime getLoginDate(DecodedJWT decodedJWT) {\n" //
						+ "		return getLocalDateTime(decodedJWT.getClaims().get(CLAIM_NAME_LOGIN_DATE).asString());\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private LocalDateTime getLocalDateTime(String s) {\n" //
						+ "		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(\"yyyy-MM-dd HH:mm:ss\");\n" //
						+ "		return LocalDateTime.parse(s, formatter);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	private AuthorizationData createAuthorizationData(DecodedJWT decodedJWT) {\n" //
						+ "		LocalDateTime loginDate = getLoginDate(decodedJWT);\n" //
						+ "		return new AuthorizationData(\n" //
						+ "				decodedJWT.getClaims().get(CLAIM_NAME_APPLICATION_NAME).asString(),\n" //
						+ "				loginDate,\n" //
						+ "				authorizationUserService.findByGlobalIdOrCreate(decodedJWT),\n" //
						+ "				decodedJWT.getClaims().get(CLAIM_NAME_APPLICATION_RIGHTS).asArray(String.class));\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	@Override\n" //
						+ "	public LocalDateTime getLoginDate(String jwt) {\n" //
						+ "		return getLocalDateTime(decodeJWT(jwt).getClaims().get(CLAIM_NAME_LOGIN_DATE).asString());\n" //
						+ "	}\n" //
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
