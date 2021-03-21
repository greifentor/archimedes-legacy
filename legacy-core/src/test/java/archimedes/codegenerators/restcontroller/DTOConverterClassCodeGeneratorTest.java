package archimedes.codegenerators.restcontroller;

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
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class DTOConverterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private RESTControllerNameGenerator nameGenerator = new RESTControllerNameGenerator();

	@InjectMocks
	private DTOConverterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".rest.dto.ATableDTO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".service.model.ATableSO;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DTO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class ATableDTOConverter {\n" + //
					"\n" + //
					"	public ATableDTO convert(ATableSO so) {\n" + //
					"		if (so == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDTO()\n" + //
					"				.setId(so.getId())\n" + //
					"				.setADate(so.getADate())\n" + //
					"				.setDescription(so.getDescription());\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDTO> convert(List<ATableSO> sos) {\n" + //
					"		if (sos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return sos.stream().map(this::convert).collect(Collectors.toList());\n" + //
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
		void happyRunForASimpleObject_WithIdClassOptionInModel() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".rest.dto.ATableDTO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".service.model.ATableSO;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DTO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class ATableDTOConverter {\n" + //
					"\n" + //
					"	public ATableDTO convert(ATableSO so) {\n" + //
					"		if (so == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDTO()\n" + //
					"				.setId(so.getId().getKey())\n" + //
					"				.setADate(so.getADate())\n" + //
					"				.setDescription(so.getDescription());\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDTO> convert(List<ATableSO> sos) {\n" + //
					"		if (sos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return sos.stream().map(this::convert).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(DTOConverterClassCodeGenerator.GENERATE_ID_CLASS));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObject_WithIdClassOptionInTable() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".rest.converter;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.stream.Collectors;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import java.time.LocalDate;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".rest.dto.ATableDTO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".service.model.ATableSO;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DTO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class ATableDTOConverter {\n" + //
					"\n" + //
					"	public ATableDTO convert(ATableSO so) {\n" + //
					"		if (so == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDTO()\n" + //
					"				.setId(so.getId().getKey())\n" + //
					"				.setADate(so.getADate())\n" + //
					"				.setDescription(so.getDescription());\n" + //
					"	}\n" + //
					"\n" + //
					"	public List<ATableDTO> convert(List<ATableSO> sos) {\n" + //
					"		if (sos == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return sos.stream().map(this::convert).collect(Collectors.toList());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.getTableByName("A_TABLE").addOption(new Option(DTOConverterClassCodeGenerator.GENERATE_ID_CLASS));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}