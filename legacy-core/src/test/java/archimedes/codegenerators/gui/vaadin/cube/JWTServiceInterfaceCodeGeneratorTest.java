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
public class JWTServiceInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private JWTServiceInterfaceCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_DataModel_DataModel {

		@Nested
		class SimpleClass {

			private String getExpected() {
				return "package base.pack.age.name.core.service;\n" //
						+ "\n" //
						+ "import java.time.LocalDateTime;\n" //
						+ "\n" //
						+ "import base.pack.age.name.core.model.AuthorizationUser;\n" //
						+ "import lombok.AllArgsConstructor;\n" //
						+ "import lombok.EqualsAndHashCode;\n" //
						+ "import lombok.Generated;\n" //
						+ "import lombok.Getter;\n" //
						+ "import lombok.experimental.Accessors;\n" //
						+ "\n" //
						+ "/**\n" //
						+ " * An interface for the JWT service.\n" //
						+ " *\n" //
						+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
						+ " */\n" //
						+ "@Generated\n" //
						+ "public interface JWTService {\n" //
						+ "\n" //
						+ "	public static final String CLAIM_NAME_APPLICATION_NAME = \"applicationName\";\n" //
						+ "	public static final String CLAIM_NAME_APPLICATION_RIGHTS = \"applicationRights\";\n" //
						+ "	public static final String CLAIM_NAME_LOGIN_DATE = \"loginDate\";\n" //
						+ "	public static final String CLAIM_NAME_USER_GLOBAL_ID = \"userGlobalId\";\n" //
						+ "	public static final String CLAIM_NAME_USER_NAME = \"userName\";\n" //
						+ "	public static final String CLAIM_NAME_USER_TOKEN = \"userToken\";\n" //
						+ "\n" //
						+ "	@Accessors(chain = true)\n" //
						+ "	@AllArgsConstructor\n" //
						+ "	@EqualsAndHashCode\n" //
						+ "	@Getter\n" //
						+ "	@Generated\n" //
						+ "	public static class AuthorizationData {\n" //
						+ "\n" //
						+ "		private String applicationName;\n" //
						+ "		private LocalDateTime loginDateTime;\n" //
						+ "		private AuthorizationUser user;\n" //
						+ "		private String[] applicationRights;\n" //
						+ "\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	AuthorizationData getAuthorizationData(String jwt);\n" //
						+ "\n" //
						+ "	LocalDateTime getLoginDate(String jwt);\n" //
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
