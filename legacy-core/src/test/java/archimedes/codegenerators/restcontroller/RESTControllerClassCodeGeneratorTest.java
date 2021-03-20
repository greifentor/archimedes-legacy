package archimedes.codegenerators.restcontroller;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class RESTControllerClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private RESTControllerNameGenerator nameGenerator = new RESTControllerNameGenerator();

	@InjectMocks
	private RESTControllerClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObjectWithoutAnyFields() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest;\n" + ////
					"\n" + //
					"import java.util.NoSuchElementException;\n" + //
					"\n" + //
					"import javax.inject.Inject;\n" + //
					"\n" + //
					"import org.springframework.web.bind.annotation.GetMapping;\n" + //
					"import org.springframework.web.bind.annotation.PathVariable;\n" + //
					"import org.springframework.web.bind.annotation.RequestMapping;\n" + //
					"import org.springframework.web.bind.annotation.RestController;\n" + //
					"\n" + //
					"import base.pack.age.name.rest.converter.ATableDTOConverter;\n" + //
					"import base.pack.age.name.rest.dto.ATableDTO;\n" + //
					"import base.pack.age.name.rest.dto.ATableListDTO;\n" + //
					"import ${IdSOClassNameQualified};\n" + //
					"import base.pack.age.name.service.model.ATableSO;\n" + //
					"\n" + //
					"@RestController\n" + //
					"@RequestMapping(\"api/v1/atables\")\n" + //
					"public class ATableRESTController {\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	private ATableSO service;\n" + //
					"	@Inject\n" + //
					"	private ATableDTOConverter converter;\n" + //
					"\n" + //
					"	@GetMapping\n" + //
					"	public ATableListDTO findAll() {\n" + //
					"		return new ATableListDTO().setContent(converter.convert(service.findAll()));\n" + //
					"	}\n" + //
					"\n" + //
					"	@GetMapping(\"/{id}\")\n" + //
					"	public ATableDTO findById(@PathVariable(name = \"id\", required = true) String id) {\n" + //
					"		return converter\n" + //
					"				.convert(\n" + //
					"						service\n" + //
					"								.findById(new ${IdSOClassName}(id))\n" + //
					"								.orElseThrow(() -> new NoSuchElementException(\"a table not found with id:\" + id)));\n"
					+ //
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