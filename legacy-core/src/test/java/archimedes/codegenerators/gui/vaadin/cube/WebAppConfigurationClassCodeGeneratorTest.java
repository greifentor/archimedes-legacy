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
public class WebAppConfigurationClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private WebAppConfigurationClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_DataModel_DataModel {

		@Nested
		class SimpleClass {

			private String getExpected() {
				return "package base.pack.age.name.gui.vaadin;\n" //
						+ "\n" //
						+ "import org.springframework.beans.factory.annotation.Value;\n" //
						+ "import org.springframework.context.annotation.Configuration;\n" //
						+ "\n" //
						+ "import lombok.Getter;\n" //
						+ "import lombok.Generated;\n" //
						+ "\n" //
						+ "/**\n" //
						+ " * A class for the web app configuration;\n" //
						+ " *\n" //
						+ " * GENERATED CODE !!! DO NOT CHANGE !!!\n" //
						+ " */\n" //
						+ "@Configuration\n" //
						+ "@Generated\n" //
						+ "@Getter\n" //
						+ "public class WebAppConfiguration {\n" //
						+ "\n" //
						+ "	@Value(\"${app.version}\")\n" //
						+ "	private String appVersion;\n" //
						+ "\n" //
						+ "	@Value(\"${cube.url}\")\n" //
						+ "	private String cubeURL;\n" //
						+ "\n" //
						+ "	@Value(\"${maximum.jwt.validity-in-minutes:60}\")\n" //
						+ "	private int maximumJWTValidityInMinutes;\n" //
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
