package archimedes.codegenerators.rest.controller.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.rest.controller.springboot.ListDTOClassCodeGenerator;
import archimedes.codegenerators.rest.controller.springboot.RESTControllerNameGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class ListDTOClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private RESTControllerNameGenerator nameGenerator = new RESTControllerNameGenerator();

	@InjectMocks
	private ListDTOClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObjectWithoutAnyFields() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.dto;\n" + //
					"\n" + //
					"import java.util.ArrayList;\n" + //
					"import java.util.List;\n" + //
					"\n" + //
					"import lombok.Data;\n" + //
					"import lombok.Generated;\n" + //
					"import lombok.experimental.Accessors;\n" + //
					"\n" + //
					"@Accessors(chain = true)\n" + //
					"@Data\n" + //
					"@Generated\n" + //
					"public class ATableListDTO {\n" + //
					"\n" + //
					"	private List<ATableDTO> content = new ArrayList<>();\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}