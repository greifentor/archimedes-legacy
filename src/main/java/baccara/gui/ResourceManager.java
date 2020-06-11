/*
 * ResourceManager.java
 *
 * 17.01.2013
 *
 * (c) by HealhCarion
 *
 */

package baccara.gui;


/**
 * An interface which covers classes which provides resources.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.01.2013 - Added.
 * @changed OLI 16.07.2013 - Added the method <CODE>getResourceKeys()</CODE>.
 */

public interface ResourceManager {

    /**
     * Returns the resource for the passed key.
     *
     * @param key The key for the resource.
     * @return A string with the resource or the key if there is no resource found for the key.
     *
     * @changed OLI 17.01.2013 - Added.
     */
    abstract public String getResource(String key);

    /**
     * Returns an array with the keys of all resources provided by the manager. 
     *
     * @return An array with the keys of all resources provided by the manager.
     *
     * @changed OLI 16.07.2013 - Added.
     */
    abstract public String[] getResourceKeys();

    /**
     * Checks if the resource key is present in the resource.
     *
     * @param key The key for the resource to check.
     * @return <CODE>true</CODE> if the resource key is present in the resources.
     *
     * @changed OLI 20.05.2016 - Added.
     */
    abstract public boolean hasResource(String key);

}