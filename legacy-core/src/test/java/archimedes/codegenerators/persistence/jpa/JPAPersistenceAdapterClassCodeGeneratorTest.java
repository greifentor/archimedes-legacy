package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

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
					"	@Inject\n" + //
					"	private ATableDBOConverter converter;\n" + //
					"	@Inject\n" + //
					"	private ATableDBORepository repository;\n" + //
					"\n" + //
					"	public ATable create(ATable model) {\n" + //
					"		model.setId(null);\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	public Optional<ATable> findById(Long key) {\n" + //
					"		return repository.findById(key).map(dbo -> converter.toModel(dbo));\n" + //
					"	}\n" + //
					"\n" + //
					"	public ATable update(ATable model) {\n" + //
					"		return converter.toModel(repository.save(converter.toDBO(model)));\n" + //
					"	}\n" + //
					"\n" + //
					"	public void delete(ATable model) {\n" + //
					"		return repository.delete(model.getId());\n" + //
					"	}\n" + //
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

	@Nested
	class TestsOfMethod_isToIgnoreFo_DataModel_TableModel {

		@Test
		void passTableModelAsNullValue_ThrowsAnException() {
			// Prepare
			DataModel model = mock(DataModel.class);
			// Run & Check
			assertThrows(NullPointerException.class, () -> unitUnderTest.isToIgnoreFor(model, null));
		}

		@Test
		void passPassAModelWithNoDependentAttribute_ReturnsFalse() {
			// Prepare
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			// Run & Check
			assertFalse(unitUnderTest.isToIgnoreFor(dataModel, table));
		}

		@Test
		void passPassAModelWithDependentAttribute_ReturnsTrue() {
			// Prepare
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table
					.getColumnByName("Description")
					.addOption(new Option(JPAPersistenceAdapterDependentClassCodeGenerator.DEPENDENT_ATTRIBUTE));
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(dataModel, table));
		}

	}

}