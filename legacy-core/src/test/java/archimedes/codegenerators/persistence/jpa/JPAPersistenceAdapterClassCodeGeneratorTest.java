package archimedes.codegenerators.persistence.jpa;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.xml.ModelXMLReader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class JPAPersistenceAdapterClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private JPAPersistenceAdapterClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected("persistence");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"
			));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String packageName) {
			return getExpected(null, packageName);
		}

		private String getExpected(String prefix, String packageName) {
			return "package " + BASE_PACKAGE_NAME + "." + (prefix != null
					? prefix + "."
					: "") + packageName + ";\n" + //
					"\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import javax.inject.Inject;\n" + //
					"import javax.inject.Named;\n" + //
					"\n" + //
					"import base.pack.age.name.persistence.converter.ATableDBOConverter;\n" + //
					"import base.pack.age.name.persistence.repository.ATableDBORepository;\n" + //
					"import base.pack.age.name.service.model.ATableSO;\n" + //
					"\n" + //
					"/**\n" + //
					" * A DBO persistence adapter for a_tables.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Named\n" + //
					"public class ATableJPAPersistenceAdapter {\n" + //
					"\n" + //
					"\t@Inject\n" + //
					"\tprivate ATableDBOConverter converter;\n" + //
					"\t@Inject\n" + //
					"\tprivate ATableDBORepository repository;\n" + //
					"\n" + //
					"\tpublic Optional<ATableSO> findById(Long key) {\n" + //
					"\t\treturn repository.findById(key).map(dbo -> converter.toSO(dbo));\n" + //
					"\t}\n" + //
					"\n}";
		}

	}

}