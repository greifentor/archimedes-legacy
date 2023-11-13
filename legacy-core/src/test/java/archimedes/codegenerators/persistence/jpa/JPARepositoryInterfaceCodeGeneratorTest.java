package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class JPARepositoryInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@InjectMocks
	private JPARepositoryInterfaceCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = getExpected("persistence.repository");
			DataModel dataModel = readDataModel("Model.xml");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		private String getExpected(String packageName) {
			return getExpected(null, packageName);
		}

		private String getExpected(String prefix, String packageName) {
			return "package " + BASE_PACKAGE_NAME + "." + (prefix != null ? prefix + "." : "") + packageName + ";\n" + //
					"\n" + //
					"public interface ATableDBORepository extends ATableGeneratedDBORepository {\n" + //
					"}";
		}

	}

	@Nested
	class TestsOfMethod_isToIgnoreFor_DataModel_TableModel {

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