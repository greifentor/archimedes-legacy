/*
 * AbstractImageProvider.java
 *
 * 09.09.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;


import static corentx.util.Checks.*;

import java.io.*;
import java.util.*;

import javax.swing.*;

import logging.Logger;


/**
 * An abstract implementation of the image provider interface. 
 *
 * @author O.Lieshoff
 *
 * @changed OLI 09.09.2013 - Added.
 */

abstract public class AbstractImageProvider implements ImageProvider {

    static Logger log = Logger.getLogger(FileImageProvider.class);

    protected Map<String, ImageIcon> images = new Hashtable<String, ImageIcon>();

    /**
     * Creates a new image provider with the passed image files.
     *
     * @param assignments A map with keys and file names of the images which should be provided
     *         by the object.
     * @param fillAutomatically Set this flag if the fill method should be called from the
     *         constructor.
     * @throws IllegalArgumentException In case of passing a null pointer.
     *
     * @changed OLI 09.09.2013 - Added.
     */
    public AbstractImageProvider(Map<String, String> keysAndFileNames, boolean fillAutomatically
            ) throws IllegalArgumentException {
        super();
        if (fillAutomatically) {
            this.fill(keysAndFileNames);
        }
    }

    /**
     * Fills the image map of the provider.
     *
     * @changed OLI 09.09.2013 - Added.
     */
    protected void fill(Map<String, String> keysAndFileNames) {
        ensure(keysAndFileNames != null, "map with keys and file names cannot be null.");
        for (String key : keysAndFileNames.keySet()) {
            String fn = keysAndFileNames.get(key);
            try {
                this.images.put(key, this.read(fn));
            } catch (FileNotFoundException e) {
                log.error("file for key not found. Key: " + key + ", FileName: " + fn);
            } catch (IOException e) {
                log.error("file for key could no be loaded. Key: " + key + ", FileName: " + fn);
            }
        }
    }

    /**
     * Reads a single image with the passed name.
     *
     * @param name The file or resource name of the image which is to read.
     * @return The image for the passed name.
     * @throws FileNotFoundException In case of passing a name of an unexisting resource or
     *         file.
     * @throws IOException If an error occurs while reading the image.
     *
     * @changed OLI 09.09.2013 - Added.
     */
    abstract protected ImageIcon read(String name) throws FileNotFoundException, IOException;

    /**
     * @changed OLI 09.09.2013 - Added.
     */
    @Override public ImageIcon getImageIcon(String key) throws IllegalArgumentException {
        return this.images.get(key);
    }

}