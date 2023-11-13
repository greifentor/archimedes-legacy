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
			String expected = getExpected(null, "persistence", false, "null");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String prefix, String packageName, boolean suppressComment, String noKeyValue) {
			String s =
					"package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
							"\n" + //
							"import javax.inject.Named;\n" + //
							"\n" + //
							"import lombok.Generated;\n" + //
							"\n";
			if (!suppressComment) {
				s += "/**\n" + //
						" * A JPA persistence adapter for a_tables.\n" + //
						" *\n" + //
						" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
						" */\n";
			}
			s += "@Generated\n" + //
					"@Named\n" + //
					"public class ATableJPAPersistenceAdapter extends ATableGeneratedJPAPersistenceAdapter {\n" + //
					"}";
			return s;
		}

		@Test
		void happyRunForASimpleObjectWithSuppressedComments() {
			// Prepare
			String expected = getExpected(null, "persistence", true, "null");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObjectWithNoSimplePK() {
			// Prepare
			String expected = getExpected(null, "persistence", false, "-1");
			DataModel dataModel = readDataModel("Model.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			table.getColumnByName("ID").setNotNull(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

	@Nested
	class TestsOfMethod_isToIgnoreFor_DataModel_TableModel {

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

		@Test
		void passAMemberModel_returnsTrue() {
			// Prepare
			ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
			DataModel dataModel = reader.read("src/test/resources/examples/dm/Example-BookStore.xml");
			TableModel table = dataModel.getTableByName("CHAPTER");
			// Run & Check
			assertTrue(unitUnderTest.isToIgnoreFor(dataModel, table));
		}

	}

}