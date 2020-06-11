/*
 * FileImageProvider.java
 *
 * 24.01.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;


import static corentx.util.Checks.*;

import java.io.*;
import java.util.*;

import javax.swing.*;


/**
 * A file system based implementation of the image provider interface.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.01.2013 - Added.
 */

public class FileImageProvider extends AbstractImageProvider {

    /**
     * Creates a new file based image provider with the passed image files.
     *
     * @param assignments A map with keys and file names of the images which should be provided
     *         by the object.
     * @throws IllegalArgumentException In case of passing a null pointer.
     *
     * @changed OLI 24.01.2013 - Added.
     */
    public FileImageProvider(Map<String, String> keysAndFileNames)
            throws IllegalArgumentException {
        super(keysAndFileNames, true);
    }

    /**
     * @changed OLI 09.09.2013 - Added.
     */
    @Override protected ImageIcon read(String fn) throws FileNotFoundException, IOException {
        ensure(new File(fn).exists(), new FileNotFoundException("image file not found: "
                + fn));
        ImageIcon img = new ImageIcon(fn);
        ensure((img.getIconHeight() > 0) && (img.getIconWidth() > 0), new IOException(
                "error occurs while reading file: " + fn));
        return img;
    }

}