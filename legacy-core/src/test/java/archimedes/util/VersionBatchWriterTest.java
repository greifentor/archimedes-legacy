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
					"EXPORT ARCHIMEDES_VERSION=" + newVersion + "\n",
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
					FileUtil.readTextFromFile(absolutePath + "\\set-version.bat"));
		}

	}

}
