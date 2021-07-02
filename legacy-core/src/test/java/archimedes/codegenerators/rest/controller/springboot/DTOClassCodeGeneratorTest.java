package archimedes.codegenerators.rest.controller.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class DTOClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private RESTControllerNameGenerator nameGenerator = new RESTControllerNameGenerator();

	@InjectMocks
	private DTOClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObjectWithoutAnyFields() {
			// Prepare
			String expected = createExpectedCode(false);
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String createExpectedCode(boolean suppressComments) {
			String code = "package " + BASE_PACKAGE_NAME + ".rest.v1.dto;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Data;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.experimental.Accessors;\n" + //
					"\n";
			if (!suppressComments) {
				code += "/**\n" + //
						" * A DTO for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			code += "@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"public class ATableDTO {\n" + //
					"\n" + //
					"	private long id;\n" + //
					"	private LocalDate aDate;\n" + //
					"	private String description;\n" + //
					"\n" + //
					"}";
			return code;
		}

		@Test
		void happyRunForASimpleObjectWithoutAnyFields_CommentsSuppressed() {
			// Prepare
			String expected = createExpectedCode(true);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}