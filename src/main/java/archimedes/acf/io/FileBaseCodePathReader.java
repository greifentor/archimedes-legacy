/*
 * BaseCodePathReader.java
 *
 * 18.04.2013
 *
 * (c) by O.Lieshoff
 *
 */

package archimedes.acf.io;


import corentx.io.*;

import gengen.*;

import java.io.*;
import java.util.*;


/**
 * Reads the path from a properties file if this is existing.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 18.04.2013 - Added.
 * @changed OLI 16.09.2015 - Moved to "Archimedes".
 */

public class FileBaseCodePathReader implements IndividualPreferencesReader {

    private String propertyFilePath = null;

    /**
     * Creates a new property file based base code path reader with the passed parameters.
     *
     * @param propertyFilePath The name of the property file to read.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    public FileBaseCodePathReader(String propertyFilePath) {
        super();
        this.propertyFilePath = propertyFilePath;
    }

    /**
     * Returns the base code path from the property file with the passed name or a default path
     * if there is no property for this in the file.
     *
     * @param defaultBasePath The default value for the path if there is no entry in the file.
     * @throws IOException If an error occurs while reading the property file.
     *
     * @changed OLI 18.04.2013 - Added.
     */
    @Override public IndividualPreferences read(String defaultBasePath) throws IOException {
        String baseCodePathPropertyName = "base.code.path";
        Properties p = new Properties();
        FileUtil.readProperties(p, this.propertyFilePath);
        IndividualPreferences ip = new IndividualPreferences(p.getProperty(
                baseCodePathPropertyName, defaultBasePath), p.getProperty("company.name",
                "COMPANYNAME"), p.getProperty("user.name", "USER NAME"), p.getProperty(
                "user.token", "USR"));
        for (String pn : p.keySet().toArray(new String[0])) {
            if (!pn.equals(baseCodePathPropertyName) && pn.startsWith(baseCodePathPropertyName))
                    {
                String projectToken = pn.replace(baseCodePathPropertyName + ".", ""
                        ).toUpperCase();
                ip.addAlternateBaseCodePath(projectToken, p.getProperty(pn));
            }
        }
        return ip;
    }

}