/*
 * ResourceUpdater.java
 *
 * 06.07.2016
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf;

import archimedes.acf.report.*;
import archimedes.model.*;
import gengen.*;

/**
 * This interface defines methods for a class which should act as updater for the leading
 * resource file.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 06.07.2016 - Added.
 */

public interface ResourceUpdater {

    /**
     * Updates the resource file with the passed required resources if necessary.
     *
     * @param resourceData An array with the data of the required resources.
     * @param ip The individual preferences of the user which starts the generation process.
     * @param dm The data model which the resources should be updated for.
     *
     * @changed OLI 06.07.2016 - Added.
     */
    abstract public void updateResources(ResourceData[] resourceData, IndividualPreferences ip,
            DataModel dm);

}