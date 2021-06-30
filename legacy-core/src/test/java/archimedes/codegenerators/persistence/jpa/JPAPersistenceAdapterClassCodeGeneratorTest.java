package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

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
			String expected = getExpected(null, "persistence", false);
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import java.util.Optional;\n" + //
							"\n" + //
							"import javax.inject.Inject;\n" + //
							"import javax.inject.Named;\n" + //
							"\n" + //
							"import base.pack.age.name.persistence.converter.ATableDBOConverter;\n" + //
							"import base.pack.age.name.persistence.repository.ATableDBORepository;\n" + //
							"import base.pack.age.name.core.model.ATable;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A DBO persistence adapter for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@Named\n" + //
					"public class ATableJPAPersistenceAdapter {\n" + //
					"\n" + //
					"\t@Inject\n" + //
					"\tprivate ATableDBOConverter converter;\n" + //
					"\t@Inject\n" + //
					"\tprivate ATableDBORepository repository;\n" + //
					"\n" + //
					"\tpublic ATable create(ATable model) {\n" + //
					"\t\tmodel.setId(null);\n" + //
					"\t\treturn converter.toModel(repository.save(converter.toDbo(model)));\n" + //
					"\t}\n" + //
					"\n" + //
					"\tpublic Optional<ATable> findById(Long key) {\n" + //
					"\t\treturn repository.findById(key).map(dbo -> converter.toModel(dbo));\n" + //
					"\t}\n" + //
					"\n" + //
					"\tpublic ATable update(ATable model) {\n" + //
					"\t\treturn converter.toModel(repository.save(converter.toDbo(model)));\n" + //
					"\t}\n" + //
					"\n" + //
					"\tpublic void delete(ATable model) {\n" + //
					"\t\treturn repository.delete(model.getId());\n" + //
					"\t}\n" + //
					"\n}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "persistence", true);
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}