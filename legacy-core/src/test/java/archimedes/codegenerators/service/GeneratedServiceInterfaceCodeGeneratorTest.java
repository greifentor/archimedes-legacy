package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
		return createExpected(suppressComment, null);
	}

	private String createExpected(boolean suppressComment, Boolean findByUnique) {
		String expected = "package " + BASE_PACKAGE_NAME + ".core.service;\n" + //
				"\n" + //
				"import java.util.List;\n" + //
				"import java.util.Optional;\n" + //
				"\n" + //
				"import base.pack.age.name.core.model.Page;\n" + //
				"import base.pack.age.name.core.model.PageParameters;\n" + //
				"import base.pack.age.name.core.model.ATable;\n" + //
				"import lombok.Generated;\n" + //
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
		String expected = createExpected(false, true);
		DataModel dataModel = readDataModel("Model.xml");
		TableModel table = dataModel.getTableByName("A_TABLE");
		ColumnModel column = table.getColumnByName("Description");
		column.addOption(new Option("FIND_BY"));
		column.setUnique(true);
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

	@Test
	void happyRunForASimpleObject_FindByNotUnique() {
		// Prepare
		String expected = createExpected(false, false);
		DataModel dataModel = readDataModel("Model.xml");
		TableModel table = dataModel.getTableByName("A_TABLE");
		ColumnModel column = table.getColumnByName("Description");
		column.addOption(new Option("FIND_BY"));
		column.setUnique(false);
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

}