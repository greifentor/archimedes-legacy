/*
 * InifileManager.java
 *
 * 25.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.files;


import static corentx.util.Checks.*;

import java.io.*;

import logging.Logger;

import corent.files.*;


/**
 * A class which helps managing ini files.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.01.2013 - Added.
 */

public class InifileManager {

    static Logger log = Logger.getLogger(InifileManager.class);

    /**
     * Opens or creates (if not existing) an ini file with the passed name.
     *
     * <P>If something goes wrong while loading the file, an error entry is written to the log. 
     *
     * @param filename The name of the ini file.
     * @return The opened ini file.
     * @throws IllegalArgumentException Passing an empty or null string.
     *
     * @changed OLI 25.01.2013 - Added.
     */
    public Inifile open(String filename) throws IllegalArgumentException {
        ensure(filename != null, "filename cannot be null.");
        ensure(!filename.isEmpty(), "filename cannot be empty.");
        Inifile ini = new Inifile(filename);
        if (new File(filename).exists()) {
            try {
                ini.load();
            } catch (Exception e) {
                log.error("error while reading inifile: " + filename + ", error: " + e);
            }
        }
        return ini;
    }

}