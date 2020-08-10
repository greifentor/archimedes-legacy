/*
 * AbstractResourceUpdater.java
 *
 * 08.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.util.Formatter;
import java.util.Properties;

import archimedes.acf.report.ResourceData;
import archimedes.legacy.model.DataModel;
import corentx.dates.PTimestamp;
import corentx.io.FileUtil;
import gengen.IndividualPreferences;
import logging.Logger;

/**
 * A basic implementation for the <CODE>ResourceUpdater</CODE> interface.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 08.07.2016 - Added.
 */

abstract public class AbstractResourceUpdater implements ResourceUpdater {

	private static final Logger LOG = Logger.getLogger(AbstractResourceUpdater.class);

	/**
	 * Returns the file name which is used for the resource file.
	 *
	 * @param ip The individual preferences of the user which starts the generation process.
	 * @param dm The data model which the resources should be updated for.
	 * @return The file name which is used for the resource file.
	 *
	 * @changed OLI 08.07.2016 - Added.
	 */
	abstract public String getFileName(IndividualPreferences ip, DataModel dm);

	/**
	 * @changed OLI 06.07.2016 - Added.
	 */
	@Override
	public void updateResources(ResourceData[] rids, IndividualPreferences ip, DataModel dm) {
		if (rids.length == 0) {
			return;
		}
		String fileName = this.getFileName(ip, dm);
		try {
			String additions = "";
			String resourceFile = FileUtil.readTextFromFile(fileName);
			Properties p = new Properties();
			p.load(new ByteArrayInputStream(resourceFile.getBytes()));
			for (ResourceData rid : rids) {
				if (p.get(rid.getResourceId()) == null) {
					if (additions.length() == 0) {
						additions += "\n\n\n";
						additions += "# Added " + ip.getUserToken() + " " + new PTimestamp() + ".\n";
					}
					additions += "\n" + rid.getResourceId() + "=" + this.escapeUnicode(rid.getDefaultValue());
				}

			}
			FileUtil.writeTextToFile(fileName, true, additions);
			LOG.info("Added to resource file: " + fileName + "\n" + additions);
		} catch (Exception e) {
			LOG.error("error while reading resource file: " + new File(fileName).getAbsolutePath() + ", error: "
					+ e.getMessage());
		}
	}

	private String escapeUnicode(String s) {
		StringBuilder b = new StringBuilder(s.length());
		Formatter f = new Formatter(b);
		for (char c : s.toCharArray()) {
			if ((c > 31) && (c < 128)) {
				b.append(c);
			} else {
				f.format("\\u%04x", (int) c);
			}
		}
		f.close();
		return b.toString();
	}

}