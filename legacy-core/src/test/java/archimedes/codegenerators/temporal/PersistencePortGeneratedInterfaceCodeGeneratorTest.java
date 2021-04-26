package archimedes.codegenerators.temporal;

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
public class PersistencePortGeneratedInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private TemporalDataNameGenerator nameGenerator = new TemporalDataNameGenerator();

	@InjectMocks
	private PersistencePortGeneratedInterfaceCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".service.ports;\n" + //
					"\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"import " + BASE_PACKAGE_NAME + ".service.ATableIdSO;\n" + //
					"\n" + //
					"/**\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"public interface ATablePersistencePortGenerated {\n" + //
					"\n" + //
					"	void saveDescription(ATableIdSO id, String description);\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.getTableByName("A_TABLE").addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
			dataModel
					.getTableByName("A_TABLE")
					.getColumnByName("Description")
					.addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
			dataModel
					.getTableByName("A_TABLE")
					.getColumnByName("ADate")
					.addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}