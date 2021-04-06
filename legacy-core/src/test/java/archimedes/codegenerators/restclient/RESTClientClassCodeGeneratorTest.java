package archimedes.codegenerators.restclient;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class RESTClientClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private RESTClientNameGenerator nameGenerator = new RESTClientNameGenerator();

	@InjectMocks
	private RESTClientClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObjectWithoutAnyFields() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import org.springframework.web.client.RestTemplate;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".rest.dto.ATableDTO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".rest.dto.ATableIdDTO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".rest.dto.ATableListDTO;\n" + //
					"\n" + //
					"/**\n" + //
					" * A REST client for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"public class ATableRESTClient {\n" + //
					"\n" + //
					"	public ATableListDTO findAll() {\n" + //
					"		RestTemplate restTemplate = new RestTemplate();\n" + //
					"		String fooResourceUrl = \"http://localhost:8080/api/v1/atables\";\n" + //
					"		return restTemplate.getForObject(fooResourceUrl, ATableListDTO.class);\n" + //
					"	}\n" + //
					"\n" + //
					"	public Optional<ATableDTO> findById(ATableIdDTO id) {\n" + //
					"		RestTemplate restTemplate = new RestTemplate();\n" + //
					"		String fooResourceUrl = \"http://localhost:8080/api/v1/atables/\" + id.getId();\n" + //
					"		return Optional\n" + //
					"				.ofNullable(restTemplate.getForObject(fooResourceUrl, ATableDTO.class));\n" + //
					"	}\n" + //
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
