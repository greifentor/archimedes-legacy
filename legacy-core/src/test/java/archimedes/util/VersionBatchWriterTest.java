package archimedes.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import corentx.io.FileUtil;

public class VersionBatchWriterTest {

	@Nested
	class TestsForLinuxEnvironment {

		@Test
		void createsAVersionBatchFileInThePassedFolder(@TempDir Path tempDir) {
			// Prepare
			String newVersion = "1.42.7";
			String absolutePath = tempDir.toAbsolutePath().toString();
			// Run
			VersionBatchWriter.main(new String[] { absolutePath, newVersion, "LINUX" });
			// Check
			assertTrue(new File(absolutePath + "/set-version.sh").exists());
		}

		@Test
		void createdFileHasTheCorrectContent(@TempDir Path tempDir) throws Exception {
			// Prepare
			String newVersion = "1.42.7";
			String absolutePath = tempDir.toAbsolutePath().toString();
			// Run
			VersionBatchWriter.main(new String[] { absolutePath, newVersion, "LINUX" });
			// Check
			assertEquals(
					"export ARCHIMEDES_VERSION=" + newVersion + "\n",
					FileUtil.readTextFromFile(absolutePath + "/set-version.sh").replace("\r", ""));
		}

	}

	@Nested
	class TestsForWindowsEnvironment {

		@Test
		void createsAVersionBatchFileInThePassedFolder(@TempDir Path tempDir) {
			// Prepare
			String newVersion = "1.42.7";
			String absolutePath = tempDir.toAbsolutePath().toString();
			// Run
			VersionBatchWriter.main(new String[] { absolutePath, newVersion, "WINDOWS" });
			// Check
			assertTrue(new File(absolutePath + "\\set-version.bat").exists());
		}

		@Test
		void createdFileHasTheCorrectContent(@TempDir Path tempDir) throws Exception {
			// Prepare
			String newVersion = "1.42.7";
			String absolutePath = tempDir.toAbsolutePath().toString();
			// Run
			VersionBatchWriter.main(new String[] { absolutePath, newVersion, "WINDOWS" });
			// Check
			assertEquals(
					"SET ARCHIMEDES_VERSION=" + newVersion + "\r\n",
					readTextFromFileForWindows(absolutePath + "\\set-version.bat"));
		}

		private String readTextFromFileForWindows(String file) throws Exception {
			String s = FileUtil.readTextFromFile(file);
			return ! s.endsWith("\r\n") ? s.replace("\n", "\r\n") : s;
		}

	}

	@Nested
	class TestOfErrors {

		@Test
		void passedToLessArguments_DoesCreateAnyFile(@TempDir Path tempDir) throws Exception {
			// Prepare
			String absolutePath = tempDir.toAbsolutePath().toString();
			// Run
			VersionBatchWriter.main(new String[] {});
			// Check
			assertTrue(new File(absolutePath).listFiles().length == 0);
		}

		@Test
		void passedToManyArguments_DoesCreateAnyFile(@TempDir Path tempDir) throws Exception {
			// Prepare
			String absolutePath = tempDir.toAbsolutePath().toString();
			// Run
			VersionBatchWriter.main(new String[] {"1", "2", "3", "4"});
			// Check
			assertTrue(new File(absolutePath).listFiles().length == 0);
		}

		@Test
		void passedAnUnknownOSName_DoesCreateAnyFile(@TempDir Path tempDir) throws Exception {
			// Prepare
			String absolutePath = tempDir.toAbsolutePath().toString();
			// Run
			VersionBatchWriter.main(new String[] {"1", "2", "NoOS"});
			// Check
			assertTrue(new File(absolutePath).listFiles().length == 0);
		}

	}

}