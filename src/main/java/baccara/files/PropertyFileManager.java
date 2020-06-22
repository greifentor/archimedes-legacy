/*
 * PropertyFileManager.java
 *
 * 25.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.files;


import static corentx.util.Checks.*;

import corentx.io.*;

import java.io.*;
import java.util.*;

import logging.Logger;


/**
 * A class which manages property files.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 25.01.2013 - Added.
 */

public class PropertyFileManager {

    static Logger log = Logger.getLogger(InifileManager.class);

    /**
     * Returns the properties which are stored in the file with the passed file name.
     *
     * @param filename The name of the property file whose content should be returned.
     * @return The properties stored in the file with the passed file. If an error occurs while
     *         reading the file, a <CODE>null</CODE> pointer will be returned.
     * @throws IllegalArgumentException Passing an empty or null string.
     *
     * @changed OLI 25.01.2013 - Added.
     */
    public Properties open(String filename) throws IllegalArgumentException {
        ensure(filename != null, "filename cannot be null.");
        ensure(!filename.isEmpty(), "filename cannot be empty.");
        try {
            Properties p = new Properties();
            FileUtil.readProperties(p, filename);
            return p;
        } catch (IOException ioe) {
            String msg = "error while reading resource file: " + ioe;
            if (ioe instanceof FileNotFoundException) {
                msg = "resource file not found: " + filename;
            }
            log.error(msg);
        }
        return null;
    }

}