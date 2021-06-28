package archimedes.codegenerators.rest.controller.springboot;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class RESTControllerSpringBootClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private RESTControllerNameGenerator nameGenerator = new RESTControllerNameGenerator();

	@InjectMocks
	private RESTControllerSpringBootClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.v1;\n" + ////
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
					"import base.pack.age.name.service.ATableService;\n" + //
					"\n" + //
					"/**\n" + //
					" * A REST controller for a tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@RestController\n" + //
					"@RequestMapping(\"api/v1/atables\")\n" + //
					"public class ATableRESTController {\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	private ATableService service;\n" + //
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
					"								.findById(id)\n" + //
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

		@Test
		void happyRunForASimpleObject_GenerateIdClassInModel() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.v1;\n" + ////
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
					"import base.pack.age.name.service.model.ATableIdSO;\n" + //
					"import base.pack.age.name.service.ATableService;\n" + //
					"\n" + //
					"/**\n" + //
					" * A REST controller for a tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@RestController\n" + //
					"@RequestMapping(\"api/v1/atables\")\n" + //
					"public class ATableRESTController {\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	private ATableService service;\n" + //
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
					"								.findById(new ATableIdSO().setKey(id))\n" + //
					"								.orElseThrow(() -> new NoSuchElementException(\"a table not found with id:\" + id)));\n"
					+ //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.GENERATE_ID_CLASS));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObject_GenerateIdClassInTable() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.v1;\n" + ////
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
					"import base.pack.age.name.service.model.ATableIdSO;\n" + //
					"import base.pack.age.name.service.ATableService;\n" + //
					"\n" + //
					"/**\n" + //
					" * A REST controller for a tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@RestController\n" + //
					"@RequestMapping(\"api/v1/atables\")\n" + //
					"public class ATableRESTController {\n" + //
					"\n" + //
					"	@Inject\n" + //
					"	private ATableService service;\n" + //
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
					"								.findById(new ATableIdSO().setKey(id))\n" + //
					"								.orElseThrow(() -> new NoSuchElementException(\"a table not found with id:\" + id)));\n"
					+ //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.getTableByName("A_TABLE").addOption(new Option(AbstractClassCodeGenerator.GENERATE_ID_CLASS));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}