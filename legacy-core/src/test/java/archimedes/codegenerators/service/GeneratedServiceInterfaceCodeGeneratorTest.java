package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.ColumnModel;
import archimedes.model.DataModel;
import archimedes.model.TableModel;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class GeneratedServiceInterfaceCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private GeneratedServiceInterfaceCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Test
	void happyRunForASimpleObject() {
		// Prepare
		String expected = createExpected(false);
		DataModel dataModel = readDataModel("Model.xml");
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

	private String createExpected(boolean suppressComment) {
		return createExpected(suppressComment, null, null);
	}

	private String createExpected(boolean suppressComment, Boolean findByUnique, Boolean listAccessReference) {
		String expected = "package " + BASE_PACKAGE_NAME + ".core.service;\n" + //
				"\n" + //
				"import java.util.List;\n" + //
				"import java.util.Optional;\n" + //
				"\n" + //
				"import base.pack.age.name.core.model.Page;\n" + //
				"import base.pack.age.name.core.model.PageParameters;\n" + //
				"import base.pack.age.name.core.model.ATable;\n";
		if ((listAccessReference != null) && (listAccessReference == true)) {
			expected += "import base.pack.age.name.core.model.AnotherTable;\n";
		}
		expected += "import lombok.Generated;\n" + //
				"\n";
		if (!suppressComment) {
			expected += "/**\n" + //
					" * A generated service interface for ATable management.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n";
		}
		expected += "@Generated\n" + //
				"public interface ATableGeneratedService {\n" + //
				"\n" + //
				"\tATable create(ATable model);\n" + //
				"\n" + //
				"\tList<ATable> findAll();\n" + //
				"\n" + //
				"\tPage<ATable> findAll(PageParameters pageParameters);\n" + //
				"\n" + //
				"\tOptional<ATable> findById(Long id);\n" + //
				"\n" + //
				"\tATable update(ATable model);\n" + //
				"\n" + //
				"\tvoid delete(ATable model);\n";
		if (findByUnique != null) {
			expected += "\n";
			if (findByUnique) {
				expected += "	Optional<ATable> findByDescription(String description);\n";
			} else {
				expected += "	List<ATable> findAllByDescription(String description);\n";
			}
		}
		if (listAccessReference != null) {
			if (listAccessReference) {
				expected += "\n" + //
						"	List<ATable> findAllByRef(AnotherTable ref);\n";
			} else {
				expected += "\n" + //
						"	List<ATable> findAllByDescription(String description);\n";
			}
		}
		expected += "\n" + //
				"}";
		return expected;
	}

	@Test
	void happyRunForASimpleObject_NoComment() {
		// Prepare
		String expected = createExpected(true);
		DataModel dataModel = readDataModel("Model.xml");
		dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void happyRunForASimpleObject_FindBy() {
		// Prepare
		String expected = createExpected(false, true, null);
		DataModel dataModel = readDataModel("Model.xml");
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
	void happyRunForASimpleObject_FindByNotUnique() {
		// Prepare
		String expected = createExpected(false, false, null);
		DataModel dataModel = readDataModel("Model.xml");
		TableModel table = dataModel.getTableByName("A_TABLE");
		ColumnModel column = table.getColumnByName("Description");
		column.addOption(new Option("FIND_BY"));
		column.setUnique(false);
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void happyRunForASimpleObject_ListAccess() {
		// Prepare
		String expected = createExpected(false, null, false);
		DataModel dataModel = readDataModel("Model.xml");
		TableModel table = dataModel.getTableByName("A_TABLE");
		table.getColumnByName("Description").addOption(new Option(AbstractClassCodeGenerator.LIST_ACCESS));
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void happyRunForASimpleObject_ListAccess_ReferenceMode() {
		// Prepare
		String expected = createExpected(false, null, true);
		DataModel dataModel = readDataModel("Model-ForeignKey.xml");
		dataModel
				.addOption(
						new Option(
								AbstractClassCodeGenerator.REFERENCE_MODE,
								AbstractClassCodeGenerator.REFERENCE_MODE_OBJECT));
		TableModel table = dataModel.getTableByName("A_TABLE");
		table.getColumnByName("REF").addOption(new Option(AbstractClassCodeGenerator.LIST_ACCESS));
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
		// Check
		assertEquals(expected, returned);
	}

	@Nested
	class TestsOfMethod_isToIgnoreFor_DataModel_TableModel {

		@Nested
		class Subclasses {

			@Test
			void passASubclassModel_returnsTrue() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				table.addOption(new Option(AbstractClassCodeGenerator.SUBCLASS));
				// Run & Check
				assertTrue(unitUnderTest.isToIgnoreFor(dataModel, table));
			}

			@Test
			void passANoSubnlassModel_returnsFalse() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				// Run & Check
				assertFalse(unitUnderTest.isToIgnoreFor(dataModel, table));
			}

		}

	}

}