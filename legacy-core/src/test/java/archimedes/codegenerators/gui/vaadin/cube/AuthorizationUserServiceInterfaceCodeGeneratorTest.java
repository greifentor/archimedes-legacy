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
public class AuthorizationUserServiceInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private AuthorizationUserServiceInterfaceCodeGenerator unitUnderTest;

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
						+ "import com.auth0.jwt.interfaces.DecodedJWT;\n" //
						+ "\n" //
						+ "import base.pack.age.name.core.model.AuthorizationUser;\n" //
						+ "import lombok.Generated;\n" //
						+ "\n" //
						+ "/**\n" //
						+ " * An interface for the authorization user service.\n" //
						+ " *\n" //
						+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
						+ " */\n" //
						+ "@Generated\n" //
						+ "public interface AuthorizationUserService {\n" //
						+ "\n" //
						+ "	AuthorizationUser findByGlobalIdOrCreate(DecodedJWT decodedJWT);\n" //
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
