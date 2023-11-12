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
public class VaadinCheckboxStylesCssFileGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private VaadinCheckboxStylesCssFileGenerator unitUnderTest;

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
						+ ":host(:not([focused])) [part=\"checkbox\"] {\n" //
						+ "    background-color: darkGray;\n" //
						+ "    border-color: black;\n" //
						+ "    border-style: solid;\n" //
						+ "    border-width: thin;\n" //
						+ "}\n" //
						+ "\n" //
						+ ":host([focused]) [part=\"checkbox\"] {\n" //
						+ "    background-color: lightred;\n" //
						+ "    border-color: darkred;\n" //
						+ "    border-style: solid;\n" //
						+ "    border-width: thin;\n" //
						+ "    opacity: 1;\n" //
						+ "}\n" //
						+ "\n" //
						+ ":host([checked]) [part=\"checkbox\"] {\n" //
						+ "    background-color: lightgreen;\n" //
						+ "}\n" //
						+ "\n" //
						+ ":host(:not([checked])) [part=\"checkbox\"] {\n" //
						+ "    background-color: lightred;\n" //
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
						"/frontend/styles/vaadin-checkbox-styles.css",
						unitUnderTest.getSourceFileName("", dataModel, dataModel));
			}

		}

	}

}
