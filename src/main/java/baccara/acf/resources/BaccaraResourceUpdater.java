/*
 * BaccaraResourceUpdater.java
 *
 * 06.07.2016
 *
 * (c) by HealthCarion
 *
 */

package baccara.acf.resources;

import java.io.File;
import java.io.IOException;

import archimedes.acf.AbstractResourceUpdater;
import archimedes.legacy.model.DataModel;
import corentx.dates.PTimestamp;
import corentx.io.FileUtil;
import gengen.IndividualPreferences;
import logging.Logger;

/**
 * An implementation of the resource updater interface for Baccara purposes.
 *
 * @author oliver.lieshoff
 *
 * @changed OLI 06.07.2016 - Added.
 */

public class BaccaraResourceUpdater extends AbstractResourceUpdater {

	private static Logger LOG = Logger.getLogger(BaccaraResourceUpdater.class);

	/**
	 * @changed OLI 08.07.2016 - Added.
	 */
	@Override
	public String getFileName(IndividualPreferences ip, DataModel dm) {
		String fileName = FileUtil.completePath(ip.getBaseCodePath(dm.getName())) + "conf/" + dm.getName().toLowerCase()
				+ "_resource_en.properties";
		if (!new File(fileName).exists()) {
			try {
				FileUtil.writeTextToFile(fileName, false, "# created " + new PTimestamp());
			} catch (IOException ioe) {
				LOG.error("while creating resource file: " + fileName + ", exception: " + ioe);
			}
		}
		return fileName;
	}

}