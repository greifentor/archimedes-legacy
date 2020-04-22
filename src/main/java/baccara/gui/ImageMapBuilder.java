/*
 * ImageMapBuilder.java
 *
 * 24.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.gui;

import static corentx.util.Checks.*;

import java.io.*;
import java.util.*;

import org.apache.log4j.*;


/**
 * A builder to create a map of file names and images as a base for the
 * <CODE>FileImageProvider</CODE>.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.01.2013 - Added.
 */

public class ImageMapBuilder {

    private static final Logger LOG = Logger.getLogger(ImageMapBuilder.class);

    private String path = null;

    /**
     * Creates a new builder for the passed path.
     *
     * @param path The path which is to be searched for image files.
     * @throws IllegalArgumentException Passing an empty or null string.
     *
     * @changed OLI 24.01.2013 - Added.
     */
    public ImageMapBuilder(String path) throws IllegalArgumentException {
        super();
        ensure(path != null, "path cannot be null.");
        ensure(!path.isEmpty(), "path cannot be empty.");
        this.path = path;
    }

    /**
     * Returns a map with file names as key (without path and extension) and the absolute file
     * names.
     *
     * @return The map with file names as key (without path and extension) and the absolute file
     *         names.
     *
     * @changed OLI 24.01.2013 - Added.
     */
    public Map<String, String> build() {
        Map<String, String> m = new Hashtable<String, String>();
        if (new File(this.path).exists()) {
            for (File f : new File(this.path).listFiles()) {
                if (!f.isDirectory()) {
                    String fn = f.getAbsolutePath().replace("\\", "/");
                    LOG.debug("adding file to map image builder: " + fn);
                    if ((fn.lastIndexOf("/") >= 0) && (fn.lastIndexOf(".") >= 0)) {
                        String key = fn.substring(fn.lastIndexOf("/")+1, fn.lastIndexOf("."));
                        m.put(key, fn);
                    }
                }
            }
        }
        return m;
    }

}