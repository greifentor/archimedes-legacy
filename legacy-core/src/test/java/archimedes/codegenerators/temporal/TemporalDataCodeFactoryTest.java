package archimedes.codegenerators.temporal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import archimedes.codegenerators.AbstractClassCodeFactory;
import archimedes.codegenerators.AbstractClassCodeGenerator;
import archimedes.legacy.scheme.ArchimedesObjectFactory;
import archimedes.model.DataModel;
import archimedes.model.MessageCollector;
import archimedes.scheme.Option;
import archimedes.scheme.xml.ModelXMLReader;

@ExtendWith(MockitoExtension.class)
public class TemporalDataCodeFactoryTest {

	private static final String APPLICATION_NAME = "App";

	@InjectMocks
	private TemporalDataCodeFactory unitUnderTest;

	static DataModel readDataModel(String fileName) {
		ModelXMLReader reader = new ModelXMLReader(new ArchimedesObjectFactory());
		DataModel dataModel = reader.read("src/test/resources/dm/codegenerators/Model.xml");
		dataModel
				.getTableByName("A_TABLE")
				.addOption(new Option(AbstractClassCodeFactory.GENERATE_ONLY_FOR, TemporalDataCodeFactory.TEMPORAL));
		dataModel
				.getTableByName("A_TABLE")
				.getColumnByName("Description")
				.addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
		dataModel
				.getTableByName("A_TABLE")
				.getColumnByName("ADate")
				.addOption(new Option(TemporalDataCodeFactory.TEMPORAL));
		return dataModel;
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
			assertServiceFilesExisting("", tempDir);
		}

		public void assertServiceFilesExisting(String prefix, Path tempDir) {
			assertTrue(
					new File(
							tempDir.toAbsolutePath().toString() + prefix
									+ "/src/main/java/base/pack/age/name/service/impl/ATableServiceImplGenerated.java")
											.exists());
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
			assertServiceFilesExisting("/app-service", tempDir);
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
			assertServiceFilesExisting("/service", tempDir);
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
			assertServiceFilesExisting("/alt-service", tempDir);
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
			assertServiceFilesExisting("/service", tempDir);
		}

		@Test
		void doesNotOverrideExistingFileWithNoGeneratedCodeComment(@TempDir Path tempDir) throws Exception {
			// Prepare
			String content = "bla";
			DataModel model = readDataModel("Model.xml");
			unitUnderTest.setDataModel(model);
			String fileName = tempDir.toAbsolutePath().toString()
					+ "/src/main/java/base/pack/age/name/service/impl/ATableServiceImplGenerated.java";
			System.out.println(fileName);
			new File(tempDir.toAbsolutePath().toString() + "/src/main/java/base/pack/age/name/service/impl").mkdirs();
			FileWriter writer = new FileWriter(fileName);
			writer.write(content);
			writer.close();
			unitUnderTest.setMessageCollector(new MessageCollector());
			// Run
			unitUnderTest.generate(tempDir.toAbsolutePath().toString());
			// Check
			assertEquals(content, Files.readString(Path.of(fileName)));
		}

	}

}