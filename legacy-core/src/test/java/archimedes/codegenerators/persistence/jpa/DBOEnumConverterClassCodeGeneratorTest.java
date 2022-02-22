package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class DBOEnumConverterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private DBOEnumConverterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_DomainModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected();
			DataModel dataModel = readDataModel("Model.xml");
			dataModel
					.getDomainByName("Description")
					.addOption(new Option(AbstractClassCodeGenerator.ENUM, "ONE,TWO,THREE"));
			// Run
			String returned =
					unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getDomainByName("Description"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected() {
			String s = "package base.pack.age.name.persistence.converter;\n" + //
					"\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.entity.DescriptionDBO;\n" + //
					"import base.pack.age.name.core.model.Description;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO enum converter for descriptions.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"@Named\n" + //
					"public class DescriptionDBOConverter {\n" + //
					"\n" + //
					"	public DescriptionDBO toDBO(Description model) {\n" + //
					"		return model == null ? null : DescriptionDBO.valueOf(model.name());\n" + //
					"	}\n" + //
					"\n" + //
					"	public Description toModel(DescriptionDBO dbo) {\n" + //
					"		return dbo == null ? null : Description.valueOf(dbo.name());\n" + //
					"	}\n" + //
					"\n" + //
					"}";
			return s;
		}

	}

}