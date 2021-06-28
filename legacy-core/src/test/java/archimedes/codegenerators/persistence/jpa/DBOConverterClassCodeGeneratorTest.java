package archimedes.codegenerators.persistence.jpa;

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
public class DBOConverterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DBOConverterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".persistence.converter;\n" + //
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
					"import " + BASE_PACKAGE_NAME + ".persistence.entity.ATableDBO;\n" + //
					"import " + BASE_PACKAGE_NAME + ".service.model.ATableSO;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO converter for a_tables.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class ATableDBOConverter {\n" + //
					"\n" + //
					"	public ATableDBO toDBO(ATableSO so) {\n" + //
					"		if (so == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableDBO()\n" + //
					"				.setId(so.getId())\n" + //
					"				.setADate(so.getADate())\n" + //
					"				.setDescription(so.getDescription());\n" + //
					"	}\n" + //
					"\n" + //
					"	public ATableSO toSO(ATableDBO dbo) {\n" + //
					"		if (dbo == null) {\n" + //
					"			return null;\n" + //
					"		}\n" + //
					"		return new ATableSO()\n" + //
					"				.setId(dbo.getId())\n" + //
					"				.setADate(dbo.getADate())\n" + //
					"				.setDescription(dbo.getDescription());\n" + //
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