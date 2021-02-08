/*
 * PropertyResourceManager.java
 *
 * 17.01.2013
 *
 * (c) by O.Lieshoff
 *
 */

package baccara.gui;


import static corentx.util.Checks.*;

import java.util.*;


/**
 * This resource manager implementation reads the resources from a property bundle.
 *
 * @author O.Lieshoff
 *
 * @changed OLI 17.01.2013 - Added.
 */

public class PropertyResourceManager implements ResourceManager {

    private Properties resources = null;

    /**
     * Creates a new property resource manager with the passed property bundle.
     *
     * @param resource The property bundle with the resources.
     * @throws IllegalArgumentException In case of passing a null pointer.
     *
     * @changed OLI 17.01.2013 - Added.
     */
    public PropertyResourceManager(Properties resources) throws IllegalArgumentException {
        super();
        this.setResources(resources);
    }

    /**
     * Adds the passed property bundle to the resources.
     *
     * @param resource The property bundle to add to the resources.
     * @throws IllegalArgumentException In case of passing a null pointer.
     *
     * @changed OLI 01.04.2016 - Added.
     */
    public void addResources(Properties resources) {
        ensure(resources != null, "resources cannot be null.");
        for (Object key : resources.keySet()) {
            String k = String.valueOf(key);
            this.resources.setProperty(k, resources.getProperty(k));
        }
    }

    /**
     * @changed OLI 17.01.2013 - Added.
     */
    @Override public String getResource(String key) {
        return this.resources.getProperty(key, key);
    }

    /**
     * @changed OLI 16.07.2013 - Added.
     */
    @Override public String[] getResourceKeys() {
        return this.resources.keySet().toArray(new String[0]);
    }

    /**
     * @changed OLI 20.05.2016 - Added.
     */
    @Override public boolean hasResource(String key) {
        for (Object k : this.resources.keySet()) {
            if (k.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Sets the passed property bundle a new resources.
     *
     * @param resource The property bundle with the resources.
     * @throws IllegalArgumentException In case of passing a null pointer.
     *
     * @changed OLI 17.01.2013 - Added.
     */
    public void setResources(Properties resources) {
        ensure(resources != null, "resources cannot be null.");
        this.resources = resources;
    }

}