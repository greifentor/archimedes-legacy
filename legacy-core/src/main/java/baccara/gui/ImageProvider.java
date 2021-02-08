/*
 * ImageProvider.java
 *
 * 24.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.gui;


import javax.swing.*;


/**
 * Implementations of this interface provide images identified by a alphanumeric key.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 24.01.2013 - Added.
 */

public interface ImageProvider {

    /**
     * Returns the image for the passed key.
     *
     * @param key The key whose image should be returns.
     * @return The image assigned to the key or <CODE>null</CODE> if there is no image for
     *         assigned to the passed key.
     * @throws IllegalArgumentException Passing an empty or null string.
     *
     * @changed OLI 24.01.2013 - Added.
     */
    abstract public ImageIcon getImageIcon(String key) throws IllegalArgumentException;

}