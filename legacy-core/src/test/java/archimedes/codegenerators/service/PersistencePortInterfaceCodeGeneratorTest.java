package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.codegenerators.AbstractCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class PersistencePortInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private PersistencePortInterfaceCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Nested
	class TestsOfMethod_generate_String_TableModel {

		@Nested
		class SimplePrimaryKey {

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = "package " + BASE_PACKAGE_NAME + ".core.service.port.persistence;\n" + //
						"\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
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
						"	ATable create(ATable model);\n" + "\n"
						+ "	Page<ATable> findAll(PageParameters pageParameters);\n" + "\n"
						+ "	Optional<ATable> findById(Long id);\n" + "\n" + "	ATable update(ATable model);\n" + "\n"
						+ "	void delete(ATable model);\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model.xml");
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
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
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import lombok.Generated;\n" + //
						"\n" + //
						"@Generated\n" + //
						"public interface ATablePersistencePort {\n" + //
						"\n" + //
						"	ATable create(ATable model);\n" + "\n"
						+ "	Page<ATable> findAll(PageParameters pageParameters);\n" + "\n"
						+ "	Optional<ATable> findById(Long id);\n" + "\n" + "	ATable update(ATable model);\n" + "\n"
						+ "	void delete(ATable model);\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model.xml");
				dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
				// Check
				assertEquals(expected, returned);
			}

		}

		@Nested
		class CompositePrimaryKey {

			@Test
			void happyRunForASimpleObject() {
				// Prepare
				String expected = "package " + BASE_PACKAGE_NAME + ".core.service.port.persistence;\n" + //
						"\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.model.ATableId;\n" + //
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
						"	ATable create(ATable model);\n" + //
						"\n" + //
						"	Page<ATable> findAll(PageParameters pageParameters);\n" + //
						"\n" + //
						"	Optional<ATable> findById(ATableId aTableId);\n" + //
						"\n" + //
						"	ATable update(ATable model);\n" + //
						"\n" + //
						"	void delete(ATable model);\n" + //
						"\n" + //
						"}";
				DataModel dataModel = readDataModel("Model-CompositeKey.xml");
				// Run
				String returned =
						unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}