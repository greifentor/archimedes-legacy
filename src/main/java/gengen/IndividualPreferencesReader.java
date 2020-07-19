/*
 * IndividualPreferencesReader
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package gengen;


/**
 * Reads some individual preferences.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 */

public interface IndividualPreferencesReader {

    /**
     * Returns the individual preferences which are provided by the reader.
     *
     * @param defaultBasePath The default value for the path if the reader cannot find any
     *         entry for the base code path.
     * @return The individual preferences which are provided by the reader.
     * @throws Exception If an error occurs while reading the base code path.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    abstract public IndividualPreferences read(String defaultBasePath) throws Exception;

}