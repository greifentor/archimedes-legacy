package archimedes.codegenerators.persistence.jpa;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.MessageCollector;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
class PersistenceJPACodeFactoryTest {

	private static final String APPLICATION_NAME = "App";

	@InjectMocks
	private PersistenceJPACodeFactory unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		return reader.read("src/test/resources/dm/codegenerators/" + fileName);
	}

	@BeforeEach
	void setUp() {
		unitUnderTest.setMessageCollector(new MessageCollector());
	}

	@Nested
	class TestsOfMethod_generate_String {

		@Test
		void happyRun(@TempDir Path tempDir) {
			// Prepare
			DataModel model = readDataModel("Model.xml");
			unitUnderTest.setDataModel(model);
			// Run
			unitUnderTest.generate(tempDir.toAbsolutePath().toString());
			// Check
			assertEntityFilesExisting("", tempDir);
		}

		public void assertEntityFilesExisting(String prefix, Path tempDir) {
			String expectedFileName = tempDir.toAbsolutePath().toString() + prefix
					+ "/src/main/java/base/pack/age/name/persistence/entity/ATableDBO.java";
			assertTrue(new File(expectedFileName).exists(), "file does not exists: " + expectedFileName);
		}

		@Test
		void happyRunInModuleMode(@TempDir Path tempDir) {
			// Prepare
			DataModel model = readDataModel("Model.xml");
			model.setApplicationName(APPLICATION_NAME);
			model.addOption(new Option(AbstractClassCodeGenerator.MODULE_MODE));
			unitUnderTest.setDataModel(model);
			// Run
			unitUnderTest.generate(tempDir.toAbsolutePath().toString());
			// Check
			assertEntityFilesExisting("/app-service", tempDir);
		}

		@Test
		void happyRunInModuleModeNoAppName(@TempDir Path tempDir) {
			// Prepare
			DataModel model = readDataModel("Model.xml");
			model.addOption(new Option(AbstractClassCodeGenerator.MODULE_MODE));
			unitUnderTest.setDataModel(model);
			// Run
			unitUnderTest.generate(tempDir.toAbsolutePath().toString());
			// Check
			assertEntityFilesExisting("/service", tempDir);
		}

		@Test
		void happyRunInModuleModeAlternateModulePrefix(@TempDir Path tempDir) {
			// Prepare
			DataModel model = readDataModel("Model.xml");
			model.setApplicationName(APPLICATION_NAME);
			model.addOption(new Option(AbstractClassCodeGenerator.MODULE_MODE));
			model.addOption(new Option(AbstractClassCodeGenerator.ALTERNATE_MODULE_PREFIX, "alt"));
			unitUnderTest.setDataModel(model);
			// Run
			unitUnderTest.generate(tempDir.toAbsolutePath().toString());
			// Check
			assertEntityFilesExisting("/alt-service", tempDir);
		}

		@Test
		void happyRunInModuleModeEmptyAlternateModulePrefixButAppName(@TempDir Path tempDir) {
			// Prepare
			DataModel model = readDataModel("Model.xml");
			model.setApplicationName(APPLICATION_NAME);
			model.addOption(new Option(AbstractClassCodeGenerator.MODULE_MODE));
			model.addOption(new Option(AbstractClassCodeGenerator.ALTERNATE_MODULE_PREFIX, ""));
			unitUnderTest.setDataModel(model);
			// Run
			unitUnderTest.generate(tempDir.toAbsolutePath().toString());
			// Check
			assertEntityFilesExisting("/service", tempDir);
		}

	}

}