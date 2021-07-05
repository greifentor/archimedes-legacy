package archimedes.codegenerators.service;

import archimedes.codegenerators.AbstractClassCodeGenerator;
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
public class ServiceImplClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private ServiceNameGenerator nameGenerator = new ServiceNameGenerator();

	@InjectMocks
	private ServiceImplClassCodeGenerator unitUnderTest;

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
		String expected = "package " + BASE_PACKAGE_NAME + ".core.service.impl;\n" +
				"\n" +
				"import java.util.Optional;\n" +
				"\n" +
				"import javax.inject.Inject;\n" +
				"import javax.inject.Named;\n" +
				"\n" +
				"import base.pack.age.name.core.model.ATable;\n" +
				"import base.pack.age.name.core.service.port.persistence.ATablePersistencePort;\n" +
				"import lombok.Generated;\n" +
				"\n";
		if (!suppressComment) {
			expected += "/**\n" +
					" * A service interface implementation for ATable management.\n" +
					" *\n" +
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" +
					" */\n";
		}
		expected += "@Generated\n" +
				"@Named\n" +
				"public class ATableServiceImpl {\n" +
				"\n" +
				"\t@Inject\n" +
				"\tprivate ATablePersistencePort persistencePort;\n" +
				"\n" +
				"\tpublic ATable create(ATable model) {\n" +
				"\t\treturn persistencePort.create(model);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic Optional<ATable> findById(Long id) {\n" +
				"\t\treturn persistencePort.findById(id);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic ATable update(ATable model) {\n" +
				"\t\treturn persistencePort.update(model);\n" +
				"\t}\n" +
				"\n" +
				"\tpublic void delete(ATable model) {\n" +
				"\t\tpersistencePort.delete(model);\n" +
				"\t}\n" +
				"\n" +
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