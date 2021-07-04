package archimedes.codegenerators.service;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.codegenerators.temporal.PersistencePortGeneratedInterfaceCodeGenerator;
import archimedes.codegenerators.temporal.TemporalDataCodeFactory;
import archimedes.codegenerators.temporal.TemporalDataNameGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class PersistencePortInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private TemporalDataNameGenerator nameGenerator = new TemporalDataNameGenerator();

	@InjectMocks
	private PersistencePortInterfaceCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Test
		void happyRunForASimpleObject() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".core.service.port.persistence;\n" + //
					"\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * A persistence port interface for ATable CRUD operations.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"public interface ATablePersistencePort {\n" + //
					"\n" + //
					"	ATable create(ATable model);\n" +
					"\n" +
					"	Optional<ATable> findById(Long id);\n" +
					"\n" +
					"	ATable update(ATable model);\n" +
					"\n" +
					"	void delete(ATable model);\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			// dataModel.getTableByName("A_TABLE").addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObject_NoComments() {
			// Prepare
			String expected = "package " + BASE_PACKAGE_NAME + ".core.service.port.persistence;\n" + //
					"\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"@Generated\n" + //
					"public interface ATablePersistencePort {\n" + //
					"\n" + //
					"	ATable create(ATable model);\n" +
					"\n" +
					"	Optional<ATable> findById(Long id);\n" +
					"\n" +
					"	ATable update(ATable model);\n" +
					"\n" +
					"	void delete(ATable model);\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
			// Check
			assertEquals(expected, returned);
		}

	}

}