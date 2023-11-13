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
public class SharedStylesCssFileGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private SharedStylesCssFileGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class Simple {

			private String getExpected(boolean suppressComment) {
				return "/*\n" //
						+ "    CSS styling examples for Vaadin apps.\n" //
						+ "    See also: https://vaadin.com/docs/flow/theme/theming-overview.html and/or\n" //
						+ "    https://vaadin.com/themes/lumo for more information.\n" //
						+ "\n" //
						+ "    " + AbstractCodeGenerator.GENERATED_CODE + "\n" //
						+ "*/\n" //
						+ "\n" //
						+ ".centered-content {\n" //
						+ "    margin: 0 auto;\n" //
						+ "    max-width: 600px;\n" //
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
						"/frontend/styles/shared-styles.css",
						unitUnderTest.getSourceFileName("", dataModel, dataModel));
			}

		}

	}

}
