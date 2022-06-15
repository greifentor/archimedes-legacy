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
import archimedes.codegenerators.FindByUtils;
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

	@Test
	void happyRunForASimpleObject_FindByWithEnumType() {
		// Prepare
		String expected = "package base.pack.age.name.core.service;\n" + //
				"\n" + //
				"import java.util.List;\n" + //
				"import java.util.Optional;\n" + //
				"\n" + //
				"import base.pack.age.name.core.model.Page;\n" + //
				"import base.pack.age.name.core.model.PageParameters;\n" + //
				"import base.pack.age.name.core.model.TableWithEnumType;\n" + //
				"import base.pack.age.name.core.model.EnumType;\n" + //
				"import lombok.Generated;\n" + //
				"\n" + //
				"/**\n" + //
				" * A generated service interface for TableWithEnumType management.\n" + //
				" *\n" + //
				" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
				" */\n" + //
				"@Generated\n" + //
				"public interface TableWithEnumTypeGeneratedService {\n" + //
				"\n" + //
				"	TableWithEnumType create(TableWithEnumType model);\n" + //
				"\n" + //
				"	List<TableWithEnumType> findAll();\n" + //
				"\n" + //
				"	Page<TableWithEnumType> findAll(PageParameters pageParameters);\n" + //
				"\n" + //
				"	Optional<TableWithEnumType> findById(Long id);\n" + //
				"\n" + //
				"	TableWithEnumType update(TableWithEnumType model);\n" + //
				"\n" + //
				"	void delete(TableWithEnumType model);\n" + //
				"\n" + //
				"	List<TableWithEnumType> findAllByEnumField(EnumType enumField);\n" + //
				"\n" + //
				"}";
		DataModel dataModel = readDataModel("Model.xml");
		TableModel table = dataModel.getTableByName("TABLE_WITH_ENUM_TYPE");
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void happyRunForASimpleObject_FindByWithUniqueString() {
		// Prepare
		String expected = "package base.pack.age.name.core.service;\n" + //
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
				" * A generated service interface for ATable management.\n" + //
				" *\n" + //
				" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
				" */\n" + //
				"@Generated\n" + //
				"public interface ATableGeneratedService {\n" + //
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
				"	Optional<ATable> findByDescription(String description);\n" + //
				"\n" + //
				"}";
		DataModel dataModel = readDataModel("Model.xml");
		TableModel table = dataModel.getTableByName("A_TABLE");
		table.getColumnByName("Description").addOption(new Option(FindByUtils.FIND_BY));
		table.getColumnByName("Description").setUnique(true);
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void happyRunForASimpleObject_FindByWithString() {
		// Prepare
		String expected = "package base.pack.age.name.core.service;\n" + //
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
				" * A generated service interface for ATable management.\n" + //
				" *\n" + //
				" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
				" */\n" + //
				"@Generated\n" + //
				"public interface ATableGeneratedService {\n" + //
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
				"	List<ATable> findAllByDescription(String description);\n" + //
				"\n" + //
				"}";
		DataModel dataModel = readDataModel("Model.xml");
		TableModel table = dataModel.getTableByName("A_TABLE");
		table.getColumnByName("Description").addOption(new Option(FindByUtils.FIND_BY));
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
			void passANoSubclassModel_returnsFalse() {
				// Prepare
				DataModel dataModel = readDataModel("Model.xml");
				TableModel table = dataModel.getTableByName("A_TABLE");
				// Run & Check
				assertFalse(unitUnderTest.isToIgnoreFor(dataModel, table));
			}

		}

	}

	@Nested
	class Inheritance {

		@Test
		void simpleInheritance() {
			// Prepare
			String expected = "package base.pack.age.name.core.service;\n" + //
					"\n" + //
					"import java.util.List;\n" + //
					"import java.util.Optional;\n" + //
					"\n" + //
					"import base.pack.age.name.core.model.Page;\n" + //
					"import base.pack.age.name.core.model.PageParameters;\n" + //
					"import base.pack.age.name.core.model.ATable;\n" + //
					"import base.pack.age.name.core.model.AnotherHeirTable;\n" + //
					"import base.pack.age.name.core.model.AnotherHeirTableWithSameReference;\n" + //
					"import base.pack.age.name.core.model.AnotherTable;\n" + //
					"import base.pack.age.name.core.model.HeirTableWithReference;\n" + //
					"import base.pack.age.name.core.model.IgnoredHeirTable;\n" + //
					"import lombok.Generated;\n" + //
					"\n" + //
					"/**\n" + //
					" * A generated service interface for ATable management.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n" + //
					"@Generated\n" + //
					"public interface ATableGeneratedService {\n" + //
					"\n" + //
					"	ATable create(ATable model);\n" + //
					"\n" + //
					"	List<ATable> findAll();\n" + //
					"\n" + //
					"	List<AnotherHeirTable> findAllAnotherHeirTable();\n" + //
					"\n" + //
					"	List<AnotherHeirTableWithSameReference> findAllAnotherHeirTableWithSameReference();\n" + //
					"\n" + //
					"	List<AnotherTable> findAllAnotherTable();\n" + //
					"\n" + //
					"	List<HeirTableWithReference> findAllHeirTableWithReference();\n" + //
					"\n" + //
					"	List<IgnoredHeirTable> findAllIgnoredHeirTable();\n" + //
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
			DataModel dataModel = readDataModel("Model-Inheritance.xml");
			TableModel table = dataModel.getTableByName("A_TABLE");
			// Run
			String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, table);
			// Check
			assertEquals(expected, returned);
		}

	}

}