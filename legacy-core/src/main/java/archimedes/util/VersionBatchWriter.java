package archimedes.util;

import java.io.IOException;

import corentx.io.FileUtil;

public class VersionBatchWriter {

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Wrong number of arguments!\n");
			System.out.println("- (0) folder name.");
			System.out.println("- (1) new version.");
			System.out.println("- (2) operating system name (valid: \"LINUX\", \"WINDOWS\").\n");
		} else {
			String folder = args[0];
			String newVersion = args[1];
			String osName = args[2];
			String completeFileName = "";
			String content = "";
			if (osName.equalsIgnoreCase("LINUX")) {
				completeFileName = folder + "/set-version.sh";
				content = "export ARCHIMEDES_VERSION=" + newVersion + "\n";
			} else if (osName.equalsIgnoreCase("WINDOWS")) {
				completeFileName = folder + "\\set-version.bat";
				content = "SET ARCHIMEDES_VERSION=" + newVersion + "\r\n";
			} else {
				System.out.println("Illegal OS name '" + osName + "'! Valid is: 'LINUX' or 'WINDOWS'.");
				return;
			}
			try {
				FileUtil.writeTextToFile(completeFileName, false, content);
			} catch (IOException ioe) {
				System.out.println("Error while writing 'set-version' file: " + ioe.getMessage());
			}
		}
	}

}