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
public class FileBasedResourceManagerImplClassCodeGeneratorTest {

	private static final String BASE_PACKAGE_NAME = "base.pack.age.name";

	@Spy
	private LocalizationNameGenerator nameGenerator = new LocalizationNameGenerator();

	@InjectMocks
	private FileBasedResourceManagerImplClassCodeGenerator unitUnderTest;

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
		String expected =
				"package " + BASE_PACKAGE_NAME + ".core.service.impl.localization;\n" //
						+ "\n" //
						+ "import java.io.File;\n" //
						+ "import java.io.FileReader;\n" //
						+ "import java.io.IOException;\n" //
						+ "import java.util.HashMap;\n" //
						+ "import java.util.Map;\n" //
						+ "import java.util.Properties;\n" //
						+ "\n" //
						+ "import javax.annotation.PostConstruct;\n" //
						+ "import javax.inject.Named;\n" //
						+ "\n" //
						+ "import org.apache.logging.log4j.LogManager;\n" //
						+ "import org.apache.logging.log4j.Logger;\n" //
						+ "\n" //
						+ "import " + BASE_PACKAGE_NAME + ".core.model.localization.LocalizationSO;\n" //
						+ "import " + BASE_PACKAGE_NAME + ".core.service.localization.ResourceManager;\n" //
						+ "import lombok.RequiredArgsConstructor;\n" //
						+ "\n";
		if (!suppressComment) {
			expected += "/**\n" + //
					" * An implementation for a file based resource manager.\n" + //
					" *\n" + //
					" * GENERATED CODE !!! DO NOT CHANGE !!!\n" + //
					" */\n";
		}
		expected +=
				"@Named\n" //
						+ "@RequiredArgsConstructor\n" //
						+ "public class FileBasedResourceManagerImpl implements ResourceManager {\n" //
						+ "\n" //
						+ "	private static final Logger logger = LogManager.getLogger(FileBasedResourceManagerImpl.class);\n" //
						+ "\n" //
						+ "	private Map<LocalizationSO, Properties> resources = new HashMap<>();\n" //
						+ "\n" //
						+ "	private final FileBasedResourceManagerConfiguration configuration;\n" //
						+ "\n" //
						+ "	@PostConstruct\n" //
						+ "	private void postConstruct() {\n" //
						+ "		logger.info(\"reading resources ...\");\n" //
						+ "		for (LocalizationSO localization : LocalizationSO.values()) {\n" //
						+ "			String fileName = configuration.getResourceFileName(localization).isEmpty()\n" //
						+ "					? \"classpath:localization/\" + configuration.getFileNamePrefix() + \"_resources_\"\n" //
						+ "							+ localization.name().toLowerCase() + \".properties\"\n" //
						+ "					: configuration.getResourceFileName(localization);\n" //
						+ "			Properties properties = new Properties();\n" //
						+ "			logger.info(\"reading resources from: \" + fileName);\n" //
						+ "			if (fileName.startsWith(\"classpath:\")) {\n" //
						+ "				try {\n" //
						+ "					properties\n" //
						+ "							.load(getClass().getClassLoader().getResourceAsStream(fileName.replace(\"classpath:\", \"\")));\n" //
						+ "				} catch (IOException ioe) {\n" //
						+ "					throw new IllegalStateException(\n" //
						+ "							\"Resource not found '\" + fileName + \"' for localization: \" + localization);\n" //
						+ "				} catch (Exception e) {\n" //
						+ "					throw new IllegalStateException(\n" //
						+ "							\"Something went wrong while reading '\" + fileName + \"' for localization: \" + localization);\n" //
						+ "				}\n" //
						+ "			} else {\n" //
						+ "				if (!new File(fileName).exists()) {\n" //
						+ "					throw new IllegalStateException(\n" //
						+ "							\"Resource file not found '\" + fileName + \"' for localization: \" + localization);\n" //
						+ "				}\n" //
						+ "				try {\n" //
						+ "					properties.load(new FileReader(fileName));\n" //
						+ "				} catch (IOException ioe) {\n" //
						+ "					throw new IllegalStateException(\n" //
						+ "							\"Resource file not readable '\" + fileName + \"' for localization: \" + localization);\n" //
						+ "				}\n" //
						+ "			}\n" //
						+ "			resources.put(localization, properties);\n" //
						+ "		}\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	@Override\n" //
						+ "	public String getLocalizedString(String resourceId) {\n" //
						+ "		return getLocalizedString(resourceId, LocalizationSO.DE);\n" //
						+ "	}\n" //
						+ "\n" //
						+ "	@Override\n" //
						+ "	public String getLocalizedString(String resourceId, LocalizationSO localization) {\n" //
						+ "		Properties properties = resources.get(localization);\n" //
						+ "		if (properties == null) {\n" //
						+ "			return resourceId;\n" //
						+ "		}\n" //
						+ "		return properties.getProperty(resourceId, resourceId);\n" //
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
		dataModel.addOption(new Option(AbstractClassCodeGenerator.COMMENTS, "Off"));
		// Run
		String returned = unitUnderTest.generate(BASE_PACKAGE_NAME, dataModel, dataModel.getTableByName("A_TABLE"));
		// Check
		assertEquals(expected, returned);
	}

}