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
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class GeneratedPersistencePortInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private GeneratedPersistencePortInterfaceCodeGenerator unitUnderTest;

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
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * A generated persistence port interface for ATable CRUD operations.\n" + //
					" *\n" + //
					" * " + AbstractCodeGenerator.GENERATED_CODE + "\n" + //
					" */\n" + //
					"@Generated\n" + //
					"public interface ATableGeneratedPersistencePort {\n" + //
					"\n" + //
					"	ATable create(ATable model);\n" + //
					"\n" + //
					"	List<ATable> findAll();\n" + //
					"\n" + //
					"	Page<ATable> findAll(PageParameters pageParameters);\n" + //
					"\n" + //
					"	Optional<ATable> findById(Long id);\n" + //
					"\n" + //
					"	ATable update(ATable model);\n" + //
					"\n" + //
					"	void delete(ATable model);\n" + //
					"\n" + //
					"}";
			DataModel dataModel = readDataModel("Model.xml");
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
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"@Generated\n" + //
					"public interface ATableGeneratedPersistencePort {\n" + //
					"\n" + //
					"	ATable create(ATable model);\n" + //
					"\n" + //
					"	List<ATable> findAll();\n" + //
					"\n" + //
					"	Page<ATable> findAll(PageParameters pageParameters);\n" + //
					"\n" + //
					"	Optional<ATable> findById(Long id);\n" + //
					"\n" + //
					"	ATable update(ATable model);\n" + //
					"\n" + //
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

		private String getExpectedForFindBy(boolean findByUnique) {
			String s = "package " + BASE_PACKAGE_NAME + ".core.service.port.persistence;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"@Generated\n" + //
					"public interface ATableGeneratedPersistencePort {\n" + //
					"\n" + //
					"	ATable create(ATable model);\n" + //
					"\n" + //
					"	List<ATable> findAll();\n" + //
					"\n" + //
					"	Page<ATable> findAll(PageParameters pageParameters);\n" + //
					"\n" + //
					"	Optional<ATable> findById(Long id);\n" + //
					"\n" + //
					"	ATable update(ATable model);\n" + //
					"\n" + //
					"	void delete(ATable model);\n" + //
					"\n";
			if (findByUnique) {
				s += "	Optional<ATable> findByDescription(String description);\n";
			} else {
				s += "	List<ATable> findAllByDescription(String description);\n";
			}
			s += "\n" + //
					"}";
			return s;
		}

		@Test
		void happyRunForASimpleObject_FindBy() {
			// Prepare
			String expected = getExpectedForFindBy(true);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
			TableModel table = dataModel.getTableByName("A_TABLE");
			ColumnModel column = table.getColumnByName("Description");
			column.addOption(new Option("FIND_BY"));
			column.setUnique(true);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Test
		void happyRunForASimpleObject_FindBy_NotUnique() {
			// Prepare
			String expected = getExpectedForFindBy(false);
			DataModel dataModel = readDataModel("Model.xml");
			dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
			TableModel table = dataModel.getTableByName("A_TABLE");
			ColumnModel column = table.getColumnByName("Description");
			column.addOption(new Option("FIND_BY"));
			column.setUnique(false);
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

		@Nested
		class ListAccess {

			private String getExpectedForListAccess() {
				String s = "package " + BASE_PACKAGE_NAME + ".core.service.port.persistence;\n" + //
						"\n" + //
						"import java.util.List;\n" + //
						"import java.util.Optional;\n" + //
						"\n" + //
						"import base.pack.age.name.core.model.Page;\n" + //
						"import base.pack.age.name.core.model.PageParameters;\n" + //
						"import base.pack.age.name.core.model.ATable;\n" + //
						"import base.pack.age.name.core.model.AnotherTable;\n" + //
						"import lombok.Generated;\n" + //
						"\n" + //
						"@Generated\n" + //
						"public interface ATableGeneratedPersistencePort {\n" + //
						"\n" + //
						"	ATable create(ATable model);\n" + //
						"\n" + //
						"	List<ATable> findAll();\n" + //
						"\n" + //
						"	Page<ATable> findAll(PageParameters pageParameters);\n" + //
						"\n" + //
						"	Optional<ATable> findById(Long id);\n" + //
						"\n" + //
						"	ATable update(ATable model);\n" + //
						"\n" + //
						"	void delete(ATable model);\n" + //
						"\n" + //
						"	List<ATable> findAllByRef(AnotherTable ref);\n" + //
						"\n" + //
						"}";
				return s;
			}

			@Test
			void happyRunForASimpleObject_ListAccess() {
				// Prepare
				String expected = getExpectedForListAccess();
				DataModel dataModel = readDataModel("Model-ForeignKey.xml");
				dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
				TableModel table = dataModel.getTableByName("A_TABLE");
				dataModel
						.addOption(
								new Option(
										AbstractClassCodeGenerator.REFERENCE_MODE,
										AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
				table.getColumnByName("REF").addOption(new Option(AbstractClassCodeGenerator.LIST_ACCESS));
				// Run
				String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
				// Check
				assertEquals(expected, returned);
			}

		}

	}

}