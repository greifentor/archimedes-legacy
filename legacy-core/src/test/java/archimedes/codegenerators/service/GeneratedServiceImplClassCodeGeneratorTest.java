package archimedes.codegenerators.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
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
		String expected = "package " + BASE_PACKAGE_NAME + ".core.service.impl;\n" + //
				"\n" + //
                "import java.util.List;\n" + //
                "import java.util.Optional;\n" + //
				"\n" + //
				"import javax.inject.Inject;\n" + //
				"\n" + //
				"import base.pack.age.name.core.model.Page;\n" + //
				"import base.pack.age.name.core.model.PageParameters;\n" + //
				"import base.pack.age.name.core.model.ATable;\n" + //
				"import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" + //
				"import base.pack.age.name.core.service.ATableService;\n" + //
				"import lombok.Generated;\n" + //
				"\n";
		if (!suppressComment) {
			expected += "/**\n" +
					" * A generated service interface implementation for ATable management.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n";
		}
		expected += "@Generated\n" + //
				"public abstract class ATableGeneratedServiceImpl implements ATableService {\n" + //
				"\n" + //
				"\t@Inject\n" + //
				"\tprotected ATablePersistencePort persistencePort;\n" + //
				"\n" + //
				"\t@Override\n" + //
				"\tpublic ATable create(ATable model) {\n" + //
				"\t\treturn persistencePort.create(model);\n" + //
				"\t}\n" + //
				"\n" + //
                "\t@Override\n" + //
                "\tpublic List<ATable> findAll() {\n" + //
                "\t\treturn persistencePort.findAll();\n" + //
                "\t}\n" + //
                "\n" + //
				"\t@Override\n" + //
				"\tpublic Page<ATable> findAll(PageParameters pageParameters) {\n" + //
				"\t\treturn persistencePort.findAll(pageParameters);\n" + //
				"\t}\n" + //
				"\n" + //
				"\t@Override\n" + //
				"\tpublic Optional<ATable> findById(Long id) {\n" + //
				"\t\treturn persistencePort.findById(id);\n" + //
				"\t}\n" + //
				"\n" + //
				"\t@Override\n" + //
				"\tpublic ATable update(ATable model) {\n" + //
				"\t\treturn persistencePort.update(model);\n" + //
				"\t}\n" + //
				"\n" + //
				"\t@Override\n" + //
				"\tpublic void delete(ATable model) {\n" + //
				"\t\tpersistencePort.delete(model);\n" + //
				"\t}\n" + //
				"\n" + //
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

}