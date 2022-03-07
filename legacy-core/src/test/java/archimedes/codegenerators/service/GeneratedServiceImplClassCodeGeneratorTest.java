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
public class GeneratedServiceImplClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private GeneratedServiceImplClassCodeGenerator unitUnderTest;

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
		return createExpected(suppressComment, null, false);
	}

	private String createExpected(boolean suppressComment, Boolean findByUnique, boolean listAccess) {
		String expected = "package " + BASE_PACKAGE_NAME + ".core.service.impl;\n" + //
				"\n" + //
				"import java.util.List;\n" + //
				"import java.util.Optional;\n" + //
				"\n" + //
				"import javax.inject.Inject;\n" + //
				"\n" + //
				"import base.pack.age.name.core.model.Page;\n" + //
				"import base.pack.age.name.core.model.PageParameters;\n" + //
				"import base.pack.age.name.core.model.ATable;\n";
		if (listAccess) {
			expected += "import base.pack.age.name.core.model.AnotherTable;\n";
		}
		expected += "import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" + //
				"import base.pack.age.name.core.service.ATableService;\n" + //
				"import lombok.Generated;\n" + //
				"\n";
		if (!suppressComment) {
			expected += "/**\n" + " * A generated service interface implementation for ATable management.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n";
		}
		expected += "@Generated\n" + //
				"public abstract class ATableGeneratedServiceImpl implements ATableService {\n" + //
				"\n" + //
				"	@Inject\n" + //
				"	protected ATablePersistencePort persistencePort;\n" + //
				"\n" + //
				"	@Override\n" + //
				"	public ATable create(ATable model) {\n" + //
				"		return persistencePort.create(model);\n" + //
				"	}\n" + //
				"\n" + //
				"	@Override\n" + //
				"	public List<ATable> findAll() {\n" + //
				"		return persistencePort.findAll();\n" + //
				"	}\n" + //
				"\n" + //
				"	@Override\n" + //
				"	public Page<ATable> findAll(PageParameters pageParameters) {\n" + //
				"		return persistencePort.findAll(pageParameters);\n" + //
				"	}\n" + //
				"\n" + //
				"	@Override\n" + //
				"	public Optional<ATable> findById(Long id) {\n" + //
				"		return persistencePort.findById(id);\n" + //
				"	}\n" + //
				"\n" + //
				"	@Override\n" + //
				"	public ATable update(ATable model) {\n" + //
				"		return persistencePort.update(model);\n" + //
				"	}\n" + //
				"\n" + //
				"	@Override\n" + //
				"	public void delete(ATable model) {\n" + //
				"		persistencePort.delete(model);\n" + //
				"	}\n";
		if (findByUnique != null) {
			expected += "\n" + //
					"	@Override\n";
			if (findByUnique) {
				expected += "	public Optional<ATable> findByDescription(String description) {\n" + //
						"		return persistencePort.findByDescription(description);\n" + //
						"	}\n";
			} else {
				expected += "	public List<ATable> findAllByDescription(String description) {\n" + //
						"		return persistencePort.findAllByDescription(description);\n" + //
						"	}\n";
			}
		}
		if (listAccess) {
			expected += "\n" + //
					"	@Override\n" + //
					"	public List<ATable> findAllByRef(AnotherTable ref) {\n" + //
					"		return persistencePort.findAllByRef(ref);\n" + //
					"	}\n";
		}
		expected += "\n}";
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
		String expected = createExpected(true, true, false);
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
	void happyRunForASimpleObject_FindByNotUnique() {
		// Prepare
		String expected = createExpected(true, false, false);
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

	@Test
	void happyRunForASimpleObject_ListAccess() {
		// Prepare
		String expected = createExpected(true, null, true);
		DataModel dataModel = readDataModel("Model-ForeignKey.xml");
		dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
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