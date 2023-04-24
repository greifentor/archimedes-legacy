package archimedes.codegenerators.localization;

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
public class FileBasedResourceManagerConfigurationClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private LocalizationNameGenerator nameGenerator = new LocalizationNameGenerator();

	@InjectMocks
	private FileBasedResourceManagerConfigurationClassCodeGenerator unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@Test
	void happyRunForASimpleObject() {
		// Prepare
		String expected = createExpected(false);
		DataModel dataModel = readDataModel("Model.xml");
		dataModel.setApplicationName("Test APP");
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

	private String createExpected(boolean suppressComment) {
		String expected =
				"package " + BASE_PACKAGE_NAME + ".core.service.impl.localization;\n" //
						+ "\n" //
						+ "import org.springframework.beans.factory.annotation.Value;\n" //
						+ "import org.springframework.context.annotation.Configuration;\n" //
						+ "\n" //
						+ "import " + BASE_PACKAGE_NAME + ".core.model.localization.LocalizationSO;\n" //
						+ "import lombok.Getter;\n" //
						+ "\n";
		if (!suppressComment) {
			expected += "/**\n" + //
					" * A Configuration for the file based resource manager.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n";
		}
		expected +=
				"@Configuration\n" //
						+ "@Getter\n" //
						+ "public class FileBasedResourceManagerConfiguration {\n" //
						+ "\n" //
						+ "	@Value(\"${localization.resource.enabled:true}\")\n" //
						+ "	private boolean enabled;\n" //
						+ "\n" //
						+ "	@Value(\"${localization.resource.file.name.prefix:test-app}\")\n" //
						+ "	private String fileNamePrefix;\n" //
						+ "\n" //
						+ "	@Value(\"${localization.resource.file.name.de:}\")\n" //
						+ "	private String resourceFileNameDE;\n" //
						+ "\n" //
						+ "	public String getResourceFileName(LocalizationSO localization) {\n" //
						+ "		switch (localization) {\n" //
						+ "		case DE:\n" //
						+ "			return resourceFileNameDE;\n" //
						+ "		default:\n" //
						+ "			return \"\";\n" //
						+ "		}\n" //
						+ "	}\n" //
						+ "\n" //
						+ "}";
		return expected;
	}

	@Test
	void happyRunForASimpleObject_NoComment() {
		// Prepare
		String expected = createExpected(true);
		DataModel dataModel = readDataModel("Model.xml");
		dataModel.setApplicationName("Test APP");
		dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

}