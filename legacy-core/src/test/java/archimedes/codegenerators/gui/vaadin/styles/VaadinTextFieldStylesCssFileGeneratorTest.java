package archimedes.codegenerators.gui.vaadin.styles;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class VaadinTextFieldStylesCssFileGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private VaadinTextFieldStylesCssFileGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class Simple {

			private String getExpected(boolean suppressComment) {
				return "/* " + AbstractCodeGenerator.GENERATED_CODE + " */\n" //
						+ "[part=\"input-field\"] {\n" //
						+ "    background-color: beige;\n" //
						+ "    border-color: gray;\n" //
						+ "    border-style: solid;\n" //
						+ "    border-width: thin;\n" //
						+ "    color: black;\n" //
						+ "}\n" //
						+ "\n" //
						+ ":host([focused]) [part=\"input-field\"] {\n" //
						+ "    background-color: white;\n" //
						+ "    border-color: darkred;\n" //
						+ "    border-style: solid;\n" //
						+ "    border-width: thin;\n" //
						+ "    color: black;\n" //
						+ "    opacity: 1;\n" //
						+ "}\n" //
						+ "\n" //
						+ ":host([focused]:not([readonly])) [part=\"label\"] {\n" //
						+ "    color: darkred;\n" //
						+ "}";
			}

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = getExpected(false);
				DataModel dataModel = readDataModel("Model.xml");
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel);
				// Check
				assertEquals(expected, returned);
			}

			@Test
			void worksWithCorrectFileName() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				// Run & Check
				assertEquals(
						"/frontend/styles/vaadin-text-field-styles.css",
						unitUnderTest.getSourceFileName("", dataModel, dataModel));
			}

		}

	}

}
